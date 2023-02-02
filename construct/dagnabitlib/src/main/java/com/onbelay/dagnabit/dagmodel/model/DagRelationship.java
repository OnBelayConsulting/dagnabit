package com.onbelay.dagnabit.dagmodel.model;

public interface DagRelationship extends DagItem {

	public DagRelationshipType getRelationshipType();
	
	public DagNode getFromNode();

	public DagNode getToNode();


	
}
