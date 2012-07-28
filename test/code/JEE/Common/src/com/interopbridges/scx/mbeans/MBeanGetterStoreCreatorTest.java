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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.jmx.MockJmx;
import com.interopbridges.scx.jmx.MockJmxThatAlwaysFails;
import com.interopbridges.scx.webservices.FauxMBeanGenerator;

/**
 * <p>
 * Test class for the creation of multiple MBean stores.
 * </p>
 *
 * @author Geoff Erasmus
 * 
 */
public class MBeanGetterStoreCreatorTest {

    /**
     * <p>
     * Creator of fake MBeans.
     * </p>
     */
    private FauxMBeanGenerator _mbeanCreator;
    private FauxMBeanGenerator _mbeanCreator2;

    /**
     * <p>
     * Method invoked before each unit-test in this class.
     * </p>
     */
    @Before
    public void Setup() {
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
     * Setup the multiple JMX stores
     * </p>
     * 
     * @throws Exception
     *             If the test misbehaved
     */
    @Test
    public void BuildMultipleMBeanStores() throws Exception {
        List<IJMX>fauxJmxStores = new ArrayList<IJMX>();

        MockJmx fauxJmxStore = new MockJmx();
        _mbeanCreator = new FauxMBeanGenerator(fauxJmxStore);
        _mbeanCreator.run();
        
        fauxJmxStores.add(fauxJmxStore);
        
        MockJmx fauxJmxStore2 = new MockJmx();
        _mbeanCreator2 = new FauxMBeanGenerator(fauxJmxStore2);
        _mbeanCreator2.run();
        
        fauxJmxStores.add(fauxJmxStore2);

        try
        {
            MockJmxThatAlwaysFails fauxJmxStore3 = new MockJmxThatAlwaysFails();
            _mbeanCreator = new FauxMBeanGenerator(fauxJmxStore3);
            _mbeanCreator.run();
            
            fauxJmxStores.add(fauxJmxStore3);
            Assert.fail("Failed to receive an exception on adding a MBean store that does not exist.");
        }
        catch (Exception e)        
        {
            Assert.assertTrue(
                "Exception should have been of type ScxException", e.getClass() == ScxException.class);
        }        
    }

}
