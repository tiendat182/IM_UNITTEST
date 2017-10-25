package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockRequestOrderTransDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Date createDatetime;
    private String errorCode;
    private String errorCodeDescription;
    private Long fromOwnerId;
    private Long fromOwnerType;
    private Long retry;
    private Long status;
    private Long stockRequestOrderId;
    private Long stockRequestOrderTransId;
    private Long stockTransId;
    private Long toOwnerId;
    private Long toOwnerType;
    private Long stockTransType;

    public Long getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(Long stockTransType) {
        this.stockTransType = stockTransType;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCodeDescription() {
        return this.errorCodeDescription;
    }

    public void setErrorCodeDescription(String errorCodeDescription) {
        this.errorCodeDescription = errorCodeDescription;
    }

    public Long getFromOwnerId() {
        return this.fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getFromOwnerType() {
        return this.fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getRetry() {
        return this.retry;
    }

    public void setRetry(Long retry) {
        this.retry = retry;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStockRequestOrderId() {
        return this.stockRequestOrderId;
    }

    public void setStockRequestOrderId(Long stockRequestOrderId) {
        this.stockRequestOrderId = stockRequestOrderId;
    }

    public Long getStockRequestOrderTransId() {
        return this.stockRequestOrderTransId;
    }

    public void setStockRequestOrderTransId(Long stockRequestOrderTransId) {
        this.stockRequestOrderTransId = stockRequestOrderTransId;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getToOwnerId() {
        return this.toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Long getToOwnerType() {
        return this.toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }
}
