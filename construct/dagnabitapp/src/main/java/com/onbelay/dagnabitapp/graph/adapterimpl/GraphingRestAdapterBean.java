package com.onbelay.dagnabitapp.graph.adapterimpl;

import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabit.graph.model.LinkAnalysis;
import com.onbelay.dagnabit.graphnode.factory.DagModelFactory;
import com.onbelay.dagnabitapp.graph.adapter.GraphingRestAdapter;
import com.onbelay.dagnabitapp.graph.assembler.GraphModelSnapshotAssembler;
import com.onbelay.dagnabitapp.graph.snapshot.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraphingRestAdapterBean implements GraphingRestAdapter {

    @Autowired
    private DagModelFactory dagModelFactory;

    @Override
    public ModelResult createModel(GraphModelSnapshot snapshot) {
        DagModel dagModel = dagModelFactory.newModel(snapshot.getName(), snapshot.getSelectingQuery());
        int total = dagModel.getRelationships().size();
        return new ModelResult(snapshot.getName(), total);
    }

    @Override
    public GraphModelCollection findGraphModels(
            int start,
            int limit,
            String filter) {

        List<DagModel> models;

        if (filter != null && filter.isEmpty() == false) {
            models = dagModelFactory.findModels()
                    .stream()
                    .filter(c -> c.getModelName().startsWith(filter))
                    .sorted()
                    .collect(Collectors.toUnmodifiableList());
        } else {
            models = dagModelFactory.findModels()
                    .stream()
                    .sorted()
                    .collect(Collectors.toUnmodifiableList());
        }

        int totalModels = models.size();
        int endIndex = start + limit;
        if (totalModels == 0 || start > totalModels)
            return new GraphModelCollection(
                    start,
                    limit,
                    models.size());

        if (endIndex > totalModels)
            endIndex = totalModels;

        List<DagModel> selected = models.subList(start, endIndex);
        GraphModelSnapshotAssembler assembler = new GraphModelSnapshotAssembler();
        return new GraphModelCollection(
                start,
                limit,
                totalModels,
                assembler.assemble(selected));
    }

    @Override
    public DagNodeCollection findDescendents(
            String modelName,
            String nodeRootName,
            String relationshipName,
            int start,
            int limit) {

        DagModel dagModel = dagModelFactory.findModel(modelName);
        List<DagNode> nodes = dagModel
                .navigate()
                .from(dagModel.getNode(nodeRootName))
                .by(dagModel.getRelationshipType(relationshipName))
                .descendants();

        int totalModels = nodes.size();
        int endIndex = start + limit;
        if (totalModels == 0 || start > totalModels)
            return new DagNodeCollection(
                    start,
                    limit,
                    nodes.size());

        if (endIndex > totalModels)
            endIndex = totalModels;

        List<DagNode> selected = nodes.subList(start, endIndex);

        return new DagNodeCollection(
                start,
                limit,
                nodes.size(),
                selected);
    }

    @Override
    public DagNodeCollection findRootNodes(
            String modelName,
            int start,
            int limit) {

        DagModel dagModel = dagModelFactory.findModel(modelName);

        List<DagNode> nodes = dagModel.findRootNodes();
        int totalModels = nodes.size();
        int endIndex = start + limit;
        if (totalModels == 0 || start > totalModels)
            return new DagNodeCollection(
                    start,
                    limit,
                    nodes.size());

        if (endIndex > totalModels)
            endIndex = totalModels;

        List<DagNode> selected = nodes.subList(start, endIndex);

        return new DagNodeCollection(
                start,
                limit,
                nodes.size(),
                selected);
    }

    @Override
    public DagNodePathCollection fetchCycleReport(
            String modelName,
            String relationshipName) {
        DagModel dagModel = dagModelFactory.findModel(modelName);

        LinkAnalysis linkAnalysis = dagModel
                .analyse()
                .by(dagModel.getRelationshipType(relationshipName))
                .result();

        List<DagNodePath> paths = linkAnalysis.getCycles();
        DagNodePathCollection collection = new DagNodePathCollection(
                0,
                paths.size(),
                paths.size(),
                paths);

        collection.setCyclic(linkAnalysis.isCyclic());

        return collection;
    }
}
