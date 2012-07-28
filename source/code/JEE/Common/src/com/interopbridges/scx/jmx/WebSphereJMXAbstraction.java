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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;


/**
 * <p>
 * Websphere 6.1 and & 7 Abstraction for the AdminClient JMX Store
 * </p>
 * 
 * @author Geoff Erasmus
 */
public class WebSphereJMXAbstraction implements IJMX {

    /**
     * <p>
     * Singleton Connection to the JMX Store
     * </p>
     */
    private MBeanServerConnection _server;

    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    ILogger _logger;

    /**
     * <p>
     * Constructor. The constructor attempts to connect to the main WebSphere JMX MBean Store
     * it does not use specific api calls but rather atempts to load the 
     * com.ibm.websphere.management.AdminServiceFactory class. If the class can be loaded it atempts to 
     * retrieve a MBean server connection from the MBean factory.
     * </p>
     *
     * @param JMXClass
     *            A class name containing 'getMBeanFactory' method, 
     *            
     * @throws ScxException
     *             The method call to getMBeanServer did not return an instance of a MBeanServer.
     */
    public WebSphereJMXAbstraction(String JMXClass) throws 
                ScxException
    {
        _logger = LoggingFactory.getLogger();
        try
        {
            ClassLoader classLoader = this.getClass().getClassLoader();

            /*
             *  Try and load the class using the default classloader. If
             *  it is able to load the WebSphere Admin classes then we are 
             *  running on WebSphere
             */
            Class<?> aClass = classLoader.loadClass(JMXClass);
        
            Method meth = aClass.getDeclaredMethod("getMBeanFactory",(Class<?>[])null);
            Object oBeanFactory = meth.invoke(this, (Object[])null);
        
            meth = oBeanFactory.getClass().getDeclaredMethod("getMBeanServer",(Class<?>[])null);
            Object oBeanServer = meth.invoke(oBeanFactory, (Object[])null);
            if(oBeanServer instanceof MBeanServer)
            {
                _server = (MBeanServerConnection)oBeanServer;
            }
            else
            {
                throw new ScxException(ScxExceptionCode.ERROR_MBEANSTORE_INSTANCE_INVALID);
            }
        }
        catch (Exception e)
        {
                _logger.fine("The constructor of WebSphereJMXAbstraction class throws an exception: " + e.getCause()); 
                throw new ScxException(ScxExceptionCode.ERROR_CONNECTING_JMXSTORE);
        }
    }

    /**
     * <p>
     * Constructor. The constructor attempts to connect to the main WebSphere JMX MBean Store
     * it does not use specific api calls but rather atempts to load the 
     * com.ibm.websphere.management.AdminServiceFactory class. If the class can be loaded it atempts to 
     * retrieve a MBean server connection from the MBean factory.
     * </p>
     *
     * @throws ScxException
     *             The method call to getMBeanServer did not return an instance of a MBeanServer.
     */
    public WebSphereJMXAbstraction() throws 
                ScxException
    {
        this("com.ibm.websphere.management.AdminServiceFactory");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getAttribute(javax.management.ObjectName,
     * java.lang.String)
     */
    public Object getAttribute(ObjectName name, String attribute)
            throws MBeanException, AttributeNotFoundException,
            InstanceNotFoundException, ReflectionException, IOException {
        return this._server.getAttribute(name, attribute);
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

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanCount()
     */
    public Integer getMBeanCount()
            throws IOException
    {
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
    /* do nothing */    
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
