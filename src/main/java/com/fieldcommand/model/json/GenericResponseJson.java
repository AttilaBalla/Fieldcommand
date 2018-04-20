package com.fieldcommand.model.json;

public class GenericResponseJson { // others should extend this class

    private boolean success;
    private String information;

    public GenericResponseJson() {

    }

    public GenericResponseJson(boolean success, String information) {
        this.success = success;
        this.information = information;
    }

    public GenericResponseJson(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
