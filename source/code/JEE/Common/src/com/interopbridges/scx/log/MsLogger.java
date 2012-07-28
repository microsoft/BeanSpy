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

package com.interopbridges.scx.log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Implementation of the ILogger interface that wraps the standard JAVA Logging
 * API.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class MsLogger implements ILogger {

	/**
	 * <p>
	 * Internal handle to the Java Logging API
	 * </p>
	 */
	private Logger _logger;

	/**
	 * <p>
	 * Default Constructor
	 * </p>
	 * 
	 */
	public MsLogger() {
		_logger = Logger.getLogger("com.interopbridges.scx");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.log.ILogger#error(java.lang.String)
	 */
	public void error(String message) {
		_logger.severe(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.log.ILogger#error(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void error(String message, Throwable t) {
		_logger.log(Level.SEVERE, message, t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.log.ILogger#fine(java.lang.String)
	 */
	public void fine(String message) {
		_logger.fine(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.log.ILogger#finer(java.lang.String)
	 */
	public void finer(String message) {
		_logger.finer(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.log.ILogger#finest(java.lang.String)
	 */
	public void finest(String message) {
		_logger.finest(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.log.ILogger#info(java.lang.String)
	 */
	public void info(String message) {
		_logger.info(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.interopbridges.scx.log.ILogger#warning(java.lang.String)
	 */
	public void warning(String message) {
		_logger.warning(message);
	}

}
