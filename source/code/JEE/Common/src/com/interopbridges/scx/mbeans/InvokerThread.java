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

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import com.interopbridges.scx.jmx.IJMX;

/**
 * <p>
 * Helper Thread class used to invoke the MBean method. 
 * This is to avoid a synchronous call to the Invoked method which could 
 * potentially lockup resources.    
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class InvokerThread extends Thread 
{
    /**
     * <p>
     * Name JMX server used to invoke the MBean method.
     * </p>
     */
    private IJMX jmxServer;
    
    /**
     * <p>
     * The ObjectName of the MBean.
     * </p>
     */
    private ObjectName objName;
    
    /**
     * <p>
     * The name of the MBean method to invoke.
     * </p>
     */
    private String operationName;
    
    /**
     * <p>
     * The parameters to pass to the MBean method.
     * </p>
     */
    private Object[] params;
    
    /**
     * <p>
     * The parameter types.
     * </p>
     */
    private String[] signature;
    
    /**
     * <p>
     * The response data from the invoked method.
     * </p>
     */
    private Object invokeResponse;
    
    /**
     * <p>
     * The result of the MBean method invoked.
     * </p>
     */
    private Exception invokeResult;
    
    /**
     * <p>
     * Constructor of the object.
     * </p>
     * 
     * <p>
     * This Class is instantiated when the used intends on invoking
     * a MBean method.
     * </p>
     * 
     * @param jmxServer
     *            the JMX server where the MBean resides
     * @param objName
     *            the object name of the MBean
     * @param operationName
     *            the name of a method to invoke
     * @param params
     *            array of parameters to pass to the MBean method
     * @param signature
     *            array of parameter signatures to pass to the MBean method
     */
    public InvokerThread(IJMX jmxServer, ObjectName objName, 
                         String operationName, Object[] params, 
                         String[] signature)
    {
        this.jmxServer = jmxServer;
        this.objName = objName;
        this.operationName = operationName;
        this.params = params.clone();
        this.signature = signature.clone();
    }
    
    /**
     * <p>
     * Thread run interface
     * </p>
     */
    public void run()
    {
        try
        {
            invokeResponse = jmxServer.invoke(objName, operationName, params, signature);
            invokeResult = null;
        }
        catch(InstanceNotFoundException e)
        {
            invokeResult = e;
        }
        catch(ReflectionException e)
        {
            invokeResult = e;
        }
        catch(MBeanException e)
        {
            invokeResult = e;
        }
        catch(IOException e)
        {
            invokeResult = e;
        }
    }
    
    /**
     * <p>
     * Getter function for returning the output of the invoked method
     * </p>
     * 
     * @return the data returned by the method invocation
     */
    public Object getInvokeResponse()
    {
        return invokeResponse;
    }
    
    /**
     * <p>
     * Getter function for returning the result of the invoked method
     * </p>
     * 
     * @return the result of the method call, null 
     *          is returned if the method executes with no error. 
     *          
     */
    public Exception getInvokeResult()
    {
        return invokeResult;
    }
}
