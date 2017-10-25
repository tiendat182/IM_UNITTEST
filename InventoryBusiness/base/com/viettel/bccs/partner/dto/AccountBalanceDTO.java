package com.viettel.bccs.partner.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class AccountBalanceDTO extends BaseDTO implements Serializable {

    private Long accountId;
    private Long balanceId;
    private Long balanceType;
    private Date dateCreated;
    private Date endDate;
    private Date lastUpdateTime;
    private Long realBalance;
    private Long realDebit;
    private Date startDate;
    private Long status;
    private String userCreated;

    public String getKeySet() {
        return keySet;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getBalanceId() {
        return this.balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public Long getBalanceType() {
        return this.balanceType;
    }

    public void setBalanceType(Long balanceType) {
        this.balanceType = balanceType;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Long getRealBalance() {
        return this.realBalance;
    }

    public void setRealBalance(Long realBalance) {
        this.realBalance = realBalance;
    }

    public Long getRealDebit() {
        return this.realDebit;
    }

    public void setRealDebit(Long realDebit) {
        this.realDebit = realDebit;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getUserCreated() {
        return this.userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }
}
