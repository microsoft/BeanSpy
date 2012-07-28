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
public class ClassLoaderStatistics extends GenericStatistics
{
    
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname         = "ClassLoader";
    
    /**
     * <p>
     * Name identifying the statistical value containing the loaded class count
     * </p>
     */
    private static final String LoadedClassCount         = "LoadedClassCount";
    
    /**
     * <p>
     * Name identifying the statistical value containing the total loaded class count.
     * This is the total number of classes loaded since the JVM started.
     * </p>
     */
    private static final String TotalLoadedClassCount     = "TotalLoadedClassCount";
    
    /**
     * <p>
     * Name identifying the statistical value containing the number 
     * of classes that have been unloaded since the JVM started.
     * </p>
     */
    private static final String UnloadedClassCount         = "UnloadedClassCount";
    
    /**
     * <p>
     * Default constructor
     * </p>
     */
    public ClassLoaderStatistics()
    {
       super(StatisticGroupname);    
    }
    
    /**
     * <p>
     * Retrieves the loaded class count from the Java management factory
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
    public Statistic getLoadedClassCount()
    {
        return     new Statistic(LoadedClassCount,    int.class,
        ManagementFactory.getClassLoadingMXBean().getLoadedClassCount());
    }
    
    /**
     * <p>
     * Retrieves the total number of classes loaded
     * since the JVM was started
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
    public Statistic getTotalLoadedClassCount()
    {
        return new Statistic(TotalLoadedClassCount,    long.class,    
                ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount());
    }
    
    /**
     * <p>
     * Retrieves the total number of classes that have been unloaded
     * since the JVM was started
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
    public Statistic getUnloadedClassCount()
    {
        return new Statistic(UnloadedClassCount,long.class,    
                ManagementFactory.getClassLoadingMXBean().getUnloadedClassCount());
    }
    
}
