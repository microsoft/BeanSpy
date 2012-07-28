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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * <p>
 * Generic annotation used to identify a method as a JEEStatistic
 * The JEEStatistic annotation is used when enumerating a class via reflection, 
 * to find all methods are a JEEStatistic.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface StatisticMethod {
	boolean value() default true;
}
