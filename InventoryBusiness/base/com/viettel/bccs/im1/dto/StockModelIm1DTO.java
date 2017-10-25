package com.viettel.bccs.im1.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockModelIm1DTO extends BaseDTO implements Serializable {
  private Long stockModelId;
    private String stockModelCode;
    private Long stockTypeId;
    private String name;
    private Long vat;
    private Long checkSerial;
    private Long checkDeposit;
    private Long checkDial;
    private String unit;
    private Long status;
    private String notes;
    private Long discountGroupId;
    private Long profileId;
    private Long telecomServiceId;
    private Long stockModelType;
    private Long sourcePrice;
    private String accountingModelCode;
    private String accountingModelName;
    private String accountingName;
    private String accountingCode;
    private Long priceCost;
    private String unitExchangeCode;
    private Date createDate;
    private Long demoDuration;
    private String policyCode;
    private Long isDemo;
    private String deviceType;
    private Long transceiver;
    private Long costsOfSale;
    public String getKeySet() {
        return keySet;
    }
    public Long getStockModelId() {
        return this.stockModelId;
    }
    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }
    public String getStockModelCode() {
        return this.stockModelCode;
    }
    public void setStockModelCode(String stockModelCode) {
        this.stockModelCode = stockModelCode;
    }
    public Long getStockTypeId() {
        return this.stockTypeId;
    }
    public void setStockTypeId(Long stockTypeId) {
        this.stockTypeId = stockTypeId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getVat() {
        return this.vat;
    }
    public void setVat(Long vat) {
        this.vat = vat;
    }
    public Long getCheckSerial() {
        return this.checkSerial;
    }
    public void setCheckSerial(Long checkSerial) {
        this.checkSerial = checkSerial;
    }
    public Long getCheckDeposit() {
        return this.checkDeposit;
    }
    public void setCheckDeposit(Long checkDeposit) {
        this.checkDeposit = checkDeposit;
    }
    public Long getCheckDial() {
        return this.checkDial;
    }
    public void setCheckDial(Long checkDial) {
        this.checkDial = checkDial;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Long getStatus() {
        return this.status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public String getNotes() {
        return this.notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Long getDiscountGroupId() {
        return this.discountGroupId;
    }
    public void setDiscountGroupId(Long discountGroupId) {
        this.discountGroupId = discountGroupId;
    }
    public Long getProfileId() {
        return this.profileId;
    }
    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
    public Long getTelecomServiceId() {
        return this.telecomServiceId;
    }
    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }
    public Long getStockModelType() {
        return this.stockModelType;
    }
    public void setStockModelType(Long stockModelType) {
        this.stockModelType = stockModelType;
    }
    public Long getSourcePrice() {
        return this.sourcePrice;
    }
    public void setSourcePrice(Long sourcePrice) {
        this.sourcePrice = sourcePrice;
    }
    public String getAccountingModelCode() {
        return this.accountingModelCode;
    }
    public void setAccountingModelCode(String accountingModelCode) {
        this.accountingModelCode = accountingModelCode;
    }
    public String getAccountingModelName() {
        return this.accountingModelName;
    }
    public void setAccountingModelName(String accountingModelName) {
        this.accountingModelName = accountingModelName;
    }
    public String getAccountingName() {
        return this.accountingName;
    }
    public void setAccountingName(String accountingName) {
        this.accountingName = accountingName;
    }
    public String getAccountingCode() {
        return this.accountingCode;
    }
    public void setAccountingCode(String accountingCode) {
        this.accountingCode = accountingCode;
    }
    public Long getPriceCost() {
        return this.priceCost;
    }
    public void setPriceCost(Long priceCost) {
        this.priceCost = priceCost;
    }
    public String getUnitExchangeCode() {
        return this.unitExchangeCode;
    }
    public void setUnitExchangeCode(String unitExchangeCode) {
        this.unitExchangeCode = unitExchangeCode;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Long getDemoDuration() {
        return this.demoDuration;
    }
    public void setDemoDuration(Long demoDuration) {
        this.demoDuration = demoDuration;
    }
    public String getPolicyCode() {
        return this.policyCode;
    }
    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }
    public Long getIsDemo() {
        return this.isDemo;
    }
    public void setIsDemo(Long isDemo) {
        this.isDemo = isDemo;
    }
    public String getDeviceType() {
        return this.deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public Long getTransceiver() {
        return this.transceiver;
    }
    public void setTransceiver(Long transceiver) {
        this.transceiver = transceiver;
    }
    public Long getCostsOfSale() {
        return this.costsOfSale;
    }
    public void setCostsOfSale(Long costsOfSale) {
        this.costsOfSale = costsOfSale;
    }
}
