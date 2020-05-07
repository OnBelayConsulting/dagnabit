package com.onbelay.dagnabit.graph.model;

public interface NodeVisitor {

	public void accept(DagContext context, DagNode startNode, DagLink link, DagNode endNode);
	
}
