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
package com.onbelay.dagnabit.node.servicesimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onbelay.dagnabit.common.exception.RuntimeDagException;
import com.onbelay.dagnabit.common.serviceimpl.BaseGraphServiceBean;
import com.onbelay.dagnabit.node.assemblers.GraphLinkAssembler;
import com.onbelay.dagnabit.node.enums.DagTransactionErrorCodes;
import com.onbelay.dagnabit.node.enums.EntityState;
import com.onbelay.dagnabit.node.model.GraphLink;
import com.onbelay.dagnabit.node.repository.GraphLinkRepository;
import com.onbelay.dagnabit.node.services.GraphLinkService;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshotCollection;

@Service(value="graphLinkService")
@Transactional
public class GraphLinkServiceBean extends BaseGraphServiceBean implements GraphLinkService {
	private static Logger logger =LogManager.getLogger();

	@Autowired
	private GraphLinkRepository graphLinkRepository;

	@Override
	public Long createGraphLink(GraphLinkSnapshot snapshot) {
		
		initializeSession();
		
		if (snapshot.getEntityState() == null)
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_LINK_SSHOT_MISSING_ENTITY_STATE.getCode());

		if (snapshot.getEntityState() != EntityState.NEW) {
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_LINK_SSHOT_INVALID_ENTITY_STATE_FOR_CREATE.getCode());
		} else {
			GraphLink graphLink = GraphLink.createFromSnapshot(snapshot);
			return graphLink.getId();
		}
	}

	@Override
	public GraphLinkSnapshot load(Long entityId) {
		
		initializeSession();
		
		Optional<GraphLink>  graphLink = graphLinkRepository.findById(entityId);
		if (graphLink.isPresent() == false)
			throw new RuntimeDagException("Error: graphlink not found for :" + entityId);
		
		GraphLinkAssembler assembler = new GraphLinkAssembler();
		
		return assembler.assemble(graphLink.get());
	}

	

	@Override
	public GraphLinkSnapshotCollection findAll() {
		
		GraphLinkAssembler assembler = new GraphLinkAssembler();
		
		List<GraphLinkSnapshot> snapshots = assembler.assemble(graphLinkRepository.findAll());
		
		return new GraphLinkSnapshotCollection(snapshots);
	}

	@Override
	public GraphLinkSnapshotCollection findByCategory(String categoryName) { 
		
		initializeSession();
		
		List<GraphLink> graphLinks = graphLinkRepository.findByDetailCategory(categoryName);
		
		GraphLinkAssembler assembler = new GraphLinkAssembler();
		
		return new GraphLinkSnapshotCollection(
				assembler.assemble(graphLinks), 
				graphLinks.size());
	}

	@Override
	public GraphLinkSnapshotCollection findByLinkTypeAndCategory(String linkType, String categoryName) { 
		
		initializeSession();
		
		List<GraphLink> graphLinks = graphLinkRepository.findByDetailLinkTypeAndDetailCategory(linkType, categoryName);
		
		GraphLinkAssembler assembler = new GraphLinkAssembler();
		
		return new GraphLinkSnapshotCollection(
				assembler.assemble(graphLinks), 
				graphLinks.size());
	}

	@Override
	public GraphLinkSnapshotCollection findByLinkType(String linkType) { 
		
		initializeSession();
		
		List<GraphLink> graphLinks = graphLinkRepository.findByDetailLinkType(linkType);
		
		GraphLinkAssembler assembler = new GraphLinkAssembler();
		
		return new GraphLinkSnapshotCollection(
				assembler.assemble(graphLinks), 
				graphLinks.size());
	}

	@Override
	public List<Long> saveOrUpdateGraphLinks(List<GraphLinkSnapshot> snapshots) {
		
		initializeSession();

		ArrayList<Long> ids = new ArrayList<Long>();
		for (GraphLinkSnapshot snapshot : snapshots) {
			
			if (snapshot.getEntityState() == null)
				throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_LINK_SSHOT_MISSING_ENTITY_STATE.getCode());

			
			if (snapshot.getEntityState() == EntityState.NEW) {
				GraphLink graphLink = GraphLink.createFromSnapshot(snapshot);
				ids.add(graphLink.getId());
			} else  if (snapshot.getEntityState() == EntityState.UNMODIFIED) {
				// simply return the entity id that was passed in as part of the snapshot if not new or modified.
				ids.add(snapshot.getEntityId());
			} else {
				Optional<GraphLink> graphLink = graphLinkRepository.findById(snapshot.getEntityId());
				graphLink.get().updateWith(snapshot);
				ids.add(graphLink.get().getId());
			}
		}
		
		return ids;
	}

	@Override
	public Long updateGraphLink(GraphLinkSnapshot snapshot) {
		
		initializeSession();

		
		if (snapshot.getEntityState() == null)
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_LINK_SSHOT_MISSING_ENTITY_STATE.getCode());
		
		if (snapshot.getEntityState() == EntityState.MODIFIED) {
			Optional<GraphLink> graphLink = graphLinkRepository.findById(snapshot.getEntityId());
			graphLink.get().updateWith(snapshot);
			return graphLink.get().getId();
		} else if (snapshot.getEntityState() == EntityState.UNMODIFIED) {
			return snapshot.getEntityId();
		
		} else {
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_LINK_SSHOT_INVALID_ENTITY_STATE_FOR_UPDATE.getCode());
		}
	}

}
