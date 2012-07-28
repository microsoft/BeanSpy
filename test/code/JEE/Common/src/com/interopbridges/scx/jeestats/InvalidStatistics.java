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
 * Invalid statistic class with a method that does not return a Statistic
 * </p>
 * 
 * 
 * @author Geoff Erasmus
 * 
 */
public class InvalidStatistics  extends GenericStatistics
 {
    /**
     * <p>
     * Default constructor
     * </p>
     */
    public InvalidStatistics() 
    {
       super("Invalid");    
    }
    
	/**
	 * <p>
	 * Retrieves a canned statistic
	 * </p>
	 * 
 	 * @return INVALID dummy string value 
	 */	
	@StatisticMethod
	public String getInvalidStat1()
	{
		return 	"Hello";
	}
}
