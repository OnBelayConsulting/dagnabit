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
import java.util.HashMap;
import java.util.List;

public class DagPathRoutes {

	private DagNode fromNode;
	private DagNode toNode;
	
    private HashMap<String, DagNodePath> pathMap = new HashMap<>();

	public DagPathRoutes(DagNode fromNode, DagNode toNode) {
		super();
		this.fromNode = fromNode;
		this.toNode = toNode;
	}
	
	public void addPath(DagNodePath path) {
		DagNodePath exists = pathMap.get(path.toString());
		if (exists == null) {
			pathMap.put(path.toString(), path);
		}

	}

    
    public static String createId(String fromNodeName, String toNodeName) {
    	return fromNodeName + ":" + toNodeName;
    }
	
	public String getId() {
		return fromNode.getName() + ":" + toNode.getName();
	}
	
	public List<DagNodePath> getPaths() {
		ArrayList<DagNodePath> paths = new ArrayList<>();
		paths.addAll(pathMap.values());
		return paths;
	}
	
	public String toString() {
		return getId() + " " + getPaths().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromNode == null) ? 0 : fromNode.hashCode());
		result = prime * result + ((toNode == null) ? 0 : toNode.hashCode());
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
		DagPathRoutes other = (DagPathRoutes) obj;
		if (fromNode == null) {
			if (other.fromNode != null)
				return false;
		} else if (!fromNode.equals(other.fromNode))
			return false;
		if (toNode == null) {
			if (other.toNode != null)
				return false;
		} else if (!toNode.equals(other.toNode))
			return false;
		return true;
	}
	
	
}
