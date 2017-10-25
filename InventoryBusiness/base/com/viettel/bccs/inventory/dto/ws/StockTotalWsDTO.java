package com.viettel.bccs.inventory.dto.ws;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockTotalWsDTO extends BaseDTO implements Serializable {

    private Long availableQuantity;
    private Long currentQuantity;
    private Long hangQuantity;
    private Date modifiedDate;
    private Long ownerId;
    private Long ownerType;
    private Long prodOfferId;
    private Long stateId;
    private Long status;
    private Long stockTotalId;
    //04012015_thaont19_start
    private String unit;
    private String name;
    private String code;
    private Long prodOfferTypeId;
    private String stateName;
    private String accountingModelCode;
    private String productTypeName;
    private Long checkSerial;
    private Long totalSerial;
    private Long index;
    private List<StockTransSerialDTO> lstSerial = Lists.newArrayList();

    public List<StockTransSerialDTO> getLstSerial() {
        return lstSerial;
    }

    public void setLstSerial(List<StockTransSerialDTO> lstSerial) {
        this.lstSerial = lstSerial;
    }

    public Long getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(Long checkSerial) {
        this.checkSerial = checkSerial;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Long getProdOfferTypeId() {
        return prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //04012015_thaont19_end
    public String getKeySet() {
        return keySet;
    }

    public Long getAvailableQuantity() {
        return this.availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Long getCurrentQuantity() {
        return this.currentQuantity;
    }

    public void setCurrentQuantity(Long currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Long getHangQuantity() {
        return this.hangQuantity;
    }

    public void setHangQuantity(Long hangQuantity) {
        this.hangQuantity = hangQuantity;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
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

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
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

    public Long getStockTotalId() {
        return this.stockTotalId;
    }

    public void setStockTotalId(Long stockTotalId) {
        this.stockTotalId = stockTotalId;
    }

    public Long getTotalSerial() {
        return totalSerial;
    }

    public void setTotalSerial(Long totalSerial) {
        this.totalSerial = totalSerial;
    }

    public String getAccountingModelCode() {
        return accountingModelCode;
    }

    public void setAccountingModelCode(String accountingModelCode) {
        this.accountingModelCode = accountingModelCode;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }
}
