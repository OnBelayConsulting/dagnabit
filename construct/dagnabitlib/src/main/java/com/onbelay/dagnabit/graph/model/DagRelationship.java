package com.onbelay.dagnabit.graph.model;

public interface DagRelationship {

	public String getName();
	
	public DagRelationshipType getRelationshipType();
	
	public DagNode getFromNode();

	public DagNode getToNode();

	public void setWeight(int weight);
	
	public int getWeight();
	
	public void setData(DagData data);
	
	public DagData getData();
	
	
	
}
