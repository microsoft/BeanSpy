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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p>
 * Helper class to load and parse an XML configuration file. 
 * </p>
 * 
 * @author Geoff Erasmus
 */
public class XMLConfig
{
  /**
    * <p>
    * XML Document.
    * </p>
    */
    protected Document _doc;
  
    /**
     * Default Construtor.
     */
    public XMLConfig() 
    {
    }

    /**
     * <p>
     * Load XML data from a file
     * </p>
     * 
     * @param fileName
     *            The name of the file containing valid XML data.
     * 
     * @throws ParserConfigurationException
     *             Thrown if given a bad parser configuration
     * @throws SAXException
     *             If there was an error parsing the XML
     * @throws IOException
     *             If there was an error parsing the XML
     */
    public void LoadFromFile(String fileName) 
        throws ParserConfigurationException, IOException, SAXException
    {
        URL u = this.getClass().getClassLoader().getResource(fileName);

        InputStream is = u.openStream();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        _doc = builder.parse(is);
    }
     
    /**
     * <p>
     * Load XML data from the input string
     * </p>
     * 
     * @param inputXml
     *            an correctly formed XML data string.
     * @throws ParserConfigurationException
     *             Thrown if given a bad parser configuration
     * @throws SAXException
     *             If there was an error parsing the XML
     * @throws IOException
     *             If there was an error parsing the XML
     */
    public void LoadFromString(String inputXml) 
        throws ParserConfigurationException, IOException, SAXException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringReader input = new StringReader(inputXml);
        InputSource inp = new InputSource(input);
        _doc = builder.parse(inp);
    }
      
    /**
     * <p>
     * Return the XML nodes matching the given query.
     * </p>
     * 
     * @param xpathQuery
     *            a valid XML XPath query.
     * @throws XPathExpressionException
     *             Thrown if an illegal XPATH query is given
     */ 
    public NodeList getNodes(String xpathQuery) 
        throws XPathExpressionException
    {
        XPathFactory xfactory = XPathFactory.newInstance();
        XPath xpath = xfactory.newXPath();
        XPathExpression expr = xpath.compile(xpathQuery);
        return _doc==null?null: (NodeList)expr.evaluate(_doc, XPathConstants.NODESET);
    }
}