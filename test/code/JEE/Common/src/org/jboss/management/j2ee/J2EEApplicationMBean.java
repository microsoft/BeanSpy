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

package org.jboss.management.j2ee;

/**
 * <p>
 * Interface for describing the fake J2EEApplication needed for unit testing.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public interface J2EEApplicationMBean
{
    /**
     * <p>
     * Return a property that signals if this (fake) JBoss Web Application has
     * an event provider
     * </p>
     * 
     * @return Some boolean value
     */
    public abstract boolean getEventProvider();

    /**
     * <p>
     * Return a property that signals if this (fake) JBoss Web Application has a
     * statistics provider
     * </p>
     * 
     * @return Some boolean value
     */
    public abstract boolean getStatisticsProvider();

    /**
     * <p>
     * Return a property that is of a boolean type
     * </p>
     * 
     * @return ObjectName of the MBean
     */
    public abstract String getObjectName();

}
