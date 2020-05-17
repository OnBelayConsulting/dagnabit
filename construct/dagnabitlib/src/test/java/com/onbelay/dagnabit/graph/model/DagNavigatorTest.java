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
 * Test the DagNavigator
 * @author lefeu
 *
 */
public class DagNavigatorTest  {
	private static Logger logger = LoggerFactory.getLogger(DagNavigatorTest.class);

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
	public void testNoCycles() {

		for (DagNode node : model.findRootNodes()) {
			for (DagLinkType linkType : model.getLinkTypes()) {
				
				LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
						linkType);
				NodeSearchResult nodeSearchResult = routeFinder.discoverFromRelationships(node);
				assertFalse(nodeSearchResult.isCyclic());
			}
		}

	}
	
	@Test
	/*
	 * Should find A -> B directly A -> C via B and A -> D directly
	 */
	public void testNavigateFromRoot() {
		
		List<DagNodePath> paths = model
									.navigate()
									.from(model.getNode("A"))
									.by(model.getLinkType("benchesTo"))
									.paths();
		
		
		DagNodePath pathAToB = null;
		DagNodePath pathAToC = null;
		DagNodePath pathAToD = null;
		
		for (DagNodePath p : paths) {
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
		
		assertEquals(2, pathAToC.getLinks().size());
	}
	
	@Test
	public void testNodeVisitor() {
		
		DagTestContext context = new DagTestContext();
		
		model
			.navigate()
			.from(model.getNode("A"))
			.by(model.getLinkType("benchesTo"))
			.using(context)
			.visitWith( (c, s, l, e) -> { ((DagTestContext) c).push(e.getName()); })
			.paths();
		
		logger.error(context.toString());
		
	}
	
	@Test
	/*
	 * Should find A -> B 
	 */
	public void testNavigateFromRootToLeaf() {
		
		List<DagNodePath> paths = model
									.navigate()
									.from(model.getNode("A"))
									.forOnly(n -> n.getName().equals("B"))
									.by(model.getLinkType("benchesTo"))
									.paths();
		

		assertEquals(1, paths.size());
		DagNodePath pathAToB = paths.get(0);
		assertEquals("A", pathAToB.getFromNode().getName());
		assertEquals("B", pathAToB.getToNode().getName());
	}
	
	@Test
	public void testNavigateToAncestors() {
		
		List<DagNode> nodes = model
				.navigate()
				.from(model.getNode("C"))
				.by(model.getLinkType("benchesTo"))
				.ancestors();
	
		assertEquals(2, nodes.size());
		for (DagNode p : nodes) {
			logger.error(p.getName());
		}
	}
	
	@Test
	public void testNavigateToDescendants() {
		
		List<DagNode> nodes = model
				.navigate()
				.from(model.getNode("A"))
				.by(model.getLinkType("benchesTo"))
				.descendants();
	
		assertEquals(3, nodes.size());
		for (DagNode p : nodes) {
			logger.error(p.getName());
		}
	}
	
	@Test
	public void testNavigateToDescendantsSorted() {
		
		List<DagNode> nodes = model
				.navigate()
				.from(model.getNode("A"))
				.by(model.getLinkType("benchesTo"))
				.sorted()
				.descendants();
	
		assertEquals(3, nodes.size());
		for (DagNode p : nodes) {
			logger.error(p.getName());
		}
	}
	
	@Test
	public void testNavigateToDescendantsBreadthFirst() {
		
		List<DagNode> nodes = model
				.navigate()
				.from(model.getNode("A"))
				.breadthFirst()
				.by(model.getLinkType("benchesTo"))
				.descendants();
	
		assertEquals(3, nodes.size());
		for (DagNode p : nodes) {
			logger.error(p.getName());
		}
	}
	
	
	@Test
	public void testNavigateToRootFromLeaf() {
		
		List<DagNodePath> paths = model
				.navigate()
				.from(model.getNode("C"))
				.by(model.getLinkType("benchesTo"))
				.pathsTo();
	
		assertEquals(1, paths.size());
		DagNodePath aPath =  paths.get(0);
		assertEquals("A", aPath.getToNode().getName());
	}
	
	
	@Test
	public void testNoCyclesWithAnalysis() {
		LinkAnalysis analysis = model
								.analyse()
								.result();
		
		assertEquals(false, analysis.isCyclic());	
	}
	

	@Test
	public void testFromLinksWithOneCycle() {
		// add additional path
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

		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getLinkType("benchesTo"));
		NodeSearchResult result = routeFinder.discoverToRelationships(model.getNode("C"));
		
		assertEquals(1, result.getPaths().size());
		
		DagNodePath pathCToA = result.getPaths().get(0);
		
		assertEquals("C", pathCToA.getFromNode().getName());
		assertEquals("A", pathCToA.getToNode().getName());
		
	}

	@Test
	public void testToLinks() {
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getLinkType("benchesTo"));
		NodeSearchResult result = routeFinder.discoverToRelationships(model.getNode("C"));
		
		assertEquals(1, result.getPaths().size());
		
		DagNodePath pathCToA = result.getPaths().get(0);
		
		assertEquals("C", pathCToA.getFromNode().getName());
		assertEquals("A", pathCToA.getToNode().getName());
		
	}

	@Test
	public void testBreadthFromLinks() {
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getLinkType("benchesTo"));
		List<DagNode> nodes = routeFinder.findDescendantsBreadthFirst(model.getNode("A"));
		
		logger.error(nodes.toString());
		assertEquals(3, nodes.size());
	}


	
	@Test
	public void testOneCycleWithAnalysisNoLinkFilter() {
		// add additional path
		model.addRelationship(
				model.getNode("C"), 
				"benchesTo", 
				model.getNode("A"));
		
		LinkAnalysis analysis = model
								.analyse()
								.result();
		
		assertEquals(true, analysis.isCyclic());
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
								.by(model.getLinkType("benchesTo"))
								.result();
		
		assertEquals(true, analysis.isCyclic());
	}

	@Test
	public void testAdjacent() {
		
		List<DagNode> neighbours = model
			.navigate()
			.from(model.getNode("A"))
			.by(model.getLinkType("benchesTo"))
			.children();
		
		assertTrue(neighbours.size() > 0);
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
