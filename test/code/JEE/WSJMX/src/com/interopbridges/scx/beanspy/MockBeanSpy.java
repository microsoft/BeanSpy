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

package com.interopbridges.scx.beanspy;

import com.interopbridges.scx.beanspy.BeanSpy;
import com.interopbridges.scx.jmx.JdkJMXAbstraction;
import com.interopbridges.scx.mbeans.MBeanGetter;
import com.interopbridges.scx.webservices.FauxMBeanGenerator;

/**
 * <p>
 * Mock implementation of BeanSpy where we can override various pieces
 * for unit testing. The most important part is overriding the MBean store so
 * that we aren't accessing the real data (and can control failure states.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class MockBeanSpy extends BeanSpy {

	/**
	 * <p>
	 * Added to satisfy the serialization interface.
	 * </p>
	 */
	private static final long serialVersionUID = 4406902895291158587L;

	/**
	 * Override the default MBean store with this.
	 * 
	 * @param store
	 *            Desired MBean Store
	 */
	public void setMBeanGenerator(FauxMBeanGenerator generator) {
		_fakeMBeans = generator;
	}

	/**
	 * Override the default MBean store with this.
	 * 
	 * @param store
	 *            Desired MBean Store
	 */
	public void setMBeanStore(MBeanGetter getter) {
		_mbeanAccessor = getter;
	}
	
	/**
	 * <p>
	 * Fake MBean store for providing stock MBeans for Endpoints, Operations,
	 * and OperationCalls. This is temporary and should be removed once the real
	 * Interceptor is in place.
	 * </p>
	 */
	protected static FauxMBeanGenerator _fakeMBeans = new FauxMBeanGenerator(
			new JdkJMXAbstraction());

	/**
	 * <p>
	 * Initialization of the servlet.
	 * </p>
	 */
	@Override
	public void init() {
		super.init();
		
		/*
		 * This try/catch is temporary as the Fake MBean Generator is not the a
		 * final solution. It is a temporary artifact that should be removed
		 * when there is a real interceptor generating real Operation Call
		 * MBeans.
		 */
		try {
			_fakeMBeans.run();
		} catch (Exception e) {
			this._logger.error("Problem Running the fake MBeans", e);
		}
	}
}
