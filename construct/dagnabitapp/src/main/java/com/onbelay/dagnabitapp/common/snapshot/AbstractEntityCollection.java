package com.onbelay.dagnabitapp.common.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityCollection<T> {

    private String entityName;
    private String errorCode = "0";
    private String parms = "";

    private int start;
    private int limit;
    private int total;

    private List<T> items = new ArrayList<>();

    public AbstractEntityCollection() { }

    public AbstractEntityCollection(
            String entityName,
            int start,
            int limit,
            int total) {

        this.entityName = entityName;
        this.start = start;
        this.limit = limit;
        this.total = total;
    }

    public AbstractEntityCollection(
            String entityName,
            int start,
            int limit,
            int total,
            List<T> items) {

        this.entityName = entityName;
        this.start = start;
        this.limit = limit;
        this.total = total;
        this.items = items;
    }

    public AbstractEntityCollection(String errorCode) {
        this.errorCode = errorCode;
    }

    public AbstractEntityCollection(
            String errorCode,
            String parms) {

        this.errorCode = errorCode;
        this.parms = parms;
    }

    @JsonIgnore
    public boolean wasSuccessful() {
        return errorCode.equals("0");
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getParms() {
        return parms;
    }

    public void setParms(String parms) {
        this.parms = parms;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
