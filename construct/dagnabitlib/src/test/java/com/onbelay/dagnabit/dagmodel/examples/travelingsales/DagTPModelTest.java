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
package com.onbelay.dagnabit.dagmodel.examples.travelingsales;

import com.onbelay.dagnabit.dagmodel.model.DagModel;
import com.onbelay.dagnabit.dagmodel.model.DagNodePath;
import com.onbelay.dagnabit.dagmodel.model.LinkRouteFinder;
import com.onbelay.dagnabit.dagmodel.model.NodeSearchResult;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class DagTPModelTest  {
	private static Logger logger = LoggerFactory.getLogger(DagTPModelTest.class);

	private DagModel model;
	
	@Before
	public void beforeRun() throws Throwable {
		
	}


	@Test
	public void testForCycles() {

		model = TPFixture.buildTPModel();
				
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(model.getRelationshipType("connects"));
		NodeSearchResult nodeSearchResult = routeFinder.discoverFromRelationships(model.getNode("1"));
		assertTrue(nodeSearchResult.isCyclic());
		
		for (DagNodePath path : nodeSearchResult.getPaths()) {
			logger.error(path.toString());
		}
		
		//assertEquals(2, nodeSearchResult.getCycles().size());
		
		for (DagNodePath p : nodeSearchResult.getCycles()) {
			logger.error(p.toString());
		}
		
	}
	@Test
	public void testForCyclesLargeModel() {

		model = TPFixture.buildLargeTPMode();
				
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(model.getRelationshipType("connects"));
		NodeSearchResult nodeSearchResult = routeFinder.discoverFromRelationships(model.getNode("1"));
		assertTrue(nodeSearchResult.isCyclic());
		
//		for (DagNodePath path : nodeSearchResult.getPaths()) {
//			logger.error(path.getId());
//		}
		
		//assertEquals(2, nodeSearchResult.getCycles().size());
		
		logger.error("Number of cycles/routes: " + nodeSearchResult.getCycles().size());
		
//		for (DagNodePath p : nodeSearchResult.getCycles()) {
//			logger.error(p.getId());
//		}
		
	}
	
	
	@Test
	/*
	 * Should find 
	 */
	public void testNavigateFromRootForCycles() {
		model = TPFixture.buildTPModel();
		
		List<DagNodePath> paths = model
									.navigate()
									.from(model.getNode("1"))
									.by(model.getRelationshipType("connects"))
									.cycles();
		
		List<DagNodePath> filtered = paths
										.stream()
										.filter(p -> p.getEndNode().equals(model.getNode("1")))
										.filter(p -> p.getRelationships().size() == 4)
										.collect(Collectors.toList());
		
		DagNodePath shortestPath = null;
		BigDecimal totalCost = BigDecimal.valueOf(100000);
		for (DagNodePath p : filtered) {
			BigDecimal totalWeight = p.calculateTotalWeight();
			if (totalWeight.compareTo(totalCost) < 0) {
				totalCost = totalWeight;
				shortestPath = p;
			}
		}
		
		logger.error("Shortest path: " + shortestPath.toString() + " with weight " + totalCost);
	}
	
	/*
	 * Should find 
	 */
	public void testNavigateFromRootForCyclesLargeModel() {
		model = TPFixture.buildLargeTPMode();
		
		List<DagNodePath> paths = model
									.navigate()
									.from(model.getNode("1"))
									.by(model.getRelationshipType("connects"))
									.cycles();
		
		List<DagNodePath> filtered = paths
										.stream()
										.filter(p -> p.getEndNode().equals(model.getNode("1")))
										.filter(p -> p.getRelationships().size() == 12)
										.collect(Collectors.toList());
		
		logger.error("number of paths: " + paths.size());
		
		DagNodePath shortestPath = null;
		BigDecimal totalCost = BigDecimal.valueOf(100000);
		for (DagNodePath p : filtered) {
			BigDecimal totalWeight = p.calculateTotalWeight();
			if (totalWeight.compareTo(totalCost) < 0) {
				totalCost = totalWeight;
				shortestPath = p;
			}
		}
		
		logger.error("Shortest path: " + shortestPath.toString() + " with weight " + totalCost);
	}
	

	@Test
	public void testToLinks() {
		model = TPFixture.buildTPModel();
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getRelationshipType("connects"));
		NodeSearchResult result = routeFinder.discoverToRelationships(model.getNode("1"));
		
		for (DagNodePath p : result.getPaths()) {
			logger.error(p.toString());
		}
		
	}

}
