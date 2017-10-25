package com.viettel.bccs.sale.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "PAYMENT_GROUP_SERVICE_DETAIL")

public class PaymentGroupServiceDetail implements Serializable {
public static enum COLUMNS {ID, MAXDAYDEBIT, PAYMENTGROUPSERVICEID, TELECOMSERVICEID, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "PAYMENT_GROUP_SERVICE_DETAIL_ID_GENERATOR", sequenceName = "PAYMENT_GROUP_SERVICE_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_GROUP_SERVICE_DETAIL_ID_GENERATOR")
    private Long id;
    @Column(name = "PAYMENT_GROUP_SERVICE_ID")
    private Long paymentGroupServiceId;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Column(name = "MAX_DAY_DEBIT")
    private Long maxDayDebit;

    public PaymentGroupServiceDetail() {
    }

    public PaymentGroupServiceDetail(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaymentGroupServiceId() {
        return paymentGroupServiceId;
    }

    public void setPaymentGroupServiceId(Long paymentGroupServiceId) {
        this.paymentGroupServiceId = paymentGroupServiceId;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public Long getMaxDayDebit() {
        return maxDayDebit;
    }

    public void setMaxDayDebit(Long maxDayDebit) {
        this.maxDayDebit = maxDayDebit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentGroupServiceDetail)) {
            return false;
        }
        PaymentGroupServiceDetail other = (PaymentGroupServiceDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.PaymentGroupServiceDetail[ id=" + id + " ]";
    }

}
