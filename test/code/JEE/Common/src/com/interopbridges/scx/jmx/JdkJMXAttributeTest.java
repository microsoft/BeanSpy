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

package com.interopbridges.scx.jmx;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import java.util.HashMap;
import javax.management.DynamicMBean;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.mbeans.TestContextMBean;
import com.interopbridges.scx.mbeans.MBeanGetter;
import com.interopbridges.scx.util.SAXParser;
import com.interopbridges.scx.mbeanserver.MockMBeanServer;

/**
 * Class to test the jeeserverinfo class
 * These tests specifically test the loading of the JMX store
 * and the retrieval of the JEE server information
 * @author Geoff Erasmus
 */
public class JdkJMXAttributeTest {

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
    private static String mbeanName = "mydomain:name=mymbean";

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
        _MBeanstore = MockMBeanServer.getInstance();
        JmxStores.addStoreToJmxStores(new MockJdkJMXAbstraction(_MBeanstore));

        Assert.assertEquals("Only one JMX Store should be connected", 1, 
                JmxStores.getListOfJmxStoreAbstractions().size());

        /* 
         * Create the MBean that will be queried by the tests.
         */
        DynamicMBean dummymbean = new TestContextMBean();
        try
        {
            /* 
             * register the MBean on the MBeanServer.
             */
            JmxStores.getListOfJmxStoreAbstractions().get(0).registerMBean(dummymbean, new ObjectName(mbeanName));
            
            /*
             * make sure the correct bean got loaded correctly
             */
            assertTrue(JmxStores.getListOfJmxStoreAbstractions().get(0).getMBeanCount()==1);   

            Set<ObjectInstance> oinst = 
                JmxStores.getListOfJmxStoreAbstractions().get(0).
                queryMBeans(new ObjectName(mbeanName),null); 
            assertTrue(oinst.size()==1); 
            
            /* 
             * Verify that the values were actually set 
             */
            Object al = JmxStores.getListOfJmxStoreAbstractions().get(0).getAttribute(new ObjectName(mbeanName), "Name");
            assertTrue("DummyMBean", al.toString().compareTo("TestContextMBean")==0);
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
        ((MockMBeanServer)_MBeanstore).setExceptionFlag(false);
        JmxStores.clearListOfJmxStores();
    }

    /**
     * <p>
     * Verify that for a illegal call to retrieve JEE server information 
     * that the correct information is received. 
     * </p>
     *
     * &lt;?xml version="1.0" encoding="UTF-8"?&rt;
     * &lt;MBeans version="NotFromLabel"&rt;
     *   &lt;MBean Name="javax.management.ObjectName" objectName="mydomain:name=mymbean"&rt;
     *     &lt;Properties&rt;
     *       &lt;Property Name="Name" type="java.lang.String"&rt;TestContextMBean&lt;/Property&gt;
     *       &lt;Property Name="SimpleClass" type="com.interopbridges.scx.mbeans.SimpleClass"&rt;
     *         &lt;Property Name="attrib" type="java.lang.String"&rt;UserClass Attribute&lt;/Property&gt;
     *         &lt;Property Name="this" type="com.interopbridges.scx.mbeans.SimpleClass"&rt;
     *           &lt;Property Name="attrib" type="java.lang.String"&rt;UserClass Attribute&lt;/Property&gt;
     *           &lt;Property Name="this" type="com.interopbridges.scx.mbeans.SimpleClass"&rt;
     *             &lt;Property Name="attrib" type="java.lang.String"&rt;UserClass Attribute&lt;/Property&gt;
     *             &lt;Property Name="this" type="com.interopbridges.scx.mbeans.SimpleClass"&rt;com.interopbridges.scx.mbeans.SimpleClass@17094d48&lt;/Property&gt;
     *           &lt;/Property&gt;
     *         &lt;/Property&gt;
     *       &lt;/Property&gt;
     *     &lt;/Properties&rt;
     *   &lt;/MBean&rt;
     * &lt;/MBeans&rt;     
     */
    @Test
    public void testDoGet_ContextSwitch() 
    {
        HashMap<String, String[]> Params = new HashMap<String, String[]>();
        Params.put("MaxDepth",new String[] {"4"});

        try 
        {
          /*
           * Setting the ExceptionFlag in the MBeanServer will cause an exception to be thrown
           * when trying to access an attribute.
           */
           ((MockMBeanServer)_MBeanstore).setExceptionFlag(true);

            MBeanGetter  _mbeanAccessor = new MBeanGetter(JmxStores.getListOfJmxStoreAbstractions());
            String xml = _mbeanAccessor.getMBeansAsXml(mbeanName,Params).toString();

            String[] s = (String []) SAXParser.XPathQuery(xml,"/MBeans/MBean[@Name='javax.management.ObjectName']/Properties/Property[@Name='SimpleClass']/Property[@Name='this']/Property[@Name='this']/Property[@Name='attrib']");

            Assert.assertTrue(
                    "Incorrect response to /MBeans/MBean[@Name='javax.management.ObjectName']/Properties/Property[@Name='SimpleClass']/Property[@Name='this']/Property[@Name='this']/Property[@Name='attrib'] does not exist.\n "+
                    "The XML element (/MBeans/MBean[@Name='javax.management.ObjectName']/Properties/Property[@Name='SimpleClass']/Property[@Name='this']/Property[@Name='this']/Property[@Name='attrib']) is empty",s[0].compareTo("UserClass Attribute")==0);
            
        } 
        catch (Exception e) 
        {
        e.printStackTrace();
          Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }

}


