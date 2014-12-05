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
 * JBoss 4.2.1/5.1.0 Abstraction for the MX4J JMX Store
 * </p>
 * 
 */
public class JBossJMXAbstraction implements IJMX
{

    /**
     * <p>
     * Singleton Connection to the JMX Store
     * </p>
     */
    private MBeanServer _server;

    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    ILogger _logger;

    /**
     * <p>
     * Constructor. The constructor attempts to connect to the main JBoss JMX
     * MBean Store. No specific API calls are made, but instead the class
     * attempts to load the org.jboss.mx.util.MBeanServerLocator class. If the
     * class can be loaded it attempts to retrieve a MBean server.
     * The MBean server is used for all MBean related queries.
     * </p>
     * 
     * @param JMXClass
     *            A class name containing 'locateJBoss' method, 
     *            the 'locateJBoss' method returns a MBeanServer.
     *            
     * @throws ScxException
     *             The method call to getMBeanServer did not return an instance
     *             of a MBeanServer.
     */
    public JBossJMXAbstraction(String JMXClass) throws ScxException
    {
		JMXClass = determineJMXClass();
		String methodToInvoke = "locateJBoss";

        if(JMXClass == "java.lang.management.ManagementFactory")
		{
			methodToInvoke = "getPlatformMBeanServer";
		}

		_logger = LoggingFactory.getLogger();
        try
        {
	    ClassLoader classLoader = this.getClass().getClassLoader();
            
	    Class<?> aClass = classLoader
                    	.loadClass(JMXClass);
                
	    Method meth = aClass
         	.getDeclaredMethod(methodToInvoke,(Class<?>[]) null);
	    Object oBeanServer = meth.invoke(this, (Object[]) null);
	    
            if (oBeanServer instanceof MBeanServer)
            {
                _server = (MBeanServer) oBeanServer;
				
				if (JMXClass == "java.lang.management.ManagementFactory")
				{
					ObjectName name = new ObjectName("jboss.as:management-root=server");
					String serverState = (String) (_server.getAttribute(name, "processType"));
					if(!serverState.isEmpty() &&
						serverState.equals("Server"))
					{
						JmxConstant.IS_JBOSS7_WILDFLY = true;
					}
				}
            }
            else
            {
                throw new ScxException(
                        ScxExceptionCode.ERROR_MBEANSTORE_INSTANCE_INVALID);
            }
        }
        catch(Exception e)
        {
		   _logger.fine("The constructor of JBossJMXAbstraction class throws an exception: " + e.getCause());   
		   throw new ScxException(ScxExceptionCode.ERROR_CONNECTING_JMXSTORE);
		}

    }

    /**
     * <p>
     * Default Constructor. The constructor attempts to connect to the main JBoss JMX
     * MBean Store. No specific API calls are made, but instead the class
     * attempts to load the org.jboss.mx.util.MBeanServerLocator class. If the
     * class can be loaded it attempts to retrieve a MBean server.
     * </p>
     * 
     * @throws ScxException
     *             The method call to getMBeanServer did not return an instance
     *             of a MBeanServer.
     */
    public JBossJMXAbstraction() throws ScxException
    {
	this("org.jboss.mx.util.MBeanServerLocator");
    }

    /*
     * (non-Javadoc)
     * 
     * This function will determine which JBoss JMX MBean Store to connect to
	 * 
	 * The function will first try to connect to the JBoss 4/5/6 store and if failed
	 * will return the JBoss 7 and Wildfly store name.
     */
	public String determineJMXClass()
    {
		boolean tryJBossWildfly = false;
		ClassLoader classLoader = this.getClass().getClassLoader();

		String classToLoadJboss456     =   "org.jboss.mx.util.MBeanServerLocator";
		String classToLoadJbossWildfly = "java.lang.management.ManagementFactory";
	
		try
		{
			Class<?> aClass = classLoader
			.loadClass("org.jboss.mx.util.MBeanServerLocator");
		}
		catch (Exception e)
		{
			tryJBossWildfly = true;
		}

		if(tryJBossWildfly)
			return classToLoadJbossWildfly;
		else
			return classToLoadJboss456;
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
    public Integer getMBeanCount() throws IOException
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
            ReflectionException, IOException
    {
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
            NotCompliantMBeanException, IOException
    {
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
