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


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing the MBeanMethodParameter data class.<br>
 * 
 * @author Geoff Erasmus
 * 
 */
public class MBeanMethodParameterTest {
    /**
     * <p>
     * Test Setup/preparation method that resets/initializes all test specific
     * variables.
     * </p>
     */
    @Before
    public void setup() {
    }

    /**
     * <p>
     * Method invoked after each unit-test in this class.
     * </p>
     */
    @After
    public void TearDown() {
    }

    /**
     * <p>
     * Verify the input stream is valid and contains the correct amount of data. <br>
     * </p>
     */
    @Test
    public void verifyTooLittleBodyData() throws Exception 
    {
        MBeanMethodParameter ma = new MBeanMethodParameter("Param1", "int", "5");
        Assert.assertEquals( "Name should be \"Param1\"", ma.getParamName() , "Param1");
        Assert.assertEquals( "Type should be \"int\"", ma.getParamType() , "int");
        Assert.assertEquals( "Name should be \"5\"", ma.getParamValue(), "5");
    }
    
}
