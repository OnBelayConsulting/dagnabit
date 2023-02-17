package com.onbelay.dagnabitapp.dagmodel.snapshot;

import com.onbelay.core.entity.snapshot.AbstractSnapshotCollection;

import java.util.List;

public class DagNodeCollection extends AbstractSnapshotCollection<DagNodeSnapshot> {
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
        super(
                name,
                errorCode);
    }

    public DagNodeCollection(String errorCode, List<String> parms) {
        super(
                name,
                errorCode,
                parms);
    }
}


