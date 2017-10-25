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
@Table(name = "INVOICE_SERIAL")
@NamedQueries({
    @NamedQuery(name = "InvoiceSerial.findAll", query = "SELECT i FROM InvoiceSerial i")})
public class InvoiceSerial implements Serializable {
public static enum COLUMNS {SHOPUSEDID, UPDATEDATETIME, UPDATEUSER, CREATEDATETIME, CREATEUSER, DESCRIPTION, INVOICESERIALID, INVOICETYPEID, SERIALNO, SHOPID, STATUS, INVOICETRANS, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INVOICE_SERIAL_ID")
    @SequenceGenerator(name = "INVOICE_SERIAL_ID_GENERATOR", sequenceName = "INVOICE_SERIAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_SERIAL_ID_GENERATOR")
    private Long invoiceSerialId;
    @Basic(optional = false)
    @Column(name = "INVOICE_TYPE_ID")
    private Long invoiceTypeId;
    @Basic(optional = false)
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "SHOP_ID")
    private Long shopId;
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
    @Column(name = "SHOP_USED_ID")
    private Long shopUsedId;
    @Column(name = "INVOICE_TRANS")
    private Long invoiceTrans;

    public InvoiceSerial() {
    }

    public InvoiceSerial(Long invoiceSerialId) {
        this.invoiceSerialId = invoiceSerialId;
    }

    public InvoiceSerial(Long invoiceSerialId, Long invoiceTypeId, String serialNo, String status) {
        this.invoiceSerialId = invoiceSerialId;
        this.invoiceTypeId = invoiceTypeId;
        this.serialNo = serialNo;
        this.status = status;
    }

    public Long getInvoiceSerialId() {
        return invoiceSerialId;
    }

    public void setInvoiceSerialId(Long invoiceSerialId) {
        this.invoiceSerialId = invoiceSerialId;
    }

    public Long getInvoiceTypeId() {
        return invoiceTypeId;
    }

    public void setInvoiceTypeId(Long invoiceTypeId) {
        this.invoiceTypeId = invoiceTypeId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceSerialId != null ? invoiceSerialId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceSerial)) {
            return false;
        }
        InvoiceSerial other = (InvoiceSerial) object;
        if ((this.invoiceSerialId == null && other.invoiceSerialId != null) || (this.invoiceSerialId != null && !this.invoiceSerialId.equals(other.invoiceSerialId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.InvoiceSerial[ invoiceSerialId=" + invoiceSerialId + " ]";
    }
    
}
