package com.onbelay.dagnabit.graphnode.controller;

import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.controller.DagControllerTestCase;
import com.onbelay.dagnabit.graphnode.model.GraphNode;
import com.onbelay.dagnabit.graphnode.model.GraphNodeFixture;
import com.onbelay.dagnabit.graphnode.model.GraphRelationshipFixture;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabitapp.graphnode.controller.GraphRelationshipRestController;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphRelationshipCollection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class GraphRelationshipRestControllerTest extends DagControllerTestCase {

	@Autowired
	private GraphRelationshipRestController graphRelationshipRestController;

	private GraphNode firstNode;
	private GraphNode secondNode;

	
	public void setUp() {
		
		super.setUp();

		firstNode = GraphNodeFixture.createSavedGraphNode("firstNode");
		secondNode = GraphNodeFixture.createSavedGraphNode("secondNode");
		flush();

		GraphRelationshipFixture.createSavedRelationship(
				firstNode,
				secondNode,
				"specialRelationship");

		flush();
	}
	
	@Test
	public void testFetchRelationships() throws Exception {
		
		MockMvc mockMvc = generateMockMvcGet(graphRelationshipRestController, "/api/relationships");
		
		ResultActions result = mockMvc.perform(get("/api/relationships"));
		MvcResult mvcResult = result.andReturn();
		String jsonString = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");
		
		assertEquals("application/json; charset=utf-8", contentType);
		
		GraphRelationshipCollection collection = super.objectMapper.readValue(jsonString, GraphRelationshipCollection.class);
		
		assertEquals(1, collection.getItems().size());
		
		List<GraphRelationshipSnapshot> snapshots = collection.getItems();

		GraphRelationshipSnapshot snapshot = snapshots.get(0);
	}
	
	@Test
	public void saveRelationship() throws Exception {
		
		MockMvc mockMvc = generateMockMvcPost(graphRelationshipRestController, "/api/relationships/");

		GraphRelationshipSnapshot snapshot = new GraphRelationshipSnapshot();
		snapshot.setFromNodeName("firstNode");
		snapshot.setToNodeName("secondNode");
		snapshot.getDetail().setName("AlphaRelationship");

		String jsonString = objectMapper.writeValueAsString(snapshot);

		ResultActions result = mockMvc.perform(post("/api/relationships/").content(jsonString));
		MvcResult mvcResult = result.andReturn();
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");

		TransactionResult restResult = objectMapper.readValue(jsonStringResponse, TransactionResult.class);
		assertTrue(restResult.wasSuccessful());
	}
	
	@Test
	public void saveRelationships() throws Exception {
		
		MockMvc mockMvc = generateMockMvcPut(graphRelationshipRestController, "/api/relationships/");


		GraphRelationshipSnapshot snapshot = new GraphRelationshipSnapshot();
		snapshot.setFromNodeName("firstNode");
		snapshot.setToNodeName("secondNode");
		snapshot.getDetail().setName("AlphaRelationship");

		List<GraphRelationshipSnapshot> snapshots = new ArrayList<>();
		snapshots.add(snapshot);
		
		String jsonString = objectMapper.writeValueAsString(snapshots);

		ResultActions result = mockMvc.perform(put("/api/relationships/").content(jsonString));
		MvcResult mvcResult = result.andReturn();
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");

		TransactionResult restResult = objectMapper.readValue(jsonStringResponse, TransactionResult.class);
		assertTrue(restResult.wasSuccessful());
	}
}
