/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.dto;

import java.text.MessageFormat;

public class StockWarrantyDTO {

    private Long prodOfferId;
    private int quantity;
    private int quantityNeedRevoke;
    private Long revokePrice;
    private Long revokeAmount;
    private String oldSerial;
    private String revokedSerial;
    private String errCode;
    private String description;
    private Long oldOwnerId;
    private Long oldOwnerType;
    private Long prodOfferTypeId;
    private String prodOfferCode;
    private Long isExists;

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityNeedRevoke() {
        return quantityNeedRevoke;
    }

    public void setQuantityNeedRevoke(int quantityNeedRevoke) {
        this.quantityNeedRevoke = quantityNeedRevoke;
    }

    public Long getRevokePrice() {
        return revokePrice;
    }

    public void setRevokePrice(Long revokePrice) {
        this.revokePrice = revokePrice;
    }

    public Long getRevokeAmount() {
        return revokeAmount;
    }

    public void setRevokeAmount(Long revokeAmount) {
        this.revokeAmount = revokeAmount;
    }

    public String getOldSerial() {
        return oldSerial;
    }

    public void setOldSerial(String oldSerial) {
        this.oldSerial = oldSerial;
    }

    public String getRevokedSerial() {
        return revokedSerial;
    }

    public void setRevokedSerial(String revokedSerial) {
        this.revokedSerial = revokedSerial;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOldOwnerId() {
        return oldOwnerId;
    }

    public void setOldOwnerId(Long oldOwnerId) {
        this.oldOwnerId = oldOwnerId;
    }

    public Long getOldOwnerType() {
        return oldOwnerType;
    }

    public void setOldOwnerType(Long oldOwnerType) {
        this.oldOwnerType = oldOwnerType;
    }

    public Long getProdOfferTypeId() {
        return prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public Long getIsExists() {
        return isExists;
    }

    public void setIsExists(Long isExists) {
        this.isExists = isExists;
    }

    @Override
    public String toString() {
        return MessageFormat.format("[prodOfferId:{0},quantity:{1},quantityNeedRevoke:{2}, revokePrice:{3}, revokeAmount:{4}, oldSerial:{5}, revokedSerial:{6}]",
                prodOfferId == null ? null : prodOfferId.toString(), quantity, quantityNeedRevoke, revokePrice, revokeAmount, oldSerial, revokedSerial);
    }

}
