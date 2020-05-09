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
package com.onbelay.dagnabit.graph.examples.travelingsales;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.DagPathRoutes;
import com.onbelay.dagnabit.graph.model.DagTestContext;
import com.onbelay.dagnabit.graph.model.LinkAnalysis;
import com.onbelay.dagnabit.graph.model.LinkRouteFinder;
import com.onbelay.dagnabit.graph.model.NavigationResult;
import com.onbelay.dagnabit.graph.model.NodeSearchResult;

public class DagTPModelTest  {
	private static Logger logger = LogManager.getLogger();

	private DagModel model;
	
	@Before
	public void beforeRun() throws Throwable {
		
		model = TPFixture.buildTPModel();
	}


	@Test
	public void testForCycles() {

				
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(model.getLinkType("connects"));
		NodeSearchResult nodeSearchResult = routeFinder.discoverFromRelationships(model.getNode("1"));
		assertTrue(nodeSearchResult.isCyclic());
		
		for (DagNodePath path : nodeSearchResult.getPaths()) {
			logger.error(path.getId());
		}
		
		//assertEquals(2, nodeSearchResult.getCycles().size());
		
		for (DagNodePath p : nodeSearchResult.getCycles()) {
			logger.error(p.getId());
		}
		
	}
	
	@Test
	/*
	 * Should find 
	 */
	public void testNavigateFromRootForCycles() {
		
		List<DagNodePath> paths = model
									.navigate()
									.from(model.getNode("1"))
									.by(model.getLinkType("connects"))
									.cycles();
		
		List<DagNodePath> filtered = paths
										.stream()
										.filter(p -> p.getToNode().equals(model.getNode("1")))
										.filter(p -> p.getLinks().size() == 4)
										.collect(Collectors.toList());
		
		DagNodePath shortestPath = null;
		int totalCost = 100000;
		for (DagNodePath p : filtered) {
			int totalWeight = p.calculateTotalWeight();
			if (totalWeight < totalCost) {
				totalCost = totalWeight;
				shortestPath = p;
			}
		}
		
		logger.error("Shortest path: " + shortestPath.getId() + " with weight " + totalCost);
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
	public void testNavigateToDescendants() {
		
		List<DagNode> nodes = model
				.navigate()
				.from(model.getNode("A"))
				.by(model.getLinkType("benchesTo"))
				.descendants();
	
		assertEquals(4, nodes.size());
		for (DagNode p : nodes) {
			logger.error(p.getName());
		}
	}
	
	@Test
	public void testNavigateToDescendantsBreadthFirst() {
		
		List<DagNode> nodes = model
				.navigate()
				.fromBreadthFirst(model.getNode("A"))
				.by(model.getLinkType("benchesTo"))
				.descendants();
	
		assertEquals(4, nodes.size());
		for (DagNode p : nodes) {
			logger.error(p.getName());
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
	public void testBreadthFromLinks() {
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getLinkType("benchesTo"));
		List<DagNode> nodes = routeFinder.discoverBreadthFromRelationships(model.getNode("A"));
		
		logger.error(nodes);
		assertEquals(4, nodes.size());
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
