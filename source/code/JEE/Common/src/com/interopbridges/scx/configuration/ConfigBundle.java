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

package com.interopbridges.scx.configuration;

import java.util.ResourceBundle;

/**
 * <p>
 * Class and Factor methods for getting access to a Resource Bundler for
 * configuration data.
 * </p>
 *
 * @author Jinlong Li
 */
public class ConfigBundle {

    /**
     * <p>
     * Handle to the resource bundle.
     * </p>
     */
    private ResourceBundle _bundle = null;
    
    /**
     * <p>
     * Private Default Constructor
     * </p>
     * 
     * @param bundle
     *            Java bundle to use
     */
    private ConfigBundle(ResourceBundle bundle) {
        _bundle = bundle;
    }
    
    /**
     * <p>
     * Static method for retrieving a bundle at the given input.
     * </p>
     * 
     * @param propertyFilename
     *         String containing a full class-path to the configuration property file. 
     * 
     * @return Reference to the configuration bundle specified by the input.
     *         Run-time exceptions are thrown if the file cannot be found.
     */
    public static ConfigBundle getBundle(String propertyFilename) {
        return new ConfigBundle(ResourceBundle.getBundle(propertyFilename));
    }

    /**
     * <p>
     * Get the value for a given key.
     * </p
     * 
     * @param key
     *            A key to look-up in the configuration file.
     * 
     * @return the valu as specified by the input parameters.
     * 
     */
    public String getString(ConfigKey key){
        return _bundle.getString(key.getKey());
    }
}
