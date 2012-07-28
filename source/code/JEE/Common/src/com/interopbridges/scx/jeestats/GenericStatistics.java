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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;

/**
 * <p>
 * Generic concrete IJEEStatistics class that implements the getStats
 * method. 
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */

public class GenericStatistics implements IStatistics
 {
    /**
     * <p>
     * Name for the Statistic Group
     * </p>
     */
    protected String _name;
    
    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    protected ILogger _logger;
    
    /**
     * <p>
     * Default Constructor
     * </p>
     * @param name
     *            A name for this group of statistics
     */
    public GenericStatistics(String name) {
        this._logger = LoggingFactory.getLogger();
        _name = name;
    };
    
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
        
        Method[] m = this.getClass().getDeclaredMethods();
        for(int i=0;i<m.length;i++)
        {
            try{
                if( m[i].isAnnotationPresent( StatisticMethod.class ) )
                {
                    Object o = m[i].invoke(this,(Object[])null);
                    if(o instanceof Statistic) 
                        _statsitemgrp.addStatistic((Statistic)o);
                    else
                    {
                        this._logger.fine(
                                new StringBuffer("Invalid return type from : ").append(m[i].getName()).
                                append(" type must be a Statistics : ").append(this.getClass().getName()).toString());
                    }
                }
            }
            catch(InvocationTargetException e){ 
                // ignore the result from this method call
                // and continue processing other entries
                this._logger.fine(
                        new StringBuffer("Unable to invoke : ").append(m[i].getName()).
                        append(" on Statistics class : ").append(this.getClass().getName()).toString());
            }
            catch(IllegalAccessException e){
                // ignore the result from this method call
                // and continue processing other entries
                this._logger.fine(
                        new StringBuffer("Unable to access : ").append(m[i].getName()).
                        append(" on Statistics class : ").append(this.getClass().getName()).toString());
            }
        }
        _statsgrp.addStatisticItemGroup(_statsitemgrp);
        return _statsgrp;
    }

}
