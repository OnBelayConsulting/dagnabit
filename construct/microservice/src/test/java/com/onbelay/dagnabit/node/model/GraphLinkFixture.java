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
package com.onbelay.dagnabit.node.model;

import com.onbelay.dagnabit.node.shared.GraphLinkDetail;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshot;

public class GraphLinkFixture {

	/**
	 * create a link with the default linkType = 'link' and a default name
	 * @param fromNode
	 * @param toNode
	 * @return
	 */
	public static GraphLink createLink(
			GraphNode fromNode,
			GraphNode toNode) {
		
		return GraphLink.create(
				new GraphLinkDetail(), 
				fromNode,
				toNode);
	}

	public static GraphLink createLink(
			String linkType,
			GraphNode fromNode,
			GraphNode toNode) {
		
		GraphLinkDetail detail = new GraphLinkDetail();
		detail.setLinkType(linkType);
		
		return GraphLink.create(
				detail, 
				fromNode,
				toNode);
	}
	

	
	/**
	 * Create a snapshot with a linkType and a default name
	 * @param type
	 * @param fromNodeId
	 * @param toNodeId
	 * @return
	 */
	public static GraphLinkSnapshot createLinkSnapshot(
			String type, 
			Long fromNodeId, 
			Long toNodeId) {
		
		return new GraphLinkSnapshot(
				type,
				fromNodeId,
				toNodeId);
	}
	
}
