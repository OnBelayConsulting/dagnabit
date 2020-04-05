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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DagNodeSearchResult implements NodeSearchResult{
    
    private Map<String, DagNode> visited = new HashMap<String, DagNode>();
    private DagLinkType dagLinkType;
    private DagNodeType dagNodeType;
    private DagNodeVector vector;
    private List<DagNodeVector> vectors = new ArrayList<DagNodeVector>();
    private List<DagNodeConnector> cycles = new ArrayList<DagNodeConnector>();
    
    private NodeSearchResult backChainedNodeSearchResult;
    
    private DagNode currentNode;

    public DagNodeSearchResult(
    		DagNodeType dagNodeType,
    		DagLinkType dagLinkType, 
    		DagNode currentNode) {
    	
    	this.dagNodeType = dagNodeType;
    	this.dagLinkType = dagLinkType;
        this.currentNode = currentNode;
        visited.put(currentNode.getName(), currentNode);
    }
    
    public DagNodeSearchResult(
    		DagNodeSearchResult copy, 
    		DagNode currentNode, 
    		DagNodeConnector relationship) {
    	
    	this.dagNodeType = copy.dagNodeType;
    	this.dagLinkType = copy.dagLinkType;
        this.currentNode = currentNode;
        this.visited.putAll(copy.visited);
        this.vectors = copy.vectors;
        this.vector = new DagNodeVector(copy.vector);
        addNodeRelationshipLink(relationship);
        visited.put(currentNode.getName(), currentNode);
        this.cycles = copy.cycles;
    }

    @Override
	public boolean isCyclic() {
    	return cycles.size() > 0;
    }
    
    public Map<String, DagNode> getVisited() {
        return visited;
    }

    public void addNodeRelationshipLink(DagNodeConnector connector) {
        if (vector == null)
            vector = new DagNodeVector();
        vector.add(connector);
    }
    
    public DagNodeVector getVector() {
        return vector;
    }

    public DagNode getCurrentNode() {
        return currentNode;
    }
    
    public void setCurrentNode(DagNode node) {
        this.currentNode = node;
    }

    @Override
	public List<DagNodeVector> getVectors() {
        return vectors;
    }
    
    public void fixCurrentVector(DagNodeConnector connector) {
        if (vector == null)
            vector = new DagNodeVector();
        vector.add(connector);
        vectors.add(vector);
    }

    public void fixCurrentVector() {
        if (vector != null)
            vectors.add(vector);
        
    }
    
    public boolean hasVisited(String code) {
        return visited.get(code) != null;
    }
    
    public void addCycle(DagNodeConnector connector, DagLinkType linkType) {
    	connector.markAsCyclic(linkType);
        cycles.add(connector);
    }
    
    public void addCycle(DagNodeConnector connector) {
    	connector.markAsCyclic();
        cycles.add(connector);
    }

    @Override
	public List<DagNodeConnector> getCycles() {
        return cycles;
    }

    public void setCycles(ArrayList<DagNodeConnector> cycles) {
        this.cycles = cycles;
    }

	public NodeSearchResult getBackChainedNodeSearchResult() {
		return backChainedNodeSearchResult;
	}

	public void setBackChainedNodeSearchResult(NodeSearchResult backChainedNodeSearchResult) {
		this.backChainedNodeSearchResult = backChainedNodeSearchResult;
	}

	public DagLinkType getDagLinkType() {
		return dagLinkType;
	}

	public DagNodeType getDagNodeType() {
		return dagNodeType;
	}

}
