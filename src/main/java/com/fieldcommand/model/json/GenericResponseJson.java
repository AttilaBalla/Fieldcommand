package com.fieldcommand.model.json;

public class GenericResponseJson {

    public boolean success;
    public String information;

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
