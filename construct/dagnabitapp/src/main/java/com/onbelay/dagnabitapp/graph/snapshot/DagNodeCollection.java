package com.onbelay.dagnabitapp.graph.snapshot;

import com.onbelay.dagnabit.graph.model.DagNode;
import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabitapp.common.snapshot.AbstractEntityCollection;

import java.util.List;

public class DagNodeCollection extends AbstractEntityCollection<DagNodeSnapshot> {
    private static final String name = "DagNode";

    public DagNodeCollection() {
    }

    public DagNodeCollection(
            int start,
            int limit,
            int total) {

        super(
                name,
                start,
                limit,
                total);
    }

    public DagNodeCollection(
            int start,
            int limit,
            int total,
            List<DagNodeSnapshot> items) {

        super(
                name,
                start,
                limit,
                total,
                items);
    }

    public DagNodeCollection(String errorCode) {
        super(errorCode);
    }

    public DagNodeCollection(String errorCode, String parms) {
        super(errorCode, parms);
    }
}


