/*
 Copyright 2019, OnBelay Consulting Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.  
 */
package com.onbelay.dagnabit.dagmodel.model;

import java.util.List;
import java.util.Map;

/**
 * A lower level method of navigating the DagModel.
 * 
 * The route finder will find routes, paths or nodes traversing either from or to relationships with provided starting and ending nodes. 
 * 
 * @author lefeu
 *
 */
public interface LinkRouteFinder {
	
	/**
	 * Return a set of routes from the fromNode to the toNode (children)
	 * @param fromNode
	 * @param toNode
	 * @return a DagPathRoutes - a container of routes
	 */
	public DagPathRoutes findRoutes(DagNode fromNode, DagNode toNode);

	
	/**
	 * Creates a map based on the nodes reached from this node and a list of one or more paths  
	 * @param fromNode navigate the from side
	 * @return a map of the to nodes and their paths
	 */
	public Map<DagNode, DagPathRoutes> findAllRoutesFrom(DagNode fromNode);
	
	/**
	 * Return a list of descendants based on a breadth-first traversal strategy.
	 * @param startNode - node to start traversal from.
	 * @return a list of nodes processed in order of search
	 */
	public List<DagNode> findDescendantsBreadthFirst(DagNode startNode);


	/**
	 * Return a list of descendants based on the default depth-first strategy
	 * @param startNode - node to start traversal from
	 * @return a list of descendant nodes in order of search
	 */
	public List<DagNode> findDescendants(DagNode startNode);
	
	/**
	 * Return a NodeSearchResult that contains the paths and any cycles from this startNode.
	 * The paths are the same paths returned by findAllPathsFrom.
	 * @param startNode - node to start the traversal from
	 * @return a NodeSearchResult
	 */
	public NodeSearchResult discoverFromRelationships(DagNode startNode);
	
	/**
	 * Return a NodeSearchResult that contains the paths and any cycles to this startNode
	 * @param startNode - the node to start traversing the "To" relationships.
	 * @return a NodeSearchResult
	 */
	public NodeSearchResult discoverToRelationships(DagNode startNode);

	/**
	 * Return a NodeSearchResult that contains the paths and any cycles to this startNode from the end node
	 * @param startingNode - the node to start traversing the "To" relationships from (ancestors search)
	 * @param endNode - filter the paths based on paths that end at this node. (Will be the ancestor the the top level from node).
	 * @return a NodeSearchResult
	 */
	public NodeSearchResult discoverToRelationships(DagNode startingNode, DagNode endNode);

	
	/**
	 * Return a list of paths from the provided start node. (Navigates the from relationship to children.)
	 * @param startNode node to start traversing the from relationships from.
	 * @return
	 */
	public List<DagNodePath> findAllPathsFrom(DagNode startNode);
	
	/**
	 * Return a list of paths to the provided end node. (navigates the to relationship to the ancestors.)
	 * @param startNode - node to start traversing the to relationships from.
	 * @return a list of zero or more paths
	 */
	public List<DagNodePath> findAllPathsTo(DagNode startNode);

	
	/**
	 * Return a list of paths starting from the startNode and ending at the endNode
	 * @param startNode - node to start traversing the from relationships from
	 * @param endNode - the node to filter out paths for
	 * @return
	 */
	public List<DagNodePath> findPathsStartingFromEndingAt(DagNode startNode, DagNode endNode);
	
	
	/**
	 * Return a list of paths starting at the startNode and ending at the endNode by navigating the to relationship. (Ancestors)
	 * @param endNode - filter the results by this node
	 * @param startNode the node to start the traversal from
	 * @return a list of zero or more paths.
	 */
	public List<DagNodePath> findPathsEndingAtStartingFrom(DagNode endNode, DagNode startNode);

}
