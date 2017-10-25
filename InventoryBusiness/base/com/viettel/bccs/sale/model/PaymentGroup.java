package com.viettel.bccs.sale.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "PAYMENT_GROUP")

public class PaymentGroup implements Serializable {
    public static enum COLUMNS {CREATEDATE, CREATEUSER, DEBITDAYTYPE, LASTUPDATEDATE, LASTUPDATEUSER, MAXDAYDEBIT, MAXMONEYDEBIT, NAME, PAYMENTGROUPID, STATUS, TYPE, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PAYMENT_GROUP_ID")
    @SequenceGenerator(name = "PAYMENT_GROUP_ID_GENERATOR", sequenceName = "PAYMENT_GROUP_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_GROUP_ID_GENERATOR")
    private Long paymentGroupId;
    @Basic(optional = false)
    @Column(name = "TYPE")
    private String type;
    @Column(name = "NAME")
    private String name;
    @Column(name = "MAX_MONEY_DEBIT")
    private Long maxMoneyDebit;
    @Column(name = "MAX_DAY_DEBIT")
    private Long maxDayDebit;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;
    @Column(name = "LAST_UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "DEBIT_DAY_TYPE")
    private Long debitDayType;

    public PaymentGroup() {
    }

    public PaymentGroup(Long paymentGroupId) {
        this.paymentGroupId = paymentGroupId;
    }

    public PaymentGroup(Long paymentGroupId, String type) {
        this.paymentGroupId = paymentGroupId;
        this.type = type;
    }

    public Long getPaymentGroupId() {
        return paymentGroupId;
    }

    public void setPaymentGroupId(Long paymentGroupId) {
        this.paymentGroupId = paymentGroupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMaxMoneyDebit() {
        return maxMoneyDebit;
    }

    public void setMaxMoneyDebit(Long maxMoneyDebit) {
        this.maxMoneyDebit = maxMoneyDebit;
    }

    public Long getMaxDayDebit() {
        return maxDayDebit;
    }

    public void setMaxDayDebit(Long maxDayDebit) {
        this.maxDayDebit = maxDayDebit;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getDebitDayType() {
        return debitDayType;
    }

    public void setDebitDayType(Long debitDayType) {
        this.debitDayType = debitDayType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymentGroupId != null ? paymentGroupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentGroup)) {
            return false;
        }
        PaymentGroup other = (PaymentGroup) object;
        if ((this.paymentGroupId == null && other.paymentGroupId != null) || (this.paymentGroupId != null && !this.paymentGroupId.equals(other.paymentGroupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.PaymentGroup[ paymentGroupId=" + paymentGroupId + " ]";
    }

}
