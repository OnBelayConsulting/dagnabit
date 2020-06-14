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

import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagLinkType;
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
	private DagLinkType linkType;
	private DagLinkType mstLinkType;
	
	
	private Predicate<DagNodeConnector> filterConnectorPredicate = c -> true;

	
	protected DagMinimumSpanningTreeFinder(
			DagModelImpl model, 
			DagLinkType linkType) {
		
		this.model = model;
		this.linkType = linkType;
	}

	protected DagMinimumSpanningTreeFinder(
			DagModelImpl model, 
			DagLinkType linkType,
			Predicate<DagNode> filterNodePredicate) {
		
		this.model = model;
		this.linkType = linkType;
		this.filterConnectorPredicate = filterConnectorPredicate.and(c -> filterNodePredicate.test(c.getToNode()));
	}
	

	
	protected DagMinimumSpanningTreeFinder(
			DagModelImpl model, 
			DagLinkType linkType,
			DagLinkType mstLinkType) {
		
		this.model = model;
		this.linkType = linkType;
		this.mstLinkType = mstLinkType;
	}

	protected DagMinimumSpanningTreeFinder(
			DagModelImpl model, 
			DagLinkType linkType,
			DagLinkType mstLinkType,
			Predicate<DagNode> filterNodePredicate) {
		
		this.model = model;
		this.linkType = linkType;
		this.mstLinkType = mstLinkType;
		this.filterConnectorPredicate = filterConnectorPredicate.and(c -> filterNodePredicate.test(c.getToNode()));
	}



	public List<DagLink> determineMinimumSpanningTree(DagNode startNodeIn) {
		
		DagNodeImpl startNode = model.getNodeImplementation(startNodeIn.getName());
		
		int totalNodes = model.getNodeMap().size();
		
		Map<DagNode, DagNode> processed = new HashMap<DagNode, DagNode>();
		processed.put(startNode, startNode);
		
		List<DagLinkWrapper> links = startNode.getFromThisNodeConnectors()
												.stream()
												.filter(c -> c.hasRelationship(linkType))
												.filter(filterConnectorPredicate)
												.map(c -> new DagLinkWrapper(c.getRelationship(linkType)))
												.collect(Collectors.toList());
		
		if (links.isEmpty()) {
			logger.error("Starting node missing relationship: " + linkType);
			return new ArrayList<DagLink>();
		}
		
		DagLinkWrapper minLink = Collections.min(links, Comparator.comparingInt( c -> c.getDagLink().getWeight()) );
		
		ArrayList<DagLink> processedLinks = new ArrayList<DagLink>();
		
		minLink.setWasProcessed(true);
		processedLinks.add(minLink.getDagLink());
		
		if (mstLinkType != null)
			model.addRelationship(startNodeIn, mstLinkType.getName(), minLink.getDagLink().getToNode());
		
		DagNodeImpl currentNode = model.getNodeImplementation(minLink.getDagLink().getToNode().getName());
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
			List<DagLink> processedLinks,
			List<DagLinkWrapper> existingLinks) {
	
		DagNodeImpl currentNode = startNode;
		existingLinks = existingLinks
									.stream()
									.filter(c -> c.isAvailable())
									.collect(Collectors.toList());
		
		List<DagLinkWrapper> currentLinks = currentNode.getFromThisNodeConnectors()
				.stream()
				.filter(c -> c.hasRelationship(linkType))
				.filter(filterConnectorPredicate)
				.map(c -> new DagLinkWrapper(c.getRelationship(linkType)))
				.collect(Collectors.toList());

		existingLinks.addAll(currentLinks);
		
		if (existingLinks.size() == 0) {
			logger.debug("emptyList");
			return;
		}
		
		boolean found = false;
		DagLinkWrapper minLink = null;
		while (found == false) {
			minLink = Collections.min(existingLinks, Comparator.comparingInt( c -> c.getDagLink().getWeight()) );
			if (minLink == null) {
				found = true;
			} else {	
				if (processed.containsKey(minLink.getDagLink().getToNode())) {
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
			processedLinks.add(minLink.getDagLink());
			
			if (mstLinkType != null)
				model.addRelationship(minLink.getDagLink().getFromNode(), mstLinkType.getName(), minLink.getDagLink().getToNode());
			
			processed.put(minLink.getDagLink().getToNode(), minLink.getDagLink().getToNode());
		}
		
		currentNode = model.getNodeImplementation(minLink.getDagLink().getToNode().getName());
		
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
