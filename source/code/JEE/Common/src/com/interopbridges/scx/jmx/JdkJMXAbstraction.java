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

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

/**
 * <p>
 * JDK 6 Abstraction for the JMX Store
 * </p>
 * 
 * @author Christopher Crammond
 */
public class JdkJMXAbstraction implements IJMX {

    /**
     * <p>
     * Connection to the JMX Store
     * </p>
     */
    protected MBeanServer _server;

    /**
     * <p>
     * Default Constructor
     * </p>
     */
    public JdkJMXAbstraction() {
        this( ManagementFactory.getPlatformMBeanServer() );
    }

    /**
     * <p>
     * Constructor, specifying the MBeanServer
     * </p>
     * 
     * @param mbeanServer
     *            An instance of a mBeanServer to use for all MBeanServer calls 
     */
    public JdkJMXAbstraction(MBeanServer mbeanServer) {
        this._server = mbeanServer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanServerID()
     */
    public int getMBeanServerID()
    {
        return this._server.hashCode();
    }

    /**
     * <p>
     * Sets the current thread classloader to a new classloader.
     * </p>
     * 
     * @param newClassLoader
     *            The classloader to become the current thread classloader.
     *
     */
    protected void setThreadClassLoader(ClassLoader newClassLoader)
    {
        Thread.currentThread().setContextClassLoader(newClassLoader);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getAttribute(javax.management.ObjectName,
     * java.lang.String)
     */
    public Object getAttribute(ObjectName name, String attribute)
            throws MBeanException, AttributeNotFoundException,
            InstanceNotFoundException, ReflectionException, IOException 
    {
        Object ret = null;

        try
        {
            ret = this._server.getAttribute(name, attribute);
        }
        catch(MBeanException e)
        {
            /*
             * There is a scenario where the classloader for BeanSpy and the 
             * given MBeanServer are different. In some situations we get a classnotfound exception
             * on trying to access certain attributes. We will atempt to set BeanSpy's
             * classloader to the MBeanServer classloader to retrieve the attribute and reset it when done.
             */
            if(e.getCause() instanceof ClassNotFoundException)
            {
                ClassLoader save_cl = Thread.currentThread().getContextClassLoader();
                try
                {
                    setThreadClassLoader(this._server.getClassLoaderFor(name));
                    ret = this._server.getAttribute(name, attribute);
                }
                finally
                {
                    setThreadClassLoader(save_cl);
                }
            }
            else
            {
               throw e;
            }
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanCount()
     */
    public Integer getMBeanCount() {
        return this._server.getMBeanCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanInfo(javax.management.ObjectName)
     */
    public MBeanInfo getMBeanInfo(ObjectName name)
            throws InstanceNotFoundException, IntrospectionException,
            ReflectionException, IOException {
        return this._server.getMBeanInfo(name);
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
    public Set<ObjectInstance> queryMBeans(ObjectName name, QueryExp query) 
            throws IOException
    {
        return this._server.queryMBeans(name, query);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#queryNames(javax.management.ObjectName,
     * javax.management.QueryExp)
     */
    public Set<ObjectName> queryNames(ObjectName name, QueryExp query) 
            throws IOException
    {
        return this._server.queryNames(name, query);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#registerMbean(java.lang.Object,
     * javax.management.ObjectName)
     */
    public void registerMBean(Object bean, ObjectName keys)
            throws InstanceAlreadyExistsException, MBeanRegistrationException,
            NotCompliantMBeanException, IOException {
        this._server.registerMBean(bean, keys);
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
     * @see com.interopbridges.scx.jmx.IJMX#invoke(ObjectName, String, Object[], String[])
     */
    public Object invoke(ObjectName name, String operationName, Object[] params, String[] signature)
    throws InstanceNotFoundException, ReflectionException, MBeanException, IOException
    {
        return this._server.invoke(name, operationName, params, signature);
    }
}
