package com.onbelay.dagnabit.graph.model;

public interface DagLink {

	public String getName();
	
	public DagLinkType getDagLinkType();
	
	public DagNode getFromNode();

	public DagNode getToNode();


	public void setData(DagData data);
	
	public DagData getData();
	
}
