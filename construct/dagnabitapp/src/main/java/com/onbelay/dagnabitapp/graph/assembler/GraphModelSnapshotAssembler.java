package com.onbelay.dagnabitapp.graph.assembler;

import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabitapp.graph.snapshot.GraphModelSnapshot;

import java.util.List;
import java.util.stream.Collectors;

public class GraphModelSnapshotAssembler {

    public GraphModelSnapshot assemble (DagModel model) {
        GraphModelSnapshot snapshot = new GraphModelSnapshot();
        snapshot.setName(model.getModelName());
        return snapshot;
    }

    public List<GraphModelSnapshot> assemble(List<DagModel> models) {
        return models
                .stream()
                .map(c-> assemble(c))
                .collect(Collectors.toUnmodifiableList());
    }

}
