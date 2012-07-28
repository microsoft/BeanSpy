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

import com.interopbridges.scx.mbeans.ComplexClass;
import com.interopbridges.scx.mbeans.ComplexTypeMBean;

/**
 * <p>
 * This class is modified from class ComplexType.
 * </p>
 *
 * <p>
 * Simple MBean that contains a complex data type. The size of the MBean can 
 * be managed so it can be used for the test case for the size of the XML files.
 * </p>
 * 
 * <p>
 * This class is used in the unit tests for verifying when the size of XML file
 * exceeds certain size, an exception should be thrown.
 * </p>
 * 
 * @author Jinlong Li
 * 
 */
public class ComplexTypeForMaxFileSize implements ComplexTypeMBean {

   
    private ComplexClass _ComplexClass;
    private ComplexClass[] _ComplexClassArray;

    private final String _theLabel = "ComplexTypeForMaxFileSize";

   /*
    * Creating a mbean with a large array in order to generate a XML file 
    * that will be larger than 4 MB.
    */
    private int _numOfArray = 65000;
    
    public void setNumOfArray(int num)
    {
        this._numOfArray = num;
    }
    
    public int getNumOfArray()
    {
        return this._numOfArray;
    }
    
    
    public ComplexTypeForMaxFileSize() 
    {
        this._ComplexClass = new ComplexClass();
        
        this._ComplexClassArray = new ComplexClass[_numOfArray];       
        
        for(int i = 0; i < _numOfArray; i++)
        {
           this._ComplexClassArray[i] = new ComplexClass();
        }
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
