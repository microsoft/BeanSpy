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

package com.ibm.websphere.management;

import javax.management.MBeanServer;

import com.interopbridges.scx.mbeanserver.MockMBeanServer;

/**
 * Class to emulate the WebSphere MBean Factory.
 * The factory will return an instance of a MBeanServer.
 * @author Geoff Erasmus
 */
public class DummyMBeanFactory {

    /**
     * <p>
     * WebSphere Emulation method to retrieve a MBeanServer
     * </p>
     */
    public static MBeanServer getMBeanServer()
    {
        return MockMBeanServer.getInstance();
    }
    
}
