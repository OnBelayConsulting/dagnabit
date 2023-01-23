package com.onbelay.dagnabitapp.graph.snapshot;

import com.onbelay.dagnabitapp.common.snapshot.AbstractEntityCollection;

import java.util.List;

public class GraphModelCollection extends AbstractEntityCollection<GraphModelSnapshot> {
    private static final String name = "GraphModel";
    private String filter;

    public GraphModelCollection(
            int start,
            int limit,
            int total) {

        super(
                name,
                start,
                limit,
                total);
    }

    public GraphModelCollection(
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


    public GraphModelCollection(
            int start,
            int limit,
            int total,
            List<GraphModelSnapshot> items) {

        super(
                name,
                start,
                limit,
                total,
                items);
    }


    public GraphModelCollection(
            String filter,
            int start,
            int limit,
            int total,
            List<GraphModelSnapshot> items) {

        super(
                name,
                start,
                limit,
                total,
                items);
        this.filter = filter;
    }


    public GraphModelCollection(String errorCode) {
        super(errorCode);
    }

    public GraphModelCollection(String errorCode, String parms) {
        super(errorCode, parms);
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
