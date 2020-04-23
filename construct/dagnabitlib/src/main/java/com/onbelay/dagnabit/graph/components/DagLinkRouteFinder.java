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

import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodeConnector;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.DagNodeSearchResult;
import com.onbelay.dagnabit.graph.model.DagNodeType;
import com.onbelay.dagnabit.graph.model.DagNodeVector;
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

	private DagModel model;

	private DagLinkType linkType;
	
	private DagNodeType nodeType;
	
	private NavigationResult navigationResult;
	

	/**
	 * Create a DagLinkRouteFinder for a given model with optional nodeType and linkType
	 * @param model - required. DagModel to navigate
	 * @param dagNodeType - optional. restricts the toNode to navigate to.
	 * @param dagLinkType - optional. restricts the link to navigate to.
	 */
	public DagLinkRouteFinder(
			DagModel model,
			DagNodeType dagNodeType,
			DagLinkType dagLinkTypeIn) {
		
		super();
		this.model = model;
		this.nodeType = dagNodeType;
		this.linkType = dagLinkTypeIn;
		initialize();
	}

	protected void initialize() {
		navigationResult = discoverToRelationships();
	}

	public Map<DagNode, DagPathRoutes> findAllRoutesFrom(DagNode fromNode) {
		Map<DagNode, DagPathRoutes> routeMap = new HashMap<>();
		
		NodeSearchResult result = discoverFromRelationships(fromNode);
		
		if (result.getVectors().size() > 0) {
			for (DagNodeVector vector : result.getVectors()) {
				for (DagNodePath path : vector.createPaths()) {
					DagPathRoutes pathRoutes = routeMap.get(path.getToNode());
					
					if (pathRoutes == null) {
						pathRoutes = new DagPathRoutes(fromNode, path.getToNode());
						routeMap.put(path.getToNode(), pathRoutes);
					}
					
					pathRoutes.addPath(path);
				}
			}
		}
		return routeMap;
	}
	
	public List<DagNodePath> findAllPaths(DagNode fromNode) {
		List<DagNodePath> paths = new ArrayList<>();
		
		NodeSearchResult result = discoverFromRelationships(fromNode);
		
		if (result.getVectors().size() > 0) {
			for (DagNodeVector vector : result.getVectors()) {
				for (DagNodePath path : vector.createPaths()) {
					
					paths.add(path);
				}
			}
		}
		return paths;
	}
	
	public List<DagNodePath> findPaths(DagNode fromNode, DagNode toNode) {
		List<DagNodePath> paths = new ArrayList<>();
		
		NodeSearchResult result = discoverFromRelationships(fromNode);
		
		if (result.getVectors().size() > 0) {
			for (DagNodeVector vector : result.getVectors()) {
				for (DagNodePath path : vector.createPaths()) {
					
					if (path.getToNode().equals(toNode))
						paths.add(path);
				}
			}
		}
		return paths;
	}


	public DagPathRoutes findRoutes(DagNode fromNode, DagNode toNode) {
		
		NodeSearchResult result = discoverFromRelationships(fromNode);
		DagPathRoutes pathRoutes = new DagPathRoutes(fromNode, toNode);
		
		if (result.getVectors().size() > 0) {
			for (DagNodeVector vector : result.getVectors()) {
				for (DagNodePath path : vector.createPaths()) {
					
					if (path.getToNode().equals(toNode))
						pathRoutes.addPath(path);
				}
			}
		}
		return pathRoutes;
	}

	public NodeSearchResult discoverFromRelationships(DagNode rootNode) {

		DagNodeSearchResult searchResult = new DagNodeSearchResult(
				nodeType,
				linkType, 
				rootNode);

		followFromRelationship(searchResult);

		if (searchResult.isCyclic()) {
			searchResult.setBackChainedNodeSearchResult(navigationResult.getNodeSearchResult(rootNode));
		}
		
		return searchResult;
	}

	public NavigationResult discoverToRelationships() {

		NavigationResult navigationResult = new NavigationResult();

		for (DagNode node : model.getNodeMap().values()) {

			if (nodeType != null) {
				if (nodeType.equals(node.getNodeType()) == false)
					continue;
			}
			
			DagNodeSearchResult searchResult = new DagNodeSearchResult(
					null,
					linkType, 
					node);

			followToRelationship(searchResult);
			navigationResult.add(node, searchResult);
		}

		return navigationResult;
	}

	private void followFromRelationship(DagNodeSearchResult searchResult) {
		DagNode currentNode = searchResult.getCurrentNode();
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
				boolean isCyclic = false;
				if (linkType != null) {
					DagLink dagLink = connector.getRelationship(linkType);
					isCyclic = dagLink.isCyclic();
				} else {
					isCyclic = connector.isConnectorIsCyclic();
				}
				
				if (isCyclic) {
					searchResult.addCycle(connector, linkType);
					foundNextLink = false;
					logger.debug("Ignoring cyclic relationship: " + connector.toString());
				} else {
					followFromRelationship(new DagNodeSearchResult(searchResult, connector.getToNode(), connector));
				}
			}
		}
		
		if (foundNextLink == false)
			searchResult.fixCurrentVector();
		

	}


	private void followToRelationship(DagNodeSearchResult searchResult) {
		DagNode currentNode = searchResult.getCurrentNode();
		
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
				
				foundNextLink = true;
				
				if (searchResult.hasVisited(connector.getFromNode().getName())) {
					searchResult.addCycle(connector, linkType);
					foundNextLink = false;
				} else {
					followToRelationship(new DagNodeSearchResult(searchResult, connector.getFromNode(), connector));
				}
			}
		}
		
		if (foundNextLink == false)
			searchResult.fixCurrentVector();
		

	}

}
