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

package com.interopbridges.scx.jeeinfo;

import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.interopbridges.scx.jeestats.GenericStatistics;
import com.interopbridges.scx.jeestats.Statistic;
import com.interopbridges.scx.jeestats.StatisticMethod;


/**
 * <p>
 * Retrieves the Java JVM values from the ManagementFactory.
 * The management factory  platform MXBean(s) represents the management 
 * interface of a component of the Java virtual machine. The platform MXBeans 
 * were introduces in J2SE 5 and therefore none of the statistic modules
 * will work on Java versions prior to 1.5. 
 * </p>
 * 
 * <p>
 * The return values are read only and cannot be modified.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class JeeJVMInfo extends GenericStatistics
{
    
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname         = "JavaVirtualMachine";
    
    /**
     * <p>
     * Name identifying the statistical value containing the JVM Name
     * </p>
     */
    private static final String Name         = "Name";
    
    /**
     * <p>
     * Name identifying the statistical value containing the JVM Version
     * </p>
     */
    private static final String Version      = "Version";
    
    /**
     * <p>
     * Name identifying the statistical value containing the JVM Build Version
     * </p>
     */
    private static final String BuildVersion      = "BuildVersion";
    
    /**
     * <p>
     * Name identifying the statistical value containing the JVM Vendor Name
     * </p>
     */
    private static final String VendorName   = "VendorName";
    
    /**
     * <p>
     * Name identifying the statistical value containing the JVM startup options
     * </p>
     */
    private static final String StartupOption  = "StartupOption";
    
    /**
     * <p>
     * Name identifying the statistical value containing the JVM startup time
     * </p>
     */
    private static final String StartupTime    = "StartupTime";
    
    /**
     * <p>
     * Name identifying the statistical value containing the JVM class path
     * </p>
     */
    private static final String ClassPath      = "ClassPath";
    
    /**
     * <p>
     * Name identifying the statistical value containing the JVM library path
     * </p>
     */
    private static final String LibraryPath   = "LibraryPath";
    
    /**
     * <p>
     * Name identifying the value containing the JVM Java Install Directory
     * </p>
     */
    private static final String JavaInstallDirectory   = "JavaInstallDirectory";
    
    /**
     * <p>
     * Default constructor
     * </p>
     */
    public JeeJVMInfo()
    {
       super(StatisticGroupname);    
    }
    
    /**
     * <p>
     * Retrieves the name of the JVM from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getName()
    {
        return     new Statistic(Name,    String.class,
                ManagementFactory.getRuntimeMXBean().getVmName());
    }
    
    /**
     * <p>
     * Retrieves the version of the JVM from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getVersion()
    {
        return     new Statistic(Version,    String.class,
                System.getProperty("java.version"));
    }
    
    /**
     * <p>
     * Retrieves the build version of the JVM from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getBuildVersion()
    {
        return     new Statistic(BuildVersion,    String.class,
        ManagementFactory.getRuntimeMXBean().getVmVersion());
    }
    
    /**
     * <p>
     * Retrieves the vendor name of the JVM from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getVendorName()
    {
        return     new Statistic(VendorName,    String.class,
        ManagementFactory.getRuntimeMXBean().getSpecVendor());
    }
    
    /**
     * <p>
     * Retrieves the startup options of the JVM from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getStartupOption()
    {
        boolean first=true;
        List<String> sl = ManagementFactory.getRuntimeMXBean().getInputArguments();
        StringBuffer val = new StringBuffer();
        for(Iterator<String> it=sl.iterator();it.hasNext();)
        {
            if(!first)
                val.append(", ");
            first=false;
            val.append(it.next());
        }
        
        return     new Statistic(StartupOption,    String.class, val );
    }
    
    /**
     * <p>
     * Retrieves the startup time of the JVM from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getStartupTime()
    {
        Date d = new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
        return     new Statistic(StartupTime,    String.class, d.toString() );
    }
    
    /**
     * <p>
     * Retrieves the ClassPath of the JVM from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getClassPath()
    {
        return     new Statistic(ClassPath,    String.class,
        ManagementFactory.getRuntimeMXBean().getBootClassPath());
    }
    
    /**
     * <p>
     * Retrieves the ClassPath of the JVM from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getLibraryPath()
    {
        return     new Statistic(LibraryPath,    String.class,
        ManagementFactory.getRuntimeMXBean().getLibraryPath());
    }
    
   /**
     * <p>
     * Retrieves the Java install Directory of the JVM from the Java runtime
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getJavaInstallDirectory()
    {
        return     new Statistic(JavaInstallDirectory,    String.class,
        System.getProperty("java.home"));
    }
}
