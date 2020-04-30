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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.onbelay.dagnabit.graph.exception.DagGraphException;
import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodeNavigator;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.DagNodeType;
import com.onbelay.dagnabit.graph.model.LinkRouteFinder;
import com.onbelay.dagnabit.graph.model.TraversalDirectionType;

public class DagNodeNavigatorImpl implements DagNodeNavigator {

	private DagNodeImpl startNode;
	private DagLinkType linkType;
	private DagNodeType nodeType;
	private DagNodeImpl endingNode;
	
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
		
		this.startNode = model.getNodeImplementation(fromNodeIn.getName());
		
		return this;
	}

	@Override
	public DagNodeNavigator by(DagLinkType linkType) {
		this.linkType = linkType;
		return this;
	}

	@Override
	public DagNodeNavigator forOnly(DagNodeType nodeType) {
		this.nodeType = nodeType;
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
	public List<DagNode> adjacent() {
		
		if (startNode == null)
			return new ArrayList<DagNode>();
		
		return startNode
				.getFromThisNodeConnectors()
				.stream()
				.filter(createPredicate())
				.map(d -> d.getToNode())
				.collect(Collectors.toList());
		
	}
	
	
	
	private Predicate<DagNodeConnector> createPredicate() {
		
		if (nodeType == null && linkType == null)
			return c -> true;
		
		if (nodeType != null && linkType == null)
			return    c -> c.getToNode().getNodeType().equals(nodeType);
		
		if (nodeType == null && linkType != null)
			return c -> c.hasRelationship(linkType);
			
		return c -> c.getToNode().getNodeType().equals(nodeType) && c.hasRelationship(linkType);	
}

	@Override
	public List<DagNodePath> paths() {
		
		if (startNode == null && endingNode == null)
			throw new DagGraphException("Either a from() or a to() is required");
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				nodeType,
				linkType);
		
		if (traversalDirectionType == TraversalDirectionType.TRAVERSE_STARTING_FROM_ENDING_TO) {
			if (startNode == null)
				throw new DagGraphException("fromNode is missing in a traverse from");
			
			if (endingNode != null)
				return routeFinder.findPathsStartingFromEndingAt(startNode, endingNode);
			else
				return routeFinder.findAllPathsFrom(startNode);
		} else {
			if (startNode != null)
				return routeFinder.findPathsEndingAtStartingFrom(endingNode, startNode);
			else
				return routeFinder.findAllPathsTo(endingNode);
		}
	}

}
