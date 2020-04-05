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

/**
 * Responsible for navigating the Directed Acyclic Graph (DAG) as defined in a DagModel.
 * Navigator is configure and initiate in a similar manner to streams.
 * 
 * The methods are called to configure return a DagNodeNavigator. The paths command will initiate the search.
 *  
 * 
 */
public interface DagNodeNavigator {

	/**
	 * Required
	 * set the starting node that will be navigated from using the "from" relationships
	 * @param fromNode
	 * @return a DagNodeNavigator set with the fromNode
	 */
	public DagNodeNavigator from (DagNode fromNode);
	
	/**
	 * Optionally specify a LinkType to filter the relationships that will be navigated.
	 * @param linkType
	 * @return a DagNodeNavigator set with the linkType
	 */
	public DagNodeNavigator by (DagLinkType linkType);
	
	/**
	 * Optionally set the nodeType for the "To" node. If the "To" node nodeType is not equal to the specified nodeType then the navigation terminates at the previous node.
	 * @param nodeType
	 * @return a DagNodeNavigator set with the nodeType
	 */
	public DagNodeNavigator forOnly(DagNodeType nodeType);
	
	
	/**
	 * Returns a list of adjacent nodes that satisfies the previous criteria
	 * @return
	 */
	public List<DagNode> adjacent();
	
	
	/**
	 * Optionally set the "To" node. All other paths are ignored.
	 * @param toNode
	 * @return a DagNodeNavigator set with the toNode
	 */
	public DagNodeNavigator to (DagNode toNode);
	
	/**
	 * return 0 or many DagNodePaths representing the paths from the specified node.
	 * @return
	 */
	public List<DagNodePath> paths();
    
}
