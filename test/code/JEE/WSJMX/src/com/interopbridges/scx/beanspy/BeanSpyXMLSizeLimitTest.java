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

package com.interopbridges.scx.beanspy;

import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.DynamicMBean;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.servlet.ServletException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.beanspy.BeanSpy;
import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.jmx.JdkJMXAbstraction;
import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.jmx.MockTomcatJMXAbstraction;
import com.interopbridges.scx.mbeans.DummyTomcatMBean;
import com.interopbridges.scx.mbeanserver.MockMBeanServer;

/**
 * Class to test BeanSpy while the size of xml response exceeds limit.
 * 
 * @author Jinlong Li
 * @since April 18th, 2011
 */
public class BeanSpyXMLSizeLimitTest
{

    /**
     * <p>
     * The underlying MBeanServer required to unregister the MBeans
     * </p>
     */
    private static javax.management.MBeanServer _MBeanstore;

    /**
     * <p>
     * The name for the registered mbean
     * </p>
     */
    private static final String mbeanName = "Catalina:type=Server";

    /**
     * <p>
     * Mock Http Servlet _request
     * </p>
     */
    private static MockHttpServletRequest _request;

    /**
     * <p>
     * Mock Http Servlet Response
     * </p>
     */
    private static MockHttpServletResponse _response;

    /**
     * <p>
     * Attributes for the mbean.
     * </p>
     */
    private static AttributeList _attributeList;

    /**
     * <p>
     * A stringbuffer to build a longer enough string for the mbean.
     * </p>
     */
    private static StringBuffer _propertyValueStrBuffer;

    /**
     * <p>
     * A fixed length string to construct a mbean. The length = 1024.
     * </p>
     */
    private static final String _constantString = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    /**
     * <p>
     * A constant integer number of 4M
     * </p>
     */
    private static final int fourMegaBytes = 4 * 1024 * 1024;

    /**
     * <p>
     * An object of JMXEntender
     * </p>
     */
    private static BeanSpy _extender;

    /**
     * <p>
     * The number of bytes used for wrapping JMXQuery by BeanSpy
     * </p>
     */
    private static int _lenOfStructureBytes;

    /**
     * <p>
     * _numOfMultiple = (fourMegaBytes - _lenOfStructureBytes)/1024
     * </p>
     */
    private static int _numOfMultiple;

    /**
     * <p>
     * a constant value for 1 kilobyte
     * </p>
     */
    private static final int _numOf1K = 1024;

    /**
     * <p>
     * Method invoked before each unit-test in this class.
     * </p>
     * 
     * @throws Execption
     *             If something went wrong when initializing the MBean Store
     */
    @Before
    public void Setup() throws Exception
    {
        /*
         * Create a new MBeanServer to host the MBean that will be queried.
         */
        JmxStores.clearListOfJmxStores();
        _MBeanstore = MockMBeanServer.getInstance();
        MockMBeanServer.Reset_TestMBeanServer();
        JmxStores.addStoreToJmxStores(new MockTomcatJMXAbstraction());
        JmxStores.addStoreToJmxStores(new JdkJMXAbstraction(_MBeanstore));
        /*
         * Adding the MockTomcatJMXAbstraction does not add a new JMX store to
         * the list of JmxStores. The MockTomcatJMXAbstraction return false for
         * the isStandAloneJmxStore() which means that does not get added or
         * queried for subsequent queries.
         */
        Assert.assertEquals("There should only be 1 JMX Stores connected", 1,
                JmxStores.getListOfJmxStoreAbstractions().size());

        /*
         * Create the MBean that will be queried by the tests.
         */
        DynamicMBean dummymbean = new DummyTomcatMBean();

        _attributeList = new AttributeList();

        try
        {
            /*
             * register the MBean on the MBeanServer.
             */
            _MBeanstore.registerMBean(dummymbean, new ObjectName(mbeanName));

            /*
             * make sure the correct bean got loaded correctly
             */
            Set<ObjectInstance> theBeans = new HashSet<ObjectInstance>();
            List<IJMX> ilist = JmxStores.getListOfJmxStoreAbstractions();
            for (Iterator<IJMX> it = ilist.iterator(); it.hasNext();)
            {
                theBeans.addAll(it.next().queryMBeans(
                        new ObjectName(mbeanName), null));
            }
            assertTrue(theBeans.size() == 1);

            _MBeanstore
                    .setAttributes(new ObjectName(mbeanName), _attributeList);
        } catch (Exception e)
        {
            Assert.fail("Unexpected Exception Received registering the MBean: "
                    + e.getMessage());
        }

        /*
         * Initialize variables which will be used for the test cases
         */
        _request = new MockHttpServletRequest("");
        _request.addParameter("JMXQuery", mbeanName);
        _response = new MockHttpServletResponse();
        _extender = new BeanSpy();
        _propertyValueStrBuffer = new StringBuffer();
        _lenOfStructureBytes = find_lenOfStructureBytes();
        _numOfMultiple = (fourMegaBytes - _lenOfStructureBytes)/1024;

        /*
         * Make sure the constant string length is what we expected: 1024 byte.
         */
        assertTrue(_numOf1K == _constantString.length());
    }

    /**
     * <p>
     * Unit Test Teardown method
     * </p>
     */
    @After
    public void Teardown()
    {
        try
        {
            _MBeanstore.unregisterMBean(new ObjectName(mbeanName));
        } catch (Exception e)
        {
            assertUnexpectedException(e);
        }
        JmxStores.clearListOfJmxStores();
    }

    /**
     * <p>
     * Helper Method for this test class to complain when getting an unexpected
     * exception. This 'pretty prints' the exception to make it easier to
     * determine the error without launching the debugger.
     * </p>
     * 
     * @param e
     *            The Exception to barf about.
     */
    private void assertUnexpectedException(Exception e)
    {
        StringBuffer message = new StringBuffer(
                "Unexpected Exception Received.\n");
        message.append("Message: ").append(e.getMessage()).append("\n");
        message.append(e.getStackTrace().toString());
        Assert.fail(message.toString());
    }

    /**
     * <p>
     * Compute the length of the structure bytes.
     * </p>
     * 
     * @author Jinlong Li
     */
    public int find_lenOfStructureBytes()
    {
        int lenOfStructureBytes = 0;
        _attributeList.add(new Attribute("serverInfo", _constantString));

        try
        {
            _MBeanstore
                    .setAttributes(new ObjectName(mbeanName), _attributeList);
        } catch (Exception e)
        {
            assertUnexpectedException(e);
        }

        _response = new MockHttpServletResponse();
        try
        {
            _extender.doGet(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();

            lenOfStructureBytes = tmpResponse.length() - _numOf1K;
        } catch (Exception e)
        {
            assertUnexpectedException(e);
        }

        return lenOfStructureBytes;
    }

    /**
     * <p>
     * Verify the size of the xml response is calculated as expected.
     * </p>
     * 
     * <p>
     * The failure of this test case indicates that the structure of the XML
     * file generated by BeanSpy has been changed. If this happens, all test
     * cases in this class should be adjusted accordingly.
     * </p>
     * 
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_XMLSizeLimitTest_verifyMBeanSize()
    {
        _attributeList.add(new Attribute("serverInfo", _constantString));

        try
        {
            _MBeanstore
                    .setAttributes(new ObjectName(mbeanName), _attributeList);
        } catch (Exception e)
        {
            assertUnexpectedException(e);
        }

        _response = new MockHttpServletResponse();
        try
        {
            _extender.doGet(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            Assert.assertTrue("The size of XML response should equal to "
                    + (_numOf1K + _lenOfStructureBytes)
                    + " while the actual size of the mbean is "
                    + tmpResponse.length() + ".",
                    (_numOf1K + _lenOfStructureBytes) == tmpResponse.length());
        } catch (Exception e)
        {
            assertUnexpectedException(e);
        }
    }

    /**
     * <p>
     * Verify while the size of the xml response equals to ABS_MAX_XML_SIZE + 1,
     * the http Response should fail and throw an exception.
     * </p>
     * 
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_XMLSizeLimitTest_4Mplus1()
    {
        for (int i = 0; i < _numOfMultiple; i++)
        {
            _propertyValueStrBuffer.append(_constantString);
        }

        _propertyValueStrBuffer.append(_constantString.substring(0, _numOf1K
                - _lenOfStructureBytes + 1));

        _attributeList.add(new Attribute("serverInfo", _propertyValueStrBuffer
                .toString()));

        try
        {
            _MBeanstore
                    .setAttributes(new ObjectName(mbeanName), _attributeList);
        } catch (Exception e)
        {
            assertUnexpectedException(e);
        }

        try
        {
            _extender.doGet(_request, _response);
            Assert
                    .fail("Function should fail because the size of the xml response exceeds the limit.");
        } catch (Exception e)
        {
            Object[] args =
            { new Integer(fourMegaBytes), mbeanName };
            String formattedMessage = MessageFormat.format(new ScxException(
                    ScxExceptionCode.ERROR_SIZE_OF_XML_FILES_EXCEED_LIMITS)
                    .getMessage(), args);

            Assert
                    .assertTrue(
                            "Should be an ScxException.",
                            (((ServletException) e).getRootCause().getClass() == ScxException.class));

            Assert
                    .assertTrue(
                            "Should be an ERROR_SIZE_OF_XML_FILE_EXCEEDS_LIMITS exception.",
                            (((ScxException) ((ServletException) e)
                                    .getRootCause()).getExceptionCode()
                                    .equals(ScxExceptionCode.ERROR_SIZE_OF_XML_FILES_EXCEED_LIMITS)));

            Assert
                    .assertTrue(
                            "Should throw an ERROR_SIZE_OF_XML_FILE_EXCEEDS_LIMITS exception:"
                                    + formattedMessage + ":" + e.getMessage(),
                            e.getMessage().toString()
                                    .contains(formattedMessage));
        }
    }

    /**
     * <p>
     * Verify while the size of the xml response equals to ABS_MAX_XML_SIZE, the
     * http Response should not fail.
     * </p>
     * 
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_XMLSizeLimitTest_Exactly_4M()
    {

        for (int i = 0; i < _numOfMultiple; i++)
        {
            _propertyValueStrBuffer.append(_constantString);
        }
        _propertyValueStrBuffer.append(_constantString.substring(0, _numOf1K
                - _lenOfStructureBytes));

        _attributeList.add(new Attribute("serverInfo", _propertyValueStrBuffer
                .toString()));

        try
        {
            _MBeanstore
                    .setAttributes(new ObjectName(mbeanName), _attributeList);
        } catch (Exception e)
        {
            assertUnexpectedException(e);
        }

        _response = new MockHttpServletResponse();
        try
        {
            _extender.doGet(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            Assert
                    .assertTrue(
                            "The size of XML response should equal to ABS_MAX_XML_SIZE.",
                            fourMegaBytes == tmpResponse.length());
        } catch (Exception e)
        {
            assertUnexpectedException(e);
        }
    }

    /**
     * <p>
     * Verify while the size of the xml response equals to ABS_MAX_XML_SIZE - 1,
     * the http Response should not fail.
     * </p>
     * 
     * @author Jinlong Li
     */
    @Test
    public void testDoGet_XMLSizeLimitTest_4Mminus1()
    {
        for (int i = 0; i < _numOfMultiple; i++)
        {
            _propertyValueStrBuffer.append(_constantString);
        }
        _propertyValueStrBuffer.append(_constantString.substring(0, _numOf1K
                - _lenOfStructureBytes - 1));

        _attributeList.add(new Attribute("serverInfo", _propertyValueStrBuffer
                .toString()));

        try
        {
            _MBeanstore
                    .setAttributes(new ObjectName(mbeanName), _attributeList);
        } catch (Exception e)
        {
            assertUnexpectedException(e);
        }

        _response = new MockHttpServletResponse();
        try
        {
            _extender.doGet(_request, _response);
            String tmpResponse = _response.ostream.buf.toString();
            Assert
                    .assertTrue(
                            "The size of XML response should equal to ABS_MAX_XML_SIZE - 1.",
                            (fourMegaBytes - 1) == tmpResponse.length());
        } catch (Exception e)
        {
            assertUnexpectedException(e);
        }
    }

}
