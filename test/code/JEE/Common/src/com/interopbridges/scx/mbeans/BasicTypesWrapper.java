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
 * The BasicTypesWrapperMBean is meant for wrapping the BasicTypesMBean. The
 * purpose being this is an object in-directly exposed to the JMX store. The
 * purpose of this class is for testing/verifying that an 'object' can be
 * recursively/indirectly represented as XML.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class BasicTypesWrapper implements BasicTypesWrapperMBean {

	/**
	 * Simple identifier
	 */
	private final String _theLabel;

	/**
	 * First-level wrapped object
	 */
	private final BasicTypesMBean _payload;

	/**
	 * <p>
	 * Standard no-argument constructor
	 * </p>
	 */
	public BasicTypesWrapper() {
		this._theLabel = "BasicTypesWrapperMBean";
		this._payload = new BasicTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesWrapperMBean#getTheLabel()
	 */
	public String getTheLabel() {
		return this._theLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.mbeans.BasicTypesWrapperMBean#getPayload()
	 */
	public BasicTypesMBean getPayload() {
		return this._payload;
	}

}
