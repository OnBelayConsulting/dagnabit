package com.onbelay.dagnabitapp.graphnode.snapshot;

import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabitapp.common.snapshot.AbstractEntityCollection;

import java.util.List;

public class GraphRelationshipCollection extends AbstractEntityCollection<GraphRelationshipSnapshot> {

    public GraphRelationshipCollection(
            int start,
            int limit,
            int total,
            List<GraphRelationshipSnapshot> items) {

        super(
                "GraphRelationship",
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
                "GraphRelationship",
                start,
                limit,
                total);
    }

    public GraphRelationshipCollection() {
    }

    public GraphRelationshipCollection(String errorCode) {
        super(errorCode);
    }

    public GraphRelationshipCollection(String errorCode, String parms) {
        super(errorCode, parms);
    }
}
