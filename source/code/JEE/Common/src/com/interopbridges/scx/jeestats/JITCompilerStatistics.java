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
 * Retrieves the Class Loader statistic values from the ManagementFactory.
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
public class JITCompilerStatistics extends GenericStatistics
{
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname = "JITCompiler";
    
    /**
     * <p>
     * Name identifying the method used to get the total 
     * time spent by the JIT compiler 
     * </p>
     */
    private static final String TotalCompilationTime = "TotalCompilationTime";
    
    /**
     * <p>
     * Default constructor
     * </p>
     */
    public JITCompilerStatistics()
    {
       super(StatisticGroupname);    
    }
    
    /**
     * <p>
     * Retrieves the cumulative time spent by the JIT compiler
     * since the JVM was started
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * accumulated JIT compilation time returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.CompilationMXBean
     */    
    @StatisticMethod
    public Statistic getTotalCompilationTime()
    {
        return new Statistic(TotalCompilationTime, 
                long.class, ManagementFactory.getCompilationMXBean().getTotalCompilationTime());
    }
}
