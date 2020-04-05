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

/**
 * A lower level method of navigating the DagModel.
 * 
 * @author lefeu
 *
 */
public interface LinkRouteFinder {
	
	public DagPathRoutes findRoutes(DagNode fromNode, DagNode toNode);

	public Map<DagNode, DagPathRoutes> findAllRoutesFrom(DagNode fromNode);

	public NodeSearchResult discoverFromRelationships(DagNode rootNode);
	
	public NavigationResult discoverToRelationships();
	
	public List<DagNodePath> findAllPaths(DagNode fromNode);

	public List<DagNodePath> findPaths(DagNode fromNode, DagNode toNode);


}
