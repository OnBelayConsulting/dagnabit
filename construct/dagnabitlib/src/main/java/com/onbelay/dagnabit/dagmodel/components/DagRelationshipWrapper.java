package com.onbelay.dagnabit.dagmodel.components;

import com.onbelay.dagnabit.dagmodel.model.DagRelationship;

public class DagRelationshipWrapper {

	private DagRelationship dagRelationship;
	
	private boolean wasProcessed = false;
	private boolean wasIgnored = false;
	
	
	public DagRelationshipWrapper(DagRelationship dagRelationship) {
		super();
		this.dagRelationship = dagRelationship;
	}

	public DagRelationship getRelationship() {
		return dagRelationship;
	}

	public boolean isWasProcessed() {
		return wasProcessed;
	}

	public void setWasProcessed(boolean wasProcessed) {
		this.wasProcessed = wasProcessed;
	}

	public boolean isWasIgnored() {
		return wasIgnored;
	}

	public void setWasIgnored(boolean wasIgnored) {
		this.wasIgnored = wasIgnored;
	}

	public void setDagLink(DagRelationship dagRelationship) {
		this.dagRelationship = dagRelationship;
	}
	
	public boolean isAvailable() {
		return (wasProcessed == false) && (wasIgnored == false);
	}
	
	public boolean isSet() {
		return wasProcessed;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer("From: ");
		buffer.append(dagRelationship.getFromNode().getName());
		buffer.append(" Weight: ");
		buffer.append(dagRelationship.getWeight());
		buffer.append(" To: ");
		buffer.append(dagRelationship.getToNode().getName());
		buffer.append(" Processed: ");
		buffer.append(wasProcessed);
		buffer.append(" Ignored: ");
		buffer.append(wasIgnored);
		return buffer.toString();
	}
	
}
