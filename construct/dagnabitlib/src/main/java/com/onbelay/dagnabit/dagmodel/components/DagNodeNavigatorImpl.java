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
package com.onbelay.dagnabit.dagmodel.components;

import com.onbelay.dagnabit.dagmodel.exception.DagGraphException;
import com.onbelay.dagnabit.dagmodel.model.*;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Concrete implementation of the DagNodeNavigator interface.
 * 
 * @author lefeu
 *
 */
public class DagNodeNavigatorImpl implements DagNodeNavigator {

	private List<DagNodeImpl> startingNodes = new ArrayList<DagNodeImpl>();
	private DagRelationshipType relationshipType;
	
	private DagContext context = new DagMapContext();

	private BiPredicate<DagContext, DagNode> endPredicate;

	private Predicate<DagNode> filterNodePredicate = c -> true;

	private Predicate<DagRelationship> filterLinkPredicate = c -> true;

	private NodeVisitor nodeVisitor = (c, n, l, e) -> { ; } ;

	private Comparator<DagNode> nodeComparator = null;
	
	private boolean noBacktracking = true;
	
	private DagModelImpl model;
	
	private TraversalDirectionType traversalDirectionType = TraversalDirectionType.TRAVERSE_DEPTH_FIRST;
	
	public DagNodeNavigatorImpl(DagModelImpl model) {
		super();
		this.model = model;
	}

	@Override
	public DagNodeNavigator from(List<DagNode> fromNodes) {
		this.startingNodes.clear();
		
		for (DagNode n : fromNodes)
			startingNodes.add(model.getNodeImplementation(n.getName()));
		return this;
	}

	
	@Override
	public DagNodeNavigator from(DagNode fromNodeIn) {
		startingNodes.add(model.getNodeImplementation(fromNodeIn.getName()));
		
		return this;
	}

	@Override
	public DagNodeNavigator breadthFirst() {
		
		traversalDirectionType = TraversalDirectionType.TRAVERSE_BREADTH_FIRST;
		
		return this;
	}


	@Override
	public DagNodeNavigator by(DagRelationshipType relationshipType) {
		this.relationshipType = relationshipType;
		return this;
	}

	@Override
	public DagNodeNavigator filterBy(Predicate<DagRelationship> fn) {
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
	public DagNodeNavigator forOnly(DagNodeCategory nodeCategory) {
		this.filterNodePredicate = c -> c.getCategory().equals(nodeCategory);
		return this;
	}
	
	@Override
	public DagNodeNavigator forOnly(Predicate<DagNode> fn) {
		this.filterNodePredicate = fn;
		return this;
	}

	@Override
	public DagNodeNavigator until (BiPredicate<DagContext, DagNode> endPredicate) {
		
		
		this.endPredicate = endPredicate;
		
		return this;
	}
	
	@Override
	public DagNodeNavigator sorted() {
		nodeComparator = (c1, c2) -> c1.compareTo(c2); 
		return this;
	}

	
	@Override
	public DagNodeNavigator sorted(Comparator<DagNode> comparator) {
		this.nodeComparator = comparator;
		return this;
	}
	
	@Override
	public DagNodeNavigator reset() {
		this.nodeComparator = null;
		this.endPredicate = null;
		this.filterLinkPredicate = c -> true;
		this.filterNodePredicate = c -> true;
		this.nodeVisitor = (c, n, l, e) -> { ; } ;
		return this;
	}
	
	public List<DagNode> adjacent() {
		if (startingNodes.isEmpty())
			return new ArrayList<DagNode>();
		
		HashSet<DagNode> endingNodes = new HashSet<>();
		
		 startingNodes.forEach( startNode -> {
			 
		 	endingNodes.addAll(startNode
				.getFromThisNodeConnectors()
				.stream()
				.filter(c -> c.hasRelationship(model.getDefaultRelationshipType()) && filterNodePredicate.test(c.getToNode()) && filterLinkPredicate.test(c.getRelationship(relationshipType)))
				.map(d -> d.getToNode())
				.collect(Collectors.toList()));
		});

		 startingNodes.forEach( startNode -> {
			 
			 	endingNodes.addAll(startNode
					.getToThisNodeConnectors()
					.stream()
					.filter(c -> c.hasRelationship(model.getDefaultRelationshipType()) && filterNodePredicate.test(c.getToNode()) && filterLinkPredicate.test(c.getRelationship(relationshipType)))
					.map(d -> d.getToNode())
					.collect(Collectors.toList()));
			});
 
		 
		if (nodeComparator == null)
			return endingNodes.stream().collect(Collectors.toList());
		else
			return endingNodes.stream().sorted(nodeComparator).collect(Collectors.toList());
		
	}
	
	@Override
	public DagNodeNavigator findAdjacent() {
		
		startingNodes = adjacent()
							.stream()
							.map(n -> (DagNodeImpl)n)
							.collect(Collectors.toList());
		
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
				.filter(c -> c.hasRelationship(relationshipType) && filterNodePredicate.test(c.getToNode()) && filterLinkPredicate.test(c.getRelationship(relationshipType)))
				.map(d -> d.getToNode())
				.collect(Collectors.toList()));
		});
		
		if (nodeComparator == null)
			return endingNodes;
		else
			return endingNodes.stream().sorted(nodeComparator).collect(Collectors.toList());
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
				.filter(c -> c.hasRelationship(relationshipType) && filterNodePredicate.test(c.getFromNode()) && filterLinkPredicate.test(c.getRelationship(relationshipType)))
				.map(d -> d.getFromNode())
				.collect(Collectors.toList()));
		 });
		if (nodeComparator == null)
			return endingNodes;
		else
			return endingNodes.stream().sorted(nodeComparator).collect(Collectors.toList());
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
				.filter(c -> c.hasRelationship(relationshipType) && filterNodePredicate.test(c.getToNode()) && filterLinkPredicate.test(c.getRelationship(relationshipType)))
				.map(d -> d.getToNode())
				.collect(Collectors.toList()));
		});
		
		if (nodeComparator == null)
			startingNodes = endingNodes;
		else
			startingNodes = endingNodes.stream().sorted(nodeComparator).collect(Collectors.toList());
		
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
				.filter(c -> filterNodePredicate.test(c.getFromNode()) && filterLinkPredicate.test(c.getRelationship(relationshipType)))
				.map(d -> d.getFromNode())
				.collect(Collectors.toList()));
		});
		if (nodeComparator == null)
			startingNodes = endingNodes;
		else
			startingNodes = endingNodes.stream().sorted(nodeComparator).collect(Collectors.toList());
		
		return this;

	}
	

	@Override
	public DagNodeNavigator visitBy(DagRelationshipType linkType, NodeVisitor visitor) {
		
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
	public DagNodeNavigator findAncestors() {
	
		startingNodes = ancestors()
							.stream()
							.map(n -> (DagNodeImpl)n).collect(Collectors.toList());
		return this;
	}

	@Override
	public DagNodeNavigator findDescendants() {
		startingNodes = descendants()
							.stream()
							.map(n -> (DagNodeImpl)n).collect(Collectors.toList());
							
		return this;
	}
	
	
	private LinkRouteFinder newLinkRouteFinder() {
			return model.createDagLinkRouteFinder(
					relationshipType,
				context,
				nodeVisitor,
				endPredicate,
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
		
		if (startingNodes.isEmpty() )
			throw new DagGraphException("No startingNodes specified");
		
		LinkRouteFinder routeFinder = newLinkRouteFinder();
		
		List<DagNodePath> paths = new ArrayList<>();
			
		for (DagNodeImpl startNode : startingNodes)
			paths.addAll(
					routeFinder.findAllPathsFrom(startNode));
		return paths;
	}


	@Override
	public List<DagNodePath> pathsTo() {
		
		if (startingNodes.isEmpty())
			throw new DagGraphException("No startingNodes specified");
		
		
		LinkRouteFinder routeFinder = newLinkRouteFinder();
		
		List<DagNodePath> paths = new ArrayList<>();
		
		for (DagNodeImpl startNode : startingNodes)
			paths.addAll(
					routeFinder.findAllPathsTo(startNode));
		
		return paths;
	}
	
	
	@Override
	public List<DagNode> ancestors() {
		if (startingNodes.isEmpty())
			throw new DagGraphException("Either a from() or a fromBreadthFirst() is required");
		
		LinkRouteFinder routeFinder = newLinkRouteFinder();
		List<DagNode> nodeList = new ArrayList<>();
		
		for (DagNodeImpl startNode : startingNodes) {
			NodeSearchResult result = routeFinder.discoverToRelationships(startNode);
			LinkedHashSet<DagNode> nodeSet = new LinkedHashSet<DagNode>();
			
			for (DagNodePath path :result.getPaths()) {
				for (DagRelationship link : path.getRelationships()) {
					nodeSet.add(link.getFromNode());
				}
			}
			nodeSet.removeAll(startingNodes);
			nodeList.addAll(nodeSet);
		}
		
		return nodeList;
	}

	
	@Override
	public List<DagNode> descendants() {
		if (startingNodes.isEmpty())
			throw new DagGraphException("Either a from() or a fromBreadthFirst() is required");
		
		LinkRouteFinder routeFinder = newLinkRouteFinder();
		
		List<DagNode> nodeList = new ArrayList<>();
		
		if (traversalDirectionType == TraversalDirectionType.TRAVERSE_DEPTH_FIRST) {
			for (DagNodeImpl  startNode : startingNodes) {
				NodeSearchResult result = routeFinder.discoverFromRelationships(startNode);
				
				LinkedHashSet<DagNode> nodeSet = new LinkedHashSet<DagNode>();
				
				for (DagNodePath path :result.getPaths()) {
					nodeSet.add(path.getStartNode());
					nodeSet.add(path.getEndNode());
				}
				nodeSet.removeAll(startingNodes);
				nodeList.addAll(nodeSet);
			}
			
		} else {
			
			for (DagNodeImpl  startNode : startingNodes) {
				nodeList.addAll(
						routeFinder.findDescendantsBreadthFirst(startNode));
			}
			
		}
		if (nodeComparator == null)
			return nodeList;
		else
			return nodeList.stream().sorted(nodeComparator).collect(Collectors.toList());
		
	}
	
	public DagNodeNavigator findShortestPaths(DagNode toNode) {

		ShortestPathFinder finder = model.createShortestPathFinder(relationshipType);

		Set<DagNode> uniqueNodes = new HashSet<DagNode>();
		
		for (DagNode startingNode : startingNodes) {
			List<DagNode> nodes = finder.findShortestPath(startingNode, toNode);
			uniqueNodes.addAll(nodes);
		}
		
		startingNodes = uniqueNodes
							.stream()
							.map(n -> (DagNodeImpl)n)
							.collect(Collectors.toList());
		
		return this;
	}
	
	public List<DagNodePath> shortestPath(DagNode toNode) {
		ArrayList<DagNodePath> paths = new ArrayList<DagNodePath>();
		ShortestPathFinder finder = model.createShortestPathFinder(relationshipType);
		
		for (DagNode startingNode : startingNodes) {
			paths.add(finder.findShortestRoute(startingNode, toNode));
		}
		return paths;
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
