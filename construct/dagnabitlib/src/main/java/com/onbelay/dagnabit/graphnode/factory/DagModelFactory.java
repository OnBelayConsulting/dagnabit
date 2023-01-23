package com.onbelay.dagnabit.graphnode.factory;

import com.onbelay.dagnabit.graph.components.DagModelImpl;
import com.onbelay.dagnabit.graph.model.DagModel;

import java.util.List;

public interface DagModelFactory {


	/**
	 * Build an empty DagModel
	 * @param modelName
	 * @return
	 */
	public DagModel newModel(String modelName);


	/**
	 * Build a model based on persisted nodes and relationships
	 * @param modelName
	 * @param selectingQuery - a definedQuery that selects relationships
	 * @return
	 */
	public DagModel newModel(String modelName, String selectingQuery);


	public DagModel findModel(String modelName);

	public List<DagModel> findModels();

	/**
	 * Remove a model if it exists under the given name
	 * @param modelName - identifies the model
	 */
	public void removeModel(String modelName);

    void cleanUp();
}
