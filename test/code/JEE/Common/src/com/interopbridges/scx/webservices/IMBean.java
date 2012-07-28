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
 * Interface describing the generic properties common to all of the SCX MBeans
 * 
 * @author Christopher Crammond
 */
public interface IMBean {

	/**
	 * Get the type of JMX MBean. For instance, this would help distinguish
	 * Endpoint vs. Operation Calls when registering with JMX (i.e. a key in
	 * MBean registration parlance).
	 * 
	 * @return String defining the type of JMX MBean.
	 */
	public String getJmxType();

}
