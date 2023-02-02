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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.onbelay.dagnabit.dagmodel.exception.DagGraphException;
import com.onbelay.dagnabit.dagmodel.model.DagRelationship;
import com.onbelay.dagnabit.dagmodel.model.DagRelationshipType;
import com.onbelay.dagnabit.dagmodel.model.DagNode;
import com.onbelay.dagnabit.dagmodel.model.DagNodePath;

/**
 * Contains a list of DagNodeConnectors representing a vector from a from to a to node.
 *
 */
public class DagNodeVector {
    
    private List<DagNodeConnector> connectors = new ArrayList<DagNodeConnector>();
    
    private DagRelationshipType relationshipType;
    
    public DagNodeVector(DagRelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }
    
    public DagNodeVector(DagRelationshipType relationshipType, DagNodeConnector connector) {
        this.relationshipType = relationshipType;
        connectors.add(connector);
    }
    
    public DagNodeVector(DagNodeVector vector, DagNodeConnector connector) {
        this.relationshipType = vector.relationshipType;
        connectors.addAll(vector.getConnectors());
        connectors.add(connector);
    }
    
    
    
    
    public DagNodePath createPath() {
    	
    	List<DagRelationship> links = connectors
    			.stream()
    				.map( c -> 	c.getRelationship(relationshipType))
    				.collect(Collectors.toList());
    	
    	return new DagNodePath(
    			getFromNode(), 
    			links,
    			getToNode());
    }
    
    public List<DagNodePath> createFromPaths() {
    	
    	DagNodeImpl startNode = getFromNode();
    	
    	ArrayList<DagNodePath> paths = new ArrayList<>();
    	
    	DagNodePath last = null;
    	
    	DagNodeConnector firstConnector = connectors.get(0);
    	
    	last = new DagNodePath(
    			startNode, 
    			firstConnector.getRelationship(relationshipType),
    			firstConnector.getToNode()); 
    			
		paths.add(last);
    	
    	for (int i=1; i <connectors.size(); i++) {
        	DagNodeConnector nextConnector = connectors.get(i);
        	
        	DagNodePath next = new DagNodePath(
        			startNode, 
        			last, 
        			nextConnector.getRelationship(relationshipType),
        			nextConnector.getToNode()); 
        	
        	paths.add(next);
    		last = next;
    	}
    	
    	return paths;
    }
    
    public List<DagNode> fetchDagNodesBreadthFirst(DagNode rootNode) {

    	if (connectors.isEmpty( ))
    		return new ArrayList<DagNode>();

    	
    	LinkedHashSet<DagNode> nodeSet = new LinkedHashSet<DagNode>();
    	
    	
    	for (DagNodeConnector connector : connectors) {
    		nodeSet.add(connector.getFromNode());
    		nodeSet.add(connector.getToNode());
    	}
    	
    	nodeSet.remove(rootNode);
    	List<DagNode> list = new ArrayList<DagNode>();
    	list.addAll(nodeSet);
    	return list;
    }
    
    public List<DagNodePath> createToPaths() {
    	
    	ArrayList<DagNodePath> paths = new ArrayList<>();
    	
    	
    	DagNodeConnector firstConnector = connectors.get(0);
    	
    	DagNodePath path = new DagNodePath(
    			firstConnector.getFromNode(), 
    			firstConnector.getRelationship(relationshipType),
    			firstConnector.getToNode());
    	
    	for (int i=1; i <connectors.size(); i++) {
        	DagNodeConnector nextConnector = connectors.get(i);
        	
        	path.addToPathLink(nextConnector.getRelationship(relationshipType));
        	
    	}
    	paths.add(path);
    	return paths;
    }
    

    
    public DagNodeVector(DagRelationshipType relationshipType, DagNodeVector copy) {
    	this.relationshipType = relationshipType;
        if (copy != null) {
        	connectors.addAll(copy.connectors);
        	if (relationshipType.equals(copy.relationshipType) == false)
        		throw new DagGraphException("LinkTypes don't match: " + relationshipType + " vs " + copy.relationshipType);
        }
    }
    
    public void add(DagNodeConnector connector) {
    	connectors.add(connector);
    }
    
    public DagNodeImpl getFromNode() {
        if (connectors.size() > 0) {
            return connectors.get(0).getFromNode();
        } else {
            return null;
        }
            
    }

    public DagNodeImpl getToNode() {
        if (connectors.size() > 0) {
        	int end = connectors.size() - 1;
            return connectors.get(end).getToNode();
        } else {
            return null;
        }
            
    }

    public String getId() {
    	return getFromNode() + " -> " + getToNode();
    }
    
    public List<DagNodeConnector> getConnectors() {
        return connectors;
    }

    public String toString() {
        StringBuffer b = new StringBuffer(connectors.get(0).toFullString());
        
        for (int i=1; i < connectors.size(); i++) {
        	b.append(" ");
            b.append(connectors.get(i).toFullString());
        }
        return b.toString();
    }
}
