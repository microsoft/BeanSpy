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

import com.interopbridges.scx.configuration.ConfigBundle;
import com.interopbridges.scx.configuration.ConfigKey;

/**
 * <p>
 * Entry class to get the configuration data from the configuration property file.
 * </p>
 * 
 * <p>
 * Usage: String value = (new Config(ConfigKey.key)).getValue();
 * For example, String value = (new Config(ConfigKey.ABS_MAX_XML_SIZE)).getValue();
 * </p>
 *
 * @author Jinlong Li
 */
public class Config
{
  /**
    * <p>
    * Resources file to store all configuration data (key/value pair).
    * </p>
    */
    public static final String resourceBundleName = "resources.configuration.config";
  
   /**
    * <p>
    * The key of the configuration data in the property file.
    * </p>
    */  
    private ConfigKey _key;    
    
   /**
    * Default Construtor.
    */
    public Config(ConfigKey key) 
    {
        this._key = key;
    }
   
  /**
    * Return the value in the property file give a key.
    */ 
    public String getValue() 
    {
        return ConfigBundle.getBundle(resourceBundleName).getString(_key);
    }
}