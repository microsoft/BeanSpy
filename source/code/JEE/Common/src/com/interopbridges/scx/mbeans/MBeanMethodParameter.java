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
 * Utility class for holding Invoke method name and parameters. <br>
 * 
 * @author Geoff Erasmus
 */
public class MBeanMethodParameter 
{

    /**
     * <p>
     * Name of the parameter
     * </p>
     */
    private String paramName;
    
    /**
     * <p>
     * Name of the parameter type
     * </p>
     */
    private String paramType;
    
    /**
     * <p>
     * String representation of the parameter value
     * </p>
     */
    private String paramValue;
    
    /**
     * <p>
     * Constructor of the object.
     * </p>
     */
    public MBeanMethodParameter(String Name, String Type, String Value)
    {
        paramName = Name;
        paramType = Type;
        paramValue = Value;
    }
    
    /**
     * <p>
     * Getter function for the parameter name
     * </p>
     * 
     * @return the parameter name
     */
    public String getParamName()
    {
        return paramName;
    }
    
    /**
     * <p>
     * Getter function for the parameter type name
     * </p>
     * 
     * @return the parameter type name
     */
    public String getParamType()
    {
        return paramType;
    }
    
    /**
     * <p>
     * Getter function for the parameter value
     * </p>
     * 
     * @return the parameter value
     */
    public String getParamValue()
    {
        return paramValue;
    }
}
