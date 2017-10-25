package com.viettel.bccs.partner.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class AccountBookBankplusDTO extends BaseDTO implements Serializable {

    private Long accountId;
    private Long amountRequest;
    private String appName;
    private String bankCode;
    private Long closingBalance;
    private String cmRequest;
    private Date createDate;
    private Long debitRequest;
    private String description;
    private String descriptionBankplus;
    private String ipAddress;
    private String isdn;
    private String isdnBankPlus;
    private Long openingBalance;
    private Date realTransDate;
    private Long receiptId;
    private Long requestId;
    private Long requestType;
    private String responseCode;
    private Date returnDate;
    private Long status;
    private Long stockTransId;
    private String transactionCp;
    private String userRequest;

    public String getKeySet() {
        return keySet; }
    public Long getAccountId() {
        return this.accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public Long getAmountRequest() {
        return this.amountRequest;
    }
    public void setAmountRequest(Long amountRequest) {
        this.amountRequest = amountRequest;
    }
    public String getAppName() {
        return this.appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getBankCode() {
        return this.bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public Long getClosingBalance() {
        return this.closingBalance;
    }
    public void setClosingBalance(Long closingBalance) {
        this.closingBalance = closingBalance;
    }
    public String getCmRequest() {
        return this.cmRequest;
    }
    public void setCmRequest(String cmRequest) {
        this.cmRequest = cmRequest;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Long getDebitRequest() {
        return this.debitRequest;
    }
    public void setDebitRequest(Long debitRequest) {
        this.debitRequest = debitRequest;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescriptionBankplus() {
        return this.descriptionBankplus;
    }
    public void setDescriptionBankplus(String descriptionBankplus) {
        this.descriptionBankplus = descriptionBankplus;
    }
    public String getIpAddress() {
        return this.ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public String getIsdn() {
        return this.isdn;
    }
    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }
    public String getIsdnBankPlus() {
        return this.isdnBankPlus;
    }
    public void setIsdnBankPlus(String isdnBankPlus) {
        this.isdnBankPlus = isdnBankPlus;
    }
    public Long getOpeningBalance() {
        return this.openingBalance;
    }
    public void setOpeningBalance(Long openingBalance) {
        this.openingBalance = openingBalance;
    }
    public Date getRealTransDate() {
        return this.realTransDate;
    }
    public void setRealTransDate(Date realTransDate) {
        this.realTransDate = realTransDate;
    }
    public Long getReceiptId() {
        return this.receiptId;
    }
    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }
    public Long getRequestId() {
        return this.requestId;
    }
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    public Long getRequestType() {
        return this.requestType;
    }
    public void setRequestType(Long requestType) {
        this.requestType = requestType;
    }
    public String getResponseCode() {
        return this.responseCode;
    }
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    public Date getReturnDate() {
        return this.returnDate;
    }
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
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
    public String getTransactionCp() {
        return this.transactionCp;
    }
    public void setTransactionCp(String transactionCp) {
        this.transactionCp = transactionCp;
    }
    public String getUserRequest() {
        return this.userRequest;
    }
    public void setUserRequest(String userRequest) {
        this.userRequest = userRequest;
    }
}
