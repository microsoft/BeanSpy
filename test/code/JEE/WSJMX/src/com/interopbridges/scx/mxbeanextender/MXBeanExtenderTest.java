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

package com.interopbridges.scx.mxbeanextender;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.beanspy.MockHttpServletRequest;
import com.interopbridges.scx.jeestats.Statistic;
import com.interopbridges.scx.jeestats.StatisticGroup;
import com.interopbridges.scx.jeestats.StatisticItemGroup;
import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.mxbeanextender.MXBeanExtender;
import com.interopbridges.scx.util.MockHttpRequestHelper;
import com.interopbridges.scx.util.SAXParser;
import com.interopbridges.scx.xml.StatisticXMLTransformer;

/**
 * Class to test the MXBean Extender servlet
 * 
 * @author Geoff Erasmus
 */
public class MXBeanExtenderTest {

    /**
     * Test implementation of a HttpServletRequest to be able to feed the
     * servlet test data
     */
    private class MockHttpServletRequest implements HttpServletRequest {
        private String _request;
        private HashMap<String,String[]> Params=null;
        
        public MockHttpServletRequest(String request) {
            _request = request;
        }

        public String getAuthType() {
            return null;
        }

        public String getContextPath() {
            return null;
        }

        public Cookie[] getCookies() {
            return null;
        }

        public long getDateHeader(String name) {
            return 0;
        }

        public String getHeader(String name) {
            return null;
        }

        public Enumeration<?> getHeaderNames() {
            return null;
        }

        public Enumeration<?> getHeaders(String name) {
            return null;
        }

        public int getIntHeader(String name) {
            return 0;
        }

        public String getMethod() {
            return null;
        }

        public String getPathInfo() {
            return _request;
        }

        public String getPathTranslated() {
            return null;
        }

       /**
         * <p>
         * Reconstruct the query string from input parameters.
         * </p> 
         */
        public String getQueryString() {       
             return MockHttpRequestHelper.getQueryString(Params);
        }

        public String getRemoteUser() {
            return null;
        }

        public String getRequestURI() {
            return null;
        }
        
        private StringBuffer _url; 

        public void setRequestURL(StringBuffer url){
            this._url = url;
        }
        
        public StringBuffer getRequestURL() {
            return _url;
        }
          
        public String getRequestedSessionId() {
            return null;
        }

        public String getServletPath() {
            return null;
        }

        public HttpSession getSession() {
            return null;
        }

        public HttpSession getSession(boolean create) {
            return null;
        }

        public Principal getUserPrincipal() {
            return null;
        }

        public boolean isRequestedSessionIdFromCookie() {
            return false;
        }

        public boolean isRequestedSessionIdFromURL() {
            return false;
        }

        public boolean isRequestedSessionIdFromUrl() {
            return false;
        }

        public boolean isRequestedSessionIdValid() {
            return false;
        }

        public boolean isUserInRole(String role) {
            return false;
        }

        public Object getAttribute(String arg0) {
            return null;
        }

        public Enumeration<?> getAttributeNames() {
            return null;
        }

        public String getCharacterEncoding() {
            return null;
        }

        public int getContentLength() {
            return 0;
        }

        public String getContentType() {
            return null;
        }

        public ServletInputStream getInputStream() throws IOException {
            return null;
        }

        public String getLocalAddr() {
            return null;
        }

        public String getLocalName() {
            return null;
        }

        public int getLocalPort() {
            return 0;
        }

        public Locale getLocale() {
            return null;
        }

        public Enumeration<?> getLocales() {
            return null;
        }

        public String getParameter(String arg0) {
            return null;
        }

        public Map<?, ?> getParameterMap() {
            return null;
        }

        public Enumeration<?> getParameterNames() {
            return null;
        }

        public String[] getParameterValues(String arg0) {
            return null;
        }

        public String getProtocol() {
            return null;
        }

        public BufferedReader getReader() throws IOException {
            return null;
        }

        public String getRealPath(String arg0) {
            return null;
        }

        public String getRemoteAddr() {
            return null;
        }

        public String getRemoteHost() {
            return null;
        }

        public int getRemotePort() {
            return 0;
        }

        public RequestDispatcher getRequestDispatcher(String arg0) {
            return null;
        }

        public String getScheme() {
            return null;
        }

        public String getServerName() {
            return null;
        }

        public int getServerPort() {
            return 0;
        }

        public boolean isSecure() {
            return false;
        }

        public void removeAttribute(String arg0) {
        }

        public void setAttribute(String arg0, Object arg1) {
        }

        public void setCharacterEncoding(String arg0)
                throws UnsupportedEncodingException {
        }
        
       /**
         * <p>
         * Add a key/value pair to a HashMap.
         * </p>
         */     
        public void addParameter(String key, String value)
        {      
             Params = MockHttpRequestHelper.addParameter(key, value, Params);
        }
    }

    /**
     * An OutputStream that writes to a StringBuffer
     */
    private class IOStringOutputStream extends ServletOutputStream {
        public StringBuffer buf = new StringBuffer();

        public void write(int character) throws java.io.IOException {
            buf.append((char) character);
        }
    }

    /**
     * Test implementation of a HttpServletResponse to be able to read the data
     * the servlet sends back
     */
    private class MockHttpServletResponse implements HttpServletResponse {
        public String contentType = new String();
        public IOStringOutputStream ostream = new IOStringOutputStream();

        public void addCookie(Cookie arg0) {
        }

        public void addDateHeader(String arg0, long arg1) {
        }

        public void addHeader(String arg0, String arg1) {
        }

        public void addIntHeader(String arg0, int arg1) {
        }

        public boolean containsHeader(String arg0) {
            return false;
        }

        public String encodeRedirectURL(String arg0) {
            return null;
        }

        public String encodeRedirectUrl(String arg0) {
            return null;
        }

        public String encodeURL(String arg0) {
            return null;
        }

        public String encodeUrl(String arg0) {
            return null;
        }

        public void sendError(int arg0) throws IOException {
        }

        public void sendError(int arg0, String arg1) throws IOException {
        }

        public void sendRedirect(String arg0) throws IOException {
        }

        public void setDateHeader(String arg0, long arg1) {
        }

        public void setHeader(String arg0, String arg1) {
        }

        public void setIntHeader(String arg0, int arg1) {
        }

        public void setStatus(int arg0) {
        }

        public void setStatus(int arg0, String arg1) {
        }

        public void flushBuffer() throws IOException {
        }

        public int getBufferSize() {
            return 0;
        }

        public String getCharacterEncoding() {
            return null;
        }

        public String getContentType() {
            return null;
        }

        public Locale getLocale() {
            return null;
        }

        public ServletOutputStream getOutputStream() throws IOException {
            return ostream;
        }

        public PrintWriter getWriter() throws IOException {
            return null;
        }

        public boolean isCommitted() {
            return false;
        }

        public void reset() {
        }

        public void resetBuffer() {
        }

        public void setBufferSize(int size) {
        }

        public void setCharacterEncoding(String charset) {
        }

        public void setContentLength(int len) {
        }

        public void setContentType(String type) {
            contentType = type;
        }

        public void setLocale(Locale loc) {
        }
    }

    /**
     * <p>
     * The system under test. Created once here and initialized in the setup
     * method to reduce code duplication across the unit-tests.
     * </p>
     */
    private MXBeanExtender _extender;

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
        JmxStores.clearListOfJmxStores();
        JmxStores.connectToJmxStores();
        Assert.assertEquals("Only one JMX Store should be connected", 1, JmxStores.getListOfJmxStoreAbstractions().size());
        _extender = new MXBeanExtender();

        /*
         * The request here is the standard that 'should' work, the expectation
         * is that per unit-tests the will most likely need to be overwritten to
         * test the conditional logic of BeanSpy.
         */
        _request = new MockHttpServletRequest(
                null);
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
        this._request = null;
        this._response = null;
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical information that
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
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * information for a specific canned class that the number 
     * of statistics returned are as expected.
     * </p>
     */
    @Test
    public void testDoGet_countStatistics() 
    {
        _request = new MockHttpServletRequest("/Canned");
        try {
            _extender.doGet(_request, _response);
            
            StatisticGroup l = _extender.getStatisticsforClass("com.interopbridges.scx.jeestats.CannedStatistics");
            assertTrue("Count statistics for a specific canned class Expected:5 got:"+l.getStatisticItemGroup().get(0).getStatistics().size(), 
                    l.getStatisticItemGroup().get(0).getStatistics().size()==5);
        } catch (Exception e) {
            Assert.fail("Count statistics for a canned class - Unexpected Exception Received: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * information on a statistic type that does not implement JeeStatistics
     * that the call fails. 
     * </p>
     */
    @Test
    public void testDoGet_StatisticClassExistsButIsIncorrect() 
    {
        _request = new MockHttpServletRequest("/Fake");
        try {
            _extender.doGet(_request, _response);
            //should get an exception thrown
            
            Assert.fail("An exception should have been thrown.");
        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * information on a invalid statistic that the call fails 
     * </p>
     */
    @Test
    public void testDoGet_StatisticReturnsAnInvalidDataType() 
    {
        _request = new MockHttpServletRequest("/Invalid/InvalidStat1");
        try {
            _extender.doGet(_request, _response);
            //should get an exception thrown
            
            Assert.fail("An exception should have been thrown.");
        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * information on a invalid statistic that no data is returned 
     * </p>
     */
    @Test
    public void testDoGet_StatisticMethosIsNotAnnotated() 
    {
        _request = new MockHttpServletRequest("/Invalid");
        try {
            _extender.doGet(_request, _response);
            String[] s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"Stats/Invalid/Properties/*");
            Assert.assertTrue(
                    "Exception no data should be returned for the Invalid statistic"+s.length, s.length==0);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * information on a invalid statistic that the call fails 
     * </p>
     */
    @Test
    public void testDoGet_StatisticReturnEmptyResultSet() 
    {
        _request = new MockHttpServletRequest("/InvalidBean");
        try {
            _extender.doGet(_request, _response);
            //should get an exception thrown
            
            Assert.fail("An exception should have been thrown. response:"+_response.ostream.buf.toString());
        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * information on a fake statistic that the call fails 
     * </p>
     */
    @Test
    public void testDoGet_SingleFakeStatisticForInvalidStatisticClass() 
    {
        _request = new MockHttpServletRequest("/Fake/FakeStat1");
        try {
            _extender.doGet(_request, _response);
            //should get an exception thrown
            
            Assert.fail("An exception should have been thrown.");
        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
        }
    }


    /**
     * <p>
     * Verify that for a legal call to retrieve ALL statistical information that
     * the content type is as expected.
     * </p>
     */
    @Test
    public void testDoGet_AllStats() 
    {
        _request = new MockHttpServletRequest(null);
        try {
            _extender.doGet(_request, _response);
            String[] results = SAXParser.XPathQuery(_response.ostream.buf.toString(), "/Stats/*");
            assertTrue("Response should contain <Stats> tag for input: \n"
                    + _response.ostream.buf.toString(), results.length > 0);
            
            new SAXParser().customQueryAssertForXmlValidationOfObject(
                    "The XML element <Stats> is not found for a generic test.",
                    _response.ostream.buf.toString(),    "Stats",    "*");
            
            new SAXParser().customQueryAssertForXmlValidationOfObject(
                    "The XML element <ClassLoader> is not found for a generic test.",
                    _response.ostream.buf.toString(),    "ClassLoader",    "/Stats/*");
            
            new SAXParser().customQueryAssertForXmlValidationOfObject(
                    "The XML element <Thread> is not found for a generic test.",
                    _response.ostream.buf.toString(),    "Thread",    "/Stats/*");
            
            new SAXParser().customQueryAssertForXmlValidationOfObject(
                    "The XML element <JITCompiler> is not found for a generic test.",
                    _response.ostream.buf.toString(),    "JITCompiler",    "/Stats/*");
            
            new SAXParser().customQueryAssertForXmlValidationOfObject(
                    "The XML element <GC> is not found for a generic test.",
                    _response.ostream.buf.toString(),    "GC",    "/Stats/*");
            
            new SAXParser().customQueryAssertForXmlValidationOfObject(
                    "The XML element <Memory> is not found for a generic test.",
                    _response.ostream.buf.toString(),    "Memory",    "/Stats/*");
            
            new SAXParser().customQueryAssertForXmlValidationOfObject(
                    "The XML element <Runtime> is not found for a generic test.",
                    _response.ostream.buf.toString(),    "Runtime",    "/Stats/*");
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received");
        }
    }
    
    /**
     * <p>
     * Unit Test to verify the XML format of the JMX Store statistic
     * </p>
     */
    @Test
    public void testJmxStoreStatisticsXmlFormat()
    {
        try
        {
            StatisticGroup statsGroup = _extender
                    .getStatisticsforClass("com.interopbridges.scx.jeestats.JmxStoreStatistics");
            Assert.assertEquals("Wrong number of statistic groups", 1, statsGroup
                    .getStatisticItemGroup().size());
            Assert.assertEquals("Wrong number of statistics", 1, statsGroup
                    .getStatisticItemGroup().get(0).getStatistics().size());
            // Verifies the XML format

            StatisticXMLTransformer xdoc = new StatisticXMLTransformer();
            String xml = xdoc.transformGroupStatistics(MXBeanExtender.StatisticXMLTag, statsGroup).toString();

            SAXParser.customQueryAssertForXmlValidationOfObjectValue(
                    "The XML element ("
                            + statsGroup.getStatisticItemGroup().get(0).getStatistics()
                                    .get(0).getStatisticName()
                            + ") for the canned class is not found.",
                    xml,
                    "com.interopbridges.scx.jmx.JdkJMXAbstraction",
                    "/Stats/JmxStores/Properties/JmxStoreNames");
        }
        catch (Exception e)
        {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * information for a specific canned class that the number 
     * of statistics returned are as expected.
     * </p>
     */
    @Test
    public void testDoGet_deep_countStatistics() {
        try {
            _extender.doGet(_request, _response);
            
            StatisticGroup l = new StatisticGroup("Canned");
            l.addStatisticItemGroup( new StatisticItemGroup());
            
            l.getStatisticItemGroup().get(0).addStatistic(new Statistic("CannedStat1",    int.class,1));
            l.getStatisticItemGroup().get(0).addStatistic(new Statistic("CannedStat2",    long.class,123L));
            l.getStatisticItemGroup().get(0).addStatistic(new Statistic("CannedStat3",    String.class,"abc"));
            l.getStatisticItemGroup().get(0).addStatistic(new Statistic("CannedStat4",    boolean.class,true));
            l.getStatisticItemGroup().get(0).addStatistic(new Statistic("CannedStat5",    int.class,100));

            StatisticXMLTransformer xdoc = new StatisticXMLTransformer();
            String xml = xdoc.transformGroupStatistics(MXBeanExtender.StatisticXMLTag, l).toString();
            
            for(int i=0;i<l.getStatisticItemGroup().size();i++)
            {
                for(int j=0;j<l.getStatisticItemGroup().get(i).getStatistics().size();j++)
                {
                    new SAXParser().customQueryAssertForXmlValidationOfObject(
                            "The XML element ("+l.getName()+") for the canned class is not found.",
                            xml,
                            l.getStatisticItemGroup().get(i).getStatistics().get(j).getStatisticName(),
                            "/Stats/Canned/Properties/*");
                }
            }
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received : " + e.getMessage());
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * Class information. 
     * </p>
     */
    @Test
    public void testDoGet_ClassLoader() {
        testDoGet_generic("ClassLoader");
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * Thread information. 
     * </p>
     */
    @Test
    public void testDoGet_Thread() {
        testDoGet_generic("Thread");
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * JITCompiler information. 
     * </p>
     */
    @Test
    public void testDoGet_JITCompiler() {
        testDoGet_generic("JITCompiler");
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * GC information. 
     * </p>
     */
    @Test
    public void testDoGet_GC() {
        testDoGet_generic("GC");
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * Memory information. 
     * </p>
     */
    @Test
    public void testDoGet_Memory() {
        testDoGet_generic("Memory");
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * Runtime information. 
     * </p>
     */
    @Test
    public void testDoGet_Runtime() {
        testDoGet_generic("Runtime");
    }

    
    /**
     * <p>
     * Verify that for an illegal request with invalid content generates the expected
     * result.
     * </p>
     */
    @Test
    public void testDoGet_InvalidStatisticType_Empty() {
        _request = new MockHttpServletRequest("/zzz");
        try {
            _extender.doGet(_request, _response);
            Assert.fail("An exception should have been thrown.");
        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
        }
    }

    /**
     * <p>
     * Verify that for an illegal request with invalid content generates the expected
     * result.
     * </p>
     */
    @Test
    public void testDoGet_InvalidInputURL() {
        _request = new MockHttpServletRequest("zzz");
        try {
            _extender.doGet(_request, _response);
            Assert.fail("An exception should have been thrown.");
        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
        }
    }
    
  
    
    /**
     * <p>
     * Presently the doPost() method is not implemented. Verify that this only
     * throws an exception.
     * </p>
     */
    @Test
    public void testDoPost_ShouldThrowException() {
        try {
            _extender.doPost(_request, _response);
            Assert.fail("An exception should have been thrown.");

        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
        }
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
     * Verify that for a legal call to retrieve statistical 
     * Class information. 
     * </p>
     */
    @Test
    public void testDoGet_StatisticGroupSingleMethod() {
        testDoGet_genericPerMethod("ClassLoader","TotalLoadedClassCount");
    }

    /**
     * <p>
     * Verify that for a illegal call to retrieve statistical 
     * Class information. 
     * </p>
     */
    @Test
    public void testDoGet_failStatisticGroupSingleMethodInvalidURL() {
        _request = new MockHttpServletRequest("/ClassLoader/TotalLoadedClassCount/abc");
        try {
            _extender.doGet(_request, _response);
            Assert.fail("An exception should have been thrown.");
        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
        }
    }

    /**
     * <p>
     * Verify that for a illegal call to retrieve statistical 
     * Class information. 
     * </p>
     */
    @Test
    public void testDoGet_failStatisticGroupSingleMethodInvalidURL2() {
        _request = new MockHttpServletRequest("/ClassLoader/TotalLoadedClassCount/abc/def");
        try {
            _extender.doGet(_request, _response);
            Assert.fail("An exception should have been thrown.");
        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
        }
    }

    /**
     * <p>
     * The init method presently does not do anything, hence this test is pretty
     * trival. Basically, it verifies that no exceptions are thrown at runtime
     * (and it does help increase our coverage numbers).
     * </p>
     */
    @Test
    public void testInit_DoesNothing() {
        try{
        _extender.init();
        }
        catch(Exception e)
        {
            Assert.fail("Init failure: " + e.getMessage());
        }
    }
    
    /**
     * <p>
     * Utility method to call the MXBeanExtender with a specific statistic type
     * and verify the number of elements and their names are correct
     * </p>
     */
    private void testDoGet_generic(String Statisticname) 
    {
        _request = new MockHttpServletRequest("/"+Statisticname);
        try {
            _extender.doGet(_request, _response);
            
            StatisticGroup l = _extender.getStatisticsforClass("com.interopbridges.scx.jeestats."+Statisticname+"Statistics");
            
            for(int i=0;i<l.getStatisticItemGroup().size();i++)
            {
                for(int j=0;j<l.getStatisticItemGroup().get(i).getStatistics().size();j++)
                {
                    new SAXParser().customQueryAssertForXmlValidationOfObject(
                            "The XML element ("+l.getName()+") for the canned class is not found.",
                            _response.ostream.buf.toString(),
                            l.getStatisticItemGroup().get(i).getStatistics().get(j).getStatisticName(),
                            "/Stats/"+Statisticname+"/Properties/*");
                }
            }
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Utility method to call the MXBeanExtender with a specific statistic type
     * and verify the number of elements and their names are correct
     * ClassLoader/TotalLoadedClassCount
     * </p>
     */
    private void testDoGet_genericPerMethod(String StatisticType, String SingleStatisticName) 
    {
        _request = new MockHttpServletRequest("/"+StatisticType+"/"+SingleStatisticName);
        try {
            _extender.doGet(_request, _response);

            new SAXParser().customQueryAssertForXmlValidationOfObject(
                    "The XML element ("+SingleStatisticName+") for the canned class is not found.",
                    _response.ostream.buf.toString(),
                    SingleStatisticName,
                    "/Stats/"+StatisticType+"/Properties/"+SingleStatisticName);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve two groups of statistics 
     * </p>
     */
    @Test
    public void testDoGet_TwoGroupsOfStatistics() 
    {
        _request = new MockHttpServletRequest("/CannedStatisticsWithTwoGroupsofStatistics");
        try {
            _extender.doGet(_request, _response);

            String[] s = (String []) SAXParser.XPathQuery(_response.ostream.buf.toString(),"Stats/TwoGroups/Properties/Name");
            Assert.assertTrue(
                    "Incorrect response to CannedStatisticsWithTwoGroupsofStatistics.\n "+
                    "The XML element ("+"Stats/TwoGroups/Properties/Name"+") for item[0] should be 'Name1'",s[0].compareTo("Name1")==0);
            Assert.assertTrue(
                    "Incorrect response to CannedStatisticsWithTwoGroupsofStatistics.\n "+
                    "The XML element ("+"Stats/TwoGroups/Properties/Name"+") for item[1] should be 'Name two'",s[1].compareTo("Name two")==0);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Verify that for a illegal call to retrieve statistical.
     * The URL data is illegal as it contains extraneous data 
     * that should not exist.
     * Class information. 
     * </p>
     */
    @Test
    public void testDoGet_InvalidCallWithExtraneousData() 
    {
        
        _request = new MockHttpServletRequest("/ClassLoader/TotalLoadedClassCount/abc/def");
        try {
            _extender.doGet(_request, _response);
            Assert.fail("An exception should have been thrown.");
        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical that the 
     * URL parameter is converted and processed correctly.
     * The input URL is "/Stats/" this needs to be processed the same as "/Stats"
     * Class information. 
     * </p>
     */
    @Test
    public void testDoGet_TransformParameterToNotContainTrailingSlash() 
    {
        
        _request = new MockHttpServletRequest("/");
        try {
            _extender.doGet(_request, _response);
            String[] s = (String []) SAXParser.XPathQuery(_response.ostream.buf.toString(),"Stats/*");
            Assert.assertTrue(
                    "Exception incorrect number of entries for the 'Stats/' call.  entries:"+s.length+" should be: 8", s.length==8);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }

    /**
     * <p>
     * Verify that for a legal call to retrieve statistical 
     * information for a specific canned class that the number 
     * of statistics returned are as expected. The call contains an extra
     * trailing '/'
     * </p>
     */
    @Test
    public void testDoGet_countStatisticsWithTrailingSlash() 
    {
        _request = new MockHttpServletRequest("/Canned/");
        try {
            _extender.doGet(_request, _response);
            
            StatisticGroup l = _extender.getStatisticsforClass("com.interopbridges.scx.jeestats.CannedStatistics");
            assertTrue("Count statistics for a specific canned class Expected:5 got:"+l.getStatisticItemGroup().get(0).getStatistics().size(), 
                    l.getStatisticItemGroup().get(0).getStatistics().size()==5);
        } catch (Exception e) {
            Assert.fail("Count statistics for a canned class - Unexpected Exception Received: " + e.getMessage());
        }
    }

     /**
     * <p>
     * Verify that for a illegal call to retrieve statistical 
     * information that an error is thrown, the URL is not empty
     * and does not begin with a '/'
     * </p>
     */
    @Test
    public void testDoGet_invalidStatsCallWithNoLeadingSlash() 
    {
        _request = new MockHttpServletRequest("?hello");
        try {
            _extender.doGet(_request, _response);
            Assert.fail("An exception should have been thrown.");
        } catch (Exception e) {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", e
                            .getClass() == ServletException.class);
        }
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
     * Verify that if the length of a request URL (without a QueryString) exceeds 2048, 
     * the request should fail and throw an exception.
     * </p>
     *
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_URLLengthExceedsLimits_ValidResponse() 
    {
        StringBuffer strURL;
        MockHttpServletRequest request = new MockHttpServletRequest("");

        strURL = buildRequestURL( 2049, "http://www.", "/BeanSpy/Stats" );

        request.setRequestURL(strURL); 

        Assert.assertTrue("The length of the constructed URL should be 2049" ,
                          2049 == request.getRequestURL().toString().length());  
              
        try {
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
     * Verify that if the length of a request URL (with a QueryString) exceeds 2048, the request should fail.
     * </p>
     *
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_URLLengthExceedsLimits_withQueryString_InvalidResponse() 
    {
       StringBuffer strURL;
       MockHttpServletRequest request = new MockHttpServletRequest("");
    
       request.addParameter("JMXQuery", "com.interopbridges.scx:jmxType=operationCall");
       strURL = buildRequestURL( 2048 - request.getQueryString().length(), "http://www.", "/BeanSpy/MBeans" );
    
       request.setRequestURL(strURL); 
     
      /*
       * "?" does not belong to either URL or query string, so add 1 here.
       */
       Assert.assertTrue("The length of the constructed URL should be 2049" ,
             2049 == (request.getRequestURL().toString().length()+ request.getQueryString().length() + 1));  
                   
       try {
           _extender.doGet(request, _response);
           Assert.fail("Function should fail");          
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
                            "Should throw an ERROR_URL_LENGTH_EXCEEDS_LIMITS exception",
                            ((ScxException) ((ServletException)e).getRootCause()).getExceptionCode(),
                            ScxExceptionCode.ERROR_URL_LENGTH_EXCEEDS_LIMITS);
       }
    }

    /**
     * <p>
     * Verify that if the length of a URL equals to 2048, the request should succeed.
     * </p>
     *
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_URLLengthEqualsTo2048_ValidResponse() 
    {
       StringBuffer strURL;
       MockHttpServletRequest request = new MockHttpServletRequest("");

       strURL = buildRequestURL( 2048, "http://www.", "/BeanSpy/Stats" );

       request.setRequestURL(strURL); 
        
       Assert.assertTrue("The length of the constructed URL should be 2048" ,
                         2048 == request.getRequestURL().toString().length());  
        
       try{
           _extender.doGet(request, _response); 
           assertTrue("Content type", _response.contentType
               .equals("application/xml; charset=utf-8"));
       } 
       catch (Exception e)
       {
          Assert.fail("Unexpected Exception Received: " + e.getMessage());
       }
    }
    
    
    /**
     * <p>
     * Verify that if the length of a request URL is less than 2048, the request should succeed.
     * </p>
     *
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_URLLengthLessThan2048_ValidResponse() {
       StringBuffer strURL;
       MockHttpServletRequest request = new MockHttpServletRequest("");

       strURL = buildRequestURL( 2047, "http://www.", "/BeanSpy/Stats" );

       request.setRequestURL(strURL); 

       Assert.assertTrue("The length of the constructed URL should be 2047",
           2047 == request.getRequestURL().toString().length());
       try {
           _extender.doGet(request, _response);
           assertTrue("Content type", _response.contentType
                      .equals("application/xml; charset=utf-8"));
        }
        catch (Exception e)
        {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }
}



