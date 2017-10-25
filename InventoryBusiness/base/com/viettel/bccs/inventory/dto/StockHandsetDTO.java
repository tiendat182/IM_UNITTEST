package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockHandsetDTO extends BaseDTO implements Serializable {

    private Date connectionDate;
    private Long connectionStatus;
    private String connectionType;
    private String contractCode;
    private Date createDate;
    private String createUser;
    private String depositPrice;
    private Date exportToCollDate;
    private Long id;
    private Long ownerId;
    private Long ownerType;
    private Long partnerId;
    private String poCode;
    private Long prodOfferId;
    private Date saleDate;
    private String serial;
    private Long stateId;
    private String stateIdName;
    private Long status;
    private String statusName;
    private Long telecomServiceId;
    private String tvmsCadId;
    private String tvmsMacId;
    private Date updateDatetime;
    private Long stockTransId;
    private String licenseKey;
    private String soPin;
    private String serialGpon;

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getKeySet() {
        return keySet; }
    public Date getConnectionDate() {
        return this.connectionDate;
    }
    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }
    public Long getConnectionStatus() {
        return this.connectionStatus;
    }
    public void setConnectionStatus(Long connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
    public String getConnectionType() {
        return this.connectionType;
    }
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
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
    public Date getExportToCollDate() {
        return this.exportToCollDate;
    }
    public void setExportToCollDate(Date exportToCollDate) {
        this.exportToCollDate = exportToCollDate;
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
    public Long getPartnerId() {
        return this.partnerId;
    }
    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }
    public String getPoCode() {
        return this.poCode;
    }
    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }
    public Long getProdOfferId() {
        return this.prodOfferId;
    }
    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
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
    public Long getTelecomServiceId() {
        return this.telecomServiceId;
    }
    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }
    public String getTvmsCadId() {
        return this.tvmsCadId;
    }
    public void setTvmsCadId(String tvmsCadId) {
        this.tvmsCadId = tvmsCadId;
    }
    public String getTvmsMacId() {
        return this.tvmsMacId;
    }
    public void setTvmsMacId(String tvmsMacId) {
        this.tvmsMacId = tvmsMacId;
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

    public String getSoPin() {
        return soPin;
    }

    public void setSoPin(String soPin) {
        this.soPin = soPin;
    }

    public String getSerialGpon() {
        return serialGpon;
    }

    public void setSerialGpon(String serialGpon) {
        this.serialGpon = serialGpon;
    }

    public String getStateIdName() {
        return stateIdName;
    }

    public void setStateIdName(String stateIdName) {
        this.stateIdName = stateIdName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
