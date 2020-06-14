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
import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodeNavigator;
import com.onbelay.dagnabit.graph.model.DagNodeType;
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
    
    private HashMap<String, DagNodeImpl> nodeMap = new HashMap<String, DagNodeImpl>();
    
    private Map<String, DagNodeType> nodeTypeMap = new HashMap<String, DagNodeType>();
    
    private Map<String, DagLinkType> linkTypeMap = new  HashMap<>();
    
    private List<DagLink> links  = new ArrayList<DagLink>();
    
    private List<DagLink> defaultLinks  = new ArrayList<DagLink>();
    
    private Map<String, DagLink> defaultLinkMap = new HashMap<String, DagLink>();
    
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
    public ShortestPathFinder createShortestPathFinder(DagLinkType dagLinkType) {
    	return new DagShortestPathRouteFinder(this, dagLinkType);
    }
    
    @Override
    public MinimumSpanningTreeFinder createMinimumSpanningTreeFinder(DagLinkType linkType) {
    	return new DagMinimumSpanningTreeFinder(this, linkType);
    }
    
    @Override
    public MinimumSpanningTreeFinder createMinimumSpanningTreeFinder(
    		DagLinkType linkType, 
    		Predicate<DagNode> filterNodePredicate) {
    	
    	return new DagMinimumSpanningTreeFinder(
    			this, 
    			linkType, 
    			filterNodePredicate);
    }
    
    @Override
    public MinimumSpanningTreeFinder createMinimumSpanningTreeFinder(
    		DagLinkType linkType, 
    		DagLinkType mstLinkType) {
    	
    	return new DagMinimumSpanningTreeFinder(
    			this, 
    			linkType, 
    			mstLinkType);
    }
    
    @Override
    public MinimumSpanningTreeFinder createMinimumSpanningTreeFinder(
    		DagLinkType linkType, 
    		DagLinkType mstLinkType, 
    		Predicate<DagNode> filterNodePredicate) {
    	
    	return new DagMinimumSpanningTreeFinder(
    			this, 
    			linkType, 
    			mstLinkType, 
    			filterNodePredicate);
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
    		BiPredicate<DagContext, DagNode> endPredicate,
    		Predicate<DagLink> filterLinkPredicate,
    		Predicate<DagNode> filterNodePredicate) {
    	
    	DagLinkRouteFinder navigator = new DagLinkRouteFinder(
    			this,
    			dagLinkType,
    			context,
    			nodeVisitor,
    			endPredicate,
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
    public  DagLink addDefaultRelationship(
    			DagNode fromNodeIn, 
    			DagNode toNodeIn) {
    	
	   	DagLinkType dagLinkType = linkTypeMap.get(DagLinkType.DEFAULT_TYPE);
	   	if (dagLinkType == null) {
	   		dagLinkType = new DagLinkType(DagLinkType.DEFAULT_TYPE);
	   		linkTypeMap.put(DagLinkType.DEFAULT_TYPE, dagLinkType);
	   	}
	   	 
	   	DagNodeImpl fromNode = nodeMap.get(fromNodeIn.getName());
	   	DagNodeImpl toNode = nodeMap.get(toNodeIn.getName());
	   	
	   	DagNodeConnector connector = fromNode.addFromThisNodeRelationshipToNode(
	       		 dagLinkType, 
	       		 toNode);
        DagLink link = connector.getRelationship(dagLinkType);
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
	public DagLink addInverse(DagLink dagLink) {
   	 DagLinkType dagLinkType = linkTypeMap.get(dagLink.getDagLinkType().getName());
   	 
   	 DagNodeImpl fromNode = nodeMap.get(dagLink.getToNode().getName());
   	 DagNodeImpl toNode = nodeMap.get(dagLink.getFromNode().getName());
   	
        DagNodeConnector connector = fromNode.addFromThisNodeRelationshipToNode(
       		 dagLinkType, 
       		 toNode);
        
        DagLink link =  connector.getRelationship(dagLinkType);
        link.setWeight(dagLink.getWeight());
        links.add(link);
        return link;
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
         
         DagLink link =  connector.getRelationship(dagLinkType);
         links.add(link);
         return link;
    }
    
    @Override
    public DagLinkType getLinkType(String name) {
    	DagLinkType link =  linkTypeMap.get(name);
    	if (link == null) {
    		link = new DagLinkType(name);
    		linkTypeMap.put(name, link);
    	}
    	return link;
    }
    
    @Override
    public List<DagLink> getLinks() {
    	return links;
    }

    protected Map<String, DagNodeImpl> getNodeMap() {
    	return nodeMap;
    }
    
    protected Map<String, DagLinkType> getLinkTypeMap() {
    	return linkTypeMap; 
    }

	@Override
	public List<DagLink> getLinks(DagNode fromNode) {
		
		return getNodeImplementation(fromNode.getName())
				.getFromThisNodeConnectors()
				.stream()
				.map( c -> c.getRelationship(getDefaultLinkType()))
				.collect(Collectors.toList());
	}

	@Override
	public DagLink getDefaultLink(DagNode fromNode, DagNode toNode) {
		
		return defaultLinkMap.get(
				createLinkKey(
						fromNode.getName(),
						toNode.getName()));
	}
    
    
}
