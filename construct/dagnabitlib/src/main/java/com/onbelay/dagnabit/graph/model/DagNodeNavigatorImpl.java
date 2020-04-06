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
package com.onbelay.dagnabit.graph.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DagNodeNavigatorImpl implements DagNodeNavigator {

	private DagNode fromNode;
	private DagLinkType linkType;
	private DagNodeType nodeType;
	private DagNode toNode;
	
	private DagModel model;
	
	
	
	public DagNodeNavigatorImpl(DagModel model) {
		super();
		this.model = model;
	}

	@Override
	public DagNodeNavigator from(DagNode fromNode) {
		this.fromNode = fromNode;
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
	public DagNodeNavigator to(DagNode toNode) {
		this.toNode = toNode;
		return this;
	}

	@Override
	public List<DagNode> adjacent() {
		
		if (fromNode == null)
			return new ArrayList<DagNode>();
		
		return fromNode
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
		
		if (fromNode == null)
			throw new DagGraphException("fromNode is required");
		
		LinkRouteFinder routeFinder = model.createDagLinkRouteFinder(
				nodeType,
				linkType);
		
		if (toNode != null)
			return routeFinder.findPaths(fromNode, toNode);
		else
			return routeFinder.findAllPaths(fromNode);
	}

}
