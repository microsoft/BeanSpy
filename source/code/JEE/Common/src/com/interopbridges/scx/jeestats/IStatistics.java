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
 * Generic Statistic interface to be implemented by all Statistic classes
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public interface IStatistics {
    
  /**
   * <p>
   * Get the values of all the statistics for this group of statistics.
   * </p>
   * 
   * @return List of statistics containing the values acquired.
   */
    public StatisticGroup getStats();
}
