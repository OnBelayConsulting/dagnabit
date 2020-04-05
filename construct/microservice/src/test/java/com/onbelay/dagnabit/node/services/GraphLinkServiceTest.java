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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.onbelay.dagnabit.common.exception.RuntimeDagException;
import com.onbelay.dagnabit.node.enums.DagTransactionErrorCodes;
import com.onbelay.dagnabit.node.enums.EntityState;
import com.onbelay.dagnabit.node.model.GraphLinkFixture;
import com.onbelay.dagnabit.node.model.GraphNode;
import com.onbelay.dagnabit.node.model.GraphNodeFixture;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshotCollection;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.persistence.TransactionalSpringTestCase;
@ComponentScan("com.onbelay.*")
@EntityScan("com.onbelay.*")
@RunWith(SpringRunner.class)
@TestPropertySource( locations="classpath:application-integrationtest.properties")
@SpringBootTest

public class GraphLinkServiceTest extends TransactionalSpringTestCase {
	
	@Autowired
	private GraphNodeService graphNodeService;
	
	@Autowired
	private GraphLinkService graphLinkService;

	
	@Test
	public void testFindLinks() {
		GraphNode fromNode = GraphNodeFixture.createNode("from", "node");
		GraphNode toNode = GraphNodeFixture.createNode("to", "node");
		
		
		GraphLinkFixture.createLink(fromNode, toNode);
		GraphLinkFixture.createLink("parentOf", fromNode, toNode);
		
		GraphLinkSnapshotCollection collection = graphLinkService.findAll();
		
		assertEquals(2, collection.getCount());
	}
	
	
	
	@Test
	public void testCreateLink() {


		GraphNodeSnapshot fromGraphNode = new GraphNodeSnapshot(
				"fromNode",
				"node",
				"from graph node in link");
		
		
		Long fromNodeKey = graphNodeService.createGraphNode(fromGraphNode);
		

		GraphNodeSnapshot toGraphNode = new GraphNodeSnapshot(
				"toNode",
				"node",
				"to graph node in link");
		
		
		Long toNodeKey = graphNodeService.createGraphNode(toGraphNode);
		
		
		GraphLinkSnapshot linkSnapshot = new GraphLinkSnapshot();
		
		linkSnapshot.getDetail().setName("nodeName->nodeName2");
		linkSnapshot.getDetail().setLinkType("myType");
		linkSnapshot.setFromGraphNodeId(fromNodeKey);
		linkSnapshot.setToGraphNodeId(toNodeKey);
		
		Long linkId = graphLinkService.createGraphLink(linkSnapshot);
		flush();

		
		GraphLinkSnapshot fetched = graphLinkService.load(linkId);
		assertEquals("nodeName->nodeName2", fetched.getDetail().getName());
		assertEquals("myType", fetched.getDetail().getLinkType());
		assertEquals(fromNodeKey, fetched.getFromGraphNodeId());
		assertEquals(toNodeKey, fetched.getToGraphNodeId());
	}
	
	
	@Test
	public void testCreateLinkFailMissingFromNode() {


		GraphNodeSnapshot toGraphNode = new GraphNodeSnapshot(
				"toNode",
				"node",
				"to graph node in link");
		
		
		Long toNodeKey = graphNodeService.createGraphNode(toGraphNode);
		
		
		GraphLinkSnapshot linkSnapshot = new GraphLinkSnapshot();
		
		linkSnapshot.getDetail().setName("nodeName->nodeName2");
		linkSnapshot.setToGraphNodeId(toNodeKey);
		
		try {
			graphLinkService.createGraphLink(linkSnapshot);
			fail ("should have thrown exception");
		} catch (RuntimeDagException e) {
			assertEquals(DagTransactionErrorCodes.GRAPH_LINK_MISSING_FROM_NODE.getCode(), e.getErrorCode());
		} catch (Throwable t) {
			fail ("Should have thrown JSRuntimeException");
		}
		
	}
	
	
	@Test
	public void testCreateLinkFailMissingToNode() {


		GraphNodeSnapshot fromGraphNode = new GraphNodeSnapshot(
				"fromNode",
				"node",
				"from graph node in link");
		
		
		Long fromNodeKey = graphNodeService.createGraphNode(fromGraphNode);
		

		
		GraphLinkSnapshot linkSnapshot = new GraphLinkSnapshot();
		
		linkSnapshot.getDetail().setName("nodeName->nodeName2");
		linkSnapshot.setFromGraphNodeId(fromNodeKey);
		
		try {
			graphLinkService.createGraphLink(linkSnapshot);
			fail ("should have thrown exception");
		} catch (RuntimeDagException e) {
			assertEquals(DagTransactionErrorCodes.GRAPH_LINK_MISSING_TO_NODE.getCode(), e.getErrorCode());
		} catch (Throwable t) {
			fail ("Should have thrown JSRuntimeException");
		}
	}
	
	
	@Test
	public void testCreateLinkFailBadFromNode() {


		
		// Invalid id
		Long fromNodeKey = new Long(-4000000l);
		

		GraphNodeSnapshot toGraphNode = new GraphNodeSnapshot(
				"toNode",
				"node",
				"to graph node in link");
		
		
		Long toNodeKey = graphNodeService.createGraphNode(toGraphNode);
		
		
		GraphLinkSnapshot linkSnapshot = new GraphLinkSnapshot();
		
		linkSnapshot.getDetail().setName("nodeName->nodeName2");
		linkSnapshot.setFromGraphNodeId(fromNodeKey);
		linkSnapshot.setToGraphNodeId(toNodeKey);
		
		try {
			graphLinkService.createGraphLink(linkSnapshot);
			fail ("should have thrown exception");
		} catch (RuntimeDagException e) {
			assertEquals(DagTransactionErrorCodes.GRAPH_LINK_MISSING_FROM_NODE.getCode(), e.getErrorCode());
		} catch (Throwable t) {
			fail ("Should have thrown JSRuntimeException");
		}
	}
	
	@Test
	public void testCreateLinkFailBadToNode() {


		GraphNodeSnapshot fromGraphNode = new GraphNodeSnapshot(
				"fromNode",
				"node",
				"from graph node in link");
		
		
		Long fromNodeKey = graphNodeService.createGraphNode(fromGraphNode);
		
		

		
		// bad id
		Long toNodeKey = new Long(-2000l);
		
		
		GraphLinkSnapshot linkSnapshot = new GraphLinkSnapshot();
		
		linkSnapshot.getDetail().setName("nodeName->nodeName2");
		linkSnapshot.setFromGraphNodeId(fromNodeKey);
		linkSnapshot.setToGraphNodeId(toNodeKey);
		
		try {
			graphLinkService.createGraphLink(linkSnapshot);
			fail ("should have thrown exception");
		} catch (RuntimeDagException e) {
			assertEquals(DagTransactionErrorCodes.GRAPH_LINK_MISSING_TO_NODE.getCode(), e.getErrorCode());
		} catch (Throwable t) {
			fail ("Should have thrown RuntimeDagException");
		}
	}
	
	
	@Test
	public void testCreateGraphLinkWithDefault() {

		GraphNodeSnapshot fromGraphNode = new GraphNodeSnapshot(
				"fromNode",
				"node",
				"from graph node in link");
		
		
		Long fromNodeKey = graphNodeService.createGraphNode(fromGraphNode);
		

		GraphNodeSnapshot toGraphNode = new GraphNodeSnapshot(
				"toNode",
				"node",
				"to graph node in link");
		
		
		Long toNodeKey = graphNodeService.createGraphNode(toGraphNode);
		
		
		GraphLinkSnapshot linkSnapshot = new GraphLinkSnapshot();
		
		linkSnapshot.getDetail().setName("nodeName->nodeName2");
		linkSnapshot.setFromGraphNodeId(fromNodeKey);
		linkSnapshot.setToGraphNodeId(toNodeKey);
		
		Long linkId = graphLinkService.createGraphLink(linkSnapshot);
		flush();
		clearCache();
		
		GraphLinkSnapshot fetched = graphLinkService.load(linkId);
		assertEquals("nodeName->nodeName2", fetched.getDetail().getName());
		assertEquals("link", fetched.getDetail().getLinkType());
		assertEquals(fromNodeKey, fetched.getFromGraphNodeId());
		assertEquals(toNodeKey, fetched.getToGraphNodeId());
		
	}
	
	
	@Test
	public void testUpdateGraphLinkName() {

		GraphNodeSnapshot fromGraphNode = new GraphNodeSnapshot(
				"fromNode",
				"node",
				"from graph node in link");
		
		
		Long fromNodeKey = graphNodeService.createGraphNode(fromGraphNode);
		

		GraphNodeSnapshot toGraphNode = new GraphNodeSnapshot(
				"toNode",
				"node",
				"to graph node in link");
		
		
		Long toNodeKey = graphNodeService.createGraphNode(toGraphNode);
		
		
		GraphLinkSnapshot linkSnapshot = new GraphLinkSnapshot();
		
		linkSnapshot.getDetail().setName("nodeName->nodeName2");
		linkSnapshot.setFromGraphNodeId(fromNodeKey);
		linkSnapshot.setToGraphNodeId(toNodeKey);
		
		Long linkId = graphLinkService.createGraphLink(linkSnapshot);
		flush();
		
		GraphLinkSnapshot fetched = graphLinkService.load(linkId);
		fetched.setEntityState(EntityState.MODIFIED);
		fetched.getDetail().setName("fred");
		
		graphLinkService.updateGraphLink(fetched);
		flush();
		
		GraphLinkSnapshot updated = graphLinkService.load(linkId);
		
		assertEquals("fred", updated.getDetail().getName());
		assertEquals("link", updated.getDetail().getLinkType());
		assertEquals(fromNodeKey, updated.getFromGraphNodeId());
		assertEquals(toNodeKey, updated.getToGraphNodeId());
		
	}
	
}
