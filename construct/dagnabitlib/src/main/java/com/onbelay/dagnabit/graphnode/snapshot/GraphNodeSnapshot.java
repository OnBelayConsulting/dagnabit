package com.onbelay.dagnabit.graphnode.snapshot;

import com.onbelay.dagnabit.common.snapshot.AbstractSnapshot;

public class GraphNodeSnapshot extends AbstractSnapshot {

    private GraphNodeDetail detail = new GraphNodeDetail();

    public GraphNodeDetail getDetail() {
        return detail;
    }

    public void setDetail(GraphNodeDetail detail) {
        this.detail = detail;
    }
}
