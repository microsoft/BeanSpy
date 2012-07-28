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

import java.io.StringReader;
import java.io.BufferedReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.util.JmxConstant;

/**
 * Testing the XML decoder associated with the POST request to Invoke a MBean method.<br>
 * 
 * @author Geoff Erasmus
 * 
 */
public class InvokeDecoderTest {
    /**
     * <p>
     * Test Setup/preparation method that resets/initializes all test specific
     * variables.
     * </p>
     */
    @Before
    public void setup() {
    }

    /**
     * <p>
     * Method invoked after each unit-test in this class.
     * </p>
     */
    @After
    public void TearDown() {
    }

    /**
     * <p>
     * Verify the input stream is valid and contains the correct amount of data. <br>
     * If the buffer does not contain the amount of data specified an error should be thrown.
     * </p>
     */
    @Test
    public void verifyTooLittleBodyData() throws Exception 
    {
        try
        {
            // We expect 100 chars of data but only supply 8
            new InvokeDecoder(new BufferedReader(new StringReader("a string")), 100);
            Assert.fail("Failed to receive an exception on instantiating an InvokeDecoder.");
        }
        catch(Exception e)
        {
            Assert.assertTrue(
                    "An SCXException should have been thrown indicating too little data was read", 
                    (e.getClass() == ScxException.class)&&
                     (((ScxException)e).getExceptionCode().equals(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML)));
        }
    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the correct amount of data. <br>
     * If the buffer contains more data than the amount specified, the extraneous data will be ignored.
     * </p>
     */
    @Test
    public void verifyExtraBodyData() throws Exception 
    {
        String str = "a string";
         InvokeDecoder id = new InvokeDecoder(new BufferedReader(new StringReader(str)), 5);
         Assert.assertEquals(
                    "Instantiation of InvokeDecoder for a buffer containing extraneous data failed",
                    5, id.getRawInputData().length());
    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the correct amount of data. <br>
     * If the size specified is 0 an error should be thrown.
     * </p>
     */
    @Test
    public void verifyNoBodyData() throws Exception 
    {
        BufferedReader br = new BufferedReader(new StringReader("a string"));
        
        try
        {
            new InvokeDecoder(br, 0);
            Assert.fail("Failed to receive an exception on instantiating an InvokeDecoder.");
        }
        catch(Exception e)
        {
            Assert.assertTrue( 
                    "An exception should have been thrown indicating the input length is zero", 
                    (e.getClass() == ScxException.class)&&
                    (((ScxException)e).getExceptionCode().equals(ScxExceptionCode.ERROR_INVOKE_NO_BODY)));
        }
    }

    /**
     * <p>
     * Verify the input stream is valid and contains the correct amount of data. <br>
     * If the input BufferedReader is not specified an error should be thrown.
     * </p>
     */
    @Test
    public void verifyNoReader() throws Exception 
    {
        try
        {
            new InvokeDecoder(null, 0);
            Assert.fail("Failed to receive an exception on instantiating an InvokeDecoder.");
        }
        catch(Exception e)
        {
            Assert.assertTrue( 
                    "An exception should have been thrown indicating the input length is zero", 
                    (e.getClass() == ScxException.class)&&
                    (((ScxException)e).getExceptionCode().equals(ScxExceptionCode.ERROR_INVOKE_NO_BODY)));
        }

    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the correct amount of data. <br>
     * If the length of the data exceeds the maximum allowed an error is thrown.  
     * </p>
     */
    @Test
    public void verifyTooMuchBodyData() throws Exception 
    {
        BufferedReader br = new BufferedReader(new StringReader("a string"));
        
        try
        {
            new InvokeDecoder(br, JmxConstant.MAX_POST_INPUT_XML_SIZE + 1);
            Assert.fail("Failed to receive an exception on instantiating an InvokeDecoder.");
        }
        catch(Exception e)
        {
            Assert.assertTrue( 
                    "An exception should have been thrown indicating the input length is too big.", 
                    (e.getClass() == ScxException.class)&&
                    (((ScxException)e).getExceptionCode().equals(ScxExceptionCode.ERROR_INVOKE_BODY_TOO_LARGE)));
        }

    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the correct Invoke XML element. <br>
     * </p>
     * 
     * <p>
     * Ideally, the XML would look something like this:<br>
     * </pre>
     *  &lt;Invoke&rt;
     *      &lt;BeanObjectName&rt;TestMBean&lt;/BeanObjectName&rt;
     *      &lt;Method name="VoidVoidMethod"&rt;&lt;/Method&rt;
     *  &lt;/Invoke&rt;"
     * </p>
     */
    @Test
    public void verifyXMLInvokeElement() throws Exception 
    {
        StringBuffer XMLData = new StringBuffer().
        append("<aInvoke>").
        append("<BeanObjectName>TestMBean</BeanObjectName>").
        append("<Method name=\"VoidVoidMethod\"></Method>").
        append("</aInvoke>");
        
        BufferedReader br = new BufferedReader(new StringReader(XMLData.toString()));
        
        try
        {
            InvokeDecoder id = new InvokeDecoder(br, XMLData.length());
            id.DecodeInput();
            Assert.fail("Failed to receive an exception on decoding the XML input");
        }
        catch(Exception e)
        {
            Assert.assertTrue( 
                    "An exception should have been thrown indicating XML input is incorrect.", 
                    (e.getClass() == ScxException.class)&&
                    (((ScxException)e).getExceptionCode().equals(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML)));
        }

    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the Invoke XML element. <br>
     * </p>
     * 
     * <p>
     * Ideally, the XML would look something like this:<br>
     * </pre>
     *  &lt;Invoke&rt;
     *      &lt;BeanObjectName&rt;TestMBean&lt;/BeanObjectName&rt;
     *      &lt;Method name="VoidVoidMethod"&rt;&lt;/Method&rt;
     *  &lt;/Invoke&rt;"
     * </p>
     */
    @Test
    public void verifyXMLInvokeElementExists() throws Exception 
    {
        StringBuffer XMLData = new StringBuffer().
        append("<BeanObjectName>TestMBean</BeanObjectName>").
        append("<Method name=\"VoidVoidMethod\"></Method>");
        
        BufferedReader br = new BufferedReader(new StringReader(XMLData.toString()));
        
        try
        {
            InvokeDecoder id = new InvokeDecoder(br, XMLData.length());
            id.DecodeInput();
            Assert.fail("Failed to receive an exception on decoding the XML input");
        }
        catch(Exception e)
        {
            Assert.assertTrue( 
                    "An exception should have been thrown indicating XML input is incorrect.", 
                    (e.getClass() == ScxException.class)&&
                    (((ScxException)e).getExceptionCode().equals(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML)));
        }

    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the BeanObjectName XML element. <br>
     * </p>
     * 
     * <p>
     * Ideally, the XML would look something like this:<br>
     * </pre>
     *  &lt;Invoke&rt;
     *      &lt;BeanObjectName&rt;TestMBean&lt;/BeanObjectName&rt;
     *      &lt;Method name="VoidVoidMethod"&rt;&lt;/Method&rt;
     *  &lt;/Invoke&rt;"
     * </p>
     */
    @Test
    public void verifyXMLBeanObjectNameElementExists() throws Exception 
    {
        StringBuffer XMLData = new StringBuffer().
        append("<Invoke>").
        append("<Method name=\"VoidVoidMethod\"></Method>").
        append("</Invoke>");
        
        BufferedReader br = new BufferedReader(new StringReader(XMLData.toString()));
        
        try
        {
            InvokeDecoder id = new InvokeDecoder(br, XMLData.length());
            id.DecodeInput();
            Assert.fail("Failed to receive an exception on instantiating an InvokeDecoder.");
        }
        catch(Exception e)
        {
            Assert.assertTrue( 
                    "An exception should have been thrown indicating XML input is incorrect.", 
                    (e.getClass() == ScxException.class)&&
                    (((ScxException)e).getExceptionCode().equals(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML)));
        }

    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the correct BeanObjectName XML element. <br>
     * </p>
     * 
     * <p>
     * Ideally, the XML would look something like this:<br>
     * </pre>
     *  &lt;Invoke&rt;
     *      &lt;BeanObjectName&rt;TestMBean&lt;/BeanObjectName&rt;
     *      &lt;Method name="VoidVoidMethod"&rt;&lt;/Method&rt;
     *  &lt;/Invoke&rt;"
     * </p>
     */
    @Test
    public void verifyXMLBeanObjectNameElement() throws Exception 
    {
        StringBuffer XMLData = new StringBuffer().
        append("<Invoke>").
        append("<aBeanObjectName>TestMBean</aBeanObjectName>").
        append("<Method name=\"VoidVoidMethod\"></Method>").
        append("</Invoke>");
        
        BufferedReader br = new BufferedReader(new StringReader(XMLData.toString()));
        
        try
        {
            InvokeDecoder id = new InvokeDecoder(br, XMLData.length());
            id.DecodeInput();
            Assert.fail("Failed to receive an exception on instantiating an InvokeDecoder.");
        }
        catch(Exception e)
        {
            Assert.assertTrue( 
                    "An exception should have been thrown indicating XML input is incorrect.", 
                    (e.getClass() == ScxException.class)&&
                    (((ScxException)e).getExceptionCode().equals(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML)));
        }

    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the Method XML element. <br>
     * </p>
     * 
     * <p>
     * Ideally, the XML would look something like this:<br>
     * </pre>
     *  &lt;Invoke&rt;
     *      &lt;BeanObjectName&rt;TestMBean&lt;/BeanObjectName&rt;
     *      &lt;Method name="VoidVoidMethod"&rt;&lt;/Method&rt;
     *  &lt;/Invoke&rt;"
     * </p>
     */
    @Test
    public void verifyXMLMethodElementExists() throws Exception 
    {
        StringBuffer XMLData = new StringBuffer().
        append("<Invoke>").
        append("<BeanObjectName>TestMBean</BeanObjectName>").
        append("<aMethod name=\"VoidVoidMethod\"></aMethod>").
        append("</Invoke>");
        
        BufferedReader br = new BufferedReader(new StringReader(XMLData.toString()));
        
        try
        {
            InvokeDecoder id = new InvokeDecoder(br, XMLData.length());
            id.DecodeInput();
            Assert.fail("Failed to receive an exception on instantiating an InvokeDecoder.");
        }
        catch(Exception e)
        {
            Assert.assertTrue( 
                    "An exception should have been thrown indicating XML input is incorrect.", 
                    (e.getClass() == ScxException.class)&&
                    (((ScxException)e).getExceptionCode().equals(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML)));
        }

    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the correct Method XML element. <br>
     * </p>
     * 
     * <p>
     * Ideally, the XML would look something like this:<br>
     * </pre>
     *  &lt;Invoke&rt;
     *      &lt;BeanObjectName&rt;TestMBean&lt;/BeanObjectName&rt;
     *      &lt;Method name="VoidVoidMethod"&rt;&lt;/Method&rt;
     *  &lt;/Invoke&rt;"
     * </p>
     */
    @Test
    public void verifyXML_NoMethodElement() throws Exception 
    {
        StringBuffer XMLData = new StringBuffer().
        append("<Invoke>").
        append("<BeanObjectName>TestMBean</BeanObjectName>").
        append("</Invoke>");
        
        BufferedReader br = new BufferedReader(new StringReader(XMLData.toString()));
        
        try
        {
            InvokeDecoder id = new InvokeDecoder(br, XMLData.length());
            id.DecodeInput();
            Assert.fail("Failed to receive an exception on instantiating an InvokeDecoder.");
        }
        catch(Exception e)
        {
            Assert.assertTrue( 
                    "An exception should have been thrown indicating XML input is incorrect.", 
                    (e.getClass() == ScxException.class)&&
                    (((ScxException)e).getExceptionCode().equals(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML)));
        }

    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the correct Parameter Type attribute
     * if the parameter is specified. <br>
     * </p>
     * 
     * <p>
     * Ideally, the XML would look something like this:<br>
     * </pre>
     *  &lt;Invoke&rt;
     *      &lt;BeanObjectName&rt;TestMBean&lt;/BeanObjectName&rt;
     *      &lt;Method name="VoidVoidMethod"&rt;
     *      &lt;Param type="int"&rt;5&lt;/Param&rt;
     *      &lt;/Method&rt;
     *  &lt;/Invoke&rt;"
     * </p>
     */
    @Test
    public void verifyXMLParamTypeAttributeMissing() throws Exception 
    {
        StringBuffer XMLData = new StringBuffer().
        append("<Invoke>").
        append("<BeanObjectName>TestMBean</BeanObjectName>").
        append("<Method name=\"VoidVoidMethod\">").
        append("<Param>5</Param>").
        append("</Method>").
        append("</Invoke>");
        
        BufferedReader br = new BufferedReader(new StringReader(XMLData.toString()));
        
        try
        {
            InvokeDecoder id = new InvokeDecoder(br, XMLData.length());
            id.DecodeInput();
            Assert.fail("Failed to receive an exception while decoding the XML input.");
        }
        catch(Exception e)
        {
            Assert.assertTrue( 
                    "An exception should have been thrown indicating XML input is incorrect.", 
                    (e.getClass() == ScxException.class)&&
                    (((ScxException)e).getExceptionCode().equals(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML)));
        }

    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the correct Parameter, 
     * the parameter has no value. <br>
     * </p>
     * 
     * <p>
     * Ideally, the XML would look something like this:<br>
     * </pre>
     *  &lt;Invoke&rt;
     *      &lt;BeanObjectName&rt;TestMBean&lt;/BeanObjectName&rt;
     *      &lt;Method name="VoidVoidMethod"&rt;
     *      &lt;Param type="int"&rt;5&lt;/Param&rt;
     *      &lt;/Method&rt;
     *  &lt;/Invoke&rt;"
     * </p>
     */
    @Test
    public void verifyXMLParamNoValue() throws Exception 
    {
        StringBuffer XMLData = new StringBuffer().
        append("<Invoke>").
        append("<BeanObjectName>TestMBean</BeanObjectName>").
        append("<Method name=\"VoidVoidMethod\">").
        append("<Param type=\"int\"></Param>").
        append("</Method>").
        append("</Invoke>");
        
        BufferedReader br = new BufferedReader(new StringReader(XMLData.toString()));
        
        try
        {
            InvokeDecoder id = new InvokeDecoder(br, XMLData.length());
            id.DecodeInput();
            Assert.assertEquals(
                    "Parameter 0 value should be \"null\"",
                    id.getMethodParams().get(0).getParamValue() , "");
        }
        catch(Exception e)
        {
            Assert.fail("An unknown exception was thrown.");
        }

    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the correct parameters, 
     * parameter types and values. <br>
     * </p>
     * 
     * <p>
     * Ideally, the XML would look something like this:<br>
     * 
     * <pre>
     *  &lt;Invoke&rt;
     *      &lt;BeanObjectName&rt;Microsoft:name=TestMBean&lt;/BeanObjectName&rt;
     *      &lt;Method name="VoidIntStringMethod"&rt;&lt;/Method&rt;
     *      &lt;Param type="int"&rt;5&lt;/Param&rt;
     *      &lt;Param type="string"&rt;Woohoo&lt;/Param&rt;
     *  &lt;/Invoke&rt;"
     * </pre>
     * 
     * </p>
     */
    @Test
    public void verifyBodyData() throws Exception 
    {
        StringBuffer XMLData = new StringBuffer().
        append("<Invoke>").
        append("<BeanObjectName>TestMBean</BeanObjectName>").
        append("<Method name=\"VoidVoidMethod\">").
        append("<Param type=\"int\">5</Param>").
        append("<Param name=\"Param1\" type=\"string\">abcdefg</Param>").
        append("</Method>").
        append("</Invoke>");
        
        BufferedReader br = new BufferedReader(new StringReader(XMLData.toString()));
        
            InvokeDecoder id = new InvokeDecoder(br, XMLData.length());
            id.DecodeInput();
            Assert.assertEquals(
                    "Object name should be \"TestMBean\"",
                    id.getBeanObjectName(), "TestMBean");
            Assert.assertEquals(
                    "Method name should be \"VoidVoidMethod\"",
                    id.getMethodName(), "VoidVoidMethod");
            Assert.assertEquals(
                    "Parameter 1 type should be \"int\"",
                    id.getMethodParams().get(0).getParamType() , "int");
            Assert.assertEquals(
                    "Parameter 1 value should be \"5\"",
                    id.getMethodParams().get(0).getParamValue() , "5");
            Assert.assertEquals(
                    "Parameter 1 name should be \"string\"",
                    id.getMethodParams().get(1).getParamName() , "Param1");
            Assert.assertEquals(
                    "Parameter 1 type should be \"string\"",
                    id.getMethodParams().get(1).getParamType() , "string");
            Assert.assertEquals(
                    "Parameter 1 value should be \"abcdefg\"",
                    id.getMethodParams().get(1).getParamValue() , "abcdefg");
    }
    
    /**
     * <p>
     * Verify the input stream is valid and contains the correct Parameter, 
     * the elements contain extra attributes which should be ignored. <br>
     * </p>
     * 
     * <p>
     * Ideally, the XML would look something like this:<br>
     * </pre>
     *  &lt;Invoke SomeTag="SomeValue"&rt;
     *      &lt;BeanObjectName&rt;TestMBean&lt;/BeanObjectName&rt;
     *      &lt;Method name="VoidVoidMethod"&rt;
     *      &lt;Param type="int"&rt;5&lt;/Param&rt;
     *      &lt;/Method&rt;
     *  &lt;/Invoke&rt;"
     * </p>
     */
    @Test
    public void verifyXML_Extra_Attribures() throws Exception 
    {
        StringBuffer XMLData = new StringBuffer().
        append("<Invoke SomeTag=\"SomeValue\">").
        append("<BeanObjectName SomeTag=\"SomeValue\">TestMBean</BeanObjectName>").
        append("<Method SomeTag=\"SomeValue\" name=\"VoidVoidMethod\">").
        append("<Param SomeTag=\"SomeValue\" type=\"int\">100</Param>").
        append("</Method>").
        append("</Invoke>");
        
        BufferedReader br = new BufferedReader(new StringReader(XMLData.toString()));
        
        try
        {
            InvokeDecoder id = new InvokeDecoder(br, XMLData.length());
            id.DecodeInput();
            Assert.assertEquals(
                    "Parameter 0 value should be \"100\"",
                    id.getMethodParams().get(0).getParamValue() , "100");
        }
        catch(Exception e)
        {
            Assert.fail("An unknown exception was thrown.");
        }
    }
}
