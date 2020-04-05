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
package com.onbelay.dagnabit.node.model;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.onbelay.dagnabit.common.component.ApplicationContextFactory;
import com.onbelay.dagnabit.node.repository.GraphNodeRepository;
import com.onbelay.dagnabit.persistence.TransactionalSpringTestCase;
@ComponentScan("com.onbelay.*")
@TestPropertySource( locations="classpath:application-integrationtest.properties")
@RunWith(SpringRunner.class)
@SpringBootTest

public class GraphNodeTest extends TransactionalSpringTestCase {
	
	private GraphNodeRepository graphNodeRepository;

	@Override
	public void beforeRun() throws Throwable {
		super.beforeRun();
		graphNodeRepository = (GraphNodeRepository) ApplicationContextFactory.getBean(GraphNodeRepository.BEAN_NAME);
	}

	@Test
	public void testCreateNode() {

		GraphNode.create(
				"nodeName",
				"people",
				"nodeDescription");
		
		flush();
		clearCache();
		
		List<GraphNode> nodes = graphNodeRepository.findAll();
		assertEquals(1, nodes.size());
		
		assertEquals("nodeName", nodes.get(0).getDetail().getName());
		assertEquals("people", nodes.get(0).getDetail().getNodeType());
		
		flush();
		
	}
	
	@Test
	public void testCreateNodeWithDefault() {

		GraphNode.create(
				"nodeName",
				"nodeDescription");
		
		flush();
		clearCache();
		
		List<GraphNode> nodes = graphNodeRepository.findAll();
		assertEquals(1, nodes.size());
		
		assertEquals("nodeName", nodes.get(0).getDetail().getName());
		assertEquals("node", nodes.get(0).getDetail().getNodeType());
		
		flush();
		
	}
	
}
