package com.viettel.bccs.inventory.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.lang.Long;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "RECEIPT_EXPENSE")
@XmlRootElement
public class ReceiptExpense implements Serializable {
public static enum COLUMNS {ADDRESS, AMOUNT, APPROVERDATE, APPROVERSTAFFID, CREATEDATETIME, DEBT, DELIVERER, DELIVERERSHOPID, DELIVERERSTAFFID, DESTROYDATE, DESTROYSTAFFID, NOTES, OWN, PAYMETHOD, REASONID, RECEIPTDATE, RECEIPTID, RECEIPTNO, RECEIPTTYPEID, SHOPID, STAFFID, STATUS, TELECOMSERVICEID, TYPE, USERNAME, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "RECEIPT_ID")
    @SequenceGenerator(name = "RECEIPT_ID_GENERATOR", sequenceName = "RECEIPT_EXPENSE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECEIPT_ID_GENERATOR")
    private Long receiptId;
    @Basic(optional = false)
    @Column(name = "RECEIPT_NO")
    private String receiptNo;
    @Basic(optional = false)
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Basic(optional = false)
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "DELIVERER_STAFF_ID")
    private Long delivererStaffId;
    @Column(name = "DELIVERER_SHOP_ID")
    private Long delivererShopId;
    @Basic(optional = false)
    @Column(name = "TYPE")
    private String type;
    @Basic(optional = false)
    @Column(name = "RECEIPT_TYPE_ID")
    private Long receiptTypeId;
    @Basic(optional = false)
    @Column(name = "RECEIPT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiptDate;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Basic(optional = false)
    @Column(name = "PAY_METHOD")
    private String payMethod;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private Long amount;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "APPROVER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approverDate;
    @Column(name = "DELIVERER")
    private String deliverer;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "DESTROY_STAFF_ID")
    private Long destroyStaffId;
    @Column(name = "DESTROY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date destroyDate;
    @Column(name = "APPROVER_STAFF_ID")
    private Long approverStaffId;
    @Column(name = "OWN")
    private String own;
    @Column(name = "DEBT")
    private String debt;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;

    public ReceiptExpense() {
    }

    public ReceiptExpense(Long receiptId) {
        this.receiptId = receiptId;
    }

    public ReceiptExpense(Long receiptId, String receiptNo, Long shopId, Long staffId, String type, Long receiptTypeId, Date receiptDate, String payMethod, Long amount, String status, String username, Date createDatetime) {
        this.receiptId = receiptId;
        this.receiptNo = receiptNo;
        this.shopId = shopId;
        this.staffId = staffId;
        this.type = type;
        this.receiptTypeId = receiptTypeId;
        this.receiptDate = receiptDate;
        this.payMethod = payMethod;
        this.amount = amount;
        this.status = status;
        this.username = username;
        this.createDatetime = createDatetime;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getDelivererStaffId() {
        return delivererStaffId;
    }

    public void setDelivererStaffId(Long delivererStaffId) {
        this.delivererStaffId = delivererStaffId;
    }

    public Long getDelivererShopId() {
        return delivererShopId;
    }

    public void setDelivererShopId(Long delivererShopId) {
        this.delivererShopId = delivererShopId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getReceiptTypeId() {
        return receiptTypeId;
    }

    public void setReceiptTypeId(Long receiptTypeId) {
        this.receiptTypeId = receiptTypeId;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getApproverDate() {
        return approverDate;
    }

    public void setApproverDate(Date approverDate) {
        this.approverDate = approverDate;
    }

    public String getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(String deliverer) {
        this.deliverer = deliverer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getDestroyStaffId() {
        return destroyStaffId;
    }

    public void setDestroyStaffId(Long destroyStaffId) {
        this.destroyStaffId = destroyStaffId;
    }

    public Date getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(Date destroyDate) {
        this.destroyDate = destroyDate;
    }

    public Long getApproverStaffId() {
        return approverStaffId;
    }

    public void setApproverStaffId(Long approverStaffId) {
        this.approverStaffId = approverStaffId;
    }

    public String getOwn() {
        return own;
    }

    public void setOwn(String own) {
        this.own = own;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (receiptId != null ? receiptId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReceiptExpense)) {
            return false;
        }
        ReceiptExpense other = (ReceiptExpense) object;
        if ((this.receiptId == null && other.receiptId != null) || (this.receiptId != null && !this.receiptId.equals(other.receiptId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.ReceiptExpense[ receiptId=" + receiptId + " ]";
    }

}

