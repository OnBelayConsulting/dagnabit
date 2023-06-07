package com.onbelay.dagnabit.dagmodel.components;

import com.onbelay.dagnabit.dagmodel.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

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
	private DagRelationshipType relationshipType;
	
	private Comparator<DagNodeConnector> sorter;
	
	
	protected DagShortestPathRouteFinder(DagModelImpl model, DagRelationshipType relationshipType) {
		sorter = buildSorter();
		this.model = model;
		this.relationshipType = relationshipType;
	}


	@Override
	public DagNodePath findShortestRoute(DagNode startNode, DagNode endNode) {

		Map<DagNode, DagNode> parents = findShortestRouteParents(startNode, endNode);

		
		if (parents.containsKey(endNode) == false) {
			logger.error("There is no path from " + startNode.getName() + " to " + endNode.getName());
			return new DagNodePath(startNode, new ArrayList<DagRelationship>(), endNode);
		}

		ArrayDeque<DagRelationship> stack = new ArrayDeque<DagRelationship>();
		
		DagNode currentNode = endNode;
		DagNode parent = parents.get(currentNode);
		while (parent != null) {
			DagRelationship link = model.getDefaultRelationship(parent, currentNode);
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
			
			int cost = costs.get(currentNode) + connector.getRelationship(relationshipType).getWeight();
			
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
			int cWeight = c.getRelationship(model.getDefaultRelationshipType()).getWeight();
			int dWeight = d.getRelationship(model.getDefaultRelationshipType()).getWeight();
			return cWeight - dWeight;
		};
		return sorter;
	}
	

}
