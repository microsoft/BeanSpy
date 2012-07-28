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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.interopbridges.scx.util.MockHttpRequestHelper;

/**
 * Test implementation of a HttpServletRequest to be able to feed the
 * servlet test data
 */
public class MockHttpServletRequest implements HttpServletRequest {
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

    

    public String getRemoteUser() {
        return null;
    }

    public String getRequestURI() {
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