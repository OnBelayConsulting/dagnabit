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

import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.onbelay.dagnabit.graph.factories.DagModelFactory;
import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.LinkRouteFinder;
import com.onbelay.dagnabit.graph.model.NodeSearchResult;

public class GenealogyModelTest  {
	private static Logger logger = LogManager.getLogger();

	private DagModel model;
	
	@Before
	public void beforeRun() throws Throwable {
		DagModelFactory factory = new DagModelFactory();
		model = factory.newModel();
		
		GenealogyFixture fixture = new GenealogyFixture(model);
		
		fixture.addPerson("Fred Jones", LocalDate.of(1961, 3, 15));
		fixture.addPerson("Sally Jones", LocalDate.of(1961, 7, 11));
		fixture.addIsSpouseOf("Fred Jones", "Sally Jones");

		fixture.addPerson("John Jones", LocalDate.of(1991, 4, 14));
		fixture.addPerson("Jill Jones", LocalDate.of(19931, 3, 10));
		

		fixture.addPerson("Mary Gifford-Jones", LocalDate.of(1970, 1, 17));

		
	}


	@Test
	public void testNoCycles() {

		for (DagNode node : model.findRootNodes()) {
			for (DagLinkType linkType : model.getLinkTypes()) {
				
				LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
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
	
}
