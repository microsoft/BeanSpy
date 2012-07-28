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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.helpers.AttributesImpl;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.ScxExceptionCode;
import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.util.JmxConstant;
import com.interopbridges.scx.util.MsVersion;
import com.interopbridges.scx.util.StringMangler;
import com.interopbridges.scx.xml.CommonXmlTransform;
import com.interopbridges.scx.xml.XMLDoc;

/**
 * Invokes a MBean method on an MBean residing in the JMX store and transforms the
 * result to XML.
 * 
 * @author Geoff Erasmus
 */
public class MBeanInvoker 
{
    /**
     * <p>
     * Map containing Class names and Boxed Names for basic types
     * </p>
     */
    private static final HashMap<String, String> BoxedMap = new HashMap<String, String>();
    static {
        BoxedMap.put("INT", Integer.class.getName());
        BoxedMap.put("INTEGER", Integer.class.getName());
        BoxedMap.put("LONG", Long.class.getName());
        BoxedMap.put("SHORT", Short.class.getName());
        BoxedMap.put("FLOAT", Float.class.getName());
        BoxedMap.put("DOUBLE", Double.class.getName());
        BoxedMap.put("STRING", String.class.getName());
        BoxedMap.put("CHAR", Character.class.getName());
        BoxedMap.put("BYTE", Byte.class.getName());
        BoxedMap.put("BOOLEAN", Boolean.class.getName());
        BoxedMap.put("BOOL", Boolean.class.getName());
        BoxedMap.put(Integer.class.getName().toUpperCase(),Integer.class.getName());
        BoxedMap.put(Long.class.getName().toUpperCase(), Long.class.getName());
        BoxedMap.put(Short.class.getName().toUpperCase(), Short.class.getName());
        BoxedMap.put(Float.class.getName().toUpperCase(), Float.class.getName());
        BoxedMap.put(Double.class.getName().toUpperCase(), Double.class.getName());
        BoxedMap.put(String.class.getName().toUpperCase(), String.class.getName());
        BoxedMap.put(Character.class.getName().toUpperCase(), Character.class.getName());
        BoxedMap.put(Byte.class.getName().toUpperCase(), Byte.class.getName());
        BoxedMap.put(Boolean.class.getName().toUpperCase(), Boolean.class.getName());
    }

    /**
     * <p>
     * Name of the MBean method to Invoke.
     * </p>
     */
    private String MethodName;
    
    /**
     * <p>
     * List of parameters to pass to the invoked method.
     * </p>
     */
    private ArrayList<MBeanMethodParameter> MethodParams;
    
    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    protected ILogger _logger;
    
    /**
     * <p>
     * Interface to getting the desired MBeans from the JMX Store (as XML).
     * </p>
     */
    private MBeanGetter mbeanAccessor;
    
    /**
     * <p>
     * The MBean query string used to specify the MBean to invoke.
     * </p>
     */
    private String MBeanQuery;
    
    /**
     * <p>
     * The ObjectName of the specific MBean to invoke.
     * </p>
     */
    private ObjectName MBeanObjectName;
    
    /**
     * <p>
     * Array of Objects to be passed to Invoke method on the MBean.
     * </p>
     */
    private Object[] parameterValues;
    
    /**
     * <p>
     * Array of Data Types to be passed to Invoke method on the MBean.
     * </p>
     */
    private String[] parameterSignature;

    /**
     * <p>
     * JMX Server housing the MBean to Invoke.
     * </p>
     */
    private IJMX jmxServer;
    

    /**
     * <p>
     * Constructor of the object.
     * </p>
     * 
     * <p>
     * This Class is instantiated when the user intends on invoking
     * a MBean method.
     * </p>
     * 
     * @param _mbeanAccessor
     *            the accessor used to retrieve valid MBeans from the list of MBean Servers
     * @param MBeanQuery
     *            the MBean query string specifying a single MBean
     * @param MethodName
     *            the Method name of a method exposed by the MBean
     * @param MethodParams
     *            array of parameters to pass to the MBean method
     */
    public MBeanInvoker(MBeanGetter _mbeanAccessor, String MBeanQuery, 
            String MethodName, ArrayList<MBeanMethodParameter> MethodParams )
    {
        this.MethodName = MethodName;
        this.mbeanAccessor = _mbeanAccessor;
        this.MethodParams = MethodParams;
        this.MBeanQuery = MBeanQuery;
        this._logger = LoggingFactory.getLogger();
    }
    
    /**
     * <p>
     * Helper function used to perform validation on the input data.
     *   Performs the following:    
     *      Validate that one MBean is returned for the specified query. 
     *      That the MBean method exists and is accessible.
     *      That there are the correct number of parameters.
     *      That the parameter types match.
     *      That we can create objects representing the data types from the input data.   
     * </p>
     * 
     * @throws ScxException
     *             If there was a problem with the number of the parameters,
     *             the parameter types or the data or MBean related issues.
     */
    protected void ValidateMBeans() throws ScxException
    {
        // throws an error if there was an error or 
        // if more than 1 MBean is returned
        HashMap<IJMX, Set<ObjectInstance>> MBeans = FetchMatchingMBeans(MBeanQuery);
        
        try
        {
            jmxServer = MBeans.keySet().iterator().next();
            Set<ObjectInstance> s = MBeans.get(jmxServer);
            ObjectInstance objInst = s.iterator().next();

            //Get a list of all the MBean methods.
            MBeanObjectName = objInst.getObjectName();
            MBeanInfo metadata = jmxServer.getMBeanInfo(MBeanObjectName);
            MBeanOperationInfo[] OperationInfo = metadata.getOperations();
            MBeanParameterInfo[] paramInfoArray=null;
            
            // validate that the Method exists and has the correct parameters.
            boolean methodfound = false;
            ScxException paramException=null;
            for(int i=0;i<OperationInfo.length;i++)
            {
                MBeanOperationInfo mboi = OperationInfo[i]; 
                if(mboi.getName().compareTo(MethodName)==0) 
                {
                    paramInfoArray = mboi.getSignature();
                    try
                    {
                        // it is possible for overloaded functions that the 
                        // parameters do not match, check all matching methods.
                        CheckParams(paramInfoArray);
                        methodfound = true;
                        break;
                    }
                    catch(ScxException e)
                    {
                        paramException = e;  
                    }
                }
            }

            if(!methodfound)
            {
                if(paramException!=null)
                {
                    throw paramException;
                }
                throw new ScxException(ScxExceptionCode.ERROR_INVOKE_METHOD_NOTFOUND);
            }
            parameterValues = new Object[MethodParams.size()];
            parameterSignature = new String[MethodParams.size()];

             
            // Try to construct the parameters of the specified types.
            try
            {
                for(int i=0;i<MethodParams.size();i++)
                {
                    // The +1 for the paramNumber parameter is to make it human readable. 
                    parameterValues[i] = createObject(MethodParams.get(i).getParamType(), MethodParams.get(i).getParamValue(),i+1);
                    parameterSignature[i] = paramInfoArray[i].getType();
                }
            }
            catch(NumberFormatException e)
            {
                throw new ScxException(ScxExceptionCode.ERROR_INVOKE_PARAM_VALUE,e);
            }

        }
        catch (ReflectionException e)
        {
            throw new ScxException(ScxExceptionCode.ERROR_CONNECTING_JMXSTORE,e);
        }
        catch (IntrospectionException e)
        {
            throw new ScxException(ScxExceptionCode.MALFORMED_OBJECT_NAME,e);
        }
        catch (InstanceNotFoundException e)
        {
            throw new ScxException(ScxExceptionCode.MALFORMED_OBJECT_NAME,e);
        }
        catch (IOException e)
        {
            throw new ScxException(ScxExceptionCode.ERROR_CONNECTING_JMXSTORE,e);
        }
        
    }

    /**
     * <p>
     * Helper function used to retrieve the matching MBeans from the MBean servers.
     * </p>
     * 
     * @param MBeanQuery
     *            the MBean query used to fetch matching MBeans
     *            
     * @return Map containing all matching MBeans and their associated MBean stores.
     * 
     * @throws ScxException
     *             If there are more than 1 matching MBeans for the query or 
     *             unable to retrieve any MBeans.
     */
    protected HashMap<IJMX, Set<ObjectInstance>> FetchMatchingMBeans(String MBeanQuery) throws ScxException
    { 
        HashMap<IJMX, Set<ObjectInstance>> MBeans;
        try
        {
            MBeans = mbeanAccessor.getMBeans(MBeanQuery);
            if(MBeans.size()==0)
            {
                throw new ScxException(ScxExceptionCode.ERROR_INVOKE_TOO_FEW_BEANS);
            }
            if(MBeans.size()!=1)
            {
                throw new ScxException(ScxExceptionCode.ERROR_INVOKE_MBEAN_NOT_UNIQUE);
            }
            if(MBeans.get(MBeans.keySet().iterator().next()).size()!=1) 
            {
                throw new ScxException(ScxExceptionCode.ERROR_INVOKE_MBEAN_NOT_UNIQUE);
            }
        }
        catch (NullPointerException e)
        {
            throw new ScxException(ScxExceptionCode.NULL_POINTER_EXCEPTION,e);
        }
        return MBeans;
    }
    
    /**
     * <p>
     * Helper function used to validate that the number of parameters supplied
     * matches the number of parameters specified on the MBean method and
     * that the parameter types match.
     * </p>
     * 
     * @param mbeanparamInfo
     *            array of parameters to check.
     */
    protected void CheckParams(MBeanParameterInfo[] mbeanparamInfo)
    throws ScxException
    {
        if(mbeanparamInfo.length==MethodParams.size())
        {
            //just do parameter matching based on type and position not name
            for(int i=0;i<mbeanparamInfo.length;i++)
            {
                String MethodDataType = BoxedMap.get(mbeanparamInfo[i].getType().toUpperCase()); 
                String datatype = BoxedMap.get(MethodParams.get(i).getParamType().toUpperCase()); 
                if((datatype != null) && (MethodDataType != null))
                {
                    if(MethodDataType.compareToIgnoreCase(datatype)!=0)
                    {
                        throw new ScxException(ScxExceptionCode.ERROR_INVOKE_PARAM_MISMATCH);
                    }
                }
                else
                {
                    throw new ScxException(ScxExceptionCode.ERROR_INVOKE_PARAM_MISMATCH);
                }
                    
            }
        }
        else
        {
            throw new ScxException(ScxExceptionCode.ERROR_INVOKE_PARAM_COUNT_MISMATCH);
        }
    }

    /**
     * <p>
     * Helper function used to create objects of the desired data type from the data.
     * </p>
     * 
     * @param className
     *            the string name for the class to be created.
     * @param value
     *            the value to associate with the object created.
     * @param paramNumber
     *            the value indicating the parameter number we are creating
     *            an object for.
     *            
     * @return an object of the type specified by the parameter
     * 
     * @throws NumberFormatException
     *             If the value of the data cannot be converted into the desired data type.
     * @throws ScxException
     *             If the value of the data is not supplied for a primitive type.
     */
    protected Object createObject(String className, String value, int paramNumber)
            throws NumberFormatException, ScxException
    {
        /*
         * we cannot use the default class constructors for the 
         * className as friendly(boxed) class names can be used
         */ 

        String datatype = BoxedMap.get(className.toUpperCase());
        
        if(((value==null) || (value=="")) && (datatype.compareTo("java.lang.String")!=0))
        {
            Object[] args = {new Integer(paramNumber)};
            throw new ScxException(ScxExceptionCode.ERROR_INVOKE_PARAM_EMPTY, args);
        }
        
        if(datatype.compareTo("java.lang.Integer")==0)
        {
            return new Integer(value);
        }
        else
        if(datatype.compareTo("java.lang.String")==0)
        {
            return new String(value);
        }
        else
        if(datatype.compareTo("java.lang.Long")==0)
        {
            return new Long(value);
        }
        else
        if(datatype.compareTo("java.lang.Boolean")==0)
        {
            return new Boolean(value);
        }
        else
        if(datatype.compareTo("java.lang.Byte")==0)
        {
            return new Byte(value);
        }
        else
        if(datatype.compareTo("java.lang.Short")==0)
        {
            return new Short(value);
        }
        else
        if(datatype.compareTo("java.lang.Float")==0)
        {
            return new Float(value);
        }
        else
        if(datatype.compareTo("java.lang.Double")==0)
        {
            return new Double(value);
        }
        else
        if(datatype.compareTo("java.lang.Character")==0)
        {
            return new Character(value.charAt(0));
        }
        return null;
     }

    /**
     * <p>
     * Invoke the MBean method.
     * </p>
     * 
     * @param MaxWaitMilliseconds
     *            The maximum time to wait for the MBean method to respond.
     * 
     * @return XML representation of the MBeans.
     * 
     * @throws ScxException
     *             If there was an error invoking the MBean method or the 
     *             method call timed out.
     */
    protected Object InvokeMethod(long MaxWaitMilliseconds)
            throws ScxException
    {
        InvokerThread it = new InvokerThread(jmxServer, 
                                             MBeanObjectName, 
                                             MethodName, 
                                             parameterValues, 
                                             parameterSignature);
        
        it.start();
        
        try 
        {
            it.join(MaxWaitMilliseconds);
            if (it.isAlive()) 
            {
                it.interrupt();
                it.join();
                throw new ScxException(ScxExceptionCode.ERROR_INVOKE_TIMEOUT);
            }
        }
        catch(InterruptedException e)
        {
            throw new ScxException(ScxExceptionCode.ERROR_INVOKE_TIMEOUT,e);
        }

        Exception exception = it.getInvokeResult();
        if(exception!=null)
        {
            throw new ScxException(ScxExceptionCode.ERROR_INVOKE_EXCEPTION,exception);
        }
        return it.getInvokeResponse();
    }
    
    /**
     * <p>
     * For a specified MBean, invoke the method using the given parameters, and return an XML
     * representation of the result.
     * </p>
     * 
     * @param MaxTime
     *            the time (in seconds) to wait for the method to return a 
     *            result before terminating the call.
     *            
     * @param MaxSize
     *            the maximum size of the response XML message, this controls the amount of 
     *            memory used to construct the resulting XML response.
     *            
     * @return XML representation of the method result.
     * 
     * @throws ScxException
     *             If there was an error generating the XML or if there was an
     *             error calling the method.
     */
    public StringWriter transformMBeanCall( String MaxTime, String MaxSize)
            throws ScxException 
    {
        AttributesImpl emptyAttributes = new AttributesImpl();
        StringWriter outputStringWriter = new StringWriter();
        StringWriter returnStringWriter;
        TransformerHandler transformer=null;

        try 
        {
            int absMaxSize = getMaxSizeValue(MaxSize);
            
            ValidateMBeans();        
            Object o = InvokeMethod(getMaxTimeValue(MaxTime));

            transformer = XMLDoc.createXmlDocument(outputStringWriter,"UTF-8","no","no");
            transformer.startDocument();
            transformer.startElement("", "", JmxConstant.STR_INVOKERESPONSE, CommonXmlTransform.getOuterMostAttributes());
            transformer.startElement("", "", JmxConstant.STR_RESULT, emptyAttributes);
            transformer.characters(JmxConstant.STR_SUCCESS.toCharArray(), 0, JmxConstant.STR_SUCCESS.length());
            transformer.endElement("", "", JmxConstant.STR_RESULT);
                        
            if(o!=null)
            {
                AttributesImpl atts = new AttributesImpl();      
                atts.addAttribute("", "", "type", "CDATA", o.getClass().getName());
                transformer.startElement("", "", JmxConstant.STR_RESPONSE, atts);

                /*
                 * For future  -  support complex return values like
                 *                CompositeData
                 *                Arrays of basic types
                 *                Arrays of CompositeData
                 */
                String text = o == null ? "null" : StringMangler.DecodeForJmx(o.toString());
                transformer.characters( o.toString().toCharArray(), 0, text.length());
                
                if(outputStringWriter.getBuffer().length() >= absMaxSize)
                {
                    throw new ScxException(ScxExceptionCode.ERROR_INVOKE_RESPONSE_TOO_LARGE);
                }
        
                transformer.endElement("", "", JmxConstant.STR_RESPONSE);
            }
            transformer.endElement("", "", JmxConstant.STR_INVOKERESPONSE);
            transformer.endDocument();
            returnStringWriter = outputStringWriter;
        }
        catch (ScxException e) 
        {
            this._logger.fine(e.getMessage());
            returnStringWriter = new StringWriter();
            returnStringWriter.write(FormatXMLError(e));
        }
        catch (Exception e)
        {
            this._logger.fine(e.getMessage());
            returnStringWriter = new StringWriter();
            returnStringWriter.write(FormatXMLError(new ScxException(ScxExceptionCode.ERROR_TRANSFORMING_INVOKE, e)));
        }
        
        return returnStringWriter;
    }
   
    /**
     * <p>
     * Helper function to create an XML error message, this does not use the 
     * DOM document builder as it may throw errors if there are insufficient resources.
     * </p>
     * 
     * @param exception
     *            The Exception message that needs to be wrapped in a response message
     *   
     * @return String representing an XML document
     * 
     */
    public static String FormatXMLError(Exception exception)
    {
        StringBuffer outputString = new StringBuffer();
        
        outputString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        outputString.append("<").append(JmxConstant.STR_INVOKERESPONSE).append(" version=\"").append(MsVersion.VERSION).append("\">");
        outputString.append("<").append(JmxConstant.STR_RESULT).append(">");
        outputString.append(JmxConstant.STR_ERROR);
        outputString.append("</").append(JmxConstant.STR_RESULT).append(">");
        outputString.append("<").append(JmxConstant.STR_ERRORREASON).append(">");
        outputString.append(exception.getMessage()==null?exception.getClass().getName():exception.getMessage());
        outputString.append("</").append(JmxConstant.STR_ERRORREASON).append(">");
        outputString.append("</").append(JmxConstant.STR_INVOKERESPONSE).append(">");
        
        return outputString.toString();
    }
    
    /**
     * <p>
     * local helper function to retrieve MaxTime parameter (in seconds).
     * </p>
     * 
     * @param value
     *            string representation of the MaxTime value
     *   
     * @return integer value of the parameter (in milliseconds)
     * 
     * @throws ScxException
     *             If there was an error parsing the MaxTime parameter.
     */
    private int getMaxTimeValue(String value) throws ScxException
    {
        // Constant is already in milliseconds
        int retval=JmxConstant.MAX_INVOKE_TIMEOUT;
        
        if(value!=null)
        {
            try
            {
               // input value is in seconds, in addition to parsing it must
               // be converted to milliseconds
               retval = Integer.parseInt(value) * 1000;
               if(retval<0)
               {
                   this._logger.finer("The MaxTime parameter cannot be less than 0.");
                   throw new ScxException(ScxExceptionCode.ERROR_INVOKE_MAXTIME_PARAM_INVALID);
               }
            }
            catch(NumberFormatException e)
            {
                this._logger.finer("The MaxTime parameter cannot be converted to a number.");
                throw new ScxException(ScxExceptionCode.ERROR_INVOKE_MAXTIME_PARAM_INVALID,e);
            }
        }
        return retval;
    }
    
    /**
     * <p>
     * local helper function to retrieve MaxSize parameter.
     * </p>
     * 
     * @param value
     *            string representation of the MaxSize value
     *   
     * @return integer value of the parameter
     * 
     * @throws ScxException
     *             If there was an error parsing the MaxSize parameter.
     */
    private int getMaxSizeValue(String value) throws ScxException
    {
        int retval=JmxConstant.MAXXMLSIZE;
        
        if(value!=null)
        {
            try
            {
               retval = Integer.parseInt(value);
               // The ABS_SMALLEST_INVOKE_RESPONSE_SIZE imposed limit is to 
               // allow for the simplest return message i.e. SUCCESS
               if(retval<JmxConstant.ABS_SMALLEST_INVOKE_RESPONSE_SIZE)
               {
                   this._logger.finer(new StringBuffer("The MaxSize parameter cannot be less than ").
                           append( String.valueOf(JmxConstant.ABS_SMALLEST_INVOKE_RESPONSE_SIZE)).toString());
                   throw new ScxException(ScxExceptionCode.ERROR_INVOKE_MAXSIZE_PARAM_INVALID);
               }
            }
            catch(NumberFormatException e)
            {
                this._logger.finer("The MaxSize parameter cannot be converted to a number.");
                throw new ScxException(ScxExceptionCode.ERROR_INVOKE_MAXSIZE_PARAM_INVALID,e);
            }
        }
        return retval;
    }
    
}
