package com.onbelay.dagnabitapp.graph.snapshot;

import com.onbelay.dagnabit.graph.model.DagNodePath;
import com.onbelay.dagnabitapp.common.snapshot.AbstractEntityCollection;

import java.util.List;

public class DagNodePathCollection extends AbstractEntityCollection<DagNodePath> {
    private static final String name = "DagNodePath";

    private boolean isCyclic = false;

    public DagNodePathCollection(
            int start,
            int limit,
            int total) {

        super(
                name,
                start,
                limit,
                total);
    }

    public DagNodePathCollection(
            int start,
            int limit,
            int total,
            List<DagNodePath> items) {

        super(
                name,
                start,
                limit,
                total,
                items);
    }

    public boolean isCyclic() {
        return isCyclic;
    }

    public void setCyclic(boolean cyclic) {
        isCyclic = cyclic;
    }

    public DagNodePathCollection(String errorCode) {
        super(errorCode);
    }

    public DagNodePathCollection(String errorCode, String parms) {
        super(errorCode, parms);
    }
}


