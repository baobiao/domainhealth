//Copyright (C) 2008-2010 Paul Done . All rights reserved.
//This file is part of the DomainHealth software distribution. Refer to the 
//file LICENSE in the root of the DomainHealth distribution.
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
//AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
//ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE 
//LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
//CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
//SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
//INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
//CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
//ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
//POSSIBILITY OF SUCH DAMAGE.
package domainhealth.backend.retriever;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkManager;

import static domainhealth.core.env.AppProperties.*;
import domainhealth.backend.jmxpoll.StatisticCapturerJMXPoll;
import domainhealth.backend.wldfcapture.HarvesterWLDFModuleCreator;
import domainhealth.backend.wldfcapture.StatisticCapturerWLDFQuery;
import domainhealth.core.env.AppLog;
import domainhealth.core.env.AppProperties;
import domainhealth.core.env.ContextAwareWork;
import domainhealth.core.jmx.DomainServiceMBeanConnection;
import domainhealth.core.jmx.WebLogicMBeanException;
import static domainhealth.core.jmx.WebLogicMBeanPropConstants.*;
import domainhealth.core.statistics.FileSystemStore;
import domainhealth.core.util.FileUtil;


/**
 * Statistics Retrieval Background Service which once a minute, initiates the 
 * process to collect statistics from every server in the domain, placing the
 * results into a set of CSV files.
 * 
 * The is either achieved by using the WebLogic WorkManager API to schedule a 
 * new work item job for the WebLogic thread pool or just by using a 
 * continuously looping background Java thread (now the default behaviour, see 
 * Bug#3077882).
 * 
 * The process for capturing statistics is pluggable (eg. use JMX Polling for 
 * stats or use WLDF harvesting of stats).
 * 
 * In addition to this background service being scheduled every minute by 
 * using the Work Manager API (or just running as background thread), the 
 * work manager API is also used to enable each server in the domain to 
 * be queried for statistics in separate parallel threads.
 * 
 * Note: Because of Bug# 3077882 which was detected in version 0.9 of DH, a 
 * quick fix was added to this class to use a background daemon thread rather 
 * than work manager, To minimise the code impact and get this fix into 0.9.1
 * version, minimal changes were added to this class. However, for a major 
 * release, this class should be REFACTORED because its now a but spaghetti 
 * like including a lot of work mgr code which is not needed.
 */
public class RetrieverBackgroundService {
	/**
	 * Create new service with the root path to write CSV file to
	 *  
	 * @param appProps The system/application key/value pairs
	 */
	public RetrieverBackgroundService(AppProperties appProps) {
		this.versionNumber = appProps.getProperty(PropKey.VERSION_NUMBER_PROP);
		this.alwaysUseJMXPoll = appProps.getBoolProperty(PropKey.ALWAYS_USE_JMXPOLL_PROP);
		this.csvStats = new FileSystemStore(appProps.getProperty(PropKey.STATS_OUTPUT_PATH_PROP));		
		int queryIntervalSecs = appProps.getIntProperty(PropKey.QUERY_INTERVAL_SECS_PROP);

		if (queryIntervalSecs < MINIMUM_SLEEP_SECS) {
			AppLog.getLogger().warning("Specified query interval seconds of '" + queryIntervalSecs + "' is too low - changing to value '" + MINIMUM_SLEEP_SECS + "'");
			queryIntervalSecs = MINIMUM_SLEEP_SECS;
		}

		queryIntervalMillis = queryIntervalSecs * ONE_SECOND_MILLIS;
		minPollIntervalMillis = (int) (MIN_POLL_FACTOR * queryIntervalMillis);
		maxPollIntervalMillis = (int) (MAX_POLL_FACTOR * queryIntervalMillis);		
		componentBlacklist = tokenizeBlacklistText(appProps.getProperty(PropKey.COMPONENT_BLACKLIST_PROP));
		WorkManager localServiceThreadWkMgr = null; 
		WorkManager localCaptureThreadsWkMgr = null;
		
		try {
			localServiceThreadWkMgr = getWorkManager(SERVICE_THREAD_WORK_MGR_JNDI);
			localCaptureThreadsWkMgr = getWorkManager(CAPUTURE_THREADS_WORK_MGR_JNDI);
		} catch (NamingException e) {
			throw new IllegalStateException(getClass() + " cannot be instantiateed because Work Manager '" + CAPUTURE_THREADS_WORK_MGR_JNDI + "' cannot be located. " + e.getMessage());
		}
		
		this.serviceThreadWkMgr = localServiceThreadWkMgr;
		this.captureThreadsWkMgr = localCaptureThreadsWkMgr;
	}
	
	/**
	 * Start the continuously repeating sleep-gather-schedule background 
	 * process.
	 */
	public void startup() {
		try {
			AppLog.getLogger().info("Statistics Retriever Background Service starting up");
			File rootDir = FileUtil.createOrRetrieveDir(csvStats.getRootDirectoryPath());
			AppLog.getLogger().notice("Statistic CSV files location: " + rootDir.getCanonicalPath());
			
			// See Bug# 3077882 description further down for why we've gone back to using daemon thread
			if (USE_BACKGROUND_JAVA_THREAD) {
				Thread backgroundThread = new Thread(new CaptureRunnable(), this.getClass().getName());
				backgroundThread.setDaemon(true);
				backgroundThread.start();
				AppLog.getLogger().debug("Created background Java daemon thread to drive data retrieval process");
			} else {
				scheduleCaptureWork();
				AppLog.getLogger().debug("Scheduled work manager work item to drive data retrieval process");
			}
		} catch (Exception e) {
			AppLog.getLogger().critical("Statistics Retriever Background Service has been disabled. Reason: " + e.toString());
			throw new RuntimeException(e);
		}
	}

	/**
	 * Send signal to the continuously repeating sleep-gather-schedule 
	 * background process should terminate as soon as possible.
	 */
	public void shutdown() {
		keepRunning = false;
		AppLog.getLogger().info("Statistics Retriever Background Service shutting down");
	}

	/**
	 * Using the WebLogic Work Manager, schedule the next work item to used to
	 * perform the next run of the statistics capture process. Also records 
	 * the time that this job was scheduled to keep track of any time lag that
	 * may occur between creating the new work item and it actually being run 
	 * by a thread in the thread pool.
	 * 
	 * @throws IllegalArgumentException Indicates that the job could not be scheduled.
	 * @throws WorkException Indicates that the job could not be scheduled.
	 */
	private void scheduleCaptureWork() throws IllegalArgumentException, WorkException {
		startWorkTime = System.currentTimeMillis();		
		serviceThreadWkMgr.schedule(new CaptureWork());
	}

	/**
	 * Work Manager work item responsible for initiating the statistics capture 
	 * process and then scheduling the next work manager work item on the 
	 * kernel to run the next iteration, and so on.
	 * 
	 * NOTE: Due to Bug# 3077882 the use of work manager has been disabled in 
	 * version 0.9.1 and greater (see member variable 
	 * USE_BACKGROUND_JAVA_THREAD in this class). This is because after about 
	 * 32 hours or running, the work manager scheduling of this task starts
	 * continuously failing, because of the recursive 'wrapping' of java:comp 
	 * JNDI contexts for each new work manager spawned from the previous work 
	 * manager. IE. seems to be a flaw if using work managers in a chained 
	 * manner.  :(
	 */
	private class CaptureWork extends ContextAwareWork {
		public void doRun() {		
			try {
				AppLog.getLogger().debug("About to sleep and then perform processing run via the scheduled work manager work item");
				performProcessingRun();
			} catch (Exception e) {
				AppLog.getLogger().warning("Statistics Retriever Background Service processing iteration (using Work Manaager) has failed abnormally for this run. Reason: " + e.toString());
			}
			
			if (keepRunning) {
				try {
					scheduleCaptureWork();
				} catch (Exception e) {
					AppLog.getLogger().critical("Statistics Retriever Background Service has been disabled because it can't be scheduled using a work manager. Reason: " + e.toString());
				}
			}
		}
	}

	/**
	 * Runnable which will be spawned as a background daemon thread 
	 * responsible for initiating the statistics capture process and
	 * then repeating the cycle over and over again.
	 * 
	 * NOTE: Due to Bug# 3077882 the use of work manager (see code further up)
	 * has been disabled in version 0.9.1 and greater (see member variable 
	 * USE_BACKGROUND_JAVA_THREAD in this class). This is because after about 
	 * 32 hours or running, the work manager scheduling of this task starts
	 * continuously failing, because of the recursive 'wrapping' of java:comp 
	 * JNDI contexts for each new work manager spawned from the previous work 
	 * manager. IE. seems to be a flaw if using work managers in a chained 
	 * manner.  :(
	 */
	private class CaptureRunnable implements Runnable {
		public void run() {
			while (keepRunning) {
				try {
					startWorkTime = System.currentTimeMillis();		
					AppLog.getLogger().debug("About to sleep and then perform processing run via the background Java daemon thread");
					performProcessingRun();
				} catch (Exception e) {
					AppLog.getLogger().warning("Statistics Retriever Background Service processing iteration (using Java Daemon Thread) has failed abnormally for this run. Reason: " + e.toString());
				}
			}			
		}
	}
	
	/**
	 * Main method responsible for running the statistics capture process. The
	 * first thing this does, when run is sleep for 1 minute before waking up 
	 * and initiating the statistics capture process to each server in the 
	 * domain, in parallel.
	 *  
	 * If this is the first time that an attempt has been made to run this 
	 * process since the server was started or this app was deployed, on the 
	 * first run, this method first performs the DomainHealth initialisation 
	 * steps, before running the normal statistic capture work.
	 * 
	 * Note: At every major step in this runnable work item code, the status 
	 * of the 'keepRunning' member variable is checked to be sure that a 
	 * signal has not been received by the background service to instruct it 
	 * to terminate. This allows the code to terminate as soon as possible 
	 * without producing incomplete CSV file entries.
	 */
	private void performProcessingRun() {
		if (keepRunning) {
			try { Thread.sleep(sleepPeriodMillis); } catch (Exception e) {}			
		}

		long newSleepIntervalMillis = queryIntervalMillis;
		
		if ((keepRunning) && (!firstTimeProcessingRanOK)) {
			try {
				runFirstTimeProcessing();
				firstTimeProcessingRanOK = true;
			} catch (Exception e) {					
				AppLog.getLogger().error("Statistics Retriever Background Service - first time processing initialisation failed - will attempt initialisation again after a pause (this can be caused if the server takes a while to start-up, in which case, the retry should sort things out). Cause: "  + e.toString());
				AppLog.getLogger().debug("First time processing failure cause: + " + e.getMessage(), e);
				newSleepIntervalMillis = INITIALISATION_ATTEMPT_AGAIN_SLEEP_DURATION;
			}
		}
			
		if ((keepRunning) && (firstTimeProcessingRanOK)) {
			runNormalProcessing();
		}
			
		if (keepRunning) {
			// Attempt to compensate for the fact that the start time of each will drift 
			// out a bit due to excess processing time consumed during each iteration 
			long lagMillis = Math.max(System.currentTimeMillis() - startWorkTime - sleepPeriodMillis, 0);
			sleepPeriodMillis = Math.max(minPollIntervalMillis, newSleepIntervalMillis - lagMillis);
		}
	}

	/**
	 * First time processing. Runs to set up the monitoring environment. Does 
	 * not run as part of application deployment / start-up because at that 
	 * point certain vital resources such as the servers' JMX trees may not 
	 * yet be available to access.
	 * 
	 * @throws WebLogicMBeanException Indicates a problem in initialising the monitoring environment - indicates that first time processing will have to be run again at a later time
	 */
	private void runFirstTimeProcessing() throws WebLogicMBeanException {
		AppLog.getLogger().debug("Statistics Retriever Background Service running first time processing initialisation steps");
		
		if (!DomainServiceMBeanConnection.isThisTheAdminServer()) {
			AppLog.getLogger().error("Attempt made to run 'DomainHealth' application on a managed server. Attempt halted. Undeploy 'DomainHealth' and re-deploy to run on the domain's Admin Server only");
			shutdown();
			return;
		}

		if (alwaysUseJMXPoll) {
			useWLDFHarvester = false;				
		} else {
			useWLDFHarvester = isDomainVersion103orGreater();
		}
		
		if (useWLDFHarvester) {				
			HarvesterWLDFModuleCreator harvesterModule = new HarvesterWLDFModuleCreator(queryIntervalMillis, versionNumber);
			
			if (harvesterModule.isDomainHealthCapable()) {
				useWLDFHarvester = true;					
				harvesterModule.create();
			} else {
				useWLDFHarvester = false;					
				AppLog.getLogger().warning("WLDF module creation problems occurred. WLDF Capture mode can't be used - must use JMX Poll mode instead");
			}
		} 
		
		if (useWLDFHarvester) {				
			AppLog.getLogger().notice("Server statistics retrieval mode: WLDF Harvested Data Capture");
		} else {
			AppLog.getLogger().notice("Server statistics retrieval mode: JMX MBean Attribute Polling");
		}

		AppLog.getLogger().info("Statistics Retriever Background Service first time processing initialisation steps finished successfully");
	}

	/**
	 * Runs the normal statistic capture process, by iterating through the 
	 * list of currently running servers in the domain, and for each of 
	 * these, schedule the query of the servers stats in a separate work item
	 * to run in parallel in the WebLogic thread pool.
	 */
	private void runNormalProcessing() {
		DomainServiceMBeanConnection conn = null;
		
		try {
			AppLog.getLogger().debug("Statistics Retriever Background Service starting another iteration to capture and log stats");
			conn = new DomainServiceMBeanConnection();
			ObjectName[] serverRuntimes = conn.getAllServerRuntimes();			
			int length = serverRuntimes.length;
			List<WorkItem> pollerWorkItemList = new ArrayList<WorkItem>();
			
			for (int i = 0; i < length; i++) {
				final String serverName = conn.getTextAttr(serverRuntimes[i], NAME);
				final StatisticCapturer capturer = getStatisticCapturer(conn, serverRuntimes[i], serverName);

				pollerWorkItemList.add(captureThreadsWkMgr.schedule(new ContextAwareWork() {
					public void doRun() {
						try {
							capturer.captureAndlogServerStats();
						} catch (Exception e) {
							AppLog.getLogger().error(e.toString());
							e.printStackTrace();
							AppLog.getLogger().error("Statistics Retriever Background Service - unable to retrieve statistics for specific server '" + serverName + "' for this iteration");
						}						
					}					
				}));				
			}			
			
			boolean allCompletedSuccessfully = captureThreadsWkMgr.waitForAll(pollerWorkItemList, maxPollIntervalMillis);
			warnIfTimedOut(allCompletedSuccessfully);
			AppLog.getLogger().info("Statistics Retriever Background Service ending another iteration successfully");
		} catch (Exception e) {
			AppLog.getLogger().error(e.toString());
			e.printStackTrace();
			AppLog.getLogger().error("Statistics Retriever Background Service - unable to retrieve statistics for domain's servers for this iteration");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * Returns the implementation of the Statistics Capturer (eg. JMX Poll, 
	 * WLDF Harvest).
	 * 
	 * @param conn JMX Connection to domain runtime
	 * @param serverRuntime Handle on the specific server runtime to do capturing for
	 * @param serverName The name of the specific server runtime to do capturing for
	 * @return The new instance of the Statistics Capturer implementation
	 */
	private StatisticCapturer getStatisticCapturer(DomainServiceMBeanConnection conn, ObjectName serverRuntime, String serverName) {
		if (useWLDFHarvester) {
			return new StatisticCapturerWLDFQuery(csvStats, conn, serverRuntime, serverName, queryIntervalMillis, componentBlacklist);
		} else {
			return new StatisticCapturerJMXPoll(csvStats, conn, serverRuntime, serverName, queryIntervalMillis, componentBlacklist);
		}
	}
		
	/**
	 * If all Work Manager work items have not completed when timeout occurs, 
	 * log warning
	 * 
	 * @param allCompletedSuccessfully Flag indicating if all Work Items completed successfully  
	 */
	private void warnIfTimedOut(boolean allCompletedSuccessfully) {
		if (!allCompletedSuccessfully) {
			AppLog.getLogger().warning(getClass().getName() + " timed-out when retrieving data from one or more servers in the domain");
		}
	}

	/**
	 * Determine if the current running WebLogic domain is based on a Web
	 * Logic version of 10.3.0 or greater.
	 * 
	 * @return True if 10.3.0 or greater; otherwise false
	 */
	private boolean isDomainVersion103orGreater() {
		boolean is103OrGreater = false;
		DomainServiceMBeanConnection conn = null;
		
		try {
			conn = new DomainServiceMBeanConnection();
			ObjectName domainConfig = conn.getDomainConfiguration();
			String version = conn.getTextAttr(domainConfig, DOMAIN_VERSION);
			is103OrGreater = isWebLogicVersion103OrGreater(version);
		} catch (WebLogicMBeanException e) {
			// Assume caused by "DomainVersion" attribute not existing which
			// would indicate that this is a 9.0 or 9.1 domain version
			is103OrGreater = false;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}	

		return is103OrGreater;
	}

	/**
	 * Utility method to determine if a WebLogic version text string indicates
	 * a WebLogic version of 10.3.0 or greater.
	 *  		
	 * @param versionText The version text string
	 * @return True if 10.3.0 or greater; otherwise false
	 */
	public static boolean isWebLogicVersion103OrGreater(String versionText) {
		boolean result = false;
		Matcher matcher = VERSION_MAJOR_MINOR_EXTRACTOR_PATTERN.matcher(versionText);		
		boolean found = matcher.find();
		
		if ((!found) || (matcher.groupCount() != 2)) {
			AppLog.getLogger().warning("Unable to establish host WebLogic version, assuming 10.0 or less");
		} else {
			int majorVersion = Integer.parseInt(matcher.group(1));
			int minorVersion = Integer.parseInt(matcher.group(2));			

			if ((majorVersion > 10) || ((majorVersion == 10) && (minorVersion >= 3))) {
				result = true;
			}  
		}
		
		return result;
	}
	
	/**
	 * Retrieves a named work manager from the local JNDI tree. 
	 * 
	 * @param wkMgrName The name of the work manager to retrieve
	 * @return The found work manager
	 * @throws NamingException Indicates that the work manager could not be located
	 */
	private WorkManager getWorkManager(String wkMgrName) throws NamingException {
		InitialContext ctx = null;
		
		try {
		    ctx = new InitialContext();
		    return (WorkManager) ctx.lookup(wkMgrName);
		} finally {
		    try { ctx.close(); } catch (Exception e) {}
		}
	}

	/**
	 * Gets list of names of web-app and ejb components which should not have 
	 * statistics collected and shown.
	 * 
	 * @param blacklistText The text containing comma separated list of names to ignore
	 * @return A strongly type list of names to ignore
	 */
	private List<String> tokenizeBlacklistText(String blacklistText) {
		List<String> blacklist = new ArrayList<String>();
		String[] blacklistArray = null;
		
		if (blacklistText != null) {
			blacklistArray = blacklistText.split(BLACKLIST_TOKENIZER_PATTERN);
		}
		
		if ((blacklistArray != null) && (blacklistArray.length > 0)) {
			blacklist = Arrays.asList(blacklistArray);
		} else {
			blacklist = new ArrayList<String>();
		}
				
		return blacklist;
	}
	
	// Members
	private final String versionNumber;
	private final boolean alwaysUseJMXPoll;
	private final int queryIntervalMillis;
	private final int minPollIntervalMillis;
	private final int maxPollIntervalMillis;
	private final List<String> componentBlacklist;
	private boolean useWLDFHarvester = false;
	private final FileSystemStore csvStats;
	private final WorkManager serviceThreadWkMgr;
	private final WorkManager captureThreadsWkMgr;
	private long sleepPeriodMillis = INITIAL_SLEEP_DURATION;
	private long startWorkTime = System.currentTimeMillis(); 
	private volatile boolean keepRunning = true;
	private boolean firstTimeProcessingRanOK = false;

	// Constants
	private final static boolean USE_BACKGROUND_JAVA_THREAD = true;
	private final static int ONE_SECOND_MILLIS = 1000;
	private final static int MINIMUM_SLEEP_SECS = 15;
	private final static float MIN_POLL_FACTOR = 0.1F;
	private final static float MAX_POLL_FACTOR = 0.9F;
	private final static int INITIAL_SLEEP_DURATION = 30 * 1000;
	private final static int INITIALISATION_ATTEMPT_AGAIN_SLEEP_DURATION = 90 * 1000;	
	private final static Pattern VERSION_MAJOR_MINOR_EXTRACTOR_PATTERN = Pattern.compile("(\\d+)\\.(\\d+).*$");
	private final static String BLACKLIST_TOKENIZER_PATTERN = ",\\s*";
	private final static String SERVICE_THREAD_WORK_MGR_JNDI = "java:comp/env/DomainHealth_BackgroundCollectorServiceWorkMngr";		
	private final static String CAPUTURE_THREADS_WORK_MGR_JNDI = "java:comp/env/DomainHealth_IndividualServerStatCapturerWorkMngr";		
}
