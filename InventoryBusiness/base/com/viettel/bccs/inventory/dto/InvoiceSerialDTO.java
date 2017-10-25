package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class InvoiceSerialDTO extends BaseDTO implements Serializable {
    private Date createDatetime;
    private String createUser;
    private String description;
    private Long invoiceSerialId;
    private Long invoiceTypeId;
    private String serialNo;
    private Long shopId;
    private String status;
    private String invoiceType;
    private String shopName;
    private String statusText;
    private String invoiceTypeText; //Hien thi mau so
    private String invoiceTypeName; //Ten loai hoa don
    private Date updateDatetime;
    private String updateUser;
    private Long shopUsedId;
    private Long invoiceTrans;

    public String getKeySet() {
        return keySet;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getInvoiceSerialId() {
        return this.invoiceSerialId;
    }

    public void setInvoiceSerialId(Long invoiceSerialId) {
        this.invoiceSerialId = invoiceSerialId;
    }

    public Long getInvoiceTypeId() {
        return this.invoiceTypeId;
    }

    public void setInvoiceTypeId(Long invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStatusText() {
        if (DataUtil.safeEqual(status, "1")) {
            statusText = "Hiệu lực";
        } else {
            statusText = "Không hiệu lực";
        }
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
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

    public Long getShopUsedId() {
        return shopUsedId;
    }

    public void setShopUsedId(Long shopUsedId) {
        this.shopUsedId = shopUsedId;
    }

    public Long getInvoiceTrans() {
        return invoiceTrans;
    }

    public void setInvoiceTrans(Long invoiceTrans) {
        this.invoiceTrans = invoiceTrans;
    }
}
