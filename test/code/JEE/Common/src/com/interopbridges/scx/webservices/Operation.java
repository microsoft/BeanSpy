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
 * Concrete representation of an Operation to match what is described in the web
 * service's WSDL.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class Operation implements OperationMBean {

	/**
	 * Key for describing for the (interopbridges) JMX type of MBean
	 */
	private String _jmxType = "operation";

	/**
	 * Name of the method which the Operation represents. * This should
	 * correspond to the information described in the following section of the
	 * WSDL: <br>
	 * 
	 * <pre>
	 * <operation name="add">
	 *   <soap:operation soapAction="" />
	 *     <input>
	 *       <soap:body use="literal" />
	 *     </input>
	 *     <output>
	 *       <soap:body use="literal" />
	 *     </output>
	 *   </operation>
	 * </pre>
	 */
	private String _name;

	/**
	 * Empty Constructor. It is considered to be a best practice to create this
	 * default constructor rather than relying on the compiler to auto-generate
	 * it.
	 */
	public Operation() {
		this._name = "";
	}

	/**
	 * Preferred constructor
	 * 
	 * @param name
	 *            Operation's name (as seen in the WSDL)
	 */
	public Operation(String name) {
		this._name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.webservices.OperationMBean#getName()
	 */
	public String getName() {
		return this._name;
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
