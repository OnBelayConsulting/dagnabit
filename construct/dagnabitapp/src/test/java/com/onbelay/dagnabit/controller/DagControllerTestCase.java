package com.onbelay.dagnabit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onbelay.dagnabit.common.DagnabitSpringTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public abstract class DagControllerTestCase extends DagnabitSpringTestCase{


	@Autowired
	protected ObjectMapper objectMapper;

	
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
	

	public void setUp() {
		super.setUp();
		mappingJackson2HttpMessageConverter = new  MappingJackson2HttpMessageConverter();
		
	    mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);

		
	}

	protected MockMvc generateMockMvc(Object restController,
			  RequestBuilder requestBuilder){
		return MockMvcBuilders.standaloneSetup(restController)
		.defaultRequest(requestBuilder)
		.build();

	}

	protected MockMvc generateMockMvcGet(Object restController,
					 String urlTemplate){
	
	
			return MockMvcBuilders.standaloneSetup(restController)
			.defaultRequest(MockMvcRequestBuilders.get(urlTemplate)
			.accept(MediaType.APPLICATION_JSON))
			.setMessageConverters(mappingJackson2HttpMessageConverter)
			.build();
	
	}

	protected MockMvc generateMockMvcPut(Object restController,
					 String urlTemplate){
			return MockMvcBuilders.standaloneSetup(restController)
			.defaultRequest(put(urlTemplate)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.setMessageConverters(mappingJackson2HttpMessageConverter)
			.build();
	
	}

	protected MockMvc generateMockMvcPost(Object restController,
				 String urlTemplate){
		return MockMvcBuilders.standaloneSetup(restController)
		.defaultRequest(post(urlTemplate)
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON))
		.setMessageConverters(mappingJackson2HttpMessageConverter)
		.build();
		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}


}
