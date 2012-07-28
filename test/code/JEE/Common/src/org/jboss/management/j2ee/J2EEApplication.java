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

package org.jboss.management.j2ee;

/**
 * <p>
 * Mock class for use with the unit tests. There is a need to transforms some
 * MBeans different to XML. For this class, the objectName needs to be sent to
 * OM as both a string (it is the key) as well as parsed out XML as the various
 * properties are of interested individually in OM
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class J2EEApplication implements J2EEApplicationMBean
{
    /**
     * A primitive boolean property representing if this is a eventProvider
     */
    private boolean      _eventProvider;

    /**
     * A primitive boolean property representing if this is a statisticsProvider
     */
    private boolean      _statisticsProvider;

    /**
     * Object Name Domain
     */
    private final String _domain     = "jboss.management.local";

    /**
     * ObjectName's J2EEServer
     */
    private final String _J2EEServer = "Local";

    /**
     * ObjectName's J2EEType
     */
    private final String _j2eeType   = "J2EEApplication";

    /**
     * ObjectName's name
     */
    private final String _name       = "BeanSpy.ear";

    /**
     * A primitive String property needed for XML conversion testing
     */
    private final String _objectName = "jboss.management.local:J2EEServer=Local,j2eeType=J2EEApplication,name=BeanSpy.ear";

    /**
     * Empty Constructor. It is considered to be a best practice to create this
     * default constructor rather than relying on the compiler to auto-generate
     * it.
     */
    public J2EEApplication()
    {
        this._eventProvider = false;
        this._statisticsProvider = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.management.j2ee.J2EEApplicationMBean#getEventProvider()
     */
    public boolean getEventProvider()
    {
        return _eventProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.management.j2ee.J2EEApplicationMBean#getStatisticsProvider()
     */
    public boolean getStatisticsProvider()
    {
        return _statisticsProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.management.j2ee.J2EEApplicationMBean#getObjectName()
     */
    public String getObjectName()
    {
        return _objectName;
    }
    

    /**
     * <p>
     * Helper function for the unit tests
     * </p>
     * 
     * @return the _domain
     */
    public String get_domain()
    {
        return _domain;
    }

    /**
     * <p>
     * Helper function for the unit tests
     * </p>
     * 
     * @return the _J2EEServer
     */
    public String get_J2EEServer()
    {
        return _J2EEServer;
    }

    /**
     * <p>
     * Helper function for the unit tests
     * </p>
     * 
     * @return the _j2eeType
     */
    public String get_j2eeType()
    {
        return _j2eeType;
    }

    /**
     * <p>
     * Helper function for the unit tests
     * </p>
     * 
     * @return the _name
     */
    public String get_name()
    {
        return _name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other)
    {
        boolean returnValue = false;

        if (other instanceof J2EEApplication)
        {
            J2EEApplication recastObject = (J2EEApplication) other;
            returnValue = (this.getEventProvider() == recastObject
                    .getEventProvider())
                    && (this._statisticsProvider == recastObject
                            .getStatisticsProvider())
                    && (this._objectName.equals(recastObject.getObjectName()));
        }

        return returnValue;
    }
}
