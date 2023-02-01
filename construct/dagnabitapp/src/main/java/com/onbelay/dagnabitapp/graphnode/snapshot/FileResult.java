package com.onbelay.dagnabitapp.graphnode.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FileResult {
    private String fileName;
    private String errorCode = "0";
    private String parms = "";
    private byte[] contents;

    public FileResult() {
    }

    public FileResult(String fileName, byte[] contents) {
        this.fileName = fileName;
        this.contents = contents;
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

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
