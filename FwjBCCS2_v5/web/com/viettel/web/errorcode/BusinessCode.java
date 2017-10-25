package com.viettel.web.errorcode;

/**
 * Created by thiendn1 on 20/4/2016.
 */
public class BusinessCode {
    private String id;
    private String action;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
