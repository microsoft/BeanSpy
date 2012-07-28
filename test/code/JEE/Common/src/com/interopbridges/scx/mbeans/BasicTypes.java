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
 * class is a simple class that contains all the basic types. This class is used
 * in the unit tests for verifying that the MBeans can be represented as XML.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class BasicTypes implements BasicTypesMBean {

	/**
	 * A primitive boolean property needed for XML conversion testing
	 */
	private boolean _isBoolean;

	/**
	 * A primitive byte property needed for XML conversion testing
	 */
	private byte _byte;

	/**
	 * A primitive char property needed for XML conversion testing
	 */
	private char _charLetter;

	/**
	 * A primitive double property needed for XML conversion testing
	 */
	private double _doubleNumber;

	/**
	 * A primitive float property needed for XML conversion testing
	 */
	private float _floatNumber;

	/**
	 * A primitive int property needed for XML conversion testing
	 */
	private int _intNumber;

	/**
	 * A primitive long property needed for XML conversion testing
	 */
	private long _longNumber;

	/**
	 * A primitive short property needed for XML conversion testing
	 */
	private short _shortNumber;

	/**
	 * A primitive String property needed for XML conversion testing
	 */
	private final String _theLabel = "basicTypes";

	/**
	 * Empty Constructor. It is considered to be a best practice to create this
	 * default constructor rather than relying on the compiler to auto-generate
	 * it.
	 */
	public BasicTypes() {
		this._isBoolean = true;
		this._byte = Byte.MAX_VALUE;
		this._charLetter = 'a';
		this._doubleNumber = Double.MAX_VALUE;
		this._floatNumber = Float.MAX_VALUE;
		this._intNumber = Integer.MAX_VALUE;
		this._longNumber = Long.MAX_VALUE;
		this._shortNumber = Short.MAX_VALUE;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesMBean#getIsBoolean()
	 */
	public boolean getIsBoolean() {
		return this._isBoolean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesMBean#getByte()
	 */
	public byte getByte() {
		return this._byte;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesMBean#getCharLetter()
	 */
	public char getCharLetter() {
		return this._charLetter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesMBean#getDoubleNumber()
	 */
	public double getDoubleNumber() {
		return this._doubleNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesMBean#getFloatNumber()
	 */
	public float getFloatNumber() {
		return this._floatNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesMBean#getIntNumber()
	 */
	public int getIntNumber() {
		return this._intNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesMBean#getLongNumber()
	 */
	public long getLongNumber() {
		return this._longNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesMBean#getShortNumber()
	 */
	public short getShortNumber() {
		return this._shortNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesMBean#getTheLabel()
	 */
	public String getTheLabel() {
		return this._theLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("isBoolean ").append(_isBoolean).append("| ");
		result.append("byte ").append(_byte).append("| ");
		result.append("charLetter ").append(_charLetter).append("| ");
		result.append("doubleNumber ").append(_doubleNumber).append("| ");
		result.append("floatNumber ").append(_floatNumber).append("| ");
		result.append("intNumber ").append(_intNumber).append("| ");
		result.append("longNumber ").append(_longNumber).append("| ");
		result.append("shortNumber ").append(_shortNumber).append("| ");
		result.append("theLabel ").append(_theLabel);
		return result.toString();
	}

}
