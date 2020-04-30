package com.onbelay.dagnabit.graph.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NodePathLink {

	private DagNode fromNode;
	private DagNode toNode;
	
	private List<DagLinkType> linkTypes = new ArrayList<DagLinkType>();
	
	public NodePathLink(
			DagNode fromNode, 
			Collection<DagLinkType> linkTypes, 
			DagNode toNode) {
		
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.linkTypes.addAll(linkTypes);
	}

	public DagNode getFromNode() {
		return fromNode;
	}

	public DagNode getToNode() {
		return toNode;
	}

	public List<DagLinkType> getLinkTypes() {
		return linkTypes;
	}
	
	
	public String toString() {
		return fromNode.getName() + " - " + linkTypes + " -> " + toNode.getName();
	}
}
