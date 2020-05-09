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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.onbelay.dagnabit.graph.exception.DagGraphException;
import com.onbelay.dagnabit.graph.model.DagContext;
import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagMapContext;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodeNavigator;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.DagNodeType;
import com.onbelay.dagnabit.graph.model.LinkRouteFinder;
import com.onbelay.dagnabit.graph.model.NodeSearchResult;
import com.onbelay.dagnabit.graph.model.NodeVisitor;
import com.onbelay.dagnabit.graph.model.TraversalDirectionType;

/**
 * Concrete implementation of the DagNodeNavigator interface.
 * 
 * @author lefeu
 *
 */
public class DagNodeNavigatorImpl implements DagNodeNavigator {

	private List<DagNodeImpl> startingNodes = new ArrayList<DagNodeImpl>();
	private DagLinkType linkType;
	private DagNodeImpl endingNode;
	
	private DagContext context = new DagMapContext();
	
	private Predicate<DagNode> filterNodePredicate = c -> true;

	private Predicate<DagLink> filterLinkPredicate = c -> true;

	private NodeVisitor nodeVisitor = (c, n, l, e) -> { ; } ;

	private boolean noBacktracking = true;
	
	private DagModelImpl model;
	
	private TraversalDirectionType traversalDirectionType;
	
	public DagNodeNavigatorImpl(DagModelImpl model) {
		super();
		this.model = model;
	}

	@Override
	public DagNodeNavigator from(DagNode fromNodeIn) {
		
		if (traversalDirectionType == null)
			traversalDirectionType = TraversalDirectionType.TRAVERSE_STARTING_FROM_ENDING_TO;
		
		startingNodes.add(model.getNodeImplementation(fromNodeIn.getName()));
		
		return this;
	}

	@Override
	public DagNodeNavigator fromBreadthFirst(DagNode fromNodeIn) {
		
		if (traversalDirectionType != null)
			throw new DagGraphException("fromBreadthFirst excludes using To as start");
		
		traversalDirectionType = TraversalDirectionType.TRAVERSE_STARTING_FROM_BREADTH_FIRST;
		
		startingNodes.add(model.getNodeImplementation(fromNodeIn.getName()));
		
		return this;
	}


	@Override
	public DagNodeNavigator by(DagLinkType linkType) {
		this.linkType = linkType;
		return this;
	}

	@Override
	public DagNodeNavigator filterBy(Predicate<DagLink> fn) {
		this.filterLinkPredicate = fn;
		return this;
	}

	@Override
	public DagNodeNavigator visitWith(NodeVisitor nodeVisitor) {
		this.nodeVisitor = nodeVisitor;
		return this;
	}

	@Override
	public DagNodeNavigator using(DagContext context) {
		this.context = context;
		return this;
	}

	@Override
	public DagNodeNavigator forOnly(DagNodeType nodeType) {
		this.filterNodePredicate = c -> c.getNodeType().equals(nodeType); 
		return this;
	}
	
	@Override
	public DagNodeNavigator forOnly(Predicate<DagNode> fn) {
		this.filterNodePredicate = fn;
		return this;
	}

	@Override
	public DagNodeNavigator to(DagNode toNodeIn) {
		
		if (traversalDirectionType == null) 
			traversalDirectionType = TraversalDirectionType.TRAVERSE_STARTING_TO_ENDING_FROM;
		
		this.endingNode = model.getNodeImplementation(toNodeIn.getName());
		
		return this;
	}

	@Override
	public List<DagNode> children() {
		
		if (startingNodes.isEmpty())
			return new ArrayList<DagNode>();
		
		ArrayList<DagNode> endingNodes = new ArrayList<>();
		
		 startingNodes.forEach( startNode -> {
			 
		 	endingNodes.addAll(startNode
				.getFromThisNodeConnectors()
				.stream()
				.filter(c -> c.hasRelationship(linkType) && filterNodePredicate.test(c.getToNode()) && filterLinkPredicate.test(c.getRelationship(linkType)))
				.map(d -> d.getToNode())
				.collect(Collectors.toList()));
		 });
		return endingNodes;
	}

	@Override
	public List<DagNode> parents() {
		
		if (startingNodes.isEmpty())
			return new ArrayList<DagNode>();
		
		ArrayList<DagNode> endingNodes = new ArrayList<>();
		
		 startingNodes.forEach( startNode -> {
			 
		 	endingNodes.addAll(startNode
				.getToThisNodeConnectors()
				.stream()
				.filter(c -> c.hasRelationship(linkType) && filterNodePredicate.test(c.getFromNode()) && filterLinkPredicate.test(c.getRelationship(linkType)))
				.map(d -> d.getFromNode())
				.collect(Collectors.toList()));
		 });
		return endingNodes;
	}
	
	@Override
	public DagNodeNavigator findChildren() {
		if (startingNodes.isEmpty())
			return this;
		
		ArrayList<DagNodeImpl> endingNodes = new ArrayList<>();
		
		 startingNodes.forEach( startNode -> {
			 
		 	endingNodes.addAll(startNode
				.getFromThisNodeConnectors()
				.stream()
				.filter(c -> c.hasRelationship(linkType) && filterNodePredicate.test(c.getToNode()) && filterLinkPredicate.test(c.getRelationship(linkType)))
				.map(d -> d.getToNode())
				.collect(Collectors.toList()));
		 });
		startingNodes = endingNodes; 
		return this;

	}
	
	@Override
	public DagNodeNavigator findParents() {
		if (startingNodes.isEmpty())
			return this;
		
		ArrayList<DagNodeImpl> endingNodes = new ArrayList<>();
		
		 startingNodes.forEach( startNode -> {
			 
		 	endingNodes.addAll(startNode
				.getToThisNodeConnectors()
				.stream()
				.filter(c -> filterNodePredicate.test(c.getFromNode()) && filterLinkPredicate.test(c.getRelationship(linkType)))
				.map(d -> d.getFromNode())
				.collect(Collectors.toList()));
		 });
		startingNodes = endingNodes; 
		return this;

	}
	

	@Override
	public DagNodeNavigator visitBy(DagLinkType linkType, NodeVisitor visitor) {
		
		startingNodes.forEach( startNode -> {
			 
			for (DagNodeConnector c : startNode.getFromThisNodeConnectors()) { 
				
				if (c.hasRelationship(linkType) == false)
					continue;
				
				if (filterNodePredicate.test(c.getFromNode()) == false)
					continue;

				if (filterLinkPredicate.test(c.getRelationship(linkType)) == false)
					continue;
				
				visitor.accept(context, startNode, c.getRelationship(linkType), c.getToNode());
			}
		});

		return this;
	}
	

	@Override
	public DagNodeNavigator findDescendants() {
		List<DagNode> nodes = descendants();
		startingNodes.clear();
		
		for (DagNode n : nodes) {
			startingNodes.add((DagNodeImpl)n);
		}
		return this;
	}
	
	
	private LinkRouteFinder newLinkRouteFinder() {
			return model.createDagLinkRouteFinder(
				linkType,
				context,
				nodeVisitor,
				filterLinkPredicate,
				filterNodePredicate);
	}
	
	
	

	@Override
	public List<DagNodePath> cycles() {
		if (startingNodes.isEmpty())
			throw new DagGraphException("Cycles require at least one starting node");
		
		ArrayList<DagNodePath> cycles = new ArrayList<DagNodePath>();
		for (DagNodeImpl startNode : startingNodes) {
			LinkRouteFinder routeFinder = newLinkRouteFinder();
			NodeSearchResult result = routeFinder.discoverFromRelationships(startNode);
			cycles.addAll(result.getCycles());
		}
		return cycles;
	}

	@Override
	public List<DagNodePath> paths() {
		
		if (startingNodes.isEmpty() && endingNode == null)
			throw new DagGraphException("Either a from() or a to() is required");
		
		LinkRouteFinder routeFinder = newLinkRouteFinder();
		
		
		List<DagNodePath> paths = new ArrayList<>();
		
		if (traversalDirectionType == TraversalDirectionType.TRAVERSE_STARTING_FROM_ENDING_TO) {
			if (startingNodes.isEmpty())
				throw new DagGraphException("fromNode is missing in a traverse from");
			
			if (endingNode != null)
				for (DagNodeImpl startNode : startingNodes)
					paths.addAll(
							routeFinder.findPathsStartingFromEndingAt(
									startNode, 
									endingNode));
			else
				for (DagNodeImpl startNode : startingNodes)
					paths.addAll(
							routeFinder.findAllPathsFrom(startNode));
		} else {
			if (startingNodes.isEmpty() == false)
				for (DagNodeImpl startNode : startingNodes)
					paths.addAll(
							routeFinder.findPathsEndingAtStartingFrom(
									endingNode, 
									startNode));
			else
				return routeFinder.findAllPathsTo(endingNode);
		}
		return paths;
	}
	
	@Override
	public List<DagNode> descendants() {
		if (startingNodes.isEmpty())
			throw new DagGraphException("Either a from() or a fromBreadthFirst() is required");
		
		LinkRouteFinder routeFinder = newLinkRouteFinder();
		
		
		List<DagNode> nodeList = new ArrayList<>();
		
		if (traversalDirectionType == TraversalDirectionType.TRAVERSE_STARTING_FROM_ENDING_TO) {
			for (DagNodeImpl  startNode : startingNodes) {
				NodeSearchResult result = routeFinder.discoverFromRelationships(startNode);
				
				LinkedHashSet<DagNode> nodeSet = new LinkedHashSet<DagNode>();
				
				for (DagNodePath path :result.getPaths()) {
					nodeSet.add(path.getFromNode());
					nodeSet.add(path.getToNode());
				}
				nodeList.addAll(nodeSet);
			}
			
		} else if (traversalDirectionType == TraversalDirectionType.TRAVERSE_STARTING_FROM_BREADTH_FIRST){
			
			for (DagNodeImpl  startNode : startingNodes) {
				nodeList.addAll(
						routeFinder.discoverBreadthFromRelationships(startNode));
			}
			
		}
		return nodeList;
		
	}

	@Override
	public List<DagNode> nodes() {
		ArrayList<DagNode> nodes = new ArrayList<DagNode>();
		for (DagNodeImpl n : startingNodes)
			nodes.add(n);
				
		return nodes;
	}

	@Override
	public DagContext getContext() {
		return context;
	}

	public void setContext(DagContext context) {
		this.context = context;
	}

	public boolean isNoBacktracking() {
		return noBacktracking;
	}

	public DagNodeNavigator setNoBacktracking(boolean noBacktracking) {
		this.noBacktracking = noBacktracking;
		return this;
	}

}
