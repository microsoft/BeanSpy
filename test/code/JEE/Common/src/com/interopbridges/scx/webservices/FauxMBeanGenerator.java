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

package com.interopbridges.scx.webservices;

import java.util.ArrayList;
import java.util.Hashtable;
import java.io.IOException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.jboss.management.j2ee.J2EEApplication;
import org.jboss.management.j2ee.J2EEApplicationMBean;

import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.mbeans.BasicTypeArrays;
import com.interopbridges.scx.mbeans.BasicTypeArraysMBean;
import com.interopbridges.scx.mbeans.BasicTypes;
import com.interopbridges.scx.mbeans.BasicTypesMBean;
import com.interopbridges.scx.mbeans.BasicTypesWrapper;
import com.interopbridges.scx.mbeans.BasicTypesWrapperClass;
import com.interopbridges.scx.mbeans.BasicTypesWrapperClassMBean;
import com.interopbridges.scx.mbeans.BasicTypesWrapperMBean;
import com.interopbridges.scx.mbeans.ComplexType;
import com.interopbridges.scx.mbeans.ComplexTypeForMaxFileSize;
import com.interopbridges.scx.mbeans.EscapedObjectName;
import com.interopbridges.scx.mbeans.EscapedObjectNameMBean;
import com.interopbridges.scx.util.StringMangler;

/**
 * 
 * <p>
 * This class will generate Fake MBeans. These are real MBeans that contain
 * fictitious information. It is a temporary measure that is need until there is
 * the necessary information to dynamically generate this information at
 * run-time.
 * </p>
 * 
 * <p>
 * This class is a temporary class associated with Backlog Item #18711 (As a
 * developer I want to retrieve MBean instances so that the information can be
 * returned to the http client), and will should be depreciated and removed when
 * it is required to return the real MBean information.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 * @see <a href="http://blogs.sun.com/jmxetc/entry/how_can_i_find_all">External
 *      Link illustrating how to perform simple JMX queries</a>
 */
public class FauxMBeanGenerator {
    
    /**
     * <p>
     * Abstraction representing the JMX Store
     * </p>
     */
    private IJMX _jmxStore;

    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    protected ILogger _logger;

    /**
     * <p>
     * List of (fake) primitive (non-arrays) types to be register with the store.
     * </p>
     */
    private ArrayList<BasicTypes> _basicTypes;
    
    /**
     * <p>
     * List of (fake) primitive wrapper class types to be register with the store.
     * </p>
     */
    private ArrayList<BasicTypesWrapperClass> _basicTypesWrapperClass;
    
    /**
     * <p>
     * List of (fake) primitive wrapper class types to be register with the store.
     * </p>
     */
    private ArrayList<ComplexType> _complexType;
    
    /**
     * <p>
     * List of (fake) primitive (arrays) types to be register with the store.
     * </p>
     */
    private ArrayList<BasicTypeArrays> _basicTypeArrays;

    /**
     * <p>
     * List of (fake) objects that wrap an object with basic types to be
     * register with the store.
     * </p>
     */
    private ArrayList<BasicTypesWrapper> _basicTypesWrapper;

    /**
     * <p>
     * List of (fake) endpoints to be registered with the store
     * </p>
     */
    private ArrayList<EndpointMBean> _endpoints;

    /**
     * <p>
     * List of (fake) operations to be registered with the store
     * </p>
     */
    private ArrayList<OperationMBean> _operations;

    /**
     * <p>
     * List of (fake) operations calls to be register with the store.
     * </p>
     */
    private ArrayList<OperationCallMBean> _operationCalls;
    
    /**
     * <p>
     * List of (fake) J2EEApplication for JBoss
     * </p>
     */
    private ArrayList<J2EEApplication> _jbossJ2EEApplications;

    /**
     * <p>
     * List of (fake) objects that wrap an object with basic types to be
     * register with the store.
     * </p>
     */
    private ArrayList<EscapedObjectName> _escapedObjectName;

    private ArrayList<ComplexTypeForMaxFileSize> _complexTypeForMaxFileSize;

    /**
     * <p>
     * Default Constructor
     * </p>
     */
    public FauxMBeanGenerator(IJMX jmxStore) {
        this._jmxStore = jmxStore;
        this._logger = LoggingFactory.getLogger();

        this._endpoints = new ArrayList<EndpointMBean>(1);
        String humanReadableEndpoint = "http://localhost:9080/WebServiceProject/CalculatorService";
        humanReadableEndpoint = StringMangler
                .EncodeForJmx(humanReadableEndpoint);
        Endpoint calc = new Endpoint(humanReadableEndpoint);
        this._endpoints.add(calc);

        this._operations = new ArrayList<OperationMBean>(4);
        OperationMBean add = new Operation("add");
        OperationMBean multiply = new Operation("multiply");
        OperationMBean subtract = new Operation("subtract");
        OperationMBean divide = new Operation("divide");
        this._operations.add(add);
        this._operations.add(multiply);
        this._operations.add(subtract);
        this._operations.add(divide);

        this._operationCalls = new ArrayList<OperationCallMBean>(4);
        OperationCallMBean multiplyToAdd = new OperationCall(calc, multiply,
                calc, add);
        OperationCallMBean divideToSubtract = new OperationCall(calc, divide,
                calc, subtract);
        this._operationCalls.add(multiplyToAdd);
        this._operationCalls.add(divideToSubtract);

        this._basicTypes = new ArrayList<BasicTypes>(1);
        BasicTypes basic = new BasicTypes();
        this._basicTypes.add(basic);

        this._basicTypesWrapperClass = new ArrayList<BasicTypesWrapperClass>(1);
        BasicTypesWrapperClass basicWrapperClass = new BasicTypesWrapperClass();
        this._basicTypesWrapperClass.add(basicWrapperClass);

        this._complexType = new ArrayList<ComplexType>(1);
        ComplexType complexType = new ComplexType();
        this._complexType.add(complexType);
        
        this._complexTypeForMaxFileSize = new ArrayList<ComplexTypeForMaxFileSize>(1);
        ComplexTypeForMaxFileSize complexType2 = new ComplexTypeForMaxFileSize();
        this._complexTypeForMaxFileSize.add(complexType2);

        this._basicTypeArrays = new ArrayList<BasicTypeArrays>(1);
        BasicTypeArrays basicArray = new BasicTypeArrays();
        this._basicTypeArrays.add(basicArray);
        
        this._basicTypesWrapper = new ArrayList<BasicTypesWrapper>(1);
        BasicTypesWrapper basicWrapper = new BasicTypesWrapper();
        this._basicTypesWrapper.add(basicWrapper);
        
        this._jbossJ2EEApplications = new ArrayList<J2EEApplication>(1);
        J2EEApplication j2eeApplication = new J2EEApplication();
        this._jbossJ2EEApplications.add(j2eeApplication);

        this._escapedObjectName = new ArrayList<EscapedObjectName>(1);
        EscapedObjectName escapedObjectName = new EscapedObjectName();
        this._escapedObjectName.add(escapedObjectName);

    }

    /**
     * <p>
     * Generate a set of fake Endpoint MBeans that can be used just like real
     * ones (tm). Note this method is meant for the registration process.
     * </p>
     */
    public ArrayList<EndpointMBean> getEndpointMbeans() {
        return this._endpoints;
    }

    /**
     * <p>
     * Generate a set of fake Operation MBeans that can be used just like real
     * ones (tm).
     * </p>
     */
    public ArrayList<OperationMBean> getOperationMbeans() {
        return this._operations;
    }

    /**
     * <p>
     * Generate a set of fake Operation Calls MBeans that can be used just like
     * real ones (tm).
     * </p>
     */
    public ArrayList<OperationCallMBean> getOperationCallsMbeans() {
        return this._operationCalls;
    }

    /**
     * <p>
     * A simple method for registering MBeans with the JMX Store
     * </p>
     * 
     * <p>
     * This method should not survive past Iteration 4 (May 2010).
     * <p>
     * 
     * @throws Exception
     *             If something went wrong
     */
    public void run() throws Exception {

        this._logger.info(new StringBuffer("Number of MBeans registered: ")
                .append(this._jmxStore.getMBeanCount()).toString());

        this.registerMBeans();

        this._logger.info(new StringBuffer("Number of MBeans registered: ")
                .append(this._jmxStore.getMBeanCount()).toString());
    }

    /**
     * <p>
     * Register the fake MBeans with the JMX Store.
     * </p>
     * 
     * @throws MalformedObjectNameException
     *             If ObjectName could not be generated properly for an MBean
     * @throws InstanceAlreadyExistsException
     *             thrown if the given MBean is already registered with the
     *             store
     * @throws MBeanRegistrationException
     *             thrown if there is a general error with registering the MBean
     * @throws NotCompliantMBeanException
     *             throw if the given MBean fails some simple introspection
     *             checks
     */
    protected void registerMBeans() throws MalformedObjectNameException,
            InstanceAlreadyExistsException, MBeanRegistrationException,
            NotCompliantMBeanException, IOException {

        for (EndpointMBean bean : this.getEndpointMbeans()) {
            String domainName = "com.interopbridges.scx";
            Hashtable<String, String> keys = new Hashtable<String, String>();
            keys.put("jmxType", bean.getJmxType());
            keys.put("Url", bean.getUrl());
            ObjectName name = new ObjectName(domainName, keys);
            this._logger.info(new StringBuffer("Registering MBean for ")
                    .append(name.getCanonicalKeyPropertyListString())
                    .toString());
            this._jmxStore.registerMBean(bean, name);
        }

        for (OperationMBean bean : this.getOperationMbeans()) {
            String domainName = "com.interopbridges.scx";
            Hashtable<String, String> keys = new Hashtable<String, String>();
            keys.put("jmxType", bean.getJmxType());
            keys.put("name", bean.getName());
            ObjectName name = new ObjectName(domainName, keys);
            this._logger.info(new StringBuffer("Registering MBean for ")
                    .append(name.getCanonicalKeyPropertyListString())
                    .toString());
            this._jmxStore.registerMBean(bean, name);
        }

        for (OperationCallMBean bean : this.getOperationCallsMbeans()) {
            String domainName = "com.interopbridges.scx";
            Hashtable<String, String> keys = new Hashtable<String, String>();
            keys.put("jmxType", bean.getJmxType());
            keys.put("sourceEndpoint", bean.getSourceEndpoint().getUrl());
            keys.put("sourceOperation", bean.getSourceOperation().getName());
            keys.put("targetEndpoint", bean.getTargetEndpoint().getUrl());
            keys.put("targetOperation", bean.getTargetOperation().getName());
            ObjectName name = new ObjectName(domainName, keys);
            this._logger.info(new StringBuffer("Registering MBean for ")
                    .append(name.getCanonicalKeyPropertyListString())
                    .toString());
            this._jmxStore.registerMBean(bean, name);
        }

        for (BasicTypesMBean bean : this._basicTypes) {
            String domainName = "com.interopbridges.scx";
            Hashtable<String, String> keys = new Hashtable<String, String>();
            keys.put("theLabel", bean.getTheLabel());
            keys.put("isBoolean", Boolean.toString(bean.getIsBoolean()));
            keys.put("byte", Byte.toString(bean.getByte()));
            keys.put("charLetter", String.valueOf(bean.getCharLetter()));
            keys.put("doubleNumber", Double.toString(bean.getDoubleNumber()));
            keys.put("floatNumber", Float.toString(bean.getFloatNumber()));
            keys.put("intNumber", Integer.toString(bean.getIntNumber()));
            keys.put("longNumber", Long.toString(bean.getLongNumber()));
            keys.put("shortNumber", Short.toString(bean.getShortNumber()));
            ObjectName name = new ObjectName(domainName, keys);
            this._logger.info(new StringBuffer("Registering MBean for ")
                    .append(name.getCanonicalKeyPropertyListString())
                    .toString());
            this._jmxStore.registerMBean(bean, name);
        }
        
        for (BasicTypesWrapperClassMBean bean : this._basicTypesWrapperClass) {
            String domainName = "com.interopbridges.scx";
            Hashtable<String, String> keys = new Hashtable<String, String>();
            keys.put("theLabel",  bean.getTheLabel());
            keys.put("boolean",   Boolean.toString(bean.getBoolean()));
            keys.put("byte",      Byte.toString(bean.getByte()));
            keys.put("character", String.valueOf(bean.getCharacter()));
            keys.put("double",    Double.toString(bean.getDouble()));
            keys.put("float",     Float.toString(bean.getFloat()));
            keys.put("integer",   Integer.toString(bean.getInteger()));
            keys.put("long",      Long.toString(bean.getLong()));
            keys.put("short",     Short.toString(bean.getShort()));
            ObjectName name = new ObjectName(domainName, keys);
            this._logger.info(new StringBuffer("Registering MBean for ")
                    .append(name.getCanonicalKeyPropertyListString())
                    .toString());
            this._jmxStore.registerMBean(bean, name);
        }
         
        for (ComplexType bean : this._complexType) {
            String domainName = "com.interopbridges.scx";
            Hashtable<String, String> keys = new Hashtable<String, String>();
            keys.put("theLabel",  bean.getTheLabel());
            keys.put("complexitem",  bean.getComplexClass().toString());
            ObjectName name = new ObjectName(domainName, keys);
            this._logger.info(new StringBuffer("Registering MBean for ")
                    .append(name.getCanonicalKeyPropertyListString())
                    .toString());
            this._jmxStore.registerMBean(bean, name);
        }
               
        for (ComplexTypeForMaxFileSize bean : this._complexTypeForMaxFileSize) {
            String domainName = "com.interopbridges.scx";
            Hashtable<String, String> keys = new Hashtable<String, String>();
            keys.put("theLabel",  bean.getTheLabel());
            keys.put("complexitem",  bean.getComplexClass().toString());
            ObjectName name = new ObjectName(domainName, keys);
            this._logger.info(new StringBuffer("Registering MBean for ")
                    .append(name.getCanonicalKeyPropertyListString())
                    .toString());
            this._jmxStore.registerMBean(bean, name);
        }     
        
        for (BasicTypeArraysMBean bean : this._basicTypeArrays) {
            String domainName = "com.interopbridges.scx";
            Hashtable<String, String> keys = new Hashtable<String, String>();
            keys.put("theLabel", bean.getTheLabel());
            ObjectName name = new ObjectName(domainName, keys);
            this._logger.info(new StringBuffer("Registering MBean for ")
                    .append(name.getCanonicalKeyPropertyListString())
                    .toString());
            this._jmxStore.registerMBean(bean, name);
        }

        for (BasicTypesWrapperMBean bean : this._basicTypesWrapper) {
            String domainName = "com.interopbridges.scx";
            Hashtable<String, String> keys = new Hashtable<String, String>();
            keys.put("theLabel", bean.getTheLabel());
            keys.put("payload", bean.getPayload().toString());
            ObjectName name = new ObjectName(domainName, keys);
            this._logger.info(new StringBuffer("Registering MBean for ")
                    .append(name.getCanonicalKeyPropertyListString())
                    .toString());
            this._jmxStore.registerMBean(bean, name);
        }

        for (J2EEApplicationMBean bean : this._jbossJ2EEApplications) {
            ObjectName name = new ObjectName(bean.getObjectName());
            this._logger.info(new StringBuffer("Registering MBean for ")
                    .append(name.getCanonicalKeyPropertyListString())
                    .toString());
            this._jmxStore.registerMBean(bean, name);
        }
        
        for (EscapedObjectNameMBean bean : this._escapedObjectName) {
            ObjectName name = new ObjectName(bean.getObjectName());
            this._logger.info(new StringBuffer("Registering MBean for ")
                    .append(name.getCanonicalKeyPropertyListString())
                    .toString());
            this._jmxStore.registerMBean(bean, name);
        }
        
    }
}
