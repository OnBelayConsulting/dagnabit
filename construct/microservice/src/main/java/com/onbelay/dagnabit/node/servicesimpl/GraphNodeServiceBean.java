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
import com.onbelay.dagnabit.node.assemblers.GraphNodeAssembler;
import com.onbelay.dagnabit.node.enums.DagTransactionErrorCodes;
import com.onbelay.dagnabit.node.enums.EntityState;
import com.onbelay.dagnabit.node.model.GraphNode;
import com.onbelay.dagnabit.node.repository.GraphNodeRepository;
import com.onbelay.dagnabit.node.services.GraphNodeService;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshotCollection;


@Service(value="graphNodeService")
@Transactional
public class GraphNodeServiceBean extends BaseGraphServiceBean implements GraphNodeService {
	private static Logger logger =LogManager.getLogger();

	@Autowired
	private GraphNodeRepository graphNodeRepository;
	
	
	public Long createGraphNode(GraphNodeSnapshot snapshot) {
		
		initializeSession();
		
		if (snapshot.getEntityState() == null) {
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_NODE_SSHOT_MISSING_ENTITY_STATE.getCode());
		}
		
		if (snapshot.getEntityState() != EntityState.NEW) {
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_NODE_SSHOT_INVALID_ENTITY_STATE_FOR_CREATE.getCode());
		} else {
			GraphNode node = GraphNode.createFromSnapshot(snapshot);
			return node.getId();
		}
	}
	
	public Long updateGraphNode(GraphNodeSnapshot snapshot) {
		
		initializeSession();
		if (snapshot.getEntityState() == null) {
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_NODE_SSHOT_MISSING_ENTITY_STATE.getCode());
		}
		
		if (snapshot.getEntityState() == EntityState.MODIFIED || snapshot.getEntityState() == EntityState.DELETE) {
			
			if (snapshot.getEntityId() == null)
				throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_NODE_UPDATE_MISSING_ID.getCode());
			
			Optional<GraphNode> nodeOptional = graphNodeRepository.findById(snapshot.getEntityId());
			GraphNode node = nodeOptional.get();
			node.updateWith(snapshot);
			return node.getId();
		} if (snapshot.getEntityState() == EntityState.UNMODIFIED) {
			return snapshot.getEntityId();
		} else {
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_NODE_SSHOT_INVALID_ENTITY_STATE_FOR_UPDATE.getCode());
		}

	}

	
	@Override
	public GraphNodeSnapshotCollection findByCategory(String categoryName) { 
		
		initializeSession();

		
		List<GraphNode> graphNodes = graphNodeRepository.findByDetailCategoryOrderByDetailName(categoryName);
		
		GraphNodeAssembler assembler = new GraphNodeAssembler();
		
		List<GraphNodeSnapshot> snapshots = assembler.assemble(graphNodes);
		
		return new GraphNodeSnapshotCollection(snapshots, snapshots.size());
	}
	
	@Override
	public GraphNodeSnapshotCollection findByNodeType(String nodeType) { 
		
		initializeSession();

		
		List<GraphNode> graphNodes = graphNodeRepository.findByDetailNodeTypeOrderByDetailName(nodeType);
		
		GraphNodeAssembler assembler = new GraphNodeAssembler();
		
		List<GraphNodeSnapshot> snapshots = assembler.assemble(graphNodes);
		
		return new GraphNodeSnapshotCollection(snapshots, snapshots.size());
	}
	
	
	@Override
	public GraphNodeSnapshotCollection findByNodeTypeAndCategory(String nodeType, String categoryName) { 
		
		initializeSession();

		
		List<GraphNode> graphNodes = graphNodeRepository.findByDetailNodeTypeAndDetailCategoryOrderByDetailName(nodeType, categoryName);
		
		GraphNodeAssembler assembler = new GraphNodeAssembler();
		
		List<GraphNodeSnapshot> snapshots = assembler.assemble(graphNodes);
		
		return new GraphNodeSnapshotCollection(snapshots, snapshots.size());
	}
	
	
	
	@Override
	public GraphNodeSnapshotCollection findAll() {
		
		GraphNodeAssembler assembler = new GraphNodeAssembler();
		
		List<GraphNodeSnapshot> nodes = assembler.assemble(graphNodeRepository.findAll());
		
		return new GraphNodeSnapshotCollection(nodes);
	}

	@Override
	public GraphNodeSnapshot loadGraphNode(Long key) {
		
		initializeSession();
		
		Optional<GraphNode> node = graphNodeRepository.findById(key);
		GraphNodeAssembler assembler = new  GraphNodeAssembler();
		
		return assembler.assemble(node.get());
	}

	@Override
	public List<Long> saveOrUpdateGraphNodes(List<GraphNodeSnapshot> snapshots) {
		
		initializeSession();

		ArrayList<Long> ids = new ArrayList<Long>();
		
		for (GraphNodeSnapshot snapshot : snapshots) { 
			if (snapshot.getEntityState() == null) {
				throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_NODE_SSHOT_MISSING_ENTITY_STATE.getCode());
			}
			if (snapshot.getEntityState() == EntityState.NEW) {
				GraphNode graphNode = GraphNode.createFromSnapshot(snapshot);
				ids.add(graphNode.getId());
			} else  if (snapshot.getEntityState() == EntityState.UNMODIFIED) {
				// simply return the entity id that was passed in as part of the snapshot if not new or modified.
				ids.add(snapshot.getEntityId());
			} else {
				Optional<GraphNode> graphNode = graphNodeRepository.findById(snapshot.getEntityId());
				graphNode.get().updateWith(snapshot);
				ids.add(graphNode.get().getId());
			}

		}
		
		return ids;
	}


}
