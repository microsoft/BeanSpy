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

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.StringWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.configuration.JMXFilterParameters;
import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.util.JmxConstant;
import com.interopbridges.scx.util.StringMangler;

import java.text.MessageFormat;

/**
 * <p>
 * Transforms Java MBeans into XML.
 * </p>
 * 
 * <p>
 * For future work it might be necessary to extract this into an interface and
 * implement a different solution depending on what type of parsers are
 * available for a given application server.
 * </p>
 * 
 * <p>
 * This relies on JAXP 1.1 (which is available WebSphere 7.0 and WebSpehre 6.1
 * with the Feature Pack for Web Services).
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class MBeanTransformer {


    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    protected ILogger _logger;
    private String _JMXQuery;
    
    /**
     * <p>
     * Class array containing the classes that are wrapper classes
     * </p>
     */
    private Class<?>[] lst = {    
                        Boolean.class,
                        Character.class,
                        Byte.class,
                        Short.class,
                        Integer.class,
                        Long.class,
                        Float.class,
                        Double.class,
                        String.class}; 
    /**
     * <p>
     * HashSet containing the classes that are wrapper classes
     * </p>
     */
    private HashSet<Class<?>> wrapperclasses = new HashSet<Class<?>>(Arrays.asList(lst));
    
    
    /**
     * <p>
     * Default Constructor
     * </p>
     */
    public MBeanTransformer() {
        this._logger = LoggingFactory.getLogger();
    };

    /**
     * <p>
     * A public set function for private member _JMXQuery.
     * </p>
     */
    public void setJMXQuery(String JMXQuery)
    {
        this._JMXQuery = JMXQuery;
    }

    /**
     * <p>
     * A public get function for private member _JMXQuery.
     * </p>
     */  
    public String getJMXQuery()
    {
        return this._JMXQuery;
    }

    /**
     * <p>
     * local helper function to retrieve parameters.
     * </p>
     * 
     * @param whichParam
     *               The name of the parameter to retrieve
     *   
     * @param Params
     *            HashMap of parameters passed in to be parsed.
     * 
     * @return integer value of the parameter of the default value if the parameter has not been set
     * 
     */
    private int getParamValue(String whichParam,HashMap<String,String[]> Params)
    {
        int retval=0;
        if(whichParam.compareTo(JmxConstant.STR_MAXDEPTH)==0)
        {
            retval = JmxConstant.MAXDEPTH;
        }
        else if(whichParam.compareTo(JmxConstant.STR_MAXCOUNT)==0)
        {
            retval = JmxConstant.MAXPROPERTIES;
        }
        else if(whichParam.compareTo(JmxConstant.STR_MAXSIZE)==0)
        {
            retval = JmxConstant.MAXXMLSIZE;
        }
        
        if(Params!=null)
        {
            String[] vals = Params.get(whichParam);
            if(vals!=null)
            {
                try
                {
                   retval = Integer.parseInt(vals[0]);
                }
                catch(NumberFormatException e)
                {
                    this._logger.finer("The "+whichParam+" parameter cannot be converted to a number.");
                }
            }
        }
        return retval;
    }

    /**
     * <p>
     * For a given MBean, transform it into XML.
     * </p>
     * 
     * @param mbeanStore
     *            The Mbean store that holds the mbean to transform
     * 
     * @param mbean
     *            MBean to transform into XML.
     * 
     * @param Params
     *            Parameter HashMap specifying MaxDepth, MaxCount, and MaxSize
     * 
     * @return XML representation of the MBeans.
     * 
     * @throws ScxException
     *             If there was an error generating the XML or if there was an
     *             error using introspection/reflection to determine more
     *             details about the MBean.
     */
    public StringWriter transformSingleMBean(IJMX mbeanStore, ObjectInstance mbean, 
            HashMap<String,String[]> Params)
            throws ScxException {
        try {
            StringWriter outputStringWriter = new StringWriter();
            TransformerHandler transformer = XMLDoc.createXmlDocument(outputStringWriter,"UTF-8","no","no");
            transformer.startDocument();
            
            int recursionCountdown = getParamValue(JmxConstant.STR_MAXDEPTH, Params);
            int maxProperties      = getParamValue(JmxConstant.STR_MAXCOUNT, Params);
            int maxBytes           = getParamValue(JmxConstant.STR_MAXSIZE, Params);

            /*
             * Build a Hashtable containing which attributes must be excluded for the given MBean
             * this will be used when processing each attribute.
             */
            JMXFilterParameters filt = JMXFilterParameters.GetInstance();
            Hashtable<String,ArrayList<String>> exclusions = filt.GetJMXStoreExclusions(mbeanStore.getClass().getName());
            Hashtable<String,String> mbeanexclusions = filt.toHashTable(filt.GetMBeanExclusions(exclusions, mbean.getObjectName()));
   
            // If all attributes are to be ignored, then ignore the whole MBean         
            if(mbeanexclusions.get("*") != null)
            {
                this._logger.fine(new StringBuffer("Excluding mbean : ").append(mbean.getObjectName().toString()).toString());
            }
            else
            {
                this.mBeanToOuterXml(mbeanStore, transformer, mbean, new ControlParameters(recursionCountdown, maxProperties, maxBytes, outputStringWriter),
                        mbeanexclusions);
            }
            transformer.endDocument();
            return outputStringWriter;
        } catch (Exception e) {
            throw new ScxException(ScxExceptionCode.ERROR_TRANSFORMING_MBEAN, e);
        }
    }

    /**
     * <p>
     * For a given MBean, transform it into XML.
     * </p>
     * 
     * @param mbeans
     *            Many MBeans to transform into XML.
     * 
     * @return XML representation of the MBean.
     * 
     * @throws ScxException
     *             If there was an error generating the XML or if there was an
     *             error using introspection/reflection to determine more
     *             details about the MBean.
     */
    public StringWriter transformMultipleMBeans(
            HashMap<IJMX, Set<ObjectInstance>> mbeans, HashMap<String,String[]> Params) throws ScxException {
        try {
            StringWriter outputStringWriter = new StringWriter();
            TransformerHandler transformer = XMLDoc.createXmlDocument(outputStringWriter,"UTF-8","no","no");
            transformer.startDocument();
            String elementTag = "MBeans";
            transformer.startElement("", "", elementTag, CommonXmlTransform.getOuterMostAttributes());

            int recursionCountdown = getParamValue(JmxConstant.STR_MAXDEPTH, Params);
            int maxProperties      = getParamValue(JmxConstant.STR_MAXCOUNT, Params);
            int maxBytes           = getParamValue(JmxConstant.STR_MAXSIZE, Params);

            JMXFilterParameters filt = JMXFilterParameters.GetInstance();
            Set<IJMX> ijmx = mbeans.keySet();
            for (IJMX ix : ijmx) 
            {
                /*
                 * Build a Hashtable containing which attributes must be excluded for the given MBean
                 * this will be used when processing each attribute.
                 */
                Hashtable<String,ArrayList<String>> exclusions = filt.GetJMXStoreExclusions(ix.getClass().getName());
                
                Set<ObjectInstance> mbset = mbeans.get(ix);
                for (ObjectInstance mbean : mbset)
                {
                    Hashtable<String,String> mbeanexclusions = filt.toHashTable(filt.GetMBeanExclusions(exclusions, mbean.getObjectName()));

                    // If all attributes are to be ignored, then ignore the whole MBean         
                    if(mbeanexclusions.get("*") != null) 
                    {
                        this._logger.fine(new StringBuffer("Excluding mbean : ").append(mbean.getObjectName().toString()).toString());
                    }
                    else
                    {
                        this.mBeanToOuterXml(ix, transformer, mbean, 
                                new ControlParameters(recursionCountdown, maxProperties, maxBytes, outputStringWriter),
                                mbeanexclusions);
                    }
                }
            }
            transformer.endElement("", "", elementTag);
            transformer.endDocument();
            return outputStringWriter;
        } catch (Exception e) {
            throw new ScxException(ScxExceptionCode.ERROR_TRANSFORMING_MBEAN, e);
        }
    }

    /**
     * <p>
     * Take the given MBean (object) and turn it into XML
     * </p>
     * 
     * @param mbeanStore
     *            The MBean store that holds the MBean to transform
     * @param transformer
     *            Desired XML parser to use
     * @param mbean
     *            The MBean to be turned into XML
     * @param controlParams
     *            ControlParameters for controlling recursion depth, number of items
     *            and size of output.  
     * 
     * @throws IntrospectionException
     *             If there was an error using introspection to understand the
     *             MBeans
     * @throws SAXException
     *             If there was an error generating the XML
     * @throws IllegalAccessException
     *             If there was a security related error to using reflection to
     *             understand the MBean
     * @throws InvocationTargetException
     *             If there an issue related to invoking methods on the MBean
     *             via reflection
     * @throws javax.management.IntrospectionException
     *             If there was an error using introspection to understand the
     *             MBeans
     * @throws AttributeNotFoundException
     *             Thrown if unable to find attribute of MBean using reflection
     * @throws InstanceNotFoundException
     *             When trying to find the attribute of a MBean
     * @throws MBeanException
     *             When trying to find the attribute of a MBean
     * @throws ReflectionException
     *             When trying to find the attribute of a MBean
     */
    private void mBeanToOuterXml(IJMX mbeanStore, TransformerHandler transformer,
            ObjectInstance mbean, ControlParameters controlParams, Hashtable<String,String> mbeanexclusions) throws 
            IntrospectionException, SAXException,
            IllegalAccessException, InvocationTargetException,
            InstanceNotFoundException, AttributeNotFoundException,
            javax.management.IntrospectionException, MBeanException, IOException,
            ReflectionException, ScxException 
     {

        
        String elementTag = mbean.getClassName();
        ObjectName objname = mbean.getObjectName();
        MBeanInfo metadata = mbeanStore.getMBeanInfo(objname);

        AttributesImpl atts = new AttributesImpl(); 
        atts.addAttribute("", "",  JmxConstant.XML_TRANSFORMER_MBEAN_NAME_ATTRRIBUTE, "CDATA", elementTag);
        atts.addAttribute("", "", JmxConstant.OBJECTNAME, "CDATA", MangleObjectName(objname.getCanonicalName()));
        transformer.startElement("", "", JmxConstant.XML_TRANSFORMER_MBEAN_TAG, atts);
        
            
        /*
         * If the MaxDepth parameter is set to Zero then at the top level
         * mbean processing, the RecursionDepthExceeded() is true.
         * We process all mbeans in a special fashion, each mbean class 
         * and its associated objectname is output as an XML element.  
         * <mbean.class.name>objectname</mbean.class.name>
         * The objectname is made up of the domain part a colon and a list of 
         * key/value pairs seperated by commas e.g.
         * "domain:key=value,key1=value1...".
         * If the MaxDepth parameter is greater than zero 
         * we perform in-depth processing of each individual mbean. 
         */
        if(controlParams.RecursionDepthExceeded())
        {
            /*
             * The ( &lt;classname&rt; ) start tag has already been output
             * follow this by the modified objectName, all %xx values in the objectName are replace by 
             * %25xx. This change is to cater for objects that have embedded objects.
             * domain:j2eeType=MBean,name=domain%3atype%3dadaptor%2cname%3dMBeanProxyRemote,J2EEServer=Local
             *
             * The modified values are returned in the XML document and can be sent back in to the servlet as a 
             * XMLQuery parameter.
             */
            this.objectNameToXml(transformer, objname.getCanonicalName(), JmxConstant.OBJECTNAME);
        }
        else
        {
            atts = new AttributesImpl(); 
            transformer.startElement("", "", JmxConstant.PROPERTIES, atts);
            this.propertiesToXml(mbeanStore, transformer, mbean, metadata, controlParams, mbeanexclusions);
            transformer.endElement("", "", JmxConstant.PROPERTIES);
        }
        transformer.endElement("", "", JmxConstant.XML_TRANSFORMER_MBEAN_TAG);
        // Note: Need to add something for methods here.
        
        controlParams.CheckXMLFileSize(_JMXQuery);
    }
    
    /**
     * <p>
     * Helper method to determine whether a class is indeed a wrapper class
     * for a basic type. the IsPrimative method on the class returns false
     * if the class is a primitive wrapper class.
     * </p>
     * 
     * @param obj
     *            The object to be checked
     * @return true or false depending on whether the object is a wrapper class
     * 
     */
    private boolean IsWrapperClass(Object obj)
    {
        return wrapperclasses.contains(obj); 
    }
    
    /**
     * <p>
     * Use the object and meta-data to dynamically add the properties to the
     * XML.
     * </p>
     * 
     * @param mbeanStore
     *               The MBean store that holds the MBean to transform  
     * @param transformer
     *            Desired XML parser to use
     * @param mbean
     *            The MBean to be turned into XML
     * @param metadata
     *            MBean Meta-data
     * @param controlParams
     *            ControlParameters for controlling recursion depth, number of items
     *            and size of output.  
     * 
     * @throws IllegalAccessException
     *             If there was a security related error to using reflection to
     *             understand the MBean
     * @throws InvocationTargetException
     *             If there an issue related to invoking methods on the MBean
     *             via reflection
     * @throws SAXException
     *             If there was an error generating the XML
     * @throws IntrospectionException
     *             If there was an error using introspection to understand the
     *             MBeans
     * @throws AttributeNotFoundException
     *             Thrown if unable to find attribute of MBean using reflection
     * @throws InstanceNotFoundException
     *             When trying to find the attribute of a MBean
     * @throws MBeanException
     *             When trying to find the attribute of a MBean
     * @throws ReflectionException
     *             When trying to find the attribute of a MBean
     * @throws IOException
     *             throw if the given MBean fails an I/O operation
     */
    private void propertiesToXml( IJMX mbeanStore, TransformerHandler transformer,
            ObjectInstance mbean, MBeanInfo metadata, ControlParameters controlParams,
            Hashtable<String,String> mbeanexclusions)
            throws IllegalAccessException, InvocationTargetException,
            SAXException, IntrospectionException, AttributeNotFoundException,
            InstanceNotFoundException, MBeanException, ReflectionException, IOException 
    {
        MBeanAttributeInfo[] propertyList = metadata.getAttributes();

        this._logger.fine(new StringBuffer("Adding Properties for ").append(
                propertyList.length).append(" metadata attributes of Bean.")
                .toString());
        controlParams.decRecursionDepth();        
        for (int i = 0; i < propertyList.length; i++) 
        {
            this._logger.fine(new StringBuffer("Adding Properties #").append(i).toString());
            try 
            {
                Object attribute = mbeanStore.getAttribute(mbean.getObjectName(),propertyList[i].getName());
                if(attribute!=null)
                {
                    if ("objectName".equals(propertyList[i].getName()))
                    {
                        this.objectNameToXml(transformer, (String) attribute, propertyList[i].getName());
                    }
                    else
                    {
                        ProcessItem(transformer, attribute, propertyList[i].getName(), controlParams,mbeanexclusions);
                    }
                }
            }
            catch(java.lang.UnsupportedOperationException e)
            {
                this._logger.finer(new StringBuffer("getAttribute not supported for ")
                            .append(propertyList[i].getName()).toString());
            }
            catch(javax.management.RuntimeMBeanException e)
            {
                this._logger.finer(new StringBuffer("getAttribute runtime exception for ")
                .append(propertyList[i].getName()).toString());
            }
            catch(javax.management.MBeanException e)
            {
                this._logger.finer(new StringBuffer("getAttribute MBean exception for ")
                .append(propertyList[i].getName()).toString());
            }
            catch(javax.management.ReflectionException e)
            {
                this._logger.finer(new StringBuffer("getAttribute reflection exception for ")
                .append(propertyList[i].getName()).toString());
            }
            catch(javax.management.AttributeNotFoundException e)
            {
                this._logger.finer(new StringBuffer("getAttribute attribute not found exception for ")
                .append(propertyList[i].getName()).toString());
            }
            /*
             * The Catch all is here for exceptional circumstances, In Weblogic accessing certain 
             * MBean attribures causes Weblogic specific SecurityExceptions to be thrown.
             * If we cannot access an attribute log the error and continue processing rather that 
             * terminate the MBean processing.
             */
            catch(Exception e)
            {
                this._logger.finer(new StringBuffer("getAttribute exception for ")
                .append(propertyList[i].getName())
                .append(" :")
                .append(e.getMessage()).toString());
            }
        }
    }
    
    /**
     * <p>
     * ObjectName's deserve a special transform to XML. This will break-apart
     * the string of comma-separated name-value-pairs into XML
     * </p>
     * 
     * <p>
     * Ideally, the XML would look like this:<br>
     * 
     * <pre>
     *   &lt;objectName type="java.lang.String"&gt;jboss.management.local:J2EEApplication=null,J2EEServer=Local,j2eeType=WebModule,name=jbossmq-httpil.war&lt;/objectName&gt;
     *     &lt;objectNameElements type="objectName"&gt;
     *     &lt;Domain&gt;jboss.management.local&lt;/Domain&gt;
     *     &lt;J2EEApplication&gt;null&lt;/J2EEApplication&gt;
     *     &lt;J2EEServer&gt;Local&lt;/J2EEServer&gt;
     *     &lt;j2eeType&gt;WebModule&lt;/j2eeType&gt;
     *     &lt;name&gt;jbossmq-httpil.war&lt;/name&gt;
     *   &lt;/objectNameElements&gt;
     * </pre>
     * </p>
     * 
     * @param transformer
     *            Desired XML parser to use
     * 
     * @param propertyValue
     *            The object to be turned into XML
     * 
     * @param propertyName
     *            Name of the Object Property
     * 
     * @throws SAXException
     *             there was an error generating the XML
     */
    private void objectNameToXml(TransformerHandler transformer, String propertyValue, String propertyName)
            throws SAXException
    {
        /*
         * Output the ( &lt;objectName type="java.lang.String"&rt; ) start tag
         * followed by the modified objectName, all %xx values in the objectName are replace by 
         * %25xx. This change is to cater for objects that have embedded objects.
         * domain:j2eeType=MBean,name=domain%3atype%3dadaptor%2cname%3dMBeanProxyRemote,J2EEServer=Local
         *
         * The modified values are returned in the XML document and can be sent back in to the servlet as a 
         * XMLQuery parameter.
         */
        AttributesImpl atts = new AttributesImpl();      
        atts.addAttribute("", "", "type", "CDATA", propertyValue.getClass().getName());
        transformer.startElement("", "", propertyName, atts);
        String text = MangleObjectName(propertyValue.toString());
        transformer.characters(text.toCharArray(), 0, text.length());
        transformer.endElement("", "", propertyName);

        this._logger.finer(new StringBuffer("Generating XML representation of ").append(propertyValue).toString());
        AttributesImpl objectNameAttributes = new AttributesImpl();
        objectNameAttributes.addAttribute("", "", "type", "CDATA", "objectName");
        transformer.startElement("", "", JmxConstant.OBJECTNAME_ELEMENTS, objectNameAttributes);
        AttributesImpl emptyAttributes = new AttributesImpl(); 
        transformer.startElement("", "", JmxConstant.DOMAIN, emptyAttributes);
        int colonLocation = propertyValue.indexOf(":");
        String parsedDomain = propertyValue. substring(0, colonLocation);
        transformer.characters(parsedDomain.toCharArray(), 0, parsedDomain.length());
        transformer.endElement("", "", JmxConstant.DOMAIN);
        String[] objectNamePieces = propertyValue.substring(colonLocation + 1).split(",");
        for (int i = 0; i < objectNamePieces.length; i++)
        {
            String[] nameValuePair = objectNamePieces[i].split("=");
            transformer.startElement("", "", nameValuePair[0], emptyAttributes);
            transformer.characters(nameValuePair[1].toCharArray(), 0, nameValuePair[1].length());
            transformer.endElement("", "", nameValuePair[0]);
        }
        transformer.endElement("", "", JmxConstant.OBJECTNAME_ELEMENTS);
    }

    /**
     * <p>
     * Convert a standard ObjectName to a URL friendly version that can be passes as a URL to BeanSpy.
     * </p>
     * 
     * @param objectName
     *            ObjectName to be made URL friendly
     * 
     * @returns URL friendly ObjectName
     */
    private String MangleObjectName(String objectName)
    {
        String text = objectName == null ? "null" : StringMangler.DecodeForJmx(objectName.toString());
        text = text.replaceAll("%", "%25");
        return text;
    }

    /**
     * <p>
     * Use the property to dynamically add the property values to the
     * XML.
     * If the controlParams.MaxDepth parameter is greater than zero 
     * we perform in-depth processing of each individual property. 
     * If the recursion depth excedes controlParams.MaxDepth then for properties 
     * that are not a base class (int,long, string etc) are not processed in depth. 
     * The value of a non-processed property is the classes toString value.
     * </p>
     * 
     * @param transformer
     *            Desired XML parser to use
     * @param property
     *            The object to be turned into XML
     * @param name
     *            The name of the property to be used in the XML
     * @param controlParams
     *            ControlParameters for controlling recursion depth, number of items
     *            and size of output.  
     * 
     * @throws IllegalAccessException
     *             If there was a security related error to using reflection to
     *             understand the MBean
     * @throws InvocationTargetException
     *             If there an issue related to invoking methods on the MBean
     *             via reflection
     * @throws SAXException
     *             If there was an error generating the XML
     * @throws IntrospectionException
     *             If there was an error using introspection to understand the
     *             MBeans
     */
    private void ProcessItem(TransformerHandler transformer, Object property, 
                     String name, ControlParameters controlParams, Hashtable<String,String> mbeanexclusions)
        throws IllegalAccessException, InvocationTargetException, SAXException, IntrospectionException, ScxException  
    {
        if(mbeanexclusions.get(name)==null)
        {
            AttributesImpl atts = new AttributesImpl();      
            atts.addAttribute("", "", JmxConstant.XML_TRANSFORMER_MBEAN_PROPERTY_NAME_ATTRRIBUTE, "CDATA", name);
            atts.addAttribute("", "", "type", "CDATA", property.getClass().getName());
            this._logger.fine(new StringBuffer("Attribute Type: ").append(property.getClass().getName()).toString());
            transformer.startElement("", "", JmxConstant.XML_TRANSFORMER_MBEAN_PROPERTY_TAG, atts);
    
            controlParams.CheckXMLFileSize(_JMXQuery);
            
            controlParams.decMaxProperties();
            if(controlParams.MaxPropertiesExceeded())
            {
                controlParams.setRecursionDepth(0);
            }          
            
            if (property.getClass().isArray()) 
            {
                this._logger.finer(new StringBuffer("Property is an array class(")
                .append(property.getClass().getCanonicalName())
                .append( ")").toString());
                int length = Array.getLength(property);
                for (int i = 0; i < length; i++) 
                {
                    Object x = Array.get(property, i);
                    if(x!=null)
                    {
                        if(isBaseClass (x))
                        {
                            String text = ((x == null) ? "null" : StringMangler.DecodeForJmx(x.toString()));
                            this._logger.fine(new StringBuffer("Attribute Value: ").append(text).toString());
                        
                            AttributesImpl indexAttribute = new AttributesImpl();
                            indexAttribute.addAttribute("", "", JmxConstant.XML_TRANSFORMER_MBEAN_PROPERTY_NAME_ATTRRIBUTE, "CDATA", name);
                            indexAttribute.addAttribute("", "", "index", "CDATA", String.valueOf(i));
                            transformer.startElement("", "", JmxConstant.XML_TRANSFORMER_MBEAN_PROPERTY_TAG, indexAttribute);
                            transformer.characters(text.toCharArray(), 0, text.length());
                            transformer.endElement("", "", JmxConstant.XML_TRANSFORMER_MBEAN_PROPERTY_TAG);
                        }
                        else
                        {
                            this._logger.fine(new StringBuffer("Array item Attribute is an object: ").append(x).toString());
                            AttributesImpl indexAttribute = new AttributesImpl();
                            indexAttribute.addAttribute("", "", JmxConstant.XML_TRANSFORMER_MBEAN_PROPERTY_NAME_ATTRRIBUTE, "CDATA", name);
                            indexAttribute.addAttribute("", "", "index", "CDATA", String.valueOf(i));
                            transformer.startElement("", "", JmxConstant.XML_TRANSFORMER_MBEAN_PROPERTY_TAG, indexAttribute);
                        
                            if(controlParams.RecursionDepthExceededForChild())
                            {
                                this._logger.fine(new StringBuffer("Recursion depth exceeded").toString());
                                String text = x == null ? "null" : StringMangler.DecodeForJmx(x.toString());
                                transformer.characters(text.toCharArray(), 0, text.length());
                            }
                            else
                            {
                                controlParams.decRecursionDepth();
                                this.ProcessUserClass( transformer, x, controlParams, mbeanexclusions);
                                controlParams.incRecursionDepth();
                            }
                            transformer.endElement("", "", JmxConstant.XML_TRANSFORMER_MBEAN_PROPERTY_TAG);
                        }
                    }
                    else
                    {
                        this._logger.fine(new StringBuffer("Array element for array [").append(property.getClass().getCanonicalName()).append("] is NULL").toString());
                    }
                }
            } 
            else 
            {
                if(isBaseClass (property))
                {            
                    this._logger.finer(new StringBuffer("Property is a base type class(")
                                .append(property.getClass().getCanonicalName())
                                .append( ")").toString());
                    String text = property == null ? "null" : StringMangler.DecodeForJmx(property.toString());
                    this._logger.fine(new StringBuffer("Attribute Value: ").append(text).toString());
                    transformer.characters(text.toCharArray(), 0, text.length());
                }
                else
                {
                    this._logger.finer("Attribute is an object.");
                    this._logger.fine(new StringBuffer(
                                    "Found attribute of type some other type (")
                                    .append(property.getClass().getCanonicalName())
                                    .append( "), assuming this is a Bean and will generate XML as such.")
                                    .toString());
                    if(controlParams.RecursionDepthExceeded())
                    {
                        this._logger.fine(new StringBuffer("Recursion depth exceeded").toString());
                        String text = property == null ? "null" : StringMangler.DecodeForJmx(property.toString());
                        transformer.characters(text.toCharArray(), 0, text.length());
                    }
                    else
                    {
                        controlParams.decRecursionDepth();
                        this.ProcessUserClass( transformer, property, controlParams, mbeanexclusions);
                        controlParams.incRecursionDepth();
                    }
                }
            }

            transformer.endElement("", "", JmxConstant.XML_TRANSFORMER_MBEAN_PROPERTY_TAG);
        }
        else
        {
            this._logger.fine(new StringBuffer("Excluding property : ").append(name).toString());
        }
    }
    
    /**
     * <p>
     * Use the object ( class object ) to dynamically add the properties to the
     * XML.
     * </p>
     * 
     * @param transformer
     *            Desired XML parser to use
     * @param mbean
     *            The MBean to be turned into XML
     * @param controlParams
     *            ControlParameters for controlling recursion depth, number of items
     *            and size of output.  
     * 
     * @throws IllegalAccessException
     *             If there was a security related error to using reflection to
     *             understand the MBean
     * @throws InvocationTargetException
     *             If there an issue related to invoking methods on the MBean
     *             via reflection
     * @throws SAXException
     *             If there was an error generating the XML
     * @throws IntrospectionException
     *             If there was an error using introspection to understand the
     *             MBeans
     */
    
    private void ProcessUserClass( TransformerHandler transformer,
            Object mbean, ControlParameters controlParams, Hashtable<String,String> mbeanexclusions) 
            throws IllegalAccessException, InvocationTargetException, 
                   SAXException, IntrospectionException, ScxException 
    {
        BeanInfo metadata = Introspector.getBeanInfo(mbean.getClass(), Object.class);
        PropertyDescriptor[] propertyList = metadata.getPropertyDescriptors();

        for (int i = 0; i < propertyList.length; i++) 
        {
            this._logger.fine(new StringBuffer("Adding Properties #").append(i).toString());
            
            Method meth = propertyList[i].getReadMethod();
            if(meth!=null) 
            {
                try {
                    Object property = meth.invoke(mbean,(Object[]) null);
                    if(property!=null)
                    {
                        ProcessItem(transformer, property,propertyList[i].getName(), 
                                controlParams, mbeanexclusions);                                                 
                    }
                }
                catch (IllegalAccessException e)
                {
                    this._logger.finer("Unable to invoke ReadMethod. IllegalAccessException");
                }
                catch(InvocationTargetException e)
                {
                    this._logger.finer("Unable to invoke ReadMethod. InvocationTargetException");
                }
            }
            else
            {
                /*
                 * Certain properties are of IndexedPropertyDescriptor type, this means they act like arrays
                 * and they support indexed read and write methods. These are not currently supported.
                 */
                if(propertyList[i] instanceof IndexedPropertyDescriptor)
                {
                    this._logger.finer(new StringBuffer("MBean property [")
                                            .append(propertyList[i].getName())
                                            .append("], does not support a read method.")
                                            .toString());
                }
                else
                {
                    this._logger.finer(new StringBuffer("MBean property [")
                                            .append(propertyList[i].getName())
                                            .append("], does not support a read method and is not an IndexedProperty.")
                                            .toString());
                }
            }
        }
    }

    /**
     * <p>
     * Helper method to determine whether an object is either a wrapper class or a primitive
     * type.
     * </p>
     * 
     * @param prop
     *            The object to be checked
     * @return true or false depending on whether the object is a wrapper class or primitive class
     * 
     */
    protected boolean isBaseClass (Object prop)
    {
        if ((prop.getClass().isPrimitive()) || (IsWrapperClass(prop.getClass()))) 
        {
            return true;
        } 
        return false;
    }
}

/**
 * <p>
 * Utility class to manage and control the parameters for XML processing.
 * </p>
 */
class ControlParameters
{
    /**
     * <p>
     * Element representing the desired original recursion depth.
     * </p>
     */
    protected int origRecursionDepth;
    
    /**
     * <p>
     * Element representing the original maximum number of properties 
     * to be processed for a MBean, when this threshold is reached
     * the recursion depth is set to zero and no further in depth processing occurs.
     * </p>
     */
    protected int origMaxProperties;
    
    /**
     * <p>
     * Element representing the original maximum number of bytes to be 
     * produced by the XML output. This is not a hard limit as the rest of the MBean still 
     * needs to be processed. When this threshold is reached 
     * the recursion depth is set to zero and no further in depth processing occurs.
     * </p>
     */
    protected int origMaxXMLSize;
    
    /**
     * <p>
     * Element representing the current recursion depth.
     * </p>
     */
    protected int RecursionDepth;
    
    /**
     * <p>
     * Element representing the current number of properties processed for a single MBean.
     * </p>
     */
    protected int MaxProperties;
    
    /**
     * <p>
     * Element representing the output string writer used to output the XML.
     * </p>
     */
    protected StringWriter outputStringWriter;
    
    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    protected ILogger _logger;
      
    
    /**
     * <p>
     * Default Constructor, used to initialize the default values for MBean processing
     * </p>
     * 
     * @param RecursionDepth
     *            Desired maximum recursion depth.
     * @param MaxProperties
     *            Desired maximum properties to process per MBean.
     * @param MaxXMLSize
     *            Desired maximum XML output size.
     * @param outputStringWriter
     *            The output string writer for the XML output.
     */
    public ControlParameters (int RecursionDepth, int MaxProperties, int MaxXMLSize, StringWriter outputStringWriter)
    {
       this.RecursionDepth  = this.origRecursionDepth  = RecursionDepth; 
       this.MaxProperties   = this.origMaxProperties   = MaxProperties;
       this.origMaxXMLSize  = MaxXMLSize;
       this.outputStringWriter = outputStringWriter;
       
       this._logger = LoggingFactory.getLogger();
    }
    
    /**
     * <p>
     * Used to check whether the recursion depth has been exceeded.
     * </p>
     * 
     * @return true if the recursion depth has been exceeded otherwise false
     */
    public boolean RecursionDepthExceeded ()
    {
       if(RecursionDepth<=0)
       {
           return true;
       }
       return false;
    }

    /**
     * <p>
     * Check if the recursion depth will be exceeded for a child element
     * </p>
     * 
     * @return true if the recursion depth has been exceeded otherwise false
     */
    public boolean RecursionDepthExceededForChild ()
    {
    	return RecursionDepth - 1 <= 0; 
    }
    
    /**
     * <p>
     * A utility function to check whether the size of the XML output exceeds a limits.
     * </p>
     * 
     * @return true if XML File size exceeds the limits otherwise false.
     */
    public boolean XMLFileSizeExceeds (int Limits)
    {
       StringBuffer XMLFileBuffer = outputStringWriter.getBuffer();
       
       if (XMLFileBuffer != null)
       {
          return XMLFileBuffer.length() > Limits;
       }
      else
      {
          return false;
      }
    }
    
    /**
     * <p>
     * This function checks whether the size of the XML output has exceeded the configured level or 
     * the absolute maximum size of the XML File.
     * <p>
     * 
     * <p>
     * If a configured size is exceeded but have not reach the absolute maximum size,
     * BeanSpy continue to proceed but reset the MaxDepth to minimum which is 0.
     * </p>
     * 
     * <p>
     * If the XML file size reach the absolute maximum limits of the XML file size, 
     * for security reasons, BeanSpy terminates and throws an exception.
     * </p>
     * 
     */   
 public void CheckXMLFileSize(String JMXQuery) throws ScxException
    {
          if(XMLFileSizeExceeds(origMaxXMLSize))
          {
             if(XMLFileSizeExceeds(JmxConstant.ABS_MAX_XML_SIZE))
             {  
                Object[] args = {new Integer(JmxConstant.ABS_MAX_XML_SIZE), JMXQuery}; 
                           
                this._logger.finer(MessageFormat.format( "The size of the XML response has reached the limits of {0} bytes by the query: {1}.", args ) ) ;        
                throw new ScxException(ScxExceptionCode.ERROR_SIZE_OF_XML_FILES_EXCEED_LIMITS, args);
             }
             else
             {
                setRecursionDepth(0);
             }
          }
    }
    
    /**
     * <p>
     * Used to check whether the number of properties has exceeded the configured level.
     * </p>
     * 
     * @return true if the number of properties is greater that the configured level otherwise false
     */
    public boolean MaxPropertiesExceeded ()
    {
       if(MaxProperties<=0)
       {
           return true;
       }
       return false;
    }
    
    /**
     * <p>
     * Used to decrement the recursion depth.
     * The recursion depth decrements for each increasing recursion level.
     * i.e. when the recursion level reaches 0 then the limit is reached.
     * </p>
     */
    public void decRecursionDepth()
    {
       RecursionDepth--;
    }
    
    /**
     * <p>
     * Used to set the recursion level to a specified value.
     * </p>
     * 
     * @param newDepth
     *            The new depth of the recursion counter.
     */
    public void setRecursionDepth(int newDepth)
    {
       RecursionDepth = newDepth;
    }
    
    /**
     * <p>
     * Used to increment the recursion depth.
     * The recursion depth increases when returning from a recursive call.
     * </p>
     */
    public void incRecursionDepth()
    {
       RecursionDepth++;
    }
    
    /**
     * <p>
     * Used to decrement the number of properties available to be processed.
     * </p>
     */
    public void decMaxProperties()
    {
       MaxProperties--;
    }
    
   
}
