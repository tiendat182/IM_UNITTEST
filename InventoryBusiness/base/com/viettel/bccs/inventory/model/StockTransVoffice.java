/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author thetm1
 */
@Entity
@Table(name = "STOCK_TRANS_VOFFICE")
@NamedQueries({
    @NamedQuery(name = "StockTransVoffice.findAll", query = "SELECT s FROM StockTransVoffice s")})
public class StockTransVoffice implements Serializable {
public static enum COLUMNS {ACCOUNTNAME, ACCOUNTPASS, ACTIONCODE, CREATEDATE, CREATEUSERID, DENIEDDATE, DENIEDUSER, ERRORCODE, ERRORCODEDESCRIPTION, FINDSERIAL, LASTMODIFY, NOTE, PREFIXTEMPLATE, RECEIPTNO, RESPONSECODE, RESPONSECODEDESCRIPTION, RETRY, SIGNUSERLIST, STATUS, STOCKTRANSACTIONID, STOCKTRANSVOFFICEID, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_VOFFICE_ID")
    private String stockTransVofficeId;
    @Column(name = "SIGN_USER_LIST")
    private String signUserList;
    @Column(name = "CREATE_USER_ID")
    private Long createUserId;
    @Basic(optional = false)
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "RETRY")
    private Long retry;
    @Column(name = "LAST_MODIFY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModify;
    @Column(name = "ERROR_CODE")
    private String errorCode;
    @Column(name = "ERROR_CODE_DESCRIPTION")
    private String errorCodeDescription;
    @Column(name = "RESPONSE_CODE")
    private String responseCode;
    @Column(name = "RESPONSE_CODE_DESCRIPTION")
    private String responseCodeDescription;
    @Column(name = "PREFIX_TEMPLATE")
    private String prefixTemplate;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name = "ACCOUNT_PASS")
    private String accountPass;
    @Column(name = "FIND_SERIAL")
    private Long findSerial;
    @Column(name = "DENIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deniedDate;
    @Column(name = "DENIED_USER")
    private String deniedUser;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "STOCK_TRANS_ACTION_ID")
    private Long stockTransActionId;
    @Column(name = "RECEIPT_NO")
    private String receiptNo;
    @Column(name = "ACTION_CODE")
    private String actionCode;

    public StockTransVoffice() {
    }

    public StockTransVoffice(String stockTransVofficeId) {
        this.stockTransVofficeId = stockTransVofficeId;
    }

    public StockTransVoffice(String stockTransVofficeId, Date createDate) {
        this.stockTransVofficeId = stockTransVofficeId;
        this.createDate = createDate;
    }

    public String getStockTransVofficeId() {
        return stockTransVofficeId;
    }

    public void setStockTransVofficeId(String stockTransVofficeId) {
        this.stockTransVofficeId = stockTransVofficeId;
    }

    public String getSignUserList() {
        return signUserList;
    }

    public void setSignUserList(String signUserList) {
        this.signUserList = signUserList;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRetry() {
        return retry;
    }

    public void setRetry(Long retry) {
        this.retry = retry;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCodeDescription() {
        return errorCodeDescription;
    }

    public void setErrorCodeDescription(String errorCodeDescription) {
        this.errorCodeDescription = errorCodeDescription;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseCodeDescription() {
        return responseCodeDescription;
    }

    public void setResponseCodeDescription(String responseCodeDescription) {
        this.responseCodeDescription = responseCodeDescription;
    }

    public String getPrefixTemplate() {
        return prefixTemplate;
    }

    public void setPrefixTemplate(String prefixTemplate) {
        this.prefixTemplate = prefixTemplate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPass() {
        return accountPass;
    }

    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }

    public Long getFindSerial() {
        return findSerial;
    }

    public void setFindSerial(Long findSerial) {
        this.findSerial = findSerial;
    }

    public Date getDeniedDate() {
        return deniedDate;
    }

    public void setDeniedDate(Date deniedDate) {
        this.deniedDate = deniedDate;
    }

    public String getDeniedUser() {
        return deniedUser;
    }

    public void setDeniedUser(String deniedUser) {
        this.deniedUser = deniedUser;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockTransVofficeId != null ? stockTransVofficeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransVoffice)) {
            return false;
        }
        StockTransVoffice other = (StockTransVoffice) object;
        if ((this.stockTransVofficeId == null && other.stockTransVofficeId != null) || (this.stockTransVofficeId != null && !this.stockTransVofficeId.equals(other.stockTransVofficeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockTransVoffice[ stockTransVofficeId=" + stockTransVofficeId + " ]";
    }

}
