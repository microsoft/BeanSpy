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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.management.DynamicMBean;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
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

import com.interopbridges.scx.beanspy.BeanSpy;
import com.interopbridges.scx.jmx.JdkJMXAbstraction;
import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.mbeans.DummyInvokeMBean;
import com.interopbridges.scx.mbeans.MBeanGetter;
import com.interopbridges.scx.mbeanserver.MockMBeanServer;
import com.interopbridges.scx.util.JmxConstant;
import com.interopbridges.scx.util.SAXParser;

/**
 * Class to test the BeanSpy servlet
 * 
 * @author Geoff Erasmus
 */
public class BeanSpyPOSTTest {

    /**
     * Test implementation of a HttpServletRequest to be able to feed the
     * servlet test data
     */
    private class MockHttpServletRequest implements HttpServletRequest {
        private String _request;
        private HashMap<String,String[]> Params=null;
        private BufferedReader _reader=null;
        private int _dataLength = 0;

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

        public String getQueryString() {
            return null;
        }

        public String getRemoteUser() {
            return null;
        }

        public String getRequestURI() {
            return null;
        }

        public StringBuffer getRequestURL() {
            return null;
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
            return _dataLength;
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
            if(Params!=null)
            {
               return Params.get(arg0)[0];
            }
            return null;
        }

        public Map<?, ?> getParameterMap() {
            return Params;
        }

        public Enumeration<?> getParameterNames() {
            return null;
        }

        public String[] getParameterValues(String arg0) {
            if(Params!=null)
            {
               return Params.get(arg0);
            }
            return null;
        }

        public String getProtocol() {
            return null;
        }

        public BufferedReader getReader() throws IOException {
            return _reader;
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
        
        /*
         * Helper function to add data to the request
         */
        public void setContext(String XMLData)
        {
            _reader = new BufferedReader(new StringReader(XMLData));
           _dataLength = XMLData.length();   
        }
        
        /*
         * Helper function to add parameters to the request
         */
        public void addParameter(String key, String value)
        {
           if (Params==null)
           {
              Params = new HashMap<String,String[]>();
           }
           String[] str;
           String[] s = Params.get(key);
           if(s!=null)
           {
               str = new String[s.length+1];
               System.arraycopy(s, 0, str, 0, s.length);
               str[s.length] = value;
           }
           else
           {
               str = new String[1];
               str[0] = value;
           }

           Params.put(key, str);
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
     * The underlying MBeanServer required to register and unregister the MBeans 
     * </p>
     */
    private javax.management.MBeanServer _MBeanstore;

    /**
     * <p>
     * The name for the registered mbean 
     * </p>
     */
    private static String mbeanName = "com.interopbridges.scx:name=DummyInvokeMBean";
    
    /**
     * <p>
     * Interface to getting the desired MBeans from the JMX Store (as XML).
     * </p>
     */
    protected MBeanGetter     _mbeanAccessor;
    
    /**
     * <p>
     * The system under test. Created once here and initialized in the setup
     * method to reduce code duplication across the unit-tests.
     * </p>
     */
    private BeanSpy _extender;
    
    
    /**
     * <p>
     * Buffer to hold the XML POST body.
     * </p>
     */
    private StringBuffer XMLData;
    
    /**
     * <p>
     * Method invoked before each unit-test in this class.
     * </p>
     * 
     * @throws Execption
     *             If something went wrong when initializing the MBean Store
     */
     @Before
     public void Setup() throws Exception 
     {
         /* 
         * Create a new MBeanServer to host the MBean that will be queried.
         */
         JmxStores.clearListOfJmxStores();
         MockMBeanServer.Reset_TestMBeanServer();
         _MBeanstore = MockMBeanServer.getInstance();
         JmxStores.addStoreToJmxStores(new JdkJMXAbstraction(_MBeanstore));
         
         Assert.assertEquals("Only one JMX Store should be connected", 1, JmxStores.getListOfJmxStoreAbstractions().size());
         
         _mbeanAccessor = new MBeanGetter(JmxStores.getListOfJmxStoreAbstractions());
         /* 
          * Create the MBean that will be queried by the tests.
          */
         DynamicMBean dummymbean = new DummyInvokeMBean();
         try
         {
             /* 
              * register the MBean on the MBeanServer.
              */
             JmxStores.getListOfJmxStoreAbstractions().get(0).registerMBean(
                     dummymbean, new ObjectName(mbeanName));
             
             /*
              * make sure the correct bean got loaded correctly
              */
             Set<ObjectInstance> oinst = 
                 JmxStores.getListOfJmxStoreAbstractions().get(0).
                 queryMBeans(new ObjectName(mbeanName),null); 
             assertTrue(oinst.size()==1); 

             assertTrue(JmxStores.getListOfJmxStoreAbstractions().get(0).
                     getMBeanInfo(new ObjectName(mbeanName)).
                     getDescription().compareTo("Microsoft Test MBean")==0); 

             /*
              * make sure that there is only 1 MBean in the store
              */
             assertTrue(JmxStores.getListOfJmxStoreAbstractions().get(0).getMBeanCount()==1); 
             
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received registering the MBean: " + e.getMessage());
         }
         
         _extender = new BeanSpy();
         _request = new MockHttpServletRequest("/Invoke");
         _response = new MockHttpServletResponse();
         XMLData = new StringBuffer().
                     append("<Invoke>").
                     append("<BeanObjectName>");
     }

     /**
     * <p>
     * Unit Test Teardown method
     * </p>
     */
     @After
     public void Teardown() 
     {
         try
         {
             _MBeanstore.unregisterMBean(new ObjectName(mbeanName));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received unregistering the MBean: " + e.getMessage());
         }
         JmxStores.clearListOfJmxStores();
     }
    
    /**
     * <p>
     * Verify that for a legal call to invoke a enumerate a supported MBeans type that
     * the content type is as expected.
     * </p>
     */
    @Test
    public void testDoGet_contentType() 
    {
        try 
        {
            _request.setContext(BuildXml(mbeanName, "VoidVoidMethod"));
            
            _extender.doPost(_request, _response);
            assertTrue("Content type", _response.contentType
                    .equals("application/xml; charset=utf-8"));

        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for a legal Invoke call to canned MBean that
     * the Response XML looks like we expected it to.
     * </p>
     */
    @Test
    public void testDoPost_ValidInput_ValidResponse()  
    {
        _request.setContext(BuildXml(mbeanName, "VoidVoidMethod"));
        
        try 
        {
            _extender.doPost(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] s = (String []) SAXParser.XPathQuery(tmpResponse,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain SUCCESS :"+tmpResponse,s[0].matches("SUCCESS"));
        } 
        catch (Exception e) 
        {
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
        _request.setContext("");
        
        try 
        {
            _extender.doPost(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] s = (String []) SAXParser.XPathQuery(tmpResponse,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR",s[0].matches("ERROR"));
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for an illegal request with no reader, generates the expected
     * result.
     * </p>
     */
    @Test
    public void testDoGet_InvalidInput_NULL_Reader() {
        try 
        {
            _extender.doPost(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] s = (String []) SAXParser.XPathQuery(tmpResponse,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR",s[0].matches("ERROR"));
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for an illegal request with a malformed XML body generates the expected
     * result.
     * </p>
     */
    @Test
    public void testDoGet_InvalidInput_MalformedXML() {
        _request.setContext("<Invoke>");
        
        try 
        {
            _extender.doPost(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] s = (String []) SAXParser.XPathQuery(tmpResponse,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR",s[0].matches("ERROR"));
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for an illegal request with the
     * incorrect path generates the expected result.
     * </p>
     */
    @Test
    public void testDoGet_InvalidInput_Incorrect_Path() {
        _request = new MockHttpServletRequest("/novalidpath");
        _request.setContext(BuildXml(mbeanName, "VoidVoidMethod"));
        
        try 
        {
            _extender.doPost(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] s = (String []) SAXParser.XPathQuery(tmpResponse,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR",s[0].matches("ERROR"));
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }

    
    /**
     * <p>
     * Verify that for an illegal request with illegal ObjectName/query syntax
     * (there is a colon missing that should separate the domain and the
     * attributes.
     * </p>
     */
    @Test
    public void testDoGet_InvalidInput_BadObjectNameSyntax() {
    }

    /**
     * <p>
     * Verify that for an legal request with no matching mbean that
     * the correct error is returned.
     * </p>
     */
    @Test
    public void testDoPost_InvalidInput_NotMBeans() 
    {
        _request.setContext(BuildXml("Garbage/com.interopbridges.scx:jmxType=operationCall", "VoidVoidMethod"));
        
        try 
        {
            _extender.doPost(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] s = (String []) SAXParser.XPathQuery(tmpResponse,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR",s[0].matches("ERROR"));
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for an invalid MBean the correct
     * error is returned.
     * </p>
     */
    @Test
    public void testDoPost_InvalidInput_WrongDomain() {
        _request.setContext(BuildXml("Garbage:jmxType=operationCall", "VoidVoidMethod"));
        
        try 
        {
            _extender.doPost(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] s = (String []) SAXParser.XPathQuery(tmpResponse,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR",s[0].matches("ERROR"));
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for a legal call to enumerate a supported MBeans type with parameters that
     * the Response XML looks like we is as expected.
     * </p>
     */
    @Test
    public void testDoPost_ValidInputWithParameters_ValidResponse() 
    {
        _request.setContext(BuildXml(mbeanName, "VoidVoidMethod"));
        _request.addParameter(JmxConstant.STR_MAXSIZE, "1000");
        _request.addParameter(JmxConstant.STR_MAXTIME, "1000");
        
        try 
        {
            _extender.doPost(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] s = (String []) SAXParser.XPathQuery(tmpResponse,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain SUCCESS",s[0].matches("SUCCESS"));
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for a legal call to enumerate a supported MBeans type with invalid parameters that
     * 
     * the Response XML looks like we is as expected. Invalid parameters should generate errors.
     * </p>
     */
    @Test
    public void testDoPost_ValidInputWithInvalidParameters_ValidResponse() 
    {
        _request.setContext(BuildXml(mbeanName, "VoidVoidMethod"));
        _request.addParameter(JmxConstant.STR_MAXSIZE, "abc");
        _request.addParameter(JmxConstant.STR_MAXTIME, "abc");
        
        try 
        {
            _extender.doPost(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            String[] s = (String []) SAXParser.XPathQuery(tmpResponse,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR",s[0].matches("ERROR"));
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for a legal call to invoke a valid MBean method an error is
     * thrown if there is NO response buffer supplied to the POST call.
     * The expected response is an exception being thrown from the servlet.
     * </p>
     */
    @Test
    public void testDoPost_ValidInput_NoResponseBuffer() 
    {
        _request.setContext(BuildXml(mbeanName, "VoidVoidMethod"));
        _request.addParameter(JmxConstant.STR_MAXSIZE, "abc");
        _request.addParameter(JmxConstant.STR_MAXTIME, "abc");
        
        try 
        {
            _response = null;
            _extender.doPost(_request, _response);
            Assert.fail("Unexpected result an Exception should have been thrown");
        } 
        catch (Exception e) 
        {
            Assert.assertTrue(
                    "Exception should have been of type ServletException", 
                    e.getClass() == ServletException.class);
        }
    }
    
    /**
     * <p>
     * Verify that for a legal call to invoke a valid MBean method an error is
     * thrown if the input XML is greater than a certain size.
     * </p>
     */
    @Test
    public void testDoPost_InvalidInput_ToooBig() 
    {
        _request.setContext(BuildXml(mbeanName, "BigMethod"));
        _request.addParameter(JmxConstant.STR_MAXSIZE, "1000");
        _request.addParameter(JmxConstant.STR_MAXTIME, "5000");
        
        try 
        {
            _extender.doPost(_request, _response);
            
            String tmpResponse = _response.ostream.buf.toString();
            String[] s = (String []) SAXParser.XPathQuery(tmpResponse,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR",s[0].matches("ERROR"));
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }
    
    /**
     * <p>
     * Verify that for a legal call to invoke a valid MBean method an error is
     * thrown if there is NO JMX store detected.
     * </p>
     */
    @Test
    public void testDoPost_ValidInput_NoJMXStore() 
    {
        _request.setContext(BuildXml(mbeanName, "VoidVoidMethod"));
        
        try 
        {
            JmxStores.clearListOfJmxStores();
            _extender.doPost(_request, _response);
            
            String tmpResponse = _response.ostream.buf.toString();
            String[] s = (String []) SAXParser.XPathQuery(tmpResponse,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR",s[0].matches("ERROR"));
        } 
        catch (Exception e) 
        {
            Assert.fail("Unexpected Exception Received");
        }
    }
    
    /**
     * <p>
     * Helper method to aid in the construction of the XML request body.
     * </p>
     */
    protected String BuildXml(String MBeanName, String MethodName)
    {
        XMLData.
            append(MBeanName).
            append("</BeanObjectName>").
            append("<Method name=\"").
            append(MethodName).
            append("\"></Method>").
            append("</Invoke>");
        return XMLData.toString();
    }
}
