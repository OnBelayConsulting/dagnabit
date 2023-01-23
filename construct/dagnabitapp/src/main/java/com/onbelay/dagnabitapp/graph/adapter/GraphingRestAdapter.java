package com.onbelay.dagnabitapp.graph.adapter;

import com.onbelay.dagnabitapp.graph.snapshot.*;

public interface GraphingRestAdapter {

    ModelResult createModel(GraphModelSnapshot snapshot);


    GraphModelCollection findGraphModels(
            int start,
            int limit,
            String filter);

    /**
     * Return a report that documents any cycles in the model.
     * @param modelName = identifies an existing model.
     * @return
     */
    DagNodePathCollection fetchCycleReport(
            String modelName,
            String relationshipName);

    DagNodeCollection findDescendents(
            String modelName,
            String nodeRootName,
            String relationship,
            int start,
            int limit);

    DagNodeCollection findRootNodes(
            String modelName,
            int start,
            int limit);
}
