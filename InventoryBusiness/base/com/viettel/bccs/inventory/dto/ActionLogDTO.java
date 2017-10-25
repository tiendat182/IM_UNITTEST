package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class ActionLogDTO extends BaseDTO implements Serializable {

    private Date actionDate;
    private Long actionId;
    private String actionIp;
    private String actionType;
    private String actionUser;
    private String description;
    private Long objectId;

    public String getKeySet() {
        return keySet; }
    public Date getActionDate() {
        return this.actionDate;
    }
    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }
    public Long getActionId() {
        return this.actionId;
    }
    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }
    public String getActionIp() {
        return this.actionIp;
    }
    public void setActionIp(String actionIp) {
        this.actionIp = actionIp;
    }
    public String getActionType() {
        return this.actionType;
    }
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    public String getActionUser() {
        return this.actionUser;
    }
    public void setActionUser(String actionUser) {
        this.actionUser = actionUser;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getObjectId() {
        return this.objectId;
    }
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }
}
