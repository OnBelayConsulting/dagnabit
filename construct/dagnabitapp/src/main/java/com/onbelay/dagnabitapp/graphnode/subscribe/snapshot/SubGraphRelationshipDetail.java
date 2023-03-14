package com.onbelay.dagnabitapp.graphnode.subscribe.snapshot;

import com.onbelay.core.exception.OBValidationException;
import com.onbelay.dagnabit.enums.TransactionErrorCode;

import javax.persistence.Column;

public class SubGraphRelationshipDetail {

    private String name;
    private String type;
    private String data;
    private Integer weight;
    private Integer externalReferenceId;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getExternalReferenceId() {
        return externalReferenceId;
    }

    public void setExternalReferenceId(Integer externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
    }
}
