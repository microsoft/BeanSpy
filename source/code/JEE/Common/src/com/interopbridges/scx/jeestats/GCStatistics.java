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

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * <p>
 * Retrieves the garbage collector statistic values from the ManagementFactory.
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
public class GCStatistics extends GenericStatistics
{
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname = "GC";

    /**
     * <p>
     * Name identifying the statistical value containing the 
     * number of times the garbage collector has run since the JVM started
     * </p>
     */
    private static final String GCCollectionCount = "GCCollectionCount";
    
    /**
     * <p>
     * Name identifying the garbage collector
     * </p>
     */
    private static final String GCName = "GCName";
    
    /**
     * <p>
     * Name identifying the statistical value containing the 
     * accumulated time the garbage collector has run since the JVM started
     * </p>
     */
    private static final String GCCollectionTime = "GCCollectionTime";

    /**
     * <p>
     * Default constructor
     * </p>
     */
    public GCStatistics()
    {
       super(StatisticGroupname);    
    }

    /**
     * <p>
     * Retrieves all the statistics exposed in this group
     * </p>
     * 
      * @return a StatisticGroup containing statistics
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */
    public StatisticGroup getStats()
    {
        StatisticGroup _statsgrp = new StatisticGroup(_name);
        
        List<GarbageCollectorMXBean> tmb = ManagementFactory.getGarbageCollectorMXBeans();

        for(GarbageCollectorMXBean l: tmb)
        {
            long count;
            long time;
            String name;
            StatisticItemGroup _statsitemgrp = new StatisticItemGroup();
            
            count = l.getCollectionCount();
            time  = l.getCollectionTime();
            name  = l.getName();
            l.getMemoryPoolNames();
            _statsitemgrp.addStatistic(new Statistic(GCCollectionCount, long.class, count));
            _statsitemgrp.addStatistic(new Statistic(GCCollectionTime, long.class, time));
            _statsitemgrp.addStatistic(new Statistic(GCName, String.class, name));
            _statsgrp.addStatisticItemGroup(_statsitemgrp);
        }
        
        return _statsgrp;
    }
}
