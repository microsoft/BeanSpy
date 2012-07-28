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

import java.util.ArrayList;
import java.util.Hashtable;

import javax.management.ObjectName;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Unit-tests to verify JMXFilterParameters methods.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class JMXFilterParametersTest {

    JMXFilterParameters _jmxFilterParameters;
    
    String goodxmlDocData_OneJMXStore_ExcludeOneProperty = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<JMXQuery>" +
            "<Exclude>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JdkJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager\">" +
                        "<Attribute>entropy</Attribute>" +
                    "</MBeanObjectName>" +
                "</JMXStore>" +
            "</Exclude>" +
        "</JMXQuery>";
    
    String goodxmlDocData_OneJMXStore_ExcludeOnePropertyWildcard = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<JMXQuery>" +
            "<Exclude>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JdkJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"Catalina:*\">" +
                        "<Attribute>entropy</Attribute>" +
                    "</MBeanObjectName>" +
                "</JMXStore>" +
            "</Exclude>" +
        "</JMXQuery>";
    
    String goodxmlDocData_OneJMXStore_ExcludeTwoProperty = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<JMXQuery>" +
            "<Exclude>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JdkJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager\">" +
                        "<Attribute>entropy</Attribute>" +
                        "<Attribute>SomeOtherProperty</Attribute>" +
                    "</MBeanObjectName>" +
                "</JMXStore>" +
            "</Exclude>" +
        "</JMXQuery>";
    
    String goodxmlDocData_OneJMXStore_ExcludeTwoPropertyWildcard = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<JMXQuery>" +
            "<Exclude>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JdkJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"Catalina:*\">" +
                        "<Attribute>entropy</Attribute>" +
                        "<Attribute>SomeOtherProperty</Attribute>" +
                    "</MBeanObjectName>" +
                "</JMXStore>" +
            "</Exclude>" +
        "</JMXQuery>";
    
    String goodxmlDocData_OneJMXStore_ExcludeAllProperty = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<JMXQuery>" +
            "<Exclude>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JdkJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager\">" +
                        "<Attribute>*</Attribute>" +
                    "</MBeanObjectName>" +
                 "</JMXStore>" +
            "</Exclude>" +
        "</JMXQuery>";
    
    String goodxmlDocData_OneJMXStore_ExcludeAllPropertyWildCard = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<JMXQuery>" +
            "<Exclude>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JdkJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"Catalina:*\">" +
                        "<Attribute>*</Attribute>" +
                    "</MBeanObjectName>" +
                "</JMXStore>" +
            "</Exclude>" +
        "</JMXQuery>";

    String goodxmlDocData_OneJMXStore_ExcludeOnePropertyAllMBeans = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<JMXQuery>" +
            "<Exclude>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JdkJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"*:*\">" +
                        "<Attribute>entropy</Attribute>" +
                    "</MBeanObjectName>" +
                "</JMXStore>" +
            "</Exclude>" +
        "</JMXQuery>";

    String goodxmlDocData_OneJMXStore_ExcludeAllPropertyAllMBeans = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<JMXQuery>" +
            "<Exclude>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JdkJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"*:*\">" +
                        "<Attribute>*</Attribute>" +
                    "</MBeanObjectName>" +
                "</JMXStore>" +
            "</Exclude>" +
        "</JMXQuery>";

    String goodxmlDocData_TwoJMXStores_ExcludeOneProperty = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<JMXQuery>" +
            "<Exclude>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JBossJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager\">" +
                        "<Attribute>entropy</Attribute>" +
                    "</MBeanObjectName>" +
                "</JMXStore>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JdkJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"MyDom:name=MyMBean\">" +
                        "<Attribute>MyAttribute</Attribute>" +
                    "</MBeanObjectName>" +
                "</JMXStore>" +
            "</Exclude>" +
        "</JMXQuery>";
    
    String goodxmlDocData_TwoJMXStores_ExcludeSomeProperties = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<JMXQuery>" +
            "<Exclude>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JBossJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"Catalina:*\">" +
                        "<Attribute>entropy</Attribute>" +
                    "</MBeanObjectName>" +
                    "<MBeanObjectName Name=\"MyDom:name=MyMBean\">" +
                        "<Attribute>MyAttribute</Attribute>" +
                    "</MBeanObjectName>" +
                    "<MBeanObjectName Name=\"Catalina:name=BeanSpy,*\">" +
                        "<Attribute>*</Attribute>" +
                    "</MBeanObjectName>" +
                "</JMXStore>" +
                "<JMXStore Name=\"com.interopbridges.scx.jmx.JdkJMXAbstraction\">" +
                    "<MBeanObjectName Name=\"Catalina:*\">" +
                        "<Attribute>entropy</Attribute>" +
                        "<Attribute>SomeOtherProperty</Attribute>" +
                    "</MBeanObjectName>" +
                    "<MBeanObjectName Name=\"Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager\">" +
                        "<Attribute>MyAttribute</Attribute>" +
                    "</MBeanObjectName>" +
                "</JMXStore>" +
            "</Exclude>" +
        "</JMXQuery>";
    
    @Before
    public void Setup() throws Exception 
    {
        _jmxFilterParameters = JMXFilterParameters.GetInstance();
        _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(goodxmlDocData_OneJMXStore_ExcludeOneProperty));
    }

   /**
    * <p>
    * Unit Test Teardown method
    * </p>
    */
    @After
    public void Teardown() 
    {
    }

   /**
    * <p>
    * A positive test case for testing the exclusions file where there is only one 
    * property exclusion for one MBean for one JMXStore.
    * </p>
    */
    @Test
    public void test_OneJMXStore_Exclude_OneProperty() 
    {
        Hashtable<String,ArrayList<String>> allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JdkJMXAbstraction");
        
        Assert.assertEquals("One MBean should be excluded" , 1, 
                allExclusions.size());
        
        ArrayList<String> props = allExclusions.get("Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager");
        Assert.assertEquals("Wrong MBean/property has been excluded" , 1, 
                props.size());
        
        Assert.assertTrue( allExclusions.get("Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager").get(0).compareTo("entropy")==0);
    }

   /**
    * <p>
    * A positive test case for testing the exclusions file where there is only one 
    * property exclusion for one wildcard MBean for one JMXStore.
    * </p>
    */
     @Test
    public void test_OneJMXStore_ExcludeMultiMBean_OneProperty() 
    {
        try
        {
            _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(goodxmlDocData_OneJMXStore_ExcludeOnePropertyWildcard));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
        
        Hashtable<String,ArrayList<String>> allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JdkJMXAbstraction");
         
        Assert.assertEquals("One MBean should be excluded" , 1, 
                allExclusions.size());
         
        try
        {
            ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                            new ObjectName("Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager"));   
             
            Assert.assertEquals("Wrong MBean/property has been excluded" , 1, thisMBeanExclusions.size());
            
            Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("entropy")==0);
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
         
    }
     
   /**
    * <p>      
    * A positive test case for testing the exclusions file where there are two 
    * properties exclusion for one wildcard MBean for one JMXStore.
    * </p>
    */
      @Test
      public void test_OneJMXStore_Exclude_TwoProperties() 
      {
          try
          {
              _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(goodxmlDocData_OneJMXStore_ExcludeTwoPropertyWildcard));
          }
          catch(Exception e)
          {
              Assert.fail("Unexpected Error loading XML");
          }
          
          Hashtable<String,ArrayList<String>> allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JdkJMXAbstraction");
          
          Assert.assertEquals("One MBean should be excluded" , 1, 
                  allExclusions.size());
          
          try
          {
              ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                      new ObjectName("Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager"));   
              Assert.assertEquals("Wrong MBean/property has been excluded" , 2, 
                      thisMBeanExclusions.size());
          
              Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("entropy")==0);
              Assert.assertTrue( thisMBeanExclusions.get(1).compareTo("SomeOtherProperty")==0);
          }
          catch(Exception e)
          {
              Assert.fail("Unexpected Error loading XML");
          }
      }
      
   /**
    * <p>      
    * A positive test case for testing the exclusions file where there are two 
    * properties exclusion for one wildcard MBean for one JMXStore.
    * </p>
    */
    @Test
    public void test_OneJMXStore_ExcludeMultiMBean_TwoProperties() 
    {
        try
        {
            _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(goodxmlDocData_OneJMXStore_ExcludeTwoPropertyWildcard));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
       
        Hashtable<String,ArrayList<String>> allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JdkJMXAbstraction");
       
        Assert.assertEquals("One MBean should be excluded" , 1, 
               allExclusions.size());
       
        try
        {
            ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                    new ObjectName("Catalina:name=//localhost/manager"));   
            Assert.assertEquals("Wrong MBean/property has been excluded" , 2, 
                    thisMBeanExclusions.size());
       
            Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("entropy")==0);
            Assert.assertTrue( thisMBeanExclusions.get(1).compareTo("SomeOtherProperty")==0);
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
    }
       
   /**
    * <p>      
    * A positive test case for testing the exclusions file where there is a wildcard 
    * property exclusion for one MBean for one JMXStore.
    * </p>
    */
    @Test
    public void test_OneJMXStore_Exclude_AllProperties() 
    {
        try
        {
            _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(goodxmlDocData_OneJMXStore_ExcludeAllProperty));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
       
        Hashtable<String,ArrayList<String>> allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JdkJMXAbstraction");
       
        Assert.assertEquals("One MBean should be excluded" , 1, 
                allExclusions.size());
       
        try
        {
            ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                    new ObjectName("Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager"));   
            Assert.assertEquals("Wrong MBean/property has been excluded" , 1, 
                    thisMBeanExclusions.size());
       
            Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("*")==0);


            // The wrong mBean name is used here and therefore there should be no exclusions 
            thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                    new ObjectName("Catalina:theCat=SatOnTheMat"));   
            Assert.assertEquals("Wrong MBean/property has been excluded" , 0, 
                    thisMBeanExclusions.size());
       
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
   }
 
   /**
    * <p>      
    * A positive test case for testing the exclusions file where there are two 
    * properties exclusion for one wildcard MBean for one JMXStore.
    * </p>
    */
    @Test
    public void test_OneJMXStore_ExcludeMultiMBean_AllProperties() 
    {
        try
        {
            _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(goodxmlDocData_OneJMXStore_ExcludeAllPropertyWildCard));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
         
        Hashtable<String,ArrayList<String>> allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JdkJMXAbstraction");
         
        Assert.assertEquals("One MBean should be excluded" , 1, 
                allExclusions.size());
         
        try
        {
            ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                    new ObjectName("Catalina:theCat=SatOnTheMat"));   
            Assert.assertEquals("Wrong MBean/property has been excluded" , 1, 
                    thisMBeanExclusions.size());
         
            Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("*")==0);
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
    }
         
   /**
    * <p>      
    * A positive test case for testing the exclusions file where there is one 
    * properties exclusion for all MBeans for one JMXStore.
    * </p>
    */
    @Test
    public void test_OneJMXStore_ExcludeAllMBean_OneProperties() 
    {
        try
        {
            _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(goodxmlDocData_OneJMXStore_ExcludeOnePropertyAllMBeans));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
          
        Hashtable<String,ArrayList<String>> allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JdkJMXAbstraction");
          
        Assert.assertEquals("One MBean should be excluded" , 1, 
                allExclusions.size());
          
        try
        {
            ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                    new ObjectName("Catalina:theCat=SatOnTheMat"));   
            Assert.assertEquals("Wrong MBean/property has been excluded" , 1, 
                    thisMBeanExclusions.size());
          
            Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("entropy")==0);
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
    }
        
   /**
    * <p>      
    * A positive test case for testing the exclusions file where all 
    * properties are excluded for all MBeans for one JMXStore.
    * </p>
    */
    @Test
    public void test_OneJMXStore_ExcludeAllMBean_AllProperties() 
    {
        try
        {
            _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(goodxmlDocData_OneJMXStore_ExcludeAllPropertyAllMBeans));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
           
        Hashtable<String,ArrayList<String>> allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JdkJMXAbstraction");
           
        Assert.assertEquals("One MBean should be excluded" , 1, 
                allExclusions.size());
           
        try
        {
            ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                    new ObjectName("Catalina:theCat=SatOnTheMat"));   
            Assert.assertEquals("Wrong MBean/property has been excluded" , 1, 
                    thisMBeanExclusions.size());
           
            Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("*")==0);
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
    }
           
   /**
    * <p>      
    * A positive test case for testing the exclusions file where one 
    * property is excluded for one MBean for two JMXStore.
    * </p>
    */
    @Test
    public void test_TwoJMXStore_Exclude_OneProperty() 
    {
        try
        {
            _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(goodxmlDocData_TwoJMXStores_ExcludeOneProperty));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
        
        
        //process the forst JMXStore and check the exclusions
        Hashtable<String,ArrayList<String>> allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JBossJMXAbstraction");
        
        Assert.assertEquals("One MBean should be excluded" , 1, allExclusions.size());
        
        try
        {
            ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                    new ObjectName("Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager"));   
            Assert.assertEquals("Wrong MBean/property has been excluded" , 1, 
                    thisMBeanExclusions.size());
        
            Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("entropy")==0);
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
        
        //process the second JMXStore and check the exclusions
        allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JdkJMXAbstraction");
        
        Assert.assertEquals("One MBean should be excluded" , 1, allExclusions.size());
        
        try
        {
            ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                    new ObjectName("MyDom:name=MyMBean"));   
            Assert.assertEquals("Wrong MBean/property has been excluded" , 1, 
                    thisMBeanExclusions.size());
        
            Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("MyAttribute")==0);
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected Error loading XML");
        }
    }
        
    /**
     * <p>      
     * A positive test case for testing the exclusions file where there are 
     * different exclusions per JMXStore store.
     * </p>
     */
     @Test
     public void test_TwoJMXStore_MultipleExclusions() 
     {
         try
         {
             _jmxFilterParameters.loadMap(_jmxFilterParameters.loadConfigFromData(goodxmlDocData_TwoJMXStores_ExcludeSomeProperties));
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Error loading XML");
         }
         
         
         //process the forst JMXStore and check the exclusions
         Hashtable<String,ArrayList<String>> allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JBossJMXAbstraction");
         
         Assert.assertEquals("Three MBeans should be excluded" , 3, allExclusions.size());
         
         try
         {
             ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                     new ObjectName("MyDom:name=MyMBean"));   
             Assert.assertEquals("Wrong MBean/property has been excluded" , 1, 
                     thisMBeanExclusions.size());
         
             Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("MyAttribute")==0);
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Error loading XML");
         }

         try
         {
             ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                     new ObjectName("Catalina:name=BeanSpy"));   
             Assert.assertEquals("Wrong MBean/property has been excluded" , 2, 
                     thisMBeanExclusions.size());
         
             Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("*")==0);
             Assert.assertTrue( thisMBeanExclusions.get(1).compareTo("entropy")==0);
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Error loading XML");
         }
         
         try
         {
             ArrayList<String> thisMBeanExclusions = _jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                     new ObjectName("Catalina:name=SomeBean"));   
             Assert.assertEquals("Wrong MBean/property has been excluded" , 1, 
                     thisMBeanExclusions.size());
         
             Assert.assertTrue( thisMBeanExclusions.get(0).compareTo("entropy")==0);
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Error loading XML");
         }
         
         //process the second JMXStore and check the exclusions
         allExclusions = _jmxFilterParameters.GetJMXStoreExclusions("com.interopbridges.scx.jmx.JdkJMXAbstraction");
         
         Assert.assertEquals("Two MBean should be excluded" , 2, allExclusions.size());
         
         try
         {
             Hashtable<String,String> thisMBeanExclusions = _jmxFilterParameters.toHashTable(_jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                     new ObjectName("Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/manager")));   
             Assert.assertEquals("Wrong MBean/property has been excluded" , 3, 
                     thisMBeanExclusions.size());
         
             Assert.assertTrue( thisMBeanExclusions.get("entropy")!=null);
             Assert.assertTrue( thisMBeanExclusions.get("SomeOtherProperty")!=null);
             Assert.assertTrue( thisMBeanExclusions.get("MyAttribute")!=null);
             Assert.assertTrue( thisMBeanExclusions.get("BogusAttribute")==null);
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Error loading XML");
         }
         
         try
         {
             Hashtable<String,String> thisMBeanExclusions = _jmxFilterParameters.toHashTable(_jmxFilterParameters.GetMBeanExclusions(allExclusions, 
                     new ObjectName("Catalina:name=manager")));   
             Assert.assertEquals("Wrong MBean/property has been excluded" , 2, 
                     thisMBeanExclusions.size());
         
             Assert.assertTrue( thisMBeanExclusions.get("entropy")!=null);
             Assert.assertTrue( thisMBeanExclusions.get("SomeOtherProperty")!=null);
         }
         catch(Exception e)
         {
             Assert.fail("Unexpected Error loading XML");
         }
     }      
}