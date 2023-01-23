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
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.onbelay.dagnabit.graph.model.DagContext;
import com.onbelay.dagnabit.graph.model.DagRelationship;
import com.onbelay.dagnabit.graph.model.DagRelationshipType;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodeNavigator;
import com.onbelay.dagnabit.graph.model.DagNodeCategory;
import com.onbelay.dagnabit.graph.model.LinkAnalyser;
import com.onbelay.dagnabit.graph.model.LinkRouteFinder;
import com.onbelay.dagnabit.graph.model.MinimumSpanningTreeFinder;
import com.onbelay.dagnabit.graph.model.NodeVisitor;
import com.onbelay.dagnabit.graph.model.ShortestPathFinder;

/**
 * Implementation of the DagModel: Container for all Directed Acyclic Graph (DAG) elements such as nodes and links (relationship, edge).
 * See DagModel for documetation.
 */
public class DagModelImpl implements DagModel {

	private String modelName;
    
    private HashMap<String, DagNodeImpl> nodeMap = new HashMap<String, DagNodeImpl>();
    
    private Map<String, DagNodeCategory> nodeTypeMap = new HashMap<String, DagNodeCategory>();
    
    private Map<String, DagRelationshipType> linkTypeMap = new  HashMap<>();
    
    private List<DagRelationship> links  = new ArrayList<DagRelationship>();
    
    private List<DagRelationship> defaultLinks  = new ArrayList<DagRelationship>();
    
    private Map<String, DagRelationship> defaultLinkMap = new HashMap<String, DagRelationship>();

	@Override
	public int compareTo(DagModel in) {
		return modelName.compareTo(in.getModelName());
	}

	public DagModelImpl(String modelName) {
		this.modelName = modelName;

		DagNodeCategory defaultNodeType = new DagNodeCategory(DagNodeCategory.DEFAULT_TYPE);
		nodeTypeMap.put(DagNodeCategory.DEFAULT_TYPE, defaultNodeType);

		DagRelationshipType defaultLinkType = new DagRelationshipType(DagRelationshipType.DEFAULT_TYPE);
		linkTypeMap.put(DagRelationshipType.DEFAULT_TYPE, defaultLinkType);
	}

	public DagModelImpl(String modelName, String defaultRelationshipName) {
		this.modelName = modelName;

    	DagNodeCategory defaultNodeType = new DagNodeCategory(DagNodeCategory.DEFAULT_TYPE);
    	nodeTypeMap.put(DagNodeCategory.DEFAULT_TYPE, defaultNodeType);
    	
    	DagRelationshipType defaultLinkType = new DagRelationshipType(defaultRelationshipName);
    	linkTypeMap.put(defaultRelationshipName, defaultLinkType);
    }

	@Override
	public String getModelName() {
		return modelName;
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
    public ShortestPathFinder createShortestPathFinder(DagRelationshipType dagRelationshipType) {
    	return new DagShortestPathRouteFinder(this, dagRelationshipType);
    }
    
    @Override
    public MinimumSpanningTreeFinder createMinimumSpanningTreeFinder(DagRelationshipType linkType) {
    	return new DagMinimumSpanningTreeFinder(this, linkType);
    }
    
    @Override
    public MinimumSpanningTreeFinder createMinimumSpanningTreeFinder(
    		DagRelationshipType linkType,
    		Predicate<DagNode> filterNodePredicate) {
    	
    	return new DagMinimumSpanningTreeFinder(
    			this, 
    			linkType, 
    			filterNodePredicate);
    }
    
    @Override
    public MinimumSpanningTreeFinder createMinimumSpanningTreeFinder(
    		DagRelationshipType linkType,
    		DagRelationshipType mstLinkType) {
    	
    	return new DagMinimumSpanningTreeFinder(
    			this, 
    			linkType, 
    			mstLinkType);
    }
    
    @Override
    public MinimumSpanningTreeFinder createMinimumSpanningTreeFinder(
    		DagRelationshipType linkType,
    		DagRelationshipType mstLinkType,
    		Predicate<DagNode> filterNodePredicate) {
    	
    	return new DagMinimumSpanningTreeFinder(
    			this, 
    			linkType, 
    			mstLinkType, 
    			filterNodePredicate);
    }
    
    
    @Override
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagRelationshipType dagRelationshipType) {
    	
    	DagLinkRouteFinder navigator = new DagLinkRouteFinder(
    			this,
				dagRelationshipType);
    	
    	
    	return navigator;
    }

    @Override
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagRelationshipType dagRelationshipType,
    		DagContext context,
    		NodeVisitor nodeVisitor) {
    	
    	DagLinkRouteFinder navigator = new DagLinkRouteFinder(
    			this,
				dagRelationshipType,
    			context,
    			nodeVisitor);
    	
    	
    	return navigator;
    }

    @Override
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagRelationshipType dagRelationshipType,
    		DagContext context,
    		NodeVisitor nodeVisitor,
    		BiPredicate<DagContext, DagNode> endPredicate,
    		Predicate<DagRelationship> filterLinkPredicate,
    		Predicate<DagNode> filterNodePredicate) {
    	
    	DagLinkRouteFinder navigator = new DagLinkRouteFinder(
    			this,
				dagRelationshipType,
    			context,
    			nodeVisitor,
    			endPredicate,
    			filterLinkPredicate,
    			filterNodePredicate);
    	
    	
    	return navigator;
    }

    
    @Override
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagRelationshipType dagRelationshipType,
    		DagNodeCategory dagNodeCategory) {
    	
    	DagLinkRouteFinder navigator = new DagLinkRouteFinder(
    			this,
				dagRelationshipType,
				dagNodeCategory);
    	
    	
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
	public DagRelationshipType getDefaultRelationshipType() {
		return getRelationshipType(DagRelationshipType.DEFAULT_TYPE);
	}
	

    
    @Override
    public List<DagRelationshipType> getRelationshipTypes() {
    	ArrayList<DagRelationshipType> types = new ArrayList<DagRelationshipType>();
    	for (DagRelationshipType linkType : linkTypeMap.values()) {
    		types.add(linkType);
    	}
    	return types;
    }
    
    @Override
    public DagNodeCategory getNodeCategory(String nodeTypeName) {
    	return nodeTypeMap.get(nodeTypeName);
    }
    
    @Override
    public List<DagNodeCategory> getNodeCategories() {
    	ArrayList<DagNodeCategory> types = new ArrayList<DagNodeCategory>();
    	for (DagNodeCategory nodeType : nodeTypeMap.values()) {
    		types.add(nodeType);
    	}
    	return types;
    }
    
    @Override
    public DagNode addNode(String nodeName) {
    	DagNodeImpl node = new DagNodeImpl(
    			nodeName, 
    			nodeTypeMap.get(DagNodeCategory.DEFAULT_TYPE));
    	
        nodeMap.put(node.getName(),node);
        
        addNodeToNodeTypeMap(node, DagNodeCategory.DEFAULT_TYPE);
        return node;
    }
    
    @Override
    public DagNode addNode(String nodeName, String nodeTypeName) {
    	
    	DagNodeCategory nodeType = nodeTypeMap.get(nodeTypeName);
    	if (nodeType == null) {
    		nodeType = new DagNodeCategory(nodeTypeName);
    		nodeTypeMap.put(nodeTypeName, nodeType);
    	}
    	
    	DagNodeImpl node = new DagNodeImpl(
    			nodeName, 
    			nodeType);
    	
        nodeMap.put(node.getName(),node);
        
        addNodeToNodeTypeMap(node, DagNodeCategory.DEFAULT_TYPE);
        return node;
    }
    
    private void addNodeToNodeTypeMap(DagNodeImpl node, String typeName) {
    	DagNodeCategory nodeType;
    	if (nodeTypeMap.containsKey(typeName)) {
    		nodeType= nodeTypeMap.get(typeName);
    	} else {
    		nodeType = new DagNodeCategory(typeName);
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
    public DagRelationship addDefaultRelationship(
    			DagNode fromNodeIn, 
    			DagNode toNodeIn) {
    	
	   	DagRelationshipType dagRelationshipType = linkTypeMap.get(DagRelationshipType.DEFAULT_TYPE);
	   	if (dagRelationshipType == null) {
	   		dagRelationshipType = new DagRelationshipType(DagRelationshipType.DEFAULT_TYPE);
	   		linkTypeMap.put(DagRelationshipType.DEFAULT_TYPE, dagRelationshipType);
	   	}
	   	 
	   	DagNodeImpl fromNode = nodeMap.get(fromNodeIn.getName());
	   	DagNodeImpl toNode = nodeMap.get(toNodeIn.getName());
	   	
	   	DagNodeConnector connector = fromNode.addFromThisNodeRelationshipToNode(
				dagRelationshipType,
	       		 toNode);
        DagRelationship link = connector.getRelationship(dagRelationshipType);
        defaultLinks.add(link);
        defaultLinkMap.put(
        		createLinkKey(
        				fromNode.getName(), 
        				toNode.getName()), 
        		link);
        
        return link;
    }
    
    private String createLinkKey(String fromNodeName, String toNodeName) {
    	return fromNodeName + ":" + toNodeName;
    }
    
    
    
    @Override
	public DagRelationship addInverse(DagRelationship dagRelationship) {
   	 DagRelationshipType dagRelationshipType = linkTypeMap.get(dagRelationship.getRelationshipType().getName());
   	 
   	 DagNodeImpl fromNode = nodeMap.get(dagRelationship.getToNode().getName());
   	 DagNodeImpl toNode = nodeMap.get(dagRelationship.getFromNode().getName());
   	
        DagNodeConnector connector = fromNode.addFromThisNodeRelationshipToNode(
				dagRelationshipType,
       		 toNode);
        
        DagRelationship link =  connector.getRelationship(dagRelationshipType);
        link.setWeight(dagRelationship.getWeight());
        links.add(link);
        return link;
	}

	@Override
    public DagRelationship addRelationship(
    		DagNode fromNodeIn, 
    		String relationshipTypeName,
    		DagNode toNodeIn) {

    	
    	 DagRelationshipType dagRelationshipType = linkTypeMap.get(relationshipTypeName);
    	 if (dagRelationshipType == null) {
    		 dagRelationshipType = new DagRelationshipType(relationshipTypeName);
    		 linkTypeMap.put(relationshipTypeName, dagRelationshipType);
    	 }
    	 
    	 DagNodeImpl fromNode = nodeMap.get(fromNodeIn.getName());
    	 DagNodeImpl toNode = nodeMap.get(toNodeIn.getName());
    	
         DagNodeConnector connector = fromNode.addFromThisNodeRelationshipToNode(
				 dagRelationshipType,
        		 toNode);

         DagRelationship link =  connector.getRelationship(dagRelationshipType);
         links.add(link);
         return link;
    }
    
    @Override
    public DagRelationshipType getRelationshipType(String name) {
    	DagRelationshipType link =  linkTypeMap.get(name);
    	if (link == null) {
    		link = new DagRelationshipType(name);
    		linkTypeMap.put(name, link);
    	}
    	return link;
    }
    
    @Override
    public List<DagRelationship> getRelationships() {
    	return links;
    }

    protected Map<String, DagNodeImpl> getNodeMap() {
    	return nodeMap;
    }
    
    protected Map<String, DagRelationshipType> getLinkTypeMap() {
    	return linkTypeMap; 
    }

	@Override
	public List<DagRelationship> getRelationships(DagNode fromNode) {
		
		return getNodeImplementation(fromNode.getName())
				.getFromThisNodeConnectors()
				.stream()
				.map( c -> c.getRelationship(getDefaultRelationshipType()))
				.collect(Collectors.toList());
	}

	@Override
	public DagRelationship getDefaultRelationship(DagNode fromNode, DagNode toNode) {
		
		return defaultLinkMap.get(
				createLinkKey(
						fromNode.getName(),
						toNode.getName()));
	}
    
    
}
