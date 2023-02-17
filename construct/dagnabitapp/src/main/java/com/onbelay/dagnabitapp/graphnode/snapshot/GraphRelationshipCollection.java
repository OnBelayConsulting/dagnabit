package com.onbelay.dagnabitapp.graphnode.snapshot;

import com.onbelay.core.entity.snapshot.AbstractSnapshotCollection;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;

import java.util.List;

public class GraphRelationshipCollection extends AbstractSnapshotCollection<GraphRelationshipSnapshot> {
    public static final String name = "GraphRelationship";

    public GraphRelationshipCollection(
            int start,
            int limit,
            int total,
            List<GraphRelationshipSnapshot> items) {

        super(
                name,
                start,
                limit,
                total,
                items);
    }

    public GraphRelationshipCollection(
            int start,
            int limit,
            int total) {

        super(
                name,
                start,
                limit,
                total);
    }

    public GraphRelationshipCollection() {
    }

    public GraphRelationshipCollection(String errorCode) {
        super(
                name,
                errorCode);
    }

    public GraphRelationshipCollection(
            String errorCode,
            List<String> parms) {

        super(
                name,
                errorCode,
                parms);
    }
}
