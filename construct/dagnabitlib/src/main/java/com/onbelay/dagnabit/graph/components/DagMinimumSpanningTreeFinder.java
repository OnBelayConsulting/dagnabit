package com.onbelay.dagnabit.graph.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onbelay.dagnabit.graph.model.DagRelationship;
import com.onbelay.dagnabit.graph.model.DagRelationshipType;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.MinimumSpanningTreeFinder;

/**
 * Find the minimum spanning tree using Prim's Algorithm.
 * 
 * Optionally filter by toNode
 * Optionally provide a linkType (mstLinkType) that will be used to create subset of the original graph as a DAG with that mstLinkType.
 *
 */
public class DagMinimumSpanningTreeFinder implements MinimumSpanningTreeFinder {
	private static final Logger logger = LoggerFactory.getLogger(DagMinimumSpanningTreeFinder.class);
	
	private DagModelImpl model;
	private DagRelationshipType relationshipType;
	private DagRelationshipType mstRelationshipType;
	
	
	private Predicate<DagNodeConnector> filterConnectorPredicate = c -> true;

	
	protected DagMinimumSpanningTreeFinder(
			DagModelImpl model, 
			DagRelationshipType relationshipType) {
		
		this.model = model;
		this.relationshipType = relationshipType;
	}

	protected DagMinimumSpanningTreeFinder(
			DagModelImpl model, 
			DagRelationshipType relationshipType,
			Predicate<DagNode> filterNodePredicate) {
		
		this.model = model;
		this.relationshipType = relationshipType;
		this.filterConnectorPredicate = filterConnectorPredicate.and(c -> filterNodePredicate.test(c.getToNode()));
	}
	

	
	protected DagMinimumSpanningTreeFinder(
			DagModelImpl model, 
			DagRelationshipType relationshipType,
			DagRelationshipType mstRelationshipType) {
		
		this.model = model;
		this.relationshipType = relationshipType;
		this.mstRelationshipType = mstRelationshipType;
	}

	protected DagMinimumSpanningTreeFinder(
			DagModelImpl model, 
			DagRelationshipType relationshipType,
			DagRelationshipType mstRelationshipType,
			Predicate<DagNode> filterNodePredicate) {
		
		this.model = model;
		this.relationshipType = relationshipType;
		this.mstRelationshipType = mstRelationshipType;
		this.filterConnectorPredicate = filterConnectorPredicate.and(c -> filterNodePredicate.test(c.getToNode()));
	}



	public List<DagRelationship> determineMinimumSpanningTree(DagNode startNodeIn) {
		
		DagNodeImpl startNode = model.getNodeImplementation(startNodeIn.getName());
		
		int totalNodes = model.getNodeMap().size();
		
		Map<DagNode, DagNode> processed = new HashMap<DagNode, DagNode>();
		processed.put(startNode, startNode);
		
		List<DagRelationshipWrapper> links = startNode.getFromThisNodeConnectors()
												.stream()
												.filter(c -> c.hasRelationship(relationshipType))
												.filter(filterConnectorPredicate)
												.map(c -> new DagRelationshipWrapper(c.getRelationship(relationshipType)))
												.collect(Collectors.toList());
		
		if (links.isEmpty()) {
			logger.error("Starting node missing relationship: " + relationshipType);
			return new ArrayList<DagRelationship>();
		}
		
		DagRelationshipWrapper minLink = Collections.min(links, Comparator.comparingInt(c -> c.getRelationship().getWeight()) );
		
		ArrayList<DagRelationship> processedLinks = new ArrayList<DagRelationship>();
		
		minLink.setWasProcessed(true);
		processedLinks.add(minLink.getRelationship());
		
		if (mstRelationshipType != null)
			model.addRelationship(startNodeIn, mstRelationshipType.getName(), minLink.getRelationship().getToNode());
		
		DagNodeImpl currentNode = model.getNodeImplementation(minLink.getRelationship().getToNode().getName());
		processed.put(currentNode, currentNode);
		
		
		followLinks(
				totalNodes, 
				currentNode, 
				processed,
				processedLinks,
				links);
	
		return processedLinks;
	}
	
	
	private void followLinks(
			int totalNodes,
			DagNodeImpl startNode,
			Map<DagNode, DagNode> processed,
			List<DagRelationship> processedLinks,
			List<DagRelationshipWrapper> existingLinks) {
	
		DagNodeImpl currentNode = startNode;
		existingLinks = existingLinks
									.stream()
									.filter(c -> c.isAvailable())
									.collect(Collectors.toList());
		
		List<DagRelationshipWrapper> currentLinks = currentNode.getFromThisNodeConnectors()
				.stream()
				.filter(c -> c.hasRelationship(relationshipType))
				.filter(filterConnectorPredicate)
				.map(c -> new DagRelationshipWrapper(c.getRelationship(relationshipType)))
				.collect(Collectors.toList());

		existingLinks.addAll(currentLinks);
		
		if (existingLinks.size() == 0) {
			logger.debug("emptyList");
			return;
		}
		
		boolean found = false;
		DagRelationshipWrapper minLink = null;
		while (found == false) {
			minLink = Collections.min(existingLinks, Comparator.comparingInt( c -> c.getRelationship().getWeight()) );
			if (minLink == null) {
				found = true;
			} else {	
				if (processed.containsKey(minLink.getRelationship().getToNode())) {
					logger.debug("Ignoring: " + minLink.toString());
					minLink.setWasIgnored(true);
					existingLinks = existingLinks
							.stream()
							.filter(c -> c.isAvailable())
							.collect(Collectors.toList());
				if (existingLinks.isEmpty())
					return;
				} else {
					found = true;
				}
			}
		}
		
		if (minLink == null) {
			return;
		} else {
			logger.debug("processing: " + minLink.toString());
			minLink.setWasProcessed(true);
			processedLinks.add(minLink.getRelationship());
			
			if (mstRelationshipType != null)
				model.addRelationship(minLink.getRelationship().getFromNode(), mstRelationshipType.getName(), minLink.getRelationship().getToNode());
			
			processed.put(minLink.getRelationship().getToNode(), minLink.getRelationship().getToNode());
		}
		
		currentNode = model.getNodeImplementation(minLink.getRelationship().getToNode().getName());
		
		if (processed.size() >= totalNodes)
			return;
		
		followLinks(
				totalNodes, 
				currentNode, 
				processed,
				processedLinks,
				existingLinks);
		

	}
	
}
