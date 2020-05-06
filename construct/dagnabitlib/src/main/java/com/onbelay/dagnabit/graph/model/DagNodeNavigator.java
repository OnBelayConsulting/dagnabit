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
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Responsible for navigating the Directed Acyclic Graph (DAG) as defined in a DagModel.
 * Navigator is configure and initiate in a similar manner to streams.
 * 
 * The methods are called to configure return a DagNodeNavigator. 
 * 
 * The following commands will resolve the search to zero or more nodes:
 * nodes
 * paths 
 * 
 * get - will cause the navigator to fire and will result the results.
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
	 * Filter the paths by a predicate based on the link
	 * @param filterLinkpredicate
	 * @return
	 */
	public DagNodeNavigator by (Predicate<DagLink> filterLinkpredicate);
	
	
	
	/**
	 * provide a function that will be called as it visits each link
	 * @param vistor
	 * @return
	 */
	public DagNodeNavigator visitLinkWith (BiConsumer<DagContext, DagLink>  vistor);
	
	/**
	 * Optionally set the nodeType for the "To" node. If the "To" node nodeType is not equal to the specified nodeType then the navigation terminates at the previous node.
	 * @param nodeType
	 * @return a DagNodeNavigator set with the nodeType
	 */
	public DagNodeNavigator forOnly(DagNodeType nodeType);
	
	
	/**
	 * Filter the paths by a predicate based on the DagNode
	 * @param filterNodepredicate
	 * @return
	 */
	public DagNodeNavigator forOnly (Predicate<DagNode> filterNodepredicate);
	
	
	/**
	 * Provide a function that is called when the navigator passes through a node.
	 * @param vistor
	 * @return
	 */
	public DagNodeNavigator visitNodeWith (BiConsumer<DagContext, DagNode>  vistor);

	
	/**
	 * Specify a class that implements a DagContext that will be provided to each visitor
	 * @param context
	 * @return
	 */
	public DagNodeNavigator using(DagContext context);

	
	/**
	 * Retrieve the current context
	 * @return
	 */
	public DagContext getContext();
	
	/**
	 * End result
	 * Generates a list of immediate child nodes that satisfies the previous criteria
	 * @return
	 */
	public List<DagNode> children();
	
	
	/**
	 * Traverse the graph to immediate children and reset the startingNodes.
	 * @return
	 */
	public DagNodeNavigator findChildren();
	
	/**
	 * Traverse the graph from starting nodes to descendants according to earlier instructions and reset the startingNodes.
	 * @return
	 */
	public DagNodeNavigator findDescendants();
	
	/**
	 * Optionally set the "To" node. All other paths are ignored.
	 * @param toNode
	 * @return a DagNodeNavigator set with the toNode
	 */
	public DagNodeNavigator to (DagNode toNode);
	
	/**
	 * End result
	 * return 0 or many DagNodePaths representing the paths from the specified node.
	 * @return
	 */
	public List<DagNodePath> paths();
	
	
	/**
	 * retrieve the current starting nodes
	 * @return a list of zero or more nodes
	 */
	public List<DagNode> nodes();
    
}
