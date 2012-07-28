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
 * @author Geoff Erasmus
 * 
 */
public interface BasicTypesWrapperClassMBean {

	/**
	 * <p>
	 * Return a property that is of a Boolean type
	 * </p>
	 * 
	 * @return Some Boolean value
	 */
	public abstract Boolean getBoolean();

	/**
	 * <p>
	 * Return a property that is of a Byte type
	 * </p>
	 * 
	 * @return Some Byte value
	 */
	public abstract Byte getByte();

	/**
	 * <p>
	 * Return a property that is of a Characer type
	 * </p>
	 * 
	 * @return Some Character value
	 */

	public abstract Character getCharacter();

	/**
	 * <p>
	 * Return a property that is of a Double type
	 * </p>
	 * 
	 * @return Some Double value
	 */
	public abstract Double getDouble();

	/**
	 * <p>
	 * Return a property that is of a Float type
	 * </p>
	 * 
	 * @return Some Float value
	 */
	public abstract Float getFloat();

	/**
	 * <p>
	 * Return a property that is of a Integer type
	 * </p>
	 * 
	 * @return Some Integer value
	 */
	public abstract Integer getInteger();

	/**
	 * <p>
	 * Return a property that is of a Long type
	 * </p>
	 * 
	 * @return Some Long value
	 */
	public abstract Long getLong();

	/**
	 * <p>
	 * Return a property that is of a Short type
	 * </p>
	 * 
	 * @return Some Short value
	 */
	public abstract Short getShort();

	/**
	 * <p>
	 * Return a property that is of a String type
	 * </p>
	 * 
	 * @return Some String value
	 */
	public abstract String getTheLabel();

}