package com.onbelay.dagnabitapp.graphnode.subscribe.snapshot;


import com.onbelay.core.entity.snapshot.AbstractSnapshot;

public class SubGraphRelationshipSnapshot extends AbstractSnapshot {

    private String fromNodeName;
    private String toNodeName;

    private SubGraphRelationshipDetail detail = new SubGraphRelationshipDetail();


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

    public SubGraphRelationshipDetail getDetail() {
        return detail;
    }

    public void setDetail(SubGraphRelationshipDetail detail) {
        this.detail = detail;
    }
}
