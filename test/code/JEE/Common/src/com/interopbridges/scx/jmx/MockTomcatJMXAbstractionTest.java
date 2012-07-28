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
import javax.management.QueryExp;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Test class to exercise the MockTomcatJMXAbstraction.
 * It is basically a sanity check to ensure methods are not 
 * called inadvertently, it also helps with code coverage
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class MockTomcatJMXAbstractionTest
{

    /**
     * <p>
     * Instance of the MBean Server.
     * </p>
     */
    private MockTomcatJMXAbstraction _theServer;
    
    /**
     * <p>
     * Method invoked before each unit-test in this class.
     * </p>
     */
    @Before
    public void Setup() 
    {
        _theServer = new MockTomcatJMXAbstraction();
    }
    
    /**
     * <p>
     * Method invoked after each unit-test in this class.
     * </p>
     */
    @After
    public void TearDown() {
        _theServer = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getAttribute(javax.management.ObjectName,
     * java.lang.String)
     */
    @Test
    public void testTomcat_getAttribute()
    {
        try
        {
            _theServer.getAttribute((ObjectName) null, (String) null);        
            Assert.fail("Failed to receive an exception on getAttribute.");
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
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanCount()
     */
    @Test
    public void testTomcat_getMBeanCount()
    {
        try
        {
            _theServer.getMBeanCount();        
            Assert.fail("Failed to receive an exception on getMBeanCount.");
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
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanInfo(javax.management.ObjectName)
     */
    @Test
    public void testTomcat_getMBeanInfo()
    {
        try
        {
            _theServer.getMBeanInfo((ObjectName) null);        
            Assert.fail("Failed to receive an exception on getMBeanInfo.");
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
     * @see com.interopbridges.scx.jmx.IJMX#queryMBeans(javax.management.ObjectName,
     * javax.management.QueryExp)
     */
    @Test
    public void testTomcat_queryMBeans()
    {
        try
        {
            _theServer.queryMBeans((ObjectName) null, (QueryExp)null);        
            Assert.fail("Failed to receive an exception on queryMBeans.");
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
     * @see com.interopbridges.scx.jmx.IJMX#queryNames(javax.management.ObjectName,
     * javax.management.QueryExp)
     */
    @Test
    public void testTomcat_queryNames()
    {
        try
        {
            _theServer.queryNames((ObjectName) null, (QueryExp)null);        
            Assert.fail("Failed to receive an exception on queryNames.");
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
     * @see com.interopbridges.scx.jmx.IJMX#registerMBean(java.lang.Object,
     * javax.management.ObjectName)
     */
    @Test
    public void testTomcat_registerMBean()
    {
        try
        {
        _theServer.registerMBean((Object)null, (ObjectName) null);        
        Assert.fail("Failed to receive an exception on registerMBean.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type UnsupportedOperationException", 
                    e.getClass() == UnsupportedOperationException.class);
        }        
    }
}

