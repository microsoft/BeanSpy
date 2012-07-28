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

package com.interopbridges.scx.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.util.MsVersion;

/**
 * <p>
 * Context listener that will attempt to connect to the various JMX stores at
 * start-up.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class JmxStoreRegistration implements ServletContextListener {
    private static ILogger theLogger = LoggingFactory.getLogger();

    /*
     * (non-Javadoc)
     * 
     * @seejavax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        theLogger.info("contextDestroyed: clearing registered JMX stores");
        JmxStores.clearListOfJmxStores();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
     * .ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // Should display a message similar to (where strings wrapped with 
        // @'s are replaced at build-time with something meaningful):
        //
        // INFO: Initializing BeanSpy (Build: @VERSION@, Label:@LABEL_NAME@, 
        // BuildDate:@BUILD_DATE@)
        
        theLogger.info(new StringBuffer("Initializing BeanSpy (Build: ")
                .append(MsVersion.VERSION).append(", Label: ").append(
                        MsVersion.LABEL_NAME).append(", BuildDate: ").append(
                        MsVersion.BUILD_DATE).append(")").toString());
        theLogger.info("contextInitialized: connecting to JMX Stores");
        JmxStores.connectToJmxStores();
    }

}
