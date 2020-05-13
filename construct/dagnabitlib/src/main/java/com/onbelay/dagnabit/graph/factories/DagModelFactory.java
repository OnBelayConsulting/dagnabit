package com.onbelay.dagnabit.graph.factories;

import com.onbelay.dagnabit.graph.components.DagModelImpl;
import com.onbelay.dagnabit.graph.model.DagModel;

public class DagModelFactory {
	
	private boolean createDefaultLink = true;
	
	public DagModelFactory() {
		
	}
	
	public DagModelFactory(boolean createDefaultLink) {
		super();
		this.createDefaultLink = createDefaultLink;
	}



	public DagModel newModel() {
		return new DagModelImpl(createDefaultLink);
	}

	public boolean isCreateDefaultLink() {
		return createDefaultLink;
	}
	
	
	
}
