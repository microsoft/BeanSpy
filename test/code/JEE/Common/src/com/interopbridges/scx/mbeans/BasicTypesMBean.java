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
 * MBean that has properties representing all of the basic primitive types. This
 * is meant for use in test purposes.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public interface BasicTypesMBean {

	/**
	 * <p>
	 * Return a property that is of a boolean type
	 * </p>
	 * 
	 * @return Some boolean value
	 */
	public abstract boolean getIsBoolean();

	/**
	 * <p>
	 * Return a property that is of a byte type
	 * </p>
	 * 
	 * @return Some byte value
	 */
	public abstract byte getByte();

	/**
	 * <p>
	 * Return a property that is of a char type
	 * </p>
	 * 
	 * @return Some char value
	 */

	public abstract char getCharLetter();

	/**
	 * <p>
	 * Return a property that is of a double type
	 * </p>
	 * 
	 * @return Some double value
	 */
	public abstract double getDoubleNumber();

	/**
	 * <p>
	 * Return a property that is of a float type
	 * </p>
	 * 
	 * @return Some float value
	 */
	public abstract float getFloatNumber();

	/**
	 * <p>
	 * Return a property that is of a int type
	 * </p>
	 * 
	 * @return Some int value
	 */
	public abstract int getIntNumber();

	/**
	 * <p>
	 * Return a property that is of a long type
	 * </p>
	 * 
	 * @return Some long value
	 */
	public abstract long getLongNumber();

	/**
	 * <p>
	 * Return a property that is of a short type
	 * </p>
	 * 
	 * @return Some short value
	 */
	public abstract short getShortNumber();

	/**
	 * <p>
	 * Return a property that is of a String type
	 * </p>
	 * 
	 * @return Some String value
	 */
	public abstract String getTheLabel();

}