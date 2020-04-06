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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Container for all Directed Acyclic Graph (DAG) elements such as nodes and links (relationship, edge).
 * The DagMode
 */
public class DagModelImpl implements DagModel {
    
    private HashMap<String, DagNode> nodeMap = new HashMap<String, DagNode>();
    
    private Map<String, DagNodeType> nodeTypeMap = new HashMap<String, DagNodeType>();
    
    private Map<String, DagLinkType> linkTypeMap = new  HashMap<>();
    
    
    public DagModelImpl() {
    	nodeMap = new HashMap<String, DagNode>();
    	nodeTypeMap = new HashMap<String, DagNodeType>();
    	
    	DagNodeType defaultNodeType = new DagNodeType(DagNodeType.DEFAULT_TYPE);
    	nodeTypeMap.put(DagNodeType.DEFAULT_TYPE, defaultNodeType);
    	
    	linkTypeMap = new HashMap<String, DagLinkType>();
    	DagLinkType defaultLinkType = new DagLinkType(DagLinkType.DEFAULT_TYPE);
    	linkTypeMap.put(DagLinkType.DEFAULT_TYPE, defaultLinkType);
    }
    
    public DagNodeNavigator navigate() {
    	return new DagNodeNavigatorImpl(this);
    }
    
    public LinkAnalyser analyse() {
    	return new LinkAnalyserImpl(this);
    }
    
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagNodeType dagNodeType,
    		DagLinkType dagLinkType) {
    	
    	DagLinkRouteFinder navigator = new DagLinkRouteFinder(
    			this,
    			dagNodeType,
    			dagLinkType);
    	
    	
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
    
    public void addNode(String nodeName) {
    	DagNode node = new DagNode(
    			nodeName, 
    			nodeTypeMap.get(DagNodeType.DEFAULT_TYPE));
    	
        nodeMap.put(node.getName(),node);
        
        addNodeToNodeTypeMap(node, DagNodeType.DEFAULT_TYPE);
    }
    
    public void addNode(String nodeName, String nodeTypeName) {
    	
    	DagNodeType nodeType = nodeTypeMap.get(nodeTypeName);
    	if (nodeType == null) {
    		nodeType = new DagNodeType(nodeTypeName);
    		nodeTypeMap.put(nodeTypeName, nodeType);
    	}
    	
    	DagNode node = new DagNode(
    			nodeName, 
    			nodeType);
    	
        nodeMap.put(node.getName(),node);
        
        addNodeToNodeTypeMap(node, DagNodeType.DEFAULT_TYPE);
    }
    
    private void addNodeToNodeTypeMap(DagNode node, String typeName) {
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
    
    public void addRelationship(
    		DagNode fromNode, 
    		String relationshipName, 
    		DagNode toNode) {

    	
    	 DagLinkType dagLinkType = linkTypeMap.get(relationshipName);
    	 if (dagLinkType == null) {
    		 dagLinkType = new DagLinkType(relationshipName);
    		 linkTypeMap.put(relationshipName, dagLinkType);
    	 }
    	
         fromNode.addFromThisNodeRelationshipToNode(
        		 dagLinkType, 
        		 toNode);
    }
    
    public DagLinkType getLinkType(String name) {
    	return linkTypeMap.get(name);
    }

    public Map<String, DagNodeType> getNodeTypeMap() {
		return nodeTypeMap;
	}

	public Map<String, DagNode> getNodeMap() {
        return nodeMap;
    }

	public Map<String, DagLinkType> getLinkTypeMap() {
		return linkTypeMap;
	}

    
}
