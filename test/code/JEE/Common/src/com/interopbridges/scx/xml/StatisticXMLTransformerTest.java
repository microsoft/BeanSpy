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

import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.interopbridges.scx.jeestats.CannedStatistics;
import com.interopbridges.scx.jeestats.ClassLoaderStatistics;
import com.interopbridges.scx.jeestats.GCStatistics;
import com.interopbridges.scx.jeestats.JITCompilerStatistics;
import com.interopbridges.scx.jeestats.MemoryStatistics;
import com.interopbridges.scx.jeestats.RuntimeStatistics;
import com.interopbridges.scx.jeestats.Statistic;
import com.interopbridges.scx.jeestats.StatisticGroup;
import com.interopbridges.scx.jeestats.ThreadStatistics;
import com.interopbridges.scx.mxbeanextender.MXBeanExtender;
import com.interopbridges.scx.util.SAXParser;

/**
 * Testing the transform of Statistic data to XML that is to be returned as a REST
 * response.<br>
 * 
 * @author Geoff Erasmus
 * 
 */
public class StatisticXMLTransformerTest {

	/**
	 * <p>
	 * Test Setup/preparation method that resets/initializes all test specific
	 * variables.
	 * </p>
	 */
	@Before
	public void setup() {
	}

	/**
	 * <p>
	 * Method invoked after each unit-test in this class.
	 * </p>
	 */
	@After
	public void TearDown() {
	}

	/**
	 * <p>
	 * Verify the transform of a single Statistic transform. <br>
	 * </p>
	 * 
	 * <p>
	 * Ideally, the XML would look something like this:<br>
	 * 
	 * <pre>
	 * &lt;Stats&gt;
	 * 	&lt;Memory&gt;
	 *   &lt;Properties&gt;
	 *     &lt;HeapUsedMemory type="long"&gt;10000&lt;/HeapUsedMemory&gt; 
	 *   &lt;/Properties&gt;
	 * 	&lt;/Memory&gt;
	 * &lt;/Stats&gt;
	 * </pre>
	 * 
	 * </p>
	 */
	@Test
	public void verifySingleStatisticTransform() throws Exception {
		
		Statistic stat = (new CannedStatistics()).getCannedStat1();
		StatisticXMLTransformer st = new StatisticXMLTransformer();
		
		String xml = st.transformSingleStatistic(MXBeanExtender.StatisticXMLTag, stat.getStatisticName(),stat).toString();

		new SAXParser().customQueryAssertForXmlValidationOfObject(
						"XML for a single OperationMBean did not have the correct 'name' property. Input was "
								+ xml, xml, stat.getStatisticName(),
						"/Stats/"+stat.getStatisticName()+"/Properties/"+stat.getStatisticName());
	}

	
	/**
	 * <p>
	 * Verify the transform of a single group of Statistics.
	 * </p>
	 * 
	 * <p>
	 * Ideally, the XML would look like this:<br>
	 * 
	 * <pre>
	 * &lt;Stats&gt;
	 * 	&lt;Memory&gt;
	 *   &lt;Properties&gt;
	 * 		&lt;HeapInitialMemoryAllocated type="long"&gt;1000&lt;/HeapInitialMemoryAllocated&gt; 
	 * 		&lt;HeapUsedMemory type="long"&gt;2000&lt;/HeapUsedMemory&gt; 
	 * 		&lt;HeapCommittedMemory type="long"&gt;3000&lt;/HeapCommittedMemory&gt; 
	 * 		&lt;HeapMaximumMemory type="long"&gt;4000&lt;/HeapMaximumMemory&gt; 
	 * 		&lt;NonHeapInitialMemoryAllocated type="long"&gt;0&lt;/NonHeapInitialMemoryAllocated&gt; 
	 * 		&lt;NonHeapUsedMemory type="long"&gt;5000&lt;/NonHeapUsedMemory&gt; 
	 * 		&lt;NonHeapCommittedMemory type="long"&gt;6000&lt;/NonHeapCommittedMemory&gt; 
	 * 		&lt;NonHeapMaximumMemory type="long"&gt;-1&lt;/NonHeapMaximumMemory&gt; 
	 *		&lt;PendingFinalizationCount type="int"&gt;0&lt;/PendingFinalizationCount&gt; 
	 *      &lt;PercentHeapMemoryUsed type="int"&gt;5&lt;/PercentHeapMemoryUsed&gt; 
	 *   &lt;/Properties&gt;
	 * 	&lt;/Memory&gt;
	 * &lt;/Stats&gt;
	 * </pre>
	 * 
	 * </p>
	 * 
	 */
	@Test
    public void verifyGroupStatisticTransform() throws Exception {
        
        StatisticGroup stat = (new CannedStatistics()).getStats();
        StatisticXMLTransformer st = new StatisticXMLTransformer();
        String xml = st.transformGroupStatistics(MXBeanExtender.StatisticXMLTag, stat).toString();

        new SAXParser().customQueryAssertForXmlValidationOfObject(
                "XML for a single Canned statistic did not contain the correct item 'CannedStat1'. Input was "
                + xml, xml, "CannedStat1","/Stats/Canned/Properties/CannedStat1");
        new SAXParser().customQueryAssertForXmlValidationOfObject(
                "XML for a single Canned statistic did not contain the correct item 'CannedStat2'. Input was "
                + xml, xml, "CannedStat2","/Stats/Canned/Properties/CannedStat2");
        new SAXParser().customQueryAssertForXmlValidationOfObject(
                "XML for a single Canned statistic did not contain the correct item 'CannedStat3'. Input was "
                + xml, xml, "CannedStat3","/Stats/Canned/Properties/CannedStat3");
        new SAXParser().customQueryAssertForXmlValidationOfObject(
                "XML for a single Canned statistic did not contain the correct item 'CannedStat4'. Input was "
                + xml, xml, "CannedStat4","/Stats/Canned/Properties/CannedStat4");
        new SAXParser().customQueryAssertForXmlValidationOfObject(
                "XML for a single Canned statistic did not contain the correct item 'CannedStat5'. Input was "
                + xml, xml, "CannedStat5","/Stats/Canned/Properties/CannedStat5");

        assertTrue("Count statistics for a specific canned class Expected:5 got:"+stat.getStatisticItemGroup().get(0).getStatistics().size(), 
                stat.getStatisticItemGroup().get(0).getStatistics().size()==5 ); 

        assertTrue("Count statistics for a specific canned class Expected:5 got:"+ SAXParser.XPathQuery(xml,"Stats/Canned/Properties/*").length+" from Xpath", 
                 SAXParser.XPathQuery(xml,"Stats/Canned/Properties/*").length==5);

    }

	
	/**
	 * <p>
	 * Verify the transform of a single group of Statistics.
	 * </p>
	 * 
	 * <p>
	 * Ideally, the XML would look like this:<br>
	 * 
	 * <pre>
	 *	  &lt;Stats&gt;
	 *		  &lt;Class&gt;
	 *		  &lt;Properties&gt;
	 *			  &lt;TotalLoadedClassCount type="int"&gt;14799&lt;/TotalLoadedClassCount&gt; 
	 *			  &lt;TotalLoadedClassCount type="long"&gt;14807&lt;/TotalLoadedClassCount&gt; 
	 *			  &lt;UnloadedClassCount type="long"&gt;8&lt;/UnloadedClassCount&gt; 
	 *		  &lt;/Properties&gt;
	 *		  &lt;/Class&gt;
	 *	  &lt;Thread&gt;
	 *		  &lt;Properties&gt;
	 *			  &lt;PeakThreadCount type="int"&gt;58&lt;/PeakThreadCount&gt; 
	 *			  &lt;ThreadCount type="int"&gt;58&lt;/ThreadCount&gt; 
	 *			  &lt;TotalStartedThreadCount type="long"&gt;80&lt;/TotalStartedThreadCount&gt; 
	 *		  &lt;/Properties&gt;
	 *	  &lt;/Thread&gt;
	 *	  &lt;JITCompiler&gt;
	 *		  &lt;Properties&gt;
	 *	  		  &lt;TotalCompilationTime type="long"&gt;5615&lt;/TotalCompilationTime&gt; 
	 *		  &lt;/Properties&gt;
	 *	  &lt;/JITCompiler&gt;
	 *	  &lt;GC&gt;
	 *		  &lt;Properties&gt;
	 *		  	  &lt;GCCollectionCount type="long"&gt;25&lt;/GCCollectionCount&gt; 
	 *		      &lt;GCCollectionTime type="long"&gt;1121&lt;/GCCollectionTime&gt; 
	 *		      &lt;GCName type="String"&gt;Name1&lt;/GCName&gt; 
	 *		  &lt;/Properties&gt;
	 *	  &lt;/GC&gt;
	 *	  &lt;GC&gt;
	 *		  &lt;Properties&gt;
	 *		  	  &lt;GCCollectionCount type="long"&gt;25&lt;/GCCollectionCount&gt; 
	 *		      &lt;GCCollectionTime type="long"&gt;1121&lt;/GCCollectionTime&gt; 
	 *		      &lt;GCName type="String"&gt;Name2&lt;/GCName&gt; 
	 *		  &lt;/Properties&gt;
	 *	  &lt;/GC&gt;
	 *	  &lt;Memory&gt;
	 *		  &lt;Properties&gt;
	 *			  &lt;HeapInitialMemoryAllocated type="long"&gt;52428800&lt;/HeapInitialMemoryAllocated&gt; 
	 *			  &lt;HeapUsedMemory type="long"&gt;41939296&lt;/HeapUsedMemory&gt; 
	 *			  &lt;HeapCommittedMemory type="long"&gt;54525952&lt;/HeapCommittedMemory&gt; 
	 *			  &lt;HeapMaximumMemory type="long"&gt;268435456&lt;/HeapMaximumMemory&gt; 
	 *			  &lt;NonHeapInitialMemoryAllocated type="long"&gt;0&lt;/NonHeapInitialMemoryAllocated&gt; 
	 *			  &lt;NonHeapUsedMemory type="long"&gt;80748012&lt;/NonHeapUsedMemory&gt; 
	 *			  &lt;NonHeapCommittedMemory type="long"&gt;135698404&lt;/NonHeapCommittedMemory&gt; 
	 *			  &lt;NonHeapMaximumMemory type="long"&gt;-1&lt;/NonHeapMaximumMemory&gt; 
	 *			  &lt;PendingFinalizationCount type="int"&gt;0&lt;/PendingFinalizationCount&gt; 
	 *            &lt;PercentHeapMemoryUsed type="int"&gt;5&lt;/PercentHeapMemoryUsed&gt; 
	 *		  &lt;/Properties&gt;
	 *	  &lt;/Memory&gt;
	 *	  &lt;Runtime&gt;
	 *		  &lt;Properties&gt;
	 *			  &lt;StartTime type="long"&gt;1276727210245&lt;/StartTime&gt; 
	 *			  &lt;UpTime type="long"&gt;68877376&lt;/UpTime&gt; 
	 *		  &lt;/Properties&gt;
	 *	  &lt;/Runtime&gt;
	 *	  &lt;/Stats&gt;	 
	 * </pre>
	 * 
	 * </p>
	 * 
	 */
	@Test
	public void verifyAllStatisticTransform() throws Exception {
		
		Vector<StatisticGroup> allstats = new Vector<StatisticGroup>();

		//Retrieve all the statistics for all classes
		//
		allstats.add((new ClassLoaderStatistics()).getStats());
		allstats.add((new ThreadStatistics()).getStats());
		allstats.add((new JITCompilerStatistics()).getStats());
		allstats.add((new GCStatistics()).getStats());
		allstats.add((new MemoryStatistics()).getStats());
		allstats.add((new RuntimeStatistics()).getStats());
		
		StatisticXMLTransformer st = new StatisticXMLTransformer();
		
		String xml = st.transformAllStatistics(MXBeanExtender.StatisticXMLTag, allstats).toString();
		
        assertTrue("Count statistics for a ClassLoader class Expected:3 got:"+allstats.get(0).getStatisticItemGroup().get(0).getStatistics().size(), 
                allstats.get(0).getStatisticItemGroup().get(0).getStatistics().size()==3 ); 

        assertTrue("Count statistics for a ClassLoader class Expected:3 got:"+ SAXParser.XPathQuery(xml,"Stats/ClassLoader/Properties/*").length+" from Xpath", 
                 SAXParser.XPathQuery(xml,"Stats/ClassLoader/Properties/*").length==3);
        

        assertTrue("Count statistics for a Thread class Expected:3 got:"+allstats.get(1).getStatisticItemGroup().get(0).getStatistics().size(), 
                allstats.get(1).getStatisticItemGroup().get(0).getStatistics().size()==3 ); 

        assertTrue("Count statistics for a Thread class Expected:3 got:"+ SAXParser.XPathQuery(xml,"Stats/Thread/Properties/*").length+" from Xpath", 
                 SAXParser.XPathQuery(xml,"Stats/Thread/Properties/*").length==3);

        assertTrue("Count statistics for a JITCompiler class Expected:1 got:"+allstats.get(2).getStatisticItemGroup().get(0).getStatistics().size(), 
                allstats.get(2).getStatisticItemGroup().get(0).getStatistics().size()==1 ); 

        assertTrue("Count statistics for a JITCompiler class Expected:1 got:"+ SAXParser.XPathQuery(xml,"Stats/JITCompiler/Properties/*").length+" from Xpath", 
                 SAXParser.XPathQuery(xml,"Stats/JITCompiler/Properties/*").length==1);

        assertTrue("Count statistics for a GC class Expected:6 got:"+
				(allstats.get(3).getStatisticItemGroup().get(0).getStatistics().size()+allstats.get(3).getStatisticItemGroup().get(1).getStatistics().size()), 
                (allstats.get(3).getStatisticItemGroup().get(0).getStatistics().size()+allstats.get(3).getStatisticItemGroup().get(1).getStatistics().size())==6 ); 

        assertTrue("Count statistics for a GC class Expected:6 got:"+ SAXParser.XPathQuery(xml,"Stats/GC/Properties/*").length+" from Xpath", 
                 SAXParser.XPathQuery(xml,"Stats/GC/Properties/*").length==6);

        assertTrue("Count statistics for a Memory class Expected:10 got:"+allstats.get(4).getStatisticItemGroup().get(0).getStatistics().size(), 
                allstats.get(4).getStatisticItemGroup().get(0).getStatistics().size()==10 ); 

        assertTrue("Count statistics for a Memory class Expected:10 got:"+ SAXParser.XPathQuery(xml,"Stats/Memory/Properties/*").length+" from Xpath", 
                 SAXParser.XPathQuery(xml,"Stats/Memory/Properties/*").length==10);

        assertTrue("Count statistics for a Runtime class Expected:2 got:"+allstats.get(5).getStatisticItemGroup().get(0).getStatistics().size(), 
                allstats.get(5).getStatisticItemGroup().get(0).getStatistics().size()==2 ); 

        assertTrue("Count statistics for a Runtime class Expected:2 got:"+ SAXParser.XPathQuery(xml,"Stats/Runtime/Properties/*").length+" from Xpath", 
                 SAXParser.XPathQuery(xml,"Stats/Runtime/Properties/*").length==2);
	}

    /**
     * <p>
     * Verify when returning all Statistics version information is included as
     * an attribute of the XML response. The purpose of this test is only to
     * look for version information in the attribute header.
     * </p>
     * 
     * @throws Exception
     *             If there was a problem with the transform.
     */
    @Test
    public void verifyVersionAttributeInAllStatisticTransform()
            throws Exception {
        // Setup
        Vector<StatisticGroup> allstats = new Vector<StatisticGroup>();
        allstats.add((new ClassLoaderStatistics()).getStats());
        allstats.add((new ThreadStatistics()).getStats());
        allstats.add((new JITCompilerStatistics()).getStats());
        allstats.add((new GCStatistics()).getStats());
        allstats.add((new MemoryStatistics()).getStats());
        allstats.add((new RuntimeStatistics()).getStats());
        StatisticXMLTransformer st = new StatisticXMLTransformer();

        // Run Test
        String xml = st.transformAllStatistics(MXBeanExtender.StatisticXMLTag,
                allstats).toString();

        // Verify results
        String[] results = SAXParser.XPathQuery(xml, "/Stats/@version");
        Assert.assertTrue(
                "Query for version attribute should produce one result",
                results.length == 1);
    }

    /**
     * <p>
     * Verify when returning Group Statistics version information is included as
     * an attribute of the XML response. The purpose of this test is only to
     * look for version information in the attribute header.
     * </p>
     * 
     * @throws Exception
     *             If there was a problem with the transform.
     */
    @Test
    public void verifyVersionAttributeInGroupStatisticTransform()
            throws Exception {
        // Setup
        StatisticGroup stat = (new CannedStatistics()).getStats();
        StatisticXMLTransformer st = new StatisticXMLTransformer();
        
        // Run Test
        String xml = st.transformGroupStatistics(
                MXBeanExtender.StatisticXMLTag, stat).toString();

        // Verify results
        String[] results = SAXParser.XPathQuery(xml, "/Stats/@version");
        Assert.assertTrue(
                "Query for version attribute should produce one result",
                results.length == 1);
    }

    /**
     * <p>
     * Verify when returning Single Statistics version information is included
     * as an attribute of the XML response. The purpose of this test is only to
     * look for version information in the attribute header.
     * </p>
     * 
     * @throws Exception
     *             If there was a problem with the transform.
     */
    @Test
    public void verifyVersionAttributeInSingleStatisticTransform()
            throws Exception {
        // Setup
        Statistic stat = (new CannedStatistics()).getCannedStat1();
        StatisticXMLTransformer st = new StatisticXMLTransformer();
        
        // Run Test
        String xml = st.transformSingleStatistic(MXBeanExtender.StatisticXMLTag, stat.getStatisticName(),stat).toString();

        // Verify results
        String[] results = SAXParser.XPathQuery(xml, "/Stats/@version");
        Assert.assertTrue(
                "Query for version attribute should produce one result",
                results.length == 1);
    }
}
