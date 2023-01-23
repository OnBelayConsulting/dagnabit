package com.onbelay.dagnabitapp.graph.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ModelResult {

    private String errorCode = "0";
    private String parms;
    private String modelName;
    private int totalRelationshipsSelected;


    public ModelResult() { }
    public ModelResult(String modelName, int totalRelationshipsSelected) {
        this.modelName = modelName;
        this.totalRelationshipsSelected = totalRelationshipsSelected;
    }

    public ModelResult(String errorCode, String parms) {
        this.errorCode = errorCode;
        this.parms = parms;
    }

    public ModelResult(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @JsonIgnore
    public boolean wasSuccessful() {
        return errorCode.equals("0");
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getTotalRelationshipsSelected() {
        return totalRelationshipsSelected;
    }

    public void setTotalRelationshipsSelected(int totalRelationshipsSelected) {
        this.totalRelationshipsSelected = totalRelationshipsSelected;
    }
}
