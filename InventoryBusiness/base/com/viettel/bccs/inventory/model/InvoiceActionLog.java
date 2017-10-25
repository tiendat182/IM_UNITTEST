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
@Table(name = "INVOICE_ACTION_LOG")
@NamedQueries({
    @NamedQuery(name = "InvoiceActionLog.findAll", query = "SELECT i FROM InvoiceActionLog i")})
public class InvoiceActionLog implements Serializable {
public static enum COLUMNS {ACTIONTYPE, APPROVEDATE, APPROVEUSER, BLOCKNO, CREATEDATETIME, CREATEUSER, CURINVOICE, DESCRIPTION, FROMINVOICE, FROMSHOPID, FROMSTAFFID, INVOICEACTIONID, INVOICELISTID, SERIALNO, TOINVOICE, TOSHOPID, TOSTAFFID, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INVOICE_ACTION_ID")
    @SequenceGenerator(name = "INVOICE_ACTION_LOG_ID_GENERATOR", sequenceName = "INVOICE_ACTION_LOG_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_ACTION_LOG_ID_GENERATOR")
    private Long invoiceActionId;
//    @Basic(optional = false)
    @Column(name = "INVOICE_LIST_ID")
    private Long invoiceListId;
    @Column(name = "ACTION_TYPE")
    private String actionType;
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "BLOCK_NO")
    private String blockNo;
    @Column(name = "FROM_INVOICE")
    private Long fromInvoice;
    @Column(name = "TO_INVOICE")
    private Long toInvoice;
    @Column(name = "CUR_INVOICE")
    private Long curInvoice;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "FROM_SHOP_ID")
    private Long fromShopId;
    @Column(name = "TO_SHOP_ID")
    private Long toShopId;
    @Column(name = "FROM_STAFF_ID")
    private Long fromStaffId;
    @Column(name = "TO_STAFF_ID")
    private Long toStaffId;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "APPROVE_USER")
    private String approveUser;
    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveDate;

    public InvoiceActionLog() {
    }

    public InvoiceActionLog(Long invoiceActionId) {
        this.invoiceActionId = invoiceActionId;
    }

    public InvoiceActionLog(Long invoiceActionId, Long invoiceListId) {
        this.invoiceActionId = invoiceActionId;
        this.invoiceListId = invoiceListId;
    }

    public Long getInvoiceActionId() {
        return invoiceActionId;
    }

    public void setInvoiceActionId(Long invoiceActionId) {
        this.invoiceActionId = invoiceActionId;
    }

    public Long getInvoiceListId() {
        return invoiceListId;
    }

    public void setInvoiceListId(Long invoiceListId) {
        this.invoiceListId = invoiceListId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public Long getFromInvoice() {
        return fromInvoice;
    }

    public void setFromInvoice(Long fromInvoice) {
        this.fromInvoice = fromInvoice;
    }

    public Long getToInvoice() {
        return toInvoice;
    }

    public void setToInvoice(Long toInvoice) {
        this.toInvoice = toInvoice;
    }

    public Long getCurInvoice() {
        return curInvoice;
    }

    public void setCurInvoice(Long curInvoice) {
        this.curInvoice = curInvoice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(Long fromShopId) {
        this.fromShopId = fromShopId;
    }

    public Long getToShopId() {
        return toShopId;
    }

    public void setToShopId(Long toShopId) {
        this.toShopId = toShopId;
    }

    public Long getFromStaffId() {
        return fromStaffId;
    }

    public void setFromStaffId(Long fromStaffId) {
        this.fromStaffId = fromStaffId;
    }

    public Long getToStaffId() {
        return toStaffId;
    }

    public void setToStaffId(Long toStaffId) {
        this.toStaffId = toStaffId;
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

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceActionId != null ? invoiceActionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceActionLog)) {
            return false;
        }
        InvoiceActionLog other = (InvoiceActionLog) object;
        if ((this.invoiceActionId == null && other.invoiceActionId != null) || (this.invoiceActionId != null && !this.invoiceActionId.equals(other.invoiceActionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.InvoiceActionLog[ invoiceActionId=" + invoiceActionId + " ]";
    }
    
}
