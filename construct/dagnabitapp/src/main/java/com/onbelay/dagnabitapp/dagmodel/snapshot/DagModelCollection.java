package com.onbelay.dagnabitapp.dagmodel.snapshot;

import com.onbelay.core.entity.snapshot.AbstractSnapshotCollection;

import java.util.List;

public class DagModelCollection extends AbstractSnapshotCollection<DagModelSnapshot> {
    private static final String name = "GraphModel";
    private String filter;

    public DagModelCollection() {
    }

    public DagModelCollection(
            int start,
            int limit,
            int total) {

        super(
                name,
                start,
                limit,
                total);
    }

    public DagModelCollection(
            String filter,
            int start,
            int limit,
            int total) {

        super(
                name,
                start,
                limit,
                total);
        this.filter = filter;
    }


    public DagModelCollection(
            int start,
            int limit,
            int total,
            List<DagModelSnapshot> items) {

        super(
                name,
                start,
                limit,
                total,
                items);
    }


    public DagModelCollection(
            String filter,
            int start,
            int limit,
            int total,
            List<DagModelSnapshot> items) {

        super(
                name,
                start,
                limit,
                total,
                items);
        this.filter = filter;
    }


    public DagModelCollection(String errorCode) {
        super(
                name,
                errorCode);
    }

    public DagModelCollection(String errorCode, List<String> parms) {
        super(
                name,
                errorCode,
                parms);
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
