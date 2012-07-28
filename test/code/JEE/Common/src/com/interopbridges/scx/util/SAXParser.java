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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.interopbridges.scx.mbeans.ComplexClass;

public class SAXParser {

	/**
	 * <p>
	 * Helper Test method to streamline Tests. This custom assert wraps the
	 * lengthy (but necessary) setup required for parsing XML documents via the
	 * various Java APIs.
	 * </p>
	 * 
	 * <p>
	 * This test method expects to be given an List of objects that are the
	 * expected values (order matters) and verifies that the XML given returns
	 * such a list
	 * </p>
	 * 
	 * @param description
	 *            Description method to display when the assert fails.
	 * @param inputXml
	 *            Input XML that needed to be validated
	 * @param expectedList
	 *            List of expected text to be discovered by the XPATH query
	 * @param xpathQuery
	 *            XPATH query to execute
	 * @throws XPathExpressionException
	 *             Thrown if an illegal XPATH query is given
	 * @throws ParserConfigurationException
	 *             Thrown if given a bad parser configuration
	 * @throws SAXException
	 *             If there was an error parsing the XML
	 * @throws IOException
	 *             If there was an error parsing the XML
	 */
    public static void customQueryAssertForXmlValidationOfListOfObjectValues(String description,
            String inputXml, ArrayList<Object> expectedList, String xpathQuery)
            throws XPathExpressionException, ParserConfigurationException,
            SAXException, IOException {

        // Create test apparatus for parsing results with XPATH
        // (Yes, all of this is required!)
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

        // This query may need to change and/or be refactored. For now,
        // the sample XPath Queries are only returning one node so I am
        // leaving this is there
        Assert
                .assertEquals(
                        "XPath query ("
                                + xpathQuery
                                + ") did not return the number of expected results for the input: "
                                + inputXml, expectedList.size(), results
                                .getLength());

        for (int i = 0; i < results.getLength(); i++) {
            /*
             * This custom assert is explicitly specific. The casts are here on
             * purpose as an extra step of verifying that the correct type of
             * class is being used for comparison.
             */
        	String expectedText = (null == expectedList.get(i)) ? "(null)" : expectedList.get(i).toString();
        	String actualText = (null == results.item(i).getTextContent()) ? "(null)" : results.item(i).getTextContent().toString();
        	String prettyDescription = description + "Expected: [" + expectedText + "] Actual: ["+ actualText +"]";
        	
            if (expectedList.get(i) instanceof Boolean) {
                Assert.assertTrue(prettyDescription, ((Boolean) expectedList.get(i))
                        .equals(Boolean.parseBoolean(results.item(i)
                                .getTextContent())));
            } else if (expectedList.get(i) instanceof Byte) {
                Assert.assertTrue(prettyDescription, ((Byte) expectedList.get(i))
                        .equals(Byte
                                .parseByte(results.item(i).getTextContent())));
            } else if ("java.lang.Character".equals(expectedList.get(i)
                    .getClass().getName())) {
                /*
                 * At this point a Character needs to be compared to a String.
                 * Strangely, there does not appear to be a method() for doing
                 * so. Instead, the test will convert both to Strings and do the
                 * comparison.
                 */
                String expected = ((Character) expectedList.get(i)).toString();
                String actual = results.item(i).getTextContent();
                Assert.assertTrue(prettyDescription, expected.equals(actual));
            } else if (expectedList.get(i) instanceof Double) {
                Assert.assertTrue(prettyDescription, ((Double) expectedList.get(i))
                        .equals(Double.parseDouble(results.item(i)
                                .getTextContent())));
            } else if (expectedList.get(i) instanceof Float) {
                Assert.assertTrue(prettyDescription, ((Float) expectedList.get(i))
                        .equals(Float.parseFloat(results.item(i)
                                .getTextContent())));
            } else if (expectedList.get(i) instanceof Integer) {
                Assert.assertTrue(prettyDescription, ((Integer) expectedList.get(i))
                        .equals(Integer.parseInt(results.item(i)
                                .getTextContent())));
            } else if (expectedList.get(i) instanceof Long) {
                Assert.assertTrue(prettyDescription, ((Long) expectedList.get(i))
                        .equals(Long
                                .parseLong(results.item(i).getTextContent())));
            } else if (expectedList.get(i) instanceof Short) {
                Assert.assertTrue(prettyDescription, ((Short) expectedList.get(i))
                        .equals(Short.parseShort(results.item(i)
                                .getTextContent())));
            } else if (expectedList.get(i) instanceof ComplexClass) {
                Assert.assertTrue(prettyDescription, (((ComplexClass) expectedList.get(i)).getClass().getMethods().length)
                        == (28));
            } else {
                /*
                 * Default compare relies in Object.equals(). Things like
                 * Strings and other non-primitive objects should use this
                 * block.
                 */
            	
                Assert.assertTrue(prettyDescription, expectedList.get(i).equals(
                        results.item(i).getTextContent()));
            }
        }
    }
    
    /**
     * <p>
     * Helper Test method to streamline Tests. This custom assert wraps the
     * lengthy (but necessary) setup required for parsing XML documents via the
     * various Java APIs.
     * </p>
     * 
     * @param description
     *            Description method to display when the assert fails.
     * @param inputXml
     *            Input XML that needed to be validated
     * @param expected
     *            expected text to be discovered by the XPATH query
     * @param xpathQuery
     *            XPATH query to execute
     * @throws XPathExpressionException
     *             Thrown if an illegal XPATH query is given
     * @throws ParserConfigurationException
     *             Thrown if given a bad parser configuration
     * @throws SAXException
     *             If there was an error parsing the XML
     * @throws IOException
     *             If there was an error parsing the XML
     */
    public static void customQueryAssertForXmlValidationOfObjectValue(String description,
            String inputXml, Object expected, String xpathQuery)
            throws XPathExpressionException, ParserConfigurationException,
            SAXException, IOException {
        ArrayList<Object> expectedList = new ArrayList<Object>(1);
        expectedList.add(expected);
        SAXParser.customQueryAssertForXmlValidationOfListOfObjectValues(description, inputXml,
                expectedList, xpathQuery);
    }

	/**
	 * <p>
	 * Helper Test method to streamline Tests. This custom assert wraps the
	 * lengthy (but necessary) setup required for parsing XML documents via the
	 * various Java APIs.
	 * </p>
	 * 
	 * @param description
	 *            Description method to display when the assert fails.
	 * @param inputXml
	 *            Input XML that needed to be validated
	 * @param expectedList
	 *            List of expected text to be discovered by the XPATH query
	 * @param xpathQuery
	 *            XPATH query to execute
	 * @throws XPathExpressionException
	 *             Thrown if an illegal XPATH query is given
	 * @throws ParserConfigurationException
	 *             Thrown if given a bad parser configuration
	 * @throws SAXException
	 *             If there was an error parsing the XML
	 * @throws IOException
	 *             If there was an error parsing the XML
	 */
	public void customQueryAssertForXmlValidationOfObject(String description,
					String inputXml, Object expectedItem, String xpathQuery)
					throws XPathExpressionException, ParserConfigurationException,
					SAXException, IOException 
			{
				// Create test apparatus for parsing results with XPATH
				// Yes, all of this is required!
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

				for (int i = 0; i < results.getLength(); i++) 
				{
					if(results.item(i).getLocalName().equalsIgnoreCase(expectedItem.toString()))
					{
						return; // found the item
					}
				}
                Assert.fail(description);
			}
			
	/**
	 * <p>
	 * Helper Test method to streamline Tests. This is a XPath query function to
	 * return the values associated with an XPath query.
	 * </p>
	 * 
	 * @param inputXml
	 *            Input XML
	 * @param xpathQuery
	 *            XPATH query to execute
	 * @return an array of objects associated with the query
	 * 
	 * @throws XPathExpressionException
	 *             Thrown if an illegal XPATH query is given
	 * @throws ParserConfigurationException
	 *             Thrown if given a bad parser configuration
	 * @throws SAXException
	 *             If there was an error parsing the XML
	 * @throws IOException
	 *             If there was an error parsing the XML
	 */
    public static String[] XPathQuery(
            String inputXml, String xpathQuery)
            throws XPathExpressionException, ParserConfigurationException,
            SAXException, IOException 
    {
        // Create test apparatus for parsing results with XPATH
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

        List<String> ans = new ArrayList<String>();
        for (int i = 0; i < results.getLength(); i++) 
        {
            ans.add(results.item(i).getTextContent());
        }
        String s[] = new String[ans.size()];
        s = ans.toArray(s);
        
        return s;
    }
}
