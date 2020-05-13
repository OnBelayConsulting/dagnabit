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
	public void testToLinks() {
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				model.getLinkType("connects"));
		NodeSearchResult result = routeFinder.discoverToRelationships(model.getNode("1"));
		
		for (DagNodePath p : result.getPaths()) {
			logger.error(p.getId());
		}
		
	}

}
