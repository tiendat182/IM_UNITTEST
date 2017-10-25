package com.viettel.bccs.inventory.common;

public class BaseMsg {
    private String response;
    private String description;

    public BaseMsg(String response, String description) {
        this.response = response;
        this.description = description;
    }

    public BaseMsg() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BaseMsg{" +
                "response='" + response + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
