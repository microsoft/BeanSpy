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
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.mxbeanextender.MXBeanExtender;
import com.interopbridges.scx.util.SAXParser;

/**
 * Class to test the MXBean Extender servlet
 * These tests specifically target the "Stats/Info" request
 * 
 * @author Geoff Erasmus
 */
public class MXBeanExtenderInfoTest {

    /**
     * Test implementation of a HttpServletRequest to be able to feed the
     * servlet test data
     */
    private class MockHttpServletRequest implements HttpServletRequest {
        private String _request;

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
     *             If something went wrong when initializing
     */
    @Before
    public void Setup() throws Exception 
    {
        
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
    public void testDoGet_contentType() 
    {
        try 
        {
            _extender.doGet(_request, _response);
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
     * Verify that for a illegal call to retrieve JEE server information 
     * that the correct information is received. 
     * </p>
     */
    @Test
    public void testDoGet_ApplicationServerInfo_JVMMemory() 
    {
        _request = new MockHttpServletRequest("/Info");
        try 
        {
            _extender.doGet(_request, _response);

            String[] s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/JVMMemory/Properties/MaxHeapSize");
            Assert.assertTrue(
                    "Incorrect response to /Info MaxHeapSize does not exist.\n "+
                    "The XML element (/Info/JVMMemory/Properties/MaxHeapSize) is empty",s[0].length()!=0);
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for a illegal call to retrieve JEE server information 
     * that the correct information is received. 
     * </p>
     */
    @Test
    public void testDoGet_ApplicationServerInfo_OperatingSystem() 
    {
        _request = new MockHttpServletRequest("/Info");
        try 
        {
            _extender.doGet(_request, _response);

            String[] s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/OperatingSystem/Properties/Name");
            Assert.assertTrue(
                    "Incorrect response to /Info/OperatingSystem/Properties/Name does not exist.\n "+
                    "The XML element (/Info/OperatingSystem/Properties/Name) is empty",s[0].length()!=0);
            
            s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/OperatingSystem/Properties/Version");
            Assert.assertTrue(
                    "Incorrect response to /Info/OperatingSystem/Properties/Version does not exist.\n "+
                    "The XML element (/Info/OperatingSystem/Properties/Version) is empty",s[0].length()!=0);
            
            s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/OperatingSystem/Properties/Architecture");
            Assert.assertTrue(
                    "Incorrect response to /Info/OperatingSystem/Properties/Architecture does not exist.\n "+
                    "The XML element (/Info/OperatingSystem/Properties/Architecture) is empty",s[0].length()!=0);
            
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received");
        }
    }

    /**
     * <p>
     * Verify that for a illegal call to retrieve JEE server information 
     * that the correct information is received. 
     * </p>
     */
    @Test
    public void testDoGet_ApplicationServerInfo_JavaVirtualMachine() 
    {
        _request = new MockHttpServletRequest("/Info");
        try 
        {
            _extender.doGet(_request, _response);

            String[] s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/JavaVirtualMachine/Properties/Name");
            Assert.assertTrue(
                    "Incorrect response to /Info/JavaVirtualMachine/Properties/Name does not exist.\n "+
                    "The XML element (/Info/JavaVirtualMachine/Properties/Name) is empty",s[0].length()!=0);
            
            s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/JavaVirtualMachine/Properties/ClassPath");
            Assert.assertTrue(
                    "Incorrect response to /Info/JavaVirtualMachine/Properties/ClassPath does not exist.\n "+
                    "The XML element (/Info/JavaVirtualMachine/Properties/ClassPath) is empty",s[0].length()!=0);
            
            s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/JavaVirtualMachine/Properties/Version");
            Assert.assertTrue(
                    "Incorrect response to /Info/JavaVirtualMachine/Properties/Version does not exist.\n "+
                    "The XML element (/Info/JavaVirtualMachine/Properties/Version) is empty",s[0].length()!=0);
            
            s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/JavaVirtualMachine/Properties/LibraryPath");
            Assert.assertTrue(
                    "Incorrect response to /Info/JavaVirtualMachine/Properties/LibraryPath does not exist.\n "+
                    "The XML element (/Info/JavaVirtualMachine/Properties/LibraryPath) is empty",s[0].length()!=0);
            
            s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/JavaVirtualMachine/Properties/StartupTime");
            Assert.assertTrue(
                    "Incorrect response to /Info/JavaVirtualMachine/Properties/StartupTime does not exist.\n "+
                    "The XML element (/Info/JavaVirtualMachine/Properties/StartupTime) is empty",s[0].length()!=0);
            
            s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/JavaVirtualMachine/Properties/BuildVersion");
            Assert.assertTrue(
                    "Incorrect response to /Info/JavaVirtualMachine/Properties/BuildVersion does not exist.\n "+
                    "The XML element (/Info/JavaVirtualMachine/Properties/BuildVersion) is empty",s[0].length()!=0);
            
            s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/JavaVirtualMachine/Properties/VendorName");
            Assert.assertTrue(
                    "Incorrect response to /Info/JavaVirtualMachine/Properties/VendorName does not exist.\n "+
                    "The XML element (/Info/JavaVirtualMachine/Properties/VendorName) is empty",s[0].length()!=0);
            
            s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/JavaVirtualMachine/Properties/StartupOption");
            Assert.assertTrue(
                    "Incorrect response to /Info/JavaVirtualMachine/Properties/StartupOption does not exist.\n "+
                    "The XML element (/Info/JavaVirtualMachine/Properties/StartupOption) is empty",s[0]!=null);
            
            s = (String [])SAXParser.XPathQuery(_response.ostream.buf.toString(),"/Info/JavaVirtualMachine/Properties/JavaInstallDirectory");
            Assert.assertTrue(
                    "Incorrect response to /Info/JavaVirtualMachine/Properties/JavaInstallDirectory does not exist.\n "+
                    "The XML element (/Info/JavaVirtualMachine/Properties/JavaInstallDirectory) is empty",s[0].length()!=0);
            
        } catch (Exception e) {
            Assert.fail("Unexpected Exception Received");
        }
    }
}



