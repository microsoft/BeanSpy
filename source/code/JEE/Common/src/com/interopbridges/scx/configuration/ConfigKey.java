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

/**
 * <p>
 * Interface to the resource bundle strings. Each entry in the
 * config.properties file should have a static string here.
 * </p>
 *
 * @author Jinlong Li
 */
public class ConfigKey {
        
    public static final ConfigKey ABS_MAX_XML_SIZE = new ConfigKey("ABS_MAX_XML_SIZE");

      
    /**
     * <p>
     * Key into the resource file indicating which value in the configuration file
     * to use.
     * </p>
     */
    private String _key = null;
   
   /**
     * <p>
     * Private Default Constructor
     * </p>
     * 
     * @param key
     *      a key of a key/value pair in the configuration property file.
     */
    public ConfigKey(String key) 
    {
      _key = key;
    }

    /**
     * <p>
     * Public get method.
     * </p>
     *
     */
    public String getKey() 
    {
      return _key;
    }
}
