package com.onbelay.dagnabit.graph.examples.genealogy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.onbelay.dagnabit.graph.model.DagContext;
import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagNode;

public class DagGenealogyContext implements DagContext {

	private Map<String, DagGenealogyNodeContext> nodes = new HashMap<>();

	public Map<String, DagGenealogyNodeContext> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, DagGenealogyNodeContext> nodes) {
		this.nodes = nodes;
	}
	
	
	public List<String> getPersonNames() {
		
		return nodes
				.values()
				.stream()
				.filter(c -> c.getLocationSchoolAttended().equals(c.getLocationWasBornIn()))
				.map(c -> c.getName())
				.collect(Collectors.toList());
	}
	
	
	public static void accept(DagContext context, DagNode startingNode, DagLink link, DagNode endingNode) {
		Map<String, DagGenealogyNodeContext> nodes = ((DagGenealogyContext)context).getNodes();
		
		
		if (startingNode.getNodeType().getTypeName().equals("PERSON")) {
 
			DagGenealogyNodeContext nodeContext;
			if (nodes.containsKey(startingNode.getName())) {
				nodeContext = nodes.get(startingNode.getName());
			} else {
				nodeContext = new DagGenealogyNodeContext(startingNode.getName());
				nodes.put(startingNode.getName(), nodeContext);
			}
		
			if (link.getDagLinkType().getName().equals(GenealogyFixture.ATTENDED_REL))
				nodeContext.setNameOfSchool(endingNode.getName());
			else
				nodeContext.setLocationWasBornIn(endingNode.getName());
		} else {
		
			for (DagGenealogyNodeContext n : nodes.values()) {
				if (n.getNameOfSchool().equals(startingNode.getName()))
					n.setLocationSchoolAttended(endingNode.getName());
			}
			
		}	
			
	}
	
}
