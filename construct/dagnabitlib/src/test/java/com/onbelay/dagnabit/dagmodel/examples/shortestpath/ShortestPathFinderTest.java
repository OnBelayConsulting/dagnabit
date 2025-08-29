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
package com.onbelay.dagnabit.dagmodel.examples.shortestpath;

import com.onbelay.dagnabit.dagmodel.model.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Shortest path is the shortest path from the start to finish based on weights. The path generally will not include all nodes.
 * @author lefeu
 *
 */
public class ShortestPathFinderTest  {
	private static Logger logger = LoggerFactory.getLogger(ShortestPathFinderTest.class);

	private DagModel model;
	
	@Before
	public void beforeRun() throws Throwable {
		
	}
	
	@Test
	public void testNoPath() {
		model = ModelFixture.buildModel();
		
		// No link to this
		model.addNode("Z");
		ShortestPathFinder finder = model.createShortestPathFinder(model.getDefaultRelationshipType());
		
		List<DagNode> path = finder.findShortestPath(model.getNode("start"), model.getNode("Z"));
		logger.error(path.toString());
		assertEquals(0, path.size());
	}
	
	@Test
	public void testNoRoute() {
		model = ModelFixture.buildModel();
		
		// No link to this
		model.addNode("Z");
		ShortestPathFinder finder = model.createShortestPathFinder(model.getDefaultRelationshipType());
		
		DagNodePath path = finder.findShortestRoute(model.getNode("start"), model.getNode("Z"));
		logger.error(path.toString());
		assertEquals(0, path.getRelationships().size());
	}
	
	
	@Test
	public void testFindShortestPathSimple() {
		model = ModelFixture.buildModel();
		
		ShortestPathFinder finder = model.createShortestPathFinder(model.getDefaultRelationshipType());
		
		List<DagNode> nodes = finder.findShortestPath(model.getNode("start"), model.getNode("finish"));
		logger.error(nodes.toString());
		
		assertEquals(4, nodes.size());
		boolean hasStart = false;
		boolean hasB = false;
		boolean hasA = false;
		boolean hasFinish = false;
		
		for (DagNode n : nodes) {
		
			if (n.getName().equals("start"))
				hasStart = true;
			
			if (n.getName().equals("B"))
				hasB = true;
			
			if (n.getName().equals("A"))
				hasA = true;
			
			if (n.getName().equals("finish"))
				hasFinish = true;
			
		}
		assertTrue(hasStart);
		assertTrue(hasB);
		assertTrue(hasA);
		assertTrue(hasFinish);
		
	}
	
	
	@Test
	public void testFindShortestRouteSimple() {
		model = ModelFixture.buildModel();
		
		ShortestPathFinder finder = model.createShortestPathFinder(model.getDefaultRelationshipType());
		
		DagNodePath path = finder.findShortestRoute(model.getNode("start"), model.getNode("finish"));
		logger.error(path.toString());
		assertEquals(3, path.getRelationships().size());
		
		DagRelationship firstLink = path.getRelationships().get(0);
		assertEquals("start", firstLink.getFromNode().getName());
		assertEquals("B", firstLink.getToNode().getName());
		assertEquals(0, firstLink.getWeight().compareTo(BigDecimal.valueOf(2)));
		
		DagRelationship secondLink = path.getRelationships().get(1);
		assertEquals("B", secondLink.getFromNode().getName());
		assertEquals("A", secondLink.getToNode().getName());
		assertEquals(0, secondLink.getWeight().compareTo(BigDecimal.valueOf(3)));
		
		DagRelationship thirdLink = path.getRelationships().get(2);
		assertEquals("A", thirdLink.getFromNode().getName());
		assertEquals("finish", thirdLink.getToNode().getName());
		assertEquals(0, thirdLink.getWeight().compareTo(BigDecimal.valueOf(1)));
		
	}
	
	
	@Test
	public void testFindShortestPathComplex() {
		model = ModelFixture.buildComplexModel();
		
		ShortestPathFinder finder = model.createShortestPathFinder(model.getDefaultRelationshipType());
		
		List<DagNode> path = finder.findShortestPath(model.getNode("start"), model.getNode("finish"));
		logger.error(path.toString());
		assertEquals("[start, B, A, D, finish]", path.toString());
		
	}

	
	@Test
	public void testFindShortestRouteComplex() {
		model = ModelFixture.buildComplexModel();
		
		ShortestPathFinder finder = model.createShortestPathFinder(model.getDefaultRelationshipType());
		
		DagNodePath path = finder.findShortestRoute(model.getNode("start"), model.getNode("finish"));
		logger.error(path.toStringWithWeights());
		assertEquals(4, path.getRelationships().size());
		
		DagRelationship firstLink = path.getRelationships().get(0);
		assertEquals("start", firstLink.getFromNode().getName());
		assertEquals("B", firstLink.getToNode().getName());
		assertEquals(0, firstLink.getWeight().compareTo(BigDecimal.valueOf(2)));
		
		DagRelationship secondLink = path.getRelationships().get(1);
		assertEquals("B", secondLink.getFromNode().getName());
		assertEquals("A", secondLink.getToNode().getName());
		assertEquals(0, secondLink.getWeight().compareTo(BigDecimal.valueOf(3)));
		
		DagRelationship thirdLink = path.getRelationships().get(2);
		assertEquals("A", thirdLink.getFromNode().getName());
		assertEquals("D", thirdLink.getToNode().getName());
		assertEquals(0, thirdLink.getWeight().compareTo(BigDecimal.valueOf(4)));
		
		DagRelationship fourthLink = path.getRelationships().get(3);
		assertEquals("D", fourthLink.getFromNode().getName());
		assertEquals("finish", fourthLink.getToNode().getName());
		assertEquals(0, fourthLink.getWeight().compareTo(BigDecimal.valueOf(3)));
		
	}

}
