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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.mbeans.MBeanGetter;
import com.interopbridges.scx.mbeans.MBeanInvoker;
import com.interopbridges.scx.util.JmxConstant;
import com.interopbridges.scx.util.JmxURLCheck;
import com.interopbridges.scx.xml.InvokeDecoder;

/**
 * <p>
 * Class to extend the JMX by enabling sending of MBean data over HTTP
 * </p>
 * 
 * @author Jonas Kallstrom
 */
public class BeanSpy extends HttpServlet
{

    /**
     * <p>
     * Version number for serialization.
     * </p>
     */
    private static final long serialVersionUID = -4343920342824939215L;

    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    protected ILogger _logger;

    /**
     * <p>
     * Interface to getting the desired MBeans from the JMX Store (as XML).
     * </p>
     */
    protected MBeanGetter     _mbeanAccessor;

    /**
     * <p>
     * Constructor of the object.
     * </p>
     */
    public BeanSpy()
    {
        super();
        this._logger = LoggingFactory.getLogger();
        _mbeanAccessor = new MBeanGetter(JmxStores.getListOfJmxStoreAbstractions());
    }

    /**
     * <p>
     * Destruction of the servlet.
     * </p>
     */
    public void destroy()
    {
        JmxStores.clearListOfJmxStores();
        super.destroy();
    }

    /**
     * <p>
     * The doGet method of the servlet.
     * </p>
     * 
     * <p>
     * This method is called when a form has its tag value method equals to get.
     * </p>
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     * @throws UnsupportedEncodingException
     *             if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, UnsupportedEncodingException
    {

        String JMXQuery = null;
        
        if(JmxURLCheck.URLLengthExceedsLimit(request.getRequestURL(), request.getQueryString()))
        {
            this._logger.fine("The length of the URL exceeds " + JmxConstant.URL_LENGTH_LIMITS + " characters.");
            throw new ServletException(new ScxException(ScxExceptionCode.ERROR_URL_LENGTH_EXCEEDS_LIMITS));
        }

        this._logger.fine(new StringBuffer("Received HttpServletRequest").toString());
        
        HashMap<String, String[]> Params = JmxURLCheck.getValidInputs(request);
        
        if (Params.get(JmxConstant.STR_JMXQUERY)!= null)
         {
             /**
              * <p>
              * If a user input multiple values for "JMXQuery", get the first one.
              * Others are stored but are ignored.
              * </p>
              */
             JMXQuery = Params.get(JmxConstant.STR_JMXQUERY)[0];
         }  

        try
        {        
            if(JMXQuery != null)
            {
                /*
                 * The JMXQuery parameter has been used to input the JMX Query the query is expected to look like
                 * '/MBeans?<params>'. In practice, this might be
                 * '/MBeans?JMXQuery=com.interopbridges.scx:jmxType=operationCall'. Call into
                 * the MBeans store and return back the XML representation of the
                 * MBean(s).
                 */
                if(JMXQuery.length()==0)
                {
                    this._logger.fine(new StringBuffer("Invalid servlet request JMXQuery specified with no value").toString());
                    throw new ScxException(ScxExceptionCode.ERROR_INVALID_SERVLET_REQUEST_EMPTY_JMXQUERY);
                }
                else
                {
                    response.setContentType("application/xml; charset=utf-8");

                    String xml = _mbeanAccessor.getMBeansAsXml(JMXQuery, Params)
                            .toString();

                    if (xml.toString().length() > JmxConstant.ABS_MAX_XML_SIZE)
                    {
                            Object[] args = {new Integer(JmxConstant.ABS_MAX_XML_SIZE), JMXQuery}; 
                            this._logger.finer(MessageFormat.format( "The size of the XML response has reached the limits of {0} bytes by the query: {1}.", args ) ) ; 
                            throw new ScxException(ScxExceptionCode.ERROR_SIZE_OF_XML_FILES_EXCEED_LIMITS, args);
                    }
             
                    Writer out = new OutputStreamWriter(response.getOutputStream(),
                            "UTF-8");

                    out.write(xml);
                    out.flush();
                    out.close();
                }
            }
            else
            {
                this._logger.fine(new StringBuffer("Invalid servlet request JMXQuery not specified as a parameter").toString());
                throw new ScxException(ScxExceptionCode.ERROR_INVALID_SERVLET_REQUEST_NO_JMXQUERY);
            }
        }
        catch (ScxException e)
        {
            throw new ServletException(e);
        }

    }

    /**
     * <p>
     * The doPost method of the servlet.
     * </p>
     * 
     * <p>
     * This method is called when a form has its tag value method equals to
     * post.
     * </p>
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException
    {
        String responseXML = null;
        MBeanInvoker mbm = null;

        this._logger.fine(new StringBuffer("Received HttpServletRequest POST").toString());

        try
        {
            if(request.getPathInfo().compareTo(JmxConstant.STR_INVOKE_URL)==0)
            {
                InvokeDecoder invokeDec = new InvokeDecoder(request.getReader(),request.getContentLength());
                
                this._logger.fine(new StringBuffer("POST Input XML data=")
                .append(invokeDec.getRawInputData()).toString());

                invokeDec.DecodeInput();
                
                mbm = new MBeanInvoker(_mbeanAccessor, invokeDec.getBeanObjectName(), 
                                                       invokeDec.getMethodName(), 
                                                       invokeDec.getMethodParams());
                responseXML = mbm.transformMBeanCall(
                                request.getParameter(JmxConstant.STR_MAXTIME),
                                request.getParameter(JmxConstant.STR_MAXSIZE)).toString();
            }
            else
            {
                this._logger.fine(new StringBuffer("Invalid POST servlet request for BeanSpy").toString());
                throw new ScxException(ScxExceptionCode.ERROR_INVALID_POST_QUERY);
            }
        }
        catch (Exception e)
        {
            this._logger.fine(e.getMessage());
            responseXML = MBeanInvoker.FormatXMLError(e);
        }
        
        try
        {
            response.setContentType("application/xml; charset=utf-8");
            
            Writer out = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
    
            out.write(responseXML);
            out.flush();
            out.close();
        }
        catch(Exception e)
        {
            this._logger.fine(e.getMessage());
            throw new ServletException(e);
        }
    }

    /**
     * <p>
     * Initialization of the servlet.
     * </p>
     */
    public void init()
    {
    }

}
