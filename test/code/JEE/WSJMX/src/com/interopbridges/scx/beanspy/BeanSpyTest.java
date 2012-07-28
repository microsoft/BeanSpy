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

package com.interopbridges.scx.beanspy;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.jmx.MockJmx;
import com.interopbridges.scx.jmx.MockJmxThatAlwaysFails;
import com.interopbridges.scx.mbeans.MBeanGetter;
import com.interopbridges.scx.util.JmxURLCheck;
import com.interopbridges.scx.util.SAXParser;
import com.interopbridges.scx.webservices.FauxMBeanGenerator;

/**
 * Class to test the BeanSpy servlet
 * 
 * @author Jonas Kallstrom
 */
public class BeanSpyTest {

    /**
     * <p>
     * Generation of the Fake MBeans (note: for unit-tests this aims at a Mock
     * JMX Store and not the real thing).
     * </p>
     */
    private FauxMBeanGenerator _beanGenerator;

    /**
     * <p>
     * Interface to getting the MBeans from the JMX Store (note: for unit-tests
     * this aims at a Mock JMX Store and not the real thing).
     * </p>
     */
    private MBeanGetter _beanGetter;

    /**
     * <p>
     * The system under test. Created once here and initialized in the setup
     * method to reduce code duplication across the unit-tests.
     * </p>
     */
    private MockBeanSpy _extender;

    /**
     * <p>
     * The system under test's input. Created once here and initialized in the
     * setup method to reduce code duplication across the unit-tests.
     * </p>
     */
    private MockHttpServletRequest _request;

    /**
     * <p>
     * The system under test's response. Created once here and initialized in
     * the setup method to reduce code duplication across the unit-tests.
     * </p>
     */
    private MockHttpServletResponse _response;

    /**
     * <p>
     * Method invoked before each unit-test in this class.
     * </p>
     * 
     * @throws Execption
     *             If something went wrong when initializing the MBean Store
     */
    @Before
    public void Setup() throws Exception {
        List<IJMX>fauxJmxStores = new ArrayList<IJMX>();
        
        JmxStores.clearListOfJmxStores();
        
        /* created 2 identical JMX stores for tests*/
        IJMX fauxJmxStore = new MockJmx();
        fauxJmxStores.add(fauxJmxStore);
        this._beanGenerator = new FauxMBeanGenerator(fauxJmxStore);
        this._beanGenerator.run();

        
        fauxJmxStore = new MockJmx();
        fauxJmxStores.add(fauxJmxStore);
        this._beanGenerator = new FauxMBeanGenerator(fauxJmxStore);
        this._beanGenerator.run();

        try
        {
            fauxJmxStore = new MockJmxThatAlwaysFails();
            Assert.fail("Exception should have been thrown creating JMX Store");
        }
        catch (Exception e)
        {
            Assert.assertTrue(
                    "Exception should have been of type ScxException", e
                            .getClass() == ScxException.class);
        }
        
        Assert.assertTrue(
                "Number of JMX Stores should have been 2", 2 == fauxJmxStores.size());
        
        this._beanGetter = new MBeanGetter(fauxJmxStores);
        _extender = new MockBeanSpy();
        _extender.setMBeanStore(_beanGetter);
        _extender.setMBeanGenerator(_beanGenerator);

        /*
         * The request here is the standard that 'should' work, the expectation
         * is that per unit-tests the will most likely need to be overwritten to
         * test the conditional logic of the BeanSpy.
         */
        _request = new MockHttpServletRequest("");
        _request.addParameter("JMXQuery", "com.interopbridges.scx:jmxType=operationCall");
        _response = new MockHttpServletResponse();
    }
    

    /**
     * <p>
     * Unit Test Teardown method
     * </p>
     */
    @After
    public void Teardown() {
        JmxStores.clearListOfJmxStores();
        
        this._beanGenerator = null;
        this._beanGetter = null;
        this._request = null;
        this._response = null;
        this._extender = null;
    }

    /**
     * <p>
     * Verify that for a legal call to enumerate a supported MBeans type that
     * the content type is as expected.
     * </p>
     */
    @Test
    public void testDoGet_contentType() {
        try {
            _extender.doGet(_request, _response);
            assertTrue("Content type", _response.contentType
                    .equals("application/xml; charset=utf-8"));

        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for a legal call to enumerate a supported MBeans type that
     * the Response XML looks like we is as expected.
     * </p>
     */
    @Test
    public void testDoGet_ValidInput_ValidResponse() {
        try {
            _extender.doGet(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] results = SAXParser.XPathQuery(tmpResponse, "/MBeans/*");
            assertTrue("Response should contain <MBeans> tag for input: \n"
                    + tmpResponse, results.length > 0);
            assertTrue(
                   "Response should contain <MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">"+"-----"+tmpResponse,
                    tmpResponse.contains(
                    "<MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">"));

        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for an illegal request with no content generates the expected
     * result.
     * </p>
     */
    @Test
    public void testDoGet_InvalidInput_Empty() {
        _request = new MockHttpServletRequest("");
        _request.addParameter("JMXQuery", "");
        try
        {
            _extender.doGet(_request, _response);
            Assert.fail("An exception should have been thrown.");
        } catch (Exception e)
        {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
            Assert.assertNotNull("Root cause should be not be null", ((ServletException)e).getRootCause());
            Assert.assertEquals("Root cause should be ScxException",
                    ScxException.class, ((ServletException)e).getRootCause().getClass());
            Assert
                    .assertEquals(
                            "Should throw an ERROR_INVALID_SERVLET_REQUEST_EMPTY_JMXQUERY exception",
                            ((ScxException) ((ServletException)e).getRootCause()).getExceptionCode(),
                            ScxExceptionCode.ERROR_INVALID_SERVLET_REQUEST_EMPTY_JMXQUERY);
        }
    }

    /**
     * <p>
     * Verify that for an illegal request with no JMXQuery parameter generates the expected
     * result.
     * </p>
     */
    @Test
    public void testDoGet_InvalidInput_NOJMXQueryParameter() {
        _request = new MockHttpServletRequest("");
        try
        {
            _extender.doGet(_request, _response);
            Assert.fail("An exception should have been thrown.");
        } catch (Exception e)
        {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
            Assert.assertNotNull("Root cause should be not be null", ((ServletException)e)
                    .getRootCause());
            Assert.assertEquals("Root cause should be ScxException",
                    ScxException.class, ((ServletException)e).getRootCause().getClass());
            Assert
                    .assertEquals(
                            "Should throw an ERROR_INVALID_SERVLET_REQUEST_NO_JMXQUERY exception",
                            ((ScxException) ((ServletException)e).getRootCause()).getExceptionCode(),
                            ScxExceptionCode.ERROR_INVALID_SERVLET_REQUEST_NO_JMXQUERY);
        }
   }

    /**
     * <p>
     * Verify that for an illegal request with illegal ObjectName/query syntax
     * (there is a colon missing that should separate the domain and the
     * attributes) that no MBeans are returned.
     * </p>
     */
    @Test
    public void testDoGet_InvalidInput_BadObjectNameSyntax() {

        _request = new MockHttpServletRequest("");
        _request.addParameter("JMXQuery", "com.interopbridges.scxjmxType=operationCall");
        try 
        {
            _extender.doGet(_request, _response);
            
            String tmpResponse = _response.ostream.buf.toString();
            String[] results = SAXParser.XPathQuery(tmpResponse, "/MBeans");
            assertTrue("Response should contain <MBeans> tag for input: \n"
                    + tmpResponse, results.length == 1);
            
            results = SAXParser.XPathQuery(tmpResponse, "/MBean/*");
            assertTrue("No MBeans shpuld be returned: \n"
                    + tmpResponse, results.length == 0);
            
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for an illegal request (no object name).
     * </p>
     */
    @Test
    public void testDoGet_InvalidInput_NotEnoughInputForMBeansEnumerate2() {

        _request = new MockHttpServletRequest("");
        _request.addParameter("JMXQuery", "");
        try
        {
            _extender.doGet(_request, _response);
            Assert.fail("An exception should have been thrown.");

        } catch (Exception e)
        {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
            Assert.assertNotNull("Root cause should be not be null", ((ServletException)e)
                    .getRootCause());
            Assert.assertEquals("Root cause should be ScxException",
                    ScxException.class, ((ServletException)e).getRootCause().getClass());
            Assert.assertEquals(
                    "Should throw an ERROR_INVALID_SERVLET_REQUEST_EMPTY_JMXQUERY exception",
                    ((ScxException) ((ServletException)e).getRootCause()).getExceptionCode(),
                    ScxExceptionCode.ERROR_INVALID_SERVLET_REQUEST_EMPTY_JMXQUERY);
        }
    }

    /**
     * <p>
     * Verify that for an legal request with no matching mbean that
     * 'MBeans/' tag is returned with no items.
     * </p>
     */
    @Test
    public void testDoGet_InvalidInput_NotMBeans() {
        MockHttpServletRequest request = new MockHttpServletRequest("");
        request.addParameter("JMXQuery", "Christopher/com.interopbridges.scx:jmxType=operationCall");
        try {
            _extender.doGet(request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] results = SAXParser.XPathQuery(tmpResponse, "/MBeans/*");
            assertTrue("Response should contain <MBeans> tag for input: \n"
                    + tmpResponse, results.length == 0);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for an no MBeans are returned b/c we are using a fictitious
     * domain.
     * </p>
     */
    @Test
    public void testDoGet_InvalidInput_WrongDomain() {
        MockHttpServletRequest request = new MockHttpServletRequest("");
        request.addParameter("JMXQuery", "com.jonas.scx:jmxType=operationCall");
        try {
            _extender.doGet(request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] results = SAXParser.XPathQuery(tmpResponse, "/MBeans/*");
            assertTrue("Response should contain <MBeans> tag for input: \n"
                    + tmpResponse, results.length == 0);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * The init method presently does not do anything, hence this test is pretty
     * trivial. Basically, it verifies that no exceptions are thrown at runtime
     * (and it does help increase our coverage numbers).
     * </p>
     */
    @Test
    public void testInit_DoesNothing() {
        _extender.init();
    }

    /**
     * <p>
     * The init method presently does not do anything, hence this test is pretty
     * trivial. Basically, it verifies that no exceptions are thrown at runtime
     * (and it does help increase our coverage numbers). There is a message
     * being set to the logger, so presumably the notification is necessary
     * enough.
     * </p>
     */
    @Test
    public void testInit_DoesNothingEvenIfTheStoreIsNull() {
        _extender.setMBeanStore(null);
        _extender.init();
    }

    /**
     * <p>
     * The destroy method presently does not do anything, hence this test is
     * pretty trival. Basically, it verifies that no exceptions are thrown at
     * runtime (and it does help increase our coverage numbers).
     * </p>
     */
    @Test
    public void testDestroy_DoesNothing() {
        _extender.destroy();
    }

    /**
     * <p>
     * Verify that for a legal call to enumerate a supported MBeans type with parameters that
     * the Response XML looks like we is as expected.
     * </p>
     */
    @Test
    public void testDoGet_ValidInputWithParameters_ValidResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest("");
        request.addParameter("JMXQuery", "com.interopbridges.scx:jmxType=operationCall");
        request.addParameter("MaxDepth", "10");
        request.addParameter("MaxCount", "10");
        request.addParameter("MaxSize", "10");
             
        try {
            _extender.doGet(request, _response);
                       
            String tmpResponse = _response.ostream.buf.toString();
            String[] results = SAXParser.XPathQuery(tmpResponse, "/MBeans/*");
            assertTrue("Response should contain <MBeans> tag for input: \n"
                    + tmpResponse, results.length > 0);
            assertTrue(
                   "Response should contain <MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">",
                    tmpResponse.contains(
                    "<MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">"));
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }

  
    /**
     * <p>
     * Verify that for a legal call to enumerate a supported MBeans type with invalid parameters that
     * the Response XML looks like we is as expected. Invalid parameters should be ignored.
     * </p>
     */
    @Test
    public void testDoGet_ValidInputWithInvalidParameters_ValidResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest("");
        request.addParameter("JMXQuery", "com.interopbridges.scx:jmxType=operationCall");
        request.addParameter("MaxDepth", "a");
        request.addParameter("MaxCount", "b");
        request.addParameter("MaxSize", "c");
        try
        {
            _extender.doGet(request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] results = SAXParser.XPathQuery(tmpResponse, "/MBeans/*");
            assertTrue("Response should contain <MBeans> tag for input: \n"
                    + tmpResponse, results.length > 0);
            assertTrue(
                    "Response should contain <MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">",
                    tmpResponse
                            .contains("<MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">"));
        } catch (Exception e)
        {
            Assert.fail("Unexpected Exception Received" + e.getMessage());
        }
    }

    /**
     * <p>
     * Verify that the list of loaded JMX stores is populated properly by the constructor
     * </p>
     */
    @Test
    public void testLoadingOfJmxStoreAbstractions() {
        JmxStores.clearListOfJmxStores();
        Assert.assertEquals("List of loaded JMX stores should be empty", 0, JmxStores.getListOfJmxStoreAbstractionNames().size());
        JmxStores.connectToJmxStores();
        Assert.assertEquals("List of loaded JMX stores should be one", 1, JmxStores.getListOfJmxStoreAbstractionNames().size());
        Assert.assertEquals("Wrong name for loaded JMX Store Name", "com.interopbridges.scx.jmx.JdkJMXAbstraction", JmxStores.getListOfJmxStoreAbstractionNames().get(0));
    }
    

    /**
     * <p>
     * Helper function to build a long URL
     * </p>
     */
    StringBuffer buildRequestURL( int sizeRequired, String protocol, String endpoint )
    {
        StringBuffer strURL = new StringBuffer();
     
        // The request URL is made up of the URL + parameter
        // i.e. "http://somemachine:100/BeanSpy/MBeans" + "JMXQuery=com.interopbridges.scx:jmxType=operationCall"
        
        // The 2048 is the total required length -1. 
        // The -1 is for the '?' that gets inserted between the request and parameters 
        int requiredPadding = sizeRequired - protocol.length() - endpoint.length();

        // Build a Query request that is the required size
        strURL.append( protocol );           
        for(int i=0;i<requiredPadding;i++)
        {
            strURL.append('a');
        }
        strURL.append( endpoint );           

        return strURL; 
    }
    
    /**
     * <p>
     * Verify that if the length of a request URL exceeds 2048, the request should fail.
     * </p>
     *
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_URLLengthExceedsLimits_invalidResponse() 
    {
        StringBuffer strURL;
        MockHttpServletRequest request = new MockHttpServletRequest("");

        request.addParameter("JMXQuery", "com.interopbridges.scx:jmxType=operationCall");
        strURL = buildRequestURL( 2048 - request.getQueryString().length(), "http://www.", "/BeanSpy/MBeans" );

        request.setRequestURL(strURL); 

        Assert.assertTrue("The length of the constructed URL should be 2049" ,
                2049 == (request.getRequestURL().toString().length()+ request.getQueryString().length()+1));  

        try
        {
           _extender.doGet(request, _response);
           Assert.fail("Function should fail");          
        } 
        catch (Exception e) 
        {
           Assert.assertTrue(
                   "Exception should have been of type ServletException", e
                           .getClass() == ServletException.class);
           Assert.assertNotNull("Root cause should be not be null", ((ServletException)e)
                   .getRootCause());
           Assert.assertEquals("Root cause should be ScxException",
                   ScxException.class, ((ServletException)e).getRootCause().getClass());
           Assert
                   .assertEquals(
                           "Should throw an ERROR_URL_LENGTH_EXCEEDS_LIMITS exception",
                           ((ScxException) ((ServletException)e).getRootCause()).getExceptionCode(),
                           ScxExceptionCode.ERROR_URL_LENGTH_EXCEEDS_LIMITS);
        }
        
    }
    
    /**
     * <p>
     * Verify that if the length of a request URL equals to 2048, the request should succed.
     * </p>
     *
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_URLLengthEqualsTo2048Limits_ValidResponse() {
        StringBuffer strURL;
        MockHttpServletRequest request = new MockHttpServletRequest("");
        
        request.addParameter("JMXQuery", "com.interopbridges.scx:jmxType=operationCall");
        strURL = buildRequestURL( 2047 - request.getQueryString().length(), "http://www.", "/BeanSpy/MBeans" );

        request.setRequestURL(strURL); 
        
        /*
         * "?" does not belong to either URL or query string, so add 1 here.
         */
        Assert.assertTrue("The length of the constructed URL should be 2048",
                2048 == (request.getRequestURL().toString().length()
                        + request.getQueryString().length() + 1));

        try
        {
            _extender.doGet(request, _response);

            String tmpResponse = _response.ostream.buf.toString();
            String[] results = SAXParser.XPathQuery(tmpResponse, "/MBeans/*");
            Assert.assertTrue(
                    "Response should contain <MBeans> tag for input: \n"
                            + tmpResponse, results.length > 0);
            assertTrue(
                    "Response should contain <MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">",
                    tmpResponse
                            .contains("<MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">"));
        } catch (Exception e)
        {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
   }

    /**
     * <p>
     * Verify that if the length of a request URL is less than 2048, the request should succed.
     * </p>
     *
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_URLLengthLessThan2048Limits_ValidResponse() { 
        StringBuffer strURL = new StringBuffer();
        MockHttpServletRequest request = new MockHttpServletRequest("");
     
        request.addParameter("JMXQuery", "com.interopbridges.scx:jmxType=operationCall");
        strURL = buildRequestURL( 2046 - request.getQueryString().length(), "http://www.", "/BeanSpy/MBeans" );

        request.setRequestURL(strURL); 
           
        /*
         * "?" does not belong to either URL or query string, so add 1 here.
         */
         Assert.assertTrue("The length of the constructed URL should be 2047" ,
                            2047 == (request.getRequestURL().toString().length()+ request.getQueryString().length() + 1));  
         
         try {
              _extender.doGet(request, _response);
                       
              String tmpResponse = _response.ostream.buf.toString();
              String[] results = SAXParser.XPathQuery(tmpResponse, "/MBeans/*");
              Assert.assertTrue("Response should contain <MBeans> tag for input: \n"
                    + tmpResponse, results.length > 0);
            assertTrue(
                   "Response should contain <MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">",
                    tmpResponse.contains(
                    "<MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">"));
         } 
         catch (Exception e) 
         {
              Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
   }
 
     /**
     * <p>
     * Verify that with a valid "JMXQuery" inputs , the Response XML looks like we is as expected.
     * </p>
     * 
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_ValidJMXQueryInputWithValidParameters_ValidResponse() {
        _request = new MockHttpServletRequest("");
        _request.addParameter("JMXQuery", "com.interopbridges.scx:jmxType=operationCall");
             
        try {
            _extender.doGet(_request, _response);
                       
            String tmpResponse = _response.ostream.buf.toString();
            String[] results = SAXParser.XPathQuery(tmpResponse, "/MBeans/*");
            assertTrue("Response should contain <MBeans> tag for input: \n"
                    + tmpResponse, results.length > 0);
            assertTrue(
                   "Response should contain <MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">",
                    tmpResponse.contains(
                    "<MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">"));
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }
       
    /**
     * <p>
     * Verify that with both valid ("JMXQuery" is required) and invalid inputs , the Response XML looks like we is as expected.
     * </p>
     * 
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_MixedValidAndInvalidInputWithParameters_ValidResponse() {
        _request = new MockHttpServletRequest("");
        _request.addParameter("JMXQuery", "com.interopbridges.scx:jmxType=operationCall");
        _request.addParameter("MaxDepth", "10");
        _request.addParameter("MaxCount", "10");
        _request.addParameter("MaxSize", "10");
        _request.addParameter("invalid", "10");
        _request.addParameter("maxSize", "10");
        _request.addParameter("Maxdepth", "10");
             
        try {
            _extender.doGet(_request, _response);
                       
            String tmpResponse = _response.ostream.buf.toString();
            String[] results = SAXParser.XPathQuery(tmpResponse, "/MBeans/*");
            assertTrue("Response should contain <MBeans> tag for input: \n"
                    + tmpResponse, results.length > 0);
            assertTrue(
                   "Response should contain <MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">",
                    tmpResponse.contains(
                    "<MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">"));
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }
    
    /**
     * <p>
     * Verify that with all valid inputs ("JMXQuery" is required), the Response XML 
     * looks like we is as expected.
     * </p>
     * 
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_AllValidInputWithParameters_ValidResponse() {
        _request = new MockHttpServletRequest("");
        _request.addParameter("JMXQuery", "com.interopbridges.scx:jmxType=operationCall");
        _request.addParameter("MaxDepth", "10");
        _request.addParameter("MaxCount", "10");
        _request.addParameter("MaxSize", "10");
             
        try {
            _extender.doGet(_request, _response);
                       
            String tmpResponse = _response.ostream.buf.toString();
            String[] results = SAXParser.XPathQuery(tmpResponse, "/MBeans/*");
            assertTrue("Response should contain <MBeans> tag for input: \n"
                    + tmpResponse, results.length > 0);
            assertTrue(
                   "Response should contain <MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">",
                    tmpResponse.contains(
                    "<MBean Name=\"com.interopbridges.scx.webservices.OperationCall\" objectName=\"com.interopbridges.scx:jmxType=operationCall,sourceEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,sourceOperation=divide,targetEndpoint=http://localhost:9080/WebServiceProject/CalculatorService,targetOperation=subtract\">"));
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }
    
 
    
    /**
     * <p>
     * Verify that with an invalid input, even if the valid object name is supplied, 
     * the Response should fail.
     * </p>
     * 
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_InvalidInput_ValidObjectNameSyntax_InvalidResponse() {

        _request = new MockHttpServletRequest("");
        _request.addParameter("JmxQuery", "com.interopbridges.scx:jmxType=operationCall");
        
        
        try {
            _extender.doGet(_request, _response);
            Assert.fail("An exception should have been thrown.");

        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
            Assert.assertNotNull("Root cause should be not be null", ((ServletException)e)
                    .getRootCause());
            Assert.assertEquals("Root cause should be ScxException",
                    ScxException.class, ((ServletException)e).getRootCause().getClass());
            Assert
                    .assertEquals(
                            "Should throw an ERROR_INVALID_SERVLET_REQUEST_NO_JMXQUERY exception",
                            ((ScxException) ((ServletException)e).getRootCause()).getExceptionCode(),
                            ScxExceptionCode.ERROR_INVALID_SERVLET_REQUEST_NO_JMXQUERY);
        }
    }
    
    /**
     * <p>
     * Verify that with all invalid inputs, the Response should fail.
     * </p>
     *  
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_AllInvalidInputWithValidParameters_InvalidResponse() {
        _request = new MockHttpServletRequest("");
        
        /**
         * The input is case-sensitive, so "JmxQuery", "maxSize" and "maxdepth" are
         * invalid inputs.
         */
        _request.addParameter("JmxQuery", "com.interopbridges.scx:jmxType=operationCall");
        _request.addParameter("invalid", "10");
        _request.addParameter("maxSize", "10");
        _request.addParameter("maxdepth", "10");
        
        try {
            _extender.doGet(_request, _response);
            Assert.fail("An exception should have been thrown.");

        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
            Assert.assertNotNull("Root cause should be not be null", ((ServletException)e)
                    .getRootCause());
            Assert.assertEquals("Root cause should be ScxException",
                    ScxException.class, ((ServletException)e).getRootCause().getClass());
            Assert
                    .assertEquals(
                            "Should throw an ERROR_INVALID_SERVLET_REQUEST_NO_JMXQUERY exception",
                            ((ScxException) ((ServletException)e).getRootCause()).getExceptionCode(),
                            ScxExceptionCode.ERROR_INVALID_SERVLET_REQUEST_NO_JMXQUERY);
        }

    }
    
    /**
     * <p>
     * Verify that with valid and invalid inputs, JmxUMLCheck is able to distinguish them.
     * For valid inputs, JmxUMLCheck should be able to populate their corresponding parameters correctly.
     * </p>
     * 
     * @author Jinlong Li
     */
    @Test
    public void testJmxURLCheck() {
        _request = new MockHttpServletRequest("");
        
        /**
         * <p>
         * "JMXQuery", "MaxSize", "MaxDepth", "MaxCount" are only four valid inputs correctly.
         * It's case-sensitive, therefore such as "JmxQuery", "maxSize" and "maxdepth" are invalid inputs.
         * </p>
         *
         */
        _request.addParameter("JMXQuery", "com.interopbridges.scx:jmxType=operationCall");
        _request.addParameter("JmxQuery", "com.interopbridges.scx:jmxType=operationCall");
        _request.addParameter("invalid", "10");
        _request.addParameter("dummy", "10");
        _request.addParameter("maxSize", "100");
        _request.addParameter("MaxSize", "100");
        _request.addParameter("maxdepth", "10");
        _request.addParameter("MaxDepth", "10");
        _request.addParameter("Maxcount", "20");
        _request.addParameter("MaxCount", "20");
        
        HashMap<String, String[]> Params = JmxURLCheck.getValidInputs(_request);
       
        Assert.assertTrue("\"JmxQuery\" is an invalid input which should be exclueded.",  
                (Params.get("JmxQuery") == null));
        
        Assert.assertTrue("\"JMXQuery\" is a valid input and its corresponding parameter should be populated correctly.",  
                (Params.get("JMXQuery")[0].equals("com.interopbridges.scx:jmxType=operationCall")));
        
        Assert.assertTrue("\"invalid\" is an invalid input which should be exclueded.",  
                (Params.get("invalid") == null));
        
        Assert.assertTrue("\"dummy\" is an invalid input which should be exclueded.",  
                (Params.get("dummy") == null));
        
        Assert.assertTrue("\"maxSize\" is an invalid input which should be exclueded.",  
                (Params.get("maxSize") == null));
        
        Assert.assertTrue("\"MaxSize\" is a valid input and its corresponding parameter should be populated correctly.",  
                (Params.get("MaxSize")[0].equals("100")));
        
        Assert.assertTrue("\"maxdepth\"is an invalid input which should be exclueded.",  
                (Params.get("maxdepth") == null));
        
        Assert.assertTrue("\"MaxDepth\" is a valid input and its corresponding parameter should be populated correctly.",  
                (Params.get("MaxDepth")[0].equals("10")));
        
        Assert.assertTrue("\"Maxcount\" is an invalid input which should be exclueded.",  
                (Params.get("Maxcount") == null));
        
        Assert.assertTrue("\"MaxCount\" is a valid input and its corresponding parameter should be populated correctly.",  
                (Params.get("MaxCount")[0].equals("20")));
    }
}

