package com.onbelay.dagnabitapp.dagmodel.assembler;

import com.onbelay.dagnabit.dagmodel.model.DagModel;
import com.onbelay.dagnabitapp.dagmodel.snapshot.DagModelSnapshot;

import java.util.List;
import java.util.stream.Collectors;

public class DagModelSnapshotAssembler {

    public DagModelSnapshot assemble (DagModel model) {
        DagModelSnapshot snapshot = new DagModelSnapshot();
        snapshot.setName(model.getModelName());
        return snapshot;
    }

    public List<DagModelSnapshot> assemble(List<DagModel> models) {
        return models
                .stream()
                .map(c-> assemble(c))
                .collect(Collectors.toUnmodifiableList());
    }

}
