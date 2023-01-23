package com.onbelay.dagnabit.graph.model;

public interface DagNode {


	public String getName();
	
	public DagNodeCategory getCategory();
	
	public boolean isLeaf();
	
	public boolean isRoot();
	
	public void setData(DagData data);
	
	public DagData getData();
	
	public int compareTo(DagNode otherNode);
	
}
