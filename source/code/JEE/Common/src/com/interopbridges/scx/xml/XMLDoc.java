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

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

/**
 * <p>
 * Helper class for creating and XML document
 * transformer.
 * </p>
 */
public class XMLDoc {

    /**
     * <p>
     * Helper method to abstract away the drudgery of getting the XML
     * transformer.
     * </p>
     * 
     * @param output
     *            Output Stream
     * @param ENCODING
     *            Output property for ENCODING
     * @param OMIT_XML_DECLARATION
     *            Output property for OMIT_XML_DECLARATION
     * @param INDENT
     *            Output property for INDENT
     * @return XML Transformer for generating XML
     * 
     * @throws TransformerFactoryConfigurationError
     *             If there were problems creating the transformer
     * @throws TransformerConfigurationException
     *             If there were problems creating the transformer
     */
    public static TransformerHandler createXmlDocument(StringWriter output, 
                                                 String ENCODING, 
                                                 String OMIT_XML_DECLARATION, 
                                                 String INDENT)
            throws TransformerFactoryConfigurationError,
            TransformerConfigurationException {
        /*
         * Begin setup of the XML document. Several levels of objects needed to
         * get created so that the XML can be displayed properly (similar to
         * .NET).
         */
        StreamResult result = new StreamResult(output);
        SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
                .newInstance();
        TransformerHandler transformer = tf.newTransformerHandler();
        Transformer serializer = transformer.getTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, ENCODING);
        serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, OMIT_XML_DECLARATION);
        serializer.setOutputProperty(OutputKeys.INDENT, INDENT);
        transformer.setResult(result);
        return transformer;
    }
}
