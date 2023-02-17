package com.onbelay.dagnabit.dagmodel.controller;

import com.onbelay.dagnabit.controller.DagControllerTestCase;
import com.onbelay.dagnabit.dagmodel.factory.DagModelFactory;
import com.onbelay.dagnabit.dagmodel.model.DagModel;
import com.onbelay.dagnabitapp.dagmodel.controller.DagModelRestController;
import com.onbelay.dagnabitapp.dagmodel.snapshot.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WithMockUser
public class DagModelRestControllerTest extends DagControllerTestCase {

	@Autowired
	private DagModelRestController dagModelRestController;

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
		
		MockMvc mockMvc = generateMockMvcGet(dagModelRestController, "/api/models");
		
		ResultActions result = mockMvc.perform(get("/api/models"));
		MvcResult mvcResult = result.andReturn();
		String jsonString = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");
		
		assertEquals("application/json; charset=utf-8", contentType);
		
		DagModelCollection collection = super.objectMapper.readValue(jsonString, DagModelCollection.class);
		
		assertEquals(1, collection.getSnapshots().size());
		
		List<DagModelSnapshot> snapshots = collection.getSnapshots();

		DagModelSnapshot snapshot = snapshots.get(0);
		assertEquals("myModel", snapshot.getName());
	}



	@Test
	public void fetchDescendants() throws Exception {

		MockMvc mockMvc = generateMockMvcGet(dagModelRestController, "/api/models");

		ResultActions result = mockMvc.perform(get("/api/models/myModel/firstNode/parentOf/descendants"));
		MvcResult mvcResult = result.andReturn();
		String jsonString = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");

		assertEquals("application/json; charset=utf-8", contentType);

		DagNodeCollection collection = super.objectMapper.readValue(jsonString, DagNodeCollection.class);

		assertEquals(4, collection.getSnapshots().size());

		List<DagNodeSnapshot> snapshots = collection.getSnapshots();

		DagNodeSnapshot snapshot = snapshots.get(0);
		assertEquals("secondNode", snapshot.getName());
	}

	@Test
	public void createModel() throws Exception {
		
		MockMvc mockMvc = generateMockMvcPost(dagModelRestController, "/api/models/");

		DagModelSnapshot snapshot = new DagModelSnapshot();

		snapshot.setName("AlphaModel");

		String jsonString = objectMapper.writeValueAsString(snapshot);

		ResultActions result = mockMvc.perform(post("/api/models/").content(jsonString));
		MvcResult mvcResult = result.andReturn();
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");

		ModelResult modelResult = objectMapper.readValue(jsonStringResponse, ModelResult.class);
		assertTrue(modelResult.isSuccessful());
	}
	
	@Test
	public void addNodes() throws Exception {
		
		MockMvc mockMvc = generateMockMvcPut(dagModelRestController, "/api/models/myModel/nodes");

		ArrayList<DagNodeSnapshot> snapshots = new ArrayList<>();

		DagNodeSnapshot snapshot = new DagNodeSnapshot();

		snapshot.setName("AlphaNode");
		snapshot.setCategory("Network");
		snapshots.add(snapshot);
		
		String jsonString = objectMapper.writeValueAsString(snapshots);

		ResultActions result = mockMvc.perform(post("/api/models/myModel/nodes").content(jsonString));
		MvcResult mvcResult = result.andReturn();
		String jsonStringResponse = mvcResult.getResponse().getContentAsString();
		String contentType = mvcResult.getResponse().getHeader("Content-type");

		ModelResult modelResult = objectMapper.readValue(jsonStringResponse, ModelResult.class);
		assertTrue(modelResult.isSuccessful());
	}
}
