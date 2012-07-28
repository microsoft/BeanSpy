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

package com.interopbridges.scx;

/**
 * <p>
 * Wrapper around the base Exception class and the parent
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class ScxException extends Exception {

    /**
     * <p>
     * Name of the resource bundle containing the localized Exception messages.
     * </p>
     */
    public static final String resourceBundleName = "resources.exceptions.exception";

    /**
     * <p>
     * Required UID for class that implements the Serializable interface
     * </p>
     */
    private static final long serialVersionUID = 8783576309802325275L;

    /**
     * <p>
     * Localized Exception Message.
     * </p>
     * 
     */
    private ScxExceptionCode _localizedMessage;

    /**
     * <p>
     * replacement arguments.
     * </p>
     * 
     */
    private Object[] _args = null;

    /**
     * <p>
     * Default Constructor. A no-argument constructor is recommended for
     * implementing the Serializable interface.
     * </p>
     */
    public ScxException() {
        super();
    }

    /**
     * <p>
     * Constructs a new exception with the specified detail message. The cause
     * is not initialized, and may subsequently be initialized by a call to
     * Throwable.initCause(java.lang.Throwable).
     * </p>
     * 
     * @param exceptionCode
     *            the detail message. The detail message is saved for later
     *            retrieval by the Throwable.getMessage() method.
     */
    public ScxException(ScxExceptionCode exceptionCode) {
        super(exceptionCode.getCode());
        this._localizedMessage = exceptionCode;
    }

    /**
     * <p>
     * Constructs a new exception with the specified detail message with variable in 
     * the bracket. For example, "The size of the XML response has reached the limits 
     * of {0} bytes by the query: {1}". An object array can get running time data to
     *  fill in a more detailed messages with running time data. 
     * </p>
     *
     * <p>
     * The cause is not initialized, and may subsequently be initialized by a call to
     * Throwable.initCause(java.lang.Throwable).
     * </p>
     * 
     * @param exceptionCode
     *            the detail message. The detail message is saved for later
     *            retrieval by the Throwable.getMessage() method.
     *    
     * @param args
     *            running time data to be filled in the detail message.
     *
     */
    public ScxException(ScxExceptionCode exceptionCode, Object[] args) {
         super(exceptionCode.getCode());
         this._args = args.clone();
         this._localizedMessage = exceptionCode;
     }

    /**
     * <p>
     * Constructs a new exception with the specified cause and a detail message
     * of (cause==null ? null : cause.toString()) (which typically contains the
     * class and detail message of cause). This constructor is useful for
     * exceptions that are little more than wrappers for other throwables (for
     * example, PrivilegedActionException).
     * </p>
     * 
     * @param exceptionCode
     *            the detail message (which is saved for later retrieval by the
     *            Throwable.getMessage() method).
     * @param cause
     *            the cause (which is saved for later retrieval by the
     *            Throwable.getCause() method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     */
    public ScxException(ScxExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode.getCode(), cause);
        this._localizedMessage = exceptionCode;
    }

    /**
     * <p>
     * Override the default getMessage() operation with code that will only get
     * Localized Messages.
     * </p>
     * 
     * @return Translated String of the ExceptionCode to the appropriate Locale.
     * 
     */
    @Override
    public String getMessage() {
        return _localizedMessage == null ? null : ScxExceptionBundle.getBundle(
                resourceBundleName).getString(_localizedMessage, _args);
    }

   /**
     * <p>
     * Getter method to retrieve the exception code associated with this ecxeption.
     * </p>
     * 
     * @return the exception code associated with this ecxeption.
     * 
     */
    public ScxExceptionCode getExceptionCode() 
    {
        return _localizedMessage;
    }
}
