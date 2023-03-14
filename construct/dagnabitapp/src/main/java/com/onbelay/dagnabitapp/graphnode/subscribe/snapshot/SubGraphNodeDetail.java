package com.onbelay.dagnabitapp.graphnode.subscribe.snapshot;

import com.onbelay.core.exception.OBValidationException;
import com.onbelay.dagnabit.enums.TransactionErrorCode;

import javax.persistence.Column;

public class SubGraphNodeDetail {

    private String name;
    private String category;
    private String data;
    private Integer weight;
    private Integer externalReferenceId;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
