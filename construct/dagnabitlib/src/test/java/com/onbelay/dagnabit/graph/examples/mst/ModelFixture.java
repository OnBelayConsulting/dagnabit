package com.onbelay.dagnabit.graph.examples.mst;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onbelay.dagnabit.graph.factories.DagModelFactory;
import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNode;
/**
 * The basic model for this is abased on 
 * 		/7  B	-6-	D	5\		
 * A 		3|	4/	|2	 	F
 * 		8\	C	-3-	E	2/	
 * 
 * 
 * 
 * @author lefeu
 *
 */
public class ModelFixture {
	private static Logger logger = LoggerFactory.getLogger(ModelFixture.class);

	
	public static DagModel buildBasicModel() {
		
		DagModelFactory factory = new DagModelFactory();
		DagModel model = factory.newModel();
		
		
		model.addNode("A");
		
		model.addNode("B");
		
		model.addNode("C");
		
		model.addNode("D");
		
		model.addNode("E");
		
		model.addNode("F");
		
		ArrayList<DagLink> links = new ArrayList<DagLink>();
		
		DagLink link = model.addDefaultRelationship(
				model.getNode("A"), 
				model.getNode("B"));
		link.setWeight(7);
		links.add(link);
		
		link = model.addDefaultRelationship(
				model.getNode("B"), 
				model.getNode("D"));
		link.setWeight(6);
		links.add(link);
		
		link = model.addDefaultRelationship(
				model.getNode("D"), 
				model.getNode("F"));
		link.setWeight(5);
		links.add(link);
		

		link = model.addDefaultRelationship(
				model.getNode("F"), 
				model.getNode("E"));
		link.setWeight(2);
		links.add(link);

		link = model.addDefaultRelationship(
				model.getNode("E"), 
				model.getNode("D"));
		link.setWeight(2);
		links.add(link);
		
		
		link = model.addDefaultRelationship(
				model.getNode("E"), 
				model.getNode("C"));
		link.setWeight(3);
		links.add(link);

		
		link = model.addDefaultRelationship(
				model.getNode("C"), 
				model.getNode("D"));
		link.setWeight(4);
		links.add(link);
		
		
		link = model.addDefaultRelationship(
				model.getNode("C"), 
				model.getNode("B"));
		link.setWeight(3);
		links.add(link);
		
		
		link = model.addDefaultRelationship(
				model.getNode("C"), 
				model.getNode("A"));
		link.setWeight(8);
		links.add(link);

		// Make Bidirectional
		for (DagLink ln : links) {
			
			DagLink inverse = model.addInverse(ln);
			
		}
		
		
		return model;
	}
	
	
	/**
	 * 
	 * 
	 */
	public static DagModel BuildComplexModel() {
		DagModel model = buildBasicModel();
		
		model.addNode("G");
		
		model.addNode("H");
		
		model.addNode("I");
		
		model.addNode("J");
		
		model.addNode("K");
		
		model.addNode("L");

		ArrayList<DagLink> links = new ArrayList<DagLink>();
		
		DagLink link = model.addDefaultRelationship(
				model.getNode("A"), 
				model.getNode("H"));
		link.setWeight(1);
		links.add(link);
		
		
		link = model.addDefaultRelationship(
				model.getNode("H"), 
				model.getNode("D"));
		link.setWeight(2);
		links.add(link);

		
		link = model.addDefaultRelationship(
				model.getNode("D"), 
				model.getNode("I"));
		link.setWeight(2);
		links.add(link);
		
		
		link = model.addDefaultRelationship(
				model.getNode("I"), 
				model.getNode("F"));
		link.setWeight(1);
		links.add(link);
		
		
		link = model.addDefaultRelationship(
				model.getNode("E"), 
				model.getNode("G"));
		link.setWeight(1);
		links.add(link);
		
		link = model.addDefaultRelationship(
				model.getNode("G"), 
				model.getNode("J"));
		link.setWeight(8);
		links.add(link);
		
		link = model.addDefaultRelationship(
				model.getNode("F"), 
				model.getNode("J"));
		link.setWeight(6);
		links.add(link);
		
		// Make Bidirectional
		for (DagLink ln : links) {
			
			DagLink inverse = model.addInverse(ln);
			
		}
		

		
		return model;

	}
	
}
