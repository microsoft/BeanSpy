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
import java.util.Iterator;

/**
 * Move some shared functions from both BeanSpy and MBeanExtender to eliminate duplicate codes.
 *
 * @author Jinlong Li
 */
public class MockHttpRequestHelper {
   
   /**
     * <p>
     * Add key/value pairs to a HashMap.
     * </p> 
	 *
     * @param key
     *            key to be added to the HashMap
     *
     * @param value
     *            value to be added to the HashMap
     *
     * @Params a HashMap holding key/value pairs.
     * 
     * @return a HashMap holding key/value pairs by adding one more key/value pair.
     *
     * @author Jinlong Li
     */

    public static HashMap<String,String[]> addParameter(String key, String value, HashMap<String,String[]> Params)
    {
       if (Params==null)
       {
          Params = new HashMap<String,String[]>();
       }
       String[] str;
       String[] s = Params.get(key);
       if(s!=null)
       {
           str = new String[s.length+1];
           System.arraycopy(s, 0, str, 0, s.length);
           str[s.length] = value;
       }
       else
       {
           str = new String[1];
           str[0] = value;
       }

       Params.put(key, str);
       
       return Params;
    }
    
    /**
     * <p>
     * Reconstruct the query string from input parameters.
     * </p> 
	 *
     * @param Params
     *            a HashMap to held key/value 
	 *
     * @return constructed queryString.
     *
     * @author Jinlong Li
     */
    public static String getQueryString(HashMap<String,String[]> Params) 
    {
        boolean isFirstQueryString = true;
        String QueryString = null;
        String[] value;
        
        if(Params != null)
        {
            QueryString = "";
            Iterator<String> iterator = Params.keySet().iterator();
        
            while(iterator.hasNext()){               
                String key = (String) iterator.next();
                value = Params.get(key);
                
                for (int i =0; i< value.length; i++){
                    if (isFirstQueryString == true)
                    {
                         isFirstQueryString = false;
                    }
                    else
                    {
                      /*
                       *Start with "&" starting from the second query string
                       */
                        QueryString += "&";        
                    }
                    QueryString += key + "=" + value[i];
                }                  
            }
        }
        return QueryString;
    }

}
