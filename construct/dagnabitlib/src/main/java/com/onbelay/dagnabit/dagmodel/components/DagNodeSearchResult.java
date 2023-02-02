package com.onbelay.dagnabit.dagmodel.components;

import java.util.ArrayList;
import java.util.List;

import com.onbelay.dagnabit.dagmodel.model.DagRelationshipType;
import com.onbelay.dagnabit.dagmodel.model.DagNode;
import com.onbelay.dagnabit.dagmodel.model.DagNodePath;
import com.onbelay.dagnabit.dagmodel.model.NodeSearchResult;

public class DagNodeSearchResult implements NodeSearchResult {

	private DagNode rootNode;
	private DagRelationshipType relationshipType;
	
	private List<DagNodePath> paths = new ArrayList<>(); 
	
	private List<DagNodePath> cycles = new ArrayList<DagNodePath>();

	public DagNodeSearchResult(
			DagRelationshipType relationshipType,
			DagNode rootNode) {

		super();
		this.relationshipType = relationshipType;
		this.rootNode = rootNode;
	}

	@Override
	public DagNode getRootNode() {
		return rootNode;
	}

	@Override
	public DagRelationshipType getRelationshipType() {
		return relationshipType;
	}

	@Override
	public List<DagNodePath> getCycles() {
		return cycles;
	}
	
	public void addCycle(DagNodePath cycle) {
		cycles.add(cycle);
	}
	

	@Override
	public List<DagNodePath> getPaths() {
		return paths;
	}

	public void addPaths(List<DagNodePath> paths) {
		this.paths.addAll(paths);
	}

	@Override
	public boolean isCyclic() {
		return cycles.isEmpty() == false;
	}

	
}
