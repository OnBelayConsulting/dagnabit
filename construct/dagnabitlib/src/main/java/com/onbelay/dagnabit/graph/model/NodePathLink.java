package com.onbelay.dagnabit.graph.model;

public class NodePathLink {

	private DagNode fromNode;
	private DagNode toNode;
	
	private DagLink dagLink;
	
	public NodePathLink(
			DagNode fromNode, 
			DagLink dagLink, 
			DagNode toNode) {
		
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.dagLink = dagLink;
	}

	public DagNode getFromNode() {
		return fromNode;
	}

	public DagNode getToNode() {
		return toNode;
	}

	public DagLinkType getLinkType() {
		return dagLink.getDagLinkType();
	}
	
	
	public DagLink getDagLink() {
		return dagLink;
	}

	public String toString() {
		return fromNode.getName() + " - " + dagLink.getDagLinkType() + " -> " + toNode.getName();
	}
}
