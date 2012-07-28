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
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;

/**
 * <p>
 * Simple MBean that emulates a JBoss info MBean. The primary intention of this
 * class is to mock up a MBean that contains the serverVendor and serverVersion attributes. 
 * This class is used in the unit tests for verifying that the MBeans can be represented as XML.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class DummyWebSphereMBean implements DynamicMBean
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
     * Logger for the class.
     * </p>
     */
    private ILogger _logger;

    
    /**
     * <p>
     * Default constructor for the MBean.
     * All the Attribute info and properties are set in the constructor.
     * </p>
     */
    public DummyWebSphereMBean()
    {
        this._logger = LoggingFactory.getLogger();
 
        mbai = new MBeanAttributeInfo[]
          {
              new MBeanAttributeInfo("Name", String.class.getName(), "The name", true, true, false),
              new MBeanAttributeInfo("objectName", String.class.getName(), "Domain:jeeType=J2EEServer", true, true, false),
              new MBeanAttributeInfo("platformName", String.class.getName(), "platform name", true, true, false),
              new MBeanAttributeInfo("platformVersion", String.class.getName(), "platform version", true, true, false)
          };
        properties = new HashMap<String, Object>();
        properties.put("Name", this.getClass().getName());
        properties.put("objectName", "Domain:jeeType=J2EEServer");
        properties.put("platformName", "IBM WebSphere Application Server");
        properties.put("platformVersion", "99.99");
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
        AttributeList al = new AttributeList();
        for(int i=0;i<attributes.length;i++)
        {
            try
            {
                al.add(new Attribute(attributes[i],getAttribute(attributes[i])));
            }
            catch(Exception e)
            {
                this._logger.fine(new StringBuffer(
                "Failed to getAttribute for ").append(attributes[i])
                .toString());
            }
        }
        return al;
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#getMBeanInfo()
     */
    public MBeanInfo getMBeanInfo() 
    {
        return new MBeanInfo(this.getClass().getName(),
                "Dummy WebSphere MBean",mbai,null,null,null);
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
        Object ans = properties.get(attribute.getName());
        if(ans!=null)
        {
            properties.put(attribute.getName(), attribute.getValue());
        }
        else
        {
            throw new AttributeNotFoundException("No such property: " + attribute);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#setAttributes(AttributeList)
     */
    public AttributeList setAttributes(AttributeList attributes) 
    {
        List<Attribute> la = attributes.asList();
        AttributeList ret = new AttributeList();
        for(int i=0;i<la.size();i++)
        {
            
            Object ans = properties.get(la.get(i).getName());
            if(ans!=null)
            {
                try
                {
                    setAttribute(la.get(i));
                    ret.add(new Attribute(la.get(i).getName(), la.get(i).getValue()));
                }
                catch(Exception e)
                {
                    this._logger.fine(new StringBuffer(
                    "Failed to setAttribute for ").append(la.get(i).getName())
                    .toString());
                }
            }
        }
        return ret;
    }
    
}
