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
package com.onbelay.dagnabit.graph.model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.onbelay.dagnabit.graph.factories.DagModelFactory;

/**
 * Test the basic methods to build a graph model and the methods to interrogate it.
 * @author lefeu
 *
 */
public class DagModelTest  {
	private static Logger logger = LoggerFactory.getLogger(DagModelTest.class);

	private DagModelFactory factory = new DagModelFactory();
	private DagModel model;
	
	@Before
	public void beforeRun() throws Throwable {
		model = factory.newModel();

		// Solitary nodes
		model.addNode("S", "special");
		model.addNode("R", "ordinary");

		
		model.addNode("A");
		
		model.addNode("B");
		
		model.addNode("C");
		
		model.addNode("D");

		
		model.addRelationship(
				model.getNode("A"), 
				"benchesTo", 
				model.getNode("D"));
		
		
		model.addRelationship(
				model.getNode("A"), 
				"benchesTo", 
				model.getNode("B"));
		
		
		model.addRelationship(
				model.getNode("B"), 
				"benchesTo", 
				model.getNode("C"));
		
		
		model.addRelationship(
				model.getNode("A"), 
				"basisTo", 
				model.getNode("C"));
		
		
		model.addRelationship(
				model.getNode("C"), 
				"basisTo", 
				model.getNode("D"));
		
		model.addNode("V");
		model.addNode("Y");
		
		
		model.addRelationship(
				model.getNode("V"), 
				"benchesTo", 
				model.getNode("Y"));

	}
	
	
	@Test
	public void testGetNodes() {
		
		assertEquals(8, model.getNodes().size());
	}
	
	
	@Test
	/*
	 * Note this will find the default link type as well.
	 */
	public void testFindLinkTypes() {
		
		List<DagLinkType> linkTypes = model.getLinkTypes();
		assertEquals(3, linkTypes.size());
		
		assertEquals(1, linkTypes.stream().filter( n -> n.getName().equals("benchesTo")).collect(Collectors.toList()).size());
		
		assertEquals(1, linkTypes.stream().filter( n -> n.getName().equals("basisTo")).collect(Collectors.toList()).size());
		
		assertEquals(1, linkTypes.stream().filter( n -> n.getName().equals("link")).collect(Collectors.toList()).size());
	}
	
	@Test
	public void testFindNodeTypes() {
		List<DagNodeType> nodeTypes = model.getNodeTypes();
		assertEquals(3, nodeTypes.size());
		
		assertEquals(1, nodeTypes.stream().filter( n -> n.getTypeName().equals("special")).collect(Collectors.toList()).size());
		
		assertEquals(1, nodeTypes.stream().filter( n -> n.getTypeName().equals("ordinary")).collect(Collectors.toList()).size());
		
		assertEquals(1, nodeTypes.stream().filter( n -> n.getTypeName().equals("node")).collect(Collectors.toList()).size());
	}


	@Test
	public void testFindSolitaryNodes() {
		List<DagNode> solitaryNodes = model.findSolitaryNodes();
		assertEquals(2, solitaryNodes.size());
		
		assertEquals(1, solitaryNodes.stream().filter( n -> n.getName().equals("R")).collect(Collectors.toList()).size());

		assertEquals(1, solitaryNodes.stream().filter( n -> n.getName().equals("S")).collect(Collectors.toList()).size());
	}
	
	@Test
	public void testFindRootNodes() {
		List<DagNode> rootNodes = model.findRootNodes();
		assertEquals(2, rootNodes.size());
		
		assertEquals(1, rootNodes.stream().filter( n -> n.getName().equals("A")).collect(Collectors.toList()).size());

		assertEquals(1, rootNodes.stream().filter( n -> n.getName().equals("V")).collect(Collectors.toList()).size());
		
	}
	
	@Test
	public void testFindLeafNodes() {
		List<DagNode> leafNodes = model.findLeafNodes();
		assertEquals(2, leafNodes.size());
		
		assertEquals(1, leafNodes.stream().filter( n -> n.getName().equals("D")).collect(Collectors.toList()).size());

		assertEquals(1, leafNodes.stream().filter( n -> n.getName().equals("Y")).collect(Collectors.toList()).size());
	}
	
}
