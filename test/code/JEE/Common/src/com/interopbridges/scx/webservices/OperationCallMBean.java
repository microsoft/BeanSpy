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
 * @author Christopher Crammond
 */
public interface OperationCallMBean extends IMBean {

	/**
	 * <p>
	 * Get the operation that originated the web service call.
	 * </p>
	 * 
	 * @return Instance of the management interface operation from which the
	 *         call originated
	 * 
	 * @see OperationMBean
	 */
	public OperationMBean getSourceOperation();

	/**
	 * <p>
	 * Get the endpoint that originated the web service call.
	 * </p>
	 * 
	 * @return Instance of the management interface endpoint from which the call
	 *         originated
	 * 
	 * @see EndpointMBean
	 */
	public EndpointMBean getSourceEndpoint();

	/**
	 * <p>
	 * Get the operation that is targeted by the web service call.
	 * </p>
	 * 
	 * @return Instance of the management interface operation from which the
	 *         call is targeted
	 * 
	 * @see OperationMBean
	 */
	public OperationMBean getTargetOperation();

	/**
	 * <p>
	 * Get the endpoint that is targeted by the web service call.
	 * </p>
	 * 
	 * @return Instance of the management interface endpoint from which the call
	 *         is targeted
	 * 
	 * @see EndpointMBean
	 */
	public EndpointMBean getTargetEndpoint();

}
