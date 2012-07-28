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

package com.interopbridges.scx.util;

/**
 * <p>
 * Interface defining the constants to be used for:
 * <ol>
 * <li>BUILD_TIME</li>
 * <li>LABEL_NAME</li>
 * <li>VERSION</li>
 * <ol>
 * </p>
 * 
 * <p>
 * Note: this constant may not contain valid information 
 * for a desktop build.
 * </p>
 * 
 * @author Christopher Crammond
 */
public interface MsVersion {

    /**
     * Constant defining the time at which the code was built.  
     */
    public static final String BUILD_DATE = "@BUILD_DATE@";

    /**
     * Constant defining the name of the label the code was built from.
     */
    public static final String LABEL_NAME = "@LABEL_NAME@";

    /**
     * Constant defining the official version of the project. 
     */
    public static final String VERSION = "@VERSION@";

}
