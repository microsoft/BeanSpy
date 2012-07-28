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
 * Retrieves the Java JVM Operating System values from the ManagementFactory.
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
public class JeeJVMOSInfo extends GenericStatistics
{
    
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname         = "OperatingSystem";
    
    /**
     * <p>
     * Name identifying the statistical value containing the Operation System name
     * </p>
     */
    private static final String Name         = "Name";
    
    /**
     * <p>
     * Name identifying the statistical value containing the Operation System Version
     * </p>
     */
    private static final String Version      = "Version";
    
    /**
     * <p>
     * Name identifying the statistical value containing the Operation System Architecture
     * </p>
     */
    private static final String Architecture = "Architecture";
    
    /**
     * <p>
     * Default constructor
     * </p>
     */
    public JeeJVMOSInfo()
    {
       super(StatisticGroupname);    
    }
    
    /**
     * <p>
     * Retrieves the OS name from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getName()
    {
        return     new Statistic(Name,    String.class,
        ManagementFactory.getOperatingSystemMXBean().getName());
    }
    
    /**
     * <p>
     * Retrieves the OS Version from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * total loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getVersion()
    {
        return new Statistic(Version,    String.class,    
                ManagementFactory.getOperatingSystemMXBean().getVersion());
    }
    
    /**
     * <p>
     * Retrieves the OS Architecture type from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * total number of unloaded class returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getArchitecture()
    {
        return new Statistic(Architecture,String.class,    
                ManagementFactory.getOperatingSystemMXBean().getArch());
    }
    
}
