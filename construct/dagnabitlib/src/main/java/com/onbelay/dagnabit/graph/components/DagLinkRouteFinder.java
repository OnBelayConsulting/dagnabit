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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.DagNodeType;
import com.onbelay.dagnabit.graph.model.DagPathRoutes;
import com.onbelay.dagnabit.graph.model.LinkRouteFinder;
import com.onbelay.dagnabit.graph.model.NavigationResult;
import com.onbelay.dagnabit.graph.model.NodeSearchResult;

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
	private static final Logger logger = LogManager.getLogger(DagLinkRouteFinder.class);

	private DagModelImpl model;

	private DagLinkType linkType;
	
	private DagNodeType nodeType;
	

	/**
	 * Create a DagLinkRouteFinder for a given model with optional nodeType and linkType
	 * @param model - required. DagModel to navigate
	 * @param dagNodeType - optional. restricts the toNode to navigate to.
	 * @param dagLinkType - optional. restricts the link to navigate to.
	 */
	protected DagLinkRouteFinder(
			DagModelImpl model,
			DagNodeType dagNodeType,
			DagLinkType dagLinkTypeIn) {
		
		super();
		this.model = model;
		this.nodeType = dagNodeType;
		this.linkType = dagLinkTypeIn;
		initialize();
	}

	protected void initialize() {
	}

	@Override
	public Map<DagNode, DagPathRoutes> findAllRoutesFrom(DagNode fromNode) {
		Map<DagNode, DagPathRoutes> routeMap = new HashMap<>();
		
		NodeSearchResult result = discoverFromRelationships(fromNode);
		
		for (DagNodePath path : result.getPaths()) {
			DagPathRoutes pathRoutes = routeMap.get(path.getToNode());
					
			if (pathRoutes == null) {
				pathRoutes = new DagPathRoutes(fromNode, path.getToNode());
				routeMap.put(path.getToNode(), pathRoutes);
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
	public List<DagNodePath> findAllPathsTo(DagNode endingNode) {
		ArrayList<DagNodePath> paths = new ArrayList<DagNodePath>();
		NavigationResult navResult = discoverToRelationships(endingNode);
		for (NodeSearchResult s : navResult.getNodeSearchResults().values()) {
			paths.addAll(s.getPaths());
		}
		return paths;
	}
	
	@Override
	public List<DagNodePath> findPathsStartingFromEndingAt(DagNode startNode, DagNode endNode) {
		
		NodeSearchResult result = discoverFromRelationships(startNode);
		ArrayList<DagNodePath> paths = new ArrayList<DagNodePath>();
		
		for (DagNodePath path : result.getPaths()) {
			
			if (path.getToNode().equals(endNode))
				paths.add(path);
		}
		
		return paths;
	}

	@Override
	public List<DagNodePath> findPathsEndingAtStartingFrom(DagNode endingNode, DagNode startingNode) {
		
		DagNodeSearchResult result = discoverToRelationships(startingNode, endingNode);
		return result.getPaths();
	}


	public DagPathRoutes findRoutes(DagNode fromNode, DagNode toNode) {
		
		NodeSearchResult result = discoverFromRelationships(fromNode);
		DagPathRoutes pathRoutes = new DagPathRoutes(fromNode, toNode);
		
		for (DagNodePath path : result.getPaths()) {
				
			if (path.getToNode().equals(toNode))
				pathRoutes.addPath(path);
		}
		
		return pathRoutes;
	}

	public NodeSearchResult discoverFromRelationships(DagNode rootNodeIn) {

		DagNodeImpl rootNode = model.getNodeImplementation(rootNodeIn.getName());
		
		DagNodeSearchState searchState = new DagNodeSearchState(
				nodeType,
				linkType, 
				rootNode);

		followFromRelationship(searchState);
		
		DagNodeSearchResult searchResult = new DagNodeSearchResult(linkType, rootNode);

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

	public NavigationResult discoverToRelationships(DagNode endingNode) {

		NavigationResult navigationResult = new NavigationResult();

		for (DagNodeImpl node : model.getNodeMap().values()) {

			if (endingNode.getName().equals(node.getName()))
				continue;
			
			if (nodeType != null) {
				if (nodeType.equals(node.getNodeType()) == false)
					continue;
			}
			
			DagNodeSearchState searchState = new DagNodeSearchState(
					null,
					linkType, 
					node,
					endingNode);

			followToRelationship(searchState);

			DagNodeSearchResult searchResult = new DagNodeSearchResult(linkType, node);
			
			for (DagNodeVector c : searchState.getCycles()) {
				searchResult.addCycle(c.createPath());
			}
			
			for (DagNodeVector v : searchState.getVectors()) {
				searchResult.addPaths(v.createToPaths());
			}
			
			
			navigationResult.add(node, searchResult);
		}

		return navigationResult;
	}
	
	@Override
	public DagNodeSearchResult discoverToRelationships(DagNode startingNode, DagNode endingNode) {


		DagNodeSearchResult searchResult = new DagNodeSearchResult(linkType, startingNode);


		if (endingNode.getName().equals(startingNode.getName()))
			return searchResult;
		
		if (nodeType != null) {
			if (nodeType.equals(startingNode.getNodeType()) == false)
				return searchResult;
		}
		
		DagNodeSearchState searchState = new DagNodeSearchState(
				null,
				linkType, 
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
		
		if (currentNode.hasFromThisNodeConnectors()) {

			for (DagNodeConnector connector : currentNode.getFromThisNodeConnectors()) {
				
				if (linkType != null) {
					if (connector.hasRelationship(linkType) == false)
						continue;
				}
				
				// if nodeType is set then don't navigate unless it is equal to.
				if (nodeType != null) {
					if (connector.getToNode().getNodeType().equals(nodeType) == false)
						continue;
				}

				
				foundNextLink = true;
				
				if (searchState.hasVisited(connector.getToNode().getName())) {
					searchState.addCycle(searchState.getVector(), connector, linkType);
					foundNextLink = false;
				} else {
					followFromRelationship(new DagNodeSearchState(searchState, connector.getToNode(), connector));
				}
			}
		}
		
		if (foundNextLink == false)
			searchState.fixCurrentVector();
		

	}


	private void followToRelationship(DagNodeSearchState searchState) {
		DagNodeImpl currentNode = searchState.getCurrentNode();
		
		boolean foundNextLink = false;
		
		if (currentNode.hasToThisNodeConnectors()) {

			for (DagNodeConnector connector : currentNode.getToThisNodeConnectors()) {

				if (linkType != null) {
					if (connector.hasRelationship(linkType) == false)
					continue;
				}
				
				// if nodeType is set then don't navigate unless it is equal to.
				if (nodeType != null) {
					if (connector.getToNode().getNodeType().equals(nodeType) == false)
						continue;
				}
				
				if (connector.getFromNode().getName().equals(searchState.getEndingNode().getName())) {
					searchState.fixCurrentVector(connector);
					return;
				}
				
				foundNextLink = true;
				
				if (searchState.hasVisited(connector.getFromNode().getName())) {
					searchState.addCycle(searchState.getVector(), connector, linkType);
					foundNextLink = false;
				} else {
					followToRelationship(new DagNodeSearchState(searchState, connector.getFromNode(), connector));
				}
			}
		}
		
		if (foundNextLink == false)
			searchState.fixCurrentVector();
		

	}

}
