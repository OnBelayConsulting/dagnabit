package com.onbelay.dagnabit.graph.controller;

import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.controller.DagControllerTestCase;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graphnode.factory.DagModelFactory;
import com.onbelay.dagnabit.graphnode.model.GraphNodeFixture;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabitapp.graph.controller.GraphingRestController;
import com.onbelay.dagnabitapp.graph.snapshot.DagNodeCollection;
import com.onbelay.dagnabitapp.graph.snapshot.DagNodeSnapshot;
import com.onbelay.dagnabitapp.graph.snapshot.GraphModelCollection;
import com.onbelay.dagnabitapp.graph.snapshot.GraphModelSnapshot;
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

public class GraphingRestControllerTest extends DagControllerTestCase {

	@Autowired
	private GraphingRestController graphingRestController;

	@Autowired
	private DagModelFactory dagModelFactory;


	private DagModel dagModel;

	public void setUp() {
		
		super.setUp();
		dagModel = dagModelFactory.newModel("myModel");

		dagModel.addNode("firstNode", "family");
		dagModel.addNode("secondNode", "family");
		dagModel.addNode("thirdNode", "family");
		dagModel.addNode("fourthNode", "family");
		dagModel.addNode("fifthNode", "family");

		dagModel.addRelationship(
				dagModel.getNode("firstNode"),
				"parentOf",
				dagModel.getNode("secondNode"));
		dagModel.addRelationship(
				dagModel.getNode("firstNode"),
				"parentOf",
				dagModel.getNode("thirdNode"));
		dagModel.addRelationship(
				dagModel.getNode("secondNode"),
				"parentOf",
				dagModel.getNode("fourthNode"));
		dagModel.addRelationship(
				dagModel.getNode("secondNode"),
				"parentOf",
				dagModel.getNode("fifthNode"));


		flush();
	}

	@Override
	public void afterRun() throws Throwable {
		super.afterRun();
		dagModelFactory.cleanUp();
	}

	@Test
	public void testFetchModels() throws Exception {
		
		MockMvc mockMvc = generateMockMvcGet(graphingRestController, "/api/graphModels");
		
		ResultActions result = mockMvc.perform(get("/api/graphModels"));
		MvcResult mvcResult = result.andReturn();
		String jsonString = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");
		
		assertEquals("application/json; charset=utf-8", contentType);
		
		GraphModelCollection collection = super.objectMapper.readValue(jsonString, GraphModelCollection.class);
		
		assertEquals(1, collection.getItems().size());
		
		List<GraphModelSnapshot> snapshots = collection.getItems();

		GraphModelSnapshot snapshot = snapshots.get(0);
		assertEquals("myModel", snapshot.getName());
	}



	@Test
	public void fetchDescendents() throws Exception {

		MockMvc mockMvc = generateMockMvcGet(graphingRestController, "/api/graphModels");

		ResultActions result = mockMvc.perform(get("/api/graphModels/myModel/firstNode/parentOf/descendents"));
		MvcResult mvcResult = result.andReturn();
		String jsonString = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");

		assertEquals("application/json; charset=utf-8", contentType);

		DagNodeCollection collection = super.objectMapper.readValue(jsonString, DagNodeCollection.class);

		assertEquals(4, collection.getItems().size());

		List<DagNodeSnapshot> snapshots = collection.getItems();

		DagNodeSnapshot snapshot = snapshots.get(0);
		assertEquals("secondNode", snapshot.getName());
	}

	@Test
	public void createModel() throws Exception {
		
		MockMvc mockMvc = generateMockMvcPost(graphingRestController, "/api/graphModels/");

		GraphModelSnapshot snapshot = new GraphModelSnapshot();

		snapshot.setName("AlphaModel");

		String jsonString = objectMapper.writeValueAsString(snapshot);

		ResultActions result = mockMvc.perform(post("/api/graphModels/").content(jsonString));
		MvcResult mvcResult = result.andReturn();
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");

		TransactionResult restResult = objectMapper.readValue(jsonStringResponse, TransactionResult.class);
		assertTrue(restResult.wasSuccessful());
	}
	
	@Test
	public void addNodes() throws Exception {
		
		MockMvc mockMvc = generateMockMvcPut(graphingRestController, "/api/graphModels/myModel/nodes");

		ArrayList<DagNodeSnapshot> snapshots = new ArrayList<>();

		DagNodeSnapshot snapshot = new DagNodeSnapshot();

		snapshot.setName("AlphaNode");
		snapshot.setCategory("Network");
		snapshots.add(snapshot);
		
		String jsonString = objectMapper.writeValueAsString(snapshots);

		ResultActions result = mockMvc.perform(post("/api/graphModels/myModel/nodes").content(jsonString));
		MvcResult mvcResult = result.andReturn();
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");

		TransactionResult restResult = objectMapper.readValue(jsonStringResponse, TransactionResult.class);
		assertTrue(restResult.wasSuccessful());
	}
}
