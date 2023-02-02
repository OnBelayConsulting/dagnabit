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
package com.onbelay.dagnabit.dagmodel.examples.mst;

import com.onbelay.dagnabit.dagmodel.model.DagRelationship;
import com.onbelay.dagnabit.dagmodel.model.DagModel;
import com.onbelay.dagnabit.dagmodel.model.DagNode;
import com.onbelay.dagnabit.dagmodel.model.MinimumSpanningTreeFinder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MinimumSpanningTreeFinderTest  {
	private static Logger logger = LoggerFactory.getLogger(MinimumSpanningTreeFinderTest.class);

	private DagModel model;
	
	@Before
	public void beforeRun() throws Throwable {
		
	}

	@Test
	public void findMSTBasicModel() {

		model = ModelFixture.buildBasicModel();
		
		MinimumSpanningTreeFinder finder = model.createMinimumSpanningTreeFinder(model.getDefaultRelationshipType());
		
		List<DagRelationship> processedLinks = finder.determineMinimumSpanningTree(model.getNode("A"));
		
		
		for (DagRelationship l : processedLinks) {
			logger.error(l.getFromNode().getName() + " --> " + l.getToNode().getName() + " with " + l.getWeight() ) ;
		}
		
		DagRelationship firstLink = processedLinks.get(0);
		assertEquals("A", firstLink.getFromNode().getName());
		assertEquals("B", firstLink.getToNode().getName());
		assertEquals(7, firstLink.getWeight());
		
		DagRelationship secondLink = processedLinks.get(1);
		assertEquals("B", secondLink.getFromNode().getName());
		assertEquals("C", secondLink.getToNode().getName());
		assertEquals(3, secondLink.getWeight());
		
		DagRelationship thirdLink = processedLinks.get(2);
		assertEquals("C", thirdLink.getFromNode().getName());
		assertEquals("E", thirdLink.getToNode().getName());
		assertEquals(3, thirdLink.getWeight());
		
		DagRelationship fourthLink = processedLinks.get(3);
		assertEquals("E", fourthLink.getFromNode().getName());
		assertEquals("D", fourthLink.getToNode().getName());
		assertEquals(2, fourthLink.getWeight());
		
		DagRelationship fifthLink = processedLinks.get(4);
		assertEquals("E", fifthLink.getFromNode().getName());
		assertEquals("F", fifthLink.getToNode().getName());
		assertEquals(2, fifthLink.getWeight());
	}
	
	@Test
	public void findMSTBasicModelWithMstLink() {

		model = ModelFixture.buildBasicModel();
		
		MinimumSpanningTreeFinder finder = model.createMinimumSpanningTreeFinder(model.getDefaultRelationshipType(), model.getRelationshipType("mst"));
		
		
		List<DagRelationship> processedLinks = finder.determineMinimumSpanningTree(model.getNode("A"));
		int total = 0;
		for (DagRelationship l : processedLinks) {
			logger.error(l.getFromNode().getName() + " --> " + l.getToNode().getName() + " with " + l.getWeight() ) ;
			total = total + l.getWeight();
		}
		
		List<DagNode> nodes = model.navigate().from(model.getNode("A")).by(model.getRelationshipType("mst")).descendants();
		logger.error(nodes.toString()) ;
		assertEquals(17, total);
		
		assertEquals("[B, C, E, D, F]", nodes.toString());
	}
	

	
	

	@Test
	public void findMSTComplex() {

		model = ModelFixture.BuildComplexModel();
		
		MinimumSpanningTreeFinder finder = model.createMinimumSpanningTreeFinder(model.getDefaultRelationshipType());
		
		List<DagRelationship> processedLinks = finder.determineMinimumSpanningTree(model.getNode("A"));
		
		int total = 0;
		for (DagRelationship l : processedLinks) {
			logger.error(l.getFromNode().getName() + " --> " + l.getToNode().getName() + " with " + l.getWeight() ) ;
			total = total + l.getWeight();
		}
		assertEquals(21, total);
	}
	
	
}
