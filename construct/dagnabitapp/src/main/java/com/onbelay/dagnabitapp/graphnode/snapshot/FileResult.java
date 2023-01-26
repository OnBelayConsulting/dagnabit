package com.onbelay.dagnabitapp.graphnode.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FileResult {
    private String errorCode = "0";
    private String parms = "";

    public FileResult() {
    }

    @JsonIgnore
    public boolean wasSuccessful() {
        return errorCode.equals("0");
    }

    public FileResult(String errorCode) {
        this.errorCode = errorCode;
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
}
