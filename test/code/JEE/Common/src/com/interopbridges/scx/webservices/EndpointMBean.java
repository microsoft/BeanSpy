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
 * service operation. <br>
 * 
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
 *@author Christopher Crammond
 */
public interface EndpointMBean extends IMBean {

	/**
	 * Retrieve address of end-point used.
	 * 
	 * @return URL matching corresponding information in WSDL
	 */
	public String getUrl();
}
