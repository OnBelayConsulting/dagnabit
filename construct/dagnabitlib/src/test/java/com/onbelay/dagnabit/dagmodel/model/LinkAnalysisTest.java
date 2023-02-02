/*
 Copyright 2019, OnBelay Consulting Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.  
 */
package com.onbelay.dagnabit.dagmodel.model;

import com.onbelay.dagnabit.dagmodel.components.DagModelImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test the LinkAnalysis
 * @author lefeu
 *
 */
public class LinkAnalysisTest  {
	private static Logger logger = LoggerFactory.getLogger(LinkAnalysisTest.class);

	private DagModel model;
	
	@Before
	public void beforeRun() throws Throwable {
		model = new DagModelImpl("test");
		// Solitary nodes
		model.addNode("S", "special");
		model.addNode("R", "ordinary");

		
		model.addNode("A");
		
		model.addNode("B");
		
		model.addNode("C");
		
		model.addNode("D");

		
		model.addRelationship(
				model.getNode("A"), 
				"benchesTo", 
				model.getNode("D"));
		
		
		model.addRelationship(
				model.getNode("A"), 
				"benchesTo", 
				model.getNode("B"));
		
		
		model.addRelationship(
				model.getNode("B"), 
				"benchesTo", 
				model.getNode("C"));
		
		
		model.addRelationship(
				model.getNode("A"), 
				"basisTo", 
				model.getNode("C"));
		
		
		model.addRelationship(
				model.getNode("C"), 
				"basisTo", 
				model.getNode("D"));
		
		model.addNode("V", "Object");
		model.addNode("Y", "Other");
		model.addNode("X", "Object");
		
		
		model.addRelationship(
				model.getNode("V"), 
				"benchesTo", 
				model.getNode("Y"));
		
		model.addRelationship(
				model.getNode("Y"), 
				"benchesTo", 
				model.getNode("X"));
	}


	
	
	@Test
	public void testNoCyclesWithAnalysis() {
		LinkAnalysis analysis = model
								.analyse()
								.result();
		
		assertEquals(false, analysis.isCyclic());	
	}
	

	
	@Test
	public void testOneCycleWithAnalysisWithLinkFilter() {
		// add additional path
		model.addRelationship(
				model.getNode("C"), 
				"benchesTo", 
				model.getNode("A"));
		
		LinkAnalysis analysis = model
								.analyse()
								.by(model.getRelationshipType("benchesTo"))
								.result();
		
		assertEquals(true, analysis.isCyclic());
	}


	@Test
	public void testOneCycleWithAnalysis() {
		// add additional path
		model.addRelationship(
				model.getNode("C"),
				"benchesTo",
				model.getNode("A"));

		LinkAnalysis analysis = model
				.analyse()
				.result();

		assertEquals(true, analysis.isCyclic());
		List<DagNodePath> paths = analysis.getCycles();
		assertEquals(3, paths.size());
	}




}
