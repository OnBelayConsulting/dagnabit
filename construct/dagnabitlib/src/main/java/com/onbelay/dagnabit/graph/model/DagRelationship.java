package com.onbelay.dagnabit.graph.model;

public interface DagRelationship extends DagItem {

	public DagRelationshipType getRelationshipType();
	
	public DagNode getFromNode();

	public DagNode getToNode();


	
}
