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
@Table(name = "INVOICE_TEMPLATE", schema = "BCCS_IM")
@XmlRootElement
public class InvoiceTemplateIm1 implements Serializable {
    public static enum COLUMNS {AMOUNT, BOOKTYPEID, CREATEDATE, CREATEUSER, DESCRIPTION, INVOICETEMPLATEID, LASTUPDATEDATE, LASTUPDATEUSER, PREAMOUNT, SERIALNO, SHOPID, STAFFID, TYPE, USEDAMOUNT, EXCLUSE_ID_LIST}

    ;

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INVOICE_TEMPLATE_ID")
    private Long invoiceTemplateId;
    @Basic(optional = false)
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "AMOUNT")
    private Long amount;
    @Column(name = "BOOK_TYPE_ID")
    private Long bookTypeId;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;
    @Column(name = "LAST_UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;
    @Column(name = "USED_AMOUNT")
    private Long usedAmount;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PRE_AMOUNT")
    private Long preAmount;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "TYPE")
    private Long type;

    public InvoiceTemplateIm1() {
    }

    public InvoiceTemplateIm1(Long invoiceTemplateId) {
        this.invoiceTemplateId = invoiceTemplateId;
    }

    public InvoiceTemplateIm1(Long invoiceTemplateId, Long shopId) {
        this.invoiceTemplateId = invoiceTemplateId;
        this.shopId = shopId;
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBookTypeId() {
        return bookTypeId;
    }

    public void setBookTypeId(Long bookTypeId) {
        this.bookTypeId = bookTypeId;
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

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
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

    public Long getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(Long preAmount) {
        this.preAmount = preAmount;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
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
        if (!(object instanceof InvoiceTemplateIm1)) {
            return false;
        }
        InvoiceTemplateIm1 other = (InvoiceTemplateIm1) object;
        if ((this.invoiceTemplateId == null && other.invoiceTemplateId != null) || (this.invoiceTemplateId != null && !this.invoiceTemplateId.equals(other.invoiceTemplateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.imbccs1.InvoiceTemplateIm1[ invoiceTemplateId=" + invoiceTemplateId + " ]";
    }

}
