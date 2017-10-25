package com.viettel.bccs.im1.dto;
import java.lang.Long;
import java.util.Date;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
public class InvoiceTemplateLogIm1DTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private Long amountApply;
    private Date createDate;
    private String createUser;
    private Long fromShopId;
    private Long invoiceTemplateId;
    private Long invoiceTemplateLogId;
    private Long logType;
    private Long orgAmount;
    private Long shopId;
    private Long staffId;
    private Long valid;
    public Long getAmountApply() {
        return this.amountApply;
    }
    public void setAmountApply(Long amountApply) {
        this.amountApply = amountApply;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
    public Long getLogType() {
        return this.logType;
    }
    public void setLogType(Long logType) {
        this.logType = logType;
    }
    public Long getOrgAmount() {
        return this.orgAmount;
    }
    public void setOrgAmount(Long orgAmount) {
        this.orgAmount = orgAmount;
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
    public Long getValid() {
        return this.valid;
    }
    public void setValid(Long valid) {
        this.valid = valid;
    }
}
