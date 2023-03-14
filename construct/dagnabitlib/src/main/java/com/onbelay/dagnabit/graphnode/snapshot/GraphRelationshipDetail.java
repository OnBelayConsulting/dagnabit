package com.onbelay.dagnabit.graphnode.snapshot;

import com.onbelay.core.exception.OBValidationException;
import com.onbelay.dagnabit.enums.TransactionErrorCode;

import javax.persistence.Column;

public class GraphRelationshipDetail {

    private String name;
    private String type;
    private String data;
    private Integer weight;
    private Integer externalReferenceId;

    public void applyDefaults() {
        type = "default";
        weight = 1;
    }


    public void shallowCopyFrom(GraphRelationshipDetail copy) {
        if (copy.type != null)
            this.type = copy.type;

        if (copy.name != null)
            this.name = copy.name;

        if (copy.data != null)
            this.data = copy.data;

        if (copy.weight != null)
            this.weight = copy.weight;

        if (copy.externalReferenceId != null)
            this.externalReferenceId = copy.externalReferenceId;
    }

    public void validate() {
        if (this.name == null)
            throw new OBValidationException(TransactionErrorCode.MISSING_GRAPH_RELATIONSHIP_NAME.getCode());
        if (this.type == null)
            throw new OBValidationException(TransactionErrorCode.MISSING_GRAPH_RELATIONSHIP_TYPE.getCode());
    }

    @Column(name = "RELATIONSHIP_TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "RELATIONSHIP_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "RELATIONSHIP_DATA")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Column(name = "ITEM_WEIGHT")
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }


    @Column(name = "EXTERNAL_REF_ID")
    public Integer getExternalReferenceId() {
        return externalReferenceId;
    }

    public void setExternalReferenceId(Integer externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
    }

}
