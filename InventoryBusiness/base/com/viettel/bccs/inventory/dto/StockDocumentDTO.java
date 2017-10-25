package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.util.Date;

public class StockDocumentDTO extends BaseDTO {

    private String actionCode;
    private Long actionId;
    private Date createDate;
    private byte[] deliveryRecords;
    private String deliveryRecordsName;
    private Date destroyDate;
    private String destroyUser;
    private byte[] exportNote;
    private String exportNoteName;
    private String fromShopCode;
    private String reason;
    private Short status;
    private Long stockDocumentId;
    private String toShopCode;
    private String userCreate;
    private String tel;

    public String getKeySet() {
        return keySet; }
    public String getActionCode() {
        return this.actionCode;
    }
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    public Long getActionId() {
        return this.actionId;
    }
    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public byte[] getDeliveryRecords() {
        return this.deliveryRecords;
    }
    public void setDeliveryRecords(byte[] deliveryRecords) {
        this.deliveryRecords = deliveryRecords;
    }
    public String getDeliveryRecordsName() {
        return this.deliveryRecordsName;
    }
    public void setDeliveryRecordsName(String deliveryRecordsName) {
        this.deliveryRecordsName = deliveryRecordsName;
    }
    public Date getDestroyDate() {
        return this.destroyDate;
    }
    public void setDestroyDate(Date destroyDate) {
        this.destroyDate = destroyDate;
    }
    public String getDestroyUser() {
        return this.destroyUser;
    }
    public void setDestroyUser(String destroyUser) {
        this.destroyUser = destroyUser;
    }
    public byte[] getExportNote() {
        return this.exportNote;
    }
    public void setExportNote(byte[] exportNote) {
        this.exportNote = exportNote;
    }
    public String getExportNoteName() {
        return this.exportNoteName;
    }
    public void setExportNoteName(String exportNoteName) {
        this.exportNoteName = exportNoteName;
    }
    public String getFromShopCode() {
        return this.fromShopCode;
    }
    public void setFromShopCode(String fromShopCode) {
        this.fromShopCode = fromShopCode;
    }
    public String getReason() {
        return this.reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public Short getStatus() {
        return this.status;
    }
    public void setStatus(Short status) {
        this.status = status;
    }
    public Long getStockDocumentId() {
        return this.stockDocumentId;
    }
    public void setStockDocumentId(Long stockDocumentId) {
        this.stockDocumentId = stockDocumentId;
    }
    public String getToShopCode() {
        return this.toShopCode;
    }
    public void setToShopCode(String toShopCode) {
        this.toShopCode = toShopCode;
    }
    public String getUserCreate() {
        return this.userCreate;
    }
    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
