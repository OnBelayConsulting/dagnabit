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

import java.util.List;
import java.util.stream.Collectors;

import com.onbelay.dagnabit.node.model.GraphLink;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshot;

public class GraphLinkAssembler extends EntityAssembler{

	
	public GraphLinkSnapshot assemble(GraphLink graphLink) {
		GraphLinkSnapshot snapshot = new GraphLinkSnapshot();
		setEntityAttributes(graphLink, snapshot);
		
		snapshot.getDetail().copyFrom(graphLink.getDetail());
		
		snapshot.setFromGraphNodeId(graphLink.getFromGraphNode().getId());
		snapshot.setToGraphNodeId(graphLink.getToGraphNode().getId());
		
		snapshot.setFromGraphNodeName(graphLink.getFromGraphNode().getDetail().getName());
		
		snapshot.setToGraphNodeName(graphLink.getToGraphNode().getDetail().getName());
		
		return snapshot;
	}
	
	public List<GraphLinkSnapshot> assemble(List<GraphLink> links) {
		
		return links
				.stream()
				.map( e -> assemble(e))
				.collect(Collectors.toList());
		
	}
	
	 
}
