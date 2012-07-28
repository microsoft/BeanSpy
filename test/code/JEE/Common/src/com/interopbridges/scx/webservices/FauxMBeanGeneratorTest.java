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

package com.interopbridges.scx.webservices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.jmx.MockJmx;
import com.interopbridges.scx.util.StringMangler;

/**
 * <p>
 * Unit Test for a Faux-class. As the fake class, will soon disappear, this
 * class will also disappear soon.
 * </p>>
 * 
 * @author Christopher Crammond
 * 
 */
public class FauxMBeanGeneratorTest {

	/**
	 * <p>
	 * System under test (should be reset per test).
	 * </p>
	 */
	private FauxMBeanGenerator fake;

	/**
	 * <p>
	 * Method invoked before each unit-test in this class.
	 * </p>
	 */
	@Before
	public void Setup() {
		MockJmx fauxJmxStore = new MockJmx();
		fake = new FauxMBeanGenerator(fauxJmxStore);
	}

	/**
	 * <p>
	 * Method invoked after each unit-test in this class.
	 * </p>
	 */
	@After
	public void TearDown() {
		fake = null;
	}

	/**
	 * <p>
	 * This test is to double-check the jmxType for our endpoints. These are
	 * important b/c our queries are based off of this information. A secondary
	 * reason for this test is to test toe default no arg constructor of our
	 * MBeans.
	 * </p>
	 */
	@Test
	public void test_VerifyJmxTypeForOurMbeans() {

		Assert.assertTrue("endpoint".equals(new Endpoint().getJmxType()));
		Assert.assertTrue("operation".equals(new Operation().getJmxType()));
		Assert.assertTrue("operationCall".equals(new OperationCall()
				.getJmxType()));
	}

	/**
	 * <p>
	 * Verify the Endpoint MBean.
	 * </p>
	 * 
	 * <p>
	 * This is a temporary test, it should be refactored when the
	 * FauxMBeanGenerator is moved out of the production code.
	 * </p>
	 */
	@Test
	public void test_VerifyNumberOfEndpointMbeans() {
		ArrayList<EndpointMBean> list = fake.getEndpointMbeans();
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(
				"http://localhost:9080/WebServiceProject/CalculatorService",
				StringMangler.DecodeForJmx(list.get(0).getUrl()));
	}

	/**
	 * <p>
	 * Verify the Operation MBean.
	 * </p>
	 * 
	 * <p>
	 * This is a temporary test, it should be refactored when the
	 * FauxMBeanGenerator is moved out of the production code.
	 * </p>
	 */
	@Test
	public void test_VerifyNumberOfOperationMbeans() {
		ArrayList<OperationMBean> list = fake.getOperationMbeans();
		assertNotNull(list);
		assertEquals(4, list.size());
		assertEquals("add", list.get(0).getName());
		assertEquals("multiply", list.get(1).getName());
		assertEquals("subtract", list.get(2).getName());
		assertEquals("divide", list.get(3).getName());
	}

	/**
	 * <p>
	 * Verify the Operation MBean.
	 * </p>
	 * 
	 * <p>
	 * This is a temporary test, it should be refactored when the
	 * FauxMBeanGenerator is moved out of the production code.
	 * </p>
	 */
	@Test
	public void test_VerifyNumberOfOperationCallsMbeans() {
		ArrayList<OperationCallMBean> list = fake.getOperationCallsMbeans();
		assertNotNull(list);
		this.verifyGetOperationCallMBeans(list);
	}

	/**
	 * <p>
	 * Helper function to very the Get on the OperationCallMBeans
	 * </p>
	 * 
	 * @param list
	 *            Collection of OperationCallMBeans that should match the
	 *            expected return values from the fake implementation.
	 */
	private void verifyGetOperationCallMBeans(ArrayList<OperationCallMBean> list) {
		assertEquals(2, list.size());

		assertEquals("multiply", list.get(0).getSourceOperation().getName());
		assertEquals(
				"http://localhost:9080/WebServiceProject/CalculatorService",
				StringMangler.DecodeForJmx(list.get(0).getSourceEndpoint()
						.getUrl()));
		assertEquals("add", list.get(0).getTargetOperation().getName());
		assertEquals(
				"http://localhost:9080/WebServiceProject/CalculatorService",
				StringMangler.DecodeForJmx(list.get(0).getTargetEndpoint()
						.getUrl()));

		assertEquals("divide", list.get(1).getSourceOperation().getName());
		assertEquals(
				"http://localhost:9080/WebServiceProject/CalculatorService",
				StringMangler.DecodeForJmx(list.get(1).getSourceEndpoint()
						.getUrl()));
		assertEquals("subtract", list.get(1).getTargetOperation().getName());
		assertEquals(
				"http://localhost:9080/WebServiceProject/CalculatorService",
				StringMangler.DecodeForJmx(list.get(1).getTargetEndpoint()
						.getUrl()));
	}


}
