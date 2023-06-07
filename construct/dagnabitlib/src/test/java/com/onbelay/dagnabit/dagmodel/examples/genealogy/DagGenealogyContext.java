package com.onbelay.dagnabit.dagmodel.examples.genealogy;

import com.onbelay.dagnabit.dagmodel.model.DagContext;
import com.onbelay.dagnabit.dagmodel.model.DagNode;
import com.onbelay.dagnabit.dagmodel.model.DagRelationship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	
	
	public static void accept(DagContext context, DagNode startingNode, DagRelationship link, DagNode endingNode) {
		Map<String, DagGenealogyNodeContext> nodes = ((DagGenealogyContext)context).getNodes();
		
		
		if (startingNode.getCategory().getCategoryName().equals("PERSON")) {
 
			DagGenealogyNodeContext nodeContext;
			if (nodes.containsKey(startingNode.getName())) {
				nodeContext = nodes.get(startingNode.getName());
			} else {
				nodeContext = new DagGenealogyNodeContext(startingNode.getName());
				nodes.put(startingNode.getName(), nodeContext);
			}
		
			if (link.getRelationshipType().getName().equals(GenealogyFixture.ATTENDED_REL))
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
