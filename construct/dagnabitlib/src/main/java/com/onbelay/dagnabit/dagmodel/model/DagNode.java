package com.onbelay.dagnabit.dagmodel.model;

public interface DagNode extends DagItem{

	public DagNodeCategory getCategory();
	
	public boolean isLeaf();
	
	public boolean isRoot();

	public int compareTo(DagNode otherNode);
	
}
