package com.onbelay.dagnabitapp.graph.assembler;

import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabitapp.graph.snapshot.DagNodeSnapshot;

import java.util.List;
import java.util.stream.Collectors;

public class DagNodeSnapshotAssembler {

    public DagNodeSnapshot assemble(DagNode dagNode) {
        DagNodeSnapshot snapshot = new DagNodeSnapshot();
        snapshot.setName(dagNode.getName());
        snapshot.setCategory(dagNode.getCategory().getCategoryName());
        snapshot.setWeight(dagNode.getWeight());
        snapshot.setReferenceNo(dagNode.getReferenceNo());
        return snapshot;
    }

    public List<DagNodeSnapshot> assemble(List<DagNode> nodes) {
        return nodes
                .stream()
                .map(c-> assemble(c))
                .collect(Collectors.toUnmodifiableList());
    }

}
