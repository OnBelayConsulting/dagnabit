package com.onbelay.dagnabit.graphnode.snapshot;

import com.onbelay.core.exception.OBValidationException;
import com.onbelay.dagnabit.enums.TransactionErrorCode;

import javax.persistence.Column;

public class GraphNodeDetail {

    private String name;
    private String category;
    private String data;
    private Integer weight;

    public void applyDefaults() {
        category = "default";
        weight = 1;
    }

    public void shallowCopyFrom(GraphNodeDetail copy) {
        if (copy.category != null)
            this.category = copy.category;

        if (copy.name != null)
            this.name = copy.name;

        if (copy.data != null)
            this.data = copy.data;

        if (copy.weight != null)
            this.weight = copy.weight;

    }

    public void validate() {
        if (this.name == null)
            throw new OBValidationException(TransactionErrorCode.MISSING_GRAPH_NODE_NAME.getCode());
        if (this.category == null)
            throw new OBValidationException(TransactionErrorCode.MISSING_GRAPH_NODE_CATEGORY.getCode());
    }

    @Column(name = "NODE_CATEGORY")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "NODE_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "NODE_DATA")
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
}
