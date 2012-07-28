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
 * Simple Class that is used by ComplexClass. The primary intention of this
 * class is a simple class that contains a reference to the parent class. This class is used
 * in the unit tests for verifying that a cyclic class reference dependency can be used from
 * within MBeans and can be represented as XML.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */

public class ComplexRecursiveClass 
{
    /**
     * the serialVersionUID property needed for distinguishing this class.
     * It is returned in the Class.toString method to create a unique class instance.
     */
    private static final long serialVersionUID = 999999999999999998L;
    
    /**
     * A reference to the parent class to create a loop in the MBean recursion algorithm
     */
    private ComplexClass   parent;
    
    /**
     * A primitive private Integer property needed for XML conversion testing
     */
     private Integer   anInteger = new Integer(Integer.MAX_VALUE);

    /**
     * Constructor. The constructor receives a parent instance.
     *
     * @param parent
     *            A reference to the parent class 
     */
    public ComplexRecursiveClass(ComplexClass parent) 
    {
       this.parent = parent;   
    }

    /**
     * <p>
     * A getter method that returns a reference to the parent class
     * </p>
     * @return A ComplexClass referencing the parent class - for XML conversion testing
     */
    public ComplexClass getParent()
    {
        return parent;
    }
    
   /**
     * <p>
     * Get Property Method for an Integer type
     * </p>
     * 
     * @return An Integer wrapper class type for XML conversion testing
     */    
     public Integer getInteger()
    {
        return anInteger;
    }
    
}
