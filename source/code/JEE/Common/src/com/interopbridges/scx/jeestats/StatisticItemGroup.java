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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Generic Statistic Item group class to encapsulate a group of statistics.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class StatisticItemGroup {
    /**
     * <p>
     * Internal container to hold statistics for the group
     * </p>
     */
     List<Statistic> _stats;
     
    /**
     * <p>
     * Generic Constructor
     * </p>
     */    
    public StatisticItemGroup()
    {
        _stats = new ArrayList<Statistic>();
    }
    
    /**
     * <p>
     * Add a statistic to this item group.
     * </p>
     * @param stat
     *            A statistic to add to the item group
     */    
    public void addStatistic(Statistic stat)
    {
        _stats.add(stat);
    }
    
    /**
     * <p>
     * Get the statistics of this item group.
     * </p>
     * @return
     *          The statistics for this item group
     */    
    public List<Statistic> getStatistics()
    {
        return _stats;
    }
}
