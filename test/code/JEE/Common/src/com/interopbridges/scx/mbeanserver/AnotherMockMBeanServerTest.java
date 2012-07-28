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

package com.interopbridges.scx.mbeanserver;



import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.mbeans.DummyJBossMBean;
import com.interopbridges.scx.mbeanserver.AnotherMockMBeanServer;


/**
 * <p>
 * Test class to exercise the TestMBeanServer.
 * It is basically a sanity check to ensure methods are not 
 * called inadvertently, it also helps with code coverage
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class AnotherMockMBeanServerTest
{

    /**
     * <p>
     * Instance of the MBean Server.
     * </p>
     */
    private AnotherMockMBeanServer _theServer;
    
    /**
     * <p>
     * Method invoked before each unit-test in this class.
     * </p>
     */
    @Before
    public void Setup() 
    {
        _theServer = AnotherMockMBeanServer.getInstance();
        try
        {
            _theServer.registerMBean(new DummyJBossMBean(), new ObjectName("test:name=TestBean"));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Exception Received registering the MBean: " + e.getMessage());
        }
    }
    
    /**
     * <p>
     * Method invoked after each unit-test in this class.
     * </p>
     */
    @After
    public void TearDown() {
        try
        {
            _theServer.unregisterMBean(new ObjectName("test:name=TestBean"));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Exception Received unregistering the MBean: " + e.getMessage());
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getAttribute
     */
    @Test
    public void addNotificationListener_onno() throws Exception 
    {
        try
        {
            _theServer.addNotificationListener((ObjectName) null,
                (NotificationListener) null, (NotificationFilter) null,
                null);        
            Assert.fail("Failed to receive an exception on adding addNotificationListener.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#addNotificationListener
     */
    @Test
    public void addNotificationListener_oono() throws Exception 
    {
        try
        {
            _theServer.addNotificationListener((ObjectName) null,
                (ObjectName) null, (NotificationFilter) null,
                null);        
            Assert.fail("Failed to receive an exception on adding addNotificationListener.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean
     */
    @Test
    public void createMBean_so() throws Exception 
    {
        try
        {
            _theServer.createMBean("",(ObjectName) null);        
            Assert.fail("Failed to receive an exception on calling createMBean.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean
     */
    @Test
    public void createMBean_soo() throws Exception 
    {
        try
        {
            _theServer.createMBean("",(ObjectName) null,(ObjectName) null);        
            Assert.fail("Failed to receive an exception on calling createMBean.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean
     */
    @Test
    public void createMBean_soos() throws Exception 
    {
        try
        {
            _theServer.createMBean("",(ObjectName) null,(Object[]) null, (String[]) null);        
            Assert.fail("Failed to receive an exception on calling createMBean.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean
     */
    @Test
    public void createMBean_sooos() throws Exception 
    {
        try
        {
            _theServer.createMBean("",(ObjectName) null,(ObjectName) null,
                    (Object[]) null, (String[]) null);        
            Assert.fail("Failed to receive an exception on calling createMBean.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize
     */
    @Test
    public void deserialize_ob() throws Exception 
    {
        try
        {
            _theServer.deserialize((ObjectName) null,(byte[]) null);        
            Assert.fail("Failed to receive an exception on calling deserialize.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

  
    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize
     */
    @Test
    public void deserialize_sb() throws Exception 
    {
        try
        {
            _theServer.deserialize("",(byte[]) null);        
            Assert.fail("Failed to receive an exception on calling deserialize.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize
     */
    @Test
    public void deserialize_sob() throws Exception 
    {
        try
        {
            _theServer.deserialize("",(ObjectName) null, (byte[]) null);        
            Assert.fail("Failed to receive an exception on calling deserialize.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getClassLoader
     */
    @Test
    public void getClassLoader_o() throws Exception 
    {
        try
        {
            _theServer.getClassLoader((ObjectName) null);        
            Assert.fail("Failed to receive an exception on calling getClassLoader.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see javax.management.getClassLoaderRepository
     */
    @Test
    public void getClassLoaderRepository() throws Exception 
    {
        try
        {
            _theServer.getClassLoaderRepository();        
            Assert.fail("Failed to receive an exception on calling getClassLoaderRepository.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getDefaultDomain
     */
    @Test
    public void getDefaultDomain() throws Exception 
    {
        try
        {
            _theServer.getDefaultDomain();        
            Assert.fail("Failed to receive an exception on calling getDefaultDomain.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getDomains
     */
    @Test
    public void getDomains() throws Exception 
    {
        try
        {
            _theServer.getDomains();        
            Assert.fail("Failed to receive an exception on calling getDomains.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getObjectInstance
     */
    @Test
    public void getObjectInstance() throws Exception 
    {
        try
        {
            _theServer.getObjectInstance((ObjectName) null);        
            Assert.fail("Failed to receive an exception on calling getObjectInstance.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate
     */
    @Test
    public void instantiate_s() throws Exception 
    {
        try
        {
            _theServer.instantiate("");        
            Assert.fail("Failed to receive an exception on calling instantiate.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate
     */
    @Test
    public void instantiate_so() throws Exception 
    {
        try
        {
            _theServer.instantiate("",(ObjectName) null);        
            Assert.fail("Failed to receive an exception on calling instantiate.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate
     */
    @Test
    public void instantiate_sos() throws Exception 
    {
        try
        {
            _theServer.instantiate("",(Object[]) null,(String[]) null);        
            Assert.fail("Failed to receive an exception on calling instantiate.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate
     */
    @Test
    public void instantiate_soos() throws Exception 
    {
        try
        {
            _theServer.instantiate("",(ObjectName) null, (Object[]) null,(String[]) null);        
            Assert.fail("Failed to receive an exception on calling instantiate.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#isInstanceOf
     */
    @Test
    public void isInstanceOf() throws Exception 
    {
        try
        {
            _theServer.isInstanceOf((ObjectName) null, "");        
            Assert.fail("Failed to receive an exception on calling isInstanceOf.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#isRegistered
     */
    @Test
    public void isRegistered() throws Exception 
    {
        try
        {
            _theServer.isRegistered((ObjectName) null);        
            Assert.fail("Failed to receive an exception on calling isRegistered.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener
     */
    @Test
    public void removeNotificationListener_oo() throws Exception 
    {
        try
        {
            _theServer.removeNotificationListener((ObjectName) null,(ObjectName) null);        
            Assert.fail("Failed to receive an exception on calling removeNotificationListener.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener
     */
    @Test
    public void removeNotificationListener_on() throws Exception 
    {
        try
        {
            _theServer.removeNotificationListener((ObjectName) null,(NotificationListener) null);        
            Assert.fail("Failed to receive an exception on calling removeNotificationListener.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener
     */
    @Test
    public void removeNotificationListener_oono() throws Exception 
    {
        try
        {
            _theServer.removeNotificationListener((ObjectName) null,(ObjectName) null,(NotificationFilter) null,(Object) null);        
            Assert.fail("Failed to receive an exception on calling removeNotificationListener.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener
     */
    @Test
    public void removeNotificationListener_onno() throws Exception 
    {
        try
        {
            _theServer.removeNotificationListener((ObjectName) null,
                    (NotificationListener) null,(NotificationFilter) null,(Object) null);        
            Assert.fail("Failed to receive an exception on calling removeNotificationListener.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#invoke
     */
    @Test
    public void invoke() throws Exception 
    {
        try
        {
            /*
             * The MBean is valid but the invoke method is not implemented by the MBean
             */
            _theServer.invoke(new ObjectName("test:name=TestBean"),"a", null, null);        
            Assert.fail("Failed to receive an exception on calling invoke.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException:"+e.getClass(), 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#queryNames
     */
    @Test
    public void queryNames() throws Exception 
    {
        Set<ObjectName> qn = _theServer.queryNames(new ObjectName("test:name=TestBean"),null);
        
        Assert.assertTrue(
            "The 'test:name=TestBean' does not exist in the MBean Store", 
                qn.size()==1);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#setAttribute
     */
    @Test
    public void setAttribute_InvalidMBean() throws Exception 
    {
        try
        {
            _theServer.setAttribute(new ObjectName("a:b=c"), new Attribute("junk","Value1"));
            Assert.fail("An exception should have been thrown.");
        }
        catch (Exception e)
        {
            Assert.assertTrue(
                    "Exception should have been of type InstanceNotFoundException:"+e.getClass(), 
                    e.getClass() == InstanceNotFoundException.class);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#setAttribute
     */
    @Test
    public void setAttributeInvalidAttribute() throws Exception 
    {
        try
        {
            _theServer.setAttribute(new ObjectName("test:name=TestBean"), new Attribute("junk","Value1"));
            Assert.fail("An exception should have been thrown.");
        }
        catch (Exception e)
        {
            Assert.assertTrue(
                    "Exception should have been of type AttributeNotFoundException", e
                            .getClass() == AttributeNotFoundException.class);
        }
    }
}

