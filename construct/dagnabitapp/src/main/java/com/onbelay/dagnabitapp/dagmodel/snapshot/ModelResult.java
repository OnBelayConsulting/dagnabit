package com.onbelay.dagnabitapp.dagmodel.snapshot;

import com.onbelay.core.entity.snapshot.ErrorHoldingSnapshot;

import java.util.List;

public class ModelResult extends ErrorHoldingSnapshot {

    private String modelName;
    private int totalRelationshipsSelected;


    public ModelResult() { }
    public ModelResult(String modelName, int totalRelationshipsSelected) {
        this.modelName = modelName;
        this.totalRelationshipsSelected = totalRelationshipsSelected;
    }

    public ModelResult(String errorCode, List<String> parms) {
        super(errorCode,parms );
    }

    public ModelResult(String errorCode) {
        super(errorCode);
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
