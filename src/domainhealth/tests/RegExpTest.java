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
package domainhealth.tests;

import domainhealth.backend.retriever.RetrieverBackgroundService;
import domainhealth.backend.wldfcapture.HarvesterWLDFModuleCreator;
import domainhealth.backend.wldfcapture.data.TypeDataRecord;
import junit.framework.TestCase;

/**
 * Test-case class for various Regular Expression utility methods in the 
 * project.
 */
public class RegExpTest extends TestCase {
	/**
	 * Test method
	 */	
    public void testMBeanObjectNameExtractor() {
    	assertEquals(TypeDataRecord.extractMBeanObjectName("com.bea:Name=WLS103MultiSvrAdminServer,Type=ServerRuntime"), 
    			"WLS103MultiSvrAdminServer");
    	assertEquals(TypeDataRecord.extractMBeanObjectName("com.bea:Name=WLS103MultiSvrAdminServer,ServerRuntime=WLS103MultiSvrAdminServer,Type=JVMRuntime"), 
    			"WLS103MultiSvrAdminServer");
    	assertEquals(TypeDataRecord.extractMBeanObjectName("com.bea:Name=ThreadPoolRuntime,ServerRuntime=WLS103MultiSvrServer1,Type=ThreadPoolRuntime"), 
    			"ThreadPoolRuntime");
    	assertEquals(TypeDataRecord.extractMBeanObjectName("com.bea:Name=MyDB,ServerRuntime=WLS103MultiSvrServer2,Type=JDBCDataSourceRuntime"), 
    			"MyDB");
    	assertEquals(TypeDataRecord.extractMBeanObjectName("com.bea:JMSServerRuntime=WLS103MultiSvrServer2_MainJMSRsc2JMSServer,Name=MainJMSRsc2!WLS103MultiSvrServer2_MainJMSRsc2JMSServer@jms.QA,ServerRuntime=WLS103MultiSvrServer2,Type=JMSDestinationRuntime"), 
    			"MainJMSRsc2!WLS103MultiSvrServer2_MainJMSRsc2JMSServer@jms.QA");
    	assertEquals(TypeDataRecord.extractMBeanObjectName("com.bea:ApplicationRuntime=MyOtherDB,Name=weblogic.wsee.mdb.DispatchPolicy,ServerRuntime=WLS103MultiSvrServer2,Type=WorkManagerRuntime"), 
    			"weblogic.wsee.mdb.DispatchPolicy");
    	assertEquals(TypeDataRecord.extractMBeanObjectName("com.bea:Name=weblogic.logging.DomainLogBroadcasterClient,ServerRuntime=WLS103MultiSvrServer2,Type=WorkManagerRuntime"), 
    			"weblogic.logging.DomainLogBroadcasterClient");
    	assertEquals(TypeDataRecord.extractMBeanObjectName("com.bea:ApplicationRuntime=DomainHealth_WLDFHarvesterModule,Name=weblogic.wsee.mdb.DispatchPolicy,ServerRuntime=WLS103MultiSvrServer2,Type=WorkManagerRuntime"), 
    			"weblogic.wsee.mdb.DispatchPolicy");
    }
    
	/**
	 * Test method
	 */	
    public void testWLDFModuleVersionTextVersion() {
    	HarvesterWLDFModuleCreator harvesterWLDFModuleCreator1 = new HarvesterWLDFModuleCreator(60*1000, "0.9.0b3");
    	assertTrue(harvesterWLDFModuleCreator1.doesModuleDescriptionContainCurrentModuleVersion("WLDF Module for DH. v0.9.0b3."));
    	assertTrue(harvesterWLDFModuleCreator1.doesModuleDescriptionContainCurrentModuleVersion(". v0.9.0b3."));
    	assertFalse(harvesterWLDFModuleCreator1.doesModuleDescriptionContainCurrentModuleVersion(". v0.0.0."));
    	HarvesterWLDFModuleCreator harvesterWLDFModuleCreator2 = new HarvesterWLDFModuleCreator(60*1000, "0.9b3");
    	assertTrue(harvesterWLDFModuleCreator2.doesModuleDescriptionContainCurrentModuleVersion(". v0.9b3."));
    }
    
	/**
	 * Test method
	 */	
    public void testWebLogicVersionComparer() {
    	// >= 10.3.0
    	assertTrue(RetrieverBackgroundService.isWebLogicVersion103OrGreater("10.3"));
    	assertTrue(RetrieverBackgroundService.isWebLogicVersion103OrGreater("10.3.0"));
    	assertTrue(RetrieverBackgroundService.isWebLogicVersion103OrGreater("10.3.1"));
    	assertTrue(RetrieverBackgroundService.isWebLogicVersion103OrGreater("10.3.1.1"));
    	assertTrue(RetrieverBackgroundService.isWebLogicVersion103OrGreater("10.3.1.1.1"));
    	assertTrue(RetrieverBackgroundService.isWebLogicVersion103OrGreater("11.0"));
    	assertTrue(RetrieverBackgroundService.isWebLogicVersion103OrGreater("11.0.1"));
    	assertTrue(RetrieverBackgroundService.isWebLogicVersion103OrGreater("11.1.1"));

    	// < 10.3.0
    	assertFalse(RetrieverBackgroundService.isWebLogicVersion103OrGreater("10.0"));
    	assertFalse(RetrieverBackgroundService.isWebLogicVersion103OrGreater("10.0.1"));
    	assertFalse(RetrieverBackgroundService.isWebLogicVersion103OrGreater("10.0.3"));
    	assertFalse(RetrieverBackgroundService.isWebLogicVersion103OrGreater("10.2"));
    	assertFalse(RetrieverBackgroundService.isWebLogicVersion103OrGreater("9.0"));
    	assertFalse(RetrieverBackgroundService.isWebLogicVersion103OrGreater("9.1"));
    	assertFalse(RetrieverBackgroundService.isWebLogicVersion103OrGreater("9.2"));
    	assertFalse(RetrieverBackgroundService.isWebLogicVersion103OrGreater("9.2.3"));
    }
}