package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;

public class InvoiceListDTO extends BaseDTO {
    private Date approveDatetime;
    private String approveUser;
    private String blockNo;
    private Date createDatetime;
    private String createUser;
    private Long currInvoice;
    private Date destroyDate;
    private String destroyReasonId;
    private String destroyUser;
    private Long fromInvoice;
    private Long invoiceListId;
    private Long invoiceSerialId;
    private String serialNo;
    private Long shopId;
    private Long staffId;
    private String status;
    private Long toInvoice;

    private String invoiceType;
    private Long invoiceTypeId;
    private String invoiceName;
    private String invoiceInv;
    private String shopName;
    private String invoiceTypeName;
    private Date updateDatetime;
    private String updateUser;
    private Boolean isUpdate;
    private Long invoiceTrans;

    //hoangnt

    private String statusText;

    public String getKeySet() {
        return keySet;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public Date getApproveDatetime() {
        return this.approveDatetime;
    }

    public void setApproveDatetime(Date approveDatetime) {
        this.approveDatetime = approveDatetime;
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

    public Long getCurrInvoice() {
        return this.currInvoice;
    }

    public void setCurrInvoice(Long currInvoice) {
        this.currInvoice = currInvoice;
    }

    public Date getDestroyDate() {
        return this.destroyDate;
    }

    public void setDestroyDate(Date destroyDate) {
        this.destroyDate = destroyDate;
    }

    public String getDestroyReasonId() {
        return this.destroyReasonId;
    }

    public void setDestroyReasonId(String destroyReasonId) {
        this.destroyReasonId = destroyReasonId;
    }

    public String getDestroyUser() {
        return this.destroyUser;
    }

    public void setDestroyUser(String destroyUser) {
        this.destroyUser = destroyUser;
    }

    public Long getFromInvoice() {
        return this.fromInvoice;
    }

    public void setFromInvoice(Long fromInvoice) {
        this.fromInvoice = fromInvoice;
    }

    public Long getInvoiceListId() {
        return this.invoiceListId;
    }

    public void setInvoiceListId(Long invoiceListId) {
        this.invoiceListId = invoiceListId;
    }

    public Long getInvoiceSerialId() {
        return this.invoiceSerialId;
    }

    public void setInvoiceSerialId(Long invoiceSerialId) {
        this.invoiceSerialId = invoiceSerialId;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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

    public Long getToInvoice() {
        return this.toInvoice;
    }

    public void setToInvoice(Long toInvoice) {
        this.toInvoice = toInvoice;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceInv() {
        return invoiceInv;
    }

    public void setInvoiceInv(String invoiceInv) {
        this.invoiceInv = invoiceInv;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStatusText() {
        if ("1".equals(status)) {
            statusText = "Hiệu lực";
        } else {
            statusText = "Không hiệu lực";
        }
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Long getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(Long invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getInvoiceTypeName() {
        return invoiceTypeName;
    }

    public void setInvoiceTypeName(String invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Long getInvoiceTrans() {
        return invoiceTrans;
    }

    public void setInvoiceTrans(Long invoiceTrans) {
        this.invoiceTrans = invoiceTrans;
    }
}
