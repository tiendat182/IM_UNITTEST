/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author hoangnt14
 */
@Entity
@Table(name = "INVOICE_TEMPLATE")
@NamedQueries({
    @NamedQuery(name = "InvoiceTemplate.findAll", query = "SELECT i FROM InvoiceTemplate i")})
public class InvoiceTemplate implements Serializable {
public static enum COLUMNS {AMOUNT, CREATEDATETIME, CREATEUSER, DESCRIPTION, FROMSHOPID, INVOICETEMPLATEID, INVOICETEMPLATETYPEID, OWNERID, OWNERTYPE, UPDATEDATETIME, UPDATEUSER, USEDAMOUNT, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INVOICE_TEMPLATE_ID")
    @SequenceGenerator(name = "INVOICE_TEMPLATE_ID_GENERATOR", sequenceName = "INVOICE_TEMPLATE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_TEMPLATE_ID_GENERATOR")
    private Long invoiceTemplateId;
    @Basic(optional = false)
    @Column(name = "INVOICE_TEMPLATE_TYPE_ID")
    private Long invoiceTemplateTypeId;
    @Column(name = "FROM_SHOP_ID")
    private Long fromShopId;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private String ownerType;
    @Column(name = "AMOUNT")
    private Long amount;
    @Column(name = "USED_AMOUNT")
    private Long usedAmount;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;
    @Column(name = "UPDATE_USER")
    private String updateUser;

    public InvoiceTemplate() {
    }

    public InvoiceTemplate(Long invoiceTemplateId) {
        this.invoiceTemplateId = invoiceTemplateId;
    }

    public InvoiceTemplate(Long invoiceTemplateId, Long invoiceTemplateTypeId) {
        this.invoiceTemplateId = invoiceTemplateId;
        this.invoiceTemplateTypeId = invoiceTemplateTypeId;
    }

    public Long getInvoiceTemplateId() {
        return invoiceTemplateId;
    }

    public void setInvoiceTemplateId(Long invoiceTemplateId) {
        this.invoiceTemplateId = invoiceTemplateId;
    }

    public Long getInvoiceTemplateTypeId() {
        return invoiceTemplateTypeId;
    }

    public void setInvoiceTemplateTypeId(Long invoiceTemplateTypeId) {
        this.invoiceTemplateTypeId = invoiceTemplateTypeId;
    }

    public Long getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(Long fromShopId) {
        this.fromShopId = fromShopId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(Long usedAmount) {
        this.usedAmount = usedAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceTemplateId != null ? invoiceTemplateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceTemplate)) {
            return false;
        }
        InvoiceTemplate other = (InvoiceTemplate) object;
        if ((this.invoiceTemplateId == null && other.invoiceTemplateId != null) || (this.invoiceTemplateId != null && !this.invoiceTemplateId.equals(other.invoiceTemplateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.InvoiceTemplate[ invoiceTemplateId=" + invoiceTemplateId + " ]";
    }
    
}
