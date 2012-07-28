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
 * The class is intended to be used by a test MBean.
 * The class has no functionality and it
 * simply returns some values.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */public class SimpleClass
{
    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Dummy string attribute.
     * </p>
     */
    private String someAttribute = "UserClass Attribute";
    
    /**
     * <p>
     * Simple method to return a class attribute.
     * </p>
     * 
     * @return predetermined string value
     */
    public String getAttrib()
    {
        return someAttribute;
    }
    
    /**
     * <p>
     * Simple method to return a self reference.
     * </p>
     * 
     * @return self reference
     */
    public SimpleClass getThis()
    {
        return this;
    }
}