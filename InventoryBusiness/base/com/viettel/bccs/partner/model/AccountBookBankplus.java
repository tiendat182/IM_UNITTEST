/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.partner.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author DungPT16
 */
@Entity
@Table(name = "ACCOUNT_BOOK_BANKPLUS", schema = "BCCS_IM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountBookBankplus.findAll", query = "SELECT a FROM AccountBookBankplus a")})
public class AccountBookBankplus implements Serializable {
public static enum COLUMNS {ACCOUNTID, AMOUNTREQUEST, APPNAME, BANKCODE, CLOSINGBALANCE, CMREQUEST, CREATEDATE, DEBITREQUEST, DESCRIPTION, DESCRIPTIONBANKPLUS, IPADDRESS, ISDN, ISDNBANKPLUS, OPENINGBALANCE, REALTRANSDATE, RECEIPTID, REQUESTID, REQUESTTYPE, RESPONSECODE, RETURNDATE, STATUS, STOCKTRANSID, TRANSACTIONCP, USERREQUEST, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "REQUEST_ID")
    @SequenceGenerator(name = "REQUEST_ID_ACCOUNT_GENERATOR", sequenceName = "BCCS_IM.ACCOUNT_BOOK_BANKPLUS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUEST_ID_ACCOUNT_GENERATOR")
    private Long requestId;
    @Column(name = "ACCOUNT_ID")
    private Long accountId;
    @Column(name = "REQUEST_TYPE")
    private Long requestType;
    @Column(name = "AMOUNT_REQUEST")
    private Long amountRequest;
    @Column(name = "DEBIT_REQUEST")
    private Long debitRequest;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "USER_REQUEST")
    private String userRequest;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "RETURN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;
    @Column(name = "RECEIPT_ID")
    private Long receiptId;
    @Column(name = "OPENING_BALANCE")
    private Long openingBalance;
    @Column(name = "CLOSING_BALANCE")
    private Long closingBalance;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CM_REQUEST")
    private String cmRequest;
    @Column(name = "TRANSACTION_CP")
    private String transactionCp;
    @Column(name = "BANK_CODE")
    private String bankCode;
    @Column(name = "ISDN_BANK_PLUS")
    private String isdnBankPlus;
    @Column(name = "REAL_TRANS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date realTransDate;
    @Column(name = "APP_NAME")
    private String appName;
    @Column(name = "RESPONSE_CODE")
    private String responseCode;
    @Column(name = "DESCRIPTION_BANKPLUS")
    private String descriptionBankplus;

    public AccountBookBankplus() {
    }

    public AccountBookBankplus(Long requestId) {
        this.requestId = requestId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getRequestType() {
        return requestType;
    }

    public void setRequestType(Long requestType) {
        this.requestType = requestType;
    }

    public Long getAmountRequest() {
        return amountRequest;
    }

    public void setAmountRequest(Long amountRequest) {
        this.amountRequest = amountRequest;
    }

    public Long getDebitRequest() {
        return debitRequest;
    }

    public void setDebitRequest(Long debitRequest) {
        this.debitRequest = debitRequest;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(String userRequest) {
        this.userRequest = userRequest;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Long getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Long openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Long getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(Long closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCmRequest() {
        return cmRequest;
    }

    public void setCmRequest(String cmRequest) {
        this.cmRequest = cmRequest;
    }

    public String getTransactionCp() {
        return transactionCp;
    }

    public void setTransactionCp(String transactionCp) {
        this.transactionCp = transactionCp;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getIsdnBankPlus() {
        return isdnBankPlus;
    }

    public void setIsdnBankPlus(String isdnBankPlus) {
        this.isdnBankPlus = isdnBankPlus;
    }

    public Date getRealTransDate() {
        return realTransDate;
    }

    public void setRealTransDate(Date realTransDate) {
        this.realTransDate = realTransDate;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescriptionBankplus() {
        return descriptionBankplus;
    }

    public void setDescriptionBankplus(String descriptionBankplus) {
        this.descriptionBankplus = descriptionBankplus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requestId != null ? requestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountBookBankplus)) {
            return false;
        }
        AccountBookBankplus other = (AccountBookBankplus) object;
        if ((this.requestId == null && other.requestId != null) || (this.requestId != null && !this.requestId.equals(other.requestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.partner.model.AccountBookBankplus[ requestId=" + requestId + " ]";
    }
    
}
