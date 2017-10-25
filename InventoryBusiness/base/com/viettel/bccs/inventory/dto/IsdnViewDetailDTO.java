package com.viettel.bccs.inventory.dto;

import com.viettel.bccs.inventory.common.Const;

/**
 * Created by sinhhv on 2/15/2016.
 */
public class IsdnViewDetailDTO {
    private String isdn;
    private String isdnType;
    private String codeShopStaff;
    private String status;
    private String statusName;
    private String proName;
    private String ruleName;
    private Long telecomServiceId;
    private String telecomServiceLabel;
    private Long proOfferId;
    private Long ownerId;
    private String ownerType;
    private String ownerCode;
    private String ownerName;
    private boolean hasPermissionView=false;
    private String exchangeName;
//    private String price;
    private String effectDateTime;
    private String createDateTime;
    private Long lockByStaff;
    private boolean hasPerimisionUnlockIsdn;

    public boolean isDisableLockIsdn() {
        return !(Const.ISDN.NEW_NUMBER.equals(status) || Const.ISDN.STOP_USE.equals(status)) || !Const.STOCK_TYPE.STOCK_ISDN_MOBILE.equals(telecomServiceId);
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getIsdnType() {
        return isdnType;
    }

    public void setIsdnType(String isdnType) {
        this.isdnType = isdnType;
    }

    public String getCodeShopStaff() {
        return codeShopStaff;
    }

    public void setCodeShopStaff(String codeShopStaff) {
        this.codeShopStaff = codeShopStaff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public Long getProOfferId() {
        return proOfferId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public boolean getHasPermissionView() {
        return hasPermissionView;
    }

    public void setHasPermissionView(boolean hasPermissionView) {
        this.hasPermissionView = hasPermissionView;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTelecomServiceLabel() {
        return telecomServiceLabel;
    }

    public void setTelecomServiceLabel(String telecomServiceLabel) {
        this.telecomServiceLabel = telecomServiceLabel;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void setProOfferId(Long proOfferId) {
        this.proOfferId = proOfferId;
    }

    public String getEffectDateTime() {
        return effectDateTime;
    }

    public void setEffectDateTime(String effectDateTime) {
        this.effectDateTime = effectDateTime;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Long getLockByStaff() {
        return lockByStaff;
    }

    public void setLockByStaff(Long lockByStaff) {
        this.lockByStaff = lockByStaff;
    }

    public boolean isHasPerimisionUnlockIsdn() {
        return hasPerimisionUnlockIsdn;
    }

    public void setHasPerimisionUnlockIsdn(boolean hasPerimisionUnlockIsdn) {
        this.hasPerimisionUnlockIsdn = hasPerimisionUnlockIsdn;
    }
}
