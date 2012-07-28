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

package com.interopbridges.scx.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit Test for the String Mangler utility class. <br>
 * 
 * Note: sut is an abreviation for System Under Test. <br>
 * 
 * @author Christopher Crammond
 * 
 */
public class StringManglerTest {

	/**
	 * Verification of various inputs are properly decoded from JMX
	 */
	@Test
	public void DecodeForJmxTest() {
		assertEquals("Empty String Test", "", StringMangler.DecodeForJmx(""));
		assertEquals("Simple ASCII String Test", "asdl1234jflkjasdflk",
				StringMangler.DecodeForJmx("asdl1234jflkjasdflk"));
		assertEquals("Something with a colon", "a:b", StringMangler
				.DecodeForJmx("a&colon;b"));

	}

	/**
	 * Verification of various inputs are properly encoded for JMX
	 */
	@Test
	public void EncodeForJmxTest() {
		assertEquals("Empty String Test", "", StringMangler.EncodeForJmx(""));
		assertEquals("Simple ASCII String Test", "asdl1234jflkjasdflk",
				StringMangler.EncodeForJmx("asdl1234jflkjasdflk"));
		assertEquals("Something with a colon", "a&colon;b", StringMangler
				.EncodeForJmx("a:b"));
	}

	/**
	 * Verification of various inputs for the roundtrip of encoding and decoding
	 * to JMX
	 */
	@Test
	public void verifyRoundTripOfEncodeToDecodeForJmx() {
		assertEquals("Empty String Test", "", StringMangler
				.DecodeForJmx(StringMangler.EncodeForJmx("")));
		assertEquals("Simple ASCII String Test", "asdl1234jflkjasdflk",
				StringMangler.DecodeForJmx(StringMangler
						.EncodeForJmx("asdl1234jflkjasdflk")));
		assertEquals("String with one colon Test", "a:b", StringMangler
				.DecodeForJmx(StringMangler.EncodeForJmx("a:b")));
	}
}
