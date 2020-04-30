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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.onbelay.dagnabit.graph.model.DagLinkType;

public class DagNodeConnector {
    
    private DagNodeImpl fromNode;
    private DagNodeImpl toNode;
    private Map<DagLinkType, DagLinkImpl> relationshipMap = new HashMap<>();
    
    
    public DagNodeConnector(
    		DagNodeImpl fromNode, 
    		DagLinkType dagLinkType, 
    		DagNodeImpl toNode) {
        
        relationshipMap.put(
        		dagLinkType,
        		new DagLinkImpl(
        				fromNode,
        				dagLinkType,
        				toNode));
        
        this.fromNode = fromNode;
        this.toNode = toNode;
    }
    
    public DagNodeImpl getFromNode() {
        return fromNode;
    }
    public void setFromNode(DagNodeImpl startNode) {
        this.fromNode = startNode;
    }
    
    public DagNodeImpl getToNode() {
        return toNode;
    }
    public void setToNode(DagNodeImpl endNode) {
        this.toNode = endNode;
    }
    
    public Map<DagLinkType, DagLinkImpl> getRelationships() {
    	return relationshipMap;
    }
    
    public void addRelationships(DagNodeConnector fromSide) {
    	relationshipMap.putAll(fromSide.getRelationships());
    }

    public void addRelationshipName(DagLinkType dagLinkType) {
    	
    	if (relationshipMap.containsKey(dagLinkType) == false) {
    		DagLinkImpl node = 	new DagLinkImpl(
    				fromNode,
    				dagLinkType,
    				toNode);
	        relationshipMap.put(
	        		dagLinkType,
	        		node);
    	}
    }
    
    public DagLinkImpl getRelationship(DagLinkType linkType) {
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
    

	public boolean hasRelationship(DagLinkType traversalRelationship) {

		return relationshipMap.containsKey(traversalRelationship);
	}

}
