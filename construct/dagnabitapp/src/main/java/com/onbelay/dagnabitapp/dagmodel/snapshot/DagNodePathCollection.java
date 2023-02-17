package com.onbelay.dagnabitapp.dagmodel.snapshot;

import com.onbelay.core.entity.snapshot.AbstractSnapshotCollection;
import com.onbelay.dagnabit.dagmodel.model.DagNodePath;

import java.util.List;

public class DagNodePathCollection extends AbstractSnapshotCollection<DagNodePath> {
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
        super(
                name,
                errorCode);
    }

    public DagNodePathCollection(
            String errorCode,
            List<String> parms) {

        super(
                name,
                errorCode,
                parms);
    }
}


