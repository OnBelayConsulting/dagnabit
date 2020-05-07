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
		
		fixture.addCity("Kelowna");
		fixture.addCity("Toronto");
		fixture.addCity("Vancouver");
		fixture.addCity("Ottawa");
		fixture.addCity("Montreal");
		fixture.addCity("Halifax");
		fixture.addCity("Waterloo");
		
		fixture.addSchool("UBC");
		fixture.addIsLocatedIn("UBC", "Vancouver");
		
		fixture.addSchool("UBCO");
		fixture.addIsLocatedIn("UBCO", "Kelowna");
		
		fixture.addSchool("University of Waterloo");
		fixture.addIsLocatedIn("University of Waterloo", "Waterloo");
		
		fixture.addSchool("U of T");
		fixture.addIsLocatedIn("U of T", "Toronto");
		
		fixture.addPerson("David Jones", LocalDate.of(1961, 3, 15));
		fixture.addAttended("David Jones", "University of Waterloo");
		fixture.addWasBornIn("David Jones", "Toronto");
		
		fixture.addPerson("Cathy Jones", LocalDate.of(1961, 7, 11));
		fixture.addIsSpouseOf("David Jones", "Cathy Jones");
		fixture.addAttended("Cathy Jones", "U of T");
		fixture.addWasBornIn("Cathy Jones", "Montreal");

		fixture.addPerson("Andrew Jones", LocalDate.of(1999, 4, 14));
		fixture.addAttended("Andrew Jones", "U of T");
		fixture.addWasBornIn("Andrew Jones", "Toronto");
		
		fixture.addPerson("Shannon Jones", LocalDate.of(2001, 3, 10));
		fixture.addAttended("Shannon Jones", "University of Waterloo");
		fixture.addWasBornIn("Shannon Jones", "Montreal");
		
		fixture.addIsParentOf("David Jones", "Andrew Jones");
		fixture.addIsParentOf("Cathy Jones", "Andrew Jones");
		
		fixture.addIsParentOf("David Jones", "Shannon Jones");
		fixture.addIsParentOf("Cathy Jones", "Shannon Jones");

		
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
	public void testNavigateToSchool() {
		
		List<DagNode> nodes = model
									.navigate()
									.from(model.getNode("David Jones"))
									.by(model.getLinkType(GenealogyFixture.ATTENDED_REL))
									.findChildren()
									.nodes();
		for (DagNode p : nodes) {
			logger.error(p.getName());
		}
	}
	
	@Test
	public void testNavigateToSchoolThenToAttendees() {
		
		List<DagNode> nodes = model
									.navigate()
									.from(model.getNode("David Jones"))
									.by(model.getLinkType(GenealogyFixture.ATTENDED_REL))
									.findChildren()
									.by(model.getLinkType(GenealogyFixture.ATTENDED_REL))
									.parents();
		for (DagNode p : nodes) {
			logger.error(p.getName());
		}
	}
	@Test
	public void testFindChildrenAttendingSchoolInCityBornIn() {
		
		DagGenealogyContext context = new DagGenealogyContext();
		
		List<DagNode> nodes = model
									.navigate()
									.from(model.getNode("David Jones"))
									.by(model.getLinkType(GenealogyFixture.PARENT_REL))
									.using(context)
									.findChildren()
									.visitBy(
											model.getLinkType(GenealogyFixture.WAS_BORN_IN_REL), 
											DagGenealogyContext::accept)
									.visitBy(
											model.getLinkType(GenealogyFixture.ATTENDED_REL), 
											DagGenealogyContext::accept)
									.by(model.getLinkType(GenealogyFixture.ATTENDED_REL))
									.findChildren()
									.visitBy(
											model.getLinkType(GenealogyFixture.LOCATED_IN_REL), 
											DagGenealogyContext::accept)
									.nodes();
		logger.error(context.getPersonNames());
	}
	
	
}
