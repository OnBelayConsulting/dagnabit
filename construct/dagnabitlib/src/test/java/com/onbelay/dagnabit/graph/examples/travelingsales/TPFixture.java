package com.onbelay.dagnabit.graph.examples.travelingsales;

import com.onbelay.dagnabit.graph.factories.DagModelFactory;
import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagModel;

public class TPFixture {

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
}
