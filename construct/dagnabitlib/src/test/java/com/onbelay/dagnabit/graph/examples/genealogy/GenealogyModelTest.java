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
import java.util.function.Predicate;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.onbelay.dagnabit.graph.factories.DagModelFactory;
import com.onbelay.dagnabit.graph.model.DagData;
import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.LinkRouteFinder;
import com.onbelay.dagnabit.graph.model.NodeSearchResult;

public class GenealogyModelTest  {
	private static Logger logger = LoggerFactory.getLogger(GenealogyModelTest.class);

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
		
		fixture.addPerson("John Smith", LocalDate.of(1969, 4, 3));
		fixture.addAttended("John Smith", "University of Waterloo");
		fixture.addWasBornIn("John Smith", "Toronto");
		
		fixture.addPerson("Jane Smith", LocalDate.of(1967, 6, 23));
		fixture.addIsSpouseOf("John Smith", "Jane Smith");
		fixture.addAttended("Jane Smith", "U of T");
		fixture.addWasBornIn("Jane Smith", "Montreal");

		
		fixture.addPerson("Andrew Smith", LocalDate.of(1991, 6, 13));
		fixture.addAttended("Andrew Smith", "U of T");
		fixture.addWasBornIn("Andrew Smith", "Toronto");
		
		fixture.addIsParentOf("John Smith", "Andrew Smith");
		fixture.addIsParentOf("Jane Smith", "Andrew Smith");
		
		fixture.addPerson("Anna Colvy-Smith", LocalDate.of(1992, 3, 11));
		fixture.addAttended("Anna Colvy-Smith", "U of T");
		fixture.addWasBornIn("Anna Colvy-Smith", "Toronto");

		fixture.addPerson("William Colvy-Smith", LocalDate.of(2012, 1, 18));
		fixture.addAttended("William Colvy-Smith", "UBCO");
		fixture.addWasBornIn("William Colvy-Smith", "Toronto");

		fixture.addIsParentOf("Andrew Smith", "William Colvy-Smith");
		fixture.addIsParentOf("Anna Colvy-Smith", "William Colvy-Smith");
		
		fixture.addPerson("Susan Smith", LocalDate.of(2001, 3, 10));
		fixture.addAttended("Susan Smith", "University of Waterloo");
		fixture.addWasBornIn("Susan Smith", "Montreal");
		
		fixture.addIsParentOf("John Smith", "Susan Smith");
		fixture.addIsParentOf("Jane Smith", "Susan Smith");
		
		fixture.addPerson("Mary Smith", LocalDate.of(1942, 1, 17));
		fixture.addPerson("Robert Smith", LocalDate.of(1941, 1, 17));

		// Grandparents
		fixture.addIsParentOf("Robert Smith", "John Smith");
		fixture.addIsParentOf("Mary Smith", "John Smith");
		
		// The Colvys
		fixture.addPerson("William Colvy", LocalDate.of(2070, 10, 18));
		fixture.addAttended("William Colvy", "UBCO");
		fixture.addWasBornIn("William Colvy", "Toronto");
		
		fixture.addPerson("Sally Colvy", LocalDate.of(2070, 5, 6));
		fixture.addAttended("Sally Colvy", "U of T");
		fixture.addWasBornIn("Sally Colvy", "Montreal");
		
		fixture.addIsParentOf("William Colvy", "Anna Colvy-Smith");
		fixture.addIsParentOf("Sally Colvy", "Anna Colvy-Smith");
		
		
		
	}


	@Test
	public void testNoCycles() {

		for (DagNode node : model.findRootNodes()) {
			for (DagLinkType linkType : model.getLinkTypes()) {
				
				LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
						linkType);
				NodeSearchResult nodeSearchResult = routeFinder.discoverFromRelationships(node);
				assertEquals(0, nodeSearchResult.getCycles().size());
			}
		}

	}
	
	@Test
	public void testAncestors() {
		
		List<DagNode> nodes = model
				.navigate()
				.from(model.getNode("William Colvy-Smith"))
				.by(model.getLinkType(GenealogyFixture.PARENT_REL))
				.ancestors();
		
		for (DagNode p : nodes) {
			logger.error(p.getName());
		}
		
		
	}
	
	@Test
	public void testPredicate() {
		
		final LocalDate date = LocalDate.of(1970, 1, 1);
		
		Predicate<DagNode> nodePredicate = n -> {
			
			DagData data = n.getData();
			PersonData personData = (PersonData)data; 
			return personData.getBirthDate().isAfter(date);
		};
		
		List<DagNode> nodes = model
				.navigate()
				.from(model.getNode("John Smith"))
				.by(model.getLinkType(GenealogyFixture.PARENT_REL))
				.forOnly(nodePredicate)
				.descendants();
		
		for (DagNode p : nodes) {
			logger.error(p.getName());
		}
	}
	
	@Test
	public void testNavigateToSchool() {
		
		List<DagNode> nodes = model
									.navigate()
									.from(model.getNode("John Smith"))
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
									.from(model.getNode("John Smith"))
									.by(model.getLinkType(GenealogyFixture.ATTENDED_REL))
									.findChildren()
									.by(model.getLinkType(GenealogyFixture.ATTENDED_REL))
									.parents();
		for (DagNode p : nodes) {
			logger.error(p.getName());
		}
	}
	
	
	@Test
	public void testParents() {
		List<DagNode> nodes = model
				.navigate()
				.from(model.getNode("Andrew Smith"))
				.by(model.getLinkType(GenealogyFixture.PARENT_REL))
				.parents();
		
		for (DagNode p : nodes) {
			logger.error(p.getName());
		}
	}
	
	
	@Test
	public void testFindChildrenAttendingSchoolInCityBornIn() {
		
		DagGenealogyContext context = new DagGenealogyContext();
		
			model
									.navigate()
									.from(model.getNode("John Smith"))
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
		
		logger.error(context.getPersonNames().toString());
	}
	
	
}
