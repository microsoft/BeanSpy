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
 * Management interface describing the JMX Management interface for a web
 * service operation.
 * </p>
 * 
 * <p>
 * The information presented here should primarily be read-only and used for
 * purposes of static discovery. This fits into the hierarchy of Endpoint(s) >
 * Operation(s) > Operation Call(s).
 * </p>
 * 
 * <p>
 * The information presented here is not strictly needed, as this information is
 * discovered during the static discovery process. The data here is needed so
 * that the Operation Call data can be added to the appropriate location within
 * Operations Manager.
 * </p>
 * 
 * <p>
 * This should correspond to the information described in the following section
 * of the WSDL: <br>
 * 
 * <pre>
 * &lt;operation name="add"&gt;
 *   &lt;soap:operation soapAction="" /&gt; 
 *   &lt;input&gt;
 *     &lt;soap:body use="literal" /&gt; 
 *   &lt;/input&gt;
 *   &lt;output&gt;
 *     &lt;soap:body use="literal" /&gt; 
 *   &lt;/output&gt;
 * &lt;/operation&gt;
 * </pre>
 * 
 * </p>
 * 
 * @author Christopher Crammond
 */
public interface OperationMBean extends IMBean {

	/**
	 * <p>
	 * Gets Retrieve the name of the operation called. Depending on the
	 * implemenation, this may be either the soap"operation's soapAction or
	 * operation's name attribute.
	 * </p>
	 * 
	 * @return An URI matching corresponding information in WSDL. This would be
	 *         of the form "add" or "urn:getBiggestSteakRestaurant".
	 */
	String getName();
}
