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

package com.interopbridges.scx.mbeans;

/**
 * <p>
 * MBean that has the ObjectName property. This is meant for use in test purposes.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public interface EscapedObjectNameMBean
{

    /**
     * <p>
     * Return a property that is of a String type
     * </p>
     * 
     * @return Some String value
     */
    public abstract String  getObjectName();

    /**
     * <p>
     * Return a property that is of a String type
     * </p>
     * 
     * @return Some String value
     */
    public abstract String getTheLabel();

}