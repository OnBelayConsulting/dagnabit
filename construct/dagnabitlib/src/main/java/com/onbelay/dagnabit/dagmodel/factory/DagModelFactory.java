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


	public DagModel findModel(String modelName);

	public List<DagModel> findModels();

	/**
	 * Remove a model if it exists under the given name
	 * @param modelName - identifies the model
	 */
	public void removeModel(String modelName);

    void cleanUp();
}
