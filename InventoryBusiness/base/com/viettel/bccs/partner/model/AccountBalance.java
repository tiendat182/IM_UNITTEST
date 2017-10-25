/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.partner.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author DungPT16
 */
@Entity
@Table(name = "ACCOUNT_BALANCE", schema = "BCCS_IM")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "AccountBalance.findAll", query = "SELECT a FROM AccountBalance a")})
public class AccountBalance implements Serializable {
    public static enum COLUMNS {ACCOUNTID, BALANCEID, BALANCETYPE, DATECREATED, ENDDATE, LASTUPDATETIME, REALBALANCE, REALDEBIT, STARTDATE, STATUS, USERCREATED, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "BALANCE_ID")
    private Long balanceId;
    @Column(name = "ACCOUNT_ID")
    private Long accountId;
    @Column(name = "BALANCE_TYPE")
    private Long balanceType;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "REAL_BALANCE")
    private Long realBalance;
    @Column(name = "REAL_DEBIT")
    private Long realDebit;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "USER_CREATED")
    private String userCreated;
    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "LAST_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    public AccountBalance() {
    }

    public AccountBalance(Long balanceId) {
        this.balanceId = balanceId;
    }

    public Long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(Long balanceType) {
        this.balanceType = balanceType;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getRealBalance() {
        return realBalance;
    }

    public void setRealBalance(Long realBalance) {
        this.realBalance = realBalance;
    }

    public Long getRealDebit() {
        return realDebit;
    }

    public void setRealDebit(Long realDebit) {
        this.realDebit = realDebit;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (balanceId != null ? balanceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountBalance)) {
            return false;
        }
        AccountBalance other = (AccountBalance) object;
        if ((this.balanceId == null && other.balanceId != null) || (this.balanceId != null && !this.balanceId.equals(other.balanceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.partner.model.AccountBalance[ balanceId=" + balanceId + " ]";
    }

}
