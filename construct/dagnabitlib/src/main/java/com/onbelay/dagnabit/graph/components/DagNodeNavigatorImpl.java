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
import java.util.List;
import java.util.function.BiConsumer;
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
import com.onbelay.dagnabit.graph.model.TraversalDirectionType;

public class DagNodeNavigatorImpl implements DagNodeNavigator {

	private List<DagNodeImpl> startingNodes = new ArrayList<DagNodeImpl>();
	private DagLinkType linkType;
	private DagNodeImpl endingNode;
	
	private DagContext context = new DagMapContext();
	
	private Predicate<DagNode> filterNodePredicate = c -> true;

	private Predicate<DagLink> filterLinkPredicate = c -> true;

	private BiConsumer<DagContext, DagNode> nodeVisitor = (c, n) -> { ; } ;

	private BiConsumer<DagContext, DagLink> linkVisitor = (c, n) -> { ; } ;
	
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
	public DagNodeNavigator by(DagLinkType linkType) {
		this.linkType = linkType;
		return this;
	}

	@Override
	public DagNodeNavigator by(Predicate<DagLink> fn) {
		this.filterLinkPredicate = fn;
		return this;
	}

	@Override
	public DagNodeNavigator visitLinkWith(BiConsumer<DagContext, DagLink> linkVisitor) {
		this.linkVisitor = linkVisitor;
		return this;
	}

	@Override
	public DagNodeNavigator visitNodeWith(BiConsumer<DagContext, DagNode> nodeVisitor) {
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
				.filter(c -> filterNodePredicate.test(c.getToNode()) && filterLinkPredicate.test(c.getRelationship(linkType)))
				.map(d -> d.getToNode())
				.collect(Collectors.toList()));
		 });
		return endingNodes;
	}
	
	
	public DagNodeNavigator findChildren() {
		if (startingNodes.isEmpty())
			return this;
		
		ArrayList<DagNodeImpl> endingNodes = new ArrayList<>();
		
		 startingNodes.forEach( startNode -> {
			 
		 	endingNodes.addAll(startNode
				.getFromThisNodeConnectors()
				.stream()
				.filter(c -> filterNodePredicate.test(c.getToNode()) && filterLinkPredicate.test(c.getRelationship(linkType)))
				.map(d -> d.getToNode())
				.collect(Collectors.toList()));
		 });
		startingNodes = endingNodes; 
		return this;

	}
	

	@Override
	public DagNodeNavigator findDescendants() {
		List<DagNodePath> paths = paths();
		
		startingNodes = paths
						.stream()
						.map(p -> (DagNodeImpl)p.getToNode())
						.collect(Collectors.toList());
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

}
