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

/**
 * Class to emulate the WebSphere Admin Factory while the 
 * getMBeanFactory function is missing from the class.
 * 
 * @author Jinlong Li
 */
public class MockAdminServiceFactoryNoMethod 
{
    /**
     * <p>
     * Emulation method to retrieve simulated WebSphere MBeanFactory but the name 
     * of the function has been named differently by purpose.
     * </p>
     */
     public static Object MissingGetMBeanFactory()
     {
        return new DummyMBeanFactory();
     }
}