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
 * Simple MBean that contains a complex data type. The primary intention of this
 * class is a simple class that contains a complex data type. This class is used
 * in the unit tests for verifying that the MBeans can be represented as XML.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class ComplexType implements ComplexTypeMBean {

    /**
     * A Complex class property needed for XML conversion testing
     */
    private ComplexClass _ComplexClass;

    /**
     * An array of Complex class propertys needed for XML conversion testing
     */
    private ComplexClass[] _ComplexClassArray;

    /**
     * A primitive String property needed for XML conversion testing
     */
    private final String _theLabel = "ComplexType";

    /**
     * Constructor. It creates an instance of a class that will be returned
     * as a data type needed for XML conversion testing
     */
    public ComplexType() 
    {
        this._ComplexClass = new ComplexClass();
        
        this._ComplexClassArray = new ComplexClass[2];
        this._ComplexClassArray[0] = new ComplexClass();
        this._ComplexClassArray[1] = new ComplexClass();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.mbeans.ComplexTypeMBean#getComplexClass()
     */
    public ComplexClass getComplexClass() 
    {
        return this._ComplexClass;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.mbeans.ComplexTypeMBean#getComplexClassArray()
     */
    public ComplexClass[] getComplexClassArray() 
    {
        return this._ComplexClassArray;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.mbeans.ComplexTypeMBea#getTheLabel()
     */
    public String getTheLabel() 
    {
        return this._theLabel;
    }

}
