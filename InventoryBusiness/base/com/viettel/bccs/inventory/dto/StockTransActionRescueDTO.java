package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockTransActionRescueDTO extends BaseDTO implements Serializable {

    private Long actionId;
    private Long actionStaffId;
    private Long actionType;
    private Date createDate;
    private String description;
    private Long stockTransId;

    public String getKeySet() {
        return keySet; }
    public Long getActionId() {
        return this.actionId;
    }
    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }
    public Long getActionStaffId() {
        return this.actionStaffId;
    }
    public void setActionStaffId(Long actionStaffId) {
        this.actionStaffId = actionStaffId;
    }
    public Long getActionType() {
        return this.actionType;
    }
    public void setActionType(Long actionType) {
        this.actionType = actionType;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getStockTransId() {
        return this.stockTransId;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
}
