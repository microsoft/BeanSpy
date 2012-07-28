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
 * Retrieves the JVM memory statistic values from the JVM ManagementFactory.
 * These values do not represent individual applications (EJB's, Servlets etc)
 * but rather the memory utilization of the entire JVM which houses
 * the application server.
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
public class MemoryStatistics extends GenericStatistics
{
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname = "Memory";
    
    /**
     * <p>
     * Name identifying the statistical value containing the amount of
     * heap memory initially allocated by the JVM
     * </p>
     */
    private static final String HeapInitialMemoryAllocated = "HeapInitialMemoryAllocated";
    
    /**
     * <p>
     * Name identifying the statistical value containing the amount of
     * heap memory currently used by the JVM
     * </p>
     */
    private static final String HeapUsedMemory = "HeapUsedMemory";
    
    /**
     * <p>
     * Name identifying the statistical value containing the amount of
     * heap memory that is currently committed, this is essentially the 
     * free memory (non contiguous) that can be allocated by the JVM
     * </p>
     */
    private static final String HeapCommittedMemory = "HeapCommittedMemory";
    
    /**
     * <p>
     * Name identifying the statistical value containing the total 
     * amount of heap memory that can be utilized by the JVM
     * </p>
     */
    private static final String HeapMaximumMemory = "HeapMaximumMemory";
    
    /**
     * <p>
     * Name identifying the statistical value containing the amount of
     * non-heap memory initially allocated by the JVM
     * </p>
     */
    private static final String NonHeapInitialMemoryAllocated = "NonHeapInitialMemoryAllocated";
    
    /**
     * <p>
     * Name identifying the statistical value containing the amount of
     * non-heap memory currently used by the JVM
     * </p>
     */
    private static final String NonHeapUsedMemory = "NonHeapUsedMemory";
    
    /**
     * <p>
     * Name identifying the statistical value containing the amount of
     * non-heap memory that is currently committed, this is essentially the 
     * free memory (non contiguous) that can be allocated by the JVM
     * </p>
     */
    private static final String NonHeapCommittedMemory = "NonHeapCommittedMemory";
    
    /**
     * <p>
     * Name identifying the statistical value containing the total 
     * amount of non-heap memory that can be utilized by the JVM
     * </p>
     */
    private static final String NonHeapMaximumMemory = "NonHeapMaximumMemory";
    
    /**
     * <p>
     * Name identifying the statistical value containing the  
     * number of objects waiting to be garbage collected by the JVM
     * </p>
     */
    private static final String PendingFinalizationCount = "PendingFinalizationCount";
    
    /**
     * <p>
     * Name identifying the statistical value containing the  
     * percentage of heap memory used
     * </p>
     */
    private static final String PercentHeapUsed = "PercentHeapMemoryUsed";
    
    /**
     * <p>
     * Default constructor
     * </p>
     */
    public MemoryStatistics()
    {
       super(StatisticGroupname);    
    }
    
    /**
     * <p>
     * Retrieves the amount of heap memory (bytes) initially allocated
     * by the JVM
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * amount of heap memory initially allocated by the JVM
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.MemoryUsage
     */    
    @StatisticMethod
    public Statistic getHeapInitialMemoryAllocated()
    {
        return new Statistic(HeapInitialMemoryAllocated, 
                long.class, ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getInit());
    }
    
    /**
     * <p>
     * Retrieves the amount of heap memory (bytes) used by the JVM
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * amount of heap memory allocated by the JVM
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.MemoryUsage
     */    
    @StatisticMethod
    public Statistic getHeapUsedMemory()
    {
        return new Statistic(HeapUsedMemory,              
                long.class, ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed());
    }
    
    /**
     * <p>
     * Retrieves the amount of free heap memory (bytes) available to the JVM
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * amount of free heap memory available to the JVM
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.MemoryUsage
     */    
    @StatisticMethod
    public Statistic getHeapCommittedMemory()
    {
        return new Statistic(HeapCommittedMemory,         
                long.class, ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getCommitted());
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
        return new Statistic(HeapMaximumMemory,              
                long.class, ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax());
    }
    
    /**
     * <p>
     * Retrieves the amount of non-heap memory (bytes) initially allocated
     * by the JVM
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * amount of non-heap memory initially allocated by the JVM
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.MemoryUsage
     */
    @StatisticMethod
    public Statistic getNonHeapInitialMemoryAllocated()
    {
        return new Statistic(NonHeapInitialMemoryAllocated, 
                long.class, ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getInit());
    }
    
    /**
     * <p>
     * Retrieves the amount of non-heap memory (bytes) used by the JVM
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * amount of non-heap memory allocated by the JVM
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.MemoryUsage
     */    
    @StatisticMethod
    public Statistic getNonHeapUsedMemory()
    {
        return new Statistic(NonHeapUsedMemory,              
                long.class, ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed());
    }
    
    /**
     * <p>
     * Retrieves the amount of free non-heap memory (bytes) available to the JVM
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * amount of free non-heap memory available to the JVM
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.MemoryUsage
     */    
    @StatisticMethod
    public Statistic getNonHeapCommittedMemory()
    {
        return new Statistic(NonHeapCommittedMemory,         
                long.class, ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getCommitted());
    }
    
    /**
     * <p>
     * Retrieves the amount of non-heap memory initially requested from the 
     * operating system, this is the maximum amount of memory managed by the JVM 
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * maximum amount of non-heap memory that can be utilized by the JVM
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.MemoryUsage
     */    
    @StatisticMethod
    public Statistic getNonHeapMaximumMemory()
    {
        return new Statistic(NonHeapMaximumMemory,              
                long.class, ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getMax());
    }
    
    /**
     * <p>
     * Retrieves the number of objects pending finalization, this is the 
     * essentially the number of objects waiting to be garbage collected 
     * by the JVM 
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
      * @return Statistic containing the number of objects 
      * pending finalization
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.MemoryUsage
     */    
    @StatisticMethod
    public Statistic getPendingFinalizationCount()
    {
        return new Statistic(PendingFinalizationCount,          
                int.class, ManagementFactory.getMemoryMXBean().getObjectPendingFinalizationCount());
    }
    
    /**
     * <p>
     * Retrieves the percentage of Heap memory used 
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
      * @return Statistic containing the number of objects 
      * pending finalization
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.MemoryUsage
     */    
    @StatisticMethod
    public Statistic getPercentMemoryUsed()
    {
        int value=0;
        
        value = (int)((ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed()* 100) /
                 ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax());
        return new Statistic(PercentHeapUsed, int.class, value);
    }
    
}

                                                                        
