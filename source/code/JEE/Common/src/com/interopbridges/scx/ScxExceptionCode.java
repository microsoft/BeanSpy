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

import java.io.Serializable;

/**
 * <p>
 * Interface to the resource bundle strings. Each entry in the
 * exception.properties file should have a static string here
 * </p>
 * 
 * <p>
 * This class is needed for internationalization/globalization.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class ScxExceptionCode implements Serializable {

    /**
     * <p>
     * Required UID for class that implements the Serializable interface
     * </p>
     */
    private static final long serialVersionUID = 4224712403978787725L;

    public static final ScxExceptionCode ERROR_TRANSFORMING_MBEAN = new ScxExceptionCode(
            "ERROR_TRANSFORMING_MBEAN");

    public static final ScxExceptionCode MALFORMED_OBJECT_NAME = new ScxExceptionCode(
            "MALFORMED_OBJECT_NAME");

    public static final ScxExceptionCode NULL_POINTER_EXCEPTION = new ScxExceptionCode(
    "NULL_POINTER_EXCEPTION");

    public static final ScxExceptionCode IO_ERROR_EXCEPTION = new ScxExceptionCode(
    "IO_ERROR_EXCEPTION");
    
    public static final ScxExceptionCode ERROR_TRANSFORMING_STATISTIC = new ScxExceptionCode(
    "ERROR_TRANSFORMING_STATISTIC");

    public static final ScxExceptionCode ERROR_MBEANSTORE_INSTANCE_INVALID = new ScxExceptionCode(
    "ERROR_INVALID_INSTANCE");

    public static final ScxExceptionCode ERROR_INVALID_SERVLET_REQUEST_EMPTY_JMXQUERY = new ScxExceptionCode(
    "ERROR_INVALID_SERVLET_REQUEST_EMPTY_JMXQUERY");

    public static final ScxExceptionCode ERROR_INVALID_SERVLET_REQUEST_NO_JMXQUERY = new ScxExceptionCode(
    "ERROR_INVALID_SERVLET_REQUEST_NO_JMXQUERY");

    public static final ScxExceptionCode ERROR_INVALID_SERVLET_REQUEST_STATS = new ScxExceptionCode(
    "ERROR_INVALID_SERVLET_REQUEST_STATS");

    public static final ScxExceptionCode ERROR_INVOKE_MBEAN_NOT_UNIQUE = new ScxExceptionCode(
    "ERROR_INVOKE_MBEAN_NOT_UNIQUE");

    public static final ScxExceptionCode ERROR_CONNECTING_JMXSTORE = new ScxExceptionCode(
    "ERROR_CONNECTING_JMXSTORE");

    public static final ScxExceptionCode ERROR_URL_LENGTH_EXCEEDS_LIMITS = new ScxExceptionCode(
    "ERROR_URL_LENGTH_EXCEEDS_LIMITS");

    public static final ScxExceptionCode ERROR_INVOKE_PARAM_MISMATCH = new ScxExceptionCode(
    "ERROR_INVOKE_PARAM_MISMATCH");

    public static final ScxExceptionCode ERROR_INVOKE_METHOD_NOTFOUND = new ScxExceptionCode(
    "ERROR_INVOKE_METHOD_NOTFOUND");
    
    public static final ScxExceptionCode ERROR_INVOKE_PARAM_VALUE = new ScxExceptionCode(
    "ERROR_INVOKE_PARAM_VALUE");

    public static final ScxExceptionCode ERROR_INVOKE_TIMEOUT = new ScxExceptionCode(
    "ERROR_INVOKE_TIMEOUT");
    
    public static final ScxExceptionCode ERROR_TRANSFORMING_INVOKE = new ScxExceptionCode(
    "ERROR_TRANSFORMING_INVOKE");
    
    public static final ScxExceptionCode ERROR_INVOKE_TOO_FEW_BEANS = new ScxExceptionCode(
    "ERROR_INVOKE_TOO_FEW_BEANS");
    
    public static final ScxExceptionCode ERROR_INVOKE_PARAM_COUNT_MISMATCH = new ScxExceptionCode(
    "ERROR_INVOKE_PARAM_COUNT_MISMATCH");
    
    public static final ScxExceptionCode ERROR_INVALID_POST_QUERY = new ScxExceptionCode(
    "ERROR_INVALID_POST_QUERY");
    
    public static final ScxExceptionCode ERROR_SIZE_OF_XML_FILES_EXCEED_LIMITS = new ScxExceptionCode(
    "ERROR_SIZE_OF_XML_FILES_EXCEED_LIMITS");

    public static final ScxExceptionCode ERROR_MALFORMED_INVOKE_XML = new ScxExceptionCode(
    "ERROR_MALFORMED_INVOKE_XML");
    
    public static final ScxExceptionCode ERROR_INVOKE_NO_BODY = new ScxExceptionCode(
    "ERROR_INVOKE_NO_BODY");
        
    public static final ScxExceptionCode ERROR_INVOKE_BODY_TOO_LARGE = new ScxExceptionCode(
    "ERROR_INVOKE_BODY_TOO_LARGE");

    public static final ScxExceptionCode ERROR_INVOKE_RESPONSE_TOO_LARGE = new ScxExceptionCode(
    "ERROR_INVOKE_RESPONSE_TOO_LARGE");
    
    public static final ScxExceptionCode ERROR_INVOKE_EXCEPTION = new ScxExceptionCode(
    "ERROR_INVOKE_EXCEPTION");
    
    public static final ScxExceptionCode ERROR_INVOKE_MAXSIZE_PARAM_INVALID = new ScxExceptionCode(
    "ERROR_INVOKE_MAXSIZE_PARAM_INVALID");
    
    public static final ScxExceptionCode ERROR_INVOKE_MAXTIME_PARAM_INVALID = new ScxExceptionCode(
    "ERROR_INVOKE_MAXTIME_PARAM_INVALID");
    
    public static final ScxExceptionCode ERROR_INVOKE_PARAM_EMPTY = new ScxExceptionCode(
    "ERROR_INVOKE_PARAM_EMPTY");
    
    /**
     * <p>
     * Key into the resource file indicating which phrase in the resource file
     * to use.
     * </p>
     */
    private String _code = null;

    /**
     * <p>
     * Default Constructor
     * </p>
     * 
     * @param code
     *         Name of key to look-up in the resource file.
     */
    public ScxExceptionCode(String code) {
        _code = code;
    }

    /**
     * <p>
     * Retrieves string representation of the code
     * </p>
     * 
     * @return String
     */
    public String getCode() {
        return _code;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ScxExceptionCode
                && ((ScxExceptionCode) obj)._code.equals(_code)) {
            result = true;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return _code.hashCode();
    }
}
