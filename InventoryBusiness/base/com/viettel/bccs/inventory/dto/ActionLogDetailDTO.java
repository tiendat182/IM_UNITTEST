package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class ActionLogDetailDTO extends BaseDTO implements Serializable {

    private Date actionDate;
    private Long actionDetailId;
    private Long actionId;
    private String columnName;
    private String description;
    private String newValue;
    private String oldValue;
    private String tableName;

    public String getKeySet() {
        return keySet; }
    public Date getActionDate() {
        return this.actionDate;
    }
    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }
    public Long getActionDetailId() {
        return this.actionDetailId;
    }
    public void setActionDetailId(Long actionDetailId) {
        this.actionDetailId = actionDetailId;
    }
    public Long getActionId() {
        return this.actionId;
    }
    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }
    public String getColumnName() {
        return this.columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getNewValue() {
        return this.newValue;
    }
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    public String getOldValue() {
        return this.oldValue;
    }
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    public String getTableName() {
        return this.tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
