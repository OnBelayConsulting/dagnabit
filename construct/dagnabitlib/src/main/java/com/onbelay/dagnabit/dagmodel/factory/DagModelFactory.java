package com.onbelay.dagnabit.dagmodel.factory;

import com.onbelay.dagnabit.dagmodel.model.DagModel;

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
