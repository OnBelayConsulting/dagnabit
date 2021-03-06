package com.onbelay.dagnabit.graph.components;

import java.util.ArrayList;
import java.util.List;

import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.NodeSearchResult;

public class DagNodeSearchResult implements NodeSearchResult {

	private DagNode rootNode;
	private DagLinkType linkType;
	
	private List<DagNodePath> paths = new ArrayList<>(); 
	
	private List<DagNodePath> cycles = new ArrayList<DagNodePath>();

	public DagNodeSearchResult(DagLinkType linkType, DagNode rootNode) {
		super();
		this.linkType = linkType;
		this.rootNode = rootNode;
	}

	@Override
	public DagNode getRootNode() {
		return rootNode;
	}

	@Override
	public DagLinkType getDagLinkType() {
		return linkType;
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
