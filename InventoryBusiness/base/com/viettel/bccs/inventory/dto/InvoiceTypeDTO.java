package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.util.Date;

public class InvoiceTypeDTO extends BaseDTO {

    private Long blockNoLength;
    private String bookType;
    private Date createDatetime;
    private String createUser;
    private String invoiceName;
    private Long invoiceNoLength;
    private String invoiceType;
    private Long invoiceTypeId;
    private Long numInvoice;
    private String status;
    private String type;
    private String typeInv;
    private Date updateDatetime;
    private String updateUser;
    private String reasonCode;
    private boolean check;
    private String invoiceTypeText; //Hien thi mau so
    private String invoiceTypeName; //Ten loai hoa don

    public String getKeySet() { return keySet; }
    public String getReasonCode() {
        return reasonCode;
    }
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
    public Long getBlockNoLength() {
        return this.blockNoLength;
    }
    public void setBlockNoLength(Long blockNoLength) {
        this.blockNoLength = blockNoLength;
    }
    public String getBookType() {
        return this.bookType;
    }
    public void setBookType(String bookType) {
        this.bookType = bookType;
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
    public String getInvoiceName() {
        return this.invoiceName;
    }
    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }
    public Long getInvoiceNoLength() {
        return this.invoiceNoLength;
    }
    public void setInvoiceNoLength(Long invoiceNoLength) {
        this.invoiceNoLength = invoiceNoLength;
    }
    public String getInvoiceType() {
        return this.invoiceType;
    }
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }
    public Long getInvoiceTypeId() {
        return this.invoiceTypeId;
    }
    public void setInvoiceTypeId(Long invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }
    public Long getNumInvoice() {
        return this.numInvoice;
    }
    public void setNumInvoice(Long numInvoice) {
        this.numInvoice = numInvoice;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTypeInv() {
        return this.typeInv;
    }
    public void setTypeInv(String typeInv) {
        this.typeInv = typeInv;
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
    public String getInvoiceTypeText() {
        return invoiceTypeText;
    }
    public void setInvoiceTypeText(String invoiceTypeText) {
        this.invoiceTypeText = invoiceTypeText;
    }
    public String getInvoiceTypeName() {
        return invoiceTypeName;
    }
    public void setInvoiceTypeName(String invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
    }
    public boolean isCheck() {
        return check;
    }
    public void setCheck(boolean check) {
        this.check = check;
    }

}
