package com.fieldcommand.model.json;

public class GenericResponseJson { // others should extend this class

    private boolean success;
    private String information;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // getErrorInformation
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
