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
package com.onbelay.dagnabit.graph.examples.genealogy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.onbelay.dagnabit.graph.components.DagModelImpl;
import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.DagNodeVector;
import com.onbelay.dagnabit.graph.model.DagPathRoutes;
import com.onbelay.dagnabit.graph.model.LinkAnalysis;
import com.onbelay.dagnabit.graph.model.LinkRouteFinder;
import com.onbelay.dagnabit.graph.model.NavigationResult;
import com.onbelay.dagnabit.graph.model.NodeSearchResult;

public class GenealogyModelTest  {
	private static Logger logger = LogManager.getLogger();

	private DagModel model;
	
	@Before
	public void beforeRun() throws Throwable {
		model = new DagModelImpl();
		
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
			for (DagLinkType linkType : model.getLinkTypeMap().values()) {
				
				LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
						null,
						linkType);
				NodeSearchResult nodeSearchResult = routeFinder.discoverFromRelationships(node);
				assertEquals(0, nodeSearchResult.getCycles());
			}
		}

	}
	
	@Test
	public void testNavigateFromRoot() {
		
		List<DagNodePath> paths = model
									.navigate()
									.from(model.getNode("A"))
									.by(model.getLinkType("benchesTo"))
									.paths();
		for (DagNodePath p : paths) {
			logger.error(p.getRouteId());
		}
	}
	
	
	@Test
	public void testNoCyclesWithAnalysis() {
		LinkAnalysis analysis = model
								.analyse()
								.result();
		
		assertEquals(false, analysis.hasCycles());	
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
				null,
				model.getLinkType("benchesTo"));
		NodeSearchResult result = routeFinder.discoverFromRelationships(rootNode);
		NodeSearchResult backChained = result.getBackChainedNodeSearchResult();
		assertTrue(result.isCyclic());
		assertEquals(1, result.getVectors().size());
		// find the A -> D
		DagNodeVector vector = result.getVectors().get(0);
		assertEquals("A -> D", vector.getId());
		
		DagNode toNode = result.getCycles().get(0).getToNode();
		for (DagNodeVector v : backChained.getVectors()) {
			for (DagNodePath p : v.createPaths()) {
				logger.error("Backchaining " + " " + p.getId());
			}
		}
	}


	@Test
	public void testToLinksWithOneCycle() {
		// add additional path
		model.addRelationship(
				model.getNode("C"), 
				"benchesTo", 
				model.getNode("A"));

		
		DagNode rootNode = model.getNode("A");
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				null,
				model.getLinkType("benchesTo"));
		NavigationResult result = routeFinder.discoverToRelationships();
		
		NodeSearchResult cyclicResult = null;
		for (NodeSearchResult nodeSearchResult : result.getNodeSearchResults().values()) {
			if (nodeSearchResult.isCyclic()) {
				cyclicResult = nodeSearchResult;
				break;
			}
				
		}
		assertNotNull(cyclicResult);
		assertEquals(1, cyclicResult.getVectors().size());
		// find the A -> D
		DagNodeVector vector = cyclicResult.getVectors().get(0);
		assertEquals("A -> D", vector.getId());
		
		DagNode toNode = cyclicResult.getCycles().get(0).getToNode();
		for (DagNodeVector v : cyclicResult.getBackChainedNodeSearchResult().getVectors()) {
			for (DagNodePath p : v.createPaths()) {
				logger.error("Backchaining " + " " + p.getId());
			}
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
			.adjacent();
		
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
				null,
				model.getLinkType("benchesTo"));
		
		for (DagPathRoutes pathRoutes : routeFinder.findAllRoutesFrom(rootNode).values()) {
			logger.error(pathRoutes.toString());
		}
		
		
	}
	
	@Test
	public void testNavigateRelationship() {
		
		
		DagNode rootNode = model.getNode("A");
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				null,
				model.getLinkType("benchesTo"));
		
		NodeSearchResult result = routeFinder.discoverFromRelationships(rootNode);
		for (DagNodeVector v : result.getVectors()) {
			logger.error(v.toString());
		}
		
	}

}
