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
 * Simple MBean that contain the basic wrapper types. The primary intention of this
 * class is a simple class that contains all the basic wrapper class types. This class is used
 * in the unit tests for verifying that the MBeans can be represented as XML.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class BasicTypesWrapperClass implements BasicTypesWrapperClassMBean {

	/**
	 * A primitive Boolean property needed for XML conversion testing
	 */
	private Boolean _Boolean;

	/**
	 * A primitive Byte property needed for XML conversion testing
	 */
	private Byte _byte;

	/**
	 * A primitive Character property needed for XML conversion testing
	 */
	private Character _charLetter;

	/**
	 * A primitive Double property needed for XML conversion testing
	 */
	private Double _doubleNumber;

	/**
	 * A primitive Float property needed for XML conversion testing
	 */
	private Float _floatNumber;

	/**
	 * A primitive Integer property needed for XML conversion testing
	 */
	private Integer _intNumber;

	/**
	 * A primitive Long property needed for XML conversion testing
	 */
	private Long _longNumber;

	/**
	 * A primitive Short property needed for XML conversion testing
	 */
	private Short _shortNumber;

	/**
	 * A primitive String property needed for XML conversion testing
	 */
	private final String _theLabel = "basicTypesWrapperClass";

	/**
	 * Empty Constructor. It is considered to be a best practice to create this
	 * default constructor rather than relying on the compiler to auto-generate
	 * it.
	 */
	public BasicTypesWrapperClass() {
		this._Boolean = new Boolean(true);
		this._byte = new Byte(Byte.MAX_VALUE);
		this._charLetter = new Character('a');
		this._doubleNumber = new Double(Double.MAX_VALUE);
		this._floatNumber = new Float(Float.MAX_VALUE);
		this._intNumber = new Integer(Integer.MAX_VALUE);
		this._longNumber = new Long(Long.MAX_VALUE);
		this._shortNumber = new Short(Short.MAX_VALUE);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesWrapperClassMBean#getBoolean()
	 */
	public Boolean getBoolean() {
		return this._Boolean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesWrapperClassMBean#getByte()
	 */
	public Byte getByte() {
		return this._byte;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesWrapperClassMBean#getCharLetter()
	 */
	public Character getCharacter() {
		return this._charLetter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesWrapperClassMBean#getDoubleNumber()
	 */
	public Double getDouble() {
		return this._doubleNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesWrapperClassMBean#getFloatNumber()
	 */
	public Float getFloat() {
		return this._floatNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesWrapperClassMBean#getIntNumber()
	 */
	public Integer getInteger() {
		return this._intNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesWrapperClassMBean#getLongNumber()
	 */
	public Long getLong() {
		return this._longNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesWrapperClassMBean#getShortNumber()
	 */
	public Short getShort() {
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

}
