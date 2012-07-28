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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.jmx.MockJmx;
import com.interopbridges.scx.webservices.FauxMBeanGenerator;

/**
 * <p>
 * Test class for the MBeans Getter functionality. For this class, once caveat
 * is that we are going to more-or-less be testing with a mock implementation of
 * the JMX Store. So this class will NOT be testing the JMX Store, it is assumed
 * that this works (and that the abstraction around this is relatively thin).
 * </p>
 * 
 * <p>
 * What this class will be testing is that the XML generated from a few
 * well-known MBeans is indeed correct.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class MBeanGetterTest {

    /**
     * <p>
     * Creator of fake MBeans.
     * </p>
     */
    private FauxMBeanGenerator _mbeanCreator;

    /**
     * /**
     * <p>
     * System under test (should be reset per test).
     * </p>
     */
    private MBeanGetter _mbeanGetter;

    /**
     * <p>
     * Method invoked before each unit-test in this class.
     * </p>
     * 
     * @throws Exception
     *             If there was a problem with test setup (due adding the MBeans
     *             to the fake JMX store)
     */
    @Before
    public void Setup() throws Exception {
        List<IJMX>fauxJmxStores = new ArrayList<IJMX>();

        MockJmx fauxJmxStore = new MockJmx();
        _mbeanCreator = new FauxMBeanGenerator(fauxJmxStore);
        _mbeanCreator.run();
        
        fauxJmxStores.add(fauxJmxStore);
        
        _mbeanGetter = new MBeanGetter(fauxJmxStores);
    }

    /**
     * <p>
     * Method invoked after each unit-test in this class.
     * </p>
     */
    @After
    public void TearDown() {
        this._mbeanCreator = null;
        this._mbeanGetter = null;
    }

    /**
     * <p>
     * Retrieve the fake MBeans from the JMX Store (note this is a fake store)
     * and dynamically transform this MBeans into XML.
     * </p> 
     * 
     * <p>
     * This test is meant to verify that the class developed for the unit-test behaves
     * as expected.
     * <p>
     * 
     * @throws Exception
     *             If the generic operation on the MBean store failed
     */
    @Test
    public void getFakeOperationMBeansAndTransformToXml() throws Exception {
        ObjectName objectName = new ObjectName("*:*");
        QueryExp query = null;
        List<ObjectName> results = _mbeanGetter.getAllMatchingMBeans(objectName,
                query);

        Assert
                .assertEquals(
                        "Query for matching operationCall MBeans returned the wrong number of results",
                        15, results.size());
        // Right now the primary reason for this method is to verify no
        // exceptions are thrown. Fake/mock implementation of the JMX Store
        // returns all contents
    }

    /**
     * <p>
     * Verify that a Null Pointer Exception (NPE) is thrown when give null
     * inputs.
     * </p>
     * 
     * @throws Exception
     *             If the test misbehaved and threw some other type of
     *             (unexpected) exception.
     */
    @Test
    public void getFakeOperationMBeansAndTransformToXml_FailureTest_NPE()
            throws Exception {
        /*
         * For this test, it is the first parameter which we are interested in
         * as this should generator the NPE. True the second argument is null,
         * but the implementation (as of the date this test was written) does
         * not care.
         */
        try {
            _mbeanGetter.getMBeansAsXml(null,null);
            Assert.fail("A Null Pointer Exception should have been thrown.");
        } catch (ScxException e) {
            Assert
                    .assertTrue(
                            "Received exception of the correct type, but the wrong exception code.",
                            new ScxException(ScxExceptionCode.NULL_POINTER_EXCEPTION).getMessage().equals(e
                                    .getMessage()));
        }
    }

    /**
     * <p>
     * Verify that a simple JMX query containing neither Quotes not wildcards 
     * passes the ObjectName validation and that two valid MBean is returned.
     * </p>
     * 
     * @throws Exception
     *             If the test misbehaved and threw some other type of
     *             (unexpected) exception.
     */
    @Test
    public void getFakeOperationMBeans()
            throws Exception 
    {
        /*
         * For this test, we are particularly interested in the creation
         * of the ObjectName from the JMX query supplied to the getMBeans method call.
         */
        HashMap<IJMX, Set<ObjectInstance>> hmap = _mbeanGetter.getMBeans("com.interopbridges.scx:jmxType=operationCall"); 
        Assert.assertEquals(
                "Query for matching operationCall MBeans returned the wrong number of JMX Store results",
                1, hmap.size());
        
        Set<ObjectInstance> oi = hmap.get(hmap.keySet().iterator().next());
        Assert.assertEquals(
                "Query for matching operationCall MBeans returned the wrong number of results",
                2, oi.size());
    }    

    /**
     * <p>
     * Verify that a Malformed Object Name Exception is thrown when using 
     * a JMX query with embedded quotes and wildcard inputs.
     * </p>
     * 
     * @throws Exception
     *             If the test misbehaved and threw some other type of
     *             (unexpected) exception.
     */
    @Test
    public void getMBeans_BadQuery_QuotesAndWildcard()
            throws Exception 
    {
        try 
        {
            HashMap<IJMX, Set<ObjectInstance>> h = _mbeanGetter.getMBeans("com.interopbridges.scx:jmxType=operationCall,Name=Test\"MBean's\",*"); 
            Assert.fail("A MALFORMED_OBJECT_NAME Exception should have been thrown.");
        } 
        catch (ScxException e) 
        {
            Assert.assertTrue(
                "Received exception of the correct type, but the wrong exception code.",
                new ScxException(ScxExceptionCode.MALFORMED_OBJECT_NAME).getMessage().equals(e
                        .getMessage()));
        }
    }    

    /**
     * <p>
     * Verify that a JMX query containing wildcards passes the 
     * ObjectName validation and that two valid MBean is returned.
     * </p>
     * 
     * @throws Exception
     *             If the test misbehaved and threw some other type of
     *             (unexpected) exception.
     */
    @Test
    public void getMBeans_Wildcard()
            throws Exception 
    {
        HashMap<IJMX, Set<ObjectInstance>> hmap = _mbeanGetter.getMBeans("com.interopbridges.scx:jmxType=operationCall,Name=*"); 
        Assert.assertEquals(
                "Query for matching operationCall MBeans returned the wrong number of JMX Store results",
                1, hmap.size());
        
        Set<ObjectInstance> oi = hmap.get(hmap.keySet().iterator().next());
        Assert.assertEquals(
                "Query for matching operationCall MBeans returned the wrong number of results",
                2, oi.size());
    }    

    /**
     * <p>
     * Verify that a JMX query containing embedded quotes passes the 
     * ObjectName validation and that two valid MBean is returned.
     * </p>
     * 
     * @throws Exception
     *             If the test misbehaved and threw some other type of
     *             (unexpected) exception.
     */
    @Test
    public void getMBeans_Quotes()
            throws Exception 
    {
        HashMap<IJMX, Set<ObjectInstance>> hmap = _mbeanGetter.getMBeans("com.interopbridges.scx:jmxType=operationCall,Name=Test\"MBean's\""); 
        Assert.assertEquals(
                "Query for matching operationCall MBeans returned the wrong number of JMX Store results",
                1, hmap.size());
        
        Set<ObjectInstance> oi = hmap.get(hmap.keySet().iterator().next());
        Assert.assertEquals(
                "Query for matching operationCall MBeans returned the wrong number of results",
                2, oi.size());
    }    
    
}
