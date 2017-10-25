package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockIsdnTransDTO extends BaseDTO implements Serializable {

    private Date createdTime;
    private String createdUserCode;
    private Long createdUserId;
    private String createdUserIp;
    private String createdUserName;
    private String fromOwnerCode;
    private Long fromOwnerId;
    private String fromOwnerName;
    private Long fromOwnerType;
    private Date lastUpdatedTime;
    private String lastUpdatedUserCode;
    private Long lastUpdatedUserId;
    private String lastUpdatedUserIp;
    private String lastUpdatedUserName;
    private String note;
    private Long quantity;
    private String reasonCode;
    private Long reasonId;
    private String reasonName;
    private Long status;
    private String stockIsdnTransCode;
    private Long stockIsdnTransId;
    private Long stockTypeId;
    private String stockTypeName;
    private String toOwnerCode;
    private Long toOwnerId;
    private String toOwnerName;
    private Long toOwnerType;

    public String getKeySet() {
        return keySet;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedUserCode() {
        return this.createdUserCode;
    }

    public void setCreatedUserCode(String createdUserCode) {
        this.createdUserCode = createdUserCode;
    }

    public Long getCreatedUserId() {
        return this.createdUserId;
    }

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserIp() {
        return this.createdUserIp;
    }

    public void setCreatedUserIp(String createdUserIp) {
        this.createdUserIp = createdUserIp;
    }

    public String getCreatedUserName() {
        return this.createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public String getFromOwnerCode() {
        return this.fromOwnerCode;
    }

    public void setFromOwnerCode(String fromOwnerCode) {
        this.fromOwnerCode = fromOwnerCode;
    }

    public Long getFromOwnerId() {
        return this.fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public String getFromOwnerName() {
        return this.fromOwnerName;
    }

    public void setFromOwnerName(String fromOwnerName) {
        this.fromOwnerName = fromOwnerName;
    }

    public Long getFromOwnerType() {
        return this.fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Date getLastUpdatedTime() {
        return this.lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getLastUpdatedUserCode() {
        return this.lastUpdatedUserCode;
    }

    public void setLastUpdatedUserCode(String lastUpdatedUserCode) {
        this.lastUpdatedUserCode = lastUpdatedUserCode;
    }

    public Long getLastUpdatedUserId() {
        return this.lastUpdatedUserId;
    }

    public void setLastUpdatedUserId(Long lastUpdatedUserId) {
        this.lastUpdatedUserId = lastUpdatedUserId;
    }

    public String getLastUpdatedUserIp() {
        return this.lastUpdatedUserIp;
    }

    public void setLastUpdatedUserIp(String lastUpdatedUserIp) {
        this.lastUpdatedUserIp = lastUpdatedUserIp;
    }

    public String getLastUpdatedUserName() {
        return this.lastUpdatedUserName;
    }

    public void setLastUpdatedUserName(String lastUpdatedUserName) {
        this.lastUpdatedUserName = lastUpdatedUserName;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getReasonCode() {
        return this.reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Long getReasonId() {
        return this.reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonName() {
        return this.reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getStockIsdnTransCode() {
        return this.stockIsdnTransCode;
    }

    public void setStockIsdnTransCode(String stockIsdnTransCode) {
        this.stockIsdnTransCode = stockIsdnTransCode;
    }

    public Long getStockIsdnTransId() {
        return this.stockIsdnTransId;
    }

    public void setStockIsdnTransId(Long stockIsdnTransId) {
        this.stockIsdnTransId = stockIsdnTransId;
    }

    public Long getStockTypeId() {
        return this.stockTypeId;
    }

    public void setStockTypeId(Long stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public String getStockTypeName() {
        return this.stockTypeName;
    }

    public void setStockTypeName(String stockTypeName) {
        this.stockTypeName = stockTypeName;
    }

    public String getToOwnerCode() {
        return this.toOwnerCode;
    }

    public void setToOwnerCode(String toOwnerCode) {
        this.toOwnerCode = toOwnerCode;
    }

    public Long getToOwnerId() {
        return this.toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public String getToOwnerName() {
        return this.toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public Long getToOwnerType() {
        return this.toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public String getFullLabelFromOwner() {
        if (!DataUtil.isNullObject(fromOwnerCode) && !DataUtil.isNullObject(fromOwnerName)) {
            return fromOwnerCode + " - " + fromOwnerName;
        }
        return "";
    }

    public String getFullLabelToOwner() {
        if (!DataUtil.isNullObject(toOwnerCode) && !DataUtil.isNullObject(toOwnerName)) {
            return toOwnerCode + " - " + toOwnerName;
        }
        return "";
    }
}
