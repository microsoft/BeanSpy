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

import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.loading.ClassLoaderRepository;

/**
 * <p>
 * Test class to emulate a javax.management.MBeanServer
 * </p>
 * 
 * <p>
 * This is a utility class that is required to test 
 * specific MBeans.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class AnotherMockMBeanServer implements javax.management.MBeanServer, Cloneable
{
    /**
     * <p>
     * Singleton instance of the MBean Server.
     * </p>
     */
    static AnotherMockMBeanServer _this=null;
    
    /**
     * <p>
     * Flag to be used to trigger a failure.
     * </p>
     */
    static boolean GenerateClassNotFoundExceptiononGetAttribute = false;
    
    /**
     * 'Simple' in-memory implementation of a MBean Server
     * The methods implemented are required by the JMX interface (IJMX)
     * certain other methods are implemented for test purposes.
     */
    private Hashtable<ObjectName, Object> _jmx;

    /**
     * Default Constructor, creates the in-memory MBean store.
     */
    private AnotherMockMBeanServer() {
        this._jmx = new Hashtable<ObjectName, Object>();
    }

    /**
     * Test utility to enable triggering of errors.
     */
    public void setExceptionFlag(boolean on)
    {
        GenerateClassNotFoundExceptiononGetAttribute = on;   
    }

    /**
     * Forcably destroy all instances.
     */
    public static void Reset_TestMBeanServer()
    {
        _this._jmx = new Hashtable<ObjectName, Object>();
    }

    /**
     * Singleton interface to retrieve an instance of the MBean store.
     */
    public static AnotherMockMBeanServer getInstance()
    {
        if (_this==null)
        {
            _this = new AnotherMockMBeanServer();
        }
        return _this;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getAttribute
     */
    public void addNotificationListener(ObjectName name,
            NotificationListener listener, NotificationFilter filter,
            Object handback) throws InstanceNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#addNotificationListener
     */
    public void addNotificationListener(ObjectName name, ObjectName listener,
            NotificationFilter filter, Object handback)
            throws InstanceNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean
     */
    public ObjectInstance createMBean(String className, ObjectName name)
            throws ReflectionException, InstanceAlreadyExistsException,
            MBeanRegistrationException, MBeanException,
            NotCompliantMBeanException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean
     */
    public ObjectInstance createMBean(String className, ObjectName name,
            ObjectName loaderName) throws ReflectionException,
            InstanceAlreadyExistsException, MBeanRegistrationException,
            MBeanException, NotCompliantMBeanException,
            InstanceNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean
     */
    public ObjectInstance createMBean(String className, ObjectName name,
            Object[] params, String[] signature) throws ReflectionException,
            InstanceAlreadyExistsException, MBeanRegistrationException,
            MBeanException, NotCompliantMBeanException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean
     */
    public ObjectInstance createMBean(String className, ObjectName name,
            ObjectName loaderName, Object[] params, String[] signature)
            throws ReflectionException, InstanceAlreadyExistsException,
            MBeanRegistrationException, MBeanException,
            NotCompliantMBeanException, InstanceNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize
     */
    public ObjectInputStream deserialize(ObjectName name, byte[] data)
            throws InstanceNotFoundException, OperationsException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize
     */
    public ObjectInputStream deserialize(String className, byte[] data)
            throws OperationsException, ReflectionException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize
     */
    public ObjectInputStream deserialize(String className,
            ObjectName loaderName, byte[] data)
            throws InstanceNotFoundException, OperationsException,
            ReflectionException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getAttribute
     */
    public Object getAttribute(ObjectName name, String attribute)
            throws MBeanException, AttributeNotFoundException,
            InstanceNotFoundException, ReflectionException 
    {
        if( GenerateClassNotFoundExceptiononGetAttribute )
        {
            throw new MBeanException(new ClassNotFoundException(attribute));
        }

        Object on = _jmx.get(name);
        if(on instanceof DynamicMBean)
        {
            DynamicMBean dn = (DynamicMBean)on;
            return dn.getAttribute(attribute);
        }
        else
        {
            throw new InstanceNotFoundException("Object not found :"+name);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getAttributes
     */
    public AttributeList getAttributes(ObjectName name, String[] attributes)
            throws InstanceNotFoundException, ReflectionException 
    {
        Object on = _jmx.get(name);
        if(on instanceof DynamicMBean)
        {
            DynamicMBean dn = (DynamicMBean)on;
            return dn.getAttributes(attributes);
        }
        else
        {
            throw new InstanceNotFoundException("Object not found :"+name);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getClassLoader
     */
    public ClassLoader getClassLoader(ObjectName loaderName)
            throws InstanceNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getClassLoaderFor
     */
    public ClassLoader getClassLoaderFor(ObjectName mbeanName)
            throws InstanceNotFoundException 
    {
        return this.getClass().getClassLoader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getClassLoaderRepository
     */
    public ClassLoaderRepository getClassLoaderRepository() 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getDefaultDomain
     */
    public String getDefaultDomain() 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getDomains
     */
    public String[] getDomains() 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getMBeanCount
     */
    public Integer getMBeanCount() 
    {
        return _jmx.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getMBeanInfo
     */
    public MBeanInfo getMBeanInfo(ObjectName name)
            throws InstanceNotFoundException, IntrospectionException,
            ReflectionException 
    {
        
        Object on = _jmx.get(name);
        if(on instanceof DynamicMBean)
        {
            DynamicMBean dn = (DynamicMBean)on;
            return dn.getMBeanInfo();
        }
        else
        {
            throw new InstanceNotFoundException("Object not found :"+name);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getObjectInstance
     */
    public ObjectInstance getObjectInstance(ObjectName name)
            throws InstanceNotFoundException 
            {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate
     */
    public Object instantiate(String className) throws ReflectionException,
            MBeanException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate
     */
    public Object instantiate(String className, ObjectName loaderName)
            throws ReflectionException, MBeanException,
            InstanceNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate
     */
    public Object instantiate(String className, Object[] params,
            String[] signature) throws ReflectionException, MBeanException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate
     */
    public Object instantiate(String className, ObjectName loaderName,
            Object[] params, String[] signature) throws ReflectionException,
            MBeanException, InstanceNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#invoke
     */
    public Object invoke(ObjectName name, String operationName,
            Object[] params, String[] signature)
            throws InstanceNotFoundException, MBeanException,
            ReflectionException 
    {
        Object on = _jmx.get(name);
        if(on instanceof DynamicMBean)
        {
            DynamicMBean dn = (DynamicMBean)on;
            return dn.invoke(operationName, params, signature);
        }
        else
        {
            throw new InstanceNotFoundException("Object not found :"+name);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#isInstanceOf
     */
    public boolean isInstanceOf(ObjectName name, String className)
            throws InstanceNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#isRegistered
     */
    public boolean isRegistered(ObjectName name) 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#queryMBeans
     */
    public Set<ObjectInstance> queryMBeans(ObjectName name, QueryExp query) 
    {
        Set<ObjectInstance> returnValue = new HashSet<ObjectInstance>();
        Set<ObjectName> searchSet = new HashSet<ObjectName>();
        
        if((name==null) || (name.getDomain()==null) || name.isDomainPattern())
        {
            searchSet = _jmx.keySet();
        }
        else
        {
            for(Iterator<ObjectName> it = _jmx.keySet().iterator();it.hasNext();)
            {
                ObjectName on = it.next();
                if(on.getDomain().compareTo(name.getDomain())==0)
                {
                    searchSet.add(on);
                }
            }
        }
        
        for(Iterator<ObjectName> it = searchSet.iterator();it.hasNext();)
        {
            ObjectName jmxName = it.next();
            returnValue.add(new ObjectInstance(jmxName, 
                        jmxName.getClass().getName()));
        }
        
        return returnValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#queryNames
     */
    public Set<ObjectName> queryNames(ObjectName name, QueryExp query) 
    {
        Set<ObjectInstance> oi = queryMBeans(name, query);
        Set<ObjectName> on = new HashSet<ObjectName>();
        for(Iterator<ObjectInstance> it = oi.iterator();it.hasNext();)
        {
            on.add(it.next().getObjectName());
        }
        return on;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#registerMBean
     */
    public ObjectInstance registerMBean(Object object, ObjectName name)
            throws InstanceAlreadyExistsException, MBeanRegistrationException,
            NotCompliantMBeanException 
    {
        ObjectInstance oi=null;
        try
        {
            oi = new ObjectInstance(name,object.getClass().getName()/*getMBeanInfo(name).getClassName()*/);
            _jmx.put(name, object);
        }
        catch(Exception e)
        {
           throw new MBeanRegistrationException(e);   
        }
        return oi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener
     */
    public void removeNotificationListener(ObjectName name, ObjectName listener)
            throws InstanceNotFoundException, ListenerNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener
     */
    public void removeNotificationListener(ObjectName name,
            NotificationListener listener) throws InstanceNotFoundException,
            ListenerNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener
     */
    public void removeNotificationListener(ObjectName name,
            ObjectName listener, NotificationFilter filter, Object handback)
            throws InstanceNotFoundException, ListenerNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener
     */
    public void removeNotificationListener(ObjectName name,
            NotificationListener listener, NotificationFilter filter,
            Object handback) throws InstanceNotFoundException,
            ListenerNotFoundException 
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#setAttribute
     */
    public void setAttribute(ObjectName name, Attribute attribute)
            throws InstanceNotFoundException, AttributeNotFoundException,
            InvalidAttributeValueException, MBeanException, ReflectionException 
    {
        Object on = _jmx.get(name);
        if(on instanceof DynamicMBean)
        {
            DynamicMBean dn = (DynamicMBean)on;
            dn.setAttribute(attribute);
        }
        else
        {
            throw new InstanceNotFoundException("Object not found :"+name);
        }
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#setAttributes
     */
    public AttributeList setAttributes(ObjectName name, AttributeList attributes)
            throws InstanceNotFoundException, ReflectionException 
    {
        Object on = _jmx.get(name);
        if(on instanceof DynamicMBean)
        {
            DynamicMBean dn = (DynamicMBean)on;
            return dn.setAttributes(attributes);
        }
        else
        {
            throw new InstanceNotFoundException("Object not found :"+name);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#unregisterMBean
     */
    public void unregisterMBean(ObjectName name)
            throws InstanceNotFoundException, MBeanRegistrationException 
    {
        _jmx.remove(name);
    }
}

