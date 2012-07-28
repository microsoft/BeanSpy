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

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import com.interopbridges.scx.util.JmxConstant;

/**
 * <p>
 * Check the URL length and validate the input parameters. 
 * </p>
 * 
 * @author Jinlong Li
 *
 */
public class JmxURLCheck {
    /**
     * <p>
     * This function calculate the length of a complete URL and compare it with the limits of the length of
     * a URL BeanSpy allowed. If the length exceeds the limits, return true, else return false.
     * </p>
     * 
     * <p>
     * A complete URL includes a URL, "?" and a queryString, in which the later two value could be missing
     * if the URL does not have a query string. 
     * </p>
     * 
     * <p>
     * For example,  A complete URL could be:
     * http://www.someCompany.com:8080/BeanSpy/MBean?JMXQuery=WebSphere:j2eeType=J2EEApplication,*&MaxDepth=10
     * </P>
     *
     * @param URL
     *            For example: http://localhost:8080/BeanSpy/MBean. 
     * @param queryString        
     *            For example: JMXQuery=WebSphere:j2eeType=J2EEApplication,*&MaxDepth=10
     * 
     * @return a boolean value indicates whether the total length of a URL exceeds the limits
     * 
     */    
    public static boolean URLLengthExceedsLimit(StringBuffer URL, String queryString)
    {
        /*
         * For production code, a URL should never be null. However, for unit test, it could be null. 
         * So, we add a null string check here for safe guide. 
         */
        if (URL != null)                            
        {
           int URLLength = URL.toString().length();    
                
           if (queryString != null )
           {
             /*
              * Adding 1 for the character of "?" which does not belong to either URL or a query string, 
              * then adding the length of the query string.
              */  
              URLLength += 1 + queryString.length();           
           }
                
           if(URLLength > JmxConstant.URL_LENGTH_LIMITS) 
           { 
              return true;      
           }
        }                  
        return false;  
    }
 
    /** 
     * <p>
     * This function filters all input parameters and returns only valid input parameters 
     * with the corresponding values.
     * </p>
     *
     * @param request
     *         a http servlet request
     * 
     * @return a HashMap contains valid input parameters with their corresponding values.
     * 
     */    
    public static HashMap<String, String[]> getValidInputs(HttpServletRequest request)
    {
         HashMap<String, String[]> Params = new HashMap<String, String[]>();
         String input = null;
         String[] validParms = JmxConstant.getValidParameters();

         for(int i=0; i<validParms.length; i++)
         {
             input = validParms[i];
             String[] values = request.getParameterValues(input);
             
             if (values != null)
             {
                 Params.put(input, values);
             }
         }
         return Params;
    }
}
