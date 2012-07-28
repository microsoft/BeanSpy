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

package com.interopbridges.scx.mbeans;

/**
 * <p>
 * Simple MBean that contains an objectname containing escaped characters. The primary intention of this
 * class is a simple class that contains an objectname with escaped characters. This class is used
 * in the unit tests for verifying that the MBeans can be represented as XML.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class EscapedObjectName implements EscapedObjectNameMBean
{

	 /**
     * The objectname containing escaped characters needed for XML conversion testing
     */
    private final String _objectName = "com.interopbridges.scx:name=com.interopbridges.scx%3aname%3dtest%2cj2eeType%3dJ2EEApplication,size=1";


    /**
     * A primitive String property needed for XML conversion testing
     */
    private final String _theLabel = "EscapedObjectNameType";

    /**
     * Constructor. It creates an instance of a class that will be returned
     * as a data type needed for XML conversion testing
     */
    public EscapedObjectName() 
    {
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.mbeans.EscapedObjectNameMBean#getObjectName()
     */
    public String getObjectName()
    {
        return _objectName;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.mbeans.EscapedObjectNameMBean#getTheLabel()
     */
    public String getTheLabel() 
    {
        return _theLabel;
    }

}
