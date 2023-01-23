package com.onbelay.dagnabit.graphnode.snapshot;

import com.onbelay.dagnabit.common.snapshot.AbstractSnapshot;

public class GraphRelationshipSnapshot extends AbstractSnapshot {

    private Integer fromNodeId;
    private String fromNodeName;
    private String fromCategory;

    private Integer toNodeId;
    private String toNodeName;
    private String toCategory;

    private GraphRelationshipDetail detail = new GraphRelationshipDetail();

    public Integer getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(Integer fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public String getFromNodeName() {
        return fromNodeName;
    }

    public void setFromNodeName(String fromNodeName) {
        this.fromNodeName = fromNodeName;
    }

    public Integer getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(Integer toNodeId) {
        this.toNodeId = toNodeId;
    }

    public String getToNodeName() {
        return toNodeName;
    }

    public void setToNodeName(String toNodeName) {
        this.toNodeName = toNodeName;
    }

    public GraphRelationshipDetail getDetail() {
        return detail;
    }

    public void setDetail(GraphRelationshipDetail detail) {
        this.detail = detail;
    }

    public String getFromCategory() {
        return fromCategory;
    }

    public void setFromCategory(String fromCategory) {
        this.fromCategory = fromCategory;
    }

    public String getToCategory() {
        return toCategory;
    }

    public void setToCategory(String toCategory) {
        this.toCategory = toCategory;
    }
}
