package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockTransVofficeDTO extends BaseDTO implements Serializable {

    private String accountName;
    private String accountPass;
    private String actionCode;
    private Date createDate;
    private Long createUserId;
    private Date deniedDate;
    private String deniedUser;
    private String errorCode;
    private String errorCodeDescription;
    private Long findSerial;
    private Date lastModify;
    private String note;
    private String prefixTemplate;
    private String receiptNo;
    private String responseCode;
    private String responseCodeDescription;
    private Long retry;
    private String signUserList;
    private String status;
    private Long stockTransActionId;
    private String stockTransVofficeId;

    private List<Long> listStatus;
    private Long maxDay;
    private Long maxRetry;
    private Long numberThread;
    private Long id;

    private String tittle;

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public StockTransVofficeDTO() {
    }

    public StockTransVofficeDTO(String prefixTemplate, String status) {
        this.prefixTemplate = prefixTemplate;
        this.status = status;
    }

    public StockTransVofficeDTO(Long maxDay, Long maxRetry, List<Long> listStatus, Long numberThread) {
        this.maxDay = maxDay;
        this.maxRetry = maxRetry;
        this.listStatus = listStatus;
        this.numberThread = numberThread;
    }

    public List<Long> getListStatus() {
        return listStatus;
    }

    public void setListStatus(List<Long> listStatus) {
        this.listStatus = listStatus;
    }

    public Long getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(Long maxDay) {
        this.maxDay = maxDay;
    }

    public Long getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Long maxRetry) {
        this.maxRetry = maxRetry;
    }

    public Long getNumberThread() {
        return numberThread;
    }

    public void setNumberThread(Long numberThread) {
        this.numberThread = numberThread;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeySet() {
        return keySet;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPass() {
        return this.accountPass;
    }

    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }

    public String getActionCode() {
        return this.actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getDeniedDate() {
        return this.deniedDate;
    }

    public void setDeniedDate(Date deniedDate) {
        this.deniedDate = deniedDate;
    }

    public String getDeniedUser() {
        return this.deniedUser;
    }

    public void setDeniedUser(String deniedUser) {
        this.deniedUser = deniedUser;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCodeDescription() {
        return this.errorCodeDescription;
    }

    public void setErrorCodeDescription(String errorCodeDescription) {
        this.errorCodeDescription = errorCodeDescription;
    }

    public Long getFindSerial() {
        return this.findSerial;
    }

    public void setFindSerial(Long findSerial) {
        this.findSerial = findSerial;
    }

    public Date getLastModify() {
        return this.lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPrefixTemplate() {
        return this.prefixTemplate;
    }

    public void setPrefixTemplate(String prefixTemplate) {
        this.prefixTemplate = prefixTemplate;
    }

    public String getReceiptNo() {
        return this.receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseCodeDescription() {
        return this.responseCodeDescription;
    }

    public void setResponseCodeDescription(String responseCodeDescription) {
        this.responseCodeDescription = responseCodeDescription;
    }

    public Long getRetry() {
        return this.retry;
    }

    public void setRetry(Long retry) {
        this.retry = retry;
    }

    public String getSignUserList() {
        return this.signUserList;
    }

    public void setSignUserList(String signUserList) {
        this.signUserList = signUserList;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStockTransActionId() {
        return this.stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public String getStockTransVofficeId() {
        return this.stockTransVofficeId;
    }

    public void setStockTransVofficeId(String stockTransVofficeId) {
        this.stockTransVofficeId = stockTransVofficeId;
    }
}
