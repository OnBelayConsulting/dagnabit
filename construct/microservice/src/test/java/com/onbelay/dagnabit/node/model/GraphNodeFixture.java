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

import java.util.ArrayList;
import java.util.List;

import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshot;

public class GraphNodeFixture {

	
	public static GraphNode createNode(String name) {
		GraphNode graphNode = GraphNode.create(name, name);
		return graphNode;
	}
	
	public static GraphNode createNode(String name, String type) {
		GraphNode graphNode = GraphNode.create(
				name,
				type,
				name);
		return graphNode;
	}
	
	
	public static GraphNodeSnapshot createNodeSnapshot(String name, String type) {
		return  new GraphNodeSnapshot(name, type, name);
	}
	
	
	
	public static List<GraphNode> createMultipleNodes(String prefix, int totalToCreate) {
		
		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
		for (int i=0; i < totalToCreate; i++) {
			nodes.add(createNode(prefix + "_" + i) );
		}
		return nodes;
	}
	
	public static List<GraphNode> createMultipleNodes(String prefix, String type, int totalToCreate) {
		
		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
		for (int i=0; i < totalToCreate; i++) {
			nodes.add(createNode(prefix + "_" + i, type) );
		}
		return nodes;
	}
	
	public static List<GraphNodeSnapshot> createMultipleNodeSnapshots(String prefix, String type, int totalToCreate) {
		
		ArrayList<GraphNodeSnapshot> nodes = new ArrayList<GraphNodeSnapshot>();
		for (int i=0; i < totalToCreate; i++) {
			nodes.add(createNodeSnapshot(prefix + "_" + i, type) );
		}
		return nodes;
	}
	
	
}
