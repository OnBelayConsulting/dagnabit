package com.onbelay.dagnabit.graph.examples.travelingsales;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onbelay.dagnabit.graph.factories.DagModelFactory;
import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNode;

public class TPFixture {
	private static Logger logger = LoggerFactory.getLogger(TPFixture.class);

	public static DagModel buildCircularModel() {
		DagModelFactory factory = new DagModelFactory();
		DagModel model = factory.newModel();
		
		model.addNode("A");
		
		model.addNode("B");
		
		model.addNode("C");
		
		model.addNode("D");
		
		model.addRelationship(
				model.getNode("A"), 
				"connects", 
				model.getNode("B"));
		
		model.addRelationship(
				model.getNode("B"), 
				"connects", 
				model.getNode("C"));
		
		
		model.addRelationship(
				model.getNode("C"), 
				"connects", 
				model.getNode("D"));
		
		
		model.addRelationship(
				model.getNode("D"), 
				"connects", 
				model.getNode("A"));
		
		return model;

	}
	

	public static DagModel buildTPModel() {
		DagModelFactory factory = new DagModelFactory();
		DagModel model = factory.newModel();
		
		model.addNode("1");
		
		model.addNode("2");
		
		model.addNode("3");
		
		model.addNode("4");
		
		DagLink a13Link = model.addRelationship(
				model.getNode("1"), 
				"connects", 
				model.getNode("3"));
		a13Link.setWeight(15);
		
		DagLink b31Link = model.addRelationship(
				model.getNode("3"), 
				"connects", 
				model.getNode("1"));
		b31Link.setWeight(15);

		DagLink a34Link = model.addRelationship(
				model.getNode("3"), 
				"connects", 
				model.getNode("4"));
		a34Link.setWeight(30);

		DagLink b43Link = model.addRelationship(
				model.getNode("4"), 
				"connects", 
				model.getNode("3"));
		b43Link.setWeight(30);

		DagLink a41Link = model.addRelationship(
				model.getNode("4"), 
				"connects", 
				model.getNode("1"));
		a41Link.setWeight(20);

		DagLink b14Link = model.addRelationship(
				model.getNode("1"), 
				"connects", 
				model.getNode("4"));
		b14Link.setWeight(20);

		
		DagLink a32Link = model.addRelationship(
				model.getNode("3"), 
				"connects", 
				model.getNode("2"));
		a32Link.setWeight(35);
		
		DagLink b23Link = model.addRelationship(
				model.getNode("2"), 
				"connects", 
				model.getNode("3"));
		b23Link.setWeight(35);
		
		DagLink a24Link =  model.addRelationship(
				model.getNode("2"), 
				"connects", 
				model.getNode("4"));
		a24Link.setWeight(25);
		
		DagLink b42Link =  model.addRelationship(
				model.getNode("4"), 
				"connects", 
				model.getNode("2"));
		b42Link.setWeight(25);
		
		DagLink a21Link =  model.addRelationship(
				model.getNode("2"), 
				"connects", 
				model.getNode("1"));
		a21Link.setWeight(10);
		
		DagLink b12Link =  model.addRelationship(
				model.getNode("1"), 
				"connects", 
				model.getNode("2"));
		b12Link.setWeight(10);
		
		return model;

	}
	
	public static DagModel buildLargeTPMode() {
		DagModel model = buildTPModel();
		
		model.addNode("5");
		
		model.addNode("6");
		
		model.addNode("7");
		
		model.addNode("8");
		
		model.addNode("9");
		
		model.addNode("10");
		
		model.addNode("11");
		
		model.addNode("12");

		
		for (int i=5; i <9; i++) {
			DagNode node = model.getNode(""+i); 
			
			for (int j=1; j < 9; j++) {
				DagNode secondNode = model.getNode(""+j);
				if (node.equals(secondNode) == false) {
					int weight = i*3 + j*5;
					DagLink firstLink =  model.addRelationship(
							node, 
							"connects", 
							secondNode);
					firstLink.setWeight(weight);
					DagLink secondLink =  model.addRelationship(
							secondNode, 
							"connects", 
							node);
					secondLink.setWeight(weight);
					
				}
			}
			
		}
		
		logger.error("total nodes: " + model.getNodes());
		
		logger.error("Total links: " + model.getLinks().size());
		
//		for (DagLink link : model.getLinks()) {
//			logger.error("Link " + link.getName() );
//		}
		
		return model;
	}
}
