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

package com.interopbridges.scx.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.ObjectInstance;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.configuration.JMXFilterParameters;
import com.interopbridges.scx.jmx.FakeJmxGenerator;
import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.jmx.MockJmx;
import com.interopbridges.scx.util.SAXParser;
import com.interopbridges.scx.webservices.FauxMBeanGenerator;
import com.interopbridges.scx.xml.MBeanTransformer;
import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import java.text.MessageFormat;
import com.interopbridges.scx.util.JmxConstant;

/**
 * Testing the transform of Java MBeans to XML that is to be returned as a REST
 * response.<br>
 * 
 * @author Christopher Crammond
 * 
 */
public class MBeanTransformerTest
{

    /**
     * <p>
     * Fake MBean Generator
     * </p>
     */
    private FauxMBeanGenerator fake;

    /**
     * <p>
     * List containing the MBean properties to filter per JMX Store
     * </p>
     */
    private JMXFilterParameters _jmxFilterParameters;

    /**
     * <p>
     * Mock implementation of a JMX Store that is user configurable
     * </p>
     */
    private List<IJMX> _stores;

    /**
     * <p>
     * Test Setup/preparation method that resets/initializes all test specific
     * variables.
     * </p>
     */
    @Before
    public void setup()
    {
        // Setup the MBean Filter and clear out the contents
        _jmxFilterParameters = JMXFilterParameters.GetInstance();
        _jmxFilterParameters.clear();

        _stores = new ArrayList<IJMX>();
        IJMX tempStore = new MockJmx();
        _stores.add(tempStore);

        fake = new FauxMBeanGenerator(tempStore);
        try
        {
            fake.run();
        }
        catch (Exception e)
        {
            Assert.fail("Test setup unexpectedly failed: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Method invoked after each unit-test in this class.
     * </p>
     */
    @After
    public void TearDown()
    {
        fake = null;
    }

    /**
     * <p>
     * Verify the transform of a single Operation transform. The OperationMBean
     * is the simplest available MBean that just contains some single string
     * property. Due to the nature in which the XML is generated, there is no
     * guarantee in which the list of properties will appear, so it is necessary
     * to search for the (case-insensitive) specific strings. <br>
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=add"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *     &lt;Property Name="name" type="java.lang.String"&gt;add&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     */
    @Test
    public void verifySingleOperationMBeanTransform() throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);

        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getAddOperationObjectInstance(), null)
                .toString();
        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single OperationMBean did not have the correct 'name' property. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getAddOperationMBean().getName(),
                        "/MBean[@Name='com.interopbridges.scx.webservices.Operation']/Properties/Property[@Name='name' and @type='java.lang.String']");
        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single OperationMBean did not have the correct 'jmxType' property. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getAddOperationMBean().getJmxType(),
                        "/MBean[@Name = 'com.interopbridges.scx.webservices.Operation']/Properties/Property[@Name='jmxType' and @type='java.lang.String']");
    }

    /**
     * <p>
     * Verify the transform of a single OperationCall transform. The
     * OperationCall is slightly more complicated and requires some recursion to
     * that all of the appropriate Keys/Properties appear in generated XML.
     * Again, due to the nature in which the XML is generated, there is no
     * guarantee in which the list of properties will appear, so it is necessary
     * to search for the (case-insensitive) specific strings.
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.webservices.OperationCall" objectName="com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=multiply,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=add"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="jmxType" type="java.lang.String"&gt;operationCall&lt;/Property&gt;
     *     &lt;Property Name="sourceEndpoint" type="com.interopbridges.scx.webservices.Endpoint"&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;endpoint&lt;/Property&gt;
     *       &lt;Property Name="url" type="java.lang.String"&gt;http://localhost:9080/WebServiceProject/CalculatorService&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="sourceOperation" type="com.interopbridges.scx.webservices.Operation"&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;multiply&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="targetEndpoint" type="com.interopbridges.scx.webservices.Endpoint"&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;endpoint&lt;/Property&gt;
     *       &lt;Property Name="url" type="java.lang.String"&gt;http://localhost:9080/WebServiceProject/CalculatorService&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="targetOperation" type="com.interopbridges.scx.webservices.Operation"&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;add&lt;/Property&gt;
     *     &lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     */
    @Test
    public void verifySingleOperationCallMBeanTransform() throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getMultiplyToAddOperationCallObjectInstance(),
                null).toString();

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single OperationCallMBean did not have the correct 'url' property for the SourceEndpoint. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getMultiplyEndpointMBean().getUrl(),
                        "/MBean[@Name='com.interopbridges.scx.webservices.OperationCall']/Properties/Property[@Name='sourceEndpoint' and @type='com.interopbridges.scx.webservices.Endpoint']/Property[@Name='url' and @type='java.lang.String']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single OperationCallMBean did not have the correct 'name' property for the SourceOperation. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getMultiplyOperationMBean().getName(),
                        "/MBean[@Name='com.interopbridges.scx.webservices.OperationCall']/Properties/Property[@Name='sourceOperation' and @type='com.interopbridges.scx.webservices.Operation']/Property[@Name='name' and @type='java.lang.String']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single OperationCallMBean did not have the correct 'url' property for the TargetEndpoint. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getAddEndpointMBean().getUrl(),
                        "/MBean[@Name='com.interopbridges.scx.webservices.OperationCall']/Properties/Property[@Name='targetEndpoint' and @type='com.interopbridges.scx.webservices.Endpoint']/Property[@Name='url' and @type='java.lang.String']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single OperationCallMBean did not have the correct 'name' property for the TargetOperation. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getAddOperationMBean().getName(),
                        "/MBean[@Name='com.interopbridges.scx.webservices.OperationCall']/Properties/Property[@Name='targetOperation' and @type='com.interopbridges.scx.webservices.Operation']/Property[@Name='name' and @type='java.lang.String']");
    }

    /**
     * <p>
     * Test to verify that a MBean that consists of primitive types (non-arrays)
     * can be transformed into XML properly.
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.BasicTypes" objectName="com.interopbridges.scx:byte=127,charLetter=a,doubleNumber=1.7976931348623157E308,floatNumber=3.4028235E38,intNumber=2147483647,isBoolean=true,longNumber=9223372036854775807,shortNumber=32767,theLabel=basicTypes"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;basicTypes&lt;/Property&gt;
     *     &lt;Property Name="isBoolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *     &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *     &lt;Property Name="charLetter" type="java.lang.Character"&gt;a&lt;/Property&gt;
     *     &lt;Property Name="doubleNumber" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *     &lt;Property Name="floatNumber" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *     &lt;Property Name="intNumber" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *     &lt;Property Name="longNumber" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *     &lt;Property Name="shortNumber" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifySingleMBeanWithSimpleDataTypesTransform()
            throws Exception
    {

        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getBasicTypesObjectInstance(), null)
                .toString();

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'bool' property for the isBoolean. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesMBean().getIsBoolean(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypes']/Properties/Property[@Name='isBoolean' and @type='java.lang.Boolean']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'byte' property for the byte. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getBasicTypesMBean().getByte(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypes']/Properties/Property[@Name='byte' and @type='java.lang.Byte']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'char' property for the charLetter. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesMBean().getCharLetter(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypes']/Properties/Property[@Name='charLetter' and @type='java.lang.Character']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'double' property for the doubleNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesMBean().getDoubleNumber(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypes']/Properties/Property[@Name='doubleNumber' and @type='java.lang.Double']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'float' property for the floatNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesMBean().getFloatNumber(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypes']/Properties/Property[@Name='floatNumber' and @type='java.lang.Float']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'int' property for the intNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesMBean().getIntNumber(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypes']/Properties/Property[@Name='intNumber' and @type='java.lang.Integer']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'long' property for the longNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesMBean().getLongNumber(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypes']/Properties/Property[@Name='longNumber' and @type='java.lang.Long']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'short' property for the shortNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesMBean().getShortNumber(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypes']/Properties/Property[@Name='shortNumber' and @type='java.lang.Short']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'String' property for the theLabel. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesMBean().getTheLabel(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypes']/Properties/Property[@Name='theLabel' and @type='java.lang.String']");
    }

    /**
     * <p>
     * Test to verify that a MBean that consists of primitive wrapper class
     * types (non-arrays) can be transformed into XML properly.
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.BasicTypesWrapperClass" objectName="com.interopbridges.scx:boolean=true,byte=127,character=a,double=1.7976931348623157E308,float=3.4028235E38,integer=2147483647,long=9223372036854775807,short=32767,theLabel=basicTypesWrapperClass"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *     &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *     &lt;Property Name="character" type="java.lang.Character"&gt;a&lt;/Property&gt;
     *     &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *     &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *     &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *     &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *     &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;basicTypesWrapperClass&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifySingleMBeanWithSimpleDataTypesWrapperClassTransform()
            throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getBasicTypesWrapperClassObjectInstance(),
                null).toString();

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeWrapperClassMBeans did not have the correct 'Boolean' property for the Boolean. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperClassMBean()
                                .getBoolean(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapperClass']/Properties/Property[@Name='boolean' and @type='java.lang.Boolean']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeWrapperClassMBeans did not have the correct 'Byte' property for the Byte. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperClassMBean()
                                .getByte(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapperClass']/Properties/Property[@Name='byte' and @type='java.lang.Byte']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeWrapperClassMBeans did not have the correct 'char' property for the Character. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperClassMBean()
                                .getCharacter(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapperClass']/Properties/Property[@Name='character' and @type='java.lang.Character']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeWrapperClassMBeans did not have the correct 'double' property for the Double. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperClassMBean()
                                .getDouble(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapperClass']/Properties/Property[@Name='double' and @type='java.lang.Double']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeWrapperClassMBeans did not have the correct 'float' property for the Float. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperClassMBean()
                                .getFloat(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapperClass']/Properties/Property[@Name='float' and @type='java.lang.Float']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeWrapperClassMBeans did not have the correct 'int' property for the Integer. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperClassMBean()
                                .getInteger(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapperClass']/Properties/Property[@Name='integer' and @type='java.lang.Integer']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeWrapperClassMBeans did not have the correct 'long' property for the Long. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperClassMBean()
                                .getLong(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapperClass']/Properties/Property[@Name='long' and @type='java.lang.Long']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeWrapperClassMBeans did not have the correct 'short' property for the Short. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperClassMBean()
                                .getShort(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapperClass']/Properties/Property[@Name='short' and @type='java.lang.Short']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeWrapperClassMBeans did not have the correct 'String' property for the theLabel. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperClassMBean()
                                .getTheLabel(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapperClass']/Properties/Property[@Name='theLabel' and @type='java.lang.String']");
    }

    /**
     * <p>
     * Test to verify that a MBean that consists of primitive types (arrays) can
     * be transformed into XML properly.
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.BasicTypeArrays" objectName="com.interopbridges.scx:theLabel=basicTypeArrays"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;basicTypeArrays&lt;/Property&gt;
     *     &lt;Property Name="isBoolean" type="[Z"&gt;
     *       &lt;Property Name="isBoolean" index="0"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="isBoolean" index="1"&gt;false&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="byte" type="[B"&gt;
     *       &lt;Property Name="byte" index="0"&gt;-128&lt;/Property&gt;
     *       &lt;Property Name="byte" index="1"&gt;127&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="charLetter" type="[C"&gt;
     *       &lt;Property Name="charLetter" index="0"&gt;a&lt;/Property&gt;
     *       &lt;Property Name="charLetter" index="1"&gt;b&lt;/Property&gt;
     *       &lt;Property Name="charLetter" index="2"&gt;c&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="doubleNumber" type="[D"&gt;
     *       &lt;Property Name="doubleNumber" index="0"&gt;4.9E-324&lt;/Property&gt;
     *       &lt;Property Name="doubleNumber" index="1"&gt;1.7976931348623157E308&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="floatNumber" type="[F"&gt;
     *       &lt;Property Name="floatNumber" index="0"&gt;1.4E-45&lt;/Property&gt;
     *       &lt;Property Name="floatNumber" index="1"&gt;3.4028235E38&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="intNumber" type="[I"&gt;
     *       &lt;Property Name="intNumber" index="0"&gt;-2147483648&lt;/Property&gt;
     *       &lt;Property Name="intNumber" index="1"&gt;2147483647&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="longNumber" type="[J"&gt;
     *       &lt;Property Name="longNumber" index="0"&gt;-9223372036854775808&lt;/Property&gt;
     *       &lt;Property Name="longNumber" index="1"&gt;9223372036854775807&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="shortNumber" type="[S"&gt;
     *       &lt;Property Name="shortNumber" index="0"&gt;-32768&lt;/Property&gt;
     *       &lt;Property Name="shortNumber" index="1"&gt;32767&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="stringArray" type="[Ljava.lang.String;"&gt;
     *       &lt;Property Name="stringArray" index="0"&gt;a&lt;/Property&gt;
     *       &lt;Property Name="stringArray" index="1"&gt;two&lt;/Property&gt;
     *     &lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifySingleMBeanWithSimpleDataTypeArraysTransform()
            throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getBasicTypeArraysObjectInstance(), null)
                .toString();

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'boolean[]' property for the byte. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getIsBoolean()[0],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='isBoolean' and @type='[Z']/Property[@Name='isBoolean' and @index='0']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'boolean[]' property for the byte. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getIsBoolean()[1],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='isBoolean' and @type='[Z']/Property[@Name='isBoolean' and @index='1']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'byte[]' property for the byte. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean().getByte()[0],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='byte' and @type='[B']/Property[@Name='byte' and @index='0']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'byte[]' property for the byte. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean().getByte()[1],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='byte' and @type='[B']/Property[@Name='byte' and @index='1']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'char[]' property for the charLetter. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getCharLetter()[0],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='charLetter' and @type='[C']/Property[@Name='charLetter' and @index='0']");
        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'char[]' property for the charLetter. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getCharLetter()[1],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='charLetter' and @type='[C']/Property[@Name='charLetter' and @index='1']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'char[]' property for the charLetter. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getCharLetter()[2],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='charLetter' and @type='[C']/Property[@Name='charLetter' and @index='2']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'double[]' property for the doubleNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getDoubleNumber()[0],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='doubleNumber' and @type='[D']/Property[@Name='doubleNumber' and @index='0']");
        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'double[]' property for the doubleNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getDoubleNumber()[1],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='doubleNumber' and @type='[D']/Property[@Name='doubleNumber' and @index='1']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'float[]' property for the floatNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getFloatNumber()[0],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='floatNumber' and @type='[F']/Property[@Name='floatNumber' and @index='0']");
        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'float[]' property for the floatNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getFloatNumber()[1],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='floatNumber' and @type='[F']/Property[@Name='floatNumber' and @index='1']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'int[]' property for the intNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getIntNumber()[0],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='intNumber' and @type='[I']/Property[@Name='intNumber' and @index='0']");
        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'int[]' property for the intNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getIntNumber()[1],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='intNumber' and @type='[I']/Property[@Name='intNumber' and @index='1']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'long[]' property for the longNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getLongNumber()[0],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='longNumber' and @type='[J']/Property[@Name='longNumber' and @index='0']");
        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'long[]' property for the longNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getLongNumber()[1],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='longNumber' and @type='[J']/Property[@Name='longNumber' and @index='1']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'short[]' property for the shortNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getShortNumber()[0],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='shortNumber' and @type='[S']/Property[@Name='shortNumber' and @index='0']");
        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'short[]' property for the shortNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getShortNumber()[1],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='shortNumber' and @type='[S']/Property[@Name='shortNumber' and @index='1']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'String' property for the theLabel. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getTheLabel(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='theLabel' and @type='java.lang.String']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'String Array' property for the stringArray. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getStringArray()[0],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='stringArray' and @type='[Ljava.lang.String;']/Property[@Name='stringArray' and @index='0']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeArrayMBeans did not have the correct 'String Array' property for the stringArray. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypeArraysMBean()
                                .getStringArray()[1],
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypeArrays']/Properties/Property[@Name='stringArray' and @type='[Ljava.lang.String;']/Property[@Name='stringArray' and @index='1']");
    }

    /**
     * <p>
     * Test to verify that a MBean that consists an object that wraps an object
     * of primitive types (non-arrays) can be transformed into XML properly.
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.BasicTypesWrapper" objectName="com.interopbridges.scx:payload=isBoolean true| byte 127| charLetter a| doubleNumber 1.7976931348623157E308| floatNumber 3.4028235E38| intNumber 2147483647| longNumber 9223372036854775807| shortNumber 32767| theLabel basicTypes,theLabel=BasicTypesWrapperMBean"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;BasicTypesWrapperMBean&lt;/Property&gt;
     *     &lt;Property Name="payload" type="com.interopbridges.scx.mbeans.BasicTypes"&gt;
     *       &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="charLetter" type="java.lang.Character"&gt;a&lt;/Property&gt;
     *       &lt;Property Name="doubleNumber" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="floatNumber" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="intNumber" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="isBoolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="longNumber" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="shortNumber" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="theLabel" type="java.lang.String"&gt;basicTypes&lt;/Property&gt;
     *     &lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifySingleMBeanObjectWithSimpleDataTypesTransform()
            throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getBasicTypesWrapperObjectInstance(), null)
                .toString();

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'String' property for the theLabel. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperMBean()
                                .getTheLabel(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapper']/Properties/Property[@Name='theLabel' and @type='java.lang.String']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'boolean' property for the isBoolean. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperMBean()
                                .getPayload().getIsBoolean(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapper']/Properties/Property[@Name='payload' and @type='com.interopbridges.scx.mbeans.BasicTypes']/Property[@Name='isBoolean' and @type='java.lang.Boolean']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'byte' property for the Byte. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperMBean()
                                .getPayload().getByte(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapper']/Properties/Property[@Name='payload' and @type='com.interopbridges.scx.mbeans.BasicTypes']/Property[@Name='byte' and @type='java.lang.Byte']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'char' property for the charLetter. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperMBean()
                                .getPayload().getCharLetter(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapper']/Properties/Property[@Name='payload' and @type='com.interopbridges.scx.mbeans.BasicTypes']/Property[@Name='charLetter' and @type='java.lang.Character']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'double' property for the doubleNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperMBean()
                                .getPayload().getDoubleNumber(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapper']/Properties/Property[@Name='payload' and @type='com.interopbridges.scx.mbeans.BasicTypes']/Property[@Name='doubleNumber' and @type='java.lang.Double']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'float' property for the floatNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperMBean()
                                .getPayload().getFloatNumber(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapper']/Properties/Property[@Name='payload' and @type='com.interopbridges.scx.mbeans.BasicTypes']/Property[@Name='floatNumber' and @type='java.lang.Float']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'int' property for the intNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperMBean()
                                .getPayload().getIntNumber(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapper']/Properties/Property[@Name='payload' and @type='com.interopbridges.scx.mbeans.BasicTypes']/Property[@Name='intNumber' and @type='java.lang.Integer']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'long' property for the longNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperMBean()
                                .getPayload().getLongNumber(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapper']/Properties/Property[@Name='payload' and @type='com.interopbridges.scx.mbeans.BasicTypes']/Property[@Name='longNumber' and @type='java.lang.Long']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single BasicTypeMBeans did not have the correct 'short' property for the shortNumber. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getBasicTypesWrapperMBean()
                                .getPayload().getShortNumber(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.BasicTypesWrapper']/Properties/Property[@Name='payload' and @type='com.interopbridges.scx.mbeans.BasicTypes']/Property[@Name='shortNumber' and @type='java.lang.Short']");
    }

    /**
     * <p>
     * Verify the transform of a single Operation transform. The OperationMBean
     * is the simplest available MBean that just contains some single string
     * property. Due to the nature in which the XML is generated, there is no
     * guarantee in which the list of properties will appear, so it is necessary
     * to search for the (case-insensitive) specific strings.
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBeans version="NotFromLabel"&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=add"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;add&lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=divide"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;divide&lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     * &lt;/MBeans&gt;
     * </pre>
     * 
     * </p>
     *
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifyMultipleOperationMBeansTransform() throws Exception
    {
        HashMap<IJMX, Set<ObjectInstance>> mbeansl = new HashMap<IJMX, Set<ObjectInstance>>();

        Assert.assertEquals("Wrong number of JMX Stores", 1, this._stores
                .size());

        for (int i = 0; i < this._stores.size(); i++)
        {
            // Input
            Set<ObjectInstance> operations = new HashSet<ObjectInstance>(2);
            // ArrayList<ObjectInstance> operations = new
            // ArrayList<ObjectInstance>(2);
            operations.add(FakeJmxGenerator.getAddOperationObjectInstance());
            operations.add(FakeJmxGenerator.getDivideOperationObjectInstance());

            // Results to Check (1/2: list of operation names)
            ArrayList<Object> nameList = new ArrayList<Object>(operations
                    .size());
            nameList.add(FakeJmxGenerator.getAddOperationMBean().getName());
            nameList.add(FakeJmxGenerator.getDivideOperationMBean().getName());

            // Results to Check (2/2: list of JMX Type)
            ArrayList<Object> jmxList = new ArrayList<Object>(operations.size());
            jmxList.add(FakeJmxGenerator.getAddOperationMBean().getJmxType());
            jmxList
                    .add(FakeJmxGenerator.getDivideOperationMBean()
                            .getJmxType());

            // Perform the actual test via helper methods

            mbeansl.put(this._stores.get(i), operations);

            MBeanTransformer sut = new MBeanTransformer();
            String xml = sut.transformMultipleMBeans(mbeansl, null).toString();
            
            SAXParser
                    .customQueryAssertForXmlValidationOfListOfObjectValues(
                            "XML for a single OperationMBean did not have the correct 'name' property. Input was "
                                    + xml,
                            xml,
                            nameList,
                            "/MBeans/MBean[@Name='com.interopbridges.scx.webservices.Operation']/Properties/Property[@Name='name' and @type='java.lang.String']");
            SAXParser
                    .customQueryAssertForXmlValidationOfListOfObjectValues(
                            "XML for a single OperationMBean did not have the correct 'jmxType' property. Input was "
                                    + xml,
                            xml,
                            jmxList,
                            "/MBeans/MBean[@Name='com.interopbridges.scx.webservices.Operation']/Properties/Property[@Name='jmxType' and @type='java.lang.String']");
        }
    }

    /**
     * *
     * <p>
     * Verify the transform of a multiple OperationCalls transform. The
     * OperationCallMBean is the 'complex' in that it contains a String property
     * and multiple other properties that are other MBeans.. Due to the nature
     * in which the XML is generated, there is no guarantee in which the list of
     * properties will appear, so it is necessary to search for the
     * (case-insensitive) specific strings.
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBeans version="NotFromLabel"&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.OperationCall" objectName="com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=multiply,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=add"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operationCall&lt;/Property&gt;
     *       &lt;Property Name="sourceEndpoint" type="com.interopbridges.scx.webservices.Endpoint"&gt;
     *         &lt;Property Name="jmxType" type="java.lang.String"&gt;endpoint&lt;/Property&gt;
     *         &lt;Property Name="url" type="java.lang.String"&gt;http://localhost:9080/WebServiceProject/CalculatorService&lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="sourceOperation" type="com.interopbridges.scx.webservices.Operation"&gt;
     *         &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *         &lt;Property Name="name" type="java.lang.String"&gt;multiply&lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="targetEndpoint" type="com.interopbridges.scx.webservices.Endpoint"&gt;
     *         &lt;Property Name="jmxType" type="java.lang.String"&gt;endpoint&lt;/Property&gt;
     *         &lt;Property Name="url" type="java.lang.String"&gt;http://localhost:9080/WebServiceProject/CalculatorService&lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="targetOperation" type="com.interopbridges.scx.webservices.Operation"&gt;
     *         &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *         &lt;Property Name="name" type="java.lang.String"&gt;add&lt;/Property&gt;
     *       &lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.OperationCall" objectName="com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operationCall&lt;/Property&gt;
     *       &lt;Property Name="sourceEndpoint" type="com.interopbridges.scx.webservices.Endpoint"&gt;
     *         &lt;Property Name="jmxType" type="java.lang.String"&gt;endpoint&lt;/Property&gt;
     *         &lt;Property Name="url" type="java.lang.String"&gt;http://localhost:9080/WebServiceProject/CalculatorService&lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="sourceOperation" type="com.interopbridges.scx.webservices.Operation"&gt;
     *         &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *         &lt;Property Name="name" type="java.lang.String"&gt;divide&lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="targetEndpoint" type="com.interopbridges.scx.webservices.Endpoint"&gt;
     *         &lt;Property Name="jmxType" type="java.lang.String"&gt;endpoint&lt;/Property&gt;
     *         &lt;Property Name="url" type="java.lang.String"&gt;http://localhost:9080/WebServiceProject/CalculatorService&lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="targetOperation" type="com.interopbridges.scx.webservices.Operation"&gt;
     *         &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *         &lt;Property Name="name" type="java.lang.String"&gt;subtract&lt;/Property&gt;
     *       &lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     * &lt;/MBeans&gt;
     * </pre>
     * 
     * </p>
     *
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     *
     */
    @Test
    public void verifyMultipleOperationCallMBeansTransform() throws Exception
    {
        HashMap<IJMX, Set<ObjectInstance>> mbeansl = new HashMap<IJMX, Set<ObjectInstance>>();
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);

        Set<ObjectInstance> operationCalls = new HashSet<ObjectInstance>(2);
        operationCalls.add(FakeJmxGenerator
                .getMultiplyToAddOperationCallObjectInstance());
        operationCalls.add(FakeJmxGenerator
                .getDivideToSubtractOperationCallObjectInstance());

        // Results to Check (1/4: list of Source Endpoint URLs)
        ArrayList<Object> sourceEndpoints = new ArrayList<Object>(
                operationCalls.size());
        sourceEndpoints.add(FakeJmxGenerator.getMultiplyEndpointMBean()
                .getUrl());
        sourceEndpoints.add(FakeJmxGenerator.getDivideEndpointMBean().getUrl());

        // Results to Check (2/4: list of Source Operations URLs)
        ArrayList<Object> sourceOperations = new ArrayList<Object>(
                operationCalls.size());
        sourceOperations.add(FakeJmxGenerator.getMultiplyOperationMBean()
                .getName());
        sourceOperations.add(FakeJmxGenerator.getDivideOperationMBean()
                .getName());

        // Results to Check (3/4: list of Source Endpoint URLs)
        ArrayList<Object> targetEndpoints = new ArrayList<Object>(
                operationCalls.size());
        targetEndpoints.add(FakeJmxGenerator.getAddEndpointMBean().getUrl());
        targetEndpoints.add(FakeJmxGenerator.getSubtractEndpointMBean()
                .getUrl());

        // Results to Check (4/4: list of Source Operations URLs)
        ArrayList<Object> targetOperations = new ArrayList<Object>(
                operationCalls.size());
        targetOperations.add(FakeJmxGenerator.getAddOperationMBean().getName());
        targetOperations.add(FakeJmxGenerator.getSubtractOperationMBean()
                .getName());

        // Perform the actual test via helper methods
        mbeansl.put(this._stores.get(0), operationCalls);

        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformMultipleMBeans(mbeansl, null).toString();
        
       SAXParser
                .customQueryAssertForXmlValidationOfListOfObjectValues(
                        "XML for a single OperationCallMBean did not have the correct 'url' property for the SourceEndpoint. Input was "
                                + xml,
                        xml,
                        sourceEndpoints,
                        "/MBeans/MBean[@Name='com.interopbridges.scx.webservices.OperationCall']/Properties/Property[@Name='sourceEndpoint' and @type='com.interopbridges.scx.webservices.Endpoint']/Property[@Name='url' and @type='java.lang.String']");

        SAXParser
                .customQueryAssertForXmlValidationOfListOfObjectValues(
                        "XML for a single OperationMBean did not have the correct 'jmxType' propertyfor the sourceOperation. Input was "
                                + xml,
                        xml,
                        sourceOperations,
                        "/MBeans/MBean[@Name='com.interopbridges.scx.webservices.OperationCall']/Properties/Property[@Name='sourceOperation' and @type='com.interopbridges.scx.webservices.Operation']/Property[@Name='name' and @type='java.lang.String']");

        SAXParser
                .customQueryAssertForXmlValidationOfListOfObjectValues(
                        "XML for a single OperationCallMBean did not have the correct 'url' property for the targetEndpoint. Input was "
                                + xml,
                        xml,
                        targetEndpoints,
                        "/MBeans/MBean[@Name='com.interopbridges.scx.webservices.OperationCall']/Properties/Property[@Name='targetEndpoint' and @type='com.interopbridges.scx.webservices.Endpoint']/Property[@Name='url' and @type='java.lang.String']");

        SAXParser
                .customQueryAssertForXmlValidationOfListOfObjectValues(
                        "XML for a single OperationMBean did not have the correct 'jmxType' property for the targetOperation. Input was "
                                + xml,
                        xml,
                        targetOperations,
                        "/MBeans/MBean[@Name='com.interopbridges.scx.webservices.OperationCall']/Properties/Property[@Name='targetOperation' and @type='com.interopbridges.scx.webservices.Operation']/Property[@Name='name' and @type='java.lang.String']");
    }

    /**
     * <p>
     * Test to verify that a MBean that consists of complex class type can be
     * transformed into XML properly. note: The inner class contained within the
     * ComplexClass is not decoded into the XML output.
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.ComplexType" objectName="com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="complexitem" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *       &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *       &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *       &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *         &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *           &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *           &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *           &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *           &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *           &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *           &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *           &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *           &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *           &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *           &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *           &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *           &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *             &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *             &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *               &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *               &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *               &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *               &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *               &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *               &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *               &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *               &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *               &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *               &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *               &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *               &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *                 &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                 &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *                   &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                   &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                   &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *                   &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                   &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                   &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                   &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                   &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                   &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                   &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                   &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *                   &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *                     &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                     &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *                       &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                       &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                       &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *                       &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                       &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                       &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                       &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                       &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                       &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                       &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                       &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *                       &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;com.interopbridges.scx.mbeans.ComplexRecursiveClass@741b3967&lt;/Property&gt;
     *                       &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                       &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                       &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                       &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                       &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"&gt;com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass@74021a1e&lt;/Property&gt;
     *                       &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                       &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *                     &lt;/Property&gt;
     *                   &lt;/Property&gt;
     *                   &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                   &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                   &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                   &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                   &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *                   &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                   &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *                 &lt;/Property&gt;
     *               &lt;/Property&gt;
     *               &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *               &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *               &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *               &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *               &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *               &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *               &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *             &lt;/Property&gt;
     *           &lt;/Property&gt;
     *           &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *           &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *           &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *           &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *           &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *           &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *           &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *         &lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *       &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="complexitemarray" type="[Lcom.interopbridges.scx.mbeans.ComplexClass;"&gt;
     *       &lt;Property Name="complexitemarray" index="0"&gt;
     *         &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *         &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *         &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *         &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *         &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *         &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *         &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *         &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *         &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *         &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *         &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *           &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *           &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *             &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *             &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *             &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *             &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *             &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *             &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *             &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *             &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *             &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *             &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *             &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *             &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *               &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *               &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *                 &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                 &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                 &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *                 &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                 &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                 &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                 &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                 &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                 &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                 &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                 &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *                 &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *                   &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                   &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *                     &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                     &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                     &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *                     &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                     &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                     &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                     &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                     &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                     &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                     &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                     &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *                     &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *                       &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                       &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *                         &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                         &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                         &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *                         &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                         &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                         &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                         &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                         &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                         &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                         &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                         &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *                         &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;com.interopbridges.scx.mbeans.ComplexRecursiveClass@743bce70&lt;/Property&gt;
     *                         &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                         &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                         &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                         &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                         &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"&gt;com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass@26e56ae&lt;/Property&gt;
     *                         &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                         &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *                       &lt;/Property&gt;
     *                     &lt;/Property&gt;
     *                     &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                     &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                     &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                     &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                     &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *                     &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                     &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *                   &lt;/Property&gt;
     *                 &lt;/Property&gt;
     *                 &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                 &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                 &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                 &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                 &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *                 &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                 &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *               &lt;/Property&gt;
     *             &lt;/Property&gt;
     *             &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *             &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *             &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *             &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *             &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *             &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *             &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *           &lt;/Property&gt;
     *         &lt;/Property&gt;
     *         &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *         &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *         &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *         &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *         &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *         &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="complexitemarray" index="1"&gt;
     *         &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *         &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *         &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *         &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *         &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *         &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *         &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *         &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *         &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *         &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *         &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *           &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *           &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *             &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *             &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *             &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *             &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *             &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *             &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *             &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *             &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *             &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *             &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *             &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *             &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *               &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *               &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *                 &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                 &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                 &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *                 &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                 &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                 &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                 &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                 &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                 &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                 &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                 &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *                 &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *                   &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                   &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *                     &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                     &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                     &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *                     &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                     &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                     &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                     &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                     &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                     &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                     &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                     &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *                     &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *                       &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                       &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *                         &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                         &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                         &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *                         &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                         &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                         &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                         &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                         &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                         &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *                         &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *                         &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *                         &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;com.interopbridges.scx.mbeans.ComplexRecursiveClass@19fcbac1&lt;/Property&gt;
     *                         &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                         &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                         &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                         &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                         &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"&gt;com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass@1f48e23b&lt;/Property&gt;
     *                         &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                         &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *                       &lt;/Property&gt;
     *                     &lt;/Property&gt;
     *                     &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                     &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                     &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                     &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                     &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *                     &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                     &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *                   &lt;/Property&gt;
     *                 &lt;/Property&gt;
     *                 &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *                 &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *                 &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *                 &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *                 &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *                 &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *                 &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *               &lt;/Property&gt;
     *             &lt;/Property&gt;
     *             &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *             &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *             &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *             &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *             &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *             &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *             &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *           &lt;/Property&gt;
     *         &lt;/Property&gt;
     *         &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *         &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *         &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *         &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *         &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *         &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *       &lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;ComplexType&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifySingleMBeanWithComplexDataTypeTransform()
            throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        HashMap<String, String[]> params = new HashMap<String, String[]>();
        String[] paramvals = { "3" };
        params.put(JmxConstant.STR_MAXDEPTH, paramvals);

        
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getComplexTypeObjectInstance(), params)
                .toString();
        
        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean did not have the correct 'String' property for the theLabel. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getComplexTypeMBean().getTheLabel(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='theLabel' and @type='java.lang.String']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean did not have the correct 'ComplexClass' property for the complexitem. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getComplexTypeMBean()
                                .getComplexClass(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem' and @type='com.interopbridges.scx.mbeans.ComplexClass']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'boolean' property for the _boolean. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .get_boolean(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='_boolean']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'byte' property for the _byte. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .get_byte(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='_byte']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'char' property for the _character. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .get_character(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='_character']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'double' property for the _double. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .get_double(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='_double']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'float' property for the _float. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .get_float(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='_float']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'int' property for the _int. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .get_int(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='_int']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'long' property for the _long. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .get_long(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='_long']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'short' property for the _short. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .get_short(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='_short']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'java.lang.Boolean' property for the boolean. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .getBoolean(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='boolean']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'java.lang.Byte' property for the byte. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .getByte(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='byte']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'java.lang.Character' property for the character. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .getCharacter(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='character']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'java.lang.Double' property for the double. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .getDouble(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='double']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'java.lang.Float' property for the float. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .getFloat(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='float']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'java.lang.Integer' property for the integer. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .getInteger(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='integer']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'java.lang.Long' property for the long. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .getLong(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='long']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'java.lang.Short' property for the short. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .getShort(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='short']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' did not have the correct 'java.lang.String' property for the string. Input was "
                                + xml, xml, FakeJmxGenerator
                                .getComplexTypeMBean().getComplexClass()
                                .getString(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='string']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a'ComplexClass' containing a 'ComplexRecursiveClass' did not have the correct 'java.lang.Integer' property. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getComplexTypeMBean()
                                .getComplexClass().getChildClass().getInteger(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='childClass' and @type='"
                                + FakeJmxGenerator.getComplexTypeMBean()
                                        .getComplexClass().getChildClass()
                                        .getClass().getCanonicalName()
                                + "']/Property[@Name='integer']");
    }

    
  

    /**
     * <p>
     * Test to verify that a MBean that consists of complex class type can be
     * transformed into XML properly. note: The inner class contained within the
     * ComplexClass is not decoded into the XML output. The complex class
     * contains a cyclic prorery list, these tests are to ensure the override
     * parameters control the output correctly. This specific test is for
     * MaxDepth=0
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.ComplexType" objectName="com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType"&gt;
     *   &lt;objectName type="java.lang.String"&gt;com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType&lt;/objectName&gt;
     *   &lt;objectNameElements type="objectName"&gt;
     *     &lt;Domain&gt;com.interopbridges.scx&lt;/Domain&gt;
     *     &lt;complexitem&gt;serialVersionUID 999999999999999999&lt;/complexitem&gt;
     *     &lt;theLabel&gt;ComplexType&lt;/theLabel&gt;
     *   &lt;/objectNameElements&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifySingleMBeanWithComplexDataTypeTransformLimitDepthtoZero()
            throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();

        HashMap<String, String[]> Params = null;
        Params = new HashMap<String, String[]>();
        String[] paramvals =
        { "0" };

        Params.put(JmxConstant.STR_MAXDEPTH, paramvals);

        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getComplexTypeObjectInstance(), Params)
                .toString();
        
        Assert.assertEquals(
                "Wrong number of elements for first level of returned XML", 2,
                SAXParser.XPathQuery(xml,
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/*").length);

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean did not have the correct 'objectName' property. Input was "
                                + xml,
                        xml,
                        "com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType",
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/objectName");

        Assert
                .assertEquals(
                        "Wrong number of elements for second level (i.e. objectNameElements) of returned XML",
                        3,
                        SAXParser
                                .XPathQuery(xml,
                                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/objectNameElements/*").length);

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean did not have the correct 'Domain' property for the objectNameElements. Input was "
                                + xml, xml, "com.interopbridges.scx",
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/objectNameElements/Domain");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean did not have the correct 'complexitem' property for the objectNameElements Input was "
                                + xml, xml,
                        "serialVersionUID 999999999999999999",
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/objectNameElements/complexitem");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean did not have the correct 'theLabel' property for the objectNameElements Input was "
                                + xml, xml, "ComplexType",
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/objectNameElements/theLabel");
    }

    /*
     * 
     * Verify the default depth value of 2 works as expected.
     * 
     */
    @Test
    public void verifySingleMBeanWithComplexDataTypeTransformLimitDefaultDepthIsTwo()
            throws Exception
    {
        Assert.assertEquals(
                "Verify the default MaxDepth is two", 2, JmxConstant.MAXDEPTH);
    	
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();

        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getComplexTypeObjectInstance(), null)
                .toString();

        SAXParser
        .customQueryAssertForXmlValidationOfListOfObjectValues(
                "XML for a single ComplexTypeMBean containing a complexitemarray did not have the correct 'String' property for the theLabel. Input was "
                        + xml,
                xml,
                new ArrayList<Object>(),
                "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitemarray']/Property[@Name='complexitemarray' and @index='0']/Property[@Name='childClass']/Property[@Name='parent']");
    }
    
    
    /**
     * <p>
     * Verify the that more than the MaxDepth is not returned.
     * </p>
     */
    @Test
    public void verifyNoMoreThanDepthTwoIsReturned()
            throws Exception
    {
        Assert.assertEquals(
                "Verify the default MaxDepth is two", 2, JmxConstant.MAXDEPTH);
    	
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);

        MBeanTransformer sut = new MBeanTransformer();

        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getComplexTypeObjectInstance(), null)
                .toString();
        
        String[] resultsFromLevelThree = SAXParser.XPathQuery(xml, "/MBean/Properties/Property/Property/Property");
        Assert.assertEquals("There should be no results from level three due MaxDepth = 2", 0, resultsFromLevelThree.length);
    }    
    /**
     * <p>
     * Test to verify that a MBean that consists of complex class type can be
     * transformed into XML properly. note: The inner class contained within the
     * ComplexClass is not decoded into the XML output. The complex class
     * contains a cyclic property list, these tests are to ensure the override
     * parameters control the output correctly. This specific test is for
     * MaxDepth=5
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.ComplexType" objectName="com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="complexitem" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *       &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *       &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *       &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *         &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *           &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *           &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *           &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *           &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *           &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *           &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *           &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *           &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *           &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *           &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *           &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *           &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *             &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *             &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *           &lt;/Property&gt;
     *           &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *           &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *           &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *           &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *           &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *           &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *           &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *         &lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *       &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="complexitemarray" type="[Lcom.interopbridges.scx.mbeans.ComplexClass;"&gt;
     *       &lt;Property Name="complexitemarray" index="0"&gt;
     *         &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *         &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *         &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *         &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *         &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *         &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *         &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *         &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *         &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *         &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *         &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *           &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *           &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *             &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *             &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *             &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *             &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *             &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *             &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *             &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *             &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *             &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *             &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *             &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *             &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *               &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *               &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *             &lt;/Property&gt;
     *             &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *             &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *             &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *             &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *             &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *             &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *             &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *           &lt;/Property&gt;
     *         &lt;/Property&gt;
     *         &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *         &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *         &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *         &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *         &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *         &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="complexitemarray" index="1"&gt;
     *         &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *         &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *         &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *         &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *         &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *         &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *         &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *         &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *         &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *         &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *         &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *           &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *           &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *             &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *             &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *             &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *             &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *             &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *             &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *             &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *             &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *             &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *             &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *             &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *             &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *               &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *               &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *             &lt;/Property&gt;
     *             &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *             &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *             &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *             &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *             &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *             &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *             &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *           &lt;/Property&gt;
     *         &lt;/Property&gt;
     *         &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *         &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *         &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *         &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"/&gt;
     *         &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *         &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *       &lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;ComplexType&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifySingleMBeanWithComplexDataTypeTransformLimitDepthtoFive()
            throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();

        HashMap<String, String[]> Params = null;
        Params = new HashMap<String, String[]>();
        String[] paramvals = { "5" };

        Params.put(JmxConstant.STR_MAXDEPTH, paramvals);

        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getComplexTypeObjectInstance(), Params)
                .toString();

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean containing a complexitemarray did not have the correct 'String' property for the theLabel. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getComplexTypeMBean()
                                .getComplexClass().toString(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitemarray']/Property[@Name='complexitemarray' and @index='0']/Property[@Name='childClass']/Property[@Name='parent']/Property[@Name='childClass']/Property[@Name='parent' and @type='com.interopbridges.scx.mbeans.ComplexClass']");
    }

    /**
     * <p>
     * Test to verify that a MBean that consists of complex class type can be
     * transformed into XML properly. note: The inner class contained within the
     * ComplexClass is not decoded into the XML output. The complex class
     * contains a cyclic property list, these tests are to ensure the override
     * parameters control the output correctly. This specific test is for
     * MaxCount=14
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.ComplexType" objectName="com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="complexitem" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *       &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *       &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *       &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *         &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"&gt;com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass@5d4fa79d&lt;/Property&gt;
     *       &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="complexitemarray" type="[Lcom.interopbridges.scx.mbeans.ComplexClass;"&gt;
     *       &lt;Property Name="complexitemarray" index="0"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *       &lt;Property Name="complexitemarray" index="1"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;ComplexType&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifySingleMBeanWithComplexDataTypeTransformLimitCountToFourteen()
            throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();

        HashMap<String, String[]> Params = null;
        Params = new HashMap<String, String[]>();
        String[] countParam = { "14" };
        String[] depthParam = { "3" };
        Params.put(JmxConstant.STR_MAXCOUNT, countParam);
        Params.put(JmxConstant.STR_MAXDEPTH, depthParam);

        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getComplexTypeObjectInstance(), Params)
                .toString();

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean did not have the correct 'String' property for the theLabel. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getComplexTypeMBean()
                                .getComplexClass().toString(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='childClass']/Property[@Name='parent' and @type='com.interopbridges.scx.mbeans.ComplexClass']");
    }

    /**
     * <p>
     * Test to verify that a MBean that consists of complex class type can be
     * transformed into XML properly. note: The inner class contained within the
     * ComplexClass is not decoded into the XML output. The complex class
     * contains a cyclic property list, these tests are to ensure the override
     * parameters control the output correctly. This specific test is for
     * MaxSize=1100
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.ComplexType" objectName="com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="complexitem" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *       &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *       &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *       &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *         &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"&gt;com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass@5d7a7de4&lt;/Property&gt;
     *       &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="complexitemarray" type="[Lcom.interopbridges.scx.mbeans.ComplexClass;"&gt;
     *       &lt;Property Name="complexitemarray" index="0"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *       &lt;Property Name="complexitemarray" index="1"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;ComplexType&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifySingleMBeanWithComplexDataTypeTransformLimitSizeTo1100Bytes()
            throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();

        HashMap<String, String[]> Params = null;
        Params = new HashMap<String, String[]>();
        String[] depthParam = { "3" };
        String[] sizeParam = { "1100" };
        
        Params.put(JmxConstant.STR_MAXDEPTH, depthParam);
        Params.put(JmxConstant.STR_MAXSIZE, sizeParam);

        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getComplexTypeObjectInstance(), Params)
                .toString();

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "XML for a single ComplexTypeMBean did not have the correct 'String' property for the theLabel. Input was "
                                + xml,
                        xml,
                        FakeJmxGenerator.getComplexTypeMBean()
                                .getComplexClass().toString(),
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='childClass']/Property[@Name='parent' and @type='com.interopbridges.scx.mbeans.ComplexClass']");
    }

    /**
     * <p>
     * Test to verify that a MBean that consists of complex class type can be
     * transformed into XML properly. note: The inner class contained within the
     * ComplexClass is not decoded into the XML output. The complex class
     * contains a cyclic property list, these tests are to ensure the override
     * parameters control the output correctly. This specific test is for
     * MaxSize=1 which creates output xml that has not recursed into any class
     * members.
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.ComplexType" objectName="com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="complexitem" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *     &lt;Property Name="complexitemarray" type="[Lcom.interopbridges.scx.mbeans.ComplexClass;"&gt;
     *       &lt;Property Name="complexitemarray" index="0"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *       &lt;Property Name="complexitemarray" index="1"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;ComplexType&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifySingleMBeanWithComplexDataTypeTransformLimitSizetoZeroBytes()
            throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();

        HashMap<String, String[]> Params = null;
        Params = new HashMap<String, String[]>();
        String[] paramvals =
        { "0" };

        Params.put(JmxConstant.STR_MAXSIZE, paramvals);

        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getComplexTypeObjectInstance(), Params)
                .toString();

        Object[] results = SAXParser
                .XPathQuery(xml,
                        "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='_boolean']");

        Assert
                .assertTrue(
                        "The MBean query should not return the class members due to size limit",
                        results.length == 0);
    }

    /**
     * <p>
     * Unit Test needed for support of JBoss Application Discovery. In
     * Operations Manager, the objectName contains information that we want to
     * represent to the user. Parsing on the OM-side is problematic, so instead
     * we'll opt to do this in the extender.
     * </p>
     * 
     * <p>
     * The returned XML will contain the objectName as a string (i.e. a
     * representation of how the MBean "really" looks), but also an XML block
     * containing
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="org.jboss.management.j2ee.J2EEApplication" objectName="jboss.management.local:J2EEServer=Local,j2eeType=J2EEApplication,name=BeanSpy.ear"&gt;
     *   &lt;Properties&gt;
     *     &lt;objectName type="java.lang.String"&gt;jboss.management.local:J2EEServer=Local,j2eeType=J2EEApplication,name=BeanSpy.ear&lt;/objectName&gt;
     *     &lt;objectNameElements type="objectName"&gt;
     *       &lt;Domain&gt;jboss.management.local&lt;/Domain&gt;
     *       &lt;J2EEServer&gt;Local&lt;/J2EEServer&gt;
     *       &lt;j2eeType&gt;J2EEApplication&lt;/j2eeType&gt;
     *       &lt;name&gt;BeanSpy.ear&lt;/name&gt;
     *     &lt;/objectNameElements&gt;
     *     &lt;Property Name="eventProvider" type="java.lang.Boolean"&gt;false&lt;/Property&gt;
     *     &lt;Property Name="statisticsProvider" type="java.lang.Boolean"&gt;false&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * @throws Exception
     *             If there was a problem transforming a MBean of a JBoss Web
     *             Application
     *
     */
    @Test
    public void verifyObjectNameForJBossMBeanAlsoSentAsXml() throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getFauxJBossManagementMBeanObjectInstance(),
                null).toString();

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "EventProvider does not match: " + xml,
                        xml,
                        FakeJmxGenerator.getFauxJBossWebApplicationMBean()
                                .getEventProvider(),
                        "/MBean[@Name='org.jboss.management.j2ee.J2EEApplication']/Properties/Property[@Name='eventProvider' and @type='java.lang.Boolean']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "StatistcsProvider does not match: " + xml,
                        xml,
                        FakeJmxGenerator.getFauxJBossWebApplicationMBean()
                                .getStatisticsProvider(),
                        "/MBean[@Name='org.jboss.management.j2ee.J2EEApplication']/Properties/Property[@Name='statisticsProvider' and @type='java.lang.Boolean']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "ObjectName should still be returned as a string (in addition to XML): "
                                + xml,
                        xml,
                        FakeJmxGenerator.getFauxJBossWebApplicationMBean()
                                .getObjectName(),
                        "/MBean[@Name='org.jboss.management.j2ee.J2EEApplication']/Properties/objectName[@type='java.lang.String']");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "Domain (as XML) missing J2EEApplication: " + xml,
                        xml,
                        FakeJmxGenerator.getFauxJBossWebApplicationMBean()
                                .get_domain(),
                        "/MBean[@Name='org.jboss.management.j2ee.J2EEApplication']/Properties/objectNameElements[@type='objectName']/Domain");
        
        SAXParser
        .customQueryAssertForXmlValidationOfObjectValue(
                "J2EEApplication (as XML) missing J2EEApplication: " + xml,
                xml,
                FakeJmxGenerator.getFauxJBossWebApplicationMBean()
                                .get_J2EEServer(),
                        "/MBean[@Name='org.jboss.management.j2ee.J2EEApplication']/Properties/objectNameElements[@type='objectName']/J2EEServer");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "j2eeType (as XML) missing J2EEApplication: " + xml,
                        xml,
                        FakeJmxGenerator.getFauxJBossWebApplicationMBean()
                                .get_j2eeType(),
                        "/MBean[@Name='org.jboss.management.j2ee.J2EEApplication']/Properties/objectNameElements[@type='objectName']/j2eeType");

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "name (as XML) missing J2EEApplication: " + xml,
                        xml,
                        FakeJmxGenerator.getFauxJBossWebApplicationMBean()
                                .get_name(),
                        "/MBean[@Name='org.jboss.management.j2ee.J2EEApplication']/Properties/objectNameElements[@type='objectName']/name");
    }

    /**
     * <p>
     * Test to verify that a MBean that containd embedded escape codes within
     * the objectname can be transformed into XML properly. The escape codes are
     * prefixed with an additional escaped percent. So an embedded "%3d" should
     * be transformed into a "%253d" this allows the HTTP transformer to convert
     * it back to "%3d".
     * </p>
     * 
     * <p>
     * The actual object name within the MBeans looks like this:<br>
     * "com.interopbridges.scx:name=com.interopbridges.scx%3aname%3dtest%2cj2eeType%3dJ2EEApplication,size=1"
     * <br>
     * 
     * Ideally, the XML output would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.EscapedObjectName" objectName="com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1"&gt;
     *   &lt;Properties&gt;
     *     &lt;objectName type="java.lang.String"&gt;com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1&lt;/objectName&gt;
     *     &lt;objectNameElements type="objectName"&gt;
     *       &lt;Domain&gt;com.interopbridges.scx&lt;/Domain&gt;
     *       &lt;name&gt;com.interopbridges.scx%3aname%3dtest%2cj2eeType%3dJ2EEApplication&lt;/name&gt;
     *       &lt;size&gt;1&lt;/size&gt;
     *     &lt;/objectNameElements&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;EscapedObjectNameType&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             If there was a problem transforming a ObjectName of the MBean
     */
    @Test
    public void verifyObjectNameForEscapedObjectNameMBean() throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getEscapedObjectNameObjectInstance(),
                null).toString();

        SAXParser
                .customQueryAssertForXmlValidationOfObjectValue(
                        "ObjectName does not match: " + xml,
                        xml,
                        "com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1",
                        "/MBean[@Name='com.interopbridges.scx.mbeans.EscapedObjectName']/Properties/objectName[@type='java.lang.String']");

    }

    /**
     * <p>
     * Verify that the returned XML for MBeans query contains an attribute for
     * version. The actual value cannot be tested because this is something that
     * should be replaced at build-time (i.e. it is non-deterministic).
     * </p>
     *
     * <p>
     * Ideally, the XML output would look like this:<br>
     * 
     * <pre>
     * &lt;MBeans version="NotFromLabel"&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=add"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;add&lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=divide"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;divide&lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     * &lt;/MBeans&gt;
     * </pre>
     * 
     * </p>
     * @throws Exception
     *             if there were some error performing the XML parsing (most
     *             likely)
     */
    @Test
    public void verifyVersionAttributeInMBeansResponse() throws Exception
    {
        HashMap<IJMX, Set<ObjectInstance>> mbeansl = new HashMap<IJMX, Set<ObjectInstance>>();

        Assert.assertEquals("Wrong number of JMX Stores", 1, this._stores
                .size());
        // Input
        Set<ObjectInstance> operations = new HashSet<ObjectInstance>(2);
        operations.add(FakeJmxGenerator.getAddOperationObjectInstance());
        operations.add(FakeJmxGenerator.getDivideOperationObjectInstance());

        // Perform the actual test via helper methods
        mbeansl.put(this._stores.get(0), operations);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformMultipleMBeans(mbeansl, null).toString();

        // Verify results
        String[] results = SAXParser.XPathQuery(xml, "/MBeans/@version");
        Assert.assertTrue(
                "Query for version attribute should produce one result",
                results.length == 1);
    }
    
    
    /**
     * <p>
     * Verify when the size of an output XML file reached the limits (in a single Bean mode),
     * an exception should be thrown.
     * </p>
     *
     * @author Jinlong Li
     *
     */
    @Test
    public void verifyXMLFileSizeExceedLimits_SingleMBean() throws Exception
    {        
        HashMap<String, String[]> Params = new HashMap<String, String[]>();
        Params.put(JmxConstant.STR_MAXDEPTH, new String [] { "65000" });
        String JMXQuery = null;
        JMXQuery = "JMXQuery=com.interopbridges.scx:jmxType=operationCall";
        
        try{
           IJMX tmpStore = _stores.get(0);
           MBeanTransformer sut = new MBeanTransformer();
           sut.setJMXQuery(JMXQuery);            
           sut.transformSingleMBean(tmpStore,
           FakeJmxGenerator.getComplexTypeObjectInstanceForMaxFileSize(), Params);
           Assert.fail("Function should fail because the size of the xml response exceeds the limit.");
        }
        catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ScxException", e
                            .getClass() == ScxException.class);
            Assert
                    .assertEquals(
                            "Should throw an ERROR_TRANSFORMING_MBEAN exception, but instead got " + 
                            ((ScxException) e).getExceptionCode().getCode(),
                            ((ScxException) e).getExceptionCode(),
                            ScxExceptionCode.ERROR_TRANSFORMING_MBEAN);
            Assert
                    .assertEquals(
                    "Should throw an ERROR_SIZE_OF_XML_FILES_EXCEED_LIMITS exception",
                    ((ScxException) (e.getCause())).getExceptionCode(),
                    ScxExceptionCode.ERROR_SIZE_OF_XML_FILES_EXCEED_LIMITS);

            Assert
                    .assertTrue(
                            "Exception does not contain the correct argument parameters",
                            e.getCause().getMessage().equals(
                            new ScxException(
                                    ScxExceptionCode.ERROR_SIZE_OF_XML_FILES_EXCEED_LIMITS,
                                    new Object[]
                                    {
                                            new Integer(
                                                    JmxConstant.ABS_MAX_XML_SIZE),
                                            JMXQuery }).getMessage()));

        }
    }
    
    /**
     * <p>
     * Verify when the size of an output XML file does not reach the limits,
     * the program should be running normally.
     * </p>
     *
     * @author Jinlong Li
     *
     */
    @Test
    public void verifyXMLFileSizeNotExceedLimits_SingleMBean() throws Exception
    {
        
        HashMap<String, String[]> Params = null;
        
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();         
        String xml = sut.transformSingleMBean(tmpStore,FakeJmxGenerator.getComplexTypeObjectInstance(), Params).toString();
        Assert.assertTrue("The size of the output file should not exceeds the limits" , xml.length() < JmxConstant.ABS_MAX_XML_SIZE); 
    }
    
    /**
     * <p>
     * Verify when the size of an output XML file reached the limits (in a multiple Bean mode),
     * an exception should be thrown.
     * </p>
     *
     * @author Jinlong Li
     */   
    @Test
    public void verifyXMLFileSizeExceedLimits_MultipleMBeans() throws Exception
    {
        HashMap<IJMX, Set<ObjectInstance>> mbeansl = new HashMap<IJMX, Set<ObjectInstance>>();      
        HashMap<String, String[]> Params = new HashMap<String, String[]>();
        Params.put(JmxConstant.STR_MAXDEPTH, new String [] { "65000" });

        String JMXQuery = null;
        JMXQuery = "JMXQuery=com.interopbridges.scx:jmxType=operationCall";

        Set<ObjectInstance> complexTypeMBeans = new HashSet<ObjectInstance>(2);           
        complexTypeMBeans.add(FakeJmxGenerator.getComplexTypeObjectInstance());
        complexTypeMBeans.add(FakeJmxGenerator.getComplexTypeObjectInstanceForMaxFileSize());        
        mbeansl.put(this._stores.get(0), complexTypeMBeans);
        MBeanTransformer sut = new MBeanTransformer();
        sut.setJMXQuery(JMXQuery);        
        try{
            sut.transformMultipleMBeans(mbeansl, Params);
            Assert.fail("Function should fail because the size of the xml response exceeds the limit.");
        }
        catch (Exception e)
        {
            Object[] args = {new Integer(JmxConstant.ABS_MAX_XML_SIZE), JMXQuery};   
            String formattedMessage =  MessageFormat.format(new ScxException(
                ScxExceptionCode.ERROR_SIZE_OF_XML_FILES_EXCEED_LIMITS).getMessage(), args );
            Assert.assertTrue("Exception should have been of type ScxException", 
                e.getClass() == ScxException.class);
            Assert.assertTrue("Should throw an ERROR_SIZE_OF_XML_FILE_EXCEEDS_LIMITS exception" + e.getMessage(),
                e.getCause().toString().contains(formattedMessage));
        }
   }
    
    /**
     * <p>
     * This test is a combined test to check that the onjectName attribute exists 
     * for each MBean, and to verify that a MBean that contains embedded escape codes 
     * within the objectname can be transformed into XML properly. The escape codes 
     * are prefixed with an additional escaped percent. So an embedded "%3d" should
     * be transformed into a "%253d" this allows the HTTP transformer to convert
     * it back to "%3d".
     * </p>
     * 
     * <p>
     * The actual object name within the MBeans looks like this:<br>
     * "com.interopbridges.scx:name=com.interopbridges.scx%3aname%3dtest%2cj2eeType%3dJ2EEApplication,size=1"
     * <br>
     * 
     * Ideally, the XML output would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.EscapedObjectName" objectName="com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1"&gt;
     *   &lt;Properties&gt;
     *     &lt;objectName type="java.lang.String"&gt;com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1&lt;/objectName&gt;
     *     &lt;objectNameElements type="objectName"&gt;
     *       &lt;Domain&gt;com.interopbridges.scx&lt;/Domain&gt;
     *       &lt;name&gt;com.interopbridges.scx%3aname%3dtest%2cj2eeType%3dJ2EEApplication&lt;/name&gt;
     *       &lt;size&gt;1&lt;/size&gt;
     *     &lt;/objectNameElements&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;EscapedObjectNameType&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             If there was a problem transforming a ObjectName of the MBean
     */
    @Test
    public void verifyObjectNameAttribute_ForEscapedObjectNameMBean() throws Exception
    {
        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getEscapedObjectNameObjectInstance(),
                null).toString();

        SAXParser
        .customQueryAssertForXmlValidationOfObjectValue(
                "ObjectName does not match: " + xml,
                xml,
                "com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1",
                "/MBean[@Name='com.interopbridges.scx.mbeans.EscapedObjectName']/Properties/objectName[@type='java.lang.String']");

        SAXParser
        .customQueryAssertForXmlValidationOfObjectValue(
                "ObjectName attribute does not match: " + xml,
                xml,
                "com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1",
                "/MBean[@Name='com.interopbridges.scx.mbeans.EscapedObjectName']/@objectName");
    }
    
    /**
     * <p>
     * This test is to verify that when using the JMXFilter that the data returned is filtered.
     * This specific test filters out a single attribute (theLabel) for the specified MBean 
     * (com.interopbridges.scx:*) for all registered JMXStores (*).
     * </p>
     * 
     * <p>
     * The actual object name within the MBeans looks like this:<br>
     * "com.interopbridges.scx:name=com.interopbridges.scx%3aname%3dtest%2cj2eeType%3dJ2EEApplication,size=1"
     * <br>
     * 
     * The original XML output would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.EscapedObjectName" objectName="com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1"&gt;
     *   &lt;Properties&gt;
     *     &lt;objectName type="java.lang.String"&gt;com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1&lt;/objectName&gt;
     *     &lt;objectNameElements type="objectName"&gt;
     *       &lt;Domain&gt;com.interopbridges.scx&lt;/Domain&gt;
     *       &lt;name&gt;com.interopbridges.scx%3aname%3dtest%2cj2eeType%3dJ2EEApplication&lt;/name&gt;
     *       &lt;size&gt;1&lt;/size&gt;
     *     &lt;/objectNameElements&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;EscapedObjectNameType&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * <p>
     * The Filtered XML output would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.EscapedObjectName" objectName="com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1"&gt;
     *   &lt;Properties&gt;
     *     &lt;objectName type="java.lang.String"&gt;com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1&lt;/objectName&gt;
     *     &lt;objectNameElements type="objectName"&gt;
     *       &lt;Domain&gt;com.interopbridges.scx&lt;/Domain&gt;
     *       &lt;name&gt;com.interopbridges.scx%3aname%3dtest%2cj2eeType%3dJ2EEApplication&lt;/name&gt;
     *       &lt;size&gt;1&lt;/size&gt;
     *     &lt;/objectNameElements&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     *   
     * @throws Exception
     *             If there was a problem transforming a ObjectName of the MBean
     */
    @Test
    public void verifySingleFilteredAttribute_ForMBean_positive() throws Exception
    {
        
        _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<JMXQuery>" +
                        "<Exclude>" +
                            "<JMXStore Name=\"*\">" +
                                "<MBeanObjectName Name=\"com.interopbridges.scx:*\">" +
                                    "<Attribute>theLabel</Attribute>" +
                                "</MBeanObjectName>" +
                            "</JMXStore>" +
                        "</Exclude>" +
                    "</JMXQuery>"));

        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getEscapedObjectNameObjectInstance(),
                null).toString();

        SAXParser
        .customQueryAssertForXmlValidationOfObjectValue(
                "ObjectName does not match: " + xml,
                xml,
                "com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1",
                "/MBean[@Name='com.interopbridges.scx.mbeans.EscapedObjectName']/Properties/objectName[@type='java.lang.String']");

        String[] s = SAXParser.XPathQuery(xml, "/MBean[@Name='com.interopbridges.scx.mbeans.EscapedObjectName']/Properties/Property[@Name='theLabel']");
        Assert.assertTrue("Invalid entry, exclusion not working", s.length==0);        

    }

    /**
     * <p>
     * This test is to verify that when using the JMXFilter that the data returned is not 
     * accidentally filtered.
     * This specific test filters out a single attribute (theLabel) for the specified MBean 
     * (com.interopbridges.scx:*) for an incorrect JMXStore (invalidJMXStore).
     * </p>
     * 
     * <p>
     * The actual object name within the MBeans looks like this:<br>
     * "com.interopbridges.scx:name=com.interopbridges.scx%3aname%3dtest%2cj2eeType%3dJ2EEApplication,size=1"
     * <br>
     * 
     * The original XML output would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.EscapedObjectName" objectName="com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1"&gt;
     *   &lt;Properties&gt;
     *     &lt;objectName type="java.lang.String"&gt;com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1&lt;/objectName&gt;
     *     &lt;objectNameElements type="objectName"&gt;
     *       &lt;Domain&gt;com.interopbridges.scx&lt;/Domain&gt;
     *       &lt;name&gt;com.interopbridges.scx%3aname%3dtest%2cj2eeType%3dJ2EEApplication&lt;/name&gt;
     *       &lt;size&gt;1&lt;/size&gt;
     *     &lt;/objectNameElements&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;EscapedObjectNameType&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * <p>
     * The Filtered XML output would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.EscapedObjectName" objectName="com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1"&gt;
     *   &lt;Properties&gt;
     *     &lt;objectName type="java.lang.String"&gt;com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1&lt;/objectName&gt;
     *     &lt;objectNameElements type="objectName"&gt;
     *       &lt;Domain&gt;com.interopbridges.scx&lt;/Domain&gt;
     *       &lt;name&gt;com.interopbridges.scx%3aname%3dtest%2cj2eeType%3dJ2EEApplication&lt;/name&gt;
     *       &lt;size&gt;1&lt;/size&gt;
     *     &lt;/objectNameElements&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;EscapedObjectNameType&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * @throws Exception
     *             If there was a problem transforming a ObjectName of the MBean
     */
    @Test
    public void verifySingleFilteredAttribute_ForMBean_negative() throws Exception
    {
        _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<JMXQuery>" +
                        "<Exclude>" +
                            "<JMXStore Name=\"invalidJMXStore\">" +
                                "<MBeanObjectName Name=\"com.interopbridges.scx:*\">" +
                                    "<Attribute>theLabel</Attribute>" +
                                "</MBeanObjectName>" +
                            "</JMXStore>" +
                        "</Exclude>" +
                    "</JMXQuery>"));

        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getEscapedObjectNameObjectInstance(),
                null).toString();

        SAXParser
        .customQueryAssertForXmlValidationOfObjectValue(
                "ObjectName does not match: " + xml,
                xml,
                "com.interopbridges.scx:name=com.interopbridges.scx%253aname%253dtest%252cj2eeType%253dJ2EEApplication,size=1",
                "/MBean[@Name='com.interopbridges.scx.mbeans.EscapedObjectName']/Properties/objectName[@type='java.lang.String']");

        String[] s = SAXParser.XPathQuery(xml, "/MBean[@Name='com.interopbridges.scx.mbeans.EscapedObjectName']/Properties/Property[@Name='theLabel']");
        Assert.assertTrue("Invalid entry, the exclusion should not work because of an invalid JMXStore name", s.length==1);        

    }
    
    /**
     * <p>
     * This test is to verify that when using the JMXFilter that the data returned is correctly filtered.
     * This specific test filters out a multiple attributes (theLabel,childClass,complexitemarray) for the specified MBean 
     * (com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType) for the correct JMXStore 
     * (com.interopbridges.scx.jmx.MockJmx).
     * </p>
     * 
     * <p>
     * The original XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBean Name="com.interopbridges.scx.mbeans.ComplexType" objectName="com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="complexitem" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *       &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *       &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *       &lt;Property Name="childClass" type="com.interopbridges.scx.mbeans.ComplexRecursiveClass"&gt;
     *         &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *         &lt;Property Name="parent" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *       &lt;/Property&gt;
     *       &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"&gt;com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass@5d7a7de4&lt;/Property&gt;
     *       &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="complexitemarray" type="[Lcom.interopbridges.scx.mbeans.ComplexClass;"&gt;
     *       &lt;Property Name="complexitemarray" index="0"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *       &lt;Property Name="complexitemarray" index="1"&gt;serialVersionUID 999999999999999999&lt;/Property&gt;
     *     &lt;/Property&gt;
     *     &lt;Property Name="theLabel" type="java.lang.String"&gt;ComplexType&lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;
     * </pre>
     * 
     * </p>
     * 
     * <p>
     * The filtered XML would look like this:<br>
     * 
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
     * &lt;MBean Name="com.interopbridges.scx.mbeans.ComplexType" objectName="com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType"&gt;
     *   &lt;Properties&gt;
     *     &lt;Property Name="complexitem" type="com.interopbridges.scx.mbeans.ComplexClass"&gt;
     *       &lt;Property Name="_boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="_byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="_character" type="java.lang.Character"&gt;x&lt;/Property&gt;
     *       &lt;Property Name="_double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="_float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="_int" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="_long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="_short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="boolean" type="java.lang.Boolean"&gt;true&lt;/Property&gt;
     *       &lt;Property Name="byte" type="java.lang.Byte"&gt;127&lt;/Property&gt;
     *       &lt;Property Name="character" type="java.lang.Character"&gt;z&lt;/Property&gt;
     *       &lt;Property Name="double" type="java.lang.Double"&gt;1.7976931348623157E308&lt;/Property&gt;
     *       &lt;Property Name="float" type="java.lang.Float"&gt;3.4028235E38&lt;/Property&gt;
     *       &lt;Property Name="integer" type="java.lang.Integer"&gt;2147483647&lt;/Property&gt;
     *       &lt;Property Name="long" type="java.lang.Long"&gt;9223372036854775807&lt;/Property&gt;
     *       &lt;Property Name="myClass" type="com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass"&gt;com.interopbridges.scx.mbeans.ComplexClass$anonInnerClass@6a755a7a&lt;/Property&gt;
     *       &lt;Property Name="short" type="java.lang.Short"&gt;32767&lt;/Property&gt;
     *       &lt;Property Name="string" type="java.lang.String"&gt;Some String&lt;/Property&gt;
     *     &lt;/Property&gt;
     *   &lt;/Properties&gt;
     * &lt;/MBean&gt;     * &lt;/pre&gt;
     * 
     * </p>
     * 
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifyMultipleFilteredAttributes_ForMBean_positive() throws Exception
    {
        _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<JMXQuery>" +
                        "<Exclude>" +
                            "<JMXStore Name=\"com.interopbridges.scx.jmx.MockJmx\">" +
                                "<MBeanObjectName Name=\"com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType\">" +
                                "<Attribute>theLabel</Attribute>" +
                                "<Attribute>childClass</Attribute>" +
                                "<Attribute>complexitemarray</Attribute>" +
                                "</MBeanObjectName>" +
                            "</JMXStore>" +
                        "</Exclude>" +
                    "</JMXQuery>"));

        Assert.assertTrue(
                "The number of MBean Stores is not equal to 1. Input was "
                        + _stores.size(), _stores.size() == 1);
        IJMX tmpStore = _stores.get(0);
        MBeanTransformer sut = new MBeanTransformer();
        
        HashMap<String, String[]> Params = new HashMap<String, String[]>();
        String[] paramvals = { "1100" };
        
        Params.put(JmxConstant.STR_MAXSIZE, paramvals);
        
        String xml = sut.transformSingleMBean(tmpStore,
                FakeJmxGenerator.getComplexTypeObjectInstance(), Params)
                .toString();

        SAXParser
        .customQueryAssertForXmlValidationOfObjectValue(
                "ObjectName does not match: " + xml,
                xml,
                "com.interopbridges.scx:complexitem=serialVersionUID 999999999999999999,theLabel=ComplexType",
                "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/@objectName");

        String[] s = SAXParser.XPathQuery(xml, "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='theLabel']");
        Assert.assertTrue("Invalid entry, the exclusion should work", s.length==0);
        
        s = SAXParser.XPathQuery(xml, "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='childClass']");
        Assert.assertTrue("Invalid entry, the exclusion should work", s.length==0);        

        s = SAXParser.XPathQuery(xml, "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='complexitemarray']");
        Assert.assertTrue("Invalid entry, the exclusion should work", s.length==0);        

        s = SAXParser.XPathQuery(xml, "/MBean[@Name='com.interopbridges.scx.mbeans.ComplexType']/Properties/Property[@Name='complexitem']/Property[@Name='_double']");
        Assert.assertTrue("Invalid entry, the exclusion has filtered too much data", s.length==1);        
    }
    
    /**
     * <p>
     * This test is to verify that when using the JMXFilter that the data returned is correctly filtered.
     * This specific test filters out the (jmxType)attribute for the specified MBeans 
     * (com.interopbridges.scx:*) for the correct JMXStore 
     * (com.interopbridges.scx.jmx.MockJmx).
     * </p>
     * 
     * <p>
     * The original XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBeans version="NotFromLabel"&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=add"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;add&lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=divide"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;divide&lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     * &lt;/MBeans&gt;
     * </pre>
     * 
     * </p>
     *
     * <p>
     * The filtered XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBeans version="NotFromLabel"&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=add"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;add&lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=divide"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;divide&lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     * &lt;/MBeans&gt;
     * </pre>
     * 
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifyFilteredAttributes_ForMultipleMBeans_positive() throws Exception
    {
        HashMap<IJMX, Set<ObjectInstance>> mbeansl = new HashMap<IJMX, Set<ObjectInstance>>();

        // setup the exclusions for the MBeans
        _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<JMXQuery>" +
                        "<Exclude>" +
                            "<JMXStore Name=\"com.interopbridges.scx.jmx.MockJmx\">" +
                                "<MBeanObjectName Name=\"com.interopbridges.scx:*\">" +
                                "<Attribute>jmxType</Attribute>" +
                                "</MBeanObjectName>" +
                            "</JMXStore>" +
                        "</Exclude>" +
                    "</JMXQuery>"));
        
        Assert.assertEquals("Wrong number of JMX Stores", 1, this._stores
                .size());

        // Input
        Set<ObjectInstance> operations = new HashSet<ObjectInstance>(2);
        
        // ArrayList<ObjectInstance> operations = new
        // ArrayList<ObjectInstance>(2);
        operations.add(FakeJmxGenerator.getAddOperationObjectInstance());
        operations.add(FakeJmxGenerator.getDivideOperationObjectInstance());

        // Results to Check (1/2: list of operation names)
        ArrayList<Object> nameList = new ArrayList<Object>(operations
                .size());
        nameList.add(FakeJmxGenerator.getAddOperationMBean().getName());
        nameList.add(FakeJmxGenerator.getDivideOperationMBean().getName());

        // Results to Check (2/2: list of JMX Type)
        ArrayList<Object> jmxList = new ArrayList<Object>(operations.size());
        jmxList.add(FakeJmxGenerator.getAddOperationMBean().getJmxType());
        jmxList
                .add(FakeJmxGenerator.getDivideOperationMBean()
                        .getJmxType());

        // Perform the actual test via helper methods

        mbeansl.put(this._stores.get(0), operations);

        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformMultipleMBeans(mbeansl, null).toString();
        
        SAXParser
                .customQueryAssertForXmlValidationOfListOfObjectValues(
                        "XML for a single OperationMBean did not have the correct 'name' property. Input was "
                                + xml,
                        xml,
                        nameList,
                        "/MBeans/MBean[@Name='com.interopbridges.scx.webservices.Operation']/Properties/Property[@Name='name' and @type='java.lang.String']");
        
        String[] s;
        s = SAXParser.XPathQuery(xml, "/MBeans/MBean[@objectName='com.interopbridges.scx:jmxType=operation,name=add']/Properties/Property[@Name='jmxType']");
        Assert.assertTrue("Invalid entry, the exclusion should work", s.length==0);
        
        s = SAXParser.XPathQuery(xml, "/MBeans/MBean[@objectName='com.interopbridges.scx:jmxType=operation,name=divide']/Properties/Property[@Name='jmxType']");
        Assert.assertTrue("Invalid entry, the exclusion should work", s.length==0);
        
        s = SAXParser.XPathQuery(xml, "/MBeans/MBean[@objectName='com.interopbridges.scx:jmxType=operation,name=add']/Properties/Property[@Name='name']");
        Assert.assertTrue("Invalid entry, the exclusion should work", s.length==1);
        
        s = SAXParser.XPathQuery(xml, "/MBeans/MBean[@objectName='com.interopbridges.scx:jmxType=operation,name=divide']/Properties/Property[@Name='name']");
        Assert.assertTrue("Invalid entry, the exclusion should work", s.length==1);
    }
    
    /**
     * <p>
     * This test is to verify that when using the JMXFilter that the data returned is correctly filtered.
     * This specific test filters out all attribute for the specified MBean 
     * (com.interopbridges.scx:jmxType=operation,name=divide) for the correct JMXStore 
     * (com.interopbridges.scx.jmx.MockJmx).
     * </p>
     * 
     * <p>
     * The original XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBeans version="NotFromLabel"&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=add"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;add&lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=divide"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;divide&lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     * &lt;/MBeans&gt;
     * </pre>
     * 
     * </p>
     *
     * <p>
     * The filtered XML would look like this:<br>
     * 
     * <pre>
     * &lt;MBeans version="NotFromLabel"&gt;
     *   &lt;MBean Name="com.interopbridges.scx.webservices.Operation" objectName="com.interopbridges.scx:jmxType=operation,name=add"&gt;
     *     &lt;Properties&gt;
     *       &lt;Property Name="jmxType" type="java.lang.String"&gt;operation&lt;/Property&gt;
     *       &lt;Property Name="name" type="java.lang.String"&gt;add&lt;/Property&gt;
     *     &lt;/Properties&gt;
     *   &lt;/MBean&gt;
     * &lt;/MBeans&gt;
     * </pre>
     * 
     * </p>
     *
     * @throws Exception
     *             In the event that something goes wrong. This is the 'golden
     *             path', so there should not be any errors.
     */
    @Test
    public void verifyFilteredAllAttributes_ForMultipleMBeans_positive() throws Exception
    {
        HashMap<IJMX, Set<ObjectInstance>> mbeansl = new HashMap<IJMX, Set<ObjectInstance>>();

        // setup the exclusions for the MBeans
        _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<JMXQuery>" +
                        "<Exclude>" +
                            "<JMXStore Name=\"com.interopbridges.scx.jmx.MockJmx\">" +
                                "<MBeanObjectName Name=\"com.interopbridges.scx:jmxType=operation,name=divide\">" +
                                "<Attribute>*</Attribute>" +
                                "</MBeanObjectName>" +
                            "</JMXStore>" +
                        "</Exclude>" +
                    "</JMXQuery>"));
        
        Assert.assertEquals("Wrong number of JMX Stores", 1, this._stores
                .size());

        // Input
        Set<ObjectInstance> operations = new HashSet<ObjectInstance>(2);
        
        // ArrayList<ObjectInstance> operations = new
        // ArrayList<ObjectInstance>(2);
        operations.add(FakeJmxGenerator.getAddOperationObjectInstance());
        operations.add(FakeJmxGenerator.getDivideOperationObjectInstance());

        // Results to Check (1/2: list of operation names)
        ArrayList<Object> nameList = new ArrayList<Object>(operations
                .size());
        nameList.add(FakeJmxGenerator.getAddOperationMBean().getName());
        nameList.add(FakeJmxGenerator.getDivideOperationMBean().getName());

        // Results to Check (2/2: list of JMX Type)
        ArrayList<Object> jmxList = new ArrayList<Object>(operations.size());
        jmxList.add(FakeJmxGenerator.getAddOperationMBean().getJmxType());
        jmxList
                .add(FakeJmxGenerator.getDivideOperationMBean()
                        .getJmxType());

        // Perform the actual test via helper methods

        mbeansl.put(this._stores.get(0), operations);

        MBeanTransformer sut = new MBeanTransformer();
        String xml = sut.transformMultipleMBeans(mbeansl, null).toString();
        
        String[] s;
        s = SAXParser.XPathQuery(xml, "/MBeans/MBean");
        Assert.assertTrue("There should only be one MBean returned", s.length==1);
        
        s = SAXParser.XPathQuery(xml, "/MBeans/MBean[@objectName='com.interopbridges.scx:jmxType=operation,name=add']/Properties/Property[@Name='jmxType']");
        Assert.assertTrue("The 'jmxType' attribute should exist for the 'com.interopbridges.scx:jmxType=operation,name=add' mbean", s.length==1);
        
        s = SAXParser.XPathQuery(xml, "/MBeans/MBean[@objectName='com.interopbridges.scx:jmxType=operation,name=add']/Properties/Property[@Name='name']");
        Assert.assertTrue("The 'name' attribute should exist for the 'com.interopbridges.scx:jmxType=operation,name=add' mbean", s.length==1);
    }
    
}
