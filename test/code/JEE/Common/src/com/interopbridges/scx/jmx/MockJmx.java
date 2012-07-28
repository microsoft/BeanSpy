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

import java.util.HashSet;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;
import java.util.Map.Entry;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

import org.jboss.management.j2ee.J2EEApplication;

import junit.framework.Assert;

import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.mbeans.BasicTypeArrays;
import com.interopbridges.scx.mbeans.BasicTypeArraysMBean;
import com.interopbridges.scx.mbeans.BasicTypes;
import com.interopbridges.scx.mbeans.BasicTypesWrapper;
import com.interopbridges.scx.mbeans.BasicTypesWrapperClass;
import com.interopbridges.scx.mbeans.BasicTypesWrapperMBean;
import com.interopbridges.scx.mbeans.ComplexType;
import com.interopbridges.scx.mbeans.ComplexTypeForMaxFileSize;
import com.interopbridges.scx.mbeans.EscapedObjectName;
import com.interopbridges.scx.webservices.Operation;
import com.interopbridges.scx.webservices.OperationCall;

/**
 * <p>
 * Mock implementation of the JMX Abstraction interface to generically use for
 * testing.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class MockJmx implements IJMX {

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
     * Default Constructor
     */
    public MockJmx() {
        this._jmx = new Hashtable<ObjectName, Object>();
        this._logger = LoggingFactory.getLogger();
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
         * The present simple implementation implies that the desired attribute
         * should be able to be parsed from the input ObjectName
         */

        _logger.finer("GetAttributes: ObjectName: "
                + name.getCanonicalKeyPropertyListString());
        _logger.finer("GetAttributes: Attribute: " + attribute);
        Object query = this._jmx.get(name);

        Assert.assertNotNull("Unable to find specified value: "
                + name.getCanonicalKeyPropertyListString(), query);

        Object returnValue = null;
        if (BasicTypes.class == query.getClass()) {
            BasicTypes temp = (BasicTypes) query;
            if (attribute.equals("theLabel")) {
                returnValue = temp.getTheLabel();
            } else if (attribute.equals("isBoolean")) {
                returnValue = temp.getIsBoolean();
            } else if (attribute.equals("byte")) {
                returnValue = temp.getByte();
            } else if (attribute.equals("charLetter")) {
                returnValue = temp.getCharLetter();
            } else if (attribute.equals("doubleNumber")) {
                returnValue = temp.getDoubleNumber();
            } else if (attribute.equals("floatNumber")) {
                returnValue = temp.getFloatNumber();
            } else if (attribute.equals("intNumber")) {
                returnValue = temp.getIntNumber();
            } else if (attribute.equals("longNumber")) {
                returnValue = temp.getLongNumber();
            } else if (attribute.equals("shortNumber")) {
                returnValue = temp.getShortNumber();
            } else {
                Assert.fail("implement for " + attribute);
            }
        } 
        else if (BasicTypesWrapperClass.class == query.getClass()) {
            BasicTypesWrapperClass temp = (BasicTypesWrapperClass) query;
            if (attribute.equals("theLabel")) {
                returnValue = temp.getTheLabel();
            } else if (attribute.equals("boolean")) {
                returnValue = temp.getBoolean();
            } else if (attribute.equals("byte")) {
                returnValue = temp.getByte();
            } else if (attribute.equals("character")) {
                returnValue = temp.getCharacter();
            } else if (attribute.equals("double")) {
                returnValue = temp.getDouble();
            } else if (attribute.equals("float")) {
                returnValue = temp.getFloat();
            } else if (attribute.equals("integer")) {
                returnValue = temp.getInteger();
            } else if (attribute.equals("long")) {
                returnValue = temp.getLong();
            } else if (attribute.equals("short")) {
                returnValue = temp.getShort();
            } else {
                Assert.fail("implement for " + attribute);
            }
        } 
        else if (ComplexType.class == query.getClass()) {
            ComplexType temp = (ComplexType) query;
            if (attribute.equals("theLabel")) {
                returnValue = temp.getTheLabel();
            } else if (attribute.equals("complexitem")) {
                returnValue = temp.getComplexClass();
            } else if (attribute.equals("complexitemarray")) {
                returnValue = temp.getComplexClassArray();
            } else {
                Assert.fail("implement for " + attribute);
            }
        }
        else if (ComplexTypeForMaxFileSize.class == query.getClass()) {
            ComplexTypeForMaxFileSize temp = (ComplexTypeForMaxFileSize) query;
            if (attribute.equals("theLabel")) {
                returnValue = temp.getTheLabel();
            } else if (attribute.equals("complexitem")) {
                returnValue = temp.getComplexClass();
            } else if (attribute.equals("complexitemarray")) {
                returnValue = temp.getComplexClassArray();
            } else {
                Assert.fail("implement for " + attribute);
            }
        } 
        else if (BasicTypeArrays.class == query.getClass()) {
            BasicTypeArraysMBean temp = (BasicTypeArraysMBean) query;
            if (attribute.equals("theLabel")) {
                returnValue = temp.getTheLabel();
            } else if (attribute.equals("isBoolean")) {
                returnValue = temp.getIsBoolean();
            } else if (attribute.equals("byte")) {
                returnValue = temp.getByte();
            } else if (attribute.equals("charLetter")) {
                returnValue = temp.getCharLetter();
            } else if (attribute.equals("doubleNumber")) {
                returnValue = temp.getDoubleNumber();
            } else if (attribute.equals("floatNumber")) {
                returnValue = temp.getFloatNumber();
            } else if (attribute.equals("intNumber")) {
                returnValue = temp.getIntNumber();
            } else if (attribute.equals("longNumber")) {
                returnValue = temp.getLongNumber();
            } else if (attribute.equals("shortNumber")) {
                returnValue = temp.getShortNumber();
            } else if (attribute.equals("stringArray")) {
                returnValue = temp.getStringArray();
            } else {
                Assert.fail("implement for " + attribute);
            }
        } else if (BasicTypesWrapper.class == query.getClass()) {
            BasicTypesWrapperMBean temp = (BasicTypesWrapperMBean) query;
            if (attribute.equals("theLabel")) {
                returnValue = temp.getTheLabel();
            } else {
                /*
                 * Assume that this is the payload
                 */
                returnValue = temp.getPayload();
            }
        } else if (Operation.class == query.getClass()) {
            returnValue = name.getKeyProperty(attribute);
        } else if (OperationCall.class == query.getClass()) {
            OperationCall temp = (OperationCall) query;
            if (attribute.equals("jmxType")) {
                returnValue = temp.getJmxType();
            } else if (attribute.equals("sourceEndpoint")) {
                returnValue = temp.getSourceEndpoint();
            } else if (attribute.equals("sourceOperation")) {
                returnValue = temp.getSourceOperation();
            } else if (attribute.equals("targetEndpoint")) {
                returnValue = temp.getTargetEndpoint();
            } else if (attribute.equals("targetOperation")) {
                returnValue = temp.getTargetOperation();
            } else {
                Assert.fail("OperationCall property not implemented");
            }
        }
        else if (J2EEApplication.class == query.getClass())
        {
            J2EEApplication temp = (J2EEApplication) query;
            if (attribute.equals("objectName"))
            {
                returnValue = temp.getObjectName();
            }
            else if (attribute.equals("eventProvider"))
            {
                returnValue = temp.getEventProvider();
            }
            else if (attribute.equals("statisticsProvider"))
            {
                returnValue = temp.getStatisticsProvider();
            }
            else
            {
                Assert.fail("J2EEApplication property not implemented: "
                        + attribute);
            }       
        } else  if (EscapedObjectName.class == query.getClass())
        {
            EscapedObjectName temp = (EscapedObjectName) query;
            if (attribute.equals("objectName"))
            {
                returnValue = temp.getObjectName();
            }
            else if (attribute.equals("theLabel")) {
                returnValue = temp.getTheLabel();
            }
            else
            {
                Assert.fail("EscapedObjectName property not implemented: "
                        + attribute);
            }       
        } else 
        {
            Assert.fail("Unhandled case for " + query.getClass().getName());
        }

        return returnValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanServerID()
     */
    public int getMBeanServerID()
    {
        return this.hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanCount()
     */
    public Integer getMBeanCount() {
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
        
        if(new J2EEApplication().getObjectName().equals(name.getCanonicalName()))
        {
            return this.getJBossWebApplicationMBeanInfo();
        }
        
        if(new EscapedObjectName().getObjectName().equals(name.getCanonicalName()))
        {
            return this.getEscapedObjectNameMBeanInfo();
        }
        
        
        // ObjectName was not specific, so try to guess from a name/key
        // from the ObjectName itself
        
        String desiredJmxType = name.getKeyProperty("jmxType");
        if (null == desiredJmxType) {
            /*
             * The simple implementation of the Mock JMX is expected to have a
             * limited set of supported MBeans. The assumption right now is that
             * of the list consists only of Operation, OperationCall, BasicType,
             * BasicTypeArrays, and BasicTypesWrapper MBeans.
             */
            _logger.finer("desired Jmx Type: null");

            String desiredLabel = name.getKeyProperty("theLabel");
            Assert.assertNotNull(desiredLabel);
            if ("basicTypes".equals(desiredLabel)) {
                return this.getBasicTypesMBeanInfo();
            } else if ("basicTypesWrapperClass".equals(desiredLabel)) {
                return this.getBasicTypesWrapperClassMBeanInfo();
            } else if ("ComplexType".equals(desiredLabel)) {
                return this.getComplexTypeMBeanInfo();
            } else if ("ComplexTypeForMaxFileSize".equals(desiredLabel)) {
                return this.getComplexTypeMBeanInfo();
            } else if ("basicTypeArrays".equals(desiredLabel)) {
                return this.getBasicTypeArraysMBeanInfo();
            } else if ("BasicTypesWrapperMBean".equals(desiredLabel)) {
                return this.getBasicTypesWrapperMBeanInfo();
            } else {
                Assert.fail("Unhandled Case for " + desiredJmxType);
                return null;
            }

        } else {
            // Old-school style of the Mock JMX (pre-May 2010 reset expected
            // that MBeans would have a JMX Type attribute for the MS specific
            // attribute
            _logger.finer("given object name: "
                    + name.getCanonicalKeyPropertyListString());
            _logger.finer("desired Jmx Type: " + desiredJmxType);

            // Note: this is a very simple implementation and should be
            // changed to meet whatever needs you may have.
            if (desiredJmxType.equals(new Operation().getJmxType()))
                return this.getOperationMBeanInfo();
            else
                return this.getOperationCallMBeanInfo();
        }
    }

    /**
     * <p>
     * Access the internal data structure and return a the given MBean
     * meta-data. Note that this implementation only works for 'simple'
     * scenarios and will likely need to be refactored for complex test-cases.
     * </p>
     * 
     * @return Meta-data for the requested BasicTypesMBean
     */
    protected MBeanInfo getBasicTypesMBeanInfo() {
        String className = "com.interopbridges.scx.mbeans.BasicTypesMBean";
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[9];
        attributes[0] = new MBeanAttributeInfo("theLabel", "java.lang.String",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[1] = new MBeanAttributeInfo("isBoolean", "boolean",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[2] = new MBeanAttributeInfo("byte", "byte", "description", /* isReadable */
        true, /* isWritable */false, /* isIs */false);
        attributes[3] = new MBeanAttributeInfo("charLetter", "char",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[4] = new MBeanAttributeInfo("doubleNumber", "double",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[5] = new MBeanAttributeInfo("floatNumber", "float",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[6] = new MBeanAttributeInfo("intNumber", "int",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[7] = new MBeanAttributeInfo("longNumber", "long",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[8] = new MBeanAttributeInfo("shortNumber", "short",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);

        MBeanInfo mBeanInfo = new MBeanInfo(className, "n/a", /* attributes */
        attributes, /* constructors */null, /* operations */null, /* notifications */
        null);
        return mBeanInfo;
    }

    /**
     * <p>
     * Access the internal data structure and return a the given MBean
     * meta-data. Note that this implementation only works for 'simple'
     * scenarios and will likely need to be refactored for complex test-cases.
     * </p>
     * 
     * @return Meta-data for the requested BasicTypesMBean
     */
    protected MBeanInfo getComplexTypeMBeanInfo() {
        String className = "com.interopbridges.scx.mbeans.ComplexTypeMBean";
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[3];
        attributes[0] = new MBeanAttributeInfo("complexitem", "ComplexClass",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[1] = new MBeanAttributeInfo("complexitemarray", "ComplexClassArray",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[2] = new MBeanAttributeInfo("theLabel", "java.lang.String",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);

        MBeanInfo mBeanInfo = new MBeanInfo(className, "n/a", /* attributes */
        attributes, /* constructors */null, /* operations */null, /* notifications */
        null);
        return mBeanInfo;
    }
        
    /**
     * <p>
     * Access the internal data structure and return a the given MBean
     * meta-data. Note that this implementation only works for 'simple'
     * scenarios and will likely need to be refactored for complex test-cases.
     * </p>
     * 
     * @return Metadata for the requested BasicTypesMBean
     */
    protected MBeanInfo getBasicTypeArraysMBeanInfo() {
        String className = "com.interopbridges.scx.mbeans.BasicTypeArraysMBean";
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[10];
        attributes[0] = new MBeanAttributeInfo("theLabel", "java.lang.String",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[1] = new MBeanAttributeInfo("isBoolean", "[Z",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[2] = new MBeanAttributeInfo("byte", "[B", "description", /* isReadable */
        true, /* isWritable */false, /* isIs */false);
        attributes[3] = new MBeanAttributeInfo("charLetter", "[C",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[4] = new MBeanAttributeInfo("doubleNumber", "[D",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[5] = new MBeanAttributeInfo("floatNumber", "[F",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[6] = new MBeanAttributeInfo("intNumber", "[I",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[7] = new MBeanAttributeInfo("longNumber", "[J",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[8] = new MBeanAttributeInfo("shortNumber", "[S",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[9] = new MBeanAttributeInfo("stringArray",
                "[Ljava.lang.String", "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);

        MBeanInfo mBeanInfo = new MBeanInfo(className, "n/a", /* attributes */
        attributes, /* constructors */null, /* operations */null, /* notifications */
        null);
        return mBeanInfo;
    }

    /**
     * <p>
     * Access the internal data structure and return a the given MBean
     * meta-data. Note that this implementation only works for 'simple'
     * scenarios and will likely need to be refactored for complex test-cases.
     * </p>
     * 
     * @return Metadata for the requested BasicTypesMBean
     */
    protected MBeanInfo getBasicTypesWrapperMBeanInfo() {
        String className = "com.interopbridges.scx.mbeans.BasicTypesWrapperMBean";
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[2];
        attributes[0] = new MBeanAttributeInfo("theLabel", "java.lang.String",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[1] = new MBeanAttributeInfo("payload",
                "com.interopbridges.scx.mbeans.BasicTypesMBean", "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);

        MBeanInfo mBeanInfo = new MBeanInfo(className, "n/a", /* attributes */
        attributes, /* constructors */null, /* operations */null, /* notifications */
        null);
        return mBeanInfo;
    }

    /**
     * <p>
     * Access the internal data structure and return a the given MBean
     * meta-data. Note that this implementation only works for 'simple'
     * scenarios and will likely need to be refactored for complex test-cases.
     * </p>
     * 
     * @return Metadata for the requested BasicTypesWrapperClassMBean
     */
    protected MBeanInfo getBasicTypesWrapperClassMBeanInfo() 
    {
        String className = "com.interopbridges.scx.mbeans.BasicTypeWrapperClassMBean";
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[9];
        attributes[0] = new MBeanAttributeInfo("boolean", "Boolean",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[1] = new MBeanAttributeInfo("byte", "Byte", "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[2] = new MBeanAttributeInfo("character", "Character",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[3] = new MBeanAttributeInfo("double", "Double",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[4] = new MBeanAttributeInfo("float", "Float",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[5] = new MBeanAttributeInfo("integer", "Integer",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[6] = new MBeanAttributeInfo("long", "Long",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[7] = new MBeanAttributeInfo("short", "Short",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[8] = new MBeanAttributeInfo("theLabel", "java.lang.String",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);

        MBeanInfo mBeanInfo = new MBeanInfo(className, "n/a", /* attributes */
        attributes, /* constructors */null, /* operations */null, /* notifications */
        null);
        return mBeanInfo;
    }

    /**
     * <p>
     * Access the internal data structure and return a the given MBean
     * meta-data. Note that this implementation only works for 'simple'
     * scenarios and will likely need to be refactored for complex test-cases.
     * </p>
     * 
     * @return Metadata for the requested JBoss Web Application
     */
    protected MBeanInfo getJBossWebApplicationMBeanInfo()
    {
        String className = "org.jboss.management.j2ee.J2EEApplication";
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[3];
        attributes[0] = new MBeanAttributeInfo("objectName", "java.lang.String",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[1] = new MBeanAttributeInfo("eventProvider", "Boolean",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[2] = new MBeanAttributeInfo("statisticsProvider", "Boolean",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);

        MBeanInfo mBeanInfo = new MBeanInfo(className, "n/a", /* attributes */
        attributes, /* constructors */null, /* operations */null, /* notifications */
        null);
        return mBeanInfo;
    }
    
    /**
     * <p>
     * Access the internal data structure and return a the given MBean
     * meta-data. Note that this implementation only works for 'simple'
     * scenarios and will likely need to be refactored for complex test-cases.
     * </p>
     * 
     * @return Metadata for the requested EscapedObjectName object
     */
    protected MBeanInfo getEscapedObjectNameMBeanInfo()
    {
        String className = "com.interopbridges.scx.mbeans.EscapedObjectName";
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[2];
        attributes[0] = new MBeanAttributeInfo("objectName", "java.lang.String",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[1] = new MBeanAttributeInfo("theLabel", "java.lang.String",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);

        MBeanInfo mBeanInfo = new MBeanInfo(className, "n/a", /* attributes */
        attributes, /* constructors */null, /* operations */null, /* notifications */
        null);
        return mBeanInfo;
    }
    
    /**
     * <p>
     * Access the internal data structure and return a the given MBean
     * meta-data. Note that this implementation only works for 'simple'
     * scenarios and will likely need to be refactored for complex test-cases.
     * </p>
     * 
     * @return Metadata for the requested OperationMBean
     */
    protected MBeanInfo getOperationMBeanInfo() {
        String className = "com.interopbridges.scx.webservices.Operation";
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[2];
        attributes[0] = new MBeanAttributeInfo("jmxType", "java.lang.String",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[1] = new MBeanAttributeInfo("name", "java.lang.String",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);

        MBeanInfo opsCallMbeanInfo = new MBeanInfo(className, "n/a", /* attributes */
        attributes, /* constructors */null, /* operations */null, /* notifications */
        null);
        return opsCallMbeanInfo;
    }

    /**
     * <p>
     * Access the internal datastructure and return a the given MBean metadata.
     * Note that this implementation only works for 'simple' scenarios and will
     * likely need to be refactors for complex testcases.
     * </p>
     * 
     * @return Metadata for the requested OperationCallMBean
     */
    protected MBeanInfo getOperationCallMBeanInfo() {
        String className = "com.interopbridges.scx.webservices.OperationCall";
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[5];
        attributes[0] = new MBeanAttributeInfo("jmxType", "java.lang.String",
                "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[1] = new MBeanAttributeInfo("sourceEndpoint",
                "com.interopbridges.scx.webservices.Endpoint", "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[2] = new MBeanAttributeInfo("sourceOperation",
                "com.interopbridges.scx.webservices.Operation", "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[3] = new MBeanAttributeInfo("targetEndpoint",
                "com.interopbridges.scx.webservices.Endpoint", "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);
        attributes[4] = new MBeanAttributeInfo("targetOperation",
                "com.interopbridges.scx.webservices.Operation", "description", /* isReadable */
                true, /* isWritable */false, /* isIs */false);

        MBeanInfo opsCallMbeanInfo = new MBeanInfo(className, "n/a", /* attributes */
        attributes, /* constructors */null, /* operations */null, /* notifications */
        null);
        return opsCallMbeanInfo;
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

        /*
         * Note: Implementation is very simple and may require modification to
         * meet your needs. Right now this is only meant to work for very simple
         * queries (it only respects the JmxType key).
         */
        Set<ObjectInstance> returnValue = new HashSet<ObjectInstance>();

        if ("com.interopbridges.scx".equals(name.getDomain())) {
            /*
             * A feature of this mock implementation is that it will only return
             * values for 'our' domain. The only reason for this is to verify
             * the unit-test from the JXM side as of this authoring. Eventually,
             * it will be required to support queries from multiple domains. At
             * such time, feel free to revisit this logic.
             */
            String jmxType = "jmxType";
            String desiredKey = name.getKeyProperty(jmxType);

            _logger.finer("Looking for Key: " + desiredKey);

            /*
             * Having extracted the only item cared about in the query, loop
             * through the fake 'JMX Store' and
             */
            Set<Entry<ObjectName, Object>> entries = _jmx.entrySet();

            for (Entry<ObjectName, Object> item : entries) {
                ObjectName jmxName = item.getKey();
                String storeHadKey = jmxName.getKeyProperty(jmxType);
                _logger.finer("Looking at " + storeHadKey);

                if ((null != storeHadKey) && storeHadKey.equals(desiredKey)) {
                    returnValue.add(new ObjectInstance(item.getKey(), item
                            .getValue().getClass().getName()));

                }
            }
        }
        return returnValue;
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
        Set<Entry<ObjectName, Object>> entries = _jmx.entrySet();
        Set<ObjectName> returnValue = new HashSet<ObjectName>();
        for (Entry<ObjectName, Object> item : entries) {
            returnValue.add(item.getKey());
        }
        return returnValue;
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
        _jmx.put(keys, bean);
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
