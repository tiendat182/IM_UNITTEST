package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockTransActionDTO extends BaseDTO implements Serializable {

    private String actionCode;
    private Long actionStaffId;
    private Date createDatetime;
    private String createUser;
    private String documentStatus;
    private String logCmdCode;
    private String logNoteCode;
    private String note;
    private Long regionOwnerId;
    private String status;
    private Long stockTransActionId;
    private Long stockTransId;
    private String actionCodeShop;
    private String signCaStatus;
    private Boolean isRegionExchange = false;
    //them 2 bien fromDate, toDate cho Doi thiet b?
    private Date fromDate;
    private Date toDate;
    public String getKeySet() {
        return keySet; }
    public String getActionCode() {
        return this.actionCode;
    }
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    public Long getActionStaffId() {
        return this.actionStaffId;
    }
    public void setActionStaffId(Long actionStaffId) {
        this.actionStaffId = actionStaffId;
    }
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    public String getCreateUser() {
        return this.createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public String getDocumentStatus() {
        return this.documentStatus;
    }
    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }
    public String getLogCmdCode() {
        return this.logCmdCode;
    }
    public void setLogCmdCode(String logCmdCode) {
        this.logCmdCode = logCmdCode;
    }
    public String getLogNoteCode() {
        return this.logNoteCode;
    }
    public void setLogNoteCode(String logNoteCode) {
        this.logNoteCode = logNoteCode;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Long getRegionOwnerId() {
        return this.regionOwnerId;
    }
    public void setRegionOwnerId(Long regionOwnerId) {
        this.regionOwnerId = regionOwnerId;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Long getStockTransActionId() {
        return this.stockTransActionId;
    }
    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }
    public Long getStockTransId() {
        return this.stockTransId;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
    public String getActionCodeShop() {
        return actionCodeShop;
    }
    public void setActionCodeShop(String actionCodeShop) {
        this.actionCodeShop = actionCodeShop;
    }
    public String getSignCaStatus() {
        return signCaStatus;
    }
    public void setSignCaStatus(String signCaStatus) {
        this.signCaStatus = signCaStatus;
    }
    public Boolean getIsRegionExchange() {
        return isRegionExchange;
    }
    public void setIsRegionExchange(Boolean isRegionExchange) {
        this.isRegionExchange = isRegionExchange;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
