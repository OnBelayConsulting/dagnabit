package com.onbelay.dagnabit.graphnode.snapshot;

import com.onbelay.dagnabit.enums.TransactionErrorCode;
import com.onbelay.dagnabit.graphnode.exception.GraphNodeException;

import javax.persistence.Column;

public class GraphNodeDetail {

    private String name;
    private String category;
    private String data;

    public void applyDefaults() {
        category = "default";
    }

    public void shallowCopyFrom(GraphNodeDetail copy) {
        if (copy.category != null)
            this.category = copy.category;

        if (copy.name != null)
            this.name = copy.name;

        if (copy.data != null)
            this.data = copy.data;

    }

    public void validate() {
        if (this.name == null)
            throw new GraphNodeException(TransactionErrorCode.MISSING_GRAPH_NODE_NAME.getCode());
        if (this.category == null)
            throw new GraphNodeException(TransactionErrorCode.MISSING_GRAPH_NODE_CATEGORY.getCode());
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
}
