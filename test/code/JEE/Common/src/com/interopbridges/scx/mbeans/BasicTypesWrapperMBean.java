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
 * for an object that has a property that is another MBean that contains arrays.
 * <p>
 * 
 * @author Christopher Crammond
 */
public interface BasicTypesWrapperMBean {

	/**
	 * <p>
	 * Get Property Method for String
	 * </p>
	 * 
	 * @return An String 'primitive' type for XML conversion testing
	 */
	public abstract String getTheLabel();

	/**
	 * An object that consists only of 'primitive' types (A String counts as a
	 * primitive).
	 * 
	 * @return BasicTypesMBean object that consists of primitive types.
	 */
	public abstract BasicTypesMBean getPayload();

}