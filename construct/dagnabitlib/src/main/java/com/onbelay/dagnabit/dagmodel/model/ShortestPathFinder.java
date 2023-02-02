package com.onbelay.dagnabit.dagmodel.model;

import java.util.List;

/**
 * Finds the shortest path or route in a Directed Graph with weights. Graph may be b-directional and it may contain cycles.
 * 
 * Note that is algorithm requires weights on the links and the weights must not be negative.
 * @author lefeu
 *
 */
public interface ShortestPathFinder {

	
	/**
	 * Return a route that minimizes the total cost from start to end
	 * @param startNode
	 * @param endNode
	 * @return a DagNodePath if there is any with one to many links from start to end.
	 */
	public DagNodePath findShortestRoute(DagNode startNode, DagNode endNode);

	
	/**
	 * Find the shortest path as a list of DagNodes from Start to end
	 * @param startNode
	 * @param endNode
	 * @return list with dagNodes in order.
	 */
	public List<DagNode> findShortestPath(DagNode startNode, DagNode endNode);
}
