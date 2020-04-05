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

import java.util.List;
import java.util.Map;

/**
 * Defines a common interface for a DagModel.
 * A Dag model acts as a Container for all Directed Acyclic Graph (DAG) elements such as nodes and links (vertices, edges).
 * 
 */
public interface DagModel {
    
    
    public DagNodeNavigator navigate();
    
    public LinkAnalyser analyse();
    
    public LinkRouteFinder createDagLinkRouteFinder(
    		DagNodeType dagNodeType,
    		DagLinkType dagLinkType);
    
    public List<DagNode> findRootNodes();
    
    public List<DagNode> findLeafNodes();
    
    public List<DagNode> findSolitaryNodes();
    
    public List<DagLinkType> getLinkTypes();
    
    public DagNodeType getNodeType(String nodeTypeName);
    
    public List<DagNodeType> getNodeTypes();
    
    public void addNode(String nodeName);
    
    public void addNode(String nodeName, String nodeTypeName);
    

    public DagNode getNode(String code);
    
    public void addRelationship(
    		DagNode fromNode, 
    		String relationshipName, 
    		DagNode toNode);
    
    public DagLinkType getLinkType(String name);

    public Map<String, DagNodeType> getNodeTypeMap();

	public Map<String, DagNode> getNodeMap();

	public Map<String, DagLinkType> getLinkTypeMap();

    
}
