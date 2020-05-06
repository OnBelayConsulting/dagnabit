package com.onbelay.dagnabit.graph.model;

public interface DagNode {


	public String getName();
	
	public DagNodeType getNodeType();
	
	public boolean isLeaf();
	
	public boolean isRoot();
	
	public void setData(DagData data);
	
	public DagData getData();
	
}
