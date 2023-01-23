package com.onbelay.dagnabit.common.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class TransactionResult {

    private String errorCode = "0";
    private String parms = "";

    private List<Integer> ids = new ArrayList<>();

    public TransactionResult() {
    }

    public TransactionResult(String errorCode) {
        this.errorCode = errorCode;
    }

    public TransactionResult(String errorCode, String parms) {
        this.errorCode = errorCode;
        this.parms = parms;
    }

    @JsonIgnore
    public boolean wasSuccessful() {
        return errorCode.equals("0");
    }

    public TransactionResult(Integer id) {
        ids.add(id);
    }

    public TransactionResult(List<Integer> ids) {
        this.ids = ids;
    }

    public Integer getId() {
        if (ids.size() > 0)
            return ids.get(0);
        else
            return null;
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

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
