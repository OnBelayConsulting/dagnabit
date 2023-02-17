package com.onbelay.dagnabitapp.dagmodel.adapterimpl;

import com.onbelay.dagnabit.dagmodel.factory.DagModelFactory;
import com.onbelay.dagnabit.dagmodel.model.*;
import com.onbelay.dagnabitapp.common.adapterimpl.BaseRestAdapterBean;
import com.onbelay.dagnabitapp.dagmodel.adapter.DagModelRestAdapter;
import com.onbelay.dagnabitapp.dagmodel.assembler.DagModelSnapshotAssembler;
import com.onbelay.dagnabitapp.dagmodel.assembler.DagNodeSnapshotAssembler;
import com.onbelay.dagnabitapp.dagmodel.snapshot.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DagModelRestAdapterBean extends BaseRestAdapterBean implements DagModelRestAdapter {

    @Autowired
    private DagModelFactory dagModelFactory;

    @Override
    public ModelResult createModel(DagModelSnapshot snapshot) {
        DagModel dagModel = dagModelFactory.newModel(snapshot.getName(), snapshot.getSelectingQuery());
        int total = dagModel.getRelationships().size();
        return new ModelResult(snapshot.getName(), total);
    }

    @Override
    public ModelResult addNodes(
            String modelName,
            List<DagNodeSnapshot> snapshots) {

        logUser();

        DagModel dagModel = dagModelFactory.findModel(modelName);

        for (DagNodeSnapshot snapshot : snapshots) {
            DagNode dagNode = dagModel.addNode(snapshot.getName(), snapshot.getCategory());
            if (snapshot.getWeight() != null)
                dagNode.setWeight(snapshot.getWeight());

            dagNode.setReferenceNo(snapshot.getReferenceNo());
        }

        return new ModelResult();
    }

    @Override
    public ModelResult addRelationships(
            String modelName,
            List<DagRelationshipSnapshot> relationships) {
        logUser();

        DagModel dagModel = dagModelFactory.findModel(modelName);

        for (DagRelationshipSnapshot snapshot : relationships) {
            DagRelationship dagRelationship = dagModel.addRelationship(
                    dagModel.getNode(snapshot.getFromNodeName()),
                    snapshot.getRelationshipName(),
                    dagModel.getNode(snapshot.getToNodeName()));
            dagRelationship.setReferenceNo(snapshot.getReferenceNo());
            dagRelationship.setWeight(snapshot.getWeight());

        }

        return new ModelResult();
    }

    @Override
    public DagModelCollection findGraphModels(
            int start,
            int limit,
            String filter) {
        logUser();

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
            return new DagModelCollection(
                    start,
                    limit,
                    models.size());

        if (endIndex > totalModels)
            endIndex = totalModels;

        List<DagModel> selected = models.subList(start, endIndex);
        DagModelSnapshotAssembler assembler = new DagModelSnapshotAssembler();
        return new DagModelCollection(
                start,
                limit,
                totalModels,
                assembler.assemble(selected));
    }

    @Override
    public DagNodeCollection findDescendants(
            String modelName,
            String nodeRootName,
            String relationshipName,
            int start,
            int limit) {

        logUser();

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
        DagNodeSnapshotAssembler assembler = new DagNodeSnapshotAssembler();

        return new DagNodeCollection(
                start,
                limit,
                nodes.size(),
                assembler.assemble(selected));
    }

    @Override
    public DagNodeCollection findRootNodes(
            String modelName,
            int start,
            int limit) {

        logUser();

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
        DagNodeSnapshotAssembler assembler = new DagNodeSnapshotAssembler();

        return new DagNodeCollection(
                start,
                limit,
                nodes.size(),
                assembler.assemble(selected));
    }

    @Override
    public DagNodePathCollection fetchCycleReport(
            String modelName,
            String relationshipName) {
        DagModel dagModel = dagModelFactory.findModel(modelName);

        logUser();

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
