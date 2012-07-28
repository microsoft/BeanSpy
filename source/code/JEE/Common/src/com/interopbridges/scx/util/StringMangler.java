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

/**
 * Utility class for string manipulation. <br>
 * 
 * Note: it may be useful to look at the URLEncoder, but there is concern that this might encode too much information. <br>
 * 
 * @author Christopher Crammond
 */
public class StringMangler {

	/**
	 * De-mangles a given string that is acceptable for JMX. Presently this
	 * method will:
	 * <ul>
	 * Transforms colons
	 * </ul>
	 * 
	 * Note: this class presently only implements functionality as needed.
	 * 
	 * @param input
	 *            String to demangle
	 * @return Transformed input string
	 */
	public static String DecodeForJmx(String input) {
		return input.replace("&colon;", ":");
	}

	/**
	 * Mangles a given string so that it is acceptable for JMX. Presently this
	 * method will:
	 * <ul>
	 * Transforms colons
	 * </ul>
	 * <br>
	 * 
	 * Note: this class presently only implements functionality as needed.
	 * 
	 * @param input
	 *            String to Mangle
	 * @return Transformed input string that should be submittable to JMX
	 */
	public static String EncodeForJmx(String input) {
		return input.replace(":", "&colon;");
	}

}
