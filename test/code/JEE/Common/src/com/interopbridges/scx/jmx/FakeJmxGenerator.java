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

import java.util.Hashtable;

import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.jboss.management.j2ee.J2EEApplication;

import junit.framework.Assert;

import com.interopbridges.scx.mbeans.BasicTypeArrays;
import com.interopbridges.scx.mbeans.BasicTypeArraysMBean;
import com.interopbridges.scx.mbeans.BasicTypes;
import com.interopbridges.scx.mbeans.BasicTypesMBean;
import com.interopbridges.scx.mbeans.BasicTypesWrapper;
import com.interopbridges.scx.mbeans.BasicTypesWrapperClass;
import com.interopbridges.scx.mbeans.BasicTypesWrapperClassMBean;
import com.interopbridges.scx.mbeans.BasicTypesWrapperMBean;
import com.interopbridges.scx.mbeans.ComplexType;
import com.interopbridges.scx.mbeans.ComplexTypeForMaxFileSize;
import com.interopbridges.scx.mbeans.ComplexTypeMBean;
import com.interopbridges.scx.mbeans.EscapedObjectName;
import com.interopbridges.scx.util.StringMangler;
import com.interopbridges.scx.webservices.Endpoint;
import com.interopbridges.scx.webservices.EndpointMBean;
import com.interopbridges.scx.webservices.Operation;
import com.interopbridges.scx.webservices.OperationCall;
import com.interopbridges.scx.webservices.OperationMBean;

/**
 * <p>
 * Utility class to generate various pieces JMX information that one would
 * expect to pull from the JMX Store.
 * </p>
 * 
 * <p>
 * This class can be used with the MockJmx class to re-constituted a JMX Store
 * for test purposes.
 * </p>
 * 
 * 
 * @author Christopher Crammond
 * 
 */
public class FakeJmxGenerator
{

    private final static String _domain = "com.interopbridges.scx";

    /**
     * <p>
     * Helper/Fixture function for returning a standard Operation MBean for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return An EndpointMBean representing an 'add' endpoint
     */
    public static EndpointMBean getAddEndpointMBean()
    {
        return new Endpoint(
                "http://localhost:9080/WebServiceProject/CalculatorService");
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard Operation MBean for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return An OperationMBean representing an 'add' operation
     */
    public static OperationMBean getAddOperationMBean()
    {
        return new Operation("add");
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard MBean that consists of
     * multiple properties
     * </p>
     * 
     * @return An PrimativeTypesMBean representing an MBean that has nothing but
     *         primitive data types
     */
    public static BasicTypesMBean getBasicTypesMBean()
    {
        return new BasicTypes();
    }

    /**
     * <p>
     * Helper/Fixture function for returning a fake MBean that looks like a
     * JBoss MBeans for Web Applications
     * </p>
     * 
     * @return a special constructed J2EE Application class that behaves like
     *         the real class
     */
    public static org.jboss.management.j2ee.J2EEApplication getFauxJBossWebApplicationMBean()
    {
        return new org.jboss.management.j2ee.J2EEApplication();
    }

    /**
     * <p>
     * Helper/Fixture function for returning a fake MBean that has
     * escaped characters in the objectname
     * </p>
     * 
     * @return a EscapedObjectNameMBean containing escaped characters in the ObjectName property
     */
    public static EscapedObjectName getEscapedObjectNameMBean()
    {
        return new EscapedObjectName();
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard MBean that consists of
     * multiple properties
     * </p>
     * 
     * @return An PrimativeTypesMBean representing an MBean that has nothing but
     *         primitive data type objects
     */
    public static BasicTypesWrapperClassMBean getBasicTypesWrapperClassMBean()
    {
        return new BasicTypesWrapperClass();
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard MBean that consists of
     * multiple properties
     * </p>
     * 
     * @return An ComplexTypesMBean representing an MBean that has complex data
     *         type objects
     */
    public static ComplexTypeMBean getComplexTypeMBean()
    {
        return new ComplexType();
    }

    /**
     *<p>
     * Modified from getComplexTypeMBean()
     * </p>
     *
     * <p>
     * Instead of return a ComplexType MBean, it return a complexTypeMBeans
     * with much more data so it can be uesd to generate a sized XML file for
     * the test case when the limit of a XML file is reached.
     * </p>
     */    
    public static ComplexTypeMBean getComplexTypeMBeanforMaxFileSize()
    {     
        return new ComplexTypeForMaxFileSize();
    }
    
    /**
     * <p>
     * Helper/Fixture function for returning a standard MBean that consists of
     * multiple properties
     * </p>
     * 
     * @return An PrimativeTypesMBean representing an MBean that has nothing but
     *         primitive data types
     */
    public static BasicTypeArraysMBean getBasicTypeArraysMBean()
    {
        return new BasicTypeArrays();
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard MBean that consists of
     * multiple properties
     * </p>
     * 
     * @return An PrimativeTypesMBean representing an MBean that has nothing but
     *         primitive data types
     */
    public static BasicTypesWrapperMBean getBasicTypesWrapperMBean()
    {
        return new BasicTypesWrapper();
    }

    /**
     * 
     * <p>
     * Helper/Fixture function for returning a standard Operation MBean for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return An ObjectInstance representing a 'add' Operation
     */
    public static ObjectInstance getAddOperationObjectInstance()
    {
        ObjectName o = null;
        try
        {
            Hashtable<String, String> properties = new Hashtable<String, String>(
                    4);
            properties.put("name", StringMangler
                    .EncodeForJmx(getAddOperationMBean().getName()));
            properties.put("jmxType", new Operation().getJmxType());
            o = new ObjectName(_domain, properties);

        }
        catch (Exception e)
        {
            Assert.fail("'Add' Operation ObjectInstance could not be created. "
                    + e.getMessage());
        }
        return new ObjectInstance(o, new Operation().getClass().getName());
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard Operation MBean for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return An EndpointMBean representing an 'divide' endpoint
     */
    public static EndpointMBean getDivideEndpointMBean()
    {
        return new Endpoint(
                "http://localhost:9080/WebServiceProject/CalculatorService");
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard Operation MBean for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return An OperationMBean representing an 'divide' operation
     */
    public static OperationMBean getDivideOperationMBean()
    {
        return new Operation("divide");
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard OperationCall MBean for
     * use in multiple tests. This method is meant to standardize and streamline
     * the test setup phase.
     * </p>
     * 
     * @return A 'divide-to-subtract' OperationCall ObjectInstance
     */
    public static ObjectInstance getDivideToSubtractOperationCallObjectInstance()
    {
        ObjectName o = null;
        try
        {
            Hashtable<String, String> properties = new Hashtable<String, String>(
                    4);
            properties.put("sourceEndpoint", StringMangler
                    .EncodeForJmx(getDivideEndpointMBean().getUrl()));
            properties.put("sourceOperation", getDivideOperationMBean()
                    .getName());
            properties.put("targetEndpoint", StringMangler
                    .EncodeForJmx(getSubtractEndpointMBean().getUrl()));
            properties.put("targetOperation", getSubtractOperationMBean()
                    .getName());
            properties.put("jmxType", new OperationCall().getJmxType());
            o = new ObjectName(_domain, properties);
        }
        catch (Exception e)
        {
            Assert
                    .fail("'Divide-Subtract' OperationCall ObjectInstance could not be created. "
                            + e.getMessage());
        }
        return new ObjectInstance(o, new OperationCall().getClass().getName());
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard ObjectInstance for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return An ObjectInstance representing a 'divide' Operation
     */
    public static ObjectInstance getDivideOperationObjectInstance()
    {
        ObjectName o = null;
        try
        {
            Hashtable<String, String> properties = new Hashtable<String, String>(
                    4);
            properties.put("name", StringMangler
                    .EncodeForJmx(getDivideOperationMBean().getName()));
            properties.put("jmxType", new Operation().getJmxType());
            o = new ObjectName(_domain, properties);

        }
        catch (Exception e)
        {
            Assert
                    .fail("'Divide' Operation ObjectInstance could not be created. "
                            + e.getMessage());
        }
        return new ObjectInstance(o, new Operation().getClass().getName());
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard Operation MBean for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return An EndpointMBean representing an 'add' operation
     */
    public static EndpointMBean getMultiplyEndpointMBean()
    {
        return new Endpoint(
                "http://localhost:9080/WebServiceProject/CalculatorService");
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard Operation MBean for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return An OperationMBean representing an 'multiple' endpoint
     */
    public static OperationMBean getMultiplyOperationMBean()
    {
        return new Operation("multiply");
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard ObjectInstance for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return A 'multiply-to-add' OperationCall ObjectInstance
     */
    public static ObjectInstance getMultiplyToAddOperationCallObjectInstance()
    {
        ObjectName o = null;
        try
        {
            Hashtable<String, String> properties = new Hashtable<String, String>(
                    4);
            properties.put("sourceEndpoint", StringMangler
                    .EncodeForJmx(getMultiplyEndpointMBean().getUrl()));
            properties.put("sourceOperation", getMultiplyOperationMBean()
                    .getName());
            properties.put("targetEndpoint", StringMangler
                    .EncodeForJmx(getAddEndpointMBean().getUrl()));
            properties.put("targetOperation", getAddOperationMBean().getName());
            properties.put("jmxType", new OperationCall().getJmxType());
            o = new ObjectName(_domain, properties);
        }
        catch (Exception e)
        {
            Assert
                    .fail("Creation of 'Divide-Subtract' OperationCall ObjectInstance could not be created. "
                            + e.getMessage());
        }
        return new ObjectInstance(o, new OperationCall().getClass().getName());
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard ObjectInstance for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return A 'BasicTypes' ObjectInstance
     */
    public static ObjectInstance getBasicTypesObjectInstance()
    {
        ObjectName o = null;
        try
        {
            Hashtable<String, String> properties = new Hashtable<String, String>(
                    1);
            properties.put("isBoolean", Boolean.toString((getBasicTypesMBean()
                    .getIsBoolean())));
            properties.put("byte", Byte
                    .toString(getBasicTypesMBean().getByte()));
            properties.put("charLetter", String.valueOf(getBasicTypesMBean()
                    .getCharLetter()));
            properties.put("doubleNumber", Double
                    .toString((getBasicTypesMBean().getDoubleNumber())));
            properties.put("floatNumber", Float.toString((getBasicTypesMBean()
                    .getFloatNumber())));
            properties.put("intNumber", Integer.toString(getBasicTypesMBean()
                    .getIntNumber()));
            properties.put("longNumber", Long.toString(getBasicTypesMBean()
                    .getLongNumber()));
            properties.put("shortNumber", Short.toString(getBasicTypesMBean()
                    .getShortNumber()));
            properties.put("theLabel", getBasicTypesMBean().getTheLabel());
            o = new ObjectName(_domain, properties);
        }
        catch (Exception e)
        {
            Assert
                    .fail("Creation of 'BasicTypes' ObjectInstance could not be created. "
                            + e.getMessage());
        }
        return new ObjectInstance(o, new BasicTypes().getClass().getName());
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard ObjectInstance for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return A 'BasicTypesWrapperClass' ObjectInstance
     */
    public static ObjectInstance getBasicTypesWrapperClassObjectInstance()
    {
        ObjectName o = null;
        try
        {
            Hashtable<String, String> properties = new Hashtable<String, String>(
                    1);
            properties.put("boolean", Boolean
                    .toString((getBasicTypesWrapperClassMBean().getBoolean())));
            properties.put("byte", Byte
                    .toString(getBasicTypesWrapperClassMBean().getByte()));
            properties.put("character", String
                    .valueOf(getBasicTypesWrapperClassMBean().getCharacter()));
            properties.put("double", Double
                    .toString((getBasicTypesWrapperClassMBean().getDouble())));
            properties.put("float", Float
                    .toString((getBasicTypesWrapperClassMBean().getFloat())));
            properties.put("integer", Integer
                    .toString(getBasicTypesWrapperClassMBean().getInteger()));
            properties.put("long", Long
                    .toString(getBasicTypesWrapperClassMBean().getLong()));
            properties.put("short", Short
                    .toString(getBasicTypesWrapperClassMBean().getShort()));
            properties.put("theLabel", getBasicTypesWrapperClassMBean()
                    .getTheLabel());
            o = new ObjectName(_domain, properties);
        }
        catch (Exception e)
        {
            Assert
                    .fail("Creation of 'BasicTypesWrapperClass' ObjectInstance could not be created. "
                            + e.getMessage());
        }
        return new ObjectInstance(o, new BasicTypesWrapperClass().getClass()
                .getName());
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard ObjectInstance for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return A 'ComplexType' ObjectInstance
     */
    public static ObjectInstance getComplexTypeObjectInstance()
    {
        ObjectName o = null;
        try
        {
            Hashtable<String, String> properties = new Hashtable<String, String>(
                    1);
            properties.put("theLabel", getComplexTypeMBean().getTheLabel());
            properties.put("complexitem", getComplexTypeMBean()
                    .getComplexClass().toString());
            o = new ObjectName(_domain, properties);
        }
        catch (Exception e)
        {
            Assert
                    .fail("Creation of 'ComplexType' ObjectInstance could not be created. "
                            + e.getMessage());
        }
        return new ObjectInstance(o, new ComplexType().getClass().getName());
    }

    /**
     *
     * <p>
     * Helper/Fixture function for returning a standard ObjectInstance for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     *
     * <P>
     * Modified from getComplexTypeObjectInstance() for the test cases 
     * when the size limit of a XML file is reached.
     * </P>
     *
     * @return A 'ComplexTypeForMaxFileSize' ObjectInstance
     * 
     */

    public static ObjectInstance getComplexTypeObjectInstanceForMaxFileSize()
    {
        ObjectName o = null;
        try
        {
            Hashtable<String, String> properties = new Hashtable<String, String>(1);
        
           
            properties.put("theLabel", getComplexTypeMBeanforMaxFileSize().getTheLabel());            
            properties.put("complexitem", getComplexTypeMBeanforMaxFileSize()
                    .getComplexClass().toString());            
            o = new ObjectName(_domain, properties);
        }
        catch (Exception e)
        {
            Assert
                    .fail("Creation of 'ComplexType' ObjectInstance could not be created. "
                            + e.getMessage());
        }
        
        return new ObjectInstance(o, new ComplexTypeForMaxFileSize().getClass().getName());
    }
    
    /**
     * <p>
     * Helper/Fixture function for returning a standard ObjectInstance for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return A 'BasicTypeArrays' ObjectInstance
     */
    public static ObjectInstance getBasicTypeArraysObjectInstance()
    {
        ObjectName o = null;
        try
        {
            Hashtable<String, String> properties = new Hashtable<String, String>(
                    1);
            properties.put("theLabel", getBasicTypeArraysMBean().getTheLabel());
            o = new ObjectName(_domain, properties);
        }
        catch (Exception e)
        {
            Assert
                    .fail("Creation of 'BasicTypeArrays' ObjectInstance could not be created. "
                            + e.getMessage());
        }
        return new ObjectInstance(o, new BasicTypeArrays().getClass().getName());
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard ObjectInstance for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return A 'BasicTypesWrapper' ObjectInstance
     */
    public static ObjectInstance getBasicTypesWrapperObjectInstance()
    {
        ObjectName o = null;
        try
        {
            Hashtable<String, String> properties = new Hashtable<String, String>(
                    1);
            properties.put("theLabel", "BasicTypesWrapperMBean");
            properties.put("payload", getBasicTypesWrapperMBean().getPayload()
                    .toString());
            o = new ObjectName(_domain, properties);
        }
        catch (Exception e)
        {
            Assert
                    .fail("Creation of 'BasicTypesWrapper' ObjectInstance could not be created. "
                            + e.getMessage());
        }
        return new ObjectInstance(o, new BasicTypesWrapper().getClass()
                .getName());
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard ObjectInstance for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return A 'JBoss Web Application' ObjectInstance
     */
    public static ObjectInstance getFauxJBossManagementMBeanObjectInstance()
    {
        ObjectName o = null;
        try
        {
            o = new ObjectName(getFauxJBossWebApplicationMBean().getObjectName());
        }
        catch (Exception e)
        {
            Assert
                    .fail("Creation of 'JBoss Web Application' ObjectInstance could not be created. "
                            + e.getMessage());
        }
        return new ObjectInstance(o, new J2EEApplication().getClass().getName());
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard ObjectInstance for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return A 'EscapedObjectName' ObjectInstance
     */
    public static ObjectInstance getEscapedObjectNameObjectInstance()
    {
        ObjectName o = null;
        try
        {
            o = new ObjectName(getEscapedObjectNameMBean().getObjectName());
        }
        catch (Exception e)
        {
            Assert
                    .fail("Creation of 'EscapedObjectName MBean' ObjectInstance could not be created. "
                            + e.getMessage());
        }
        return new ObjectInstance(o, new EscapedObjectName().getClass().getName());
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard Operation MBean for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return An EndpointMBean representing an 'subtract' endpoint
     */
    public static EndpointMBean getSubtractEndpointMBean()
    {
        return new Endpoint(
                "http://localhost:9080/WebServiceProject/CalculatorService");
    }

    /**
     * <p>
     * Helper/Fixture function for returning a standard Operation MBean for use
     * in multiple tests. This method is meant to standardize and streamline the
     * test setup phase.
     * </p>
     * 
     * @return An OperationMBean representing an 'add' operation
     */
    public static OperationMBean getSubtractOperationMBean()
    {
        return new Operation("subtract");
    }

}
