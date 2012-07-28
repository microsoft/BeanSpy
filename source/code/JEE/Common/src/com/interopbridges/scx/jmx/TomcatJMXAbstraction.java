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

import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.util.JmxConstant;

/**
 * <p>
 * Abstraction representing the Tomcat JMX store.
 * </p>
 * 
 * <p>
 * In reality, there is not an actual Tomcat JMX store. Tomcat (5.5) must be
 * started with a special invocation so that the MBeans representing WebModules
 * and Servlets are exposed in the MBean store.
 * </p>
 * 
 * <p>
 * Because there is no actual Tomcat MBeans store, it is expected that
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class TomcatJMXAbstraction implements IJMX
{
    /**
     * <p>
     * Well-defined query that should return at least one (BeanSpy is
     * deployed if this query is run) entry. If the result of the JMX query is
     * zero, then it is likely that the store is not properly configured.
     * </p>
     */
    private final String _stockMBeanQuery = "Catalina:*";

    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    protected ILogger    _logger;

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
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanServerID()
     */
    public int getMBeanServerID()
    {
        return JmxConstant.INVALID_MBEANSERVER_HASHCODE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanCount()
     */
    public Integer getMBeanCount() throws IOException
    {
        return null;
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
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#isStandAloneJmxStore()
     */
    public boolean isStandAloneJmxStore()
    {
        return false;
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
        return null;
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
            NotCompliantMBeanException, IOException
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#verifyStoreConnection()
     */
    public boolean verifyStoreConnection()
    {
        /*
         * Unlike the other application servers, just connecting to the JMX
         * store is not enough. For Tomcat 5.5, the web application server needs
         * to be started with a specific invocation.
         */
        boolean doesStoreExposeDesiredMBeans = false;
        JdkJMXAbstraction jmxstore = new JdkJMXAbstraction();
        ObjectName query;
        try
        {
            query = new ObjectName(_stockMBeanQuery);
            Set<ObjectInstance> results = jmxstore.queryMBeans(query, null);
            doesStoreExposeDesiredMBeans = results.size() > 0;
        }
        catch (Exception e)
        {
            /*
             * This catch clause is greedy because the general expectation is
             * that this block should not be hit. A more likely scenario is that
             * the query returns zero items because Tomcat was not started with
             * the proper security constraints.
             */
            _logger
                    .fine("Unexpected error for verifying the Tomcat JMX store connection: "
                            + e.getMessage());
        }

        return doesStoreExposeDesiredMBeans;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#invoke(ObjectName, String, Object[], String[])
     */
    public Object invoke(ObjectName name, String operationName, Object[] params, String[] signature)
    throws InstanceNotFoundException, ReflectionException, MBeanException, IOException
    {
        return null;
    }
}
