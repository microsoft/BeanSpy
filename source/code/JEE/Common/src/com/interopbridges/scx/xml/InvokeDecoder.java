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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.mbeans.MBeanMethodParameter;
import com.interopbridges.scx.util.JmxConstant;

/**
 * <p>
 * Class to decode the XML input from an Method invocation servlet request 
 * </p>
 * 
 * @author Geoff Erasmus
 */
public class InvokeDecoder 
{
    /**
     * <p>
     * Raw XML string containing the XML request
     * </p>
     */
    private String RawInputData;
    
    /**
     * <p>
     * MBean object name representing the MBean to be acted upon
     * </p>
     */
    private String BeanObjectName;
    
    /**
     * <p>
     * Name of the MBean method to invoke
     * </p>
     */
    private String MethodName;
    
    /**
     * <p>
     * The parameters to the MBean method
     * </p>
     */
    private ArrayList<MBeanMethodParameter> MethodParams;
    
    /**
     * <p>
     * Default Constructor, receives the input XML and the size of the XML data.
     * </p>
     * 
     * @param inputBuffer
     *            input data source of the XML data
     * @param ExpectedInputLen
     *            expected length of data read from the input data source
     * 
     * @throws ScxException
     *             If there was an error reading the XML data from the input data source
     *             or the Expected length is not within bounds
     */
    public InvokeDecoder(BufferedReader inputBuffer, int ExpectedInputLen) throws ScxException
    {
        
        if((ExpectedInputLen <= 0) || (inputBuffer==null))
        {
            throw new ScxException(ScxExceptionCode.ERROR_INVOKE_NO_BODY);
        }
        
        if(ExpectedInputLen > JmxConstant.MAX_POST_INPUT_XML_SIZE)
        {
            throw new ScxException(ScxExceptionCode.ERROR_INVOKE_BODY_TOO_LARGE);
        }

        try
        {
            char[] inp = new char[ExpectedInputLen];
            if(inputBuffer.read(inp,0, ExpectedInputLen) == ExpectedInputLen)
            {
                RawInputData = new String(inp);
            }
            else
            {
                throw new ScxException(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML);
            }
        }
        catch(IOException e)
        {
            throw new ScxException(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML);
        }
    }

    /**
     * <p>
     * Decode the XML input data into its relevant parts.
     * </p>
     * 
     * @throws ScxException
     *             If there was an error decoding the XML data
     */
    public void DecodeInput() throws ScxException
    {
        try
        {
            NodeList nl = Domparser(RawInputData, JmxConstant.STR_POST_XML_INVOKE);
            if(nl.getLength()!=1)
            {
                throw new ScxException(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML);
            }
            
            nl = Domparser(RawInputData, JmxConstant.STR_POST_XML_BEANOBJECTNAME);
            if(nl.getLength()!=1)
            {
                throw new ScxException(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML);
            }
            BeanObjectName = nl.item(0).getTextContent();
            
            nl = Domparser(RawInputData, JmxConstant.STR_POST_XML_METHOD);
            if(nl.getLength()!=1)
            {
                throw new ScxException(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML);
            }
            NamedNodeMap nnm = nl.item(0).getAttributes();
            MethodName = nnm.getNamedItem(JmxConstant.STR_METHOD_NAME_ATTRIBUTE).getNodeValue();
            
                
            NodeList ParamNodeList = nl.item(0).getChildNodes();
            MethodParams = new ArrayList<MBeanMethodParameter>();

            // for each method parameter
            // add it to the MethodParams list.
            for(int j=0;j<ParamNodeList.getLength();j++)
            {
                //Ignore text nodes e.g. '\n'
                if(ParamNodeList.item(j).getNodeType() != Node.TEXT_NODE)
                {
                    if(ParamNodeList.item(j).getNodeName().equals(JmxConstant.STR_POST_XML_PARAM))
                    {
                        NamedNodeMap ParamMap = ParamNodeList.item(j).getAttributes();
                        Node name = ParamMap.getNamedItem(JmxConstant.STR_PARAM_NAME_ATTRIBUTE);
                        Node type = ParamMap.getNamedItem(JmxConstant.STR_PARAM_TYPE_ATTRIBUTE);
                        if(type==null)
                        {
                            throw new ScxException(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML);
                        }
                        
                        MethodParams.add(new MBeanMethodParameter(
                                         name==null?"":name.getNodeValue(), 
                                         type.getNodeValue(), 
                                         ParamNodeList.item(j).getTextContent()));
                    }
                }
            }
        }
        catch(SAXException e)
        {
            throw new ScxException(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML);
        }
        catch(ParserConfigurationException e)
        {
            throw new ScxException(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML);
        }
        catch(XPathExpressionException e)
        {
            throw new ScxException(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML);
        }
        catch(IOException e)
        {
            throw new ScxException(ScxExceptionCode.ERROR_MALFORMED_INVOKE_XML);
        }
    }

    /**
     * <p>
     * Helper method to retrieve the raw XML data
     * </p>
     */
    public String getRawInputData()
    {
        return RawInputData;
    }
    
    /**
     * <p>
     * Helper method to retrieve the decoded ObjectName specifying an MBean
     * </p>
     */
    public String getBeanObjectName()
    {
        return BeanObjectName;
    }
    
    /**
     * <p>
     * Helper method to retrieve the name of the method to invoke
     * </p>
     */
    public String getMethodName()
    {
        return MethodName; 
    }
    
    /**
     * <p>
     * Helper method to retrieve the parameters for the method to invoke
     * </p>
     */
    public ArrayList<MBeanMethodParameter> getMethodParams()
    {
        return MethodParams;
    }
    
    /**
     * <p>
     * Helper DOM parser to retrieve data from an XML document
     * </p>
     * 
     * @param inputXml
     *              input XML document to parse
     * @param xpathQuery
     *              XPath query for the document
     * 
     * @throws XPathExpressionException 
     *              represents an error in an XPath expression.
     * @throws ParserConfigurationException             
     *              Indicates a serious configuration error. 
     * @throws SAXException             
     *              Encapsulate a general SAX error or warning
     * @throws IOException
     *              Signals that an I/O exception of some sort has occurred.
     */
    static NodeList Domparser(String inputXml, String xpathQuery)
            throws XPathExpressionException, ParserConfigurationException,
            SAXException, IOException 
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringReader input = new StringReader(inputXml);
        Document doc = builder.parse(new InputSource(input));
        XPathFactory xfactory = XPathFactory.newInstance();
        XPath xpath = xfactory.newXPath();
        XPathExpression expr = xpath.compile(xpathQuery);
    
        NodeList results = (NodeList) expr
                .evaluate(doc, XPathConstants.NODESET);
        return results;
    }
}