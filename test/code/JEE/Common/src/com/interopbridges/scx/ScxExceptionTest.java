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

package com.interopbridges.scx;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;

/**
 * <p>
 * Simple checks for the project specific exception & exception related tasks
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class ScxExceptionTest {

	/**
	 * <p>
	 * Verify the empty constructor's getMessage() works as expected. It is
	 * generally expected that this exception not be used directly by coders,
	 * but indirectly when/if serialization is used.
	 * </p>
	 */
	@Test
	public void VerifyEmptyException() {
		Assert
				.assertNull(
						"Received exception of the correct type, but the wrong exception code.",
						new ScxException().getMessage());
	}

	/**
	 * <p>
	 * Verify that attempting to get the resource bundle does not generate an
	 * exception.
	 * </p>
	 * 
	 * <p>
	 * To do this, one exception is chosen at random and we just want to verify
	 * that some non-empty String is returned. We could actually try to verify
	 * that the string matches, but that probably is not a good idea as then
	 * this unit test would fail when run on a system with a different locale.
	 * </p>
	 */
	@Test
	public void VerifyThatGettingTheExceptionCodeDoesNotThrowAnException() {
		ScxExceptionBundle a = ScxExceptionBundle
				.getBundle(ScxException.resourceBundleName);
		String valueFromBundle = a.getString(
				ScxExceptionCode.ERROR_TRANSFORMING_MBEAN, null);

		ILogger logger = LoggingFactory.getLogger();
		logger.warning("Exception Message: " + valueFromBundle);

		Assert.assertNotNull(valueFromBundle);
		Assert.assertTrue(0 < valueFromBundle.length());

		/*
		 * If the message could not be found in the bundle, then the output seen
		 * is the key (which according to convention) should be the same as the
		 * variable name. If this test fails, verify that someone has not
		 * mistakenly changed the string value in the properties file.
		 */
		Assert
				.assertFalse(
						"String value returned matches the key (and that implies the logic for finding the resource bundle is not working properly)",
						"ERROR_TRANSFORMING_MBEAN".equals(valueFromBundle));
	}

	/**
	 * <p>
	 * Verify that attempting to get the resource bundle does not generate an
	 * exception. This is pretty much the same test logic as the default locale,
	 * the difference being we should get a different string than the default
	 * locale.
	 * </p>
	 * 
	 * <p>
	 * To do this, one exception is chosen at random and we just want to verify
	 * that some non-empty String is returned. We could actually try to verify
	 * that the string matches, but that probably is not a good idea as then
	 * this unit test would fail when run on a system with a different locale.
	 * </p>
	 */
	@Test
	public void VerifyThatGettingTheExceptionCodeDoesNotThrowAnExceptionForNonDefaultLocale() {
		ScxExceptionBundle english = ScxExceptionBundle.getBundle(
				ScxException.resourceBundleName, Locale.US);
		ScxExceptionBundle chinese = ScxExceptionBundle.getBundle(
				ScxException.resourceBundleName, Locale.SIMPLIFIED_CHINESE);
		String valueFromEnglishBundle = english.getString(
				ScxExceptionCode.ERROR_TRANSFORMING_MBEAN, null);
		String valueFromChineseBundle = chinese.getString(
				ScxExceptionCode.ERROR_TRANSFORMING_MBEAN, null);

		ILogger logger = LoggingFactory.getLogger();
		logger.warning("Exception Message: " + valueFromChineseBundle);

		Assert.assertNotNull(valueFromChineseBundle);
		Assert.assertTrue(0 < valueFromChineseBundle.length());

		/*
		 * If the message could not be found in the bundle, then the output seen
		 * is the key (which according to convention) should be the same as the
		 * variable name. If this test fails, verify that someone has not
		 * mistakenly changed the string value in the properties file.
		 */
		Assert
				.assertFalse(
						"String value returned matches the key (and that implies the logic for finding the resource bundle is not working properly)",
						"ERROR_TRANSFORMING_MBEAN"
								.equals(valueFromChineseBundle));

		Assert
				.assertFalse(
						"String value returned matches the key (and that implies the logic for finding the resource bundle is not working properly)",
						valueFromEnglishBundle.equals(valueFromChineseBundle));

	}

	/**
	 * <p>
	 * Verify that the equals() method (and indirectly hashcode()) work properly
	 * for the expected 'true' cases for the ScxExceptionCode class.
	 * </p>
	 */
	@Test
	public void ExceptionEqualsReturnsTrueForEqualObjects() {
		ScxExceptionCode exception1 = ScxExceptionCode.ERROR_TRANSFORMING_MBEAN;
		ScxExceptionCode exception2 = exception1;

		Assert.assertEquals("Exception should equal itself!", exception1,
				exception1);
		Assert.assertTrue("Test Shallow Copy is equal", exception1
				.equals(exception2));
		Assert.assertTrue("Test Shallow Copy is equal (reciprocal)", exception2
				.equals(exception1));

	}

	/**
	 * <p>
	 * Verify that the equals() method work properly for the expected 'false'
	 * cases for the ScxExceptionCode class. Here we have two objects of the
	 * same type but different values.
	 * </p>
	 */
	@Test
	public void ExceptionEqualsReturnsFalseForInequalObjectsOfTheSameType() {
		ScxExceptionCode exception1 = ScxExceptionCode.ERROR_TRANSFORMING_MBEAN;
		ScxExceptionCode exception2 = ScxExceptionCode.MALFORMED_OBJECT_NAME;

		Assert.assertFalse("Test Inequality", exception1.equals(exception2));
		Assert.assertFalse("Test Inequality (reciprocal)", exception2
				.equals(exception1));
	}

	/**
	 * <p>
	 * Verify that the equals() method work properly for the expected 'false'
	 * cases for the ScxExceptionCode class. This test case is for equals
	 * against an object of a different type.
	 * </p>
	 */
	@Test
	public void ExceptionEqualsReturnsFalseForInequalObjectsOfTheDifferntType() {
		ScxExceptionCode exception1 = ScxExceptionCode.ERROR_TRANSFORMING_MBEAN;
		String exception2 = "this should not match";

		Assert.assertFalse("Test Inequality", exception1.equals(exception2));
	}

	/**
	 * <p>
	 * Verify that the hashCode() method work properly for the expected 'true'
	 * cases for the ScxExceptionCode class.
	 * </p>
	 */
	@Test
	public void ExceptionHashCodeReturnsTrueForEqualObjects() {
		ScxException exception1 = new ScxException(
				ScxExceptionCode.ERROR_TRANSFORMING_MBEAN);
		ScxException exception2 = exception1;

		Assert.assertEquals("Exception should equal itself!", exception1,
				exception1);
		Assert.assertEquals("Test Shallow Copy is equal",
				exception1.hashCode(), exception2.hashCode());
	}

	/**
	 * <p>
	 * Verify that the equals() method work properly for the expected 'false'
	 * cases for the ScxExceptionCode class. Here we have two objects of the
	 * same type but different values.
	 * </p>
	 */
	@Test
	public void ExceptionHashCodeReturnsFalseForInequalObjectsOfTheSameType() {
		ScxExceptionCode exception1 = ScxExceptionCode.ERROR_TRANSFORMING_MBEAN;
		ScxExceptionCode exception2 = ScxExceptionCode.MALFORMED_OBJECT_NAME;

		Assert.assertNotSame(exception1.hashCode(), exception2.hashCode());
	}

}
