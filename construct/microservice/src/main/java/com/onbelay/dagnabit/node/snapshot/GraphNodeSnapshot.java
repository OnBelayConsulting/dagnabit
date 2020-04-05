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

import java.util.ArrayList;
import java.util.List;

import com.onbelay.dagnabit.node.shared.GraphNodeDetail;

public class GraphNodeSnapshot extends AbstractSnapshot {
	private GraphNodeDetail detail = new GraphNodeDetail();
	
	private List<GraphLinkSnapshot> fromGraphLinks = new ArrayList<GraphLinkSnapshot>();
	private List<GraphLinkSnapshot> toGraphLinks = new ArrayList<GraphLinkSnapshot>();
	
	public GraphNodeSnapshot() {
		
	}
	
	public GraphNodeSnapshot(Long entityKey) {
		super(entityKey);
	}
	
	public GraphNodeSnapshot(
			String name,
			String description) {
		
		this.detail.setName(name);
		this.detail.setDescription(description);
	}

	public GraphNodeSnapshot(
			String name,
			String nodeType,
			String description) {
		
		this.detail.setName(name);
		this.detail.setNodeType(nodeType);
		this.detail.setDescription(description);
	}
	
	public GraphNodeDetail getDetail() {
		return detail;
	}

	public void setDetail(GraphNodeDetail graphNodeDetail) {
		this.detail = graphNodeDetail;
	}

	public List<GraphLinkSnapshot> getFromGraphLinks() {
		return fromGraphLinks;
	}

	public void setFromGraphLinks(List<GraphLinkSnapshot> fromGraphLinks) {
		this.fromGraphLinks = fromGraphLinks;
	}

	public List<GraphLinkSnapshot> getToGraphLinks() {
		return toGraphLinks;
	}

	public void setToGraphLinks(List<GraphLinkSnapshot> toGraphLinks) {
		this.toGraphLinks = toGraphLinks;
	}

	
}
