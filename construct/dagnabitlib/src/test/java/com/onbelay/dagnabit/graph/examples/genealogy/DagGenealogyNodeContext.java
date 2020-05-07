package com.onbelay.dagnabit.graph.examples.genealogy;

public class DagGenealogyNodeContext {

	private String name;
	
	private String nameOfSchool;
	
	private String locationSchoolAttended;
	
	private String locationWasBornIn;

	public DagGenealogyNodeContext(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	

	public String getNameOfSchool() {
		return nameOfSchool;
	}

	public void setNameOfSchool(String nameOfSchool) {
		this.nameOfSchool = nameOfSchool;
	}

	public String getLocationSchoolAttended() {
		return locationSchoolAttended;
	}

	public void setLocationSchoolAttended(String locationSchoolAttended) {
		this.locationSchoolAttended = locationSchoolAttended;
	}

	public String getLocationWasBornIn() {
		return locationWasBornIn;
	}

	public void setLocationWasBornIn(String locationWasBornIn) {
		this.locationWasBornIn = locationWasBornIn;
	}
	
	
	
}
