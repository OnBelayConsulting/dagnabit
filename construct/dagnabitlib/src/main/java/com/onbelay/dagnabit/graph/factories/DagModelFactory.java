package com.onbelay.dagnabit.graph.factories;

import com.onbelay.dagnabit.graph.components.DagModelImpl;
import com.onbelay.dagnabit.graph.model.DagModel;

public class DagModelFactory {

	
	public DagModel newModel() {
		return new DagModelImpl();
	}
	
}
