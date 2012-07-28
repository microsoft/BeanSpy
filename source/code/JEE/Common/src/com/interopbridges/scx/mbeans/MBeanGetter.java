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

import java.io.StringWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.xml.MBeanTransformer;

/**
 * <p>
 * Retrieves MBeans from the JMX Store and transforms them to XML.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class MBeanGetter {

    /**
     * <p>
     * Abstraction representing the JMX Store
     * </p>
     */
    protected List<IJMX> _jmxStores;

    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    protected ILogger _logger;
    
    /**
     * <p>
     * Default Constructor
     * </p>
     */
    public MBeanGetter(List<IJMX> jmxStores) {
        this._jmxStores = jmxStores;
        this._logger = LoggingFactory.getLogger();
    }

    /**
     * <p>
     * For a given mbean, generate the appropriate XML. This method is an
     * abstraction/wrapper for generating the XML.
     * </p>
     * 
     * @param jmxQuery
     *            JMX Query string
     * @param mbean
     *            List of MBeans to tranform into XML
     * 
     * @return Stream containing a XML representation of the MBean
     * 
     * @throws ScxException
     *             If there was an inspecting the MBean or if there was an error
     *             generating the XML
     */
    private StringWriter generateXmlforMBeans(String jmxQuery, HashMap<IJMX, Set<ObjectInstance>> mbeans, HashMap<String,String[]> Params)
            throws ScxException 
    {       
        MBeanTransformer mtf = new MBeanTransformer();
        
       /*
        * pass _JMXQuery to MBeanTransformer.java.
        */
        mtf.setJMXQuery(jmxQuery); 
               
        return mtf.transformMultipleMBeans(mbeans, Params);
    }

    /**
     * <p>
     * Return a set of MBeans that match the given query.
     * </p>
     * 
     * @param name
     *            Name
     * 
     * @param query
     *            A JMX Query
     * 
     * 
     * @return List of matching MBeans which match the given query
     */
    public List<ObjectName> getAllMatchingMBeans(ObjectName name, QueryExp query) 
            throws IOException
    {
        List<ObjectName> ret = new ArrayList<ObjectName>();
        for(int i=0;i<this._jmxStores.size();i++)
        {
            ret.addAll(this._jmxStores.get(i).queryNames(name, query));
        }
        return ret;
    }

    /**
     * <p>
     * Get a XML representation of the the MBeans that match the given JMX Type.
     * </p>
     * 
     * @param jmxQuery
     *            JMX Query
     * 
     * @throws ScxException
     *             If there was a problem getting the MBean, inspecting it, or
     *             transforming it to XML. For more details review the inner
     *             exception.
     * 
     */
    public StringWriter getMBeansAsXml(String jmxQuery,HashMap<String,String[]> Params) throws ScxException, IOException {

        try {
            StringWriter xmlResponse = new StringWriter();
            
            HashMap<IJMX, Set<ObjectInstance>> mbeans = getMBeans(jmxQuery);

            xmlResponse.append(this.generateXmlforMBeans(jmxQuery,mbeans,Params).getBuffer());

            this._logger.finest(new StringBuffer("Resulting XML for query: ")
                    .append(xmlResponse.toString()).toString());

            return xmlResponse;
        } catch (NullPointerException npe) {
            /*
             * The only declared method to throw this exception is the creation
             * of the ObjectName. which cannot happen (at least as of this
             * writing) due to constructor having a string built in its
             * argument.
             */

            throw new ScxException(ScxExceptionCode.NULL_POINTER_EXCEPTION, npe);
        }
    }

    /**
     * <p>
     * Get all MBeans that have an Objectname matching the input parameter.
     * All relevant JMX stores are checked for the matching MBeans. 
     * </p>
     * 
     * @param objectName
     *            JMX Query
     * @return Map containing all matching MBeans and their associated MBean stores.
     * 
     * @throws ScxException
     *             If there was a problem getting the MBean, inspecting it, or
     *             transforming it to XML. For more details review the inner
     *             exception.
     * @throws MalformedObjectNameException            
     *             The format of the string does not correspond to a valid ObjectName.
     */
    public HashMap<IJMX, Set<ObjectInstance>> getMBeans(String objectName) 
            throws  ScxException 
    {
        int TotalMBeanCount=0;
        HashMap<IJMX, Set<ObjectInstance>> mbeans = new HashMap<IJMX,Set<ObjectInstance>>();
        
        this._logger.finer(new StringBuffer("Executing query for MBeans: ")
                .append(objectName).toString());
        
        ObjectName objName;
        try 
        {
            // Try and use the String constructor of the ObjectName
            // this will fail if a property has embedded quotes in a property value
            // This JMXQuery will cause a failure - [interopbridges:Age=42,Name=Test"MBean's"]
            objName =  new ObjectName(objectName);
        }
        catch (MalformedObjectNameException e) 
        {
            /*
             * The argument to the ObjectName constructor
             * contains illegal characters
             */
            try
            {
                // Try and use the key value pair constructor of the ObjectName
                // this will fail if a property has wildcard values
                // This JMXQuery will cause a failure - [interopbridges:Name=Test"MBean's",*]
                // If the complete MBeans ObjectName is used with no wildcard this will work correctly
                // [interopbridges:Name=Test"MBean's",Age=42]
                String dom = GetDomain (objectName);
                Hashtable<String, String> keyVal = SplitStringObjectNameToTokens (objectName);
                objName = new ObjectName(dom, keyVal);
            }
            catch (MalformedObjectNameException ee)
            {
                /*
                 * At this point we have tried the String based constructor and the 
                 * hashtable based constructor and both have failed, we throw an 
                 * exception and don't process the request.
                 */
                throw new ScxException(ScxExceptionCode.MALFORMED_OBJECT_NAME, ee);
            }
            catch (NullPointerException npe) 
            {
                /*
                 * One of the parameters passed into the ObjectName constructor is null.
                 */
                throw new ScxException(ScxExceptionCode.NULL_POINTER_EXCEPTION, npe);
            }
        } 
        catch (NullPointerException npe) 
        {
            /*
             * The string passed into the ObjectName constructor is null.
             */
            throw new ScxException(ScxExceptionCode.NULL_POINTER_EXCEPTION, npe);
        }
        
        try 
        {
            for(int i=0;i<this._jmxStores.size();i++)
            {
                this._logger.fine(new StringBuffer("Query mbean store: ")
                .append(this._jmxStores.get(i).getClass().getName()).toString());
                
                Set<ObjectInstance> theBeans = this._jmxStores.get(i).queryMBeans( objName, null);
                
                if(theBeans.size()>0)
                {
                    mbeans.put(this._jmxStores.get(i), theBeans );
                }
                TotalMBeanCount += theBeans.size(); 
            }
            
            this._logger.finer(new StringBuffer("Found ").append(TotalMBeanCount)
                    .append(" MBeans matching the ObjectName '").append(
                            objectName).append("'").toString());

        }  catch (IOException ioe) {
            /*
             * The only declared method to throw this exception is queryMBeans
             * call
             */
            throw new ScxException(ScxExceptionCode.IO_ERROR_EXCEPTION, ioe);
        }
        return mbeans;
    }
 
    /**
     * <p>
     * Get the domain part of the JMX Query. 
     * </p>
     * 
     * @param objName
     *            JMX Query
     * @return String containing the domain part of the JMX Query.
     */
    private String GetDomain (String objName)
    {
        String retval="";
        int pos = objName.indexOf(':');
        if(pos>=0)
        {
            retval = objName.substring(0, pos);
        }   
        return(retval);
    }
    
    /**
     * <p>
     * Split up the JMX Query into <key><value> pairs. 
     * </p>
     * 
     * @param objName
     *            JMX Query
     * @return Table containing Key, Value pairs for each property in the MBean objectname.
     * 
     */
    private Hashtable<String, String> SplitStringObjectNameToTokens (String objName)
    {
        Hashtable<String, String> result = new Hashtable<String, String>();
        boolean inDoubleQuotes=false;
        int pos;

        // strip off the Domain portion
        pos = objName.indexOf(':');
        String properties = objName.substring(pos+1);
        char[] name_chars = properties.toCharArray();
        
        //Split string on ',' but only if not quoted
        pos = 0;
        for(int ix=0;ix<name_chars.length;ix++)
        {
            switch(name_chars[ix])
            {
            case '"' :
                inDoubleQuotes = !inDoubleQuotes;
                break;
            case ',' :
                if(!inDoubleQuotes)
                {
                    SplitAndAddValuePair(result, properties.substring(pos,ix));
                    pos=ix+1;
                }
                break;
            }
        }
        if(!inDoubleQuotes)
        {
            SplitAndAddValuePair(result, properties.substring(pos));
        }
        return result;
    }
    
    /**
     * <p>
     * Split a Key=Value pair into separate components and add it to a Hashtable. 
     * </p>
     * 
     * @param table
     *            Output Hashtable containing the Key,Value pair.
     * @param keyValPair
     *            String containing the Key,Value pair to be split.
     */
    private void SplitAndAddValuePair(Hashtable<String, String> table, String keyValPair)
    {
        int pos = keyValPair.indexOf('=');
        if(pos>=0)
        {
            String key = keyValPair.substring(0,pos);
            String val = keyValPair.substring(pos+1);
            table.put(key,val);
        }
        else
        {
            table.put(keyValPair,"");
        }
    }
    
}
