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
 * MBean Interface for testing purposes. This represent a management interface
 * for an object with properties of arrays of the basic types.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public interface BasicTypeArraysMBean {

	/**
	 * <p>
	 * Get Property Method for a boolean array
	 * </p>
	 * 
	 * @return An array of float primitive types for XML conversion testing
	 */
	public abstract boolean[] getIsBoolean();

	/**
	 * <p>
	 * Get Property Method for a byte array
	 * </p>
	 * 
	 * @return An array of byte primitive types for XML conversion testing
	 */
	public abstract byte[] getByte();

	/**
	 * <p>
	 * Get Property Method for a char array
	 * </p>
	 * 
	 * @return An array of char primitive types for XML conversion testing
	 */
	public abstract char[] getCharLetter();

	/**
	 * <p>
	 * Get Property Method for a double array
	 * </p>
	 * 
	 * @return An array of double primitive types for XML conversion testing
	 */
	public abstract double[] getDoubleNumber();

	/**
	 * <p>
	 * Get Property Method for a float array
	 * </p>
	 * 
	 * @return An array of float primitive types for XML conversion testing
	 */
	public abstract float[] getFloatNumber();

	/**
	 * <p>
	 * Get Property Method for a int array
	 * </p>
	 * 
	 * @return An array of int primitive types for XML conversion testing
	 */
	public abstract int[] getIntNumber();

	/**
	 * <p>
	 * Get Property Method for a long array
	 * </p>
	 * 
	 * @return An array of long primitive types for XML conversion testing
	 */
	public abstract long[] getLongNumber();

	/**
	 * <p>
	 * Get Property Method for a short array
	 * </p>
	 * 
	 * @return An array of short primitive types for XML conversion testing
	 */
	public abstract short[] getShortNumber();

	/**
	 * <p>
	 * Get Property Method for String
	 * </p>
	 * 
	 * @return An String 'primitive' type for XML conversion testing
	 */
	public abstract String getTheLabel();

	/**
	 * <p>
	 * Get Property Method for a String array
	 * </p>
	 * 
	 * @return An array of String primitive types for XML conversion testing
	 */
	public abstract String[] getStringArray();

}