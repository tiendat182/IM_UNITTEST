package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class ChangeModelTransDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Date approveDate;
    private Long approveUserId;
    private Long changeModelTransId;
    private Date createDate;
    private Long createUserId;
    private Long fromOwnerId;
    private Long fromOwnerType;
    private String listStockTransId;
    private Long status;
    private Long stockTransId;
    private Long toOwnerId;
    private Long toOwnerType;
    private Long requestType;

    private String shopCodeName;
    private String createOwnerName;
    private String actionCode;
    private String toOwnerName;
    private String requestStatus;
    private String approveOwnerName;
    private Long retry;
    private String errorCode;
    private String errorCodeDescription;

    public Long getRetry() {
        return retry;
    }

    public void setRetry(Long retry) {
        this.retry = retry;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCodeDescription() {
        return errorCodeDescription;
    }

    public void setErrorCodeDescription(String errorCodeDescription) {
        this.errorCodeDescription = errorCodeDescription;
    }

    public Date getApproveDate() {
        return this.approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Long getApproveUserId() {
        return this.approveUserId;
    }

    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }

    public Long getChangeModelTransId() {
        return this.changeModelTransId;
    }

    public void setChangeModelTransId(Long changeModelTransId) {
        this.changeModelTransId = changeModelTransId;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
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

    public String getListStockTransId() {
        return this.listStockTransId;
    }

    public void setListStockTransId(String listStockTransId) {
        this.listStockTransId = listStockTransId;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
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

    public Long getRequestType() {
        return requestType;
    }

    public void setRequestType(Long requestType) {
        this.requestType = requestType;
    }

    public String getShopCodeName() {
        return shopCodeName;
    }

    public void setShopCodeName(String shopCodeName) {
        this.shopCodeName = shopCodeName;
    }

    public String getCreateOwnerName() {
        return createOwnerName;
    }

    public void setCreateOwnerName(String createOwnerName) {
        this.createOwnerName = createOwnerName;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getApproveOwnerName() {
        return approveOwnerName;
    }

    public void setApproveOwnerName(String approveOwnerName) {
        this.approveOwnerName = approveOwnerName;
    }
}
