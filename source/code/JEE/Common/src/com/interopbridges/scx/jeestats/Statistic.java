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
 * Generic Statistic class to encapsulate a single statistic.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class Statistic {
     /**
      * <p>
      * Internal name for the Statistic
      * </p>
      */
      String _Name;
      
    /**
     * <p>
     * Internal type for the Statistic
     * The type can be any object that implements the toString method
     * </p>
     */
    Object _Type;
    
    
    /**
     * <p>
     * Internal Value of the Statistic
     * The Value can be any object that implements the toString method
     * </p>
     */
    Object _Value;

    
    /**
     * <p>
     * Constructor
     * </p>
     * @param Name
     *            Name of the statistic
     * 
     * @param Type
     *            The data Type of the statistic
     *            
     * @param Value
     *            The data Value of the statistic
     * 
     */    
    public Statistic(String Name, Object Type, Object Value)
    {
        this._Name = Name;
        this._Type = Type;
        this._Value = Value;
    }
    
    /**
     * <p>
     * getter method for retrieving the Statistic name.
     * </p>
     * 
     * @return String
     */    
    public String getStatisticName()
    {
        return _Name;
    }
    
    /**
     * <p>
     * getter method for retrieving the Statistic data type.
     * </p>
     * 
     * @return Object
     */    
    public Object getStatisticType()
    {
        return _Type;
    }
    
    /**
     * <p>
     * getter method for retrieving the Statistic value.
     * </p>
     * 
     * @return Object
     */    
    public Object getStatisticValue()
    {
        return _Value;
    }
    
}
