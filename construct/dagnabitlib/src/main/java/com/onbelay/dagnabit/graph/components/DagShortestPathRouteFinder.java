package com.onbelay.dagnabit.graph.components;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.ShortestPathFinder;

/**
 * Finds the shortest path or route in a Directed Acyclic Graph with weights using Dijkstra's algorithm.
 * 
 * Note that is algorithm requires weights on the links and assumes a DAG. 
 * Weights must not be negative.
 * @author lefeu
 *
 */
public class DagShortestPathRouteFinder implements ShortestPathFinder {
	private static final Logger logger = LoggerFactory.getLogger(DagShortestPathRouteFinder.class);
	
	private DagModelImpl model;
	private DagLinkType linkType;
	
	private Comparator<DagNodeConnector> sorter;
	
	
	protected DagShortestPathRouteFinder(DagModelImpl model, DagLinkType linkType) {
		sorter = buildSorter();
		this.model = model;
		this.linkType = linkType;
	}


	@Override
	public DagNodePath findShortestRoute(DagNode startNode, DagNode endNode) {

		Map<DagNode, DagNode> parents = findShortestRouteParents(startNode, endNode);

		
		if (parents.containsKey(endNode) == false) {
			logger.error("There is no path from " + startNode.getName() + " to " + endNode.getName());
			return new DagNodePath(startNode, new ArrayList<DagLink>(), endNode);
		}

		ArrayDeque<DagLink> stack = new ArrayDeque<DagLink>();
		
		DagNode currentNode = endNode;
		DagNode parent = parents.get(currentNode);
		while (parent != null) {
			DagLink link = model.getDefaultLink(parent, currentNode);
			stack.push(link);
			currentNode = parent;
			parent = parents.get(currentNode);
		}
		
		return new DagNodePath(
				startNode, 
				stack.stream().collect(Collectors.toList()), 
				endNode);
	}

	@Override
	public List<DagNode> findShortestPath(DagNode startNode, DagNode endNode) {
		
		Map<DagNode, DagNode> parents = findShortestRouteParents(startNode, endNode);
		
		if (parents.containsKey(endNode) == false) {
			logger.error("There is no path from " + startNode.getName() + " to " + endNode.getName());
			return new ArrayList<DagNode>();
		}
		
		ArrayDeque<DagNode> stack = new ArrayDeque<DagNode>();
		stack.push(endNode);
		DagNode parent = parents.get(endNode);
		
		while (parent != null) {
			stack.push(parent);
			parent = parents.get(parent);
		}
		
		return stack.stream().collect(Collectors.toList());
	}
	

	private Map<DagNode, DagNode> findShortestRouteParents(DagNode startNodeIn, DagNode endNode) {
		
		Map<DagNode, DagNode> parents = new HashMap<>();
		Map<DagNode, Integer> costs = new HashMap<>();
		Map<DagNode, DagNode> processed = new HashMap<>();
		
		DagNodeImpl startNode = model.getNodeImplementation(startNodeIn.getName());
		costs.put(startNode, 0);
		parents.put(startNode, null);
		
		if (startNode.hasFromThisNodeConnectors() == false)
			return null;
		
		followLinks(
				startNode,
				endNode,
				processed,
				parents,
				costs);
		
		logger.debug("Start -> Finsh cost: " + costs.get(endNode));
		
		return parents;
	}
	
	
	
	
	
	private void followLinks(
			DagNodeImpl currentNode,
			DagNode endNode,
			Map<DagNode, DagNode> processed,
			Map<DagNode, DagNode> parents,
			Map<DagNode, Integer> costs) {
	
		
		for (DagNodeConnector connector : currentNode.getSortedFromThisNodeConnectors(sorter)) {
			
			int cost = costs.get(currentNode) + connector.getRelationship(linkType).getWeight();
			
			if (costs.containsKey(connector.getToNode())) {
				if (cost < costs.get(connector.getToNode())) {
					costs.put(connector.getToNode(), cost);
					parents.put(connector.getToNode(), currentNode);
				}
			} else {
				costs.put(connector.getToNode(), cost);
				parents.put(connector.getToNode(), currentNode);
			}
			
			if (endNode.equals(connector.getToNode()) == false) {
				
				// Don't process what you have already processed once
				if (processed.containsKey(connector.getToNode()) == false) {
				
					processed.put(connector.getToNode(), connector.getToNode());
					followLinks(
							connector.getToNode(),
							endNode,
							processed,
							parents,
							costs);
				}
			}
		}

		
	}
	
	private Comparator<DagNodeConnector> buildSorter() {
		Comparator<DagNodeConnector> sorter = (c, d) -> {
			int cWeight = c.getRelationship(model.getDefaultLinkType()).getWeight();
			int dWeight = d.getRelationship(model.getDefaultLinkType()).getWeight();
			return cWeight - dWeight;
		};
		return sorter;
	}
	

}
