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
package com.onbelay.dagnabit.dagmodel.components;

import com.onbelay.dagnabit.dagmodel.model.DagNode;
import com.onbelay.dagnabit.dagmodel.model.DagRelationshipType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DagNodeSearchState {
    
    private Map<String, DagNodeImpl> visited = new HashMap<String, DagNodeImpl>();
    private DagRelationshipType dagRelationshipType;
    private DagNodeVector vector;
    private List<DagNodeVector> vectors = new ArrayList<DagNodeVector>();
    private List<DagNodeVector> cycles = new ArrayList<DagNodeVector>();
    
    private boolean isHalting = false;
    
    private DagNode endingNode;
    
    private DagNodeImpl previousNode;
    private DagNodeImpl currentNode;

    public DagNodeSearchState(
    		DagRelationshipType dagRelationshipType,
    		DagNodeImpl currentNode) {
    	
    	this.dagRelationshipType = dagRelationshipType;
        this.currentNode = currentNode;
        visited.put(currentNode.getName(), currentNode);
    }
    
    public DagNodeSearchState(
    		DagRelationshipType dagRelationshipType,
    		DagNodeImpl currentNode,
    		DagNode endingNode) {
    	
    	this.endingNode = endingNode;
    	this.dagRelationshipType = dagRelationshipType;
        this.currentNode = currentNode;
        visited.put(currentNode.getName(), currentNode);
    }
    
    public DagNodeSearchState(
    		DagNodeSearchState copy, 
    		DagNodeImpl currentNode, 
    		DagNodeConnector relationship) {
    	
    	this.endingNode = copy.endingNode;
    	this.isHalting = copy.isHalting;
    	this.dagRelationshipType = copy.dagRelationshipType;
    	this.previousNode = copy.currentNode;
        this.currentNode = currentNode;
        this.visited.putAll(copy.visited);
        this.vectors = copy.vectors;
        this.vector = new DagNodeVector(dagRelationshipType, copy.vector);
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
            vector = new DagNodeVector(dagRelationshipType);
        vector.add(connector);
    }
    
    public DagNodeVector getVector() {
        return vector;
    }

    public DagNodeImpl getCurrentNode() {
        return currentNode;
    }
    
    public boolean hasPreviousNode() {
    	return previousNode != null;
    }
    
    public DagNodeImpl getPreviousNode() {
    	return previousNode;
    }

	public List<DagNodeVector> getVectors() {
        return vectors;
    }
    
    public void fixCurrentVector(DagNodeConnector connector) {
    	if (vector != null) {
    		vector.add(connector);
    		vectors.add(vector);
    	} else {
	    	vector = new DagNodeVector(dagRelationshipType);
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
    
    public void addCycle(DagNodeVector currentVector, DagNodeConnector connector, DagRelationshipType linkType) {
    	DagNodeVector cyclicVector = new DagNodeVector(linkType, currentVector);
    	cyclicVector.add(connector);
        cycles.add(cyclicVector);
    }

	public List<DagNodeVector> getCycles() {
        return cycles;
    }

    public void setCycles(ArrayList<DagNodeVector> cycles) {
        this.cycles = cycles;
    }

	public DagRelationshipType getDagLinkType() {
		return dagRelationshipType;
	}

	public DagNode getEndingNode() {
		return endingNode;
	}

	public boolean isHalting() {
		return isHalting;
	}

	public void setHalting(boolean isHalting) {
		this.isHalting = isHalting;
	}

}
