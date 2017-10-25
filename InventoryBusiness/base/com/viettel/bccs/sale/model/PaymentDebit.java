package com.viettel.bccs.sale.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "PAYMENT_DEBIT")
public class PaymentDebit implements Serializable {
    public static enum COLUMNS {CREATEDATE, CREATEUSER, LASTUPDATEDATE, LASTUPDATEUSER, OWNERID, OWNERTYPE, PAYMENTDEBITID, PAYMENTGROUPID, PAYMENTGROUPSERVICEID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PAYMENT_DEBIT_ID")
    @SequenceGenerator(name = "PAYMENT_DEBIT_ID_GENERATOR", sequenceName = "PAYMENT_DEBIT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_DEBIT_ID_GENERATOR")
    private Long paymentDebitId;
    @Basic(optional = false)
    @Column(name = "PAYMENT_GROUP_ID")
    private Long paymentGroupId;
    @Basic(optional = false)
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Basic(optional = false)
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "LAST_UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;
    @Column(name = "PAYMENT_GROUP_SERVICE_ID")
    private Long paymentGroupServiceId;

    public PaymentDebit() {
    }

    public PaymentDebit(Long paymentDebitId) {
        this.paymentDebitId = paymentDebitId;
    }

    public PaymentDebit(Long paymentDebitId, Long paymentGroupId, Long ownerId, Long ownerType) {
        this.paymentDebitId = paymentDebitId;
        this.paymentGroupId = paymentGroupId;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
    }

    public Long getPaymentDebitId() {
        return paymentDebitId;
    }

    public void setPaymentDebitId(Long paymentDebitId) {
        this.paymentDebitId = paymentDebitId;
    }

    public Long getPaymentGroupId() {
        return paymentGroupId;
    }

    public void setPaymentGroupId(Long paymentGroupId) {
        this.paymentGroupId = paymentGroupId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Long getPaymentGroupServiceId() {
        return paymentGroupServiceId;
    }

    public void setPaymentGroupServiceId(Long paymentGroupServiceId) {
        this.paymentGroupServiceId = paymentGroupServiceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymentDebitId != null ? paymentDebitId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentDebit)) {
            return false;
        }
        PaymentDebit other = (PaymentDebit) object;
        if ((this.paymentDebitId == null && other.paymentDebitId != null) || (this.paymentDebitId != null && !this.paymentDebitId.equals(other.paymentDebitId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.PaymentDebit[ paymentDebitId=" + paymentDebitId + " ]";
    }

}
