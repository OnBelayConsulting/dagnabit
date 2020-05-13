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
 * Implementation of the DagModel: Container for all Directed Acyclic Graph (DAG) elements such as nodes and links (relationship, edge).
 * See DagModel for documetation.
 */
public class DagModelImpl implements DagModel {
    
    private HashMap<String, DagNodeImpl> nodeMap = new HashMap<String, DagNodeImpl>();
    
    private Map<String, DagNodeType> nodeTypeMap = new HashMap<String, DagNodeType>();
    
    private Map<String, DagLinkType> linkTypeMap = new  HashMap<>();
    
    private boolean createDefaultLink = true;
    
    
    public DagModelImpl(boolean createDefaultLink) {
    	this.createDefaultLink = createDefaultLink;
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
	public List<DagNode> getNodes() {
		return nodeMap.values().stream().collect(Collectors.toList());
	}

    @Override
	public List<DagNode> findRootNodes() {
    	return nodeMap
    		.values()
    		.stream()
    		.filter(x -> x.isRoot())
    		.collect(Collectors.toList());
    	
    }
    
    @Override
    public List<DagNode> findLeafNodes() {
    	return nodeMap
        		.values()
        		.stream()
        		.filter( e-> e.isLeaf())
        		.collect(Collectors.toList());
    	
    }
    
    @Override
    public List<DagNode> findSolitaryNodes() {
    	
    	return nodeMap
    		.values()
    		.stream()
    		.filter( e-> e.hasNoConnectors())
    		.collect(Collectors.toList());
    	
    }
    
    @Override
	public DagLinkType getDefaultLinkType() {
		return getLinkType(DagLinkType.DEFAULT_TYPE);
	}
	

    
    @Override
    public List<DagLinkType> getLinkTypes() {
    	ArrayList<DagLinkType> types = new ArrayList<DagLinkType>();
    	for (DagLinkType linkType : linkTypeMap.values()) {
    		types.add(linkType);
    	}
    	return types;
    }
    
    @Override
    public DagNodeType getNodeType(String nodeTypeName) {
    	return nodeTypeMap.get(nodeTypeName);
    }
    
    @Override
    public List<DagNodeType> getNodeTypes() {
    	ArrayList<DagNodeType> types = new ArrayList<DagNodeType>();
    	for (DagNodeType nodeType : nodeTypeMap.values()) {
    		types.add(nodeType);
    	}
    	return types;
    }
    
    @Override
    public DagNode addNode(String nodeName) {
    	DagNodeImpl node = new DagNodeImpl(
    			nodeName, 
    			nodeTypeMap.get(DagNodeType.DEFAULT_TYPE));
    	
        nodeMap.put(node.getName(),node);
        
        addNodeToNodeTypeMap(node, DagNodeType.DEFAULT_TYPE);
        return node;
    }
    
    @Override
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

    @Override
    public DagNode getNode(String code) {
        return nodeMap.get(code);
    }
    
    protected DagNodeImpl getNodeImplementation(String code) {
    	return nodeMap.get(code);
    }
    
    @Override
    public  void addDefaultRelationship(
    			DagNode fromNodeIn, 
    			DagNode toNodeIn) {
    	
	   	DagLinkType dagLinkType = linkTypeMap.get(DagLinkType.DEFAULT_TYPE);
	   	if (dagLinkType == null) {
	   		dagLinkType = new DagLinkType(DagLinkType.DEFAULT_TYPE);
	   		linkTypeMap.put(DagLinkType.DEFAULT_TYPE, dagLinkType);
	   	}
	   	 
	   	DagNodeImpl fromNode = nodeMap.get(fromNodeIn.getName());
	   	DagNodeImpl toNode = nodeMap.get(toNodeIn.getName());
	   	
	   	fromNode.addFromThisNodeRelationshipToNode(
	       		 dagLinkType, 
	       		 toNode);
    }
    
    @Override
    public DagLink addRelationship(
    		DagNode fromNodeIn, 
    		String linkTypeName, 
    		DagNode toNodeIn) {

    	
    	 DagLinkType dagLinkType = linkTypeMap.get(linkTypeName);
    	 if (dagLinkType == null) {
    		 dagLinkType = new DagLinkType(linkTypeName);
    		 linkTypeMap.put(linkTypeName, dagLinkType);
    	 }
    	 
    	 DagNodeImpl fromNode = nodeMap.get(fromNodeIn.getName());
    	 DagNodeImpl toNode = nodeMap.get(toNodeIn.getName());
    	
         DagNodeConnector connector = fromNode.addFromThisNodeRelationshipToNode(
        		 dagLinkType, 
        		 toNode);
         
         if (createDefaultLink)
        	 addDefaultRelationship(fromNodeIn, toNodeIn);
         
         return connector.getRelationship(dagLinkType);
    }
    
    @Override
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
