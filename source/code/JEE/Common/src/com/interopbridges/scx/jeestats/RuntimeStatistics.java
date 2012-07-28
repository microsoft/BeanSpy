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
 * Retrieves the Runtime statistic values from the ManagementFactory.
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
public class RuntimeStatistics extends GenericStatistics
{
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname = "Runtime";
    
    /**
     * <p>
     * Name identifying the statistical value containing the 
     * time the JVM was started
     * </p>
     */
    private static final String StartTime = "StartTime";
    
    /**
     * <p>
     * Name identifying the statistical value containing the 
     * amount of time the JVM has been running
     * </p>
     */
    private static final String UpTime = "UpTime";
    
    /**
     * <p>
     * Default constructor
     * </p>
     */
    public RuntimeStatistics()
    {
       super(StatisticGroupname);    
    }
    
    /**
     * <p>
     * Retrieves the time the JVM was started in milliseconds 
     * from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
      * @return Statistic containing the time the JVM was started
      * returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.RuntimeMXBean
     */    
    @StatisticMethod
    public Statistic getStartTime()
    {
        return new Statistic(StartTime, 
                long.class, ManagementFactory.getRuntimeMXBean().getStartTime());
    }
    
    /**
     * <p>
     * Retrieves the time the JVM has been running 
     * from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
      * @return Statistic containing the time the JVM has been running
      * returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.RuntimeMXBean
     */    
    @StatisticMethod
    public Statistic getUpTime()
    {
        return new Statistic(UpTime,    
                long.class,    ManagementFactory.getRuntimeMXBean().getUptime());
    }
}
