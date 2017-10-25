package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class ReceiptExpenseDTO extends BaseDTO implements Serializable {

    private String address;
    private Long amount;
    private Date approverDate;
    private Long approverStaffId;
    private Date createDatetime;
    private String debt;
    private String deliverer;
    private Long delivererShopId;
    private Long delivererStaffId;
    private Date destroyDate;
    private Long destroyStaffId;
    private String notes;
    private String own;
    private String payMethod;
    private Long reasonId;
    private Date receiptDate;
    private Long receiptId;
    private String receiptNo;
    private Long receiptTypeId;
    private Long shopId;
    private Long staffId;
    private String status;
    private Long telecomServiceId;
    private String type;
    private String username;
    private String reasonName;
    private Long deliverShopId;
    private String deliverShopName;
    private String payMethodName;
    private Long depositId;

    public String getKeySet() {
        return keySet;
    }

    public Long getDepositId() {
        return depositId;
    }

    public void setDepositId(Long depositId) {
        this.depositId = depositId;
    }

    public String getPayMethodName() {
        return payMethodName;
    }

    public void setPayMethodName(String payMethodName) {
        this.payMethodName = payMethodName;
    }

    public Long getDeliverShopId() {
        return deliverShopId;
    }

    public void setDeliverShopId(Long deliverShopId) {
        this.deliverShopId = deliverShopId;
    }

    public String getDeliverShopName() {
        return deliverShopName;
    }

    public void setDeliverShopName(String deliverShopName) {
        this.deliverShopName = deliverShopName;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getApproverDate() {
        return this.approverDate;
    }

    public void setApproverDate(Date approverDate) {
        this.approverDate = approverDate;
    }

    public Long getApproverStaffId() {
        return this.approverStaffId;
    }

    public void setApproverStaffId(Long approverStaffId) {
        this.approverStaffId = approverStaffId;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getDebt() {
        return this.debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getDeliverer() {
        return this.deliverer;
    }

    public void setDeliverer(String deliverer) {
        this.deliverer = deliverer;
    }

    public Long getDelivererShopId() {
        return this.delivererShopId;
    }

    public void setDelivererShopId(Long delivererShopId) {
        this.delivererShopId = delivererShopId;
    }

    public Long getDelivererStaffId() {
        return this.delivererStaffId;
    }

    public void setDelivererStaffId(Long delivererStaffId) {
        this.delivererStaffId = delivererStaffId;
    }

    public Date getDestroyDate() {
        return this.destroyDate;
    }

    public void setDestroyDate(Date destroyDate) {
        this.destroyDate = destroyDate;
    }

    public Long getDestroyStaffId() {
        return this.destroyStaffId;
    }

    public void setDestroyStaffId(Long destroyStaffId) {
        this.destroyStaffId = destroyStaffId;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOwn() {
        return this.own;
    }

    public void setOwn(String own) {
        this.own = own;
    }

    public String getPayMethod() {
        return this.payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Long getReasonId() {
        return this.reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Date getReceiptDate() {
        return this.receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Long getReceiptId() {
        return this.receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptNo() {
        return this.receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Long getReceiptTypeId() {
        return this.receiptTypeId;
    }

    public void setReceiptTypeId(Long receiptTypeId) {
        this.receiptTypeId = receiptTypeId;
    }

    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTelecomServiceId() {
        return this.telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
