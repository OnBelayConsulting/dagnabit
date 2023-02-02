package com.onbelay.dagnabit.dagmodel.model;

import java.util.List;

public interface MinimumSpanningTreeFinder {
	
	/**
	 * Determine the minimum spanning tree and return its representation as a series of DagLinks.
	 * If mstLinkType is set then also create a new sub DAG with that linkType to represent the MST.
	 * @param startingNode - node the finder starts at.
	 * @return
	 */
	public List<DagRelationship> determineMinimumSpanningTree(DagNode startingNode);
	

}
