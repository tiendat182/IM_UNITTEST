package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class RevokeTransDetailDTO extends BaseDTO implements Serializable {

    private Date createDate;
    private String description;
    private String errCode;
    private Long oldOwnerId;
    private Long oldOwnerType;
    private String oldSerial;
    private Long prodOfferId;
    private Long prodOfferTypeId;
    private Long quantity;
    private Long revokePrice;
    private Long revokeTransDetailId;
    private Long revokeTransId;
    private String revokedSerial;

    public String getKeySet() {
        return keySet;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public Long getOldOwnerId() {
        return this.oldOwnerId;
    }

    public void setOldOwnerId(Long oldOwnerId) {
        this.oldOwnerId = oldOwnerId;
    }

    public Long getOldOwnerType() {
        return this.oldOwnerType;
    }

    public void setOldOwnerType(Long oldOwnerType) {
        this.oldOwnerType = oldOwnerType;
    }

    public String getOldSerial() {
        return this.oldSerial;
    }

    public void setOldSerial(String oldSerial) {
        this.oldSerial = oldSerial;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getProdOfferTypeId() {
        return this.prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getRevokePrice() {
        return this.revokePrice;
    }

    public void setRevokePrice(Long revokePrice) {
        this.revokePrice = revokePrice;
    }

    public Long getRevokeTransDetailId() {
        return this.revokeTransDetailId;
    }

    public void setRevokeTransDetailId(Long revokeTransDetailId) {
        this.revokeTransDetailId = revokeTransDetailId;
    }

    public Long getRevokeTransId() {
        return this.revokeTransId;
    }

    public void setRevokeTransId(Long revokeTransId) {
        this.revokeTransId = revokeTransId;
    }

    public String getRevokedSerial() {
        return this.revokedSerial;
    }

    public void setRevokedSerial(String revokedSerial) {
        this.revokedSerial = revokedSerial;
    }
}
