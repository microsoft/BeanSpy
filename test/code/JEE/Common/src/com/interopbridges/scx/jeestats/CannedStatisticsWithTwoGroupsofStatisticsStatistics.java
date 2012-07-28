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

/**
 * <p>
 * Dummy statistics class to facilitate canned (Fixed) tests.
 * This test creates two sets of statistical data.
 * </p>
 * 
 * 
 * @author Geoff Erasmus
 * 
 */
public class CannedStatisticsWithTwoGroupsofStatisticsStatistics extends GenericStatistics 
{
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname = "TwoGroups";

    /**
     * <p>
     * Name identifying the statistical value containing a count 
     * </p>
     */
    private static final String Count = "Count";
    
    /**
     * <p>
     * Name identifying the group
     * </p>
     */
    private static final String Name = "Name";
    

    /**
     * <p>
     * Default constructor
     * </p>
     */
    public CannedStatisticsWithTwoGroupsofStatisticsStatistics() 
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
        
        StatisticItemGroup _statsitemgrp = new StatisticItemGroup();
        
        _statsitemgrp.addStatistic(new Statistic(Count, long.class, 100));
        _statsitemgrp.addStatistic(new Statistic(Name, String.class, "Name1"));
        _statsgrp.addStatisticItemGroup(_statsitemgrp);
        
        _statsitemgrp = new StatisticItemGroup();
        _statsitemgrp.addStatistic(new Statistic(Count, long.class, 200));
        _statsitemgrp.addStatistic(new Statistic(Name, String.class, "Name two"));
        _statsgrp.addStatisticItemGroup(_statsitemgrp);
        
        return _statsgrp;
    }
}
