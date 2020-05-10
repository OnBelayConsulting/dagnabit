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

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Responsible for navigating the Directed Acyclic Graph (DAG) as defined in a DagModel.
 * Navigator is configurable and initiated in a similar manner to streams.
 * 
 * The methods that are called to configure return a DagNodeNavigator. 
 * 
 * The following commands will resolve the search to zero or more nodes:
 * paths 
 * descendants
 * children
 * parents
 *  
 * nodes will return the current list of starting nodes.
 */
public interface DagNodeNavigator {

	
	/**
	 * A variation of from that takes a list of nodes.
	 * Note that the results of each traversal are simply appended.
	 * @param fromNodes
	 * @return
	 */
	public DagNodeNavigator from (List<DagNode> fromNodes);

	
	/**
	 * One From Required 
	 * set the starting node that will be navigated from using the "from" relationships
	 * @param fromNode
	 * @return a DagNodeNavigator set with the fromNode
	 */
	public DagNodeNavigator from (DagNode fromNode);
	

	/**
	 * One From Required 
	 * Set the starting node for a breadth first search.
	 * @param fromNodeIn
	 * @return
	 */
	public DagNodeNavigator fromBreadthFirst(DagNode fromNode);

	
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
	public DagNodeNavigator filterBy (Predicate<DagLink> filterLinkpredicate);

	
	/**
	 * Sort according to natural order
	 * @return
	 */
	public DagNodeNavigator sorted();

	
	/**
	 * Sort according to the provide comparator
	 * @param comparator
	 * @return
	 */
	public DagNodeNavigator sorted(Comparator<DagNode> comparator);
	
	/**
	 * Reset all the visitors, predicates and sort criteria
	 * @return
	 */
	public DagNodeNavigator reset();
	
	
	/**
	 * provide a function that will be called as it visits each connection (startNode, link, endNode)
	 * @param vistor
	 * @return
	 */
	public DagNodeNavigator visitWith (NodeVisitor vistor);
	
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
	 * Visit children nodes with the following visitor.
	 * This method will not alter the starting nodes but will simply visit. 
	 * @param linkType
	 * @param visitor - a lambda that implements the NodeVisitor accept
	 * @return
	 */
	public DagNodeNavigator visitBy(DagLinkType linkType, NodeVisitor visitor);
	
	/**
	 * Specify a class that implements a DagContext that will be provided to each visitor
	 * @param context
	 * @return
	 */
	public DagNodeNavigator using(DagContext context);

	
	public DagNodeNavigator setNoBacktracking(boolean backtracking);
	
	/**
	 * Retrieve the current context
	 * @return
	 */
	public DagContext getContext();
	
	/**
	 * End result
	 * Generates a list of immediate child nodes (navigates the from relationship).
	 * @return
	 */
	public List<DagNode> children();
	
	/**
	 * End result
	 * Generates a list of immediate parent nodes (navigates the to relationship).
	 * @return
	 */
	public List<DagNode> parents();
	
	
	/**
	 * Traverse the graph using the from relationship to the immediate children and reset the startingNodes.
	 * @return
	 */
	public DagNodeNavigator findChildren();

	
	/**
	 * Navigate the to relationship to find the parents of this node abd reset the startingNodes.
	 * @return
	 */
	public DagNodeNavigator findParents();

	/**
	 * Traverse the graph from starting nodes to descendants according to earlier instructions and reset the startingNodes.
	 * @return
	 */
	public DagNodeNavigator findDescendants();
	

	/**
	 * End Result
	 * Return a list of descendants either in breadth  first or depth first order.
	 * @return
	 */
	public List<DagNode> descendants();

	
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
	 * Return all the paths that result in a circular reference
	 * @return
	 */
	public List<DagNodePath> cycles();

	
	/**
	 * retrieve the current starting nodes
	 * @return a list of zero or more nodes
	 */
	public List<DagNode> nodes();
    
}
