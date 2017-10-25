package com.viettel.bccs.inventory.dto;
import com.viettel.fw.dto.BaseDTO;

import java.util.Date;
public class InvoiceTemplateTypeDTO extends BaseDTO {

    private Date createDatetime;
    private String createUser;
    private Long invoiceTemplateTypeId;
    private String invoiceType;
    private String name;
    private String status;
    private Date updateDatetime;
    private String updateUser;
    public String getKeySet() {
        return keySet; }
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
    public Long getInvoiceTemplateTypeId() {
        return this.invoiceTemplateTypeId;
    }
    public void setInvoiceTemplateTypeId(Long invoiceTemplateTypeId) {
        this.invoiceTemplateTypeId = invoiceTemplateTypeId;
    }
    public String getInvoiceType() {
        return this.invoiceType;
    }
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
    public String getUpdateUser() {
        return this.updateUser;
    }
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
