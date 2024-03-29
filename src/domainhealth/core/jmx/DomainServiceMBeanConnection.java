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
package domainhealth.core.jmx;

import static domainhealth.core.jmx.WebLogicMBeanPropConstants.*;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Creates an WebLogic JMX Connection to WebLogic's Domain Service Tree. See 
 * description of WebLogicMBeanConnection for more info. Used primarily to
 * retrieve data about a Domain Runtime and its Server Runtime plus get a read
 * only view on the domain's current configuration.
 *  
 * @see WebLogicMBeanConnection
 */
public class DomainServiceMBeanConnection extends WebLogicMBeanConnection {
	/**
	 * Create a new connection to the WebLogic server's Domain Runtime Service
	 * MBean Tree.
	 * 
	 * @see WebLogicMBeanException
	 * 
	 * @param protocol The admin server connection protocol (ie. 't3' or 't3s')
	 * @param host The hostname/ip-address of the admin server
	 * @param port The listen admin port of the admin server
	 * @param username A WebLogic Administrator username to connect with 
	 * @param password A WebLogic Administrator password to connect with
	 * @throws WebLogicMBeanException Indicates that a JMX connection to server could not be made
	 */
	public DomainServiceMBeanConnection(String protocol, String host, int port, String username, String password) throws WebLogicMBeanException {
		super(protocol, host, port, username, password, DOMAIN_RUNTIME_SERVICE_NAME);
	}

	/**
	 * Create a new connection to the WebLogic server's Domain Runtime Service
	 * MBean Tree.
	 * 
	 * @see WebLogicMBeanException
	 *
	 * @throws WebLogicMBeanException Indicates that a JMX connection to server could not be made
	 */
	public DomainServiceMBeanConnection() throws WebLogicMBeanException {
		super(DOMAIN_RUNTIME_SERVICE_NAME);
	}

	/**
	 * Gets the root Domain Runtime MBean
	 * 
	 * @return The root Domain Runtime MBean 
	 * @throws WebLogicMBeanException Indicates that a JMX connection error occurred
	 */
	public ObjectName getDomainRuntime() throws WebLogicMBeanException {
		try {
			return (ObjectName) getConn().getAttribute(domainRuntimeServiceMBean, DOMAIN_RUNTIME);
		} catch (Exception e) {
			throw new WebLogicMBeanException(e.toString(), e);
		}
	}

	/**
	 * Gets the root Domain Configuration MBean
	 * 
	 * @return The root Domain Config MBean
	 * @throws WebLogicMBeanException Indicates that a JMX connection error occurred
	 */
	public ObjectName getDomainConfiguration() throws WebLogicMBeanException {
		try {
			return (ObjectName) getConn().getAttribute(domainRuntimeServiceMBean, DOMAIN_CONFIGURATION);	
		} catch (Exception e) {
			throw new WebLogicMBeanException(e.toString(), e);
		}
	}

	/**
	 * Gets the list of all Servers' root Runtime MBeans
	 * 
	 * @return The list of all Server Runtimes
	 * @throws WebLogicMBeanException Indicates that a JMX connection error occurred
	 */
	public ObjectName[] getAllServerRuntimes() throws WebLogicMBeanException {
		try {
			return (ObjectName[]) getConn().getAttribute(domainRuntimeServiceMBean, SERVER_RUNTIMES); 		
		} catch (Exception e) {
			throw new WebLogicMBeanException(e.toString(), e);
		}
	}

	// Constants
	private static final String DOMAIN_RUNTIME_SERVICE_NAME = "weblogic.management.mbeanservers.domainruntime";
	private static final ObjectName domainRuntimeServiceMBean;

	static {
		try {
			domainRuntimeServiceMBean = new ObjectName("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
		} catch (MalformedObjectNameException e) {
			throw new AssertionError(e.toString());
		}
	}	
}
