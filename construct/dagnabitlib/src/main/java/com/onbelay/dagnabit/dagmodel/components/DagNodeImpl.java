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
import com.onbelay.dagnabit.dagmodel.model.DagNodeCategory;
import com.onbelay.dagnabit.dagmodel.model.DagRelationshipType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * represents a node on a graph with connectors to other nodes.
 * 
 * this node (from) connects To (to Node)
 * 
 * this node is connected from ( fromNode) 
 * 
 * @author canmxf
 *
 */
public class DagNodeImpl extends DagItemImpl implements DagNode {
	private static final Logger logger = LoggerFactory.getLogger(DagNodeImpl.class);

    private List<DagNodeConnector> fromThisConnectorToNodes = new  ArrayList<DagNodeConnector>();
    private List<DagNodeConnector> toThisConnectorFromNodes = new  ArrayList<DagNodeConnector>();
    
    private DagNodeCategory nodeCategory;
    
    public DagNodeImpl(String name, DagNodeCategory nodeCategory) {
       super(name);
        this.nodeCategory = nodeCategory;
    }

    public void addFromThisNodeConnector(DagNodeConnector r) {
    	fromThisConnectorToNodes.add(r);
    }
    
    
    public DagNodeConnector addFromThisNodeRelationshipToNode(
    		DagRelationshipType dagRelationshipType,
    		DagNodeImpl toNode) {
    	
        DagNodeConnector connector = findFromThisNodeConnectorTo(toNode);
        if (connector != null) {
        	connector.addRelationshipName(dagRelationshipType);
        } else {
        	connector = new DagNodeConnector(this, dagRelationshipType, toNode);
            fromThisConnectorToNodes.add(connector);
        }
    	toNode.addToThisNodeRelationshipFromNode(connector);
    	
    	if (toNode.checkForReciprical(this, dagRelationshipType)) {
    		logger.debug("reciprical relationship exists " + this.getName() + "-" + dagRelationshipType.getName() + "-" + toNode.getName());
    	}
    			
    	
        return connector;
    }

    public DagNodeConnector addToThisNodeRelationshipFromNode(DagNodeConnector fromSide) {
        DagNodeConnector connector = findToThisNodeConnectorFrom(fromSide.getFromNode());
        if (connector != null) {
        	connector.addRelationships(fromSide);
        } else {
            toThisConnectorFromNodes.add(fromSide);
        }
        return connector;
    }
    
    public boolean isLeaf() {
    	return (hasFromThisNodeConnectors() == false && hasToThisNodeConnectors());
    }
    
    public boolean isRoot() {
    	return (hasFromThisNodeConnectors() && hasToThisNodeConnectors() == false);
    }
    
    public DagNodeCategory getCategory() {
		return nodeCategory;
	}

	public boolean hasNoConnectors() {
    	return !(hasFromThisNodeConnectors() || hasToThisNodeConnectors());
    }

	public boolean hasFromThisNodeConnectors() {
        return fromThisConnectorToNodes.size() > 0;
    }
    
    public boolean hasToThisNodeConnectors() {
        return toThisConnectorFromNodes.size() > 0;
    }
    
    protected boolean checkForReciprical(
    		DagNodeImpl fromNode, 
    		DagRelationshipType dagRelationshipType) {
    	DagNodeConnector connector = findFromThisNodeConnectorTo(fromNode);
    	if (connector == null)
    		return false;
    	
    	return (connector.hasRelationship(dagRelationshipType));
    	
    }

    public boolean hasToConnectorFrom(DagNodeImpl fromNode) {
        for (DagNodeConnector r: toThisConnectorFromNodes) {
            if (r.getFromNode().equals(fromNode))
                return true;
        }
        return false;
    }

    public boolean hasFromConnectorTo(DagNodeImpl toNode) {
        for (DagNodeConnector r: fromThisConnectorToNodes) {
            if (r.getToNode().equals(toNode))
                return true;
        }
        return false;
    }


    public DagNodeConnector findFromThisNodeConnectorTo(DagNodeImpl toNode) {
        for (DagNodeConnector r: fromThisConnectorToNodes) {
            if (r.getToNode().equals(toNode))
                return r;
        }
        return null;
    }

    public DagNodeConnector findToThisNodeConnectorFrom(DagNodeImpl fromNode) {
        for (DagNodeConnector r: toThisConnectorFromNodes) {
            if (r.getFromNode().equals(fromNode))
                return r;
        }
        return null;
    }

    public List<DagNodeConnector> getFromThisNodeConnectors() {
        return fromThisConnectorToNodes;
    }
    
    public List<DagNodeConnector> getSortedFromThisNodeConnectors(Comparator<DagNodeConnector> sorter) {
    	
    	return fromThisConnectorToNodes
    		.stream()
    		.sorted(sorter)
    		.collect(Collectors.toList());
    	
    }


    public List<DagNodeConnector> getToThisNodeConnectors() {
        return toThisConnectorFromNodes;
    }
    
	public int compareTo(DagNode c2) {
		return name.compareTo(c2.getName());
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((nodeCategory == null) ? 0 : nodeCategory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DagNodeImpl other = (DagNodeImpl) obj;
		if (nodeCategory == null) {
			if (other.nodeCategory != null)
				return false;
		} else if (!nodeCategory.equals(other.nodeCategory))
			return false;
		return true;
	}


}
