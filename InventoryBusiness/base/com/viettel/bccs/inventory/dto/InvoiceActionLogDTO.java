package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;
public class InvoiceActionLogDTO extends BaseDTO {

    private String actionType;
    private Date approveDate;
    private String approveUser;
    private String blockNo;
    private Date createDatetime;
    private String createUser;
    private Long curInvoice;
    private String description;
    private Long fromInvoice;
    private Long fromShopId;
    private Long fromStaffId;
    private Long invoiceActionId;
    private Long invoiceListId;
    private String serialNo;
    private Long toInvoice;
    private Long toShopId;
    private Long toStaffId;
    public String getKeySet() {
        return keySet; }
    public String getActionType() {
        return this.actionType;
    }
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    public Date getApproveDate() {
        return this.approveDate;
    }
    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }
    public String getApproveUser() {
        return this.approveUser;
    }
    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }
    public String getBlockNo() {
        return this.blockNo;
    }
    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
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
    public Long getCurInvoice() {
        return this.curInvoice;
    }
    public void setCurInvoice(Long curInvoice) {
        this.curInvoice = curInvoice;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getFromInvoice() {
        return this.fromInvoice;
    }
    public void setFromInvoice(Long fromInvoice) {
        this.fromInvoice = fromInvoice;
    }
    public Long getFromShopId() {
        return this.fromShopId;
    }
    public void setFromShopId(Long fromShopId) {
        this.fromShopId = fromShopId;
    }
    public Long getFromStaffId() {
        return this.fromStaffId;
    }
    public void setFromStaffId(Long fromStaffId) {
        this.fromStaffId = fromStaffId;
    }
    public Long getInvoiceActionId() {
        return this.invoiceActionId;
    }
    public void setInvoiceActionId(Long invoiceActionId) {
        this.invoiceActionId = invoiceActionId;
    }
    public Long getInvoiceListId() {
        return this.invoiceListId;
    }
    public void setInvoiceListId(Long invoiceListId) {
        this.invoiceListId = invoiceListId;
    }
    public String getSerialNo() {
        return this.serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public Long getToInvoice() {
        return this.toInvoice;
    }
    public void setToInvoice(Long toInvoice) {
        this.toInvoice = toInvoice;
    }
    public Long getToShopId() {
        return this.toShopId;
    }
    public void setToShopId(Long toShopId) {
        this.toShopId = toShopId;
    }
    public Long getToStaffId() {
        return this.toStaffId;
    }
    public void setToStaffId(Long toStaffId) {
        this.toStaffId = toStaffId;
    }
}
