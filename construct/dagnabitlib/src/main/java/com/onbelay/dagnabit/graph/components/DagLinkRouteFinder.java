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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.onbelay.dagnabit.graph.exception.DagGraphException;
import com.onbelay.dagnabit.graph.model.DagContext;
import com.onbelay.dagnabit.graph.model.DagRelationship;
import com.onbelay.dagnabit.graph.model.DagRelationshipType;
import com.onbelay.dagnabit.graph.model.DagMapContext;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.DagNodeCategory;
import com.onbelay.dagnabit.graph.model.DagPathRoutes;
import com.onbelay.dagnabit.graph.model.LinkRouteFinder;
import com.onbelay.dagnabit.graph.model.NodeSearchResult;
import com.onbelay.dagnabit.graph.model.NodeVisitor;

/**
 * The DagLinkRouteFinder finds routes between nodes.
 * Its usually used to find paths from a fromNode to one or more nodes based on:
 * <ul>
 * <li> nodeType - all if not specified. Stops path traversal at the first "to" node.
 * <li> linkType - all if not specified 
 * <li> toNode - all if not specified.
 * </ul>
 * 
 * @author lefeu
 *
 */
public class DagLinkRouteFinder implements LinkRouteFinder {
	private static final Logger logger = LoggerFactory.getLogger(DagLinkRouteFinder.class);

	private DagModelImpl model;

	private DagRelationshipType relationshipType;
	
	private Predicate<DagRelationship> filterLinkPredicate = c -> true;
	
	private Predicate<DagNode> filterNodePredicate = c -> true;
	
	private BiPredicate<DagContext, DagNode> endPredicate;
	
	private NodeVisitor nodeVisitor = (c, s, l, e) -> { ; };

	private boolean noBacktracking = true;
	
	private DagContext context = new DagMapContext();
	
	protected DagLinkRouteFinder(
			DagModelImpl model,
			DagRelationshipType relationshipType) {
		
		this.model = model;
		if (relationshipType == null)
			throw new DagGraphException("Link is required");

		this.relationshipType = relationshipType;
	}
	
	
	protected DagLinkRouteFinder(
			DagModelImpl model,
			DagRelationshipType relationshipType,
			DagContext context,
			NodeVisitor nodeVisitor) {
		
		this.model = model;
		if (relationshipType == null)
			throw new DagGraphException("Link is required");

		this.relationshipType = relationshipType;
		if (context != null)
			this.context = context;
		
		this.nodeVisitor = nodeVisitor;
	}

	protected DagLinkRouteFinder(
			DagModelImpl model,
			DagRelationshipType relationshipType,
			Predicate<DagNode> filterNodePredicate) {
		super();
		this.model = model;

		if (relationshipType == null)
			throw new DagGraphException("Link is required");

		this.relationshipType = relationshipType;
		this.filterNodePredicate = filterNodePredicate;
	}


	protected DagLinkRouteFinder(
			DagModelImpl model,
			DagRelationshipType relationshipType,
			DagContext context,
			NodeVisitor nodeVisitor,
			BiPredicate<DagContext, DagNode> endPredicate,
			Predicate<DagRelationship> filterLinkPredicate,
			Predicate<DagNode> filterNodePredicate) {
		super();
		this.model = model;

		if (relationshipType == null)
			throw new DagGraphException("Link is required");

		
		this.endPredicate = endPredicate;
		this.relationshipType = relationshipType;
		this.filterLinkPredicate = filterLinkPredicate;
		this.filterNodePredicate = filterNodePredicate;
		if (context != null)
			this.context = context;
		
		this.nodeVisitor = nodeVisitor;
	}


	

	/**
	 * Create a RouteFinder with two predicates
	 * @param model - required
	 * @param relationshipType
	 * @param filterLinkPredicate - required. return true to navigate as default
	 * @param filterNodePredicate - required, return true to navigate as default
	 */
	protected DagLinkRouteFinder(
			DagModelImpl model,
			DagRelationshipType relationshipType,
			Predicate<DagRelationship> filterLinkPredicate,
			Predicate<DagNode> filterNodePredicate) {
		super();
		this.model = model;

		if (relationshipType == null)
			throw new DagGraphException("Link is required");

		this.relationshipType = relationshipType;
		this.filterLinkPredicate = filterLinkPredicate;
		this.filterNodePredicate = filterNodePredicate;
	}



	/**
	 * Create a DagLinkRouteFinder for a given model
	 * Create predicates for filtering based on the optional nodeType and linkType
	 * @param model - required. DagModel to navigate
	 * @param relationshipType - required. restricts the link to navigate to.
	 * @param dagNodeCategory - required. restricts the toNode to navigate to.
	 */
	protected DagLinkRouteFinder(
			DagModelImpl model,
			DagRelationshipType relationshipType,
			DagNodeCategory dagNodeCategory) {
		
		super();
		this.model = model;
		
		if (dagNodeCategory != null) {
			this.filterNodePredicate = c ->  c.getCategory().equals(dagNodeCategory);
		}
		
		if (relationshipType == null)
			throw new DagGraphException("Link is required");
		
		this.relationshipType = relationshipType;
		
		initialize();
	}

	protected void initialize() {
	}

	@Override
	public Map<DagNode, DagPathRoutes> findAllRoutesFrom(DagNode fromNode) {
		Map<DagNode, DagPathRoutes> routeMap = new HashMap<>();
		
		NodeSearchResult result = discoverFromRelationships(fromNode);
		
		for (DagNodePath path : result.getPaths()) {
			DagPathRoutes pathRoutes = routeMap.get(path.getEndNode());
					
			if (pathRoutes == null) {
				pathRoutes = new DagPathRoutes(fromNode, path.getEndNode());
				routeMap.put(path.getEndNode(), pathRoutes);
			}
					
			pathRoutes.addPath(path);
		}
		
		return routeMap;
	}
	
	@Override
	public List<DagNodePath> findAllPathsFrom(DagNode startNode) {
		
		NodeSearchResult result = discoverFromRelationships(startNode);
		
		return result.getPaths();
	}
	
	@Override
	public List<DagNodePath> findAllPathsTo(DagNode startingNode) {
		
		NodeSearchResult navResult = discoverToRelationships(startingNode);
		return navResult.getPaths();	
	}
	
	@Override
	public List<DagNodePath> findPathsStartingFromEndingAt(DagNode startNode, DagNode endNode) {
		
		NodeSearchResult result = discoverFromRelationships(startNode);
		ArrayList<DagNodePath> paths = new ArrayList<DagNodePath>();
		
		for (DagNodePath path : result.getPaths()) {
			
			if (path.getEndNode().equals(endNode))
				paths.add(path);
		}
		
		return paths;
	}

	@Override
	public List<DagNodePath> findPathsEndingAtStartingFrom(DagNode endingNode, DagNode startingNode) {
		
		DagNodeSearchResult result = discoverToRelationships(startingNode, endingNode);
		return result.getPaths();
	}

	@Override
	public DagPathRoutes findRoutes(DagNode fromNode, DagNode toNode) {
		
		NodeSearchResult result = discoverFromRelationships(fromNode);
		DagPathRoutes pathRoutes = new DagPathRoutes(fromNode, toNode);
		
		for (DagNodePath path : result.getPaths()) {
				
			if (path.getEndNode().equals(toNode))
				pathRoutes.addPath(path);
		}
		
		return pathRoutes;
	}
	
	@Override
	public List<DagNode> findDescendants(DagNode startNode) {
		DagNodeImpl rootNode = model.getNodeImplementation(startNode.getName());
		
		DagNodeSearchState searchState = new DagNodeSearchState(
				relationshipType,
				rootNode);

		followFromRelationship(searchState);

		HashSet<DagNode> nodes = new HashSet<DagNode>();
		
		for (DagNodeVector v : searchState.getVectors()) {
			for (DagNodeConnector c : v.getConnectors()) {
				nodes.add(c.getToNode());
			}
		}
		
		return nodes.stream().collect(Collectors.toList());
	}

	@Override
	public NodeSearchResult discoverFromRelationships(DagNode rootNodeIn) {

		DagNodeImpl rootNode = model.getNodeImplementation(rootNodeIn.getName());
		
		DagNodeSearchState searchState = new DagNodeSearchState(
				relationshipType,
				rootNode);

		followFromRelationship(searchState);
		
		DagNodeSearchResult searchResult = new DagNodeSearchResult(relationshipType, rootNode);

		for (DagNodeVector v : searchState.getVectors()) {
			searchResult.addPaths(v.createFromPaths());
		}

		
		if (searchState.isCyclic()) {
			
			for (DagNodeVector c : searchState.getCycles()) {
				searchResult.addCycle(c.createPath());
			}
		}
		
		return searchResult;
	}


	/**
	 * Find all the descendants (traversing from the "from" relationship) using a breadth first search.
	 * @param rootNodeIn
	 * @return a 
	 */
	@Override
	public List<DagNode> findDescendantsBreadthFirst(DagNode rootNodeIn) {

		DagNodeImpl rootNode = model.getNodeImplementation(rootNodeIn.getName());
		
		DagNodeSearchState searchState = new DagNodeSearchState(
				relationshipType,
				rootNode);

		ArrayList<DagNodeImpl> startingNodes = new ArrayList<DagNodeImpl>();
		startingNodes.add(rootNode);
		moveFromRelationship(startingNodes, searchState);
		
		DagNodeVector v = searchState.getVector();
		return v.fetchDagNodesBreadthFirst(rootNodeIn);
	}

	@Override
	public DagNodeSearchResult discoverToRelationships(DagNode startingNode) {


		DagNodeSearchState searchState = new DagNodeSearchState(
				relationshipType,
				model.getNodeImplementation(startingNode.getName()));

		followToRelationship(searchState);

		DagNodeSearchResult searchResult = new DagNodeSearchResult(relationshipType, startingNode);
		
		for (DagNodeVector c : searchState.getCycles()) {
			searchResult.addCycle(c.createPath());
		}
		
		for (DagNodeVector v : searchState.getVectors()) {
			searchResult.addPaths(v.createToPaths());
		}
		
		

		return searchResult;
	}

	
	@Override
	public DagNodeSearchResult discoverToRelationships(DagNode startingNode, DagNode endingNode) {


		DagNodeSearchResult searchResult = new DagNodeSearchResult(relationshipType, startingNode);


		if (endingNode.getName().equals(startingNode.getName()))
			return searchResult;
		
		if (filterNodePredicate.test(startingNode) == false) {
			return searchResult;
		}
		
		DagNodeSearchState searchState = new DagNodeSearchState(
				relationshipType,
				model.getNodeImplementation(startingNode.getName()),
				endingNode);

		followToRelationship(searchState);
		
		for (DagNodeVector c : searchState.getCycles()) {
			searchResult.addCycle(c.createPath());
		}
		
		for (DagNodeVector v : searchState.getVectors()) {
			searchResult.addPaths(v.createToPaths());
		}
			

		return searchResult;
	}

	private void followFromRelationship(DagNodeSearchState searchState) {
		DagNodeImpl currentNode = searchState.getCurrentNode();
		boolean foundNextLink = false;
		
		if (searchState.isHalting())
			return;
		
		if (currentNode.hasFromThisNodeConnectors()) {

			for (DagNodeConnector connector : currentNode.getFromThisNodeConnectors()) {
				
				if (connector.hasRelationship(relationshipType) == false)
					continue;
				
				if (filterLinkPredicate.test(connector.getRelationship(relationshipType)) == false)
					continue;
				
				
				if (filterNodePredicate.test(connector.getToNode()) == false) {
					continue;
				}
				
				if (noBacktracking) {
					if (searchState.hasPreviousNode()) {
						if (searchState.getPreviousNode().equals(connector.getToNode()))
							continue;
					}
				}
				
				nodeVisitor.accept(context, connector.getFromNode(), connector.getRelationship(relationshipType), connector.getToNode());
				
				foundNextLink = true;
				
				if (endPredicate != null) {
					
					if (endPredicate.test(context, connector.getToNode())) {
						foundNextLink = false;
						searchState.setHalting(true);
						searchState.addNodeRelationshipLink(connector);
						searchState.fixCurrentVector();
						return;
					}
					
				}
					
				
				if (searchState.hasVisited(connector.getToNode().getName())) {
					searchState.addCycle(searchState.getVector(), connector, relationshipType);
					foundNextLink = false;
				} else {
					followFromRelationship(new DagNodeSearchState(searchState, connector.getToNode(), connector));
				}
			}
		}
		
		if (foundNextLink == false)
			searchState.fixCurrentVector();
		

	}

	private void moveFromRelationship(List<DagNodeImpl> startingNodes, DagNodeSearchState searchState) {
		
		
		List<DagNodeImpl> endingNodes = new ArrayList<DagNodeImpl>();
		
		if (searchState.isHalting())
			return;
	
		
		for (DagNodeImpl currentNode : startingNodes) {
			
			if (currentNode.hasFromThisNodeConnectors()) {
	
				for (DagNodeConnector connector : currentNode.getFromThisNodeConnectors()) {
					
					if (connector.hasRelationship(relationshipType) == false)
						continue;
					
					if (filterLinkPredicate.test(connector.getRelationship(relationshipType)) == false)
						continue;
					
					if (filterNodePredicate.test(connector.getToNode()) == false) {
						continue;
					}
					
					nodeVisitor.accept(context, connector.getFromNode(), connector.getRelationship(relationshipType), connector.getToNode());
					
					searchState.addNodeRelationshipLink(connector);
					
					if (endPredicate != null) {
						
						if (endPredicate.test(context, connector.getToNode())) {
							searchState.setHalting(true);
							searchState.addNodeRelationshipLink(connector);
							return;
						}
							
						
					}
					
					if (searchState.hasVisited(connector.getToNode().getName())) {
						searchState.addCycle(searchState.getVector(), connector, relationshipType);
					} else {
						endingNodes.add(connector.getToNode());
					}
				}
			}
		}
		
		if ( (searchState.isHalting() == false) && (endingNodes.isEmpty() == false) )
			moveFromRelationship(endingNodes, searchState);

	}


	private void followToRelationship(DagNodeSearchState searchState) {
		DagNodeImpl currentNode = searchState.getCurrentNode();
		
		boolean foundNextLink = false;
		
		if (searchState.isHalting())
			return;
		
		if (currentNode.hasToThisNodeConnectors()) {

			for (DagNodeConnector connector : currentNode.getToThisNodeConnectors()) {
				
				if (connector.hasRelationship(relationshipType) == false)
					continue;
				
				if (filterLinkPredicate.test(connector.getRelationship(relationshipType)) == false)
					continue;
				
				if (filterNodePredicate.test(connector.getFromNode()) == false) {
					continue;
				}
				
				nodeVisitor.accept(context, connector.getFromNode(), connector.getRelationship(relationshipType), connector.getToNode());
				
				foundNextLink = true;
				
				if (endPredicate != null) {
					
					if (endPredicate.test(context, connector.getFromNode())) {
						foundNextLink = false;
						searchState.setHalting(true);
						searchState.addNodeRelationshipLink(connector);
						searchState.fixCurrentVector();
						return;
					}
						
					
				}
				
				if (searchState.hasVisited(connector.getFromNode().getName())) {
					searchState.addCycle(searchState.getVector(), connector, relationshipType);
					foundNextLink = false;
				} else {
					followToRelationship(new DagNodeSearchState(searchState, connector.getFromNode(), connector));
				}
			}
		}
		
		if (foundNextLink == false)
			searchState.fixCurrentVector();
		

	}


	public boolean isNoBacktracking() {
		return noBacktracking;
	}


	public void setNoBacktracking(boolean noBacktracking) {
		this.noBacktracking = noBacktracking;
	}

}
