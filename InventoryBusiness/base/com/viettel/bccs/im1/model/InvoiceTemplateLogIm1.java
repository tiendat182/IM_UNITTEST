/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.im1.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DungPT16
 */
@Entity
@Table(name = "INVOICE_TEMPLATE_LOG", schema = "BCCS_IM")
@XmlRootElement
public class InvoiceTemplateLogIm1 implements Serializable {
    public static enum COLUMNS {AMOUNTAPPLY, CREATEDATE, CREATEUSER, FROMSHOPID, INVOICETEMPLATEID, INVOICETEMPLATELOGID, LOGTYPE, ORGAMOUNT, SHOPID, STAFFID, VALID, EXCLUSE_ID_LIST}

    ;

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INVOICE_TEMPLATE_LOG_ID")
    @SequenceGenerator(name = "INVOICE_TEMPLATE_LOG1_ID_GENERATOR", sequenceName = "bccs_im.invoice_template_log_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_TEMPLATE_LOG1_ID_GENERATOR")
    private Long invoiceTemplateLogId;
    @Basic(optional = false)
    @Column(name = "INVOICE_TEMPLATE_ID")
    private Long invoiceTemplateId;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "FROM_SHOP_ID")
    private Long fromShopId;
    @Column(name = "ORG_AMOUNT")
    private Long orgAmount;
    @Column(name = "AMOUNT_APPLY")
    private Long amountApply;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LOG_TYPE")
    private Long logType;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "VALID")
    private Long valid;

    public InvoiceTemplateLogIm1() {
    }

    public InvoiceTemplateLogIm1(Long invoiceTemplateLogId) {
        this.invoiceTemplateLogId = invoiceTemplateLogId;
    }

    public InvoiceTemplateLogIm1(Long invoiceTemplateLogId, Long invoiceTemplateId) {
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(Long fromShopId) {
        this.fromShopId = fromShopId;
    }

    public Long getOrgAmount() {
        return orgAmount;
    }

    public void setOrgAmount(Long orgAmount) {
        this.orgAmount = orgAmount;
    }

    public Long getAmountApply() {
        return amountApply;
    }

    public void setAmountApply(Long amountApply) {
        this.amountApply = amountApply;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getLogType() {
        return logType;
    }

    public void setLogType(Long logType) {
        this.logType = logType;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getValid() {
        return valid;
    }

    public void setValid(Long valid) {
        this.valid = valid;
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
        if (!(object instanceof InvoiceTemplateLogIm1)) {
            return false;
        }
        InvoiceTemplateLogIm1 other = (InvoiceTemplateLogIm1) object;
        if ((this.invoiceTemplateLogId == null && other.invoiceTemplateLogId != null) || (this.invoiceTemplateLogId != null && !this.invoiceTemplateLogId.equals(other.invoiceTemplateLogId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.imbccs1.InvoiceTemplateLogIm1[ invoiceTemplateLogId=" + invoiceTemplateLogId + " ]";
    }

}
