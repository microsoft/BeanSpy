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

package com.interopbridges.scx.jmx;


import javax.management.MBeanServer;

import com.interopbridges.scx.mbeanserver.MockMBeanServer;

/**
 * <p>
 * Extension of the  Java JDK JMX store for testing
 * the switching of the default class loader.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class MockJdkJMXAbstraction extends JdkJMXAbstraction
{
    public MockJdkJMXAbstraction(MBeanServer mbeanServer) 
    {
        super(mbeanServer);
    }
    
    /**
     * <p>
     * Sets the current thread classloader to a new classloader.
     * </p>
     * 
     * @param newClassLoader
     *            The classloader to become the current thread classloader.
     *
     */
    protected void setThreadClassLoader(ClassLoader newClassLoader)
    {
        if(this._server instanceof MockMBeanServer)
        {
            ((MockMBeanServer)this._server).setExceptionFlag(false);
        }
    }

}
