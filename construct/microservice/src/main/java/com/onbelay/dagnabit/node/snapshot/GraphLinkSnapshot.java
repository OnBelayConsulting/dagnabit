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
package com.onbelay.dagnabit.node.snapshot;

import com.onbelay.dagnabit.node.shared.GraphLinkDetail;

/**
 * Create a directed link (edge) from one node to another.
 * The to and from graphNode references may be by referenced either by entity id or by name. 
 * If both id and name are present then id will be used.
 * 
 * @author lefeu
 *
 */
public class GraphLinkSnapshot extends AbstractSnapshot {
	private GraphLinkDetail detail = new GraphLinkDetail();
	
	private Long toGraphNodeId;
	private Long fromGraphNodeId;
	
	private String toGraphNodeName;
	private String fromGraphNodeName;
	
	public GraphLinkSnapshot() {
		
	}
	
	public GraphLinkSnapshot(
			String type, 
			Long fromNodeId, 
			Long toNodeId) {
		
		detail.setLinkType(type);
		detail.setLinkType(type);
		this.fromGraphNodeId = fromNodeId;
		this.toGraphNodeId = toNodeId;
		
	}
	
	public GraphLinkSnapshot(Long entityKey) {
		super(entityKey);
	}

	public GraphLinkDetail getDetail() {
		return detail;
	}

	public void setDetail(GraphLinkDetail graphLinkDetail) {
		this.detail = graphLinkDetail;
	}

	public Long getToGraphNodeId() {
		return toGraphNodeId;
	}

	public void setToGraphNodeId(Long toGraphNodeKey) {
		this.toGraphNodeId = toGraphNodeKey;
	}

	public Long getFromGraphNodeId() {
		return fromGraphNodeId;
	}

	public void setFromGraphNodeId(Long fromGraphNodeKey) {
		this.fromGraphNodeId = fromGraphNodeKey;
	}

	/**
	 * Alternate method of referring to the toGraphNode
	 */
	public String getToGraphNodeName() {
		return toGraphNodeName;
	}

	public void setToGraphNodeName(String toGraphNodeName) {
		this.toGraphNodeName = toGraphNodeName;
	}

	/**
	 * Alternate method of referring from the toGraphNode
	 */
	public String getFromGraphNodeName() {
		return fromGraphNodeName;
	}

	public void setFromGraphNodeName(String fromGraphNodeName) {
		this.fromGraphNodeName = fromGraphNodeName;
	}

	
}
