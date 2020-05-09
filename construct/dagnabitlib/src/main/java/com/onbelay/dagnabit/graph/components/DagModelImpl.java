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
package com.onbelay.dagnabit.graph.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.onbelay.dagnabit.graph.model.DagContext;
import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodeNavigator;
import com.onbelay.dagnabit.graph.model.DagNodeType;
import com.onbelay.dagnabit.graph.model.LinkAnalyser;
import com.onbelay.dagnabit.graph.model.LinkRouteFinder;
import com.onbelay.dagnabit.graph.model.NodeVisitor;

/**
 * Container for all Directed Acyclic Graph (DAG) elements such as nodes and links (relationship, edge).
 * The DagMode
 */
public class DagModelImpl implements DagModel {
    
    private HashMap<String, DagNodeImpl> nodeMap = new HashMap<String, DagNodeImpl>();
    
    private Map<String, DagNodeType> nodeTypeMap = new HashMap<String, DagNodeType>();
    
    private Map<String, DagLinkType> linkTypeMap = new  HashMap<>();
    
    
    public DagModelImpl() {
    	nodeMap = new HashMap<String, DagNodeImpl>();
    	nodeTypeMap = new HashMap<String, DagNodeType>();
    	
    	DagNodeType defaultNodeType = new DagNodeType(DagNodeType.DEFAULT_TYPE);
    	nodeTypeMap.put(DagNodeType.DEFAULT_TYPE, defaultNodeType);
    	
    	linkTypeMap = new HashMap<String, DagLinkType>();
    	DagLinkType defaultLinkType = new DagLinkType(DagLinkType.DEFAULT_TYPE);
    	linkTypeMap.put(DagLinkType.DEFAULT_TYPE, defaultLinkType);
    }
    
    @Override
    public DagNodeNavigator navigate() {
    	return new DagNodeNavigatorImpl(this);
    }
    
    @Override
    public LinkAnalyser analyse() {
    	return new LinkAnalyserImpl(this);
    }

    @Override
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagLinkType dagLinkType) {
    	
    	DagLinkRouteFinder navigator = new DagLinkRouteFinder(
    			this,
    			dagLinkType);
    	
    	
    	return navigator;
    }

    @Override
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagLinkType dagLinkType,
    		DagContext context,
    		NodeVisitor nodeVisitor) {
    	
    	DagLinkRouteFinder navigator = new DagLinkRouteFinder(
    			this,
    			dagLinkType,
    			context,
    			nodeVisitor);
    	
    	
    	return navigator;
    }

    @Override
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagLinkType dagLinkType,
    		DagContext context,
    		NodeVisitor nodeVisitor,
    		Predicate<DagLink> filterLinkPredicate,
    		Predicate<DagNode> filterNodePredicate) {
    	
    	DagLinkRouteFinder navigator = new DagLinkRouteFinder(
    			this,
    			dagLinkType,
    			context,
    			nodeVisitor,
    			filterLinkPredicate,
    			filterNodePredicate);
    	
    	
    	return navigator;
    }

    
    @Override
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagLinkType dagLinkType,
    		DagNodeType dagNodeType) {
    	
    	DagLinkRouteFinder navigator = new DagLinkRouteFinder(
    			this,
    			dagLinkType,
    			dagNodeType);
    	
    	
    	return navigator;
    }
    
    @Override
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagLinkType dagLinkType,
    		Predicate<DagNode> filterNodePredicate) {
    	
    	DagLinkRouteFinder navigator = new DagLinkRouteFinder(
    			this,
    			dagLinkType,
    			filterNodePredicate);
    	
    	
    	return navigator;
    }
    
    public List<DagNode> findRootNodes() {
    	return nodeMap
    		.values()
    		.stream()
    		.filter(x -> x.isRoot())
    		.collect(Collectors.toList());
    	
    }
    
    public List<DagNode> findLeafNodes() {
    	return nodeMap
        		.values()
        		.stream()
        		.filter( e-> e.isLeaf())
        		.collect(Collectors.toList());
    	
    }
    
    public List<DagNode> findSolitaryNodes() {
    	
    	return nodeMap
    		.values()
    		.stream()
    		.filter( e-> e.hasNoConnectors())
    		.collect(Collectors.toList());
    	
    }
    
    public List<DagLinkType> getLinkTypes() {
    	ArrayList<DagLinkType> types = new ArrayList<DagLinkType>();
    	for (DagLinkType linkType : linkTypeMap.values()) {
    		types.add(linkType);
    	}
    	return types;
    }
    
    public DagNodeType getNodeType(String nodeTypeName) {
    	return nodeTypeMap.get(nodeTypeName);
    }
    
    public List<DagNodeType> getNodeTypes() {
    	ArrayList<DagNodeType> types = new ArrayList<DagNodeType>();
    	for (DagNodeType nodeType : nodeTypeMap.values()) {
    		types.add(nodeType);
    	}
    	return types;
    }
    
    public DagNode addNode(String nodeName) {
    	DagNodeImpl node = new DagNodeImpl(
    			nodeName, 
    			nodeTypeMap.get(DagNodeType.DEFAULT_TYPE));
    	
        nodeMap.put(node.getName(),node);
        
        addNodeToNodeTypeMap(node, DagNodeType.DEFAULT_TYPE);
        return node;
    }
    
    public DagNode addNode(String nodeName, String nodeTypeName) {
    	
    	DagNodeType nodeType = nodeTypeMap.get(nodeTypeName);
    	if (nodeType == null) {
    		nodeType = new DagNodeType(nodeTypeName);
    		nodeTypeMap.put(nodeTypeName, nodeType);
    	}
    	
    	DagNodeImpl node = new DagNodeImpl(
    			nodeName, 
    			nodeType);
    	
        nodeMap.put(node.getName(),node);
        
        addNodeToNodeTypeMap(node, DagNodeType.DEFAULT_TYPE);
        return node;
    }
    
    private void addNodeToNodeTypeMap(DagNodeImpl node, String typeName) {
    	DagNodeType nodeType;
    	if (nodeTypeMap.containsKey(typeName)) {
    		nodeType= nodeTypeMap.get(typeName);
    	} else {
    		nodeType = new DagNodeType(typeName);
    		nodeTypeMap.put(typeName, nodeType);
    	}
    }

    public DagNode getNode(String code) {
        return nodeMap.get(code);
    }
    
    protected DagNodeImpl getNodeImplementation(String code) {
    	return nodeMap.get(code);
    }
    
    public DagLink addRelationship(
    		DagNode fromNodeIn, 
    		String relationshipName, 
    		DagNode toNodeIn) {

    	
    	 DagLinkType dagLinkType = linkTypeMap.get(relationshipName);
    	 if (dagLinkType == null) {
    		 dagLinkType = new DagLinkType(relationshipName);
    		 linkTypeMap.put(relationshipName, dagLinkType);
    	 }
    	 
    	 DagNodeImpl fromNode = nodeMap.get(fromNodeIn.getName());
    	 DagNodeImpl toNode = nodeMap.get(toNodeIn.getName());
    	
         DagNodeConnector connector = fromNode.addFromThisNodeRelationshipToNode(
        		 dagLinkType, 
        		 toNode);
         
         return connector.getRelationship(dagLinkType);
    }
    
    public DagLinkType getLinkType(String name) {
    	return linkTypeMap.get(name);
    }

    protected Map<String, DagNodeImpl> getNodeMap() {
    	return nodeMap;
    }
    
    protected Map<String, DagLinkType> getLinkTypeMap() {
    	return linkTypeMap; 
    }
}
