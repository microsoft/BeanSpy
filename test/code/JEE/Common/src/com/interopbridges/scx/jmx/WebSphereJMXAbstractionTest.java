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

import javax.management.ObjectName;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.mbeanserver.MockMBeanServer;

/**
 * <p>
 * Simple unit-tests to verify WebSphereJMXAbstraction constructor.
 * </p>
 * 
 * @author Jinlong Li
 * 
 */
public class WebSphereJMXAbstractionTest {
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
    private static String mbeanName = "WebSphere:j2eeType=J2EEServer";

    @Before
    public void Setup() throws Exception 
    {
         /* 
          * Create a new MBeanServer to host the MBean that will be queried.
          */
          _MBeanstore = MockMBeanServer.getInstance();
          MockMBeanServer.Reset_TestMBeanServer();       
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
    * A positive test case for test_WebSphereJMXAbstraction_NoSuchMethod.
    * </p>
    */
    @Test
    public void test_WebSphereJMXAbstraction_ConnectSuccuss_Test() 
    {
        try 
        {    
            JmxStores.clearListOfJmxStores();
            WebSphereJMXAbstraction jmxStoreAbstraction = new WebSphereJMXAbstraction("com.ibm.websphere.management.MockAdminServiceFactory"); 
            JmxStores.addStoreToJmxStores(jmxStoreAbstraction);
            Assert.assertEquals("One JMX Store should be connected successfully" , 1, 
                                                         JmxStores.getListOfJmxStoreAbstractions().size());
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected exception catched" + e.getMessage());
        }
    }

   /**
    * <p>
    * Verify that if the getMBeanFactory function is missing in the class,
    * a scxException would be thrown. 
    * </p>
    */
    @Test
    public void test_WebSphereJMXAbstraction_NoSuchMethod() 
    {
        try 
        {
            JmxStores.clearListOfJmxStores();
            WebSphereJMXAbstraction jmxStoreAbstraction = new WebSphereJMXAbstraction("com.ibm.websphere.management.MockAdminServiceFactoryNoMethod"); 
            Assert.fail("Constructor should fail.");
        }
        catch (Exception e) 
        {
            Assert.assertEquals("Should be a ScxException", ScxException.class, e.getClass());
            Assert.assertNotNull("Exception Code should not be null", ((ScxException) e).getExceptionCode());
            Assert.assertEquals("Should throw an ERROR_CONNECTING_JMXSTORE exception",
                                                             ((ScxException) e).getExceptionCode(), 
                                                              ScxExceptionCode.ERROR_CONNECTING_JMXSTORE);
        }
    }
}
