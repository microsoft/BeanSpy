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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;

/**
 * <p>
 * Helper class to load and parse the MBean exclusions XML configuration file. 
 * </p>
 * 
 * @author Geoff Erasmus
 */
public class JMXFilterParameters
{
    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    protected ILogger _logger;

    /**
     * <p>
     * Singleton instance of the class.
     * </p>
     */
    static JMXFilterParameters _inst = null;

   /**
    * <p>
    * XML Resources file used to store the MBean exclusions.
    * </p>
    */
    public static final String resourceName = "resources/configuration/JMXQuery.Exclusions.xml";
    
    /**
     * <p>
     * Map containing the hierarchical structure of the exclusions.
     * JMXStore -
     *            MBean
     *                   - Attribute
     *                   - Attribute
     *            MBean
     *                   - Attribute
     * JMXStore -
     *            MBean
     *                   - Attribute
     * </p>
     */
    protected Hashtable<String,Hashtable<String,ArrayList<String>>> JMXStoreMBeanMap;
    
     
    /**
     * <p>
     * Load up the exclusion data from a given file.
     * </p>
     * 
     * @param fileName
     *            Name of a file containing the exclusion data.
     *                  
     * @return 
     *            an XMLConfig class that can be used parse the xml contents.
     *
     * @throws ParserConfigurationException
     *            Indicates a serious configuration error.
     * @throws IOException           
     *            Signals that an I/O exception of some sort has occurred. This class is the 
     *            general class of exceptions produced by failed or interrupted I/O operations.
     * @throws SAXException     
     *            Encapsulate a general SAX error or warning.      
     */
    public XMLConfig loadConfigFromFile(String fileName) throws ParserConfigurationException, IOException, SAXException
    {
        XMLConfig cfg = new XMLConfig();
        cfg.LoadFromFile(resourceName);
        return cfg;
    }
    
    /**
     * <p>
     * Load up the exclusion data from a given XML data string.
     * </p>
     * 
     * @param xmlDocData
     *            Name of a file containing the exclusion data.
     *                  
     * @return 
     *            an XMLConfig class that can be used parse the xml contents.
     *
     * @throws ParserConfigurationException
     *            Indicates a serious configuration error.
     * @throws IOException           
     *            Signals that an I/O exception of some sort has occurred. This class is the 
     *            general class of exceptions produced by failed or interrupted I/O operations.
     * @throws SAXException     
     *            Encapsulate a general SAX error or warning.
     */
    public XMLConfig loadConfigFromData(String xmlDocData) throws ParserConfigurationException, IOException, SAXException
    {
        XMLConfig cfg = new XMLConfig();
        cfg.LoadFromString(xmlDocData);
        return cfg;
    }

    /**
     * <p>
     * Load up the Map containing the relationships between the 
     * JMSStore, MBeans and MBean attributes.
     *      JMXStore 
     *            MBean
     *                   - Attribute
     *                   - Attribute
     *            MBean
     *                   - Attribute
     *      JMXStore 
     *            MBean
     *                   - Attribute
     * </p>
     * 
     * @param xmlConfig
     *            wrapper around a XML Document containg the document to parse.
     */
    public void loadMap(XMLConfig xmlConfig)
    {
        try
        {
            String saveStoreName = "";
            String saveMBeanName = "";
            Hashtable<String,ArrayList<String>> MBeanAttrMap = new Hashtable<String,ArrayList<String>>();
            ArrayList<String> attrs = new ArrayList<String>();

            // Get all Attribute nodes from the XML Doc
            NodeList nl = xmlConfig.getNodes("//Attribute");
            for(int i=0;i<nl.getLength();i++)
            {
                // Get the attributeName from the XML
                String attrName = nl.item(i).getTextContent();

                // Get the ObjectName from the XML
                Node objectName = nl.item(i).getParentNode(); 
                String mbeanName = objectName.getAttributes().getNamedItem("Name").getTextContent();
                
                // Get the JMXStore name from the XML
                Node store = objectName.getParentNode();
                String storeName = store.getAttributes().getNamedItem("Name").getTextContent();
                
                if(saveStoreName.compareTo(storeName) != 0)
                {
                  saveStoreName = storeName;
                  MBeanAttrMap = new Hashtable<String,ArrayList<String>>();
                  JMXStoreMBeanMap.put(storeName, MBeanAttrMap);
                  saveMBeanName = "";
                }
                
                if(saveMBeanName.compareTo(mbeanName) != 0)
                {
                    attrs = new ArrayList<String>();
                    MBeanAttrMap.put(mbeanName, attrs);
                    saveMBeanName = mbeanName;
                }
                
                attrs.add(attrName);
            }
        }
        
        catch(XPathExpressionException e)
        {
            this._logger.fine(new StringBuffer("Error loading Filter parameters from file - XPathExpressionException").toString());
        }
    }

    /**
     * <p>
     * Clear out the Map containing the relationships between the 
     * JMSStore, MBeans and MBean attributes.
     */
    public void clear()
    {
        JMXStoreMBeanMap.clear();
    }

    /**
     * Default Constructor that reads the exclusions file and builds the relationships.
     */
     private JMXFilterParameters() 
     {
         this._logger = LoggingFactory.getLogger();

         JMXStoreMBeanMap = new Hashtable<String,Hashtable<String,ArrayList<String>>>();
         try
         {
             loadMap(loadConfigFromFile(resourceName));
         }    
         catch(SAXException e)
         {
             this._logger.warning(new StringBuffer("Error loading Filter parameters from file.").toString());
         }
         catch(IOException e)
         {
             this._logger.warning(new StringBuffer("Error loading Filter parameters from file.").toString());
         }
         catch(ParserConfigurationException e)
         {
             this._logger.warning(new StringBuffer("Error loading Filter parameters from file - ParserConfigurationException").toString());
         }

     }    

    /**
     * Singleton entry point.
     *
     * @return 
     *            returns an instance of the JMXFilterParameters class
     */
     public static JMXFilterParameters GetInstance()
     {
         if(_inst==null)
         {
             _inst = new JMXFilterParameters();
         }
         return _inst;
     }
     
    /**
     * <p>
     * Retrieve a map containing the MBeans and their related attributes that are to be excluded for the given JMXStore,
     *            MBean
     *                   - Attribute
     *                   - Attribute
     *            MBean
     *                   - Attribute
     * </p>
     * 
     * @param JMXStoreName
     *            the JMXStore for which exclusions are required.
     * @return 
     *            the exclusions for the specified JMXStore. 
     */
     public Hashtable<String,ArrayList<String>> GetJMXStoreExclusions(String JMXStoreName) 
     {
         Hashtable<String,ArrayList<String>> result = new Hashtable<String,ArrayList<String>>();
              
         Hashtable<String,ArrayList<String>> ans = JMXStoreMBeanMap.get(JMXStoreName);
         if(ans!=null)
         {
             result.putAll( ans );
         }
         ans = JMXStoreMBeanMap.get("*");
         if(ans!=null)
         {
             result.putAll( ans );
         }
          
         return result;
     }

    /**
     * <p>
     * Retrieve a list containing the attributes that are to be excluded for the given MBean.
     * </p>
     * 
     * @param exclusionSet
     *            the JMXStore exclusion set, this is the map returned by GetJMXStoreExclusions.
     * @param MBeanName
     *            the MBean for which the exclusions are required.
     * @return 
     *            list containing the attributes that are to be excluded for the given MBean. 
     */
     public ArrayList<String> GetMBeanExclusions(Hashtable<String,ArrayList<String>> exclusionSet, ObjectName MBeanName) 
     {
         ArrayList<String> results = new ArrayList<String>();

         for(Iterator<Entry<String,ArrayList<String>>> it = exclusionSet.entrySet().iterator(); it.hasNext();)
         {
             Entry<String,ArrayList<String>> entry = it.next();
               
             String mbeanName = entry.getKey();
             try
             {
                 if(new ObjectName(mbeanName).apply(MBeanName))
                 {
                     results.addAll(entry.getValue());
                 }    
             }
             catch(MalformedObjectNameException e)
             {
                 this._logger.fine(new StringBuffer("The exclusion MBeanObjectName does not equate to a JMX query : ").append(entry.getKey()).toString());
             }
         }
         return results;
     }
      
    /**
     * Helper method to convert the Attribute list into a map. The map is more efficient at finding entries.
     * 
     * @param attrs
     *            a list of entries.
     * @return 
     *            a map containing the entries in the list. 
     */
     public  Hashtable<String,String> toHashTable(ArrayList<String> attrs) 
     {
        Hashtable<String,String> results = new Hashtable<String,String>(); 
        for(int i=0;i<attrs.size();i++)
        {
            results.put(attrs.get(i),"");
        }
       return results;
     }
}