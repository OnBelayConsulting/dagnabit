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
package com.onbelay.dagnabit.node.assemblers;

import java.util.ArrayList;
import java.util.List;

import com.onbelay.dagnabit.node.model.GraphNode;
import com.onbelay.dagnabit.node.shared.GraphNodeDirective;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshot;

public class GraphNodeAssembler extends EntityAssembler {
	
	private GraphNodeDirective directive = new GraphNodeDirective();
	
	public GraphNodeAssembler() {
		
	}
	
	
	
	public GraphNodeAssembler(GraphNodeDirective directive) {
		super();
		this.directive = directive;
	}



	public GraphNodeDirective getDirective() {
		return directive;
	}



	public GraphNodeSnapshot assemble(GraphNode node) {
		GraphNodeSnapshot snapshot = new GraphNodeSnapshot();
		setEntityAttributes(node, snapshot);
		snapshot.getDetail().copyFrom(node.getDetail());
		
		if (directive.isIncludeLinks()) {
			includeLinks(node, snapshot);
		}
		
		return snapshot;
	}
	
	private void includeLinks(GraphNode node, GraphNodeSnapshot snapshot) {
//		GraphLinkAssembler linkAssembler = new GraphLinkAssembler();
//		snapshot.setFromGraphLinks(
//				linkAssembler.assembleSummaries( node.fetchFromGraphLinks()));
//		
//		snapshot.setToGraphLinks(
//				linkAssembler.assembleSummaries( node.fetchToGraphLinks()));
	}
	
	public List<GraphNodeSnapshot> assemble(List<GraphNode> nodes) {
		ArrayList<GraphNodeSnapshot> shots = new ArrayList<>();
		for (GraphNode n : nodes) {
			shots.add(assemble(n));
		}
		return shots;
	}

}
