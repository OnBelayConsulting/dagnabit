package com.onbelay.dagnabit.graph.examples.genealogy;

import java.time.LocalDate;

import com.onbelay.dagnabit.graph.model.DagData;
import com.onbelay.dagnabit.graph.model.DagModel;

public class GenealogyFixture {
	private static final String PERSON_TYPE = "PERSON";
	private static final String LOCATION_TYPE = "LOCATION";
	private static final String PARENT_REL = "IS_PARENT_OF";
	private static final String IS_SPOUSE_REL = "IS_SPOUSE_OF";

	private static final String WAS_BORN_IN_REL = "WAS_BORN_IN";

	
	private DagModel model;

	public GenealogyFixture(DagModel model) {
		super();
		this.model = model;
	}
	
	
	public void addPerson(String name, LocalDate birthDate) {
		PersonData data = new PersonData();
		data.setBirthDate(birthDate);
		model
			.addNode(name, PERSON_TYPE)
			.setData(data);
	}
	
	public void addLocation(String name) {
		model.addNode(name, LOCATION_TYPE);
		
	}
	
	public void addIsParentOf(String parentName, String childName) {
		model.addRelationship(
				model.getNode(parentName), 
				PARENT_REL, 
				model.getNode(childName));
	}

	public void addWasBornIn(String personName, String locationName) {
		model.addRelationship(
				model.getNode(personName), 
				WAS_BORN_IN_REL
				, 
				model.getNode(locationName));
	}

	
	public void addIsSpouseOf(String firstName, String secondName) {
		model.addRelationship(
				model.getNode(firstName), 
				IS_SPOUSE_REL, 
				model.getNode(secondName));
		model.addRelationship(
				model.getNode(secondName), 
				IS_SPOUSE_REL, 
				model.getNode(firstName));
	}
	

	public DagData createPersonData() {
		return new PersonData();
	}
	
	
}
