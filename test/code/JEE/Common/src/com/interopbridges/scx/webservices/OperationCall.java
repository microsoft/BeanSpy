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

package com.interopbridges.scx.webservices;

/**
 * <p>
 * Concrete representation of an Operation Call to represent calls between the
 * operations to another web services's operations.
 * </p>
 * 
 * @author Christopher Crammond
 */
public class OperationCall implements OperationCallMBean {

	/**
	 * Key for describing for the (interopbridges) JMX type of MBean
	 */
	private String _jmxType = "operationCall";

	/**
	 * The operation from which the operation call originates
	 */
	private OperationMBean _sourceOperation;

	/**
	 * The endpoint from which the operation call originates
	 */
	private EndpointMBean _sourceEndpoint;

	/**
	 * The operation from which the operation call targets
	 */
	private OperationMBean _targetOperation;

	/**
	 * The endpoint from which the operation call targets
	 */
	private EndpointMBean _targetEndpoint;

	/**
	 * <p>
	 * Empty Constructor. It is considered to be a best practice to create this
	 * default constructor rather than relying on the compiler to auto-generate
	 * it.
	 * </p>
	 */
	public OperationCall() {
	}

	/**
	 * <p>
	 * Preferred Constructor that represents the dynamic connections between two
	 * webservices
	 * </p>
	 * 
	 * @param sourceEndpoint
	 *            management interface representing the Web Service's endpoint
	 *            that the call originated from
	 * @param sourceOperation
	 *            management interface representing the Web Service's operation
	 *            that the call originated from
	 * @param targetEndpoint
	 *            management interface representing the Web Service's operation
	 *            that the call targets
	 * @param targetOperation
	 *            management interface representing the Web Service's operation
	 *            that the call targets
	 */
	public OperationCall(EndpointMBean sourceEndpoint,
			OperationMBean sourceOperation, EndpointMBean targetEndpoint,
			OperationMBean targetOperation) {
		this._sourceOperation = sourceOperation;
		this._sourceEndpoint = sourceEndpoint;
		this._targetOperation = targetOperation;
		this._targetEndpoint = targetEndpoint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.interopbridges.scx.webservices.OperationCallMBean#getSourceOperation()
	 */
	public OperationMBean getSourceOperation() {
		return this._sourceOperation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.webservices.OperationCallMBean#getSourceEndpoint()
	 */
	public EndpointMBean getSourceEndpoint() {
		return this._sourceEndpoint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.interopbridges.scx.webservices.OperationCallMBean#getTargetOperation()
	 */
	public OperationMBean getTargetOperation() {
		return this._targetOperation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.webservices.OperationCallMBean#getTargetEndpoint()
	 */
	public EndpointMBean getTargetEndpoint() {
		return this._targetEndpoint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.webservices.IMBean#getJmxType()
	 */
	public String getJmxType() {
		return this._jmxType;
	}

}
