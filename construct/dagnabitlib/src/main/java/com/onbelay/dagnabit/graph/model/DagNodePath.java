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
import java.util.List;

/**
 * DagNodePath defines a path from the fromNode to the toNode as a set of zero to many DagLinks.
 * The list of DagLinks will be empty if there is no path between the two nodes.
 * @author lefeu
 *
 */
public class DagNodePath {
    
    private DagNode startNode;
    private DagNode endNode;
    
    private List<DagLink> links = new ArrayList<>();
    
    public DagNodePath(
    		DagNode startNode, 
    		List<DagLink> links, 
    		DagNode endNode) {
    	
    	this.startNode = startNode;
    	this.links = links;
    	this.endNode = endNode;
    	
    }

    public DagNodePath(DagNode startNode, DagLink link, DagNode endNode) {
        super();
        this.startNode = startNode;
        this.endNode = endNode;
        links.add(link);
    }
    
    public DagNodePath(
    		DagNode startNode, 
    		DagNodePath lastPath, 
    		DagLink link,
    		DagNode endNode) {
    	
    	links.addAll(lastPath.getLinks());
        this.startNode = startNode;
        this.endNode = endNode;
        links.add(link);
    }

    public void addPathLink(DagLink v) {
    	links.add(v);
    }
    
    public void addToPathLink(DagLink v) {
    	this.startNode = v.getFromNode();
    	links.add(v);
    }
    
    /**
     * returns true if there is a path (links is not empty) from the fromNode to the toNode.
     * @return true if path exists
     */
    public boolean pathExists() {
    	return links.isEmpty() == false;
    }
    
    /**
     * Returns a list of DagLinks that describes the path in order from the fromNode to the toNode.
     * @return a list of DagLinks. The list will be empty if there is no path.
     */
    public List<DagLink> getLinks() {
        return links;
    }

    public DagNode getStartNode() {
        return startNode;
    }

    public DagNode getEndNode() {
        return endNode;
    }
    
    /**
     * Calculate the total weight of the path based on the weights of the links.
     * @return 0 if no links or total weight.
     */
    public int calculateTotalWeight() {
    	int totalWeight = 0;
    	for (DagLink c : links) {
    		totalWeight = totalWeight + c.getWeight();
    	}
    	return totalWeight;
    }

    /**
     * Returns an id based on the from and to node names. This is not unique for models that have multiple link types.
     * @return
     */
    public String getRouteId() {
    	return startNode.getName() + ":" + endNode.getName();
    }
    
    /**
     * Returns an id based on the list of links. This id is unique for any combination of fromNode and toNode.
     * @return
     */
    public String toString() {
    	if (pathExists()) {
	    	StringBuffer buffer = new StringBuffer("Path:[ ");
	    	buffer.append(getStartNode().getName());
	    	buffer.append( " --> "); 
	    	buffer.append(getEndNode().getName());
	    	buffer.append("]     Via: ");
	    	
	    	DagLink c = links.get(0);
    		buffer.append(c.getFromNode().getName());
    		buffer.append(" -> ");
    		buffer.append(c.getToNode().getName());
	    	
	    	for (int i=1; i < links.size(); i++) {
	    		c = links.get(i);
	    		buffer.append(", ");
	    		buffer.append(c.getFromNode().getName());
	    		buffer.append(" -> ");
	    		buffer.append(c.getToNode().getName());
	    	}
	    	return buffer.toString();
    	} else {
    		return startNode.getName() + " <> " + endNode.getName();
    	}
    }
    
    public String toStringWithWeights() {
    	if (pathExists()) {
	    	StringBuffer buffer = new StringBuffer(getStartNode().getName());
	    	for (DagLink c : links) {
	    		buffer.append(" - ");
	    		buffer.append(c.getWeight());
	    		buffer.append(" > ");
	    		buffer.append(c.getToNode().getName());
	    	}
	    	return buffer.toString();
    	} else {
    		return startNode.getName() + " <> " + endNode.getName();
    	}
    	
    }
    

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + toString().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DagNodePath other = (DagNodePath) obj;
		return toString().equals(other.toString());
	}

    
}
