package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.util.Date;

public class InvoiceTemplateDTO extends BaseDTO {
    private Long amount;
    private Date createDatetime;
    private String createUser;
    private String description;
    private Long fromShopId;
    private Long invoiceTemplateId;
    private Long invoiceTemplateTypeId;
    private Long ownerId;
    private String ownerType;
    private Date updateDatetime;
    private String updateUser;
    private Long usedAmount;
    private String invoiceTemplateTypeName;
    private String fromShopName;
    private String ownerdName;
    private Long amountDelivery;
    private String strAmountDelivery;
    private Long amountRetrieve;
    private String strAmountRetrieve;
    private Long updateAmount;
    private Long amountNotUsed;

    public String getKeySet() {

        return keySet;
    }

    public String getInvoiceTemplateTypeName() {
        return invoiceTemplateTypeName;
    }

    public void setInvoiceTemplateTypeName(String invoiceTemplateTypeName) {
        this.invoiceTemplateTypeName = invoiceTemplateTypeName;
    }

    public Long getAmountDelivery() {
        return amountDelivery;
    }

    public void setAmountDelivery(Long amountDelivery) {
        this.amountDelivery = amountDelivery;
    }

    public Long getAmountRetrieve() {
        return amountRetrieve;
    }

    public void setAmountRetrieve(Long amountRetrieve) {
        this.amountRetrieve = amountRetrieve;
    }

    public String getFromShopName() {
        return fromShopName;
    }

    public void setFromShopName(String fromShopName) {
        this.fromShopName = fromShopName;
    }

    public String getOwnerdName() {
        return ownerdName;
    }

    public void setOwnerdName(String ownerdName) {
        this.ownerdName = ownerdName;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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

    public Long getInvoiceTemplateTypeId() {
        return this.invoiceTemplateTypeId;
    }

    public void setInvoiceTemplateTypeId(Long invoiceTemplateTypeId) {
        this.invoiceTemplateTypeId = invoiceTemplateTypeId;
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

    public Long getUsedAmount() {
        return this.usedAmount;
    }

    public void setUsedAmount(Long usedAmount) {
        this.usedAmount = usedAmount;
    }

    public Long getUpdateAmount() {
        return updateAmount;
    }

    public void setUpdateAmount(Long updateAmount) {
        this.updateAmount = updateAmount;
    }

    public String getStrAmountRetrieve() {
        return strAmountRetrieve;
    }

    public void setStrAmountRetrieve(String strAmountRetrieve) {
        this.strAmountRetrieve = strAmountRetrieve;
        amountRetrieve = DataUtil.safeToLong(strAmountRetrieve);
    }

    public String getStrAmountDelivery() {

        return strAmountDelivery;
    }

    public void setStrAmountDelivery(String strAmountDelivery) {
        this.strAmountDelivery = strAmountDelivery;
        amountDelivery = DataUtil.safeToLong(strAmountDelivery);
    }

    public Long getAmountNotUsed() {
        return amountNotUsed;
    }

    public void setAmountNotUsed(Long amountNotUsed) {
        this.amountNotUsed = amountNotUsed;
    }
}
