package com.viettel.bccs.inventory.dto;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class RevokeKitFileImportDTO extends BaseDTO {
public String getKeySet() {
 return keySet; }

    private int index;
    private String isdn;
    private String serial;
    private String ownerCode;
    private String ownerType;
    private String revokeType;
    private String resultRevoke;
    private Long fromOwnerId;
    private Long fromOwnerType;
    private String reasonCode;

    public Long getStateId() {
        // truong hop thu hoi Kit chia lam 2 theo revoke type
        //20170222: stateId lay duy nhat = 4, bo qua revokeType
        return "1".equals(revokeType) ? Const.GOODS_STATE.NEW : ("2".equals(revokeType) ? Const.GOODS_STATE.RETRIVE : null);
    }

    private RevokeKitDetailDTO revokeKitDetailDTO;

    public RevokeKitFileImportDTO() {
    }

    public RevokeKitFileImportDTO(String isdn, String serial) {
        this.isdn = isdn;
        this.serial = serial;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getRevokeType() {
        return revokeType;
    }

    public void setRevokeType(String revokeType) {
        this.revokeType = revokeType;
    }

    public String getResultRevoke() {
        return resultRevoke;
    }

    public void setResultRevoke(String resultRevoke) {
        this.resultRevoke = resultRevoke;
    }

    public RevokeKitDetailDTO getRevokeKitDetailDTO() {
        return revokeKitDetailDTO;
    }

    public void setRevokeKitDetailDTO(RevokeKitDetailDTO revokeKitDetailDTO) {
        this.revokeKitDetailDTO = revokeKitDetailDTO;
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

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
}
