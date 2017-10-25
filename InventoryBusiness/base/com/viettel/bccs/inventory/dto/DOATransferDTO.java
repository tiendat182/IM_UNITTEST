package com.viettel.bccs.inventory.dto;

import java.util.Date;

/**
 * Created by hoangnt14 on 8/13/2016.
 */
public class DOATransferDTO {
    private String ownerCode;
    private String ownerName;
    private String prodOfferCode;
    private String prodOfferName;
    private String serial;
    private String fromSerial;
    private String toSerial;
    private Long quantity;
    private String actionCode;
    private String createUser;
    private Long stockTransId;
    private Long parrentShopId;
    private Long ownerType;
    private Long ownerId;
    private Date createDate;

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSerial() {
        return toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getParrentShopId() {
        return parrentShopId;
    }

    public void setParrentShopId(Long parrentShopId) {
        this.parrentShopId = parrentShopId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
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

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
}
