package com.viettel.bccs.inventory.dto;

/**
 * Created by sinhhv on 1/28/2016.
 */
public class InputDistributeByRangeDTO {
    Long telecomServiceId;
    String telecomServiceName;
    String startRange;
    String endRange;
    String toOwnerType;
    Long toOwnerId;
    String toOwnerCode;
    String toOwnerName;
    Long reasonId;
    String reasonCode;
    String reasonName;
    String fromOwnerType;
    Long fromOwnerId;
    String fromOwnerCode;
    String fromOwnerName;
    String note;
    String userCreate;
    String userIp;
    String userCode;
    Long userId;
    Long currentShopid;
    String currentShopPath;

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public String getTelecomServiceName() {
        return telecomServiceName;
    }

    public void setTelecomServiceName(String telecomServiceName) {
        this.telecomServiceName = telecomServiceName;
    }

    public String getStartRange() {
        return startRange;
    }

    public void setStartRange(String startRange) {
        this.startRange = startRange;
    }

    public String getEndRange() {
        return endRange;
    }

    public void setEndRange(String endRange) {
        this.endRange = endRange;
    }

    public String getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(String toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public String getToOwnerCode() {
        return toOwnerCode;
    }

    public void setToOwnerCode(String toOwnerCode) {
        this.toOwnerCode = toOwnerCode;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(String fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public String getFromOwnerCode() {
        return fromOwnerCode;
    }

    public void setFromOwnerCode(String fromOwnerCode) {
        this.fromOwnerCode = fromOwnerCode;
    }

    public String getFromOwnerName() {
        return fromOwnerName;
    }

    public void setFromOwnerName(String fromOwnerName) {
        this.fromOwnerName = fromOwnerName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCurrentShopPath() {
        return currentShopPath;
    }

    public void setCurrentShopPath(String currentShopPath) {
        this.currentShopPath = currentShopPath;
    }

    public Long getCurrentShopid() {
        return currentShopid;
    }

    public void setCurrentShopid(Long currentShopid) {
        this.currentShopid = currentShopid;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
