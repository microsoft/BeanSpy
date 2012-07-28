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

package com.interopbridges.scx.xml;

import org.xml.sax.helpers.AttributesImpl;

import com.interopbridges.scx.util.MsVersion;

/**
 * <p>
 * Common utility class that contains common functionality needed for all of the XML transforms.
 * </p>
 * 
 * @author Christopher Crammond
 *
 */
public class CommonXmlTransform {

    /**
     * <p>
     * Create a common attribute object that can be used at the outermost layer
     * of all the XML returned to OM. Right now, this includes version (which
     * should ideally match the version of System Center Operations Manager)
     * <p>
     * 
     * @return Attribute Object that has all of the necessary attributes
     *         expected at the outermost layer
     */
    public static AttributesImpl getOuterMostAttributes() {
        AttributesImpl baseAttributes = new AttributesImpl();
        baseAttributes.addAttribute("", "", "version", "CDATA",
                MsVersion.VERSION);
        return baseAttributes;
    }
}
