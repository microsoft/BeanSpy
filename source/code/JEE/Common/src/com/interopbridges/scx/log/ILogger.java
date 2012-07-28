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

/**
 * <p>
 * Generic Logging interface that to encapsulate the common logging
 * functionality.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public interface ILogger {

	/**
	 * <p>
	 * Log a message an an error level.
	 * </p>
	 * 
	 * @param message
	 *            Desired message to appear in the log
	 */
	public void error(String message);

	/**
	 * <p>
	 * Log a message an an error level.
	 * </p>
	 * 
	 * @param message
	 *            Desired message to appear in the log
	 * 
	 * @param t
	 *            Throwable exception to insert into the log message
	 */
	public void error(String message, Throwable t);

	/**
	 * <p>
	 * Log a message an an fine level. Typically used for debugging.
	 * </p>
	 * 
	 * @param message
	 *            Desired message to appear in the log
	 */
	public void fine(String message);

	/**
	 * <p>
	 * Log a message an an finer level. Typically used for debugging.
	 * </p>
	 * 
	 * @param message
	 *            Desired message to appear in the log
	 */
	public void finer(String message);

	/**
	 * <p>
	 * Log a message an an finest level. Typically used for debugging.
	 * </p>
	 * 
	 * @param message
	 *            Desired message to appear in the log
	 */
	public void finest(String message);

	/**
	 * <p>
	 * Log a message an an warning level.
	 * </p>
	 * 
	 * @param message
	 *            Desired message to appear in the log
	 */
	public void warning(String message);

	/**
	 * <p>
	 * Log a message an an informational level.
	 * </p>
	 * 
	 * @param message
	 *            Desired message to appear in the log
	 */
	public void info(String message);

}
