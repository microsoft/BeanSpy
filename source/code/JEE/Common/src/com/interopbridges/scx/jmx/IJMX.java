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

/**
 * <p>
 * Interface for abstracting the interface to the JMX Store.
 * </p>
 * 
 * <p>
 * There basics of JMX are the same for our purposes, but there are slight
 * differences regarding setup and access that this interface is meant to
 * abstract.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public interface IJMX
{

    /**
     * <p>
     * Gets the value of a specific attribute of a named MBean. The MBean is
     * identified by its object name.
     * </p>
     * 
     * @param name
     *            The object name of the MBean from which the attribute is to be
     *            retrieved.
     * @param attribute
     *            A String specifying the name of the attribute to be retrieved.
     * 
     * @return The value of the retrieved attribute.
     * 
     * @throws MBeanException
     *             Wraps an exception thrown by the MBean's getter.
     * @throws AttributeNotFoundException
     *             The attribute specified is not accessible in the MBean.
     * @throws InstanceNotFoundException
     *             The MBean specified is not registered in the MBean server.
     * @throws ReflectionException
     *             Wraps a java.lang.Exception thrown when trying to invoke the
     *             setter.
     * @throws IOException
     *             throw if the given MBean fails an I/O operation
     */
    public Object getAttribute(ObjectName name, String attribute)
            throws MBeanException, AttributeNotFoundException,
            InstanceNotFoundException, ReflectionException, IOException;

    /**
     * Returns the number of MBeans registered in the MBean server.
     * 
     * @return the number of registered MBeans, wrapped in an Integer. If the
     *         caller's permissions are restricted, this number may be greater
     *         than the number of MBeans the caller can access.
     * @throws IOException
     *             throw if the given MBean fails an I/O operation
     * 
     */
    public Integer getMBeanCount() throws IOException;

    /**
     * <p>
     * This method discovers the attributes and operations that an MBean exposes
     * for management.
     * </p>
     * 
     * @param name
     *            The name of the MBean to analyze
     * @return An instance of MBeanInfo allowing the retrieval of all attributes
     *         and operations of this MBean.
     * @throws InstanceNotFoundException
     *             An exception occurred during introspection.
     * @throws IntrospectionException
     *             The MBean specified was not found.
     * @throws ReflectionException
     *             An exception occurred when trying to invoke the getMBeanInfo
     *             of a Dynamic MBean.
     * @throws IOException
     *             throw if the given MBean fails an I/O operation
     */
    public MBeanInfo getMBeanInfo(ObjectName name)
            throws InstanceNotFoundException, IntrospectionException,
            ReflectionException, IOException;

    /**
     * <p>
     * For some application servers (WebSphere / WebLogic) there are separate
     * JMX stores which must be connected to. For others (Tomcat) there is not a
     * unique store (it just uses the JDK); however, the application server
     * needs to be started in a unique way so that the desired MBeans are
     * available.
     * </p>
     * 
     * <p>
     * This function is for the later case because we need an abstraction to
     * represent whether the MBeans representing application server specifics
     * are available, but it is not desirable to have a separate store. Again,
     * in the Tomcat case the JDK store is used.
     * 
     * </p>
     * 
     * @return true if this is a standalone JMX store
     */
    public boolean isStandAloneJmxStore();

    /**
     * <p>
     * Gets the names of MBeans controlled by the MBean server. This method
     * enables any of the following to be obtained: The names of all MBeans, the
     * names of a set of MBeans specified by pattern matching on the ObjectName
     * and/or a Query expression, a specific MBean name (equivalent to testing
     * whether an MBean is registered). When the object name is null or no
     * domain and key properties are specified, all objects are selected (and
     * filtered if a query is specified). It returns the set of ObjectNames for
     * the MBeans selected.
     * </p>
     * 
     * @param name
     *            The object name pattern identifying the MBean names to be
     *            retrieved. If null oror no domain and key properties are
     *            specified, the name of all registered MBeans will be
     *            retrieved.
     * 
     * @param query
     *            The query expression to be applied for selecting MBeans. If
     *            null no query expression will be applied for selecting MBeans.
     * 
     * @return A set containing the ObjectNames for the MBeans selected. If no
     *         MBean satisfies the query, an empty set is returned.
     * @throws IOException
     *             throw if the given MBean fails an I/O operation
     */
    public Set<ObjectInstance> queryMBeans(ObjectName name, QueryExp query)
            throws IOException;

    /**
     * <p>
     * Gets the names of MBeans controlled by the MBean server. This method
     * enables any of the following to be obtained: The names of all MBeans, the
     * names of a set of MBeans specified by pattern matching on the ObjectName
     * and/or a Query expression, a specific MBean name (equivalent to testing
     * whether an MBean is registered). When the object name is null or no
     * domain and key properties are specified, all objects are selected (and
     * filtered if a query is specified). It returns the set of ObjectNames for
     * the MBeans selected.
     * </p>
     * 
     * @param name
     *            The object name pattern identifying the MBean names to be
     *            retrieved. If null or no domain and key properties are
     *            specified, the name of all registered MBeans will be
     *            retrieved.
     * @param query
     *            The query expression to be applied for selecting MBeans. If
     *            null no query expression will be applied for selecting MBeans.
     * @return A set containing the ObjectNames for the MBeans selected. If no
     *         MBean satisfies the query, an empty list is returned.
     * @throws IOException
     *             throw if the given MBean fails an I/O operation
     * 
     */
    public Set<ObjectName> queryNames(ObjectName name, QueryExp query)
            throws IOException;

    /**
     * <p>
     * Registers a given MBean with the JMX Store Abstraction.
     * </p>
     * 
     * @param bean
     *            the MBean to register
     * @param keys
     *            list of unique identifiers for the MBean
     * @throws InstanceAlreadyExistsException
     *             thrown if the given MBean is already registered with the
     *             store
     * @throws MBeanRegistrationException
     *             thrown if there is a general error with registering the MBean
     * @throws NotCompliantMBeanException
     *             throw if the given MBean fails some simple introspection
     *             checks
     * @throws IOException
     *             throw if the given MBean fails an I/O operation
     */
    public void registerMBean(Object bean, ObjectName keys)
            throws InstanceAlreadyExistsException, MBeanRegistrationException,
            NotCompliantMBeanException, IOException;

    /**
     * <p>
     * Self contained check that should be implemented if more than calling the
     * constructor is needed to verify that the connection to the JMX is valid.
     * </p>
     * 
     * @return true if the JMX store connection works for a stock query,
     *         otherwise false
     */
    public boolean verifyStoreConnection();

    /**
     * <p>
     * Getter method to return the underlying MBeanServers ID, this is primarily used to
	 * ascertain whether there are duplicate MBeanServers loaded.
     * </p>
     * 
     * @return the underlying MBeanServer object hashcode
     */
    public int getMBeanServerID();
    
    /**
     * <p>
     * Invoke a method on a given MBean residing in the JMX Store Abstraction.
     * </p>
     * 
     * @param name
     *            The object name of the MBean on which the method is to be invoked.
     * @param operationName
     *            The name of the operation to be invoked.
     * @param params
     *            An array containing the parameters to be set when the operation is invoked
     * @param signature
     *            An array containing the signature of the operation. 
     *            The class objects will be loaded using the same class loader 
     *            as the one used for loading the MBean on which the operation was invoked.
     * @throws InstanceNotFoundException
     *             An exception occurred during introspection.
     * @throws ReflectionException
     *             An exception occurred when trying to invoke the getMBeanInfo
     *             of a Dynamic MBean.
     * @throws MBeanException
     *             Wraps an exception thrown by the MBean's getter.
     * @throws IOException
     *             throw if the given MBean fails an I/O operation
     */
    public Object invoke(ObjectName name, String operationName, Object[] params, String[] signature)
            throws InstanceNotFoundException, ReflectionException, MBeanException, IOException;

}
