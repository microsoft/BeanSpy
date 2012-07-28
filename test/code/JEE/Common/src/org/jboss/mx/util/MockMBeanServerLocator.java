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

package org.jboss.mx.util;

import javax.management.MBeanServer;

import com.interopbridges.scx.mbeanserver.MockMBeanServer;

/**
 * Class to emulate the JBoss JMX store locator
 * 
 * @author Geoff Erasmus
 */
public class MockMBeanServerLocator 
{
    /**
     * <p>
     * Emulation method to retrieve the default MBeanServer from
     * a JBoss instance
     * </p>
     */
     public static MBeanServer locateJBoss()
     {
        return MockMBeanServer.getInstance();
     }
}