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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import com.interopbridges.scx.jmx.MockTomcatJMXAbstraction;
import com.interopbridges.scx.mbeans.DummyTomcatMBean;
import com.interopbridges.scx.mbeanserver.MockMBeanServer;
import com.interopbridges.scx.mxbeanextender.MXBeanExtender;
import com.interopbridges.scx.util.SAXParser;
import com.interopbridges.scx.xml.StatisticXMLTransformer;

/**
 * Class to test the jeeserverinfo class
 * These tests specifically test the loading of the JMX store
 * and the retrieval of the JEE server information
 * @author Geoff Erasmus
 */
public class MXBeanExtenderTomcatInfoTest {

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
    private static String mbeanName = "Catalina:type=Server";

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
        * Create a new MBeanServer to host the MBean that will be queried.
        */
        JmxStores.clearListOfJmxStores();
        MockMBeanServer.Reset_TestMBeanServer();
        _MBeanstore = MockMBeanServer.getInstance();
        JmxStores.addNamedStoreToJmxStores(new MockTomcatJMXAbstraction(),"com.interopbridges.scx.jmx.TomcatJMXAbstraction");
        JmxStores.addStoreToJmxStores(new JdkJMXAbstraction(_MBeanstore));

        /*
         * Adding the MockTomcatJMXAbstraction does not add a new JMX store to the list of JmxStores.
         * The MockTomcatJMXAbstraction return false for the isStandAloneJmxStore() which means that 
         * does not get added or queried for subsequent queries.
         */
        Assert.assertEquals("There should only be 1 JMX Stores connected", 1, 
                JmxStores.getListOfJmxStoreAbstractions().size());

        /* 
         * Create the MBean that will be queried by the tests.
         */
        DynamicMBean dummymbean = new DummyTomcatMBean();
        try
        {
            /* 
             * register the MBean on the MBeanServer.
             */
        	_MBeanstore.registerMBean(dummymbean, new ObjectName(mbeanName));
            
            /*
             * make sure the correct bean got loaded correctly
             */
        	Set<ObjectInstance> theBeans = new HashSet<ObjectInstance>();
        	List<IJMX> ilist = JmxStores.getListOfJmxStoreAbstractions();
            for(Iterator<IJMX> it = ilist.iterator();it.hasNext();)
            {
            	theBeans.addAll(it.next().queryMBeans( new ObjectName(mbeanName), null));
            }
            assertTrue(theBeans.size()==1);
            
            /*
             * Set the attributes for the mbean
             */
            AttributeList al = new AttributeList();
            al.add(new Attribute("serverInfo","Tomkitty 6"));
            _MBeanstore.setAttributes(new ObjectName(mbeanName), al);
            
            /* 
             * Verify that the values were actually set 
             */
            al = _MBeanstore.getAttributes(new ObjectName(mbeanName), new String[] {"serverInfo"});
            assertTrue("DummyTomcatMBean", al.asList().get(0).getValue().toString().compareTo("Tomkitty 6")==0);
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
            StatisticGroup sg = getJEEInfoStatistic();
            StatisticXMLTransformer xdoc = new StatisticXMLTransformer();
            String xml = xdoc.transformGroupStatistics(MXBeanExtender.InformationXMLTag, sg).toString();

            String[] s = (String []) SAXParser.XPathQuery(xml,"/Info/JEEServer/Properties/AppServerVersion");
            Assert.assertTrue(
                    "Incorrect response to /Info/JEEServer/Properties/AppServerVersion.\n "+
                    "The XML element (/Info/JEEServer/Properties/AppServerVersion) should not exist",s!=null);
            
            
            s = (String []) SAXParser.XPathQuery(xml,"/Info/JEEServer/Properties/AppServerName");
            Assert.assertTrue(
                    "Incorrect response to /Info/JEEServer/Properties/AppServerName does not exist.\n "+
                    "The XML element (/Info/JEEServer/Properties/AppServerName) is empty",s[0].compareTo("Tomkitty 6")==0);
        } 
        catch (Exception e) 
        {
          Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve JEE server information 
     * when there is no JMXStore registered that the correct information is received. 
     * </p>
     */
    @Test
    public void testDoGet_ApplicationServerInfo_JEEServer_NoJMXStore() 
    {
        JmxStores.clearListOfJmxStores();
        try 
        {
            StatisticGroup sg = getJEEInfoStatistic();
            StatisticXMLTransformer xdoc = new StatisticXMLTransformer();
            String xml = xdoc.transformGroupStatistics(MXBeanExtender.InformationXMLTag, sg).toString();

            String[] s = (String []) SAXParser.XPathQuery(xml,"/Info/JEEServer/Properties/*");
            Assert.assertTrue(
                    "Incorrect response to /Info JEEServer should not contain any entries.\n "+
                    "The XML element (/Info/JEEServer/Properties/*) must be empty",s.length==0);
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received:"+e.getMessage());
        }
    }

    /**
     * <p>
     * Internal helper method to retrieve all JEE Info statistics.
     * </p>
     */
    protected StatisticGroup getJEEInfoStatistic()
        throws  ClassNotFoundException, 
                InstantiationException, 
                IllegalAccessException  
    {
        Class<?> StatsClass = Class.forName("com.interopbridges.scx.jeeinfo.JeeServerInfo");
        Object ClassInstance = StatsClass.newInstance();
        return ((IStatistics)ClassInstance).getStats();
    }
}


