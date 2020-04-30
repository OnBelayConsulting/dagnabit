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
import java.util.List;
import java.util.stream.Collectors;

import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.NodePathLink;

/**
 * Contains a list of DagNodeConnectors representing a vector from a from to a to node.
 *
 */
public class DagNodeVector {
    
    private List<DagNodeConnector> connectors = new ArrayList<DagNodeConnector>();
    
    public DagNodeVector() {
        
    }
    
    public DagNodePath createPath() {
    	
    	List<NodePathLink> links = connectors
    			.stream()
    				.map( c -> new NodePathLink(
    						c.getFromNode(), 
    						c.getRelationships().keySet(), 
    						c.getToNode()))
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
    			new NodePathLink(
    					firstConnector.getFromNode(), 
    					firstConnector.getRelationships().keySet(), 
    					firstConnector.getToNode()));
    			
		paths.add(last);
    	
    	for (int i=1; i <connectors.size(); i++) {
        	DagNodeConnector nextConnector = connectors.get(i);
        	
        	DagNodePath next = new DagNodePath(
        			startNode, 
        			last, 
        			new NodePathLink(
        					nextConnector.getFromNode(), 
        					nextConnector.getRelationships().keySet(), 
        					nextConnector.getToNode()));
        	
        	paths.add(next);
    		last = next;
    	}
    	
    	return paths;
    }
    
    public List<DagNodePath> createToPaths() {
    	
    	ArrayList<DagNodePath> paths = new ArrayList<>();
    	
    	
    	DagNodeConnector firstConnector = connectors.get(0);
    	
    	DagNodePath path = new DagNodePath(
    			firstConnector.getToNode(), 
    			new NodePathLink(
    					firstConnector.getToNode(), 
    					firstConnector.getRelationships().keySet(), 
    					firstConnector.getFromNode()));
    			
    	
    	for (int i=1; i <connectors.size(); i++) {
        	DagNodeConnector nextConnector = connectors.get(i);
        	
        	path.addToPathLink(
        			new NodePathLink(
        					nextConnector.getToNode(), 
        					nextConnector.getRelationships().keySet(), 
        					nextConnector.getFromNode()));
        	
    	}
    	paths.add(path);
    	return paths;
    }
    

    
    public DagNodeVector(DagNodeVector copy) {
        if (copy != null)
        	connectors.addAll(copy.connectors);
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
