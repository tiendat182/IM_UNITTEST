package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class PartnerContractDTO extends BaseDTO implements Serializable {

    private String actionCode;
    private String contractCode;
    private Date contractDate;
    private Short contractStatus;
    private Date createDate;
    private Long currencyRate;
    private String currencyType;
    private String deliveryLocation;
    private Date importDate;
    private Date kcsDate;
    private String kcsNo;
    private Date lastModified;
    private String orderCode;
    private Long partnerContractId;
    private Long partnerId;
    private String poCode;
    private Date poDate;
    private Long prodOfferId;
    private Date requestDate;
    private Date startDateWarranty;
    private Long stockTransId;
    private Long unitPrice;

    public String getKeySet() {
        return keySet; }
    public String getActionCode() {
        return this.actionCode;
    }
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    public String getContractCode() {
        return this.contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public Date getContractDate() {
        return this.contractDate;
    }
    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }
    public Short getContractStatus() {
        return this.contractStatus;
    }
    public void setContractStatus(Short contractStatus) {
        this.contractStatus = contractStatus;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Long getCurrencyRate() {
        return this.currencyRate;
    }
    public void setCurrencyRate(Long currencyRate) {
        this.currencyRate = currencyRate;
    }
    public String getCurrencyType() {
        return this.currencyType;
    }
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
    public String getDeliveryLocation() {
        return this.deliveryLocation;
    }
    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }
    public Date getImportDate() {
        return this.importDate;
    }
    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }
    public Date getKcsDate() {
        return this.kcsDate;
    }
    public void setKcsDate(Date kcsDate) {
        this.kcsDate = kcsDate;
    }
    public String getKcsNo() {
        return this.kcsNo;
    }
    public void setKcsNo(String kcsNo) {
        this.kcsNo = kcsNo;
    }
    public Date getLastModified() {
        return this.lastModified;
    }
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    public String getOrderCode() {
        return this.orderCode;
    }
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    public Long getPartnerContractId() {
        return this.partnerContractId;
    }
    public void setPartnerContractId(Long partnerContractId) {
        this.partnerContractId = partnerContractId;
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
    public Date getPoDate() {
        return this.poDate;
    }
    public void setPoDate(Date poDate) {
        this.poDate = poDate;
    }
    public Long getProdOfferId() {
        return this.prodOfferId;
    }
    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }
    public Date getRequestDate() {
        return this.requestDate;
    }
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
    public Date getStartDateWarranty() {
        return this.startDateWarranty;
    }
    public void setStartDateWarranty(Date startDateWarranty) {
        this.startDateWarranty = startDateWarranty;
    }
    public Long getStockTransId() {
        return this.stockTransId;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
    public Long getUnitPrice() {
        return this.unitPrice;
    }
    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }
}
