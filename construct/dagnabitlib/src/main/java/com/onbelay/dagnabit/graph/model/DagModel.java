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
import java.util.function.Predicate;



/**
 * Defines a common interface for a DagModel.
 * A Dag model acts as a Container for all Directed Acyclic Graph (DAG) elements such as nodes and links (vertices, edges).
 * 
 */
public interface DagModel {
    
    
	/**
	 * Create a navigator to be used in the fluent style of navigating this model.
	 * See DagNodeNavigor for more information.
	 * @return an instance of DagNodeNavigator
	 */
    public DagNodeNavigator navigate();
    
    /**
     * Return a linkAnalyser to be use in the fluent style of analyzing the links or relationships in this model.
     * @return
     */
    public LinkAnalyser analyse();
    
    /**
     * Create a LinkRouteFinder to navigate the model. This is a lower level alternative to the navigate fluent style.
     * @param linkType - the relationship or link type to navigate.
     * @return an instance of LinkRouteFinder
     */
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagLinkType linkType);

    /**
     * Create a LinkRouteFinder to navigate the model. This is a lower level alternative to the navigate fluent style.
     * @param linkType - the relationship or link type to navigate.
     * @param context - a user provided implementation of DagContext that is passed in to the nodeVisitor.
     * @param nodeVisitor - a lambda that implements the NodeVisitor interface that is called the graph traverses from one node to the other.
     * @param filterLinkPredicate - a lambda predicate based on a DagLink will return a true or false on whether the relationship should be navigated.
     * @param filterNodePredicate - a lambda predicate based on the target node in the traversal that will return true or false on whether the relationship should be navigated.   
     * @return an instance of LinkRouteFinder
     */
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagLinkType linkType,
    		DagContext context,
    		NodeVisitor nodeVisitor,
    		Predicate<DagLink> filterLinkPredicate,
    		Predicate<DagNode> filterNodePredicate);

    /**
     * Create a LinkRouteFinder to navigate the model. This is a lower level alternative to the navigate fluent style.
     * @param linkType - the relationship or link type to navigate.
     * @param nodeType - use the nodeType of the targetNode to determine if the relationship should be traversed. Similar to the filterNodePredicate but checks nodeType.
     * @return an instance of LinkRouteFinder
     */
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagLinkType linkType,
    		DagNodeType nodeType);

    /**
     * Create a LinkRouteFinder to navigate the model. This is a lower level alternative to the navigate fluent style.
     * @param linkType - the relationship or link type to navigate.
     * @param context - a user provided implementation of DagContext that is passed in to the nodeVisitor.
     * @param nodeVisitor - a lambda that implements the NodeVisitor interface that is called the graph traverses from one node to the other.
     * @return an instance of LinkRouteFinder
     */
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagLinkType linkType,
    		DagContext context,
    		NodeVisitor nodeVisitor);

    /**
     * Return a list of all the nodes added to this model.
     * @return
     */
    public List<DagNode> getNodes();
    
    /**
     * Return a list of nodes which have only "From" relationships but no "To" relationships.
     * @return
     */
    public List<DagNode> findRootNodes();
    
    /**
     * Return a list of nodes that have only "To" relationships but no "From" relationships.
     * @return
     */
    public List<DagNode> findLeafNodes();
    
    /**
     * Return a list of nodes that have no "From" or "To" relationships
     * @return
     */
    public List<DagNode> findSolitaryNodes();
    
    /**
     * Return a list of linkTypes that were used to create relationships.
     * @return
     */
    
	public DagLinkType getDefaultLinkType();
	

    public List<DagLinkType> getLinkTypes();
    
    /**
     * Return a DagNodeType based on the node type name. Note that node type names are registered when a node is added with a node type.
     * @param nodeTypeName
     * @return
     */
    public DagNodeType getNodeType(String nodeTypeName);

    /**
     * Return a list of all the node types registered in th model.
     * @return
     */
    public List<DagNodeType> getNodeTypes();
    
    /**
     * Add a new node with the given nodeName. This will create a DagNode internally.
     * @param nodeName
     * @return a DagNode instance.
     */
    public DagNode addNode(String nodeName);
    
    /**
     * Add a new node with a node type name. The node will be created and the node type will be registered.
     * @param nodeName - name of node to create
     * @param nodeTypeName - name of node type to register if not already registered.
     * @return
     */
    public DagNode addNode(String nodeName, String nodeTypeName);
    

    /**
     * Return the node identified by name
     * @param name - node name
     * @return null or a DagNode.
     */
    public DagNode getNode(String name);
    
    /**
     * Explicitly add a default relationship between the two nodes. Note that the default behaviour is to add
     * a default link when a addRelationship() is called.
     * @param fromNode create a default relationship using DagLinkType.DEFAULT_REL from fromNode to
     * @param toNode -the toNode
     */
    public void addDefaultRelationship(
    		DagNode fromNode, 
    		DagNode toNode);
    
    /**
     * Add a named relationship between to nodes. This method will register the linkTypeName if it hasn't been used before.
     * @param fromNode
     * @param linkTypeName - the linkType name for example "isParentOf". This not the same as the daglink name which which is automatically created as
     * 						fromNode.name + "-" linkTypeName + ">" + toNode.name
     * @param toNode
     * @return a DagLink representing the link between the two nodes.
     */
    public DagLink addRelationship(
    		DagNode fromNode, 
    		String linkTypeName, 
    		DagNode toNode);
    
    /**
     * Return a DagLinkType for a previously registered linkType. Note adding a relationship with a new 
     * @param name
     * @return
     */
    public DagLinkType getLinkType(String name);

    
}
