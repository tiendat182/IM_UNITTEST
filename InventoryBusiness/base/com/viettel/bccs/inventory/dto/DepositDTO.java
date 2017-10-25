package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class DepositDTO extends BaseDTO implements Serializable {

    private String address;
    private Long amount;
    private String branchId;
    private String contractNo;
    private String createBy;
    private Date createDate;
    private String custName;
    private Long deliverId;
    private Long depositId;
    private Long depositTypeId;
    private String description;
    private String idNo;
    private String isdn;
    private String primaryAccount;
    private Long reasonId;
    private Long receiptId;
    private String requestId;
    private Long shopId;
    private Long staffId;
    private String status;
    private Long stockTransId;
    private Long subId;
    private Long telecomServiceId;
    private String tin;
    private String type;

    public String getKeySet() {
        return keySet; }
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
    public String getBranchId() {
        return this.branchId;
    }
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    public String getContractNo() {
        return this.contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getCreateBy() {
        return this.createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCustName() {
        return this.custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public Long getDeliverId() {
        return this.deliverId;
    }
    public void setDeliverId(Long deliverId) {
        this.deliverId = deliverId;
    }
    public Long getDepositId() {
        return this.depositId;
    }
    public void setDepositId(Long depositId) {
        this.depositId = depositId;
    }
    public Long getDepositTypeId() {
        return this.depositTypeId;
    }
    public void setDepositTypeId(Long depositTypeId) {
        this.depositTypeId = depositTypeId;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIdNo() {
        return this.idNo;
    }
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
    public String getIsdn() {
        return this.isdn;
    }
    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }
    public String getPrimaryAccount() {
        return this.primaryAccount;
    }
    public void setPrimaryAccount(String primaryAccount) {
        this.primaryAccount = primaryAccount;
    }
    public Long getReasonId() {
        return this.reasonId;
    }
    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }
    public Long getReceiptId() {
        return this.receiptId;
    }
    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }
    public String getRequestId() {
        return this.requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
    public Long getStockTransId() {
        return this.stockTransId;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
    public Long getSubId() {
        return this.subId;
    }
    public void setSubId(Long subId) {
        this.subId = subId;
    }
    public Long getTelecomServiceId() {
        return this.telecomServiceId;
    }
    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }
    public String getTin() {
        return this.tin;
    }
    public void setTin(String tin) {
        this.tin = tin;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
