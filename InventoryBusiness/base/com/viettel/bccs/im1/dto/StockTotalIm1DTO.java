package com.viettel.bccs.im1.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockTotalIm1DTO extends BaseDTO implements Serializable {

    private Long ownerId;
    private Long ownerType;
    private Long stockModelId;
    private Long stateId;
    private Long quantity;
    private Long quantityIssue;
    private Date modifiedDate;
    private Long status;
    private Long quantityDial;
    private Long maxDebit;
    private Long currentDebit;
    private Long dateReset;
    private Long limitQuantity;
    private Long quantityHang;
    private String stockModelName;
    public Long getOwnerId() {
        return this.ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Long getOwnerType() {
        return this.ownerType;
    }
    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }
    public Long getStockModelId() {
        return this.stockModelId;
    }
    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public String getStockModelName() {
        return stockModelName;
    }

    public void setStockModelName(String stockModelName) {
        this.stockModelName = stockModelName;
    }

    public Long getStateId() {
        return this.stateId;
    }
    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
    public Long getQuantity() {
        return this.quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public Long getQuantityIssue() {
        return this.quantityIssue;
    }
    public void setQuantityIssue(Long quantityIssue) {
        this.quantityIssue = quantityIssue;
    }
    public Date getModifiedDate() {
        return this.modifiedDate;
    }
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    public Long getStatus() {
        return this.status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public Long getQuantityDial() {
        return this.quantityDial;
    }
    public void setQuantityDial(Long quantityDial) {
        this.quantityDial = quantityDial;
    }
    public Long getMaxDebit() {
        return this.maxDebit;
    }
    public void setMaxDebit(Long maxDebit) {
        this.maxDebit = maxDebit;
    }
    public Long getCurrentDebit() {
        return this.currentDebit;
    }
    public void setCurrentDebit(Long currentDebit) {
        this.currentDebit = currentDebit;
    }
    public Long getDateReset() {
        return this.dateReset;
    }
    public void setDateReset(Long dateReset) {
        this.dateReset = dateReset;
    }
    public Long getLimitQuantity() {
        return this.limitQuantity;
    }
    public void setLimitQuantity(Long limitQuantity) {
        this.limitQuantity = limitQuantity;
    }
    public Long getQuantityHang() {
        return this.quantityHang;
    }
    public void setQuantityHang(Long quantityHang) {
        this.quantityHang = quantityHang;
    }

    public String getKeySet() {
        return keySet;
    }
}
