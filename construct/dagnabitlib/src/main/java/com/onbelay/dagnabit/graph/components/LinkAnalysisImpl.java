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

import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagNodeConnector;
import com.onbelay.dagnabit.graph.model.LinkAnalysis;

public class LinkAnalysisImpl  implements LinkAnalysis {
	
	private Map<DagLinkType, List<DagNodeConnector>> cyclesByLinkType = new HashMap<DagLinkType, List<DagNodeConnector>>();
	private List<DagNodeConnector> connectorCycles = new ArrayList<DagNodeConnector>();
	
	public void addCycle(List<DagNodeConnector> cycles) {
		connectorCycles.addAll(cycles);
	}

	public void addCycleByLinkType(
			DagLinkType linkType, 
			List<DagNodeConnector> cycles) {
		
		List<DagNodeConnector> existingCycles = cyclesByLinkType.get(linkType);
		if (existingCycles != null) {
			existingCycles.addAll(cycles);
		} else {
			cyclesByLinkType.put(linkType, cycles);
		}
	}
	
	public List<DagNodeConnector> getCycles() {
		List<DagNodeConnector> cycles = new ArrayList<DagNodeConnector>();
		
		if (connectorCycles.isEmpty() == false) {
			cycles.addAll(connectorCycles);
		} else {
			for (List<DagNodeConnector> connectors : cyclesByLinkType.values()) {
				cycles.addAll(connectors);
			}
		}
		return cycles;
	}
	
	public Map<DagLinkType, List<DagNodeConnector>> getCyclesByLinkType() {
		return cyclesByLinkType;
	}

	public void setCyclesByLinkType(Map<DagLinkType, List<DagNodeConnector>> cyclesByLinkType) {
		this.cyclesByLinkType = cyclesByLinkType;
	}
	
	public boolean isCyclic() {
		return (connectorCycles.isEmpty() == false);
	}
	
	public boolean isCyclic(DagLinkType linkType) {
		boolean hasCycles = false;
		for (DagNodeConnector c : cyclesByLinkType.get(linkType))
			if (c.hasCycles())
				hasCycles = true;
	
		return hasCycles;
		
	}
	
	public boolean hasCycles() {
		boolean hasCycles = false;
		for (List<DagNodeConnector> cycles : cyclesByLinkType.values()) {
			for (DagNodeConnector c : cycles)
				if (c.hasCycles())
					hasCycles = true;
		}
		return hasCycles;
	}
	
	
	
}
