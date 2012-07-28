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

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;

import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;

/**
 * <p>
 * Simple MBean that implements methods. The primary intention of this
 * class is to mock up a MBean that contains methods with different parameters and
 * return types. 
 * This class is used in the unit tests for verifying that the MBean methods can be invoked.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class DummyInvokeMBean implements DynamicMBean
{
    /**
     * <p>
     * Attribute info for this MBean.
     * </p>
     */
    private MBeanAttributeInfo[] mbai;
    
    /**
     * <p>
     * Operation info for this MBean.
     * </p>
     */
    private MBeanOperationInfo[] mboi;
    
    /**
     * <p>
     * Map of properties contain name,value pairs.
     * </p>
     */
    private HashMap<String, Object> properties;
    
    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    private ILogger _logger;

    public static final String MBeanName = "Microsoft:name=TestMBean";    
    /**
     * <p>
     * Default constructor for the MBean.
     * All the Attribute info and properties are set in the constructor.
     * </p>
     */
    public DummyInvokeMBean()
    {
        this._logger = LoggingFactory.getLogger();
 
        mboi = new MBeanOperationInfo[]
          {
              new MBeanOperationInfo("VoidVoidMethod",      "",
                      null,
                      "void",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("VoidStringMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",String.class.getName(),"String Parameter")},
                      "void",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("VoidIntMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",Integer.class.getName(),"Int Parameter")},
                      "void",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("VoidShortMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",Short.class.getName(),"Short Parameter")},
                      "void",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("VoidLongMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",Long.class.getName(),"Long Parameter")},
                      "void",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("VoidFloatMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",Float.class.getName(),"Float Parameter")},
                      "void",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("VoidDoubleMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",Double.class.getName(),"Double Parameter")},
                      "void",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("VoidByteMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",Byte.class.getName(),"Byte Parameter")},
                      "void",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("VoidBooleanMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",Boolean.class.getName(),"Boolean Parameter")},
                      "void",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("VoidCharMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",Character.class.getName(),"Char Parameter")},
                      "void",MBeanOperationInfo.UNKNOWN),
                      
              new MBeanOperationInfo("StringVoidMethod",      "",
                      null,
                      String.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("IntVoidMethod",      "",
                      null,
                      Integer.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("ShortVoidMethod",      "",
                      null,
                      Short.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("LongVoidMethod",      "",
                      null,
                      Long.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("FloatVoidMethod",      "",
                      null,
                      Float.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("DoubleVoidMethod",      "",
                      null,
                      Double.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("ByteVoidMethod",      "",
                      null,
                      Byte.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("BooleanVoidMethod",      "",
                      null,
                      Boolean.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("CharVoidMethod",      "",
                      null,
                      Character.class.getName(),MBeanOperationInfo.UNKNOWN),
                      
              new MBeanOperationInfo("VoidIntStringMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0","int","int Parameter"), 
                      new MBeanParameterInfo("p1",String.class.getName(),"String Parameter")},
                      "void",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("StringStringMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",String.class.getName(),"String Parameter")},
                      String.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("FloatFloatIntMethod",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0","float","Float Parameter"),
                      new MBeanParameterInfo("p1","int","int Parameter")},
                      "float",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("Sleeper",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("Time","int","Time in milliseconds to sleep before returning")},
                      "void",MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("BigMethod",      "",
                      null,
                      String.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("Overloaded",      "",
                      null,
                      String.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("Overloaded",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",String.class.getName(),"String Parameter")},
                      String.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("Overloaded",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",Integer.class.getName(),"Integer Parameter")},
                      String.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("Overloaded",      "",
                      new MBeanParameterInfo[] {new MBeanParameterInfo("p0",Float.class.getName(),"Float Parameter")},
                      String.class.getName(),MBeanOperationInfo.UNKNOWN),
              new MBeanOperationInfo("ExceptionMethod",      "",
                      null,
                      "void",MBeanOperationInfo.UNKNOWN),
          };
    mbai = new MBeanAttributeInfo[]
          {
              new MBeanAttributeInfo("Name", String.class.getName(), "The name", true, true, false),
              new MBeanAttributeInfo("objectName", String.class.getName(), MBeanName, true, true, false)
          };
        properties = new HashMap<String, Object>();
        properties.put("Name", this.getClass().getName());
        properties.put("objectName", MBeanName);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#getAttribute(java.lang.String)
     */
    public Object getAttribute(String attribute)
            throws AttributeNotFoundException, MBeanException,
            ReflectionException 
    {
        return properties.get(attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#getAttributes(java.lang.String[])
     */
    public AttributeList getAttributes(String[] attributes) 
    {
        AttributeList al = new AttributeList();
        for(int i=0;i<attributes.length;i++)
        {
            try
            {
                al.add(new Attribute(attributes[i],getAttribute(attributes[i])));
            }
            catch(Exception e)
            {
                this._logger.fine(new StringBuffer(
                "Failed to getAttribute for ").append(attributes[i])
                .toString());
            }
        }
        return al;
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#getMBeanInfo()
     */
    public MBeanInfo getMBeanInfo() 
    {
        return new MBeanInfo(this.getClass().getName(),
                "Microsoft Test MBean",mbai,null,mboi,null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#invoke(String, Object[], String[])
     */
    public Object invoke(String actionName, Object[] params, String[] signature)
            throws MBeanException, ReflectionException 
    {
        if (actionName.equals("VoidVoidMethod"))     
        { 
            VoidVoidMethod();
            return null; 
        }else
        if (actionName.equals("VoidStringMethod"))     
        { 
            VoidStringMethod(((String)params[0]).toString());
            return null; 
        }else
        if (actionName.equals("VoidIntMethod"))     
        { 
            VoidIntMethod(((Integer)params[0]).intValue());
            return null; 
        }else
        if (actionName.equals("VoidShortMethod"))     
        { 
            VoidShortMethod(((Short)params[0]).shortValue());
            return null; 
        }else
        if (actionName.equals("VoidLongMethod"))     
        { 
            VoidLongMethod(((Long)params[0]).longValue());
            return null; 
        }else
        if (actionName.equals("VoidFloatMethod"))     
        { 
            VoidFloatMethod(((Float)params[0]).floatValue());
            return null; 
        }else
        if (actionName.equals("VoidDoubleMethod"))     
        { 
            VoidDoubleMethod(((Double)params[0]).doubleValue());
            return null; 
        }else
        if (actionName.equals("VoidByteMethod"))     
        { 
            VoidByteMethod(((Byte)params[0]).byteValue());
            return null; 
        }else
        if (actionName.equals("VoidBooleanMethod"))     
        { 
            VoidBooleanMethod(((Boolean)params[0]).booleanValue());
            return null; 
        }else
        if (actionName.equals("VoidCharMethod"))     
        { 
            VoidCharMethod(((Character)params[0]).charValue());
            return null; 
        }else
            
        if (actionName.equals("StringVoidMethod"))     
        { 
            return StringVoidMethod();
        }else
        if (actionName.equals("IntVoidMethod"))     
        { 
            return IntVoidMethod();
        }else
        if (actionName.equals("ShortVoidMethod"))     
        { 
            return ShortVoidMethod();
        }else
        if (actionName.equals("LongVoidMethod"))     
        { 
            return LongVoidMethod();
        }else
        if (actionName.equals("FloatVoidMethod"))     
        { 
            return FloatVoidMethod();
        }else
        if (actionName.equals("DoubleVoidMethod"))     
        { 
            return DoubleVoidMethod();
        }else
        if (actionName.equals("ByteVoidMethod"))     
        { 
            return ByteVoidMethod();
        }else
        if (actionName.equals("BooleanVoidMethod"))     
        { 
            return BooleanVoidMethod();
        }else
        if (actionName.equals("CharVoidMethod"))     
        { 
            return CharVoidMethod();
        }else

        
        if (actionName.equals("VoidIntStringMethod"))     
        { 
            VoidIntStringMethod(((Integer)params[0]).intValue(),(String)params[1]);
            return null; 
        } 
        else
        if (actionName.equals("StringStringMethod"))     
        { 
            return StringStringMethod(((String)params[0]).toString()); 
        } 
        else
        if (actionName.equals("FloatFloatIntMethod"))     
        { 
            return FloatFloatIntMethod(((Float)params[0]).floatValue(),((Integer)params[1]).intValue()); 
        } 
        else
        if (actionName.equals("Sleeper"))     
        { 
            Sleeper(((Integer)params[0]).intValue()); 
            return null;
        } 
        else
        if (actionName.equals("BigMethod"))     
        { 
            return BigMethod(); 
        }
        else
        if (actionName.equals("Overloaded"))     
        { 
            if(params.length==0) // overloaded - void method
            {
                return Overloaded(); 
            }   
            else
            {
                if(signature[0].compareToIgnoreCase("java.lang.String")==0)
                {
                    return Overloaded(((String)params[0]).toString()); 
                }
                else
                if(signature[0].compareToIgnoreCase("java.lang.Integer")==0)
                {
                    return Overloaded(((Integer)params[0]).intValue()); 
                }
                else
                if(signature[0].compareToIgnoreCase("java.lang.Float")==0)
                {
                    return Overloaded(((Float)params[0]).floatValue()); 
                }
            }
        }
        else
        if (actionName.equals("ExceptionMethod"))     
        { 
            ExceptionMethod(); 
            return null;
        }
        throw new UnsupportedOperationException();      
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#setAttribute(Attribute)
     */
    public void setAttribute(Attribute attribute)
            throws AttributeNotFoundException, InvalidAttributeValueException,
            MBeanException, ReflectionException 
    {
        Object ans = properties.get(attribute.getName());
        if(ans!=null)
        {
            properties.put(attribute.getName(), attribute.getValue());
        }
        else
        {
            throw new AttributeNotFoundException("No such property: " + attribute);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see DynamicMBean#setAttributes(AttributeList)
     */
    public AttributeList setAttributes(AttributeList attributes) 
    {
        List<Attribute> la = attributes.asList();
        AttributeList ret = new AttributeList();
        for(int i=0;i<la.size();i++)
        {
            
            Object ans = properties.get(la.get(i).getName());
            if(ans!=null)
            {
                try
                {
                    setAttribute(la.get(i));
                    ret.add(new Attribute(la.get(i).getName(), la.get(i).getValue()));
                }
                catch(Exception e)
                {
                    this._logger.fine(new StringBuffer(
                    "Failed to setAttribute for ").append(la.get(i).getName())
                    .toString());
                }
            }
        }
        return ret;
    }

    /**
     * <p>
     * Implemented method that takes no input and returns no data.
     * </p>
     */
    public void VoidVoidMethod()
    {
        this._logger.fine(new StringBuffer("Void Void method called").toString());
    }
    
    /**
     * <p>
     * Implemented method that takes a string as input and returns no data.
     * </p>
     * @param param
     *          dummy input parameter that is output to the console
     */
    public void VoidStringMethod(String param)
    {
        this._logger.fine(new StringBuffer("Void String method called").toString());
    }
    
    /**
     * <p>
     * Implemented method that takes a int as input and returns no data.
     * </p>
     * @param param
     *          dummy input parameter that is output to the console
     */
    public void VoidIntMethod(int param)
    {
        this._logger.fine(new StringBuffer("Void int method called").toString());
    }
    
    /**
     * <p>
     * Implemented method that takes a short as input and returns no data.
     * </p>
     * @param param
     *          dummy input parameter that is output to the console
     */
    public void VoidShortMethod(short param)
    {
        this._logger.fine(new StringBuffer("Void short method called").toString());
    }
    
    /**
     * <p>
     * Implemented method that takes a long as input and returns no data.
     * </p>
     * @param param
     *          dummy input parameter that is output to the console
     */
    public void VoidLongMethod(long param)
    {
        this._logger.fine(new StringBuffer("Void long method called").toString());
    }
    
    /**
     * <p>
     * Implemented method that takes a float as input and returns no data.
     * </p>
     * @param param
     *          dummy input parameter that is output to the console
     */
    public void VoidFloatMethod(float param)
    {
        this._logger.fine(new StringBuffer("Void float method called").toString());
    }
    
    /**
     * <p>
     * Implemented method that takes a double as input and returns no data.
     * </p>
     * @param param
     *          dummy input parameter that is output to the console
     */
    public void VoidDoubleMethod(double param)
    {
        this._logger.fine(new StringBuffer("Void float method called").toString());
    }
    
    /**
     * <p>
     * Implemented method that takes a string as input and returns no data.
     * </p>
     * @param param
     *          dummy input parameter that is output to the console
     */
    public void VoidByteMethod(byte param)
    {
        this._logger.fine(new StringBuffer("Void byte method called").toString());
    }
    
    /**
     * <p>
     * Implemented method that takes a boolean as input and returns no data.
     * </p>
     * @param param
     *          dummy input parameter that is output to the console
     */
    public void VoidBooleanMethod(boolean param)
    {
        this._logger.fine(new StringBuffer("Void boolean method called").toString());
    }

    /**
     * <p>
     * Implemented method that takes a char as input and returns no data.
     * </p>
     * @param param
     *          dummy input parameter that is output to the console
     */
    public void VoidCharMethod(char param)
    {
        this._logger.fine(new StringBuffer("Void char method called").toString());
    }


    /**
     * <p>
     * Implemented method that takes no input and returns a string.
     * </p>
     * @returns
     *          test data
     */
    public String StringVoidMethod()
    {
        return "Test data";
    }

    /**
     * <p>
     * Implemented method that takes no parameters and returns a int.
     * </p>
     * 
     * @return test data
     */
    public int IntVoidMethod()
    {
        return Integer.MAX_VALUE;
    }
    
    /**
     * <p>
     * Implemented method that takes no parameters and returns a short.
     * </p>
     * 
     * @return test data
     */
    public short ShortVoidMethod()
    {
        return Short.MAX_VALUE;
    }
    
    /**
     * <p>
     * Implemented method that takes no parameters and returns a long.
     * </p>
     * 
     * @return test data
     */
    public long LongVoidMethod()
    {
        return Long.MAX_VALUE;
    }
    
    /**
     * <p>
     * Implemented method that takes no parameters and returns a float.
     * </p>
     * 
     * @return test data
     */
    public float FloatVoidMethod()
    {
        return Float.MAX_VALUE;
    }
    
    /**
     * <p>
     * Implemented method that takes no parameters and returns a double.
     * </p>
     * 
     * @return test data
     */
    public double DoubleVoidMethod()
    {
        return Double.MAX_VALUE;
    }
    
    /**
     * <p>
     * Implemented method that takes no parameters and returns a byte.
     * </p>
     * 
     * @return test data
     */
    public byte ByteVoidMethod()
    {
        return Byte.MAX_VALUE;
    }
    
    /**
     * <p>
     * Implemented method that takes no parameters and returns a boolean.
     * </p>
     * 
     * @return test data
     */
    public boolean BooleanVoidMethod()
    {
        return true;
    }
    
    /**
     * <p>
     * Implemented method that takes no parameters and returns a char.
     * </p>
     * 
     * @return test data
     */
    public char CharVoidMethod()
    {
        return 'a';
    }
    
    /**
     * <p>
     * Implemented method that takes two input parameters and returns no data.
     * </p>
     * @param param1
     *          dummy input parameter that is output to the console
     * @param param2
     *          dummy input parameter that is output to the console
     */
    public void VoidIntStringMethod(int param1, String param2 )
    {
        this._logger.fine(new StringBuffer(
                "Void String,int method called IntArg=").append(String.valueOf(param1)).
                append (" StringArg="+param2).append(String.valueOf(param2))
                .toString());
    }
    
    
    /**
     * <p>
     * Implemented method that takes a string as a parameter and returns the string 
     * and a random number.
     * </p>
     * 
     * @param param1
     *          dummy input parameter that is output to the console
     * 
     * @return String containing the input parameter and a concatenated random number
     */
    public String StringStringMethod(String param1)
    {
        Random r = new Random();
        int retval = r.nextInt();
        return param1+String.valueOf(retval);
    }
    
    /**
     * <p>
     * Implemented method that takes a float and integer as input parameters and returns 
     * a float containing the product of the two values.
     * </p>
     * 
     * @param param1
     *          multiplicand
     * @param param2
     *          multiplicand
     * 
     * @return product of the input parameters
     */
    public float FloatFloatIntMethod(float param1, int param2)
    {
        return param1*param2;
    }
    
    /**
     * <p>
     * Implemented method that takes a specified time to run.
     * </p>
     * 
     * @param tim
     *          time in milliseconds to sleep 
     * @throws Exception         
     */
    public void Sleeper(int tim) throws MBeanException
    {
        try
        {
            Thread.sleep(tim);
        }
        catch(InterruptedException e)
        {
            throw new MBeanException(e,"Sleeper Interrupted");
        }
    }

    /**
     * <p>
     * Implemented method that returns 1000 bytes of data. 
     * </p>
     * 
     * @return String containing some data
     */
    public String BigMethod()
    {
        String stat = "1234567890";
        StringBuffer sb = new StringBuffer(1000);
        
        for(int i=0;i<100;i++)
        {
            sb.insert(i*10, stat);
        }
        return sb.toString();
    }
    
    /**
     * <p>
     * Implemented overloaded method that returns a string. 
     * </p>
     * 
     * @return String containing some data
     */
    public String Overloaded()
    {
        return StringVoidMethod();
    }
    
    /**
     * <p>
     * Implemented overloaded method that returns a string. 
     * </p>
     * 
     * @param str
     *          input string 
     * @return String containing some data
     */
    public String Overloaded(String str)
    {
        return str;
    }
    
    /**
     * <p>
     * Implemented overloaded method that returns a string. 
     * </p>
     * 
     * @param val
     *          int parameter value 
     * @return String containing some data
     */
    public String Overloaded(int val)
    {
        return String.valueOf(val);
    }
    
    /**
     * <p>
     * Implemented overloaded method that returns a string. 
     * </p>
     * 
     * @param val
     *          float parameter value 
     * @return String containing some data
     */
    public String Overloaded(float val)
    {
        return String.valueOf(val);
    }
    
    /**
     * <p>
     * Implemented method that throws a MBeanException.
     * </p>
     * @throws MBeanException
     *          An arbitrary exception for testing
     */
    public void ExceptionMethod() throws MBeanException
    {
       throw new MBeanException(new Exception("Exception method"));
    }
}
