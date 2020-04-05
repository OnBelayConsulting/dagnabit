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
package com.onbelay.dagnabit.graph.serviceimpl;

import java.util.List;

import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagModelImpl;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshot;

/**
 * Builds a DagModel from lists of nodes and links.
 * @author lefeu
 *
 */
public class DagNodeModelBuilder {
	
	private DagModel model = new DagModelImpl();
	
	public void addNodes(List<GraphNodeSnapshot> nodes) {
		
		for (GraphNodeSnapshot s : nodes) {
			model.addNode(s.getDetail().getName(), s.getDetail().getNodeType());
		}

	}
	
	public void addLinks(List<GraphLinkSnapshot> links) {
		
		for (GraphLinkSnapshot link : links) {
			
			DagNode fromNode = model.getNode(link.getFromGraphNodeName());
			DagNode toNode = model.getNode(link.getToGraphNodeName());
			
			model.addRelationship(
					fromNode, 
					link.getDetail().getName(), 
					toNode);
			
		}
		
	}
	
	public DagModel getModel() {
		return model;
	}

}
