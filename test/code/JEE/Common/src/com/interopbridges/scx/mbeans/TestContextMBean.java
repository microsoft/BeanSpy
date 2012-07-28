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

package com.interopbridges.scx.mbeans;

import java.util.HashMap;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

/**
 * <p>
 * Simple MBean. The primary intention of this
 * class is to mock up a MBean that contains a class that is unknown by the remote
 * JMX client. 
 * This class is used in the unit tests for verifying that the MBeans can be represented as XML.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class TestContextMBean implements DynamicMBean
{
    /**
     * <p>
     * Attribute info for this MBean.
     * </p>
     */
    private MBeanAttributeInfo[] mbai;
    
    /**
     * <p>
     * Map of properties contain name,value pairs.
     * </p>
     */
    private HashMap<String, Object> properties;
    
    /**
     * <p>
     * Default constructor for the MBean.
     * All the Attribute info and properties are set in the constructor.
     * </p>
     */
    public TestContextMBean()
    {
 
        mbai = new MBeanAttributeInfo[]
          {
              new MBeanAttributeInfo("Name", String.class.getName(), "The name", true, true, false),
              new MBeanAttributeInfo("SimpleClass", SimpleClass.class.getName(), "SimpleClass name", true, true, false),
          };
        properties = new HashMap<String, Object>();
        properties.put("Name", "TestContextMBean");
        properties.put("SimpleClass", new SimpleClass());
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#getAttribute(java.lang.String)
     */
    public Object getAttribute(String attribute)
            throws AttributeNotFoundException, MBeanException,
            ReflectionException 
    {
        return properties.get(attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#getAttributes(java.lang.String[])
     */
    public AttributeList getAttributes(String[] attributes) 
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#getMBeanInfo()
     */
    public MBeanInfo getMBeanInfo() 
    {
        return new MBeanInfo("TestContextMBean",
                "Dummy Mean",mbai,null,null,null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#invoke(String, Object[], String[])
     */
    public Object invoke(String actionName, Object[] params, String[] signature)
            throws MBeanException, ReflectionException 
    {
        throw new UnsupportedOperationException();      
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#setAttribute(Attribute)
     */
    public void setAttribute(Attribute attribute)
            throws AttributeNotFoundException, InvalidAttributeValueException,
            MBeanException, ReflectionException 
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#setAttributes(AttributeList)
     */
    public AttributeList setAttributes(AttributeList attributes) 
    {
        return null;
    }
    
}

