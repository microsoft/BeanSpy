/**
 * Copyright (c) Microsoft Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use 
 * this file except in compliance with the License. You may obtain a copy of the 
 * License at http://www.apache.org/licenses/LICENSE-2.0.
 *  
 * THIS CODE IS PROVIDED *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS 
 * OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION 
 * ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE, 
 * MERCHANTABLITY OR NON-INFRINGEMENT. 
 *
 * See the Apache Version 2.0 License for specific language governing 
 * permissions and limitations under the License.
 */

package com.interopbridges.scx.mxbeanextender;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.DynamicMBean;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.jeestats.IStatistics;
import com.interopbridges.scx.jeestats.StatisticGroup;
import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.jmx.JdkJMXAbstraction;
import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.mbeans.DummyJBossMBean;
import com.interopbridges.scx.mbeanserver.MockMBeanServer;
import com.interopbridges.scx.mxbeanextender.MXBeanExtender;
import com.interopbridges.scx.util.JmxConstant;
import com.interopbridges.scx.util.SAXParser;
import com.interopbridges.scx.xml.StatisticXMLTransformer;

/**
 * Class to test the jeeserverinfo class for JBoss6
 * These tests specifically test the loading of the two JMX stores (one
 * for the JDK and another for JBoss6, they are the same underlying MBeanServer)
 * We then test the retrieval of the JEE server information from the JBoss store.
 * @author Geoff Erasmus
 */
public class MXBeanExtenderJBoss6InfoTest {

    /**
     * <p>
     * The underlying MBeanServer required to unregister the MBeans 
     * </p>
     */
    private javax.management.MBeanServer _MBeanstore;

    /**
     * <p>
     * The name for the registered mbean 
     * </p>
     */
    private static String mbeanName = "jboss.management.local:j2eeType=J2EEServer";

    /**
    * <p>
    * Method invoked before each unit-test in this class.
    * </p>
    * 
    * @throws Execption
    *             If something went wrong when initializing the MBean Store
    */
    @Before
    public void Setup() throws Exception 
    {
        /* 
        * For JBoss6 there are 2 MBean Servers detected, one for the JDK and the other for JBoss6
        * The JBoss6 store however is the same as the JDK store.
        */
        MockMBeanServer.Reset_TestMBeanServer();

        _MBeanstore = MockMBeanServer.getInstance();
        JmxStores.clearListOfJmxStores();
        
        IJMX ijmx = new JdkJMXAbstraction(_MBeanstore);
        
        /* Add the JMX store as a JBoss store. */
        JmxStores.addNamedStoreToJmxStores(ijmx,"com.interopbridges.scx.jmx.JBossJMXAbstraction");

        /* Add the JMX store as the JDK store. */
        JmxStores.addStoreToJmxStores(ijmx);
        
        /* 
         * Due to the fact that the underlying MBeanServer is the same the JDK store takes precidence 
         * and there is only one store connected.
         */
        Assert.assertEquals("Only one JMX Stores should be connected", 1, 
                JmxStores.getListOfJmxStoreAbstractions().size());
        
        /* 
         * As we have added both the JBoss and the JDK store both store names must exist. 
         */
        Assert.assertEquals("Two Named JMX Stores should be connected", 2, 
                JmxStores.getListOfJmxStoreAbstractionNames().size());

        /* 
         * Create the MBean that will be queried by the tests.
         */
        DynamicMBean dummymbean = new DummyJBossMBean();
        try
        {
            /* 
             * register the MBean on the MBeanServer.
             */
            JmxStores.getListOfJmxStoreAbstractions().get(0).registerMBean(
                    dummymbean, new ObjectName(mbeanName));
            
            /*
             * make sure the correct bean got loaded correctly
             */
            Set<ObjectInstance> oinst = 
                JmxStores.getListOfJmxStoreAbstractions().get(0).
                queryMBeans(new ObjectName(mbeanName),null); 
            assertTrue(oinst.size()==1); 

            assertTrue(JmxStores.getListOfJmxStoreAbstractions().get(0).
                    getMBeanInfo(new ObjectName(mbeanName)).
                    getDescription().compareTo("Dummy JBoss Mean")==0); 

            /*
             * make sure that there is only 1 MBean in the store
             */
            assertTrue(JmxStores.getListOfJmxStoreAbstractions().get(0).getMBeanCount()==1); 
            
            /*
             * Set the attributes for the mbean
             */
            AttributeList al = new AttributeList();
            al.add(new Attribute("serverVersion","99.99.99"));
            al.add(new Attribute("serverVendor","JBoos"));
            _MBeanstore.setAttributes(new ObjectName(mbeanName), al);
            
            /* 
             * Verify that the values were actually set 
             */
            String[] s = {"serverVersion","serverVendor"};
            al = _MBeanstore.getAttributes(new ObjectName(mbeanName), s);
            assertTrue("DummyJBossMBean", al.asList().get(0).getValue().toString().compareTo("99.99.99")==0);
            assertTrue("DummyJBossMBean", al.asList().get(1).getValue().toString().compareTo("JBoos")==0);
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Exception Received registering the MBean: " + e.getMessage());
        }
    }

    /**
    * <p>
    * Unit Test Teardown method
    * </p>
    */
    @After
    public void Teardown() 
    {
        try
        {
            _MBeanstore.unregisterMBean(new ObjectName(mbeanName));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Exception Received unregistering the MBean: " + e.getMessage());
        }
        JmxStores.clearListOfJmxStores();
    }

    /**
     * <p>
     * Verify that for a illegal call to retrieve JEE server information 
     * that the correct information is received. 
     * </p>
     */
    @Test
    public void testDoGet_ApplicationServerInfo_JEEServer() 
    {
        try 
        {
            Class<?> StatsClass = Class.forName("com.interopbridges.scx.jeeinfo.JeeServerInfo");
            Object ClassInstance = StatsClass.newInstance();
            StatisticGroup sg =  ((IStatistics)ClassInstance).getStats();

            StatisticXMLTransformer xdoc = new StatisticXMLTransformer();
            String xml = xdoc.transformGroupStatistics(MXBeanExtender.InformationXMLTag, sg).toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/Info/JEEServer/Properties/AppServerVersion");

            Assert.assertTrue(
                    "Incorrect response to /Info/JEEServer/Properties/AppServerVersion does not exist.\n "+
                    "The XML element (/Info/JEEServer/Properties/AppServerVersion) is empty",s[0].compareTo("99.99.99")==0);
            
            
            s = (String []) SAXParser.XPathQuery(xml,"/Info/JEEServer/Properties/AppServerName");
            Assert.assertTrue(
                    "Incorrect response to /Info/JEEServer/Properties/AppServerName does not exist.\n "+
                    "The XML element (/Info/JEEServer/Properties/AppServerName) is empty",s[0].compareTo("JBoos")==0);
        } 
        catch (Exception e) 
        {
          Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }
}


