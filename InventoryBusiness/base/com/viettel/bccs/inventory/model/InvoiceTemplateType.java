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
@Table(name = "INVOICE_TEMPLATE_TYPE")
@NamedQueries({
    @NamedQuery(name = "InvoiceTemplateType.findAll", query = "SELECT i FROM InvoiceTemplateType i")})
public class InvoiceTemplateType implements Serializable {
public static enum COLUMNS {CREATEDATETIME, CREATEUSER, INVOICETEMPLATETYPEID, INVOICETYPE, NAME, STATUS, UPDATEDATETIME, UPDATEUSER, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INVOICE_TEMPLATE_TYPE_ID")
    @SequenceGenerator(name = "INVOICE_TEMPLATE_TYPE_ID_GENERATOR", sequenceName = "INVOICE_TEMPLATE_TYPE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_TEMPLATE_TYPE_ID_GENERATOR")
    private Long invoiceTemplateTypeId;
    @Basic(optional = false)
    @Column(name = "INVOICE_TYPE")
    private String invoiceType;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;

    public InvoiceTemplateType() {
    }

    public InvoiceTemplateType(Long invoiceTemplateTypeId) {
        this.invoiceTemplateTypeId = invoiceTemplateTypeId;
    }

    public InvoiceTemplateType(Long invoiceTemplateTypeId, String invoiceType) {
        this.invoiceTemplateTypeId = invoiceTemplateTypeId;
        this.invoiceType = invoiceType;
    }

    public Long getInvoiceTemplateTypeId() {
        return invoiceTemplateTypeId;
    }

    public void setInvoiceTemplateTypeId(Long invoiceTemplateTypeId) {
        this.invoiceTemplateTypeId = invoiceTemplateTypeId;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceTemplateTypeId != null ? invoiceTemplateTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceTemplateType)) {
            return false;
        }
        InvoiceTemplateType other = (InvoiceTemplateType) object;
        if ((this.invoiceTemplateTypeId == null && other.invoiceTemplateTypeId != null) || (this.invoiceTemplateTypeId != null && !this.invoiceTemplateTypeId.equals(other.invoiceTemplateTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.InvoiceTemplateType[ invoiceTemplateTypeId=" + invoiceTemplateTypeId + " ]";
    }
    
}
