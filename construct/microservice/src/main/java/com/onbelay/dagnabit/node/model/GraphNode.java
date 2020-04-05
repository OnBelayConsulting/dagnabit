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


import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.onbelay.dagnabit.common.component.ApplicationContextFactory;
import com.onbelay.dagnabit.common.exception.RuntimeDagException;
import com.onbelay.dagnabit.node.enums.EntityState;
import com.onbelay.dagnabit.node.repository.GraphNodeRepository;
import com.onbelay.dagnabit.node.shared.GraphNodeDetail;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshot;

@Entity
@Table(name = "GRAPH_NODE")
public class GraphNode extends AbstractEntity {
	private Long id;
    private GraphNodeDetail detail = new GraphNodeDetail();
	
	protected GraphNode() {
		
	}
	
	private GraphNode(
			String name,
			String type,
			String description) {
		
		this.detail.setName(name);
		this.detail.setNodeType(type);
		this.detail.setDescription(description);
		
	}
	
	private GraphNode(
			String name,
			String description) {
		
		this.detail.setDefaults();
		this.detail.setName(name);
		this.detail.setDescription(description);
		
	}
	
	
	public static GraphNode create(
			String name,
			String type,
			String description) {
		
		GraphNode node = new GraphNode(
				name,
				type,
				description);
		node.save();
		return node;
	}

	public void save() {
		validate();
		getRepository().save(this);
	}
	
	public static GraphNode create(
			String name,
			String description) {
		
		GraphNode node = new GraphNode(
				name,
				description);
		node.save();
		return node;
	}

	public static GraphNode createFromSnapshot(GraphNodeSnapshot snapshot) {
		GraphNode node = new GraphNode();
		node.createWith(snapshot);
		node.save();
		return node;
	}

	protected void createWith(GraphNodeSnapshot snapshot) {
		detail.setDefaults();
		this.detail.copyFrom(snapshot.getDetail());
	}
	
	public void updateWith(GraphNodeSnapshot snapshot) {
		if (snapshot.getEntityState() == EntityState.MODIFIED) {
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

	@Id
    @Column(name="ENTITY_ID", insertable = false, updatable = false)
	@SequenceGenerator(name="graphNodeGen", sequenceName="GRAPH_NODE_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "graphNodeGen")

	public Long getId() {
		return id;
	}

    private void setId(Long graphNodeId) {
		this.id = graphNodeId;
	}

    
    protected void validate() throws RuntimeDagException {
    	detail.validate();
    	
    }


    @Embedded
    public GraphNodeDetail getDetail() {
		return detail;
	}

	protected void setDetail(GraphNodeDetail graphNodeDetail) {
		this.detail = graphNodeDetail;
	}

	@Transient
	protected GraphNodeRepository getRepository() {
		return (GraphNodeRepository) ApplicationContextFactory.getBean(GraphNodeRepository.BEAN_NAME);
	}

}
