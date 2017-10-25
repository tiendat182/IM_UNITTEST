package com.viettel.bccs.inventory.dto;

import java.lang.Character;
import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockKitDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Date aucRegDate;
    private Date aucRemoveDate;
    private Character aucStatus;
    private Long bankplusStatus;
    private Long checkDial;
    private Date connectionDate;
    private Long connectionStatus;
    private Long connectionType;
    private String contractCode;
    private Date createDate;
    private String createUser;
    private Long depositPrice;
    private Long dialStatus;
    private String hlrId;
    private Date hlrRegDate;
    private Date hlrRemoveDate;
    private Long hlrStatus;
    private String iccid;
    private Long id;
    private String imsi;
    private String isdn;
    private Long oldOwnerId;
    private Long oldOwnerType;
    private String orderCode;
    private Long ownerId;
    private Long ownerReceiverId;
    private Long ownerReceiverType;
    private Long ownerType;
    private String pin;
    private String pin2;
    private String poCode;
    private Long prodOfferId;
    private String puk;
    private String puk2;
    private String receiverName;
    private Date saleDate;
    private String serial;
    private Long serialReal;
    private String simType;
    private Date startDateWarranty;
    private Long stateId;
    private Long status;
    private Long stockTransId;
    private Long telecomServiceId;
    private Date updateDatetime;
    private Long updateNumber;
    private String userSessionId;

    private String prodOfferName;
    private Date expireDate;

    public Date getAucRegDate() {
        return this.aucRegDate;
    }

    public void setAucRegDate(Date aucRegDate) {
        this.aucRegDate = aucRegDate;
    }

    public Date getAucRemoveDate() {
        return this.aucRemoveDate;
    }

    public void setAucRemoveDate(Date aucRemoveDate) {
        this.aucRemoveDate = aucRemoveDate;
    }

    public Character getAucStatus() {
        return this.aucStatus;
    }

    public void setAucStatus(Character aucStatus) {
        this.aucStatus = aucStatus;
    }

    public Long getBankplusStatus() {
        return this.bankplusStatus;
    }

    public void setBankplusStatus(Long bankplusStatus) {
        this.bankplusStatus = bankplusStatus;
    }

    public Long getCheckDial() {
        return this.checkDial;
    }

    public void setCheckDial(Long checkDial) {
        this.checkDial = checkDial;
    }

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

    public Long getConnectionType() {
        return this.connectionType;
    }

    public void setConnectionType(Long connectionType) {
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

    public Long getDepositPrice() {
        return this.depositPrice;
    }

    public void setDepositPrice(Long depositPrice) {
        this.depositPrice = depositPrice;
    }

    public Long getDialStatus() {
        return this.dialStatus;
    }

    public void setDialStatus(Long dialStatus) {
        this.dialStatus = dialStatus;
    }

    public String getHlrId() {
        return this.hlrId;
    }

    public void setHlrId(String hlrId) {
        this.hlrId = hlrId;
    }

    public Date getHlrRegDate() {
        return this.hlrRegDate;
    }

    public void setHlrRegDate(Date hlrRegDate) {
        this.hlrRegDate = hlrRegDate;
    }

    public Date getHlrRemoveDate() {
        return this.hlrRemoveDate;
    }

    public void setHlrRemoveDate(Date hlrRemoveDate) {
        this.hlrRemoveDate = hlrRemoveDate;
    }

    public Long getHlrStatus() {
        return this.hlrStatus;
    }

    public void setHlrStatus(Long hlrStatus) {
        this.hlrStatus = hlrStatus;
    }

    public String getIccid() {
        return this.iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImsi() {
        return this.imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIsdn() {
        return this.isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
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

    public String getOrderCode() {
        return this.orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerReceiverId() {
        return this.ownerReceiverId;
    }

    public void setOwnerReceiverId(Long ownerReceiverId) {
        this.ownerReceiverId = ownerReceiverId;
    }

    public Long getOwnerReceiverType() {
        return this.ownerReceiverType;
    }

    public void setOwnerReceiverType(Long ownerReceiverType) {
        this.ownerReceiverType = ownerReceiverType;
    }

    public Long getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPin2() {
        return this.pin2;
    }

    public void setPin2(String pin2) {
        this.pin2 = pin2;
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

    public String getPuk() {
        return this.puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }

    public String getPuk2() {
        return this.puk2;
    }

    public void setPuk2(String puk2) {
        this.puk2 = puk2;
    }

    public String getReceiverName() {
        return this.receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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

    public Long getSerialReal() {
        return this.serialReal;
    }

    public void setSerialReal(Long serialReal) {
        this.serialReal = serialReal;
    }

    public String getSimType() {
        return this.simType;
    }

    public void setSimType(String simType) {
        this.simType = simType;
    }

    public Date getStartDateWarranty() {
        return this.startDateWarranty;
    }

    public void setStartDateWarranty(Date startDateWarranty) {
        this.startDateWarranty = startDateWarranty;
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

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
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

    public Long getUpdateNumber() {
        return this.updateNumber;
    }

    public void setUpdateNumber(Long updateNumber) {
        this.updateNumber = updateNumber;
    }

    public String getUserSessionId() {
        return this.userSessionId;
    }

    public void setUserSessionId(String userSessionId) {
        this.userSessionId = userSessionId;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
