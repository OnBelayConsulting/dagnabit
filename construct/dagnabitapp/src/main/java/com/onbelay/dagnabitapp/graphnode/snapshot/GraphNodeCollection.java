package com.onbelay.dagnabitapp.graphnode.snapshot;

import com.onbelay.core.entity.snapshot.AbstractSnapshotCollection;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;

import java.util.List;

public class GraphNodeCollection extends AbstractSnapshotCollection<GraphNodeSnapshot> {
    public static final String name = "GraphNode";

    public GraphNodeCollection() {
        super(name);
    }

    public GraphNodeCollection(
            int start,
            int limit,
            int total,
            List<GraphNodeSnapshot> items) {

        super(
                name,
                start,
                limit,
                total,
                items);
    }

    public GraphNodeCollection(
            int start,
            int limit,
            int total) {

        super(
                name,
                start,
                limit,
                total);
    }

    public GraphNodeCollection(String errorCode) {
        super(
                name,
                errorCode);
    }

    public GraphNodeCollection(
            String errorCode,
            List<String> parms) {

        super(
                name,
                errorCode,
                parms);
    }
}
