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
@Table(name = "INVOICE_TEMPLATE_LOG")
@NamedQueries({
    @NamedQuery(name = "InvoiceTemplateLog.findAll", query = "SELECT i FROM InvoiceTemplateLog i")})
public class InvoiceTemplateLog implements Serializable {
public static enum COLUMNS {AFTERAMOUNT, CREATEDATETIME, CREATEUSER, FROMSHOPID, INVOICETEMPLATEID, INVOICETEMPLATELOGID, LOGTYPE, ORGAMOUNT, OWNERID, OWNERTYPE, REASONID, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INVOICE_TEMPLATE_LOG_ID")
    @SequenceGenerator(name = "INVOICE_TEMPLATE_LOG_ID_GENERATOR", sequenceName = "INVOICE_TEMPLATE_LOG_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_TEMPLATE_LOG_ID_GENERATOR")
    private Long invoiceTemplateLogId;
    @Basic(optional = false)
    @Column(name = "INVOICE_TEMPLATE_ID")
    private Long invoiceTemplateId;
    @Column(name = "FROM_SHOP_ID")
    private Long fromShopId;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private String ownerType;
    @Column(name = "ORG_AMOUNT")
    private Long orgAmount;
    @Column(name = "AFTER_AMOUNT")
    private String afterAmount;
    @Column(name = "LOG_TYPE")
    private String logType;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;

    public InvoiceTemplateLog() {
    }

    public InvoiceTemplateLog(Long invoiceTemplateLogId) {
        this.invoiceTemplateLogId = invoiceTemplateLogId;
    }

    public InvoiceTemplateLog(Long invoiceTemplateLogId, Long invoiceTemplateId) {
        this.invoiceTemplateLogId = invoiceTemplateLogId;
        this.invoiceTemplateId = invoiceTemplateId;
    }

    public Long getInvoiceTemplateLogId() {
        return invoiceTemplateLogId;
    }

    public void setInvoiceTemplateLogId(Long invoiceTemplateLogId) {
        this.invoiceTemplateLogId = invoiceTemplateLogId;
    }

    public Long getInvoiceTemplateId() {
        return invoiceTemplateId;
    }

    public void setInvoiceTemplateId(Long invoiceTemplateId) {
        this.invoiceTemplateId = invoiceTemplateId;
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

    public Long getOrgAmount() {
        return orgAmount;
    }

    public void setOrgAmount(Long orgAmount) {
        this.orgAmount = orgAmount;
    }

    public String getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(String afterAmount) {
        this.afterAmount = afterAmount;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceTemplateLogId != null ? invoiceTemplateLogId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceTemplateLog)) {
            return false;
        }
        InvoiceTemplateLog other = (InvoiceTemplateLog) object;
        if ((this.invoiceTemplateLogId == null && other.invoiceTemplateLogId != null) || (this.invoiceTemplateLogId != null && !this.invoiceTemplateLogId.equals(other.invoiceTemplateLogId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.InvoiceTemplateLog[ invoiceTemplateLogId=" + invoiceTemplateLogId + " ]";
    }
    
}
