package com.onbelay.dagnabit.graphnode.snapshot;


import com.onbelay.core.entity.snapshot.AbstractSnapshot;
import com.onbelay.core.entity.snapshot.EntitySlot;

public class GraphRelationshipSnapshot extends AbstractSnapshot {

    private EntitySlot fromNodeId;
    private String fromNodeName;
    private String fromCategory;

    private EntitySlot toNodeId;
    private String toNodeName;
    private String toCategory;

    private GraphRelationshipDetail detail = new GraphRelationshipDetail();

    public EntitySlot getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(EntitySlot fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public EntitySlot getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(EntitySlot toNodeId) {
        this.toNodeId = toNodeId;
    }

    public String getFromNodeName() {
        return fromNodeName;
    }

    public void setFromNodeName(String fromNodeName) {
        this.fromNodeName = fromNodeName;
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
