package com.onbelay.dagnabit.graph.model;

public interface NodeVisitor {

	public void accept(
			DagContext context,
			DagNode startNode,
			DagRelationship relationship,
			DagNode endNode);
	
}
