package com.onbelay.dagnabit.graph.components;

import com.onbelay.dagnabit.graph.model.DagLink;

public class DagLinkWrapper {

	private DagLink dagLink;
	
	private boolean wasProcessed = false;
	private boolean wasIgnored = false;
	
	
	public DagLinkWrapper(DagLink dagLink) {
		super();
		this.dagLink = dagLink;
	}

	public DagLink getDagLink() {
		return dagLink;
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

	public void setDagLink(DagLink dagLink) {
		this.dagLink = dagLink;
	}
	
	public boolean isAvailable() {
		return (wasProcessed == false) && (wasIgnored == false);
	}
	
	public boolean isSet() {
		return wasProcessed;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer("From: ");
		buffer.append(dagLink.getFromNode().getName());
		buffer.append(" Weight: ");
		buffer.append(dagLink.getWeight());
		buffer.append(" To: ");
		buffer.append(dagLink.getToNode().getName());
		buffer.append(" Processed: ");
		buffer.append(wasProcessed);
		buffer.append(" Ignored: ");
		buffer.append(wasIgnored);
		return buffer.toString();
	}
	
}
