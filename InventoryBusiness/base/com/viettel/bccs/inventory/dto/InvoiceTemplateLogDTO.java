package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;
public class InvoiceTemplateLogDTO extends BaseDTO {
    private String afterAmount;
    private Date createDatetime;
    private String createUser;
    private Long fromShopId;
    private Long invoiceTemplateId;
    private Long invoiceTemplateLogId;
    private String logType;
    private Long orgAmount;
    private Long ownerId;
    private String ownerType;
    private Long reasonId;
    public String getKeySet() {
        return keySet; }
    public String getAfterAmount() {
        return this.afterAmount;
    }
    public void setAfterAmount(String afterAmount) {
        this.afterAmount = afterAmount;
    }
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    public String getCreateUser() {
        return this.createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public Long getFromShopId() {
        return this.fromShopId;
    }
    public void setFromShopId(Long fromShopId) {
        this.fromShopId = fromShopId;
    }
    public Long getInvoiceTemplateId() {
        return this.invoiceTemplateId;
    }
    public void setInvoiceTemplateId(Long invoiceTemplateId) {
        this.invoiceTemplateId = invoiceTemplateId;
    }
    public Long getInvoiceTemplateLogId() {
        return this.invoiceTemplateLogId;
    }
    public void setInvoiceTemplateLogId(Long invoiceTemplateLogId) {
        this.invoiceTemplateLogId = invoiceTemplateLogId;
    }
    public String getLogType() {
        return this.logType;
    }
    public void setLogType(String logType) {
        this.logType = logType;
    }
    public Long getOrgAmount() {
        return this.orgAmount;
    }
    public void setOrgAmount(Long orgAmount) {
        this.orgAmount = orgAmount;
    }
    public Long getOwnerId() {
        return this.ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public String getOwnerType() {
        return this.ownerType;
    }
    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
    public Long getReasonId() {
        return this.reasonId;
    }
    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }
}
