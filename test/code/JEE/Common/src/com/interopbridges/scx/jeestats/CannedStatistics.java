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
 * </p>
 * 
 * 
 * @author Geoff Erasmus
 * 
 */
public class CannedStatistics extends GenericStatistics 
{
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname         = "Canned";
        
    /**
     * <p>
     * Name identifying the statistical value
     * </p>
     */
    private static final String CannedStat1         = "CannedStat1";
    /**
     * <p>
     * Name identifying the statistical value
     * </p>
     */
    private static final String CannedStat2         = "CannedStat2";
    /**
     * <p>
     * Name identifying the statistical value
     * </p>
     */
    private static final String CannedStat3         = "CannedStat3";
    /**
     * <p>
     * Name identifying the statistical value
     * </p>
     */
    private static final String CannedStat4         = "CannedStat4";
    /**
     * <p>
     * Name identifying the statistical value
     * </p>
     */
    private static final String CannedStat5         = "CannedStat5";
    
    
    /**
     * <p>
     * Default constructor
     * </p>
     */
    public CannedStatistics() 
    {
       super(StatisticGroupname);    
    }
    
    
    /**
     * <p>
     * Retrieves the canned statistic
     * </p>
     * 
      * @return Statistic containing a dummy value 
     */    
    @StatisticMethod
    public Statistic getCannedStat1()
    {
        return     new Statistic(CannedStat1,    int.class,
        new Integer(1));
    }
    
    /**
     * <p>
     * Retrieves the canned statistic
     * </p>
     * 
      * @return Statistic containing a dummy value 
     */    
    @StatisticMethod
    public Statistic getCannedStat2()
    {
        return     new Statistic(CannedStat2,    long.class,
        123L);
    }
    
    /**
     * <p>
     * Retrieves the canned statistic
     * </p>
     * 
      * @return Statistic containing a dummy value 
     */    
    @StatisticMethod
    public Statistic getCannedStat3()
    {
        return     new Statistic(CannedStat3,    String.class,
        "abc");
    }
    
    /**
     * <p>
     * Retrieves the canned statistic
     * </p>
     * 
      * @return Statistic containing a dummy value 
     */    
    @StatisticMethod
    public Statistic getCannedStat4()
    {
        return     new Statistic(CannedStat4,    boolean.class,
        true);
    }
    
    /**
     * <p>
     * Retrieves the canned statistic
     * </p>
     * 
      * @return Statistic containing a dummy value 
     */    
    @StatisticMethod
    public Statistic getCannedStat5()
    {
        return     new Statistic(CannedStat5,    int.class,
        100);
    }
}
