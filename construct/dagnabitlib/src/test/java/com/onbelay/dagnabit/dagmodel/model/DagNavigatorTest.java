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

import static org.junit.Assert.*;

/**
 * Test the DagNavigator
 * @author lefeu
 *
 */
public class DagNavigatorTest  {
	private static Logger logger = LoggerFactory.getLogger(DagNavigatorTest.class);

	private DagModel model;
	
	@Before
	public void beforeRun() throws Throwable {
		// Solitary nodes
		model = new DagModelImpl("test");
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
			for (DagRelationshipType linkType : model.getRelationshipTypes()) {
				
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
									.by(model.getRelationshipType("benchesTo"))
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
		
		assertEquals(2, pathAToC.getRelationships().size());
	}
	
	@Test
	public void testNodeVisitor() {
		
		DagTestContext context = new DagTestContext();
		
		model
			.navigate()
			.from(model.getNode("A"))
			.by(model.getRelationshipType("benchesTo"))
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
									.by(model.getRelationshipType("benchesTo"))
									.paths();
		

		assertEquals(1, paths.size());
		DagNodePath pathAToB = paths.get(0);
		assertEquals("A", pathAToB.getStartNode().getName());
		assertEquals("B", pathAToB.getEndNode().getName());
	}
	
	@Test
	public void testNavigateToAncestors() {
		
		List<DagNode> nodes = model
				.navigate()
				.from(model.getNode("C"))
				.by(model.getRelationshipType("benchesTo"))
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
				.by(model.getRelationshipType("benchesTo"))
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
				.by(model.getRelationshipType("benchesTo"))
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
				.by(model.getRelationshipType("benchesTo"))
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
				.by(model.getRelationshipType("benchesTo"))
				.pathsTo();
	
		assertEquals(1, paths.size());
		DagNodePath aPath =  paths.get(0);
		assertEquals("A", aPath.getStartNode().getName());
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
				model.getRelationshipType("benchesTo"));
		
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
		assertEquals("A", cycle.getEndNode().getName());
	}


	@Test
	public void testToLinksWithOneCycle() {
		// add additional path
		model.addRelationship(
				model.getNode("C"), 
				"benchesTo", 
				model.getNode("A"));

		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getRelationshipType("benchesTo"));
		NodeSearchResult result = routeFinder.discoverToRelationships(model.getNode("C"));
		
		assertEquals(1, result.getPaths().size());
		
		DagNodePath pathCToA = result.getPaths().get(0);
		
		assertEquals("A", pathCToA.getStartNode().getName());
		assertEquals("C", pathCToA.getEndNode().getName());
		
	}

	@Test
	public void testToLinks() {
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getRelationshipType("benchesTo"));
		NodeSearchResult result = routeFinder.discoverToRelationships(model.getNode("C"));
		
		assertEquals(1, result.getPaths().size());
		
		DagNodePath pathCToA = result.getPaths().get(0);
		
		assertEquals("A", pathCToA.getStartNode().getName());
		assertEquals("C", pathCToA.getEndNode().getName());
		
		logger.error(pathCToA.toString());
		
	}

	@Test
	public void testBreadthFromLinks() {
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getRelationshipType("benchesTo"));
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
								.by(model.getRelationshipType("benchesTo"))
								.result();
		
		assertEquals(true, analysis.isCyclic());
	}

	@Test
	public void testAdjacent() {
		
		List<DagNode> neighbours = model
			.navigate()
			.from(model.getNode("A"))
			.by(model.getRelationshipType("benchesTo"))
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
				model.getRelationshipType("benchesTo"));
		
		for (DagPathRoutes pathRoutes : routeFinder.findAllRoutesFrom(rootNode).values()) {
			logger.error(pathRoutes.toString());
		}
		
		
	}
	
	@Test
	public void testNavigateRelationship() {
		
		
		DagNode rootNode = model.getNode("A");
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getRelationshipType("benchesTo"));
		
		NodeSearchResult result = routeFinder.discoverFromRelationships(rootNode);
		for (DagNodePath p : result.getPaths()) {
			logger.error("path " + " " + p.toString());
		}
		
	}

}
