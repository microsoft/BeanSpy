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

package com.interopbridges.scx.jeeinfo;

import java.lang.management.ManagementFactory;

import com.interopbridges.scx.jeestats.GenericStatistics;
import com.interopbridges.scx.jeestats.Statistic;
import com.interopbridges.scx.jeestats.StatisticMethod;


/**
 * <p>
 * Retrieves the Java JVM memory values from the ManagementFactory.
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
public class JeeJVMMemoryInfo extends GenericStatistics
{
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname = "JVMMemory";
    
    /**
     * <p>
     * Name identifying the statistical value containing the total 
     * amount of heap memory that can be utilized by the JVM
     * </p>
     */
    private static final String HeapMaximumMemory = "MaxHeapSize";
    
    
    /**
     * <p>
     * Default constructor
     * </p>
     */
    public JeeJVMMemoryInfo()
    {
       super(StatisticGroupname);    
    }
    
    /**
     * <p>
     * Retrieves the amount of heap memory initially requested from the 
     * operating system, this is the maximum amount of memory managed by the JVM 
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * maximum amount of heap memory that can be utilized by the JVM
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.MemoryUsage
     */    
    @StatisticMethod
    public Statistic getHeapMaximumMemory()
    {
        long mb = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax()/1024/1024;
        String val = String.valueOf(mb)+" MB";
        return new Statistic(HeapMaximumMemory,              
                String.class, val);
    }
    
}
