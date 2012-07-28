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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Set;

import javax.management.DynamicMBean;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.jmx.JdkJMXAbstraction;
import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.mbeanserver.MockMBeanServer;
import com.interopbridges.scx.mbeanserver.AnotherMockMBeanServer;
import com.interopbridges.scx.util.SAXParser;

/**
 * Class to test the MBeanInvoker class
 * These tests specifically test the invocation of a MBean method.
 * There are numerous types of method calls with different parameters and return values.
 * 
 * @author Geoff Erasmus
 */
public class MBeanInvokerTest 
{
    /**
     * <p>
     * The underlying MBeanServer required to register and unregister the MBeans 
     * </p>
     */
    private javax.management.MBeanServer _MBeanstore;

    /**
     * <p>
     * The name for the registered mbean 
     * </p>
     */
    private static String mbeanName = "com.interopbridges.scx:name=DummyInvokeMBean";
    
    /**
     * <p>
     * Interface to getting the desired MBeans from the JMX Store (as XML).
     * </p>
     */
    protected MBeanGetter     _mbeanAccessor;
    
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
         MockMBeanServer.Reset_TestMBeanServer();
         _MBeanstore = MockMBeanServer.getInstance();
         JmxStores.addStoreToJmxStores(new JdkJMXAbstraction(_MBeanstore));
         
         Assert.assertEquals("Only one JMX Store should be connected", 1, JmxStores.getListOfJmxStoreAbstractions().size());
         
         _mbeanAccessor = new MBeanGetter(JmxStores.getListOfJmxStoreAbstractions());
         /* 
          * Create the MBean that will be queried by the tests.
          */
         DynamicMBean dummymbean = new DummyInvokeMBean();
         try
         {
             /* 
              * register the MBean on the MBeanServer.
              */
             JmxStores.getListOfJmxStoreAbstractions().get(0).registerMBean(
                     dummymbean, new ObjectName(mbeanName));
             
             /*
              * make sure the correct bean got loaded correctly
              */
             Set<ObjectInstance> oinst = 
                 JmxStores.getListOfJmxStoreAbstractions().get(0).
                 queryMBeans(new ObjectName(mbeanName),null); 
             assertTrue(oinst.size()==1); 

             assertTrue(JmxStores.getListOfJmxStoreAbstractions().get(0).
                     getMBeanInfo(new ObjectName(mbeanName)).
                     getDescription().compareTo("Microsoft Test MBean")==0); 

             /*
              * make sure that there is only 1 MBean in the store
              */
             assertTrue(JmxStores.getListOfJmxStoreAbstractions().get(0).getMBeanCount()==1); 
             
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received registering the MBean: " + e.getMessage());
         }
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
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received unregistering the MBean: " + e.getMessage());
         }
         JmxStores.clearListOfJmxStores();
     }

     /**
      * <p>
      * Verify that for a legal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_VoidVoid_positive() 
     {
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
             "VoidVoidMethod", new ArrayList<MBeanMethodParameter>() );
         
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain SUCCESS",s[0].matches("SUCCESS"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for a legal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_BaseType_Void_positives() 
     {
         test_Method_takes_No_Param_Returns_Base_Type("StringVoidMethod", "Test data"); 
         test_Method_takes_No_Param_Returns_Base_Type("IntVoidMethod", Integer.toString(Integer.MAX_VALUE)); 
         test_Method_takes_No_Param_Returns_Base_Type("ShortVoidMethod", Short.toString(Short.MAX_VALUE)); 
         test_Method_takes_No_Param_Returns_Base_Type("LongVoidMethod", Long.toString(Long.MAX_VALUE)); 
         test_Method_takes_No_Param_Returns_Base_Type("FloatVoidMethod", Float.toString(Float.MAX_VALUE)); 
         test_Method_takes_No_Param_Returns_Base_Type("DoubleVoidMethod", Double.toString(Double.MAX_VALUE)); 
         test_Method_takes_No_Param_Returns_Base_Type("CharVoidMethod", "a"); 
         test_Method_takes_No_Param_Returns_Base_Type("ByteVoidMethod", Byte.toString(Byte.MAX_VALUE)); 
         test_Method_takes_No_Param_Returns_Base_Type("BooleanVoidMethod", "true"); 
     }

     /**
      * <p>
      * Verify that for a legal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_Void_BaseType_positives() 
     {
         test_Method_takes_Base_Param_Returns_void("VoidStringMethod", "String",  "String"); 
         test_Method_takes_Base_Param_Returns_void("VoidIntMethod",    "int",     Integer.toString(Integer.MAX_VALUE)); 
         test_Method_takes_Base_Param_Returns_void("VoidIntMethod",    "integer", Integer.toString(Integer.MAX_VALUE)); 
         test_Method_takes_Base_Param_Returns_void("VoidShortMethod",  "short",   Short.toString(Short.MAX_VALUE)); 
         test_Method_takes_Base_Param_Returns_void("VoidLongMethod",   "long",    Long.toString(Long.MAX_VALUE)); 
         test_Method_takes_Base_Param_Returns_void("VoidFloatMethod",  "float",   Float.toString(Float.MAX_VALUE)); 
         test_Method_takes_Base_Param_Returns_void("VoidDoubleMethod", "double",  Double.toString(Double.MAX_VALUE)); 
         test_Method_takes_Base_Param_Returns_void("VoidCharMethod",   "char",    "a"); 
         test_Method_takes_Base_Param_Returns_void("VoidByteMethod",   "byte",    Byte.toString(Byte.MAX_VALUE)); 
         test_Method_takes_Base_Param_Returns_void("VoidBooleanMethod","boolean", "true"); 
         test_Method_takes_Base_Param_Returns_void("VoidBooleanMethod","bool",    "true"); 
     }

     
     /**
      * <p>
      * Helper function to call methods and compare base return types.
      * Verify that for a legal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     public void test_Method_takes_No_Param_Returns_Base_Type(String Methodname, String ExpectedValue) 
     {
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 Methodname, new ArrayList<MBeanMethodParameter>() );
         
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Response");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Response) did not contain the correct value - "+s[0],s[0].matches( ExpectedValue ));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Helper function to call methods and compare base return types.
      * Verify that for a legal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     public void test_Method_takes_Base_Param_Returns_void(String Methodname, String type, String value) 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         params.add( new MBeanMethodParameter("name",type,value) );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 Methodname, params );
         
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain SUCCESS\n"+
                    "Return data ["+xml+"]\n",s[0].matches("SUCCESS"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for an illegal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_Unknown_Method() 
     {
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "InvalidMethod", new ArrayList<MBeanMethodParameter>() );
         
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }

     /**
      * <p>
      * Verify that for an illegal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_Invalid_Param_Type() 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         params.add( new MBeanMethodParameter("name","int","123") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "VoidStringMethod", params );
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for an illegal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_Invalid_Param_Count() 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         params.add( new MBeanMethodParameter("param1","String","a string") );
         params.add( new MBeanMethodParameter("param2","String","another string") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "VoidStringMethod", params );
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for an illegal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_Invalid_Param_Data() 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         params.add( new MBeanMethodParameter("param1","int","a string") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "VoidIntMethod", params );
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for an illegal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_Invalid_MBeanName() 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         params.add( new MBeanMethodParameter("param1","String","a string") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, "InvalidMBeanName", 
                 "VoidStringMethod", params );
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for an illegal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_No_Matching_MBean() 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         params.add( new MBeanMethodParameter("param1","String","a string") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, "InvalidMBeanName:*", 
                 "VoidStringMethod", params );
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for an illegal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_Too_Many_Matching_MBeans() 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         
         /*
          * Add an additional MBean to the MBeanServer 
          */
         DynamicMBean dummymbean = new DummyInvokeMBean();
         try
         {
             /* 
              * register the MBean on the MBeanServer.
              */
             JmxStores.getListOfJmxStoreAbstractions().get(0).registerMBean(
                     dummymbean, new ObjectName(mbeanName+"1"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received registering the MBean: " + e.getMessage());
         }

         
         params.add( new MBeanMethodParameter("param1","String","a string") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, "com.interopbridges.scx:name=*,*", 
                 "VoidStringMethod", params );
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }

     /**
      * <p>
      * Verify that for an legal Invoke call to canned MBean that
      * the Response XML looks like we expected it to. This particular test is 
      * for the scenario where multiple MBeans are returned however there is only one per JMX store.
      * This means that we need to create matching MBeans across multiple stores.
      * </p>
      */
     @Test
     public void testInvoke_Too_Many_Matching_MBean_Too_Many_Stores() 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         
         /*
          * Add a new MBean store
          */
         javax.management.MBeanServer MyMBeanstore;
         MyMBeanstore = AnotherMockMBeanServer.getInstance();
         JmxStores.addStoreToJmxStores(new JdkJMXAbstraction(MyMBeanstore));
         
         Assert.assertEquals("There should be 2 JMX Stores connected", 2, JmxStores.getListOfJmxStoreAbstractions().size());
         
         /*
          * Add an MBean to the new MBeanStore 
          */
         DynamicMBean dummymbean = new DummyInvokeMBean();
         try
         {
             /* 
              * register the MBean on the MBeanServer.
              */
             MyMBeanstore.registerMBean(dummymbean, new ObjectName(mbeanName));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received registering the MBean: " + e.getMessage());
         }

         
         params.add( new MBeanMethodParameter("param1","String","a string") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "VoidStringMethod", params );
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for an illegal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_Invalid_Param_Type_Not_BaseType() 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         params.add( new MBeanMethodParameter("param1","invalid","a string") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "VoidIntMethod", params );
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for an illegal Invoke calls with empty parameter values to a canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_Invalid_Empty_Param_Value()
     {
         /*
          * The String parameter type is the only parameter that should allow empty values
          * all other data types should throw an error.
          */
         test_Method_takes_Base_Param_Returns_void("VoidStringMethod", "String", ""); 
         Invoke_Invalid_Empty_Param_Value("VoidIntMethod",    "int"); 
         Invoke_Invalid_Empty_Param_Value("VoidIntMethod",    "integer"); 
         Invoke_Invalid_Empty_Param_Value("VoidShortMethod",  "short"); 
         Invoke_Invalid_Empty_Param_Value("VoidLongMethod",   "long"); 
         Invoke_Invalid_Empty_Param_Value("VoidFloatMethod",  "float"); 
         Invoke_Invalid_Empty_Param_Value("VoidDoubleMethod", "double"); 
         Invoke_Invalid_Empty_Param_Value("VoidCharMethod",   "char"); 
         Invoke_Invalid_Empty_Param_Value("VoidByteMethod",   "byte"); 
         Invoke_Invalid_Empty_Param_Value("VoidBooleanMethod","boolean"); 
         Invoke_Invalid_Empty_Param_Value("VoidBooleanMethod","bool"); 
     }
     
     /**
      * <p>
      * Helper function to call methods with empty parameter values.
      * Verify that for a legal Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     public void Invoke_Invalid_Empty_Param_Value(String method, String paramType) 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         params.add( new MBeanMethodParameter("param1",paramType,"") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 method, params );
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received");
         }
     }
     
     /**
      * <p>
      * Verify that for an legal Invoke call to canned MBean that
      * a method that takes too long to ececute will return an error.
      * </p>
      */
     @Test
     public void testInvoke_Slow_Method() 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         params.add( new MBeanMethodParameter("param1","int","5000") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "Sleeper", params );
         try
         {
            String xml = mbi.transformMBeanCall( "3", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for an legal Invoke call to canned MBean that
      * returns too much data will return an error.
      * </p>
      */
     @Test
     public void testInvoke_MethodThatReturnsTooMuch() 
     {
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "BigMethod", new ArrayList<MBeanMethodParameter>() );
         try
         {
            String xml = mbi.transformMBeanCall( "5", "1000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for an legal Invoke call to canned MBean that
      * the parameters are correctly validated.
      * </p>
      */
     @Test
     public void testInvoke_InvalidMaxTimeParameter() 
     {
         testInvoke_Parameters("a", "1000"); 
     }
     
     /**
      * <p>
      * Verify that for an legal Invoke call to canned MBean that
      * the parameters are correctly validated.
      * </p>
      */
     @Test
     public void testInvoke_NegativeMaxTimeParameter() 
     {
         testInvoke_Parameters("-1", "1000"); 
     }
     
     /**
      * <p>
      * Verify that for an legal Invoke call to canned MBean that
      * the parameters are correctly validated.
      * </p>
      */
     @Test
     public void testInvoke_InvalidMaxSizeParameter() 
     {
         testInvoke_Parameters("1", "a"); 
     }
     
     /**
      * <p>
      * Verify that for an legal Invoke call to canned MBean that
      * the parameters are correctly validated.
      * </p>
      */
     @Test
     public void testInvoke_NegativeMaxSizeParameter() 
     {
         testInvoke_Parameters("1", "-1000"); 
     }
     
     /**
      * <p>
      * Helper method to test MaxTime and MaxSize
      * </p>
      */
     private void testInvoke_Parameters(String MaxTime, String MaxSize) 
     {
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "VoidVoidMethod", new ArrayList<MBeanMethodParameter>() );
         try
         {
            String xml = mbi.transformMBeanCall( MaxTime, MaxSize).toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for an legal Invoke call to canned MBean method that
      * throws an exception, that the response looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_MethodThatThrowsException() 
     {
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "ExceptionMethod", new ArrayList<MBeanMethodParameter>() );
         try
         {
            String xml = mbi.transformMBeanCall( "5", "1000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     
     /**
      * <p>
      * Verify that for a legal overloaded Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_String_Overloaded_positives() 
     {
         test_Method_takes_No_Param_Returns_Base_Type("Overloaded", "Test data"); 
         test_Method_takes_Overloaded_Param_Returns_String_Type("Overloaded", "String", "test data", "test data"); 
         test_Method_takes_Overloaded_Param_Returns_String_Type("Overloaded", "Integer", "4321", "4321"); 
         test_Method_takes_Overloaded_Param_Returns_String_Type("Overloaded", "Float", "4321.123", "4321.123");
     }

     /**
      * <p>
      * Verify that for a illegal overloaded Invoke call to canned MBean that
      * the Response XML looks like we expected it to.
      * </p>
      */
     @Test
     public void testInvoke_String_Overloaded_No_Method_Exists() 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         params.add( new MBeanMethodParameter("name","Double","12.12") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "Overloaded", params );
         
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
            Assert.assertTrue(
                    "Incorrect response to Invoke method call\n "+
                    "The XML element (/InvokeResponse/Result) must contain ERROR\n"+
                    "Return data ["+xml+"]\n",s[0].matches("ERROR"));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
    /**
     * <p>
     * Helper function to call methods and compare base return types.
     * Verify that for a legal Invoke call to canned MBean that
     * the Response XML looks like we expected it to.
     * </p>
     */
    public void test_Method_takes_Overloaded_Param_Returns_String_Type(String Methodname, String type, String value, String ExpectedValue) 
    {
        ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
        params.add( new MBeanMethodParameter("name",type,value) );
        
        MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                Methodname, params );
        
        try
        {
           String xml = mbi.transformMBeanCall( "5", "5000").toString();
           String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Result");
           Assert.assertTrue(
                   "Incorrect response to Invoke method call\n "+
                   "The XML element (/InvokeResponse/Result) must contain SUCCESS\n"+
                   "Return data ["+xml+"]\n",s[0].matches("SUCCESS"));
           
           s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/Response");
           Assert.assertTrue(
                   "Incorrect response to Invoke method call\n "+
                   "The XML element (/InvokeResponse/Response) must contain "+ ExpectedValue +"\n"+
                   "Return data ["+xml+"]\n",s[0].matches(ExpectedValue));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Exception Received: " + e.getMessage());
        }
    }

     /**
      * <p>
      * Verify that for a legal Invoke call to canned MBean that
      * the Response XML contains the version attribute.
      * </p>
      */
     @Test
     public void testInvoke_positive_version_attribute() 
     {
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
             "VoidVoidMethod", new ArrayList<MBeanMethodParameter>() );
         
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/@version");

            Assert.assertTrue("Query for version attribute should produce one result", s.length == 1);
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }
     
     /**
      * <p>
      * Verify that for a illegal Invoke call to canned MBean that
      * the Response XML contains the version attribute.
      * </p>
      */
     @Test
     public void testInvoke_negative_version_attribute() 
     {
         ArrayList<MBeanMethodParameter> params = new ArrayList<MBeanMethodParameter>();
         params.add( new MBeanMethodParameter("name","Double","12.12") );
         
         MBeanInvoker mbi = new MBeanInvoker( _mbeanAccessor, mbeanName, 
                 "Overloaded", params );
         
         try
         {
            String xml = mbi.transformMBeanCall( "5", "5000").toString();
            String[] s = (String []) SAXParser.XPathQuery(xml,"/InvokeResponse/@version");

            Assert.assertTrue("Query for version attribute should produce one result", s.length == 1);
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Exception Received: " + e.getMessage());
         }
     }

}
