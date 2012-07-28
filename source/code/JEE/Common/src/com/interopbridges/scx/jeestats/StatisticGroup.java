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
 * Generic Statistic group class to encapsulate a group of item statistics.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class StatisticGroup {
    /**
     * <p>
     * Internal container to hold Item groups of statistics
     * </p>
     */
     List<StatisticItemGroup> _statsgroup;
     
     /**
      * <p>
      * The name for the group
      * </p>
      */
      String _Name;
      
    /**
     * <p>
     * Generic Constructor
     * </p>
     * @param Name
     *            The name for the group of statistic items
     */    
    public StatisticGroup(String Name)
    {
        _statsgroup = new ArrayList<StatisticItemGroup>();
        _Name = Name;
    }
    
    /**
     * <p>
     * Get the name of the group.
     * </p>
     * @return
     *         The name of the group.
     */    
    public String getName()
    {
        return _Name;
    }
    
    /**
     * <p>
     * Add a statistic item group.
     * </p>
     * @param stat
     *       A statistic item group to add to the group
     */     
    public void addStatisticItemGroup(StatisticItemGroup stat)
    {
        _statsgroup.add(stat);
    }
    
    /**
     * <p>
     * Get the statistics item group from this group.
     * </p>
     * @return
     *      The statistics item group of this group
     */    
    public List<StatisticItemGroup> getStatisticItemGroup()
    {
        return _statsgroup;
    }
}
