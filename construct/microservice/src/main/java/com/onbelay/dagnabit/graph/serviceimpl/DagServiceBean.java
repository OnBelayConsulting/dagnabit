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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNodeConnector;
import com.onbelay.dagnabit.graph.model.DagNodeNavigator;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.LinkAnalysis;
import com.onbelay.dagnabit.graph.service.DagModelManager;
import com.onbelay.dagnabit.graph.service.DagService;
import com.onbelay.dagnabit.graph.snapshot.AnalysisSnapshot;
import com.onbelay.dagnabit.graph.snapshot.DagLinkSnapshot;
import com.onbelay.dagnabit.graph.snapshot.DagNavigationCriteria;
import com.onbelay.dagnabit.graph.snapshot.DagNodeSnapshot;
import com.onbelay.dagnabit.graph.snapshot.DagPathSnapshot;
import com.onbelay.dagnabit.node.services.GraphLinkService;
import com.onbelay.dagnabit.node.services.GraphNodeService;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshotCollection;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshotCollection;

@Service(value="dagService")
@Transactional
public class DagServiceBean implements DagService {

	@Autowired
	private DagModelManager dagModelManager;
	
	@Autowired
	private GraphNodeService graphNodeService;
	
	@Autowired
	private GraphLinkService graphLinkService;
	
	@Override
	public String loadModel() {
		GraphNodeSnapshotCollection nodesCollection = graphNodeService.findAll();
		GraphLinkSnapshotCollection linksCollection = graphLinkService.findAll();
		
		DagNodeModelBuilder builder = new DagNodeModelBuilder();
		builder.addNodes(nodesCollection.getSnapshots());
		builder.addLinks(linksCollection.getSnapshots());
		
		UUID handle = UUID.randomUUID();
		
		dagModelManager.addModel(
				handle.toString(),
				builder.getModel());
		
		return handle.toString();
	}
	
	@Override
	public String loadModel(String categoryName) {
		GraphNodeSnapshotCollection nodesCollection = graphNodeService.findByCategory(categoryName);
		GraphLinkSnapshotCollection linksCollection = graphLinkService.findByCategory(categoryName);
		
		DagNodeModelBuilder builder = new DagNodeModelBuilder();
		builder.addNodes(nodesCollection.getSnapshots());
		builder.addLinks(linksCollection.getSnapshots());
		
		UUID handle = UUID.randomUUID();
		
		dagModelManager.addModel(
				handle.toString(),
				builder.getModel());
		
		return handle.toString();
	}
	
	@Override
	public String createModel(
			List<GraphNodeSnapshot> nodes, 
			List<GraphLinkSnapshot> links) {
		
		DagNodeModelBuilder builder = new DagNodeModelBuilder();
		builder.addNodes(nodes);
		builder.addLinks(links);
		
		UUID handle = UUID.randomUUID();
		
		dagModelManager.addModel(
				handle.toString(),
				builder.getModel());
		
		return handle.toString();
	}

	@Override
	public void forgetModel(String handle) {
		dagModelManager.forgetModel(handle);
		
	}

	@Override
	public List<DagNodeSnapshot> fetchRootNodes(String handle) {
		
		DagModel model = dagModelManager.getModel(handle);
		
		return model
				.findRootNodes()
				.stream()
				.map(x -> new DagNodeSnapshot(x.getName(), x.getNodeType().getTypeName()))
				.collect(Collectors.toList());
	}

	@Override
	public List<DagNodeSnapshot> fetchLeafNodes(String handle) {
		
		DagModel model = dagModelManager.getModel(handle);
		
		return model
				.findLeafNodes()
				.stream()
				.map(x -> new DagNodeSnapshot(x.getName(), x.getNodeType().getTypeName()))
				.collect(Collectors.toList());
	}

	@Override
	public List<DagNodeSnapshot> fetchSolitaryNodes(String handle) {
		
		DagModel model = dagModelManager.getModel(handle);
		
		return model
				.findSolitaryNodes()
				.stream()
				.map(x -> new DagNodeSnapshot(x.getName(), x.getNodeType().getTypeName()))
				.collect(Collectors.toList());
	}

	@Override
	public AnalysisSnapshot analyse(String handle) {
		DagModel model = dagModelManager.getModel(handle);
		
		LinkAnalysis analysis = model.analyse().result();
		
		AnalysisSnapshot analysisSnapshot = new  AnalysisSnapshot();
		analysisSnapshot.setCyclic(analysis.isCyclic());
		
		if (analysis.isCyclic()) {
			ArrayList<DagPathSnapshot> paths = new ArrayList<DagPathSnapshot>();
			for (DagNodeConnector connector : analysis.getCycles()) {
				DagPathSnapshot path = new DagPathSnapshot();
				path.addLink(
						new DagLinkSnapshot(
								connector.getFromNode().getName(),
								connector.getToNode().getName()));
				paths.add(path);
			}
			analysisSnapshot.setCycles(paths);
		}
		
		
		
		return null;
	}

	@Override
	public AnalysisSnapshot analyse(String handle, String linkType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DagPathSnapshot> navigate(String handle, DagNavigationCriteria criteria) {
		DagModel model = dagModelManager.getModel(handle);
		
		DagNodeNavigator navigator = model.navigate().from(model.getNode(criteria.getFromNodeName()));
		
		if (criteria.getLinkType() != null)
			navigator.by(
					model.getLinkType(
							criteria.getLinkType()));
		
		if (criteria.getNodeType() != null)
			navigator.forOnly(
					model.getNodeType(
							criteria.getNodeType()));
		
		if (criteria.getToNodeName() != null)
			navigator.to(
					model.getNode(
							criteria.getToNodeName()));
		
		ArrayList<DagPathSnapshot> paths = new ArrayList<DagPathSnapshot>();
		
		for (DagNodePath nodePath : navigator.paths()) {
			
			DagPathSnapshot path = new  DagPathSnapshot();
			
			for (DagNodeConnector c : nodePath.getConnectors()) {
				
				if (criteria.getLinkType() != null) {
					DagLink link = c.getRelationship(
							model.getLinkType(criteria.getLinkType()));
					path.addLink(
							new DagLinkSnapshot(
									link.getName(),
									link.getType().getName(),
									c.getFromNode().getName(),
									c.getToNode().getName()));
				} else {
					path.addLink(
							new DagLinkSnapshot(
									c.getFromNode().getName(),
									c.getToNode().getName()));
				}
			}
			paths.add(path);
					
		}
		
		return paths;
	}
	
	

	
	
}
