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
package com.onbelay.dagnabit.graph.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagNode;

public class DagNodeSearchState {
    
    private Map<String, DagNodeImpl> visited = new HashMap<String, DagNodeImpl>();
    private DagLinkType dagLinkType;
    private DagNodeVector vector;
    private List<DagNodeVector> vectors = new ArrayList<DagNodeVector>();
    private List<DagNodeVector> cycles = new ArrayList<DagNodeVector>();
    
    private DagNode endingNode;
    
    private DagNodeImpl currentNode;

    public DagNodeSearchState(
    		DagLinkType dagLinkType, 
    		DagNodeImpl currentNode) {
    	
    	this.dagLinkType = dagLinkType;
        this.currentNode = currentNode;
        visited.put(currentNode.getName(), currentNode);
    }
    
    public DagNodeSearchState(
    		DagLinkType dagLinkType, 
    		DagNodeImpl currentNode,
    		DagNode endingNode) {
    	
    	this.endingNode = endingNode;
    	this.dagLinkType = dagLinkType;
        this.currentNode = currentNode;
        visited.put(currentNode.getName(), currentNode);
    }
    
    public DagNodeSearchState(
    		DagNodeSearchState copy, 
    		DagNodeImpl currentNode, 
    		DagNodeConnector relationship) {
    	
    	this.endingNode = copy.endingNode;
    	this.dagLinkType = copy.dagLinkType;
        this.currentNode = currentNode;
        this.visited.putAll(copy.visited);
        this.vectors = copy.vectors;
        this.vector = new DagNodeVector(copy.vector);
        addNodeRelationshipLink(relationship);
        visited.put(currentNode.getName(), currentNode);
        this.cycles = copy.cycles;
    }

	public boolean isCyclic() {
    	return cycles.size() > 0;
    }
    
    public Map<String, DagNodeImpl> getVisited() {
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

    public DagNodeImpl getCurrentNode() {
        return currentNode;
    }
    
    public void setCurrentNode(DagNodeImpl node) {
        this.currentNode = node;
    }

	public List<DagNodeVector> getVectors() {
        return vectors;
    }
    
    public void fixCurrentVector(DagNodeConnector connector) {
    	if (vector != null) {
    		vector.add(connector);
    		vectors.add(vector);
    	} else {
	    	vector = new DagNodeVector();
	        vector.add(connector);
	        vectors.add(vector);
    	}
    }

    public void fixCurrentVector() {
        if (vector != null)
            vectors.add(vector);
        
    }
    
    public boolean hasVisited(String code) {
        return visited.get(code) != null;
    }
    
    public void addCycle(DagNodeVector currentVector, DagNodeConnector connector, DagLinkType linkType) {
    	DagNodeVector cyclicVector = new DagNodeVector(currentVector);
    	cyclicVector.add(connector);
        cycles.add(cyclicVector);
    }
    
    public void addCycle(DagNodeVector currentVector, DagNodeConnector connector) {
    	DagNodeVector cyclicVector = new DagNodeVector(currentVector);
    	cyclicVector.add(connector);
        cycles.add(cyclicVector);
    }

	public List<DagNodeVector> getCycles() {
        return cycles;
    }

    public void setCycles(ArrayList<DagNodeVector> cycles) {
        this.cycles = cycles;
    }

	public DagLinkType getDagLinkType() {
		return dagLinkType;
	}

	public DagNode getEndingNode() {
		return endingNode;
	}

}
