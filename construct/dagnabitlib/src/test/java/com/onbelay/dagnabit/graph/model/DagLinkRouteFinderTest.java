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
package com.onbelay.dagnabit.graph.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.onbelay.dagnabit.graph.factories.DagModelFactory;

/**
 * Test the DagLinkRouteFinder
 * @author lefeu
 *
 */
public class DagLinkRouteFinderTest  {
	private static Logger logger = LoggerFactory.getLogger(DagLinkRouteFinderTest.class);

	private DagModelFactory factory = new DagModelFactory();
	private DagModel model;
	
	@Before
	public void beforeRun() throws Throwable {
		// Solitary nodes
		model = factory.newModel();
		model.addNode("S", "special");
		model.addNode("R", "ordinary");

		
		model.addNode("A");
		
		model.addNode("B");
		
		model.addNode("C");
		
		model.addNode("D");

		
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
				model.getNode("A"), 
				"benchesTo", 
				model.getNode("D"));
		
		
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
	public void testNoCycles() {

		for (DagNode node : model.findRootNodes()) {
			for (DagLinkType linkType : model.getLinkTypes()) {
				
				LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(linkType);
				
				NodeSearchResult nodeSearchResult = routeFinder.discoverFromRelationships(node);
				assertFalse(nodeSearchResult.isCyclic());
			}
		}

	}
	

	@Test
	public void testToLinks() {
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(model.getLinkType("benchesTo"));
		
		NodeSearchResult result = routeFinder.discoverToRelationships(model.getNode("C"));
		
		assertEquals(1, result.getPaths().size());
		
		DagNodePath pathCToA = result.getPaths().get(0);
		
		assertEquals("C", pathCToA.getFromNode().getName());
		assertEquals("A", pathCToA.getToNode().getName());
		
		assertFalse(result.isCyclic());
	}

	
	@Test
	public void testEndingAtStartingFrom() {
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(model.getLinkType("benchesTo"));
		
		List<DagNodePath> paths = routeFinder.findPathsEndingAtStartingFrom(model.getNode("A"), model.getNode("C"));
		
		assertEquals(1, paths.size());
		
		logger.error(paths.get(0).getId());
	}

	
	@Test
	/**
	 * Breadth first traversal will result in B, D, C
	 */
	public void testBreadthFromLinks() {
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getLinkType("benchesTo"));
		List<DagNode> nodes = routeFinder.findDescendantsBreadthFirst(model.getNode("A"));
		
		logger.error(nodes.toString());
		assertEquals(3, nodes.size());
		String id = nodes
				.stream()
				.map( n -> n.getName())
				.reduce("",  (s, t) -> s + t); 
		assertEquals("BDC", id);
	}

	@Test
	/**
	 * Note that depthFirst is the default.
	 * This will result in three paths from A to B, A to B to C, and A to D. 
	 */
	public void testDepthFirstFromLinks() {
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getLinkType("benchesTo"));
		NodeSearchResult nodeSearchResult = routeFinder.discoverFromRelationships(model.getNode("A"));
		
		String pathAB = null;
		String pathABC = null;
		String pathAD = null;
		
		assertEquals(3, nodeSearchResult.getPaths().size());
		
		for (DagNodePath p : nodeSearchResult.getPaths()) {
			logger.error(p.getId());
			if (p.getId().equals("A -> B"))
				pathAB = p.getId();
			if (p.getId().equals("A -> B -> C"))
				pathABC = p.getId();
			if (p.getId().equals("A -> D"))
				pathAD = p.getId();
		}
		
		assertNotNull(pathAB);
		assertNotNull(pathABC);
		assertNotNull(pathAD);
		
		
		logger.error(nodeSearchResult.getPaths().toString());
		String id = nodeSearchResult.getPaths()
				.stream()
				.map( n -> n.getToNode().getName())
				.reduce("",  (s, t) -> s + t); 
		assertEquals("BCD", id);
		
	}

	
	@Test
	public void testFromLinksWithOneCycle() {
		// add additional path that is a cycle A -> C -> A
		model.addRelationship(
				model.getNode("C"), 
				"benchesTo", 
				model.getNode("A"));

		
		DagNode rootNode = model.getNode("A");
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getLinkType("benchesTo"));
		
		NodeSearchResult result = routeFinder.discoverFromRelationships(rootNode);
		assertTrue(result.isCyclic());
		
		assertEquals(3, result.getPaths().size());
		DagNodePath pathAToB = null;
		DagNodePath pathAToC = null;
		DagNodePath pathAToD = null;
		
		for (DagNodePath p : result.getPaths()) {
			logger.error(p.getRouteId());
			
			if (p.getRouteId().equals("A:B"))
				pathAToB = p;
			
			if (p.getRouteId().equals("A:C"))
				pathAToC = p;
			
			if (p.getRouteId().equals("A:D"))
				pathAToD = p;
		}
		
		assertNotNull(pathAToB);
		assertNotNull(pathAToC);
		assertNotNull(pathAToD);
		
		assertEquals(1, result.getCycles().size());
		DagNodePath cycle = result.getCycles().get(0);
		assertEquals("A", cycle.getToNode().getName());
	}


	@Test
	public void testToLinksWithOneCycle() {
		// add additional path
		model.addRelationship(
				model.getNode("C"), 
				"benchesTo", 
				model.getNode("A"));

		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(model.getLinkType("benchesTo"));
		
		NodeSearchResult result = routeFinder.discoverToRelationships(model.getNode("C"));
		
		assertEquals(1, result.getPaths().size());
		
		DagNodePath pathCToA = result.getPaths().get(0);
		
		assertEquals("C", pathCToA.getFromNode().getName());
		assertEquals("A", pathCToA.getToNode().getName());
		
		assertEquals(1, result.getCycles().size());
		DagNodePath cycle = result.getCycles().get(0);
		assertEquals("A", cycle.getToNode().getName());
	}

	@Test
	public void testFindRoutesFromNode() {
		// add additional path
		model.addRelationship(
				model.getNode("A"), 
				"benchesTo", 
				model.getNode("C"));
		
		
		DagNode rootNode = model.getNode("A");
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getLinkType("benchesTo"));
		
		for (DagPathRoutes pathRoutes : routeFinder.findAllRoutesFrom(rootNode).values()) {
			logger.error(pathRoutes.toString());
		}
		
		
	}
	
	@Test
	public void testNavigateRelationship() {
		
		
		DagNode rootNode = model.getNode("A");
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getLinkType("benchesTo"));
		
		NodeSearchResult result = routeFinder.discoverFromRelationships(rootNode);
		for (DagNodePath p : result.getPaths()) {
			logger.error("path " + " " + p.getId());
		}
		
	}

}
