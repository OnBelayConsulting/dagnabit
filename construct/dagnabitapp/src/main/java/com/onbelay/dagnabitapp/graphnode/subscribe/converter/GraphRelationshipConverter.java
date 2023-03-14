package com.onbelay.dagnabitapp.graphnode.subscribe.converter;

import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabitapp.graphnode.subscribe.snapshot.SubGraphRelationshipSnapshot;

import java.util.List;
import java.util.stream.Collectors;

public class GraphRelationshipConverter {



    public List<GraphRelationshipSnapshot> convert(List<SubGraphRelationshipSnapshot> snapshotsIn) {
        return snapshotsIn
                .stream()
                .map( c-> convert(c))
                .collect(Collectors.toUnmodifiableList());
    }



    public GraphRelationshipSnapshot convert(SubGraphRelationshipSnapshot snapshotIn) {
        GraphRelationshipSnapshot snapshot = new GraphRelationshipSnapshot();
        snapshot.setEntityState(snapshotIn.getEntityState());

        snapshot.setFromNodeName(snapshotIn.getFromNodeName());
        snapshot.setToNodeName(snapshotIn.getToNodeName());

        snapshot.getDetail().setExternalReferenceId(snapshotIn.getDetail().getExternalReferenceId());
        snapshot.getDetail().setName(snapshotIn.getDetail().getName());
        snapshot.getDetail().setType(snapshotIn.getDetail().getType());
        snapshot.getDetail().setData(snapshotIn.getDetail().getData());
        snapshot.getDetail().setWeight(snapshotIn.getDetail().getWeight());
        return snapshot;
    }

}
