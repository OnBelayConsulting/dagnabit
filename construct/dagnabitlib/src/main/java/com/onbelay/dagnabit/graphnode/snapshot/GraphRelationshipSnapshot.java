package com.onbelay.dagnabit.graphnode.snapshot;


import com.onbelay.core.entity.snapshot.AbstractSnapshot;
import com.onbelay.core.entity.snapshot.EntityId;

public class GraphRelationshipSnapshot extends AbstractSnapshot {

    private EntityId fromNodeId =new EntityId();
    private String fromCategory;

    private EntityId toNodeId = new EntityId();
    private String toCategory;

    private GraphRelationshipDetail detail = new GraphRelationshipDetail();

    public EntityId getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(EntityId fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public EntityId getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(EntityId toNodeId) {
        this.toNodeId = toNodeId;
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
