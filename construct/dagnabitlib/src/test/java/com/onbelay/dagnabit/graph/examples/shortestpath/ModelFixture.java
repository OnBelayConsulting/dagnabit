package com.onbelay.dagnabit.graph.examples.shortestpath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onbelay.dagnabit.graph.factories.DagModelFactory;
import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagModel;

public class ModelFixture {
	private static Logger logger = LoggerFactory.getLogger(ModelFixture.class);

	public static DagModel buildModel() {
		DagModelFactory factory = new DagModelFactory();
		DagModel model = factory.newModel();
		
		model.addNode("start");
		
		model.addNode("A");
		
		model.addNode("B");
		
		model.addNode("finish");
		
		DagLink link = model.addDefaultRelationship(
				model.getNode("start"), 
				model.getNode("A"));
		link.setWeight(6);

		
		link = model.addDefaultRelationship(
				model.getNode("start"), 
				model.getNode("B"));
		link.setWeight(2);
		

		link = model.addDefaultRelationship(
				model.getNode("B"), 
				model.getNode("A"));
		link.setWeight(3);
		
		link = model.addDefaultRelationship(
				model.getNode("A"), 
				model.getNode("finish"));
		link.setWeight(1);
		
		link = model.addDefaultRelationship(
				model.getNode("B"), 
				model.getNode("finish"));
		link.setWeight(5);
		
		
		return model;

	}
	
	public static DagModel buildComplexModel() {
		
		DagModelFactory factory = new DagModelFactory();
		DagModel model = factory.newModel();
		
		model.addNode("start");
		
		model.addNode("A");
		
		model.addNode("B");
		
		model.addNode("C");
		
		model.addNode("D");
		
		model.addNode("finish");
		
		DagLink link = model.addDefaultRelationship(
				model.getNode("start"), 
				model.getNode("A"));
		link.setWeight(6);

		
		link = model.addDefaultRelationship(
				model.getNode("start"), 
				model.getNode("B"));
		link.setWeight(2);
		
		link = model.addDefaultRelationship(
				model.getNode("start"), 
				model.getNode("C"));
		link.setWeight(3);
		

		link = model.addDefaultRelationship(
				model.getNode("B"), 
				model.getNode("A"));
		link.setWeight(3);

		link = model.addDefaultRelationship(
				model.getNode("C"), 
				model.getNode("B"));
		link.setWeight(1);
		

		
		link = model.addDefaultRelationship(
				model.getNode("A"), 
				model.getNode("D"));
		link.setWeight(4);

		
		link = model.addDefaultRelationship(
				model.getNode("B"), 
				model.getNode("D"));
		link.setWeight(8);
		
		
		link = model.addDefaultRelationship(
				model.getNode("D"), 
				model.getNode("finish"));
		link.setWeight(3);
		
		
		link = model.addDefaultRelationship(
				model.getNode("C"), 
				model.getNode("finish"));
		link.setWeight(13);
		
		
		return model;

		
		
		
		
	}
}
