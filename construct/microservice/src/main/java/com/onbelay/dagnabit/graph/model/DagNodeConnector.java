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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DagNodeConnector {
    
    private DagNode fromNode;
    private DagNode toNode;
    private boolean connectorIsCyclic = false;
    private Map<DagLinkType, DagLink> relationshipMap = new HashMap<>();
    
    
    public DagNodeConnector(
    		DagNode fromNode, 
    		DagLinkType dagLinkType, 
    		DagNode toNode) {
        
        relationshipMap.put(
        		dagLinkType,
        		new DagLink(
        				fromNode,
        				dagLinkType,
        				toNode));
        
        this.fromNode = fromNode;
        this.toNode = toNode;
    }
    
    public DagNode getFromNode() {
        return fromNode;
    }
    public void setFromNode(DagNode startNode) {
        this.fromNode = startNode;
    }
    
    public DagNode getToNode() {
        return toNode;
    }
    public void setToNode(DagNode endNode) {
        this.toNode = endNode;
    }
    
    public Map<DagLinkType, DagLink> getRelationships() {
    	return relationshipMap;
    }
    
    public void addRelationships(DagNodeConnector fromSide) {
    	relationshipMap.putAll(fromSide.getRelationships());
    }

    public void addRelationshipName(DagLinkType dagLinkType) {
    	
    	if (relationshipMap.containsKey(dagLinkType) == false) {
    		DagLink node = 	new DagLink(
    				fromNode,
    				dagLinkType,
    				toNode);
	        relationshipMap.put(
	        		dagLinkType,
	        		node);
    	}
    }
    
    public DagLink getRelationship(DagLinkType linkType) {
    	return relationshipMap.get(linkType);
    }
    
     
    public String toString() {
        return fromNode.getName() +  "->" + toNode.getName();
    }
    
    public String toFullString() {
		StringBuffer buffer = new StringBuffer(fromNode.getName());
		buffer.append(" - [");
		Set<DagLinkType> names = relationshipMap.keySet();
		Iterator<DagLinkType> itor = names.iterator();
		buffer.append(itor.next().getName());
		while (itor.hasNext()) {
			buffer.append(", ");
			buffer.append(itor.next().getName());
		}
		buffer.append("] -> ");
		buffer.append(toNode.getName());
		return buffer.toString();
    }
    
    public void markAsCyclic() {
    	connectorIsCyclic = true;
    }
    
    public void markAsCyclic(DagLinkType linkType) {
 		
    	if (linkType != null) {
	    	
	    	if (relationshipMap.containsKey(linkType)) {
	    		DagLink relationship = relationshipMap.get(linkType);
	    		relationship.makeCyclic();
	    	}
    	} else {
    		connectorIsCyclic = true;
    	}
 				
 	
     }
    
	public boolean isConnectorIsCyclic() {
		return connectorIsCyclic;
	}

	public void setConnectorIsCyclic(boolean connectorIsCyclic) {
		this.connectorIsCyclic = connectorIsCyclic;
	}

	public boolean hasRelationship(DagLinkType traversalRelationship) {

		return relationshipMap.containsKey(traversalRelationship);
	}
	
	public boolean isCyclic(DagLinkType linkType) {
		if (hasRelationship(linkType))
			return relationshipMap.get(linkType).isCyclic();
		else
			return false;
	}

	public boolean hasCycles() {
		for (DagLink link : relationshipMap.values()) {
			if (link.isCyclic())
				return true;
		}
		return false;
	}

}
