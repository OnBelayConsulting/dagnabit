package com.onbelay.dagnabit.graph.examples.genealogy;

import java.time.LocalDate;

import com.onbelay.dagnabit.graph.model.DagData;
import com.onbelay.dagnabit.graph.model.DagModel;

public class GenealogyFixture {
	public static final String PERSON_TYPE = "PERSON";
	public static final String CITY_TYPE = "CITY";
	public static final String SCHOOL_TYPE = "SCHOOL";
	
	
	public static final String PARENT_REL = "IS_PARENT_OF";
	public static final String IS_SPOUSE_REL = "IS_SPOUSE_OF";

	public static final String WAS_BORN_IN_REL = "WAS_BORN_IN";

	public static final String ATTENDED_REL = "ATTENDED";

	public static final String LOCATED_IN_REL = "LOCATED_IN";

	
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
	
	public void addCity(String name) {
		model.addNode(name, CITY_TYPE);
		
	}

	public void addSchool(String name) {
		model.addNode(name, SCHOOL_TYPE);
		
	}

	
	public void addIsParentOf(String parentName, String childName) {
		model.addRelationship(
				model.getNode(parentName), 
				PARENT_REL, 
				model.getNode(childName));
	}
	
	public void addIsLocatedIn(String nodeName, String locationName) {
		model.addRelationship(
				model.getNode(nodeName), 
				LOCATED_IN_REL, 
				model.getNode(locationName));
	}
	
	public void addAttended(String personName, String schooName) {
		model.addRelationship(
				model.getNode(personName), 
				ATTENDED_REL, 
				model.getNode(schooName));
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
