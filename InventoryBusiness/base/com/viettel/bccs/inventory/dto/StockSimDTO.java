package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockSimDTO extends BaseDTO implements Serializable {

    private String contractCode;
    private Date createDate;
    private String createUser;
    private String depositPrice;
    private Long id;
    private Long ownerId;
    private Long ownerType;
    private String poCode;
    private Long prodOfferId;
    private Date saleDate;
    private String serial;
    private Long stateId;
    private Long status;
    private Long telecomServiceId;
    private Date updateDatetime;
    private Long stockTransId;
    private String a3a8;
    private String adm1;
    private Date aucRegDate;
    private Date aucRemoveDate;
    private Long aucStatus;
    private Date connectionDate;
    private Long connectionStatus;
    private String connectionType;
    private String eki;
    private String hlrId;
    private Date hlrRegDate;
    private Date hlrRemoveDate;
    private Long hlrStatus;
    private String iccid;
    private String imsi;
    private String kind;
    private String pin;
    private String pin2;
    private String puk;
    private String puk2;
    private String simType;

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
    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
    public Long getStockTransId() {
        return stockTransId;
    }
    public String getA3a8() {
        return a3a8;
    }
    public void setA3a8(String a3a8) {
        this.a3a8 = a3a8;
    }
    public String getAdm1() {
        return adm1;
    }
    public void setAdm1(String adm1) {
        this.adm1 = adm1;
    }
    public Date getAucRegDate() {
        return aucRegDate;
    }
    public void setAucRegDate(Date aucRegDate) {
        this.aucRegDate = aucRegDate;
    }
    public Date getAucRemoveDate() {
        return aucRemoveDate;
    }
    public void setAucRemoveDate(Date aucRemoveDate) {
        this.aucRemoveDate = aucRemoveDate;
    }
    public Long getAucStatus() {
        return aucStatus;
    }
    public void setAucStatus(Long aucStatus) {
        this.aucStatus = aucStatus;
    }
    public Date getConnectionDate() {
        return connectionDate;
    }
    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }
    public Long getConnectionStatus() {
        return connectionStatus;
    }
    public void setConnectionStatus(Long connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
    public String getConnectionType() {
        return connectionType;
    }
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
    public String getEki() {
        return eki;
    }
    public void setEki(String eki) {
        this.eki = eki;
    }
    public String getHlrId() {
        return hlrId;
    }
    public void setHlrId(String hlrId) {
        this.hlrId = hlrId;
    }
    public Date getHlrRegDate() {
        return hlrRegDate;
    }
    public void setHlrRegDate(Date hlrRegDate) {
        this.hlrRegDate = hlrRegDate;
    }
    public Date getHlrRemoveDate() {
        return hlrRemoveDate;
    }
    public void setHlrRemoveDate(Date hlrRemoveDate) {
        this.hlrRemoveDate = hlrRemoveDate;
    }
    public Long getHlrStatus() {
        return hlrStatus;
    }
    public void setHlrStatus(Long hlrStatus) {
        this.hlrStatus = hlrStatus;
    }
    public String getIccid() {
        return iccid;
    }
    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
    public String getImsi() {
        return imsi;
    }
    public void setImsi(String imsi) {
        this.imsi = imsi;
    }
    public String getKind() {
        return kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
    public String getPin2() {
        return pin2;
    }
    public void setPin2(String pin2) {
        this.pin2 = pin2;
    }
    public String getPuk() {
        return puk;
    }
    public void setPuk(String puk) {
        this.puk = puk;
    }
    public String getPuk2() {
        return puk2;
    }
    public void setPuk2(String puk2) {
        this.puk2 = puk2;
    }
    public String getSimType() {
        return simType;
    }
    public void setSimType(String simType) {
        this.simType = simType;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
}
