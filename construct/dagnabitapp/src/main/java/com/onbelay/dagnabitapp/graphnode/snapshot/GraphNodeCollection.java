package com.onbelay.dagnabitapp.graphnode.snapshot;

import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabitapp.common.snapshot.AbstractEntityCollection;

import java.util.List;

public class GraphNodeCollection extends AbstractEntityCollection<GraphNodeSnapshot> {


    public GraphNodeCollection() {
        setEntityName("GraphNode");
    }

    public GraphNodeCollection(
            int start,
            int limit,
            int total,
            List<GraphNodeSnapshot> items) {

        super(
                "GraphNode",
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
                "GraphNode",
                start,
                limit,
                total);
    }

    public GraphNodeCollection(String errorCode) {
        super(errorCode);
    }

    public GraphNodeCollection(String errorCode, String parms) {
        super(errorCode, parms);
    }
}
