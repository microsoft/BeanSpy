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

import java.util.List;

import com.interopbridges.scx.jmx.JmxStores;

/**
 * <p>
 * Provide 'statistics' about the JMX stores that BeanSpy connects to.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class JmxStoreStatistics extends GenericStatistics
{
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname = "JmxStores";

    /**
     * <p>
     * Name of a JMX Store Adapter Classes that have been successfully loaded.
     * </p>
     */
    private static final String JmxStoreNames      = "JmxStoreNames";

    /**
     * <p>
     * Default constructor
     * </p>
     */
    public JmxStoreStatistics()
    {
        super(StatisticGroupname);
    }

    /**
     * Retrieves a list of names for the JMX stores that BeanSpy has
     * successfully established a connection with.
     */
    public StatisticGroup getStats()
    {
        List<String> stores = JmxStores.getListOfJmxStoreAbstractionNames();
        StatisticGroup statisticsGroup = new StatisticGroup(_name);

        for (String store : stores)
        {
            StatisticItemGroup itemGroup = new StatisticItemGroup();
            itemGroup.addStatistic(new Statistic(JmxStoreNames, String.class, store));
            statisticsGroup.addStatisticItemGroup(itemGroup);
        }
        
        return statisticsGroup;
    }
}
