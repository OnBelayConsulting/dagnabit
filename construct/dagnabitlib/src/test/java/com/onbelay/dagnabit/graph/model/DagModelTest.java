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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.onbelay.dagnabit.graph.factories.DagModelFactory;

public class DagModelTest  {
	private static Logger logger = LogManager.getLogger();

	private DagModelFactory factory = new DagModelFactory();
	private DagModel model;
	
	@Before
	public void beforeRun() throws Throwable {
		model = factory.newModel();
		
		model.addNode("A");
		
		model.addNode("B");
		
		model.addNode("C");
		
		model.addNode("D");
		
		model.addRelationship(
				model.getNode("A"), 
				"benchesTo", 
				model.getNode("B"));
		
		model.addRelationship(
				model.getNode("A"), 
				"benchesTo", 
				model.getNode("D"));
		
		
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
			.visitNodeWith( (c, n) -> { ((DagTestContext) c).push(n.getName()); })
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
									.to(model.getNode("B"))
									.by(model.getLinkType("benchesTo"))
									.paths();
		

		assertEquals(1, paths.size());
		DagNodePath pathAToB = paths.get(0);
		assertEquals("A", pathAToB.getFromNode().getName());
		assertEquals("B", pathAToB.getToNode().getName());
	}
	
	@Test
	public void testNavigateToRoot() {
		
		List<DagNodePath> paths = model
				.navigate()
				.to(model.getNode("A"))
				.by(model.getLinkType("benchesTo"))
				.paths();
	
		assertEquals(3, paths.size());
		for (DagNodePath p : paths) {
			logger.error(p.getRouteId());
		}
	}
	
	@Test
	public void testNavigateToRootFromLeaf() {
		
		List<DagNodePath> paths = model
				.navigate()
				.to(model.getNode("A"))
				.from(model.getNode("C"))
				.by(model.getLinkType("benchesTo"))
				.paths();
	
		assertEquals(1, paths.size());
		DagNodePath path  = paths.get(0);
		assertEquals("A", path.getToNode().getName());
		assertEquals("C", path.getFromNode().getName());
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
		NavigationResult result = routeFinder.discoverToRelationships(model.getNode("A"));
		
		DagNodePath pathBtoA = null;
		DagNodePath pathCToA = null;
		DagNodePath pathDToA = null;
		
		for (NodeSearchResult nodeSearchResult : result.getNodeSearchResults().values()) {
			for (DagNodePath path : nodeSearchResult.getPaths()) {
				if (path.getFromNode().getName().equals("B"))
					pathBtoA = path;
				if (path.getFromNode().getName().equals("C"))
					pathCToA = path;
				if (path.getFromNode().getName().equals("D"))
					pathDToA = path;
			}
		}
		
		
		assertNotNull(pathBtoA);
		assertNotNull(pathCToA);
		assertNotNull(pathDToA);
	}

	@Test
	public void testToLinks() {
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getLinkType("benchesTo"));
		NavigationResult result = routeFinder.discoverToRelationships(model.getNode("A"));
		
		NodeSearchResult nodeSearchResultCtoA =  result.getNodeSearchResult(model.getNode("C"));
		NodeSearchResult nodeSearchResultBtoA =  result.getNodeSearchResult(model.getNode("B"));
		NodeSearchResult nodeSearchResultDtoA =  result.getNodeSearchResult(model.getNode("D"));
		
		
		assertNotNull(nodeSearchResultCtoA);
		assertNotNull(nodeSearchResultBtoA);
		assertNotNull(nodeSearchResultDtoA);

		
		for (DagNodePath p : nodeSearchResultCtoA.getPaths()) {
			logger.error("C to A " + " " + p.getRouteId());
		}
		
		for (DagNodePath p : nodeSearchResultBtoA.getPaths()) {
			logger.error("B to A " + " " + p.getRouteId());
		}
		
		for (DagNodePath p : nodeSearchResultDtoA.getPaths()) {
			logger.error("D to A " + " " + p.getRouteId());
		}
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
