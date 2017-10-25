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
 * @author thetm1
 */
@Entity
@Table(name = "DEBIT_REQUEST_DETAIL")
public class DebitRequestDetail implements Serializable {
    public static enum COLUMNS {DEBITDAYTYPE, DEBITVALUE, FINANCETYPE, NOTE, OWNERID, OWNERTYPE, REQUESTDATE, REQUESTDETAILID, REQUESTID, PAYMENTTYPE, PAYMENTGROUPID, PAYMENTGROUPSERVICEID, REQUESTTYPE, STATUS, EXCLUSE_ID_LIST}

    ;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "REQUEST_DETAIL_ID")
    @SequenceGenerator(name = "DEBIT_REQUEST_DETAIL_ID_GENERATOR", sequenceName = "DEBIT_REQUEST_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEBIT_REQUEST_DETAIL_ID_GENERATOR")
    private Long requestDetailId;
    @Column(name = "REQUEST_TYPE")
    private String requestType;
    @Column(name = "DEBIT_DAY_TYPE")
    private String debitDayType;
    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private String ownerType;
    @Column(name = "DEBIT_VALUE")
    private Long debitValue;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "FINANCE_TYPE")
    private String financeType;
    @Column(name = "REQUEST_ID")
    private Long requestId;
    @Column(name = "PAYMENT_TYPE")
    private String paymentType;
    @Column(name = "PAYMENT_GROUP_ID")
    private Long paymentGroupId;
    @Column(name = "PAYMENT_GROUP_SERVICE_ID")
    private Long paymentGroupServiceId;
    @Column(name = "PAYMENT_GROUP_NAME")
    private String paymentGroupName;
    @Column(name = "PAYMENT_GROUP_SERVICE_NAME")
    private String paymentGroupServiceName;
    @Column(name = "PAYMENT_DEBIT_VALUE")
    private Long paymentDebitValue;
    @Column(name = "DISTANCE")
    private Double distance;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getPaymentGroupName() {
        return paymentGroupName;
    }

    public void setPaymentGroupName(String paymentGroupName) {
        this.paymentGroupName = paymentGroupName;
    }

    public String getPaymentGroupServiceName() {
        return paymentGroupServiceName;
    }

    public void setPaymentGroupServiceName(String paymentGroupServiceName) {
        this.paymentGroupServiceName = paymentGroupServiceName;
    }

    public Long getPaymentDebitValue() {
        return paymentDebitValue;
    }

    public void setPaymentDebitValue(Long paymentDebitValue) {
        this.paymentDebitValue = paymentDebitValue;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getPaymentGroupId() {
        return paymentGroupId;
    }

    public void setPaymentGroupId(Long paymentGroupId) {
        this.paymentGroupId = paymentGroupId;
    }

    public Long getPaymentGroupServiceId() {
        return paymentGroupServiceId;
    }

    public void setPaymentGroupServiceId(Long paymentGroupServiceId) {
        this.paymentGroupServiceId = paymentGroupServiceId;
    }

    public DebitRequestDetail() {
    }

    public DebitRequestDetail(Long requestDetailId) {
        this.requestDetailId = requestDetailId;
    }

    public Long getRequestDetailId() {
        return requestDetailId;
    }

    public void setRequestDetailId(Long requestDetailId) {
        this.requestDetailId = requestDetailId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getDebitDayType() {
        return debitDayType;
    }

    public void setDebitDayType(String debitDayType) {
        this.debitDayType = debitDayType;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getDebitValue() {
        return debitValue;
    }

    public void setDebitValue(Long debitValue) {
        this.debitValue = debitValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFinanceType() {
        return financeType;
    }

    public void setFinanceType(String financeType) {
        this.financeType = financeType;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requestDetailId != null ? requestDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DebitRequestDetail)) {
            return false;
        }
        DebitRequestDetail other = (DebitRequestDetail) object;
        if ((this.requestDetailId == null && other.requestDetailId != null) || (this.requestDetailId != null && !this.requestDetailId.equals(other.requestDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.DebitRequestDetail[ requestDetailId=" + requestDetailId + " ]";
    }

}
