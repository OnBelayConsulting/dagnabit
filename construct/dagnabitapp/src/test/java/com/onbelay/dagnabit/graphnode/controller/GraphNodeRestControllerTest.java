package com.onbelay.dagnabit.graphnode.controller;

import com.onbelay.core.entity.snapshot.TransactionResult;
import com.onbelay.dagnabit.controller.DagControllerTestCase;
import com.onbelay.dagnabit.graphnode.model.GraphNodeFixture;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabitapp.graphnode.controller.GraphNodeRestController;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphNodeCollection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class GraphNodeRestControllerTest extends DagControllerTestCase {

	@Autowired
	private GraphNodeRestController graphNodeRestController;
	
	
	public void setUp() {
		
		super.setUp();

		GraphNodeFixture.createSavedGraphNode("specialNode");

		flush();
	}
	
	@Test
	public void testFetchNodes() throws Exception {
		
		MockMvc mockMvc = generateMockMvcGet(graphNodeRestController, "/api/nodes");
		
		ResultActions result = mockMvc.perform(get("/api/nodes"));
		MvcResult mvcResult = result.andReturn();
		String jsonString = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");
		
		assertEquals("application/json; charset=utf-8", contentType);
		
		GraphNodeCollection collection = super.objectMapper.readValue(jsonString, GraphNodeCollection.class);
		
		assertEquals(1, collection.getSnapshots().size());
		
		List<GraphNodeSnapshot> snapshots = collection.getSnapshots();

		GraphNodeSnapshot snapshot = snapshots.get(0);
	}
	
	@Test
	public void saveNode() throws Exception {
		
		MockMvc mockMvc = generateMockMvcPost(graphNodeRestController, "/api/nodes/");

		GraphNodeSnapshot snapshot = new GraphNodeSnapshot();

		snapshot.getDetail().setName("AlphaNode");
		snapshot.getDetail().setCategory("Network");

		String jsonString = objectMapper.writeValueAsString(snapshot);

		ResultActions result = mockMvc.perform(post("/api/nodes/").content(jsonString));
		MvcResult mvcResult = result.andReturn();
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");

		TransactionResult restResult = objectMapper.readValue(jsonStringResponse, TransactionResult.class);
		assertTrue(restResult.isSuccessful());
	}
	
	@Test
	public void saveNodes() throws Exception {
		
		MockMvc mockMvc = generateMockMvcPut(graphNodeRestController, "/api/nodes/");


		GraphNodeSnapshot snapshot = new GraphNodeSnapshot();

		snapshot.getDetail().setName("AlphaNode");
		snapshot.getDetail().setCategory("Network");

		List<GraphNodeSnapshot> snapshots = new ArrayList<>();
		snapshots.add(snapshot);
		
		String jsonString = objectMapper.writeValueAsString(snapshots);

		ResultActions result = mockMvc.perform(put("/api/nodes/").content(jsonString));
		MvcResult mvcResult = result.andReturn();
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");

		TransactionResult restResult = objectMapper.readValue(jsonStringResponse, TransactionResult.class);
		assertTrue(restResult.isSuccessful());
	}
}
