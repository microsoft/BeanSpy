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
 * Simple MBean that contain the basic types. The primary intention of this
 * class is a simple class that contains all the basic types as arrays. This
 * class is used in the unit tests for verifying that the MBeans can be
 * represented as XML. In addition, a random type was choosen again and has an
 * empty array (As the logic is the same internally, there is not a need to ).
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class BasicTypeArrays implements BasicTypeArraysMBean {

	/**
	 * <p>
	 * An array of boolean primitive types for XML conversion testing
	 * </p>
	 */
	private boolean[] _isBoolean = { true, false };

	/**
	 * <p>
	 * An array of byte primitive types for XML conversion testing
	 * </p>
	 */
	private byte[] _byte = { Byte.MIN_VALUE, Byte.MAX_VALUE };

	/**
	 * <p>
	 * An array of char primitive types for XML conversion testing
	 * </p>
	 */
	private char[] _charLetter = { 'a', 'b', 'c' };

	/**
	 * <p>
	 * An array of boolean double types for XML conversion testing
	 * </p>
	 */
	private double[] _doubleNumber = { Double.MIN_VALUE, Double.MAX_VALUE };

	/**
	 * <p>
	 * An array of float primitive types for XML conversion testing
	 * </p>
	 */
	private float[] _floatNumber = { Float.MIN_VALUE, Float.MAX_VALUE };

	/**
	 * <p>
	 * An array of int primitive types for XML conversion testing
	 * </p>
	 */
	private int[] _intNumber = { Integer.MIN_VALUE, Integer.MAX_VALUE };

	/**
	 * <p>
	 * An array of long primitive types for XML conversion testing
	 * </p>
	 */
	private long[] _longNumber = { Long.MIN_VALUE, Long.MAX_VALUE };

	/**
	 * <p>
	 * An array of short primitive types for XML conversion testing
	 * </p>
	 */
	private short[] _shortNumber = { Short.MIN_VALUE, Short.MAX_VALUE };

	/**
	 * <p>
	 * A Label for use in the mock JMX framework
	 * </p>
	 */
	private final String _theLabel = "basicTypeArrays";

	/**
	 * <p>
	 * An array of String primitive types for XML conversion testing
	 * </p>
	 */
	private String[] _stringArray = { "a", "two" };

	/**
	 * Empty Constructor. It is considered to be a best practice to create this
	 * default constructor rather than relying on the compiler to auto-generate
	 * it.
	 */
	public BasicTypeArrays() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypeArraysMBean#getIsBoolean()
	 */
	public boolean[] getIsBoolean() {
		return this._isBoolean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypeArraysMBean#getByte()
	 */
	public byte[] getByte() {
		return this._byte;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypeArraysMBean#getCharLetter()
	 */
	public char[] getCharLetter() {
		return this._charLetter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypeArraysMBean#getDoubleNumber()
	 */
	public double[] getDoubleNumber() {
		return this._doubleNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypeArraysMBean#getFloatNumber()
	 */
	public float[] getFloatNumber() {
		return this._floatNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypeArraysMBean#getIntNumber()
	 */
	public int[] getIntNumber() {
		return this._intNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypeArraysMBean#getLongNumber()
	 */
	public long[] getLongNumber() {
		return this._longNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypeArraysMBean#getShortNumber()
	 */
	public short[] getShortNumber() {
		return this._shortNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypeArraysMBean#getTheLabel()
	 */
	public String getTheLabel() {
		return this._theLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypeArraysMBean#getStringArray()
	 */
	public String[] getStringArray() {
		return this._stringArray;
	}

}
