package com.onbelay.dagnabit.dagmodel.examples.travelingsales;

import com.onbelay.dagnabit.dagmodel.components.DagModelImpl;
import com.onbelay.dagnabit.dagmodel.model.DagModel;
import com.onbelay.dagnabit.dagmodel.model.DagNode;
import com.onbelay.dagnabit.dagmodel.model.DagRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class TPFixture {
	private static Logger logger = LoggerFactory.getLogger(TPFixture.class);

	public static DagModel buildCircularModel() {
		DagModel model = new DagModelImpl("test");
		
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
		DagModel model = new DagModelImpl("test");
		
		model.addNode("1");
		
		model.addNode("2");
		
		model.addNode("3");
		
		model.addNode("4");
		
		DagRelationship a13Link = model.addRelationship(
				model.getNode("1"), 
				"connects", 
				model.getNode("3"));
		a13Link.setWeight(BigDecimal.valueOf(15));
		
		DagRelationship b31Link = model.addRelationship(
				model.getNode("3"), 
				"connects", 
				model.getNode("1"));
		b31Link.setWeight(BigDecimal.valueOf(15));

		DagRelationship a34Link = model.addRelationship(
				model.getNode("3"), 
				"connects", 
				model.getNode("4"));
		a34Link.setWeight(BigDecimal.valueOf(30));

		DagRelationship b43Link = model.addRelationship(
				model.getNode("4"), 
				"connects", 
				model.getNode("3"));
		b43Link.setWeight(BigDecimal.valueOf(30));

		DagRelationship a41Link = model.addRelationship(
				model.getNode("4"), 
				"connects", 
				model.getNode("1"));
		a41Link.setWeight(BigDecimal.valueOf(20));

		DagRelationship b14Link = model.addRelationship(
				model.getNode("1"), 
				"connects", 
				model.getNode("4"));
		b14Link.setWeight(BigDecimal.valueOf(20));

		
		DagRelationship a32Link = model.addRelationship(
				model.getNode("3"), 
				"connects", 
				model.getNode("2"));
		a32Link.setWeight(BigDecimal.valueOf(35));
		
		DagRelationship b23Link = model.addRelationship(
				model.getNode("2"), 
				"connects", 
				model.getNode("3"));
		b23Link.setWeight(BigDecimal.valueOf(35));
		
		DagRelationship a24Link =  model.addRelationship(
				model.getNode("2"), 
				"connects", 
				model.getNode("4"));
		a24Link.setWeight(BigDecimal.valueOf(25));
		
		DagRelationship b42Link =  model.addRelationship(
				model.getNode("4"), 
				"connects", 
				model.getNode("2"));
		b42Link.setWeight(BigDecimal.valueOf(25));
		
		DagRelationship a21Link =  model.addRelationship(
				model.getNode("2"), 
				"connects", 
				model.getNode("1"));
		a21Link.setWeight(BigDecimal.valueOf(10));
		
		DagRelationship b12Link =  model.addRelationship(
				model.getNode("1"), 
				"connects", 
				model.getNode("2"));
		b12Link.setWeight(BigDecimal.valueOf(10));
		
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
					DagRelationship firstLink =  model.addRelationship(
							node, 
							"connects", 
							secondNode);
					firstLink.setWeight(BigDecimal.valueOf(weight));
					DagRelationship secondLink =  model.addRelationship(
							secondNode, 
							"connects", 
							node);
					secondLink.setWeight(BigDecimal.valueOf(weight));
					
				}
			}
			
		}
		
		logger.error("total nodes: " + model.getNodes());
		
		logger.error("Total links: " + model.getRelationships().size());
		
//		for (DagLink link : model.getLinks()) {
//			logger.error("Link " + link.getName() );
//		}
		
		return model;
	}
}
