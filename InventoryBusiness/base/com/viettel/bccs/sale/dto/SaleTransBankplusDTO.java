package com.viettel.bccs.sale.dto;

import com.viettel.fw.dto.BaseDTO;
import com.viettel.payment.bankplus.SaleTransBankPlus;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class SaleTransBankplusDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    public SaleTransBankplusDTO() {
    }

    private Long balance;
    private String bankCode;
    private String branchCode;
    private Long channelTypeId;
    private String contentTrans;
    private Date createDate;
    private String description;
    private String districtCode;
    private String isdnBankPlus;
    private Date lastUpdate;
    private Long ownerId;
    private Long ownerType;
    private String paymentType;
    private Long requestId;
    private Long requestIdBankPlus;
    private String responseCode;
    private Long retry;
    private Long saleTransBankplusId;
    private String shopCode;
    private String staffCode;
    private String staffvhrCode;
    private Long status;
    private Long type;
    private Long contractId;
    private Long subId;
    private String isdn;
    private Long loadStatus;

    public Long getLoadStatus() {
        return loadStatus;
    }

    public void setLoadStatus(Long loadStatus) {
        this.loadStatus = loadStatus;
    }

    public Long getBalance() {
        return this.balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public Long getChannelTypeId() {
        return this.channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public String getContentTrans() {
        return this.contentTrans;
    }

    public void setContentTrans(String contentTrans) {
        this.contentTrans = contentTrans;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistrictCode() {
        return this.districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getIsdnBankPlus() {
        return this.isdnBankPlus;
    }

    public void setIsdnBankPlus(String isdnBankPlus) {
        this.isdnBankPlus = isdnBankPlus;
    }

    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
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

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getRequestIdBankPlus() {
        return this.requestIdBankPlus;
    }

    public void setRequestIdBankPlus(Long requestIdBankPlus) {
        this.requestIdBankPlus = requestIdBankPlus;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Long getRetry() {
        return this.retry;
    }

    public void setRetry(Long retry) {
        this.retry = retry;
    }

    public Long getSaleTransBankplusId() {
        return this.saleTransBankplusId;
    }

    public void setSaleTransBankplusId(Long saleTransBankplusId) {
        this.saleTransBankplusId = saleTransBankplusId;
    }

    public String getShopCode() {
        return this.shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getStaffCode() {
        return this.staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffvhrCode() {
        return this.staffvhrCode;
    }

    public void setStaffvhrCode(String staffvhrCode) {
        this.staffvhrCode = staffvhrCode;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getType() {
        return this.type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }
}
