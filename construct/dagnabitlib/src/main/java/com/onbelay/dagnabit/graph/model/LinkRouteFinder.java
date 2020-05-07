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
package com.onbelay.dagnabit.graph.model;

import java.util.List;
import java.util.Map;

import com.onbelay.dagnabit.graph.components.DagNodeSearchResult;

/**
 * A lower level method of navigating the DagModel.
 * 
 * The route finder will find routes or paths traversing either from or to relationships with provided starting and ending nodes. 
 * 
 * @author lefeu
 *
 */
public interface LinkRouteFinder {
	
	public DagPathRoutes findRoutes(DagNode fromNode, DagNode toNode);

	public Map<DagNode, DagPathRoutes> findAllRoutesFrom(DagNode fromNode);
	
	public List<DagNode> discoverBreadthFromRelationships(DagNode rootNodeIn);

	public NodeSearchResult discoverFromRelationships(DagNode startNode);
	
	public NavigationResult discoverToRelationships(DagNode endNode);

	public DagNodeSearchResult discoverToRelationships(DagNode startingNode, DagNode endNode);

	public List<DagNodePath> findAllPathsFrom(DagNode startNode);
	
	/**
	 * Find all paths that end at the endNode
	 * @param endNode - required
	 * @return a list - may be empty of paths/
	 */
	public List<DagNodePath> findAllPathsTo(DagNode endNode);

	public List<DagNodePath> findPathsStartingFromEndingAt(DagNode startNode, DagNode endNode);
	
	public List<DagNodePath> findPathsEndingAtStartingFrom(DagNode endNode, DagNode startNode);

}
