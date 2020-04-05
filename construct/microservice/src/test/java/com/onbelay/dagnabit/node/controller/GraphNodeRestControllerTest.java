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
package com.onbelay.dagnabit.node.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onbelay.dagnabit.node.model.GraphLinkFixture;
import com.onbelay.dagnabit.node.model.GraphNode;
import com.onbelay.dagnabit.node.model.GraphNodeFixture;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshotCollection;
import com.onbelay.dagnabit.persistence.TransactionalSpringTestCase;

@ComponentScan("com.onbelay")
@RunWith(SpringRunner.class)
@SpringBootTest()
@TestPropertySource( locations="classpath:application-integrationtest.properties")

public class GraphNodeRestControllerTest extends TransactionalSpringTestCase {
	private static final Logger logger = LogManager.getLogger(GraphNodeRestControllerTest.class);
	
	@Autowired
	private GraphNodeRestController graphNodeRestController;

	
	@Override
	public void beforeRun() throws Throwable {
		super.beforeRun();
		flush();
	}
	
	
	@Test
	@WithMockUser(username="test")
	public void testPostCreateGraphNode() throws Exception {

		MockMvc mvc = MockMvcBuilders.standaloneSetup(graphNodeRestController)
				.build();
		
		ObjectMapper mapper = new ObjectMapper();

		GraphNodeSnapshot snapshot = GraphNodeFixture.createNodeSnapshot(
				"hht-3", 
				"node");
		
		String jsonPayload = mapper.writeValueAsString(snapshot);
		
		logger.error(jsonPayload);
		
		ResultActions result = mvc.perform(post("/api/graphNodes")
								.accept(MediaType.APPLICATION_JSON)
								.contentType(MediaType.APPLICATION_JSON)
								.content(jsonPayload));
		
		MvcResult mvcResult = result.andReturn();
		
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		
		logger.debug("Json: " + jsonStringResponse);
		
		GraphNodeSnapshot created = mapper.readValue(jsonStringResponse, GraphNodeSnapshot.class);
		
		assertEquals("hht-3", created.getDetail().getName());
		assertEquals("node", created.getDetail().getNodeType());
		assertEquals("hht-3", created.getDetail().getDescription());
		
	}
	
	
	@Test
	@WithMockUser(username="test")
	public void testPostCreateGraphLink() throws Exception {

		MockMvc mvc = MockMvcBuilders.standaloneSetup(graphNodeRestController)
				.build();
		
		ObjectMapper mapper = new ObjectMapper();

		GraphNode fromNode = GraphNodeFixture.createNode(
				"fromNode", 
				"node");

		GraphNode toNode = GraphNodeFixture.createNode(
				"toNode", 
				"node");
		
		GraphLinkSnapshot snapshot = GraphLinkFixture.createLinkSnapshot(
				"parentOf", 
				fromNode.getId(), 
				toNode.getId());
		
		String jsonPayload = mapper.writeValueAsString(snapshot);
		
		logger.error(jsonPayload);
		
		ResultActions result = mvc.perform(post("/api/graphLinks")
								.accept(MediaType.APPLICATION_JSON)
								.contentType(MediaType.APPLICATION_JSON)
								.content(jsonPayload));
		
		MvcResult mvcResult = result.andReturn();
		
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		
		logger.debug("Json: " + jsonStringResponse);
		
		GraphLinkSnapshot created = mapper.readValue(jsonStringResponse, GraphLinkSnapshot.class);
		
		assertEquals("fromNode -[parentOf]-> toNode", created.getDetail().getName());
		assertEquals("parentOf", created.getDetail().getLinkType());
		
		assertEquals("fromNode", created.getFromGraphNodeName());
		assertEquals("toNode", created.getToGraphNodeName());
	}
	
	
	
	
	@Test
	@WithMockUser(username="test")
	public void testGetFindGraphNodes() throws Exception {
		
		GraphNodeFixture.createMultipleNodes("JH_", "node", 10);
		flush();
		
		MockMvc mvc = MockMvcBuilders.standaloneSetup(graphNodeRestController)
				.build();

		
		ResultActions result = mvc.perform(get("/api/graphNodes"));
		
		MvcResult mvcResult = result.andReturn();
		
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		
		logger.debug("Json: " + jsonStringResponse);
		

		
		ObjectMapper mapper = new ObjectMapper();
		
		GraphNodeSnapshotCollection collection = mapper.readValue(jsonStringResponse, GraphNodeSnapshotCollection.class);
		
		assertEquals(10, collection.getSnapshots().size());
		
		assertEquals(10, collection.getCount());
		
		assertEquals(10, collection.getTotalItems());
		
		for (GraphNodeSnapshot snapshot : collection.getSnapshots()) {
			logger.error(snapshot.toString());
		}
	}

	
	@Test
	@WithMockUser(username="test")
	public void testGetFindNodesWithNameQuery() throws Exception {
		
		MockMvc mvc = MockMvcBuilders.standaloneSetup(graphNodeRestController)
				.build();
		
		GraphNodeFixture.createMultipleNodes("JH", "node", 10);
		flush();

		
		ResultActions result = mvc.perform(get("/api/graphNodes?type='node'"));
		
		MvcResult mvcResult = result.andReturn();
		
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		
		logger.debug("Json: " + jsonStringResponse);
		

		
		ObjectMapper mapper = new ObjectMapper();
		
		GraphNodeSnapshotCollection collection = mapper.readValue(jsonStringResponse, GraphNodeSnapshotCollection.class);
		
		assertEquals(1, collection.getSnapshots().size());
		
		assertEquals(1, collection.getCount());
		
		assertEquals(1, collection.getTotalItems());
		
		for (GraphNodeSnapshot snapshot : collection.getSnapshots()) {
			logger.error(snapshot.toString());
		}
	}

	
	@Test
	@WithMockUser(username="test")
	public void testGetFindNodesWithTypeQuery() throws Exception {
		
		MockMvc mvc = MockMvcBuilders.standaloneSetup(graphNodeRestController)
				.build();
		
		GraphNodeFixture.createMultipleNodes("JH", "node", 10);
		GraphNodeFixture.createMultipleNodes("AB", "thing", 10);
		flush();

		
		ResultActions result = mvc.perform(get("/api/graphNodes?type='thing'"));
		
		MvcResult mvcResult = result.andReturn();
		
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		
		logger.debug("Json: " + jsonStringResponse);
		

		
		ObjectMapper mapper = new ObjectMapper();
		
		GraphNodeSnapshotCollection collection = mapper.readValue(jsonStringResponse, GraphNodeSnapshotCollection.class);
		
		assertEquals(10, collection.getSnapshots().size());
		
		assertEquals(10, collection.getCount());
		
		assertEquals(10, collection.getTotalItems());
		
		for (GraphNodeSnapshot snapshot : collection.getSnapshots()) {
			logger.error(snapshot.toString());
		}
	}



	
}
