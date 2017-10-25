package com.viettel.bccs.inventory.dto;

/**
 * Created by sinhhv on 1/11/2016.
 */
public class ManageTransStockViewDto {
    private Long stockTranId;
    private Long stockTranActionId;
    private String transCode;
    private String takeStockName;
    private String receiveStockName;
    private String status;
    private String reason;
    private String notes;
    private String createdStaff;
    private String createdDate;
    private Long actionStaffId;
    private Long fromOwnerId;
    private Long fromOwnerType;
    private Long toOwnerId;
    private Long toOwnerType;
    private Long reasonId;
    private String reasonCode;
    private String reasonName;

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getTakeStockName() {
        return takeStockName;
    }

    public void setTakeStockName(String takeStockName) {
        this.takeStockName = takeStockName;
    }

    public String getReceiveStockName() {
        return receiveStockName;
    }

    public void setReceiveStockName(String receiveStockName) {
        this.receiveStockName = receiveStockName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedStaff() {
        return createdStaff;
    }

    public void setCreatedStaff(String createdStaff) {
        this.createdStaff = createdStaff;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public Long getStockTranId() {
        return stockTranId;
    }

    public void setStockTranId(Long stockTranId) {
        this.stockTranId = stockTranId;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Long getActionStaffId() {
        return actionStaffId;
    }

    public void setActionStaffId(Long actionStaffId) {
        this.actionStaffId = actionStaffId;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Long getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonName() {
        return reasonName;
    }

    public Long getStockTranActionId() {
        return stockTranActionId;
    }

    public void setStockTranActionId(Long stockTranActionId) {
        this.stockTranActionId = stockTranActionId;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }
}
