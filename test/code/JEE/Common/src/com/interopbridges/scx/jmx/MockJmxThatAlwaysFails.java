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

import junit.framework.Assert;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;


import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.util.JmxConstant;

/**
 * <p>
 * Mock implementation of the JMX Abstraction interface to support a JMX store that 
 * cannot be instantiated. This class simulates a failed connection to 3rd party JMX Store (like WebSphere). 
 * This JMX store must always fail, this is done by throwing an exception in the constructor.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class MockJmxThatAlwaysFails implements IJMX {

    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    private ILogger _logger;

    /**
     * 'Simple' in-memory implementation of a JMX Store
     */
    private Hashtable<ObjectName, Object> _jmx;

    /**
     * <p>
     * Default constructor, this specific instance throws an exception 
     * in the constructor. 
     * This means that this specific instance of the JMX MBean store 
     * is not accessible.
     * </p>
     */
    public MockJmxThatAlwaysFails() throws ScxException{
        this._jmx = new Hashtable<ObjectName, Object>();
        this._logger = LoggingFactory.getLogger();
        
        this._logger.info(new StringBuffer("Forced failure connecting to JMX Store").toString());
        throw new ScxException(ScxExceptionCode.ERROR_MBEANSTORE_INSTANCE_INVALID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanServerID()
     */
    public int getMBeanServerID()
    {
        Assert.fail("This method should not be called as the constructor throws an exception");
        return JmxConstant.INVALID_MBEANSERVER_HASHCODE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getAttribute(javax.management.ObjectName,
     * java.lang.String)
     */
    public Object getAttribute(ObjectName name, String attribute)
            throws MBeanException, AttributeNotFoundException,
            InstanceNotFoundException, ReflectionException {
        /*
         * As this class should never be used and calls to its methods must fail
         */

        Assert.fail("This method should not be called as the constructor throws an exception");
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanCount()
     */
    public Integer getMBeanCount() {
        Assert.fail("This method should not be called as the constructor throws an exception");
        return _jmx.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanInfo(javax.management.ObjectName)
     */
    public MBeanInfo getMBeanInfo(ObjectName name)
            throws InstanceNotFoundException, IntrospectionException,
            ReflectionException {
        Assert.fail("This method should not be called as the constructor throws an exception");
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#isStandAloneJmxStore()
     */
    public boolean isStandAloneJmxStore()
    {
        return true;
    }
        
    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#queryMBeans(javax.management.ObjectName,
     * javax.management.QueryExp)
     */
    public Set<ObjectInstance> queryMBeans(ObjectName name, QueryExp query) {
        Assert.fail("This method should not be called as the constructor throws an exception");
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#queryNames(javax.management.ObjectName,
     * javax.management.QueryExp)
     */
    public Set<ObjectName> queryNames(ObjectName name, QueryExp query) {
        /*
         * Note: Implementation is very simple and may require modification to
         * meet your needs. Right now this is only meant to work for very simple
         * queries (like *).
         */
        Assert.fail("This method should not be called as the constructor throws an exception");
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#registerMBean(java.lang.Object,
     * javax.management.ObjectName)
     */
    public void registerMBean(Object bean, ObjectName keys)
            throws InstanceAlreadyExistsException, MBeanRegistrationException,
            NotCompliantMBeanException {
        Assert.fail("This method should not be called as the constructor throws an exception");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#verifyStoreConnection()
     */
    public boolean verifyStoreConnection()
    {
        // No additional checks are needed for this platform to
        // verify the JMX store connection
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#invoke()
     */
    public Object invoke(ObjectName name, String operationName, Object[] params, String[] signature)
            throws InstanceNotFoundException, ReflectionException, MBeanException, IOException
    {
        throw new UnsupportedOperationException();
    }

}
