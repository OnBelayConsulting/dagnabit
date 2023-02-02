package com.onbelay.dagnabit.dagmodel.examples.shortestpath;

import com.onbelay.dagnabit.dagmodel.components.DagModelImpl;
import com.onbelay.dagnabit.dagmodel.model.DagModel;
import com.onbelay.dagnabit.dagmodel.model.DagRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelFixture {
	private static Logger logger = LoggerFactory.getLogger(ModelFixture.class);

	public static DagModel buildModel() {
		DagModel model = new DagModelImpl("test");
		
		model.addNode("start");
		
		model.addNode("A");
		
		model.addNode("B");
		
		model.addNode("finish");
		
		DagRelationship link = model.addDefaultRelationship(
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

		DagModel model = new DagModelImpl("test");
		
		model.addNode("start");
		
		model.addNode("A");
		
		model.addNode("B");
		
		model.addNode("C");
		
		model.addNode("D");
		
		model.addNode("finish");
		
		DagRelationship link = model.addDefaultRelationship(
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
