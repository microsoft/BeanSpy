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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.jeestats.IStatistics;
import com.interopbridges.scx.jeestats.Statistic;
import com.interopbridges.scx.jeestats.StatisticGroup;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.util.JmxConstant;
import com.interopbridges.scx.util.JmxURLCheck;
import com.interopbridges.scx.xml.StatisticXMLTransformer;


/**
 * <p><pre>
 * Servlet class to extend the management extensions provided 
 * by the Java 1.5 runtime and return the XML management metrics over HTTP.
 *  
 * The servlet supports 3 different types of request:
 * 1. Empty request:           
 *         The response for an empty request is a XML document containing all statistical 
 *         information exposed by the servlet.
 * 2. Type specific request: 
 *         The response for a type specific request is a XML document containing all 
 *         statistical information exposed by the servlet for a specific type (Memory, Runtime etc).
 * 3. Statistic specific request: 
 *         The response for a statistic specific request is a XML document containing the  
 *         statistical information exposed by the servlet for a specific statistic 
 *         (Memory\HeapUsedMemory, Runtime\UpTime etc).
 * 
 * 
 * </pre></p>
 * 
 * @author Geoff Erasmus
 */
public class MXBeanExtender extends HttpServlet 
{

    /**
     * 
     */
    private static final long serialVersionUID = 1417683441123718358L;

    /**
     * <p>
     * Prefix for all statistic classes
     * </p>
     */
    private static final String jeeStatsPrefix = "com.interopbridges.scx.jeestats";
    
    /**
     * <p>
     * Prefix for all statistic classes
     * </p>
     */
    private static final String jeeInfoPrefix = "com.interopbridges.scx.jeeinfo";
    
    /**
     * <p>
     * URL part denoting a statistical query
     * </p>
     */
    public static final String StatisticXMLTag = "Stats";

    /**
     * <p>
     * URL part denoting a JEE Information query
     * </p>
     */
    public static final String InformationXMLTag = "Info";
    
    /**
     * <p>
     * Suffix for all statistic classes
     * </p>
     */
    private static final String jeeStatsSuffix = "Statistics";
    
    /**
     * <p>
     * JmxStore statistic class name
     * </p>
     */
    private static final String JmxStoreStatisticsClassName = jeeStatsPrefix+".JmxStoreStatistics";
    
    /**
     * <p>
     * ClassLoader statistic class name
     * </p>
     */
    private static final String ClassLoaderStatisticsClassName = jeeStatsPrefix+".ClassLoaderStatistics";
    
    /**
     * <p>
     * Thread statistic class name
     * </p>
     */
    private static final String ThreadStatisticsClassName = jeeStatsPrefix+".ThreadStatistics";
    
    /**
     * <p>
     * Just In Time Compiler statistic class name
     * </p>
     */
    private static final String JITCompilerStatisticsClassName = jeeStatsPrefix+".JITCompilerStatistics";
    
    /**
     * <p>
     * garbage Collector statistic class name
     * </p>
     */
    private static final String GCStatisticsClassName = jeeStatsPrefix+".GCStatistics";
    
    /**
     * <p>
     * Memory statistic class name
     * </p>
     */
    private static final String MemoryStatisticsClassName = jeeStatsPrefix+".MemoryStatistics";

    /**
     * <p>
     * Runtime statistic class name
     * </p>
     */
    private static final String RuntimeStatisticsClassName = jeeStatsPrefix+".RuntimeStatistics";

    /**
     * <p>
     * Jee JVM info class name
     * </p>
     */
    private static final String JeeJVMInfoClassName = jeeInfoPrefix+".JeeJVMInfo";
    
    /**
     * <p>
     * Jee JVM Memoryinfo class name
     * </p>
     */
    private static final String JeeJVMMemoryInfoClassName = jeeInfoPrefix+".JeeJVMMemoryInfo";
    
    /**
     * <p>
     * Jee JVM Operating System info class name
     * </p>
     */
    private static final String JeeJVMOSInfoClassName = jeeInfoPrefix+".JeeJVMOSInfo";
    
    /**
     * <p>
     * Jee Server info class name
     * </p>
     */
    private static final String JeeServerInfoClassName = jeeInfoPrefix+".JeeServerInfo";
    
    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    protected ILogger _logger;

    
    /**
     * Constructor of the object.
     */
    public MXBeanExtender() {
        super();
        this._logger = LoggingFactory.getLogger();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
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

        String path = request.getPathInfo();

        if(JmxURLCheck.URLLengthExceedsLimit(request.getRequestURL(), request.getQueryString()))
        {
            this._logger.fine("The length of the URL exceeds " + JmxConstant.URL_LENGTH_LIMITS + " characters.");
            throw new ServletException(new ScxException(ScxExceptionCode.ERROR_URL_LENGTH_EXCEEDS_LIMITS));
        }

        /*
         * The request generally start with a /. This causes the split method to
         * create an extra 'empty' value at the beginning of the results. To
         * bypass this trivial oddity, start the split on a substring that skips
         * the first (assumed) slash.
         */
        String[] pieces = null;
        if (path != null)
        {
            /*
             * Remove trailing '/' from the input URL 
             */
            if(path.endsWith("/"))
            {
                path = path.substring(0,path.length()-1); 
            }

            if ((path.length() > 0) && (path.charAt(0) == '/'))
            {
                this._logger.fine(new StringBuffer(
                        "Received HttpServletRequest for ").append(path)
                        .toString());

                pieces = path.substring(1).split("/");
                this._logger.finer(new StringBuffer(
                        "Splitting the path at / yielded ")
                        .append(pieces.length).append(" pieces").toString());
            }
            else
            {
                if (path.length() !=0)
                {
                    this._logger.fine(new StringBuffer("Invalid servlet request for stats, invalid character:").append(path.charAt(0)).toString());
                    throw new ServletException(new ScxException(ScxExceptionCode.ERROR_INVALID_SERVLET_REQUEST_STATS));
                }
            }
        }

        // There are 3 different kinds of request:
        // Generic request - no command line arguments and the expected 
        // response is all available statistics.
        //"http://xxx/extender/Stats" 
        //
        // Group request - the command line specifies the specific group 
        // (category) of statistics to retrieve.
        // "http://xxx/extender/Stats/Memory"
        //
        // Specific request- the command line contains a specific statistic to retrieve.
        // "http://xxx/extender/Stats/Memory/HeapUsedMemory"

        if ((pieces == null) || (pieces.length == 0)) // command line ends
        // "/Stats"
        {
            try
            {

                Vector<StatisticGroup> stats = new Vector<StatisticGroup>();
                // Retrieve all the statistics for all classes
                //
                stats.add(getStatisticsforClass(JmxStoreStatisticsClassName));
                stats.add(getStatisticsforClass(ClassLoaderStatisticsClassName));
                stats.add(getStatisticsforClass(ThreadStatisticsClassName));
                stats.add(getStatisticsforClass(JITCompilerStatisticsClassName));
                stats.add(getStatisticsforClass(GCStatisticsClassName));
                stats.add(getStatisticsforClass(MemoryStatisticsClassName));
                stats.add(getStatisticsforClass(RuntimeStatisticsClassName));

                StatisticXMLTransformer xdoc = new StatisticXMLTransformer();
                String xml = xdoc.transformAllStatistics(StatisticXMLTag, stats).toString();
                response.setContentType("application/xml; charset=utf-8");
                Writer out = new OutputStreamWriter(response.getOutputStream(),
                        "UTF-8");
                out.write(xml);
                out.flush();
                out.close();
            }
            catch (ScxException e)
            {
                throw new ServletException(e);
            }
        }
        else
        {
            if (pieces.length == 1) // command line ends "/stats/thread"
            {
                try
                {
                    String xml = null;
                    StatisticXMLTransformer xdoc = new StatisticXMLTransformer();

                    /*
                     * The /stats/info is a special case and returns information
                     * about the operating system, JVM, Heap size and Application server
                     */
                    if(pieces[0].compareToIgnoreCase("Info")==0)
                    {
                        Vector<StatisticGroup> stats = new Vector<StatisticGroup>();
                        /*
                         * Retrieve all the information for all classes
                         */
                        stats.add(getStatisticsforClass(JeeJVMMemoryInfoClassName));
                        stats.add(getStatisticsforClass(JeeJVMOSInfoClassName));
                        stats.add(getStatisticsforClass(JeeJVMInfoClassName));
                        stats.add(getStatisticsforClass(JeeServerInfoClassName));

                        xml = xdoc.transformAllStatistics(InformationXMLTag, stats).toString();
                    }
                    else
                    {
                        StatisticGroup stat = getStatisticsforClass(jeeStatsPrefix
                                + "." + pieces[0] + jeeStatsSuffix);

                        xml = xdoc.transformGroupStatistics(StatisticXMLTag, stat).toString();
                    }
                    response.setContentType("application/xml; charset=utf-8");
                    Writer out = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
                    out.write(xml);
                    out.flush();
                    out.close();

                }
                catch (ScxException e)
                {
                    throw new ServletException(e);
                }
            }
            else if (pieces.length == 2) // command line ends
            // "/stats/thread/ThreadCount"
            {
                try
                {
                    StatisticXMLTransformer xdoc = new StatisticXMLTransformer();
                    Statistic s = getSingleStatisticforClass(jeeStatsPrefix
                            + "." + pieces[0] + jeeStatsSuffix, "get"
                            + pieces[1]);
                    String xml = xdoc.transformSingleStatistic(StatisticXMLTag, pieces[0], s)
                            .toString();
                    response.setContentType("application/xml; charset=utf-8");
                    Writer out = new OutputStreamWriter(response
                            .getOutputStream(), "UTF-8");
                    out.write(xml);
                    out.flush();
                    out.close();
                }
                catch (ScxException e)
                {
                    throw new ServletException(e);
                }
            }
            else
            {
                throw new ServletException();
            }
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
            throws ServletException, IOException {

        throw new ServletException("Post method not supported");
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {

    }

    /**
     * <p>
     * Internal helper method to retrieve all statistics for a specific type.
     * </p>
     * 
     * <p>
     * This method is called to retrieve the statistic information from a 
     * JeeStatistics class.  
     * post.
     * </p>
     * 
     * @param StatisticsClassname
     *            the name of the statistic type to retrieve statistics for
     *            
     * @return a List of statistical information for the specified statistic
     * 
     * @throws ServletException
     *             if an error occurred
     */
    protected StatisticGroup getStatisticsforClass(String StatisticsClassname)
        throws ServletException  
    {
        Object ClassInstance = null;
        
        try {
            Class<?> StatsClass = Class.forName(StatisticsClassname);
            ClassInstance = StatsClass.newInstance();
        }
        catch(Exception e) 
        {
            throw new ServletException(e);
        }
        
        if(ClassInstance instanceof IStatistics)
        {
            return ((IStatistics)ClassInstance).getStats();
        }
        
        throw new ServletException("returned object is not a statistic");
    }
    
    /**
     * <p>
     * Internal helper method to retrieve a single statistics 
     * for a specific type .
     * </p>
     * 
     * <p>
     * This method is called to retrieve a single statistic metric from a 
     * JeeStatistics class.  
     * post.
     * </p>
     * 
     * @param StatisticsClassname
     *            the name of the statistic type to retrieve statistics for
     *            
     * @param MethodName
     *            the specific statistical value to retrieve
     *            
     * @return Information for the specified statistic
     * 
     * @throws ServletException
     *             if an error occurred
     */
    protected Statistic getSingleStatisticforClass(String StatisticsClassname,String MethodName)
        throws ServletException  
    {
        IStatistics StatsClassInstance = null;
        Class<?> StatsClass = null;
        Object ClassInstance = null;
        Method ClassMethod = null;
        Object returnedObject = null;

        try
        {
            StatsClass = Class.forName(StatisticsClassname);
            ClassInstance = StatsClass.newInstance();
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }

        if (!(ClassInstance instanceof IStatistics))
        {
            throw new ServletException(
                    "Current class instance is not a statistic");
        }

        StatsClassInstance = (IStatistics) ClassInstance;

        try
        {
            ClassMethod = StatsClass.getDeclaredMethod(MethodName,
                    (Class<?>[]) null);
            returnedObject = ClassMethod.invoke(StatsClassInstance,
                    (Object[]) null);
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }

        if (returnedObject instanceof Statistic)
        {
            return (Statistic) returnedObject;
        }

        throw new ServletException("returned object is not a statistic");
    }
    
}
