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
 * 
 * <p>
 * Concrete representation of an Endpoint to match what is described in the web
 * service's WSDL.
 * </p>
 * 
 * <p>
 * 
 * <pre>
 * &lt;service name="CalculatorService"&gt;
 *   &lt;port name="CalculatorPort" binding="tns:CalculatorPortBinding"&gt;
 *     &lt;soap:address location="http://scxom64-ws7-02:9080/WebServiceProject/CalculatorService" /&gt; 
 *   &lt;/port&gt;
 * &lt;/service&gt;
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * Typically this might look like:
 * <ol>
 * <li><b>http://scxom64-ws7-02:9080/WebServiceProject/CalculatorService</b></li>
 * <li><b>http://scxom-ws7-02:8080/axis2/services/DinnerFinderService</li>
 * DinnerFinderServiceHttpSoap11Endpoint/</b>
 * </ol>>
 * </p>
 * 
 * @author Christopher Crammond
 */
public class Endpoint implements EndpointMBean {

	/**
	 * Key for describing for the (interopbridges) JMX type of MBean
	 */
	private String _jmxType = "endpoint";

	/**
	 * String representing the full URL of the endpoint address. This should
	 * match the soap:address's location attribute from the WSDL. <br>
	 * 
	 */
	private String _url;

	/**
	 * Empty Constructor. It is considered to be a best practice to create this
	 * default constructor rather than relying on the compiler to auto-generate
	 * it.
	 */
	public Endpoint() {
		this._url = "";
	}

	/**
	 * Preferred Constructor
	 * 
	 * @param url
	 *            String representing the full URL of the endpoint address.
	 */
	public Endpoint(String url) {
		this._url = url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.webservices.EndpointMBean#getUrl()
	 */
	public String getUrl() {
		return this._url;
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
