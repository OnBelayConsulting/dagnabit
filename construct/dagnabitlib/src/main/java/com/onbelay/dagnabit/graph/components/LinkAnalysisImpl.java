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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onbelay.dagnabit.graph.model.DagRelationshipType;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.LinkAnalysis;

public class LinkAnalysisImpl  implements LinkAnalysis {
	
	private Map<DagRelationshipType, List<DagNodePath>> cyclesByRelationshipType = new HashMap<DagRelationshipType, List<DagNodePath>>();

	public void addCycleByLinkType(
			DagRelationshipType relationshipType,
			List<DagNodePath> cycles) {
		
		List<DagNodePath> existingCycles = cyclesByRelationshipType.get(relationshipType);
		if (existingCycles != null) {
			existingCycles.addAll(cycles);
		} else {
			cyclesByRelationshipType.put(relationshipType, cycles);
		}
	}
	
	@Override
	public List<DagNodePath> getCycles() {
		List<DagNodePath> cycles = new ArrayList<DagNodePath>();
		
		for (List<DagNodePath> connectors : cyclesByRelationshipType.values()) {
			cycles.addAll(connectors);
		}
		
		return cycles;
	}
	
	@Override
	public Map<DagRelationshipType, List<DagNodePath>> getCyclesByRelationshipType() {
		return cyclesByRelationshipType;
	}

	public void setCyclesByRelationshipType(Map<DagRelationshipType, List<DagNodePath>> cyclesByRelationshipType) {
		this.cyclesByRelationshipType = cyclesByRelationshipType;
	}
	
	@Override
	public boolean isCyclic() {
		return (getCycles().isEmpty() == false);
	}
	
	@Override
	public boolean isCyclic(DagRelationshipType relationshipType) {
		return (cyclesByRelationshipType.containsKey(relationshipType));
		
	}
	
	
}
