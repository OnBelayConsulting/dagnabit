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

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.onbelay.dagnabit.common.component.ApplicationContextFactory;
import com.onbelay.dagnabit.common.exception.RuntimeDagException;
import com.onbelay.dagnabit.node.enums.DagTransactionErrorCodes;
import com.onbelay.dagnabit.node.enums.EntityState;
import com.onbelay.dagnabit.node.repository.GraphLinkRepository;
import com.onbelay.dagnabit.node.repository.GraphNodeRepository;
import com.onbelay.dagnabit.node.shared.GraphLinkDetail;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshot;

@Entity
@Table(name = "GRAPH_LINK")
public class GraphLink extends AbstractEntity {
	private Long id;
    
    private GraphNode fromGraphNode;
    private GraphNode toGraphNode;
    
    private GraphLinkDetail detail = new GraphLinkDetail();
	
	protected GraphLink() {
		
	}
	
	
	protected GraphLink(
			String name,
			String type,
			GraphNode fromNode,
			GraphNode toNode) {
		
		this.detail.setDefaults();
		this.detail.setName(name);
		this.fromGraphNode = fromNode;
		this.toGraphNode = toNode;
		
	}
	
	public static GraphLink create(
			GraphLinkDetail detailIn,
			GraphNode fromNode,
			GraphNode toNode) {
		
		GraphLink graphLink = new GraphLink();
		graphLink.getDetail().setDefaults();
		graphLink.getDetail().copyFrom(detailIn);
		graphLink.setFromGraphNode(fromNode);
		graphLink.setToGraphNode(toNode);
		
		graphLink.getDetail().createNameIfMissing(
				fromNode.getDetail().getName(),
				toNode.getDetail().getName());
		
		graphLink.save();
		
		return graphLink;
	}
	

	public static GraphLink createFromSnapshot(GraphLinkSnapshot snapshot) {
		GraphLink link = new GraphLink();
		link.createWith(snapshot);
		return link;
	}

	protected void createWith(GraphLinkSnapshot snapshot) {
		setAssociationsFromSnapshot(snapshot);
		this.detail.setDefaults();
		this.detail.copyFrom(snapshot.getDetail());
		
		String fromNodeName = "missing";
		if (fromGraphNode != null)
			fromNodeName = fromGraphNode.getDetail().getName();
		
		String toNodeName = "missing";
		if (toGraphNode != null)
			toNodeName = toGraphNode.getDetail().getName();
		
		getDetail().createNameIfMissing(
				fromNodeName,
				toNodeName);
		save();
	}
	
	public void save() {
		validate();
		getRepository().save(this);
	}
	
	public void updateWith(GraphLinkSnapshot snapshot) {
		
		if (snapshot.getEntityState() == EntityState.MODIFIED) {
			setAssociationsFromSnapshot(snapshot);
			this.detail.copyFrom(snapshot.getDetail());
			update();
		} else if (snapshot.getEntityState() == EntityState.DELETE) {
			delete();
		}
	}
	
	private void delete() {
		getRepository().delete(this);
	}
	
	private void update() {
		validate();
	}
	
	private void setAssociationsFromSnapshot(GraphLinkSnapshot snapshot) {
		if (snapshot.getFromGraphNodeId() != null) {
			Optional<GraphNode> node = getNodeRepository().findById(snapshot.getFromGraphNodeId());
			if (node.isPresent())
				this.fromGraphNode = node.get();
		} else {
			if (snapshot.getFromGraphNodeName() != null) {
				Optional<GraphNode> fromNode = getNodeRepository().findOneByDetailName(snapshot.getFromGraphNodeName());
				if (fromNode.isPresent())
					this.fromGraphNode = fromNode.get();
			}
				
		}
		
		if (snapshot.getToGraphNodeId() != null) {
			Optional<GraphNode> toNode = getNodeRepository().findById(snapshot.getToGraphNodeId());
			if (toNode.isPresent())
				this.toGraphNode = toNode.get();
		} else {
			Optional<GraphNode> toNode = getNodeRepository().findOneByDetailName(snapshot.getToGraphNodeName());
			if (toNode.isPresent())
				this.toGraphNode = toNode.get();
		}
	}
	

	protected void validate() throws RuntimeDagException {
		detail.validate();
		
		if (fromGraphNode == null)
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_LINK_MISSING_FROM_NODE.getCode());
		
		if (toGraphNode == null)
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_LINK_MISSING_TO_NODE.getCode());
	}


	@Id
    @Column(name="ENTITY_ID" , insertable = false, updatable = false)
	@SequenceGenerator(name="graphLinkGen", sequenceName="GRAPH_LINK_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "graphLinkGen")

	public Long getId() {
		return id;
	}

    private void setId(Long graphLinkId) {
		this.id = graphLinkId;
	}


	@ManyToOne
	@JoinColumn(name ="FROM_GRAPH_NODE_ID")
    public GraphNode getFromGraphNode() {
		return fromGraphNode;
	}


	private void setFromGraphNode(GraphNode fromGraphNode) {
		this.fromGraphNode = fromGraphNode;
	}


	@ManyToOne
	@JoinColumn(name ="TO_GRAPH_NODE_ID")
	public GraphNode getToGraphNode() {
		return toGraphNode;
	}

	private void setToGraphNode(GraphNode toGraphNode) {
		this.toGraphNode = toGraphNode;
	}


	@Embedded
    public GraphLinkDetail getDetail() {
		return detail;
	}

	protected void setDetail(GraphLinkDetail graphLinkDetail) {
		this.detail = graphLinkDetail;
	}
	

	@Transient
	protected GraphLinkRepository getRepository() {
		return (GraphLinkRepository) ApplicationContextFactory.getBean(GraphLinkRepository.BEAN_NAME);
	}


	@Transient
	protected GraphNodeRepository getNodeRepository() {
		return (GraphNodeRepository) ApplicationContextFactory.getBean(GraphNodeRepository.BEAN_NAME);
	}

}
