package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 12/26/2016.
 */
public class StampDTO extends BaseMessage {

    private String stockModelCode;
    private String stockModelName;
    private String ownerCode;
    private String ownerName;
    private Long ownerId;
    private Long ownerType;
    private Long status;
    private String fromSerial;
    private String toSerial;
    private Long quantity;
    private Date updateDatetime;
    private String shopPath;
    private String phoneNumber;
    private String parrentShopCode;
    private String parrentShopName;
    private Long parrentShopId;
    private String parrentShopPath;

    public String getParrentShopCode() {
        return parrentShopCode;
    }

    public void setParrentShopCode(String parrentShopCode) {
        this.parrentShopCode = parrentShopCode;
    }

    public String getParrentShopName() {
        return parrentShopName;
    }

    public void setParrentShopName(String parrentShopName) {
        this.parrentShopName = parrentShopName;
    }

    public Long getParrentShopId() {
        return parrentShopId;
    }

    public void setParrentShopId(Long parrentShopId) {
        this.parrentShopId = parrentShopId;
    }

    public String getParrentShopPath() {
        return parrentShopPath;
    }

    public void setParrentShopPath(String parrentShopPath) {
        this.parrentShopPath = parrentShopPath;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getShopPath() {
        return shopPath;
    }

    public void setShopPath(String shopPath) {
        this.shopPath = shopPath;
    }

    public StampDTO() {

    }

    public StampDTO(String stockModelCode, boolean success, String errCode, String description, String... paramsMsg) {
        super(success, errCode, description, paramsMsg);
        this.stockModelCode = stockModelCode;
    }

    public String getStockModelCode() {
        return stockModelCode;
    }

    public void setStockModelCode(String stockModelCode) {
        this.stockModelCode = stockModelCode;
    }

    public String getStockModelName() {
        return stockModelName;
    }

    public void setStockModelName(String stockModelName) {
        this.stockModelName = stockModelName;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
