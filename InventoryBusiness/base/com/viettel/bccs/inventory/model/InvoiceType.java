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
@Table(name = "INVOICE_TYPE")
@NamedQueries({
    @NamedQuery(name = "InvoiceType.findAll", query = "SELECT i FROM InvoiceType i")})
public class InvoiceType implements Serializable {
public static enum COLUMNS {BLOCKNOLENGTH, BOOKTYPE, CREATEDATETIME, CREATEUSER, INVOICENAME, INVOICENOLENGTH, INVOICETYPE, INVOICETYPEID, NUMINVOICE, STATUS, TYPE, TYPEINV, UPDATEDATETIME, UPDATEUSER, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INVOICE_TYPE_ID")
    @SequenceGenerator(name = "INVOICE_TYPE_ID_GENERATOR", sequenceName = "INVOICE_TYPE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_TYPE_ID_GENERATOR")
    private Long invoiceTypeId;
    @Basic(optional = false)
    @Column(name = "INVOICE_TYPE")
    private String invoiceType;
    @Basic(optional = false)
    @Column(name = "TYPE")
    private String type;
    @Basic(optional = false)
    @Column(name = "INVOICE_NAME")
    private String invoiceName;
    @Column(name = "BLOCK_NO_LENGTH")
    private Long blockNoLength;
    @Basic(optional = false)
    @Column(name = "INVOICE_NO_LENGTH")
    private Long invoiceNoLength;
    @Column(name = "NUM_INVOICE")
    private Long numInvoice;
    @Column(name = "BOOK_TYPE")
    private String bookType;
    @Basic(optional = false)
    @Column(name = "TYPE_INV")
    private String typeInv;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @Column(name = "CREATE_USER")
    private String createUser;
    @Basic(optional = false)
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;

    public InvoiceType() {
    }

    public InvoiceType(Long invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public InvoiceType(Long invoiceTypeId, String invoiceType, String type, String invoiceName, Long invoiceNoLength, String typeInv, String status, String createUser, Date createDatetime) {
        this.invoiceTypeId = invoiceTypeId;
        this.invoiceType = invoiceType;
        this.type = type;
        this.invoiceName = invoiceName;
        this.invoiceNoLength = invoiceNoLength;
        this.typeInv = typeInv;
        this.status = status;
        this.createUser = createUser;
        this.createDatetime = createDatetime;
    }

    public Long getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(Long invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public Long getBlockNoLength() {
        return blockNoLength;
    }

    public void setBlockNoLength(Long blockNoLength) {
        this.blockNoLength = blockNoLength;
    }

    public Long getInvoiceNoLength() {
        return invoiceNoLength;
    }

    public void setInvoiceNoLength(Long invoiceNoLength) {
        this.invoiceNoLength = invoiceNoLength;
    }

    public Long getNumInvoice() {
        return numInvoice;
    }

    public void setNumInvoice(Long numInvoice) {
        this.numInvoice = numInvoice;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getTypeInv() {
        return typeInv;
    }

    public void setTypeInv(String typeInv) {
        this.typeInv = typeInv;
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
        hash += (invoiceTypeId != null ? invoiceTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceType)) {
            return false;
        }
        InvoiceType other = (InvoiceType) object;
        if ((this.invoiceTypeId == null && other.invoiceTypeId != null) || (this.invoiceTypeId != null && !this.invoiceTypeId.equals(other.invoiceTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.InvoiceType[ invoiceTypeId=" + invoiceTypeId + " ]";
    }
    
}
