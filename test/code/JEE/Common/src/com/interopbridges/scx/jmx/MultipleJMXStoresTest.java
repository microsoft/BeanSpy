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

import java.util.HashMap;
import java.util.Set;

import javax.management.DynamicMBean;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.jmx.JdkJMXAbstraction;
import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.mbeans.DummyInvokeMBean;
import com.interopbridges.scx.mbeans.MBeanGetter;
import com.interopbridges.scx.mbeanserver.MockMBeanServer;
import com.interopbridges.scx.mbeanserver.AnotherMockMBeanServer;

/**
 * Class to test the JMXStores class for specific
 * scenarios where there are multiple JMXStores.
 * The production JmxStores class has a specific filter 
 * that filters out duplicate MBeanServers. This class 
 * tests that these stores are handled correctly.  
 * 
 * @author Geoff Erasmus
 */
public class MultipleJMXStoresTest 
{
    /**
     * <p>
     * The underlying MBeanServer required to register and unregister the MBeans 
     * </p>
     */
    private javax.management.MBeanServer _MBeanstore;

    /**
     * <p>
     * The name for the registered mbean 
     * </p>
     */
    private static String mbeanName = "com.interopbridges.scx:name=DummyInvokeMBean";

    /**
     * <p>
     * Interface to getting the desired MBeans from the JMX Store (as XML).
     * </p>
     */
    protected MBeanGetter     _mbeanAccessor;
    
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
         JmxStores.addStoreToJmxStores(new JdkJMXAbstraction(_MBeanstore));
         
         Assert.assertEquals("Only one JMX Store should be connected", 1, JmxStores.getListOfJmxStoreAbstractions().size());
         
         _mbeanAccessor = new MBeanGetter(JmxStores.getListOfJmxStoreAbstractions());
         
         /* 
          * Create the MBean that will be registered on the MBean server.
          */
         DynamicMBean dummymbean = new DummyInvokeMBean();
         try
         {
             /* 
              * register the MBean on the MBeanServer.
              */
             JmxStores.getListOfJmxStoreAbstractions().get(0).registerMBean(
                     dummymbean, new ObjectName(mbeanName));
             
             /*
              * make sure that there is only 1 MBean in the store
              */
             assertTrue(JmxStores.getListOfJmxStoreAbstractions().get(0).getMBeanCount()==1);
             
             /*
              * make sure the correct bean got loaded correctly
              */
             Set<ObjectInstance> oinst = 
                 JmxStores.getListOfJmxStoreAbstractions().get(0).
                 queryMBeans(new ObjectName(mbeanName),null); 
             assertTrue(oinst.size()==1); 
             
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
      * Specific test to verify that if we add a second JMX store 
      * to the JMXStores collect that the new store is not added 
      * if an existing JMX store with the same underlying MBeanServer 
	  * already exists.
      * </p>
      */
     @Test
     public void DuplicateMBeanServer() 
     {
         /*
          * Add a new JMX Store to the list
          */
         JmxStores.addStoreToJmxStores(new JdkJMXAbstraction(MockMBeanServer.getInstance()));
         
         Assert.assertEquals("There should only be 1 JMX Stores connected", 1, JmxStores.getListOfJmxStoreAbstractions().size());
     }

     /**
      * <p>
      * Specific test to verify that if we add a second JMX store 
      * to the JMXStores collect that the new store is added 
      * if the underlying MBeanServer is unique. Each underlying 
      * MBeanServer is populated with the same MBean and a query is
      * run to verify that 2 MBeans are indeed returned.  
      * </p>
      */
     @Test
     public void MultipleMBeanServer() 
     {
         /*
          * Add a new JMX Store to the list
          */
         JmxStores.addStoreToJmxStores(new JdkJMXAbstraction(AnotherMockMBeanServer.getInstance()));
         
         Assert.assertEquals("There should be 2 JMX Stores connected", 2, JmxStores.getListOfJmxStoreAbstractions().size());
         
         DynamicMBean dummymbean = new DummyInvokeMBean();
         try
         {
             /* 
              * register the MBean on the MBeanServer.
              */
             JmxStores.getListOfJmxStoreAbstractions().get(1).registerMBean(
                     dummymbean, new ObjectName(mbeanName));
             
             /*
              * make sure that there is only 1 MBean in the store
              */
             assertTrue(JmxStores.getListOfJmxStoreAbstractions().get(1).getMBeanCount()==1);
             
             /*
              * make sure the correct bean got loaded correctly
              */
             HashMap<IJMX, Set<ObjectInstance>> oinst = _mbeanAccessor.getMBeans(mbeanName); 
             assertTrue(oinst.size()==2); 
             
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received registering the MBean: " + e.getMessage());
         }
         
     }
}
