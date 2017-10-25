package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockKitIm1DTO extends BaseDTO implements Serializable {

    //a.id, a.stock_model_id, a.owner_id, a.owner_type, a.serial, a.state_id,a.status  from stock_kit
    private String contractCode;
    private Date createDate;
    private String createUser;
    private String depositPrice;
    private Long id;
    private Long ownerId;
    private Long ownerType;
    private String poCode;
    private Long stockModelId;
    private Date saleDate;
    private String serial;
    private Long stateId;
    private Long status;
    private Long stockSimInfoId;
    private Long telecomServiceId;
    private Date updateDatetime;
    private Long stockTransId;

    public String getKeySet() {
        return keySet; }
    public String getContractCode() {
        return this.contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateUser() {
        return this.createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public String getDepositPrice() {
        return this.depositPrice;
    }
    public void setDepositPrice(String depositPrice) {
        this.depositPrice = depositPrice;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getOwnerId() {
        return this.ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Long getOwnerType() {
        return this.ownerType;
    }
    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }
    public String getPoCode() {
        return this.poCode;
    }
    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }
    public Long getStockModelId() {
        return stockModelId;
    }
    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }
    public Date getSaleDate() {
        return this.saleDate;
    }
    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
    public String getSerial() {
        return this.serial;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
    public Long getStateId() {
        return this.stateId;
    }
    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
    public Long getStatus() {
        return this.status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public Long getStockSimInfoId() {
        return this.stockSimInfoId;
    }
    public void setStockSimInfoId(Long stockSimInfoId) {
        this.stockSimInfoId = stockSimInfoId;
    }
    public Long getTelecomServiceId() {
        return this.telecomServiceId;
    }
    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }
    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
    public Long getStockTransId() {
        return stockTransId;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
}
