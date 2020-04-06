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

public class DagNodePath {
    
    private DagNode fromNode;
    private DagNode toNode;
    
    private List<DagNodeConnector> connectors = new ArrayList<>();

    public DagNodePath(DagNode startNode, DagNodeConnector connector) {
        super();
        this.fromNode = startNode;
        this.toNode = connector.getToNode();
        connectors.add(connector);
    }
    
    public DagNodePath(DagNode startNode, DagNodePath lastPath, DagNodeConnector connector) {
    	connectors.addAll(lastPath.getConnectors());
        this.fromNode = startNode;
        this.toNode = connector.getToNode();
        connectors.add(connector);
    }

    public void addConnector(DagNodeConnector v) {
    	connectors.add(v);
    }
    
    public List<DagNodeConnector> getConnectors() {
        return connectors;
    }

    public DagNode getFromNode() {
        return fromNode;
    }

    public DagNode getToNode() {
        return toNode;
    }


    public String getRouteId() {
    	return getFromNode() + ":" + getToNode();
    }
    
    public String getId() {
    	StringBuffer buffer = new StringBuffer(getFromNode().getName());
    	for (DagNodeConnector c : connectors) {
    		buffer.append(" -> ");
    		buffer.append(c.getToNode().getName());
    	}
    	return buffer.toString();
    }
    
    public String toString() {
    	return getId();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getId().hashCode();
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
		return getId().equals(other.getId());
	}

    
}
