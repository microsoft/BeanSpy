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

package com.interopbridges.scx.jeestats;

import java.lang.management.ManagementFactory;


/**
 * <p>
 * Retrieves the Thread statistic values from the ManagementFactory.
 * The management factory  platform MXBean(s) represents the management 
 * interface of a component of the Java virtual machine. The platform MXBeans 
 * were introduces in J2SE 5 and therefore none of the statistic modules
 * will work on Java versions prior to 1.5. 
 * </p>
 * 
 * <p>
 * The return values are read only and cannot be modified.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class ThreadStatistics  extends GenericStatistics
{
     
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname = "Thread";
    
    /**
     * <p>
     * Name identifying the statistical value containing the 
     * highest number of running threads
     * </p>
     */
    private static final String PeakThreadCount = "PeakThreadCount";
    
    /**
     * <p>
     * Name identifying the statistical value containing the 
     * current number of running threads
     * </p>
     */
    private static final String ThreadCount = "ThreadCount";
    
    /**
     * <p>
     * Name identifying the statistical value containing the 
     * total number of threads created by the JVM
     * </p>
     */
    private static final String TotalStartedThreadCount = "TotalStartedThreadCount";

    
    /**
     * <p>
     * Default constructor
     * </p>
     */
    public ThreadStatistics()
    {
       super(StatisticGroupname);    
    }
    
    /**
     * <p>
     * Retrieves the peak thread count from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
      * @return Statistic containing the highest number of concurrent 
      * threads running in the JVM returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ThreadMXBean
     */    
    @StatisticMethod
    public Statistic getPeakThreadCount()
    {
        return new Statistic(PeakThreadCount,
                int.class,ManagementFactory.getThreadMXBean().getPeakThreadCount());
    }
    
    /**
     * <p>
     * Retrieves the current running thread count from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
      * @return Statistic containing the number of current threads running
      * in the JVM returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ThreadMXBean
     */    
    @StatisticMethod
    public Statistic getThreadCount()
    {
        return new Statistic(ThreadCount,
                int.class,ManagementFactory.getThreadMXBean().getThreadCount());
    }
    
    /**
     * <p>
     * Retrieves the total number of threads created by the JVM
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
      * @return Statistic containing the total number of threads 
      * created since the JVM was started returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ThreadMXBean
     */    
    @StatisticMethod
    public Statistic getTotalStartedThreadCount()
    {
        return new Statistic(TotalStartedThreadCount,
                long.class,ManagementFactory.getThreadMXBean().getTotalStartedThreadCount());
    }
    
}
