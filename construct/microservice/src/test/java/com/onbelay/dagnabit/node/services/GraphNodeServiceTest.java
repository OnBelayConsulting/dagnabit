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
package com.onbelay.dagnabit.node.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.onbelay.dagnabit.common.exception.RuntimeDagException;
import com.onbelay.dagnabit.node.enums.DagTransactionErrorCodes;
import com.onbelay.dagnabit.node.enums.EntityState;
import com.onbelay.dagnabit.node.model.GraphNode;
import com.onbelay.dagnabit.node.model.GraphNodeFixture;
import com.onbelay.dagnabit.node.repository.GraphNodeRepository;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshotCollection;
import com.onbelay.dagnabit.persistence.TransactionalSpringTestCase;
@ComponentScan("com.onbelay.*")
@RunWith(SpringRunner.class)
@TestPropertySource( locations="classpath:application-integrationtest.properties")
@SpringBootTest

public class GraphNodeServiceTest extends TransactionalSpringTestCase {
	
	@Autowired
	private GraphNodeService graphNodeService;
	
	@Autowired
	private GraphNodeRepository graphNodeRepository;

	@Override
	public void beforeRun() throws Throwable {
		super.beforeRun();
		flush();
	}
	
	@Test
	public void testFindAllNodes() {
		GraphNodeFixture.createMultipleNodes("p_", "person", 10);
		GraphNodeFixture.createMultipleNodes("l_", "location", 10);
		flush();
		
		// should return all
		GraphNodeSnapshotCollection collection = graphNodeService.findAll();
		
		assertEquals(20, collection.getCount());
		
	}

	@Test
	public void testFindByType() {
		GraphNodeFixture.createMultipleNodes("p_", "person", 10);
		GraphNodeFixture.createMultipleNodes("l_", "location", 10);
		flush();
		
		// should return all
		GraphNodeSnapshotCollection collection = graphNodeService.findByNodeType("location");
		
		assertEquals(10, collection.getCount());
		
	}

	
	@Test
	public void testCreateNode() {


		GraphNodeSnapshot snapshot = new GraphNodeSnapshot(
				"nodeName",
				"myType",
				"description");
		
		
		Long entityId = graphNodeService.createGraphNode(snapshot);
		
		GraphNodeSnapshot fetched = graphNodeService.loadGraphNode(entityId);
		assertEquals("nodeName", fetched.getDetail().getName());
		assertEquals("myType", fetched.getDetail().getNodeType());
		assertEquals("description", fetched.getDetail().getDescription());
	}
	
	@Test
	public void testCreateNodeFailMissingName() {


		GraphNodeSnapshot snapshot = new GraphNodeSnapshot(
				null,
				"myType",
				"description");
		
		
		try {
			graphNodeService.createGraphNode(snapshot);
			fail("Should have thrown exception");
		} catch (RuntimeDagException e) {
			assertEquals(DagTransactionErrorCodes.GRAPH_NODE_MISSING_NAME.getCode(), e.getErrorCode());
		} catch (Throwable t) {
			fail("Should have thrown RuntimeDagException");
		}
		
	}
	
	@Test
	public void testCreateNodeFailInvalidEntityState() {


		GraphNodeSnapshot snapshot = new GraphNodeSnapshot(
				null,
				"myType",
				"description");
		
		// Invalid entity state for a create - must be 	NEW
		snapshot.setEntityState(EntityState.MODIFIED);
		
		try {
			graphNodeService.createGraphNode(snapshot);
			fail("Should have thrown exception");
		} catch (RuntimeDagException e) {
			assertEquals(DagTransactionErrorCodes.GRAPH_NODE_SSHOT_INVALID_ENTITY_STATE_FOR_CREATE.getCode(), e.getErrorCode());
		} catch (Throwable t) {
			fail("Should have thrown JSRuntimeException");
		}
		
	}
	
	@Test
	public void testCreateNodeFailMissingType() {


		GraphNodeSnapshot snapshot = new GraphNodeSnapshot(
				"mine",
				"myType",
				"description");
		snapshot.getDetail().setNodeType(null); // setting explicitly to null
		
		try {
			graphNodeService.createGraphNode(snapshot);
			fail("Should have thrown exception");
		} catch (RuntimeDagException e) {
			assertEquals(DagTransactionErrorCodes.GRAPH_NODE_MISSING_TYPE.getCode(), e.getErrorCode());
		} catch (Throwable t) {
			fail("Should have thrown RuntimeDagException");
		}
		
	}
	
	
	@Test
	public void testCreateNodeWithDefaultTypeWillBeNode() {


		GraphNodeSnapshot snapshot = new GraphNodeSnapshot(
				"nodeName",
				"description");
		
		
		Long entityId = graphNodeService.createGraphNode(snapshot);
		GraphNodeSnapshot fetched = graphNodeService.loadGraphNode(entityId);
		assertEquals("nodeName", fetched.getDetail().getName());
		assertEquals("node", fetched.getDetail().getNodeType());
		assertEquals("description", fetched.getDetail().getDescription());

	}
	
	public void testSaveOrUpdateNodes() {

		GraphNodeSnapshot snapshot = new GraphNodeSnapshot(
				"nodeName",
				"description");
		

		// create an existing snapshot to update
		Long entityId = graphNodeService.createGraphNode(snapshot);
		GraphNodeSnapshot fetched = graphNodeService.loadGraphNode(entityId);

		GraphNodeSnapshot newSnapshot = new GraphNodeSnapshot(
				"newNodeName",
				"description");
		
		fetched.setEntityState(EntityState.MODIFIED);
		fetched.getDetail().setDescription("changed");
		
		List<GraphNodeSnapshot> snapshots = new ArrayList<GraphNodeSnapshot>();
		snapshots.add(fetched);
		snapshots.add(newSnapshot);
		
		List<Long> ids = graphNodeService.saveOrUpdateGraphNodes(snapshots);
		flush();
		
		GraphNodeSnapshot created = graphNodeService.loadGraphNode(ids.get(0));
		assertEquals("newNodeName", created.getDetail().getName());
		
		// fetch updated
		fetched = graphNodeService.loadGraphNode(entityId);
		assertEquals("changed", fetched.getDetail().getDescription());
	}

	
	public void testSaveOrUpdateNodesFailMissingEntityState() {

		GraphNodeSnapshot snapshot = new GraphNodeSnapshot(
				"nodeName",
				"description");
		

		// create an existing snapshot to update
		Long entityId = graphNodeService.createGraphNode(snapshot);
		GraphNodeSnapshot fetched = graphNodeService.loadGraphNode(entityId);

		GraphNodeSnapshot newSnapshot = new GraphNodeSnapshot(
				"newNodeName",
				"description");
		
		fetched.setEntityState(null);
		fetched.getDetail().setDescription("changed");
		
		List<GraphNodeSnapshot> snapshots = new ArrayList<GraphNodeSnapshot>();
		snapshots.add(fetched);
		snapshots.add(newSnapshot);
		
		try {
			graphNodeService.saveOrUpdateGraphNodes(snapshots);
			fail("Should have thrown exception");
		} catch (RuntimeDagException e) {
			assertEquals(DagTransactionErrorCodes.GRAPH_NODE_SSHOT_MISSING_ENTITY_STATE.getCode(), e.getErrorCode());
		} catch (Throwable t) {
			fail("Should have thrown exception");
		}
	}

	@Test
	public void testUpdateNode() {


		GraphNodeSnapshot snapshot = new GraphNodeSnapshot(
				"nodeName",
				"myType",
				"description");
		
		
		Long entityId = graphNodeService.createGraphNode(snapshot);
		
		GraphNodeSnapshot fetched = graphNodeService.loadGraphNode(entityId);
		
		fetched.getDetail().setName("updated");
		fetched.setEntityState(EntityState.MODIFIED);
		
		graphNodeService.updateGraphNode(fetched);
		flush();
		
		GraphNodeSnapshot updated = graphNodeService.loadGraphNode(entityId);
		assertEquals("updated", updated.getDetail().getName());
		assertEquals("myType", updated.getDetail().getNodeType());
		assertEquals("description", updated.getDetail().getDescription());
	}
	
	@Test
	public void testDeleteNode() {


		GraphNodeSnapshot snapshot = new GraphNodeSnapshot(
				"nodeName",
				"myType",
				"description");
		
		
		Long entityId = graphNodeService.createGraphNode(snapshot);

		Optional<GraphNode> node  =graphNodeRepository.findOneByDetailName("nodeName");
		assertTrue(node.isPresent());
		
		
		GraphNodeSnapshot fetched = graphNodeService.loadGraphNode(entityId);
		
		fetched.setEntityState(EntityState.DELETE);
		
		graphNodeService.updateGraphNode(fetched);
		flush();
		
		Optional<GraphNode> nodeUpdated  = graphNodeRepository.findOneByDetailName("nodeName");
		assertFalse(nodeUpdated.isPresent());
	}
	
	@Test
	public void testUpdateNodeFailMissingId() {


		GraphNodeSnapshot snapshot = new GraphNodeSnapshot(
				"nodeName",
				"myType",
				"description");
		
		
		graphNodeService.createGraphNode(snapshot);
		
		GraphNodeSnapshot toUpdate = new GraphNodeSnapshot();
		
		toUpdate.getDetail().setName("updated");
		toUpdate.setEntityState(EntityState.MODIFIED);
		
		// This will fail because the entity Id was not set. This is automatically set if you fetch from the service.
		try {
			graphNodeService.updateGraphNode(toUpdate);
			fail("Should have thrown exception");
		} catch (RuntimeDagException e) {
			assertEquals(DagTransactionErrorCodes.GRAPH_NODE_UPDATE_MISSING_ID.getCode(), e.getErrorCode());
		} catch (Throwable t) {
			fail("Should have thrown JSRuntimeException");
		}
	}
	
	@Test
	public void testUpdateNodeFailInvalid() {


		GraphNodeSnapshot snapshot = new GraphNodeSnapshot(
				"nodeName",
				"myType",
				"description");
		
		
		graphNodeService.createGraphNode(snapshot);
		
		GraphNodeSnapshot toUpdate = new GraphNodeSnapshot();
		
		toUpdate.getDetail().setName("updated");
		toUpdate.setEntityState(EntityState.MODIFIED);
		
		// This will fail because the entity Id was not set. This is automatically set if you fetch from the service.
		try {
			graphNodeService.updateGraphNode(toUpdate);
			fail("Should have thrown exception");
		} catch (RuntimeDagException e) {
			assertEquals(DagTransactionErrorCodes.GRAPH_NODE_UPDATE_MISSING_ID.getCode(), e.getErrorCode());
		} catch (Throwable t) {
			fail("Should have thrown JSRuntimeException");
		}
	}
	

}
