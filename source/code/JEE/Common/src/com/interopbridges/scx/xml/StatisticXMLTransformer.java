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

import java.io.StringWriter;
import java.util.Vector;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.jeestats.Statistic;
import com.interopbridges.scx.jeestats.StatisticGroup;
import com.interopbridges.scx.jeestats.StatisticItemGroup;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;


/**
 * <p>
 * Utility class to create Statistics response XML.
 * </p>
 * 
 * <p>
 * This module is primarily created for the J2SE 5 platform using JAXP 1.3 based on the 
 * Apache "Xerces" library. It will work on Java 1.4 however that relies on JAXP 1.1 
 * which will need to be endorsed on the specific platform.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class StatisticXMLTransformer {

    /**
     * <p>
     * XML Element representing the Properties of a single or group of statistics
     * </p>
     */
    public static final String PROPERTIES = "Properties";

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
    public StatisticXMLTransformer() {
        this._logger = LoggingFactory.getLogger();
    };

    
    /**
     * <p>
     * For a given Statistic, transform it into XML.
     * </p>
     * 
     * @param GroupName
     *            The name of the statistic group.
     * @param stat
     *            A Statistic to transform into XML.
     * 
     * @return XML representation of a single Statistic.
     * 
     * @throws ScxException
     *             If there was an error generating the XML.
     */
    public StringWriter transformSingleStatistic(String BaseElementTag, String GroupName, Statistic stat)
            throws ScxException 
    {
        try 
        {
            StringWriter output = new StringWriter();
            TransformerHandler transformer = createXmlDocument(output);
            transformer.startDocument();
            transformer.startElement("", "", BaseElementTag, CommonXmlTransform.getOuterMostAttributes());
            transformer.startElement("", "", GroupName, new AttributesImpl());
            transformer.startElement("", "", PROPERTIES, new AttributesImpl());
            
            this.StatisticToOuterXml(transformer, stat);
            
            transformer.endElement("", "", PROPERTIES);
            transformer.endElement("", "", GroupName);
            transformer.endElement("", "", BaseElementTag);
            transformer.endDocument();

            return output;
        } catch (Exception e) {
            throw new ScxException(ScxExceptionCode.ERROR_TRANSFORMING_STATISTIC, e);
        }
    }

    /**
     * <p>
     * For a given group of Statistics, transform it into XML.
     * </p>
     * 
     * @param stats
     *            An StatisticGroup containing all the statistics for a single group to transform into XML.
     * 
     * @return XML representation of the Statistics.
     * 
     * @throws ScxException
     *             If there was an error generating the XML.
     */
    public StringWriter transformGroupStatistics(String BaseElementTag, StatisticGroup stats) 
            throws ScxException 
    {
        try 
        {
            StringWriter output = new StringWriter();
            TransformerHandler transformer = createXmlDocument(output);
            transformer.startDocument();
            String GroupName = stats.getName();
            transformer.startElement("", "", BaseElementTag, CommonXmlTransform.getOuterMostAttributes());
            
            for (StatisticItemGroup stat : stats.getStatisticItemGroup()) 
            {
                transformer.startElement("", "", GroupName, new AttributesImpl());
                transformer.startElement("", "", PROPERTIES, new AttributesImpl());
                
                for (Statistic onestat : stat.getStatistics()) 
                {
                    this.StatisticToOuterXml(transformer, onestat);
                }
                transformer.endElement("", "", PROPERTIES);
                transformer.endElement("", "", GroupName);
            }
            
            transformer.endElement("", "", BaseElementTag);
            transformer.endDocument();

            return output;
        } catch (Exception e) {
            throw new ScxException(ScxExceptionCode.ERROR_TRANSFORMING_MBEAN, e);
        }
    }

    /**
     * <p>
     * For many groups of Statistics, transform them into XML.
     * </p>
     * 
     * @param stats
     *            Many StatisticGroups to transform into XML.
     * 
     * @return XML representation of the Statistics.
     * 
     * @throws ScxException
     *             If there was an error generating the XML.
     */
    public StringWriter transformAllStatistics(String BaseElementTag, Vector<StatisticGroup> stats) 
            throws ScxException 
    {
        try 
        {
            StringWriter output = new StringWriter();
            TransformerHandler transformer = createXmlDocument(output);
            transformer.startDocument();
            
            transformer.startElement("", "", BaseElementTag, CommonXmlTransform.getOuterMostAttributes());
            
            for(int i=0;i<stats.size();i++)
            {
                StatisticGroup group = stats.get(i);
                for (StatisticItemGroup stat : group.getStatisticItemGroup()) 
                {
                    transformer.startElement("", "", group.getName(), new AttributesImpl());
                    transformer.startElement("", "", PROPERTIES, new AttributesImpl());
                    
                    for (Statistic onestat : stat.getStatistics()) 
                    {
                        this.StatisticToOuterXml(transformer, onestat);
                    }
                    transformer.endElement("", "", PROPERTIES);
                    transformer.endElement("", "", group.getName());
                }
            }
            
            transformer.endElement("", "", BaseElementTag);
            
            transformer.endDocument();

            return output;
        } catch (Exception e) {
            throw new ScxException(ScxExceptionCode.ERROR_TRANSFORMING_MBEAN, e);
        }
    }

    /**
     * <p>
     * Helper method to abstract away the drudgery of getting the XML
     * transformer.
     * </p>
     * 
     * @param output
     *            Output Stream
     * @return XML Transformer for generating XML
     * 
     * @throws TransformerFactoryConfigurationError
     *             If there were problems creating the transformer
     * @throws TransformerConfigurationException
     *             If there were problems creating the transformer
     */
    private TransformerHandler createXmlDocument(StringWriter output)
            throws TransformerFactoryConfigurationError,
            TransformerConfigurationException {
        /*
         * Begin setup of the XML document. Several levels of objects needed to
         * get created so that the XML can be outputted properly (similar to
         * .NET).
         */
        StreamResult result = new StreamResult(output);
        SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
                .newInstance();
        TransformerHandler transformer = tf.newTransformerHandler();
        Transformer serializer = transformer.getTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        serializer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setResult(result);
        return transformer;
    }

    /**
     * <p>
     * Internal method to create XML for a single statistic item
     * </p>
     * 
     * @param transformer
     *            Desired XML parser to use
     * 
     * @param stat
     *            The Statistic to be turned into XML
     * 
     * @throws SAXException
     *             If there was an error generating the XML
     */
    private void StatisticToOuterXml(TransformerHandler transformer,
            Statistic stat) throws SAXException
    {
        AttributesImpl atts = new AttributesImpl();
        String type = stat.getStatisticType().toString();
        String val = stat.getStatisticValue().toString();

        atts.addAttribute("", "", "type", "CDATA", type);
        
        transformer.startElement("", "", stat.getStatisticName(), atts);
        
        this._logger.fine(new StringBuffer("Attribute Type: ").append(type).toString());
        this._logger.fine(new StringBuffer("Attribute Value: ").append(    val ).toString());
        transformer.characters(val.toCharArray(), 0, val.length());

        transformer.endElement("", "", stat.getStatisticName().toString());
    }

}
