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
@Table(name = "INVOICE_LIST")
@NamedQueries({
    @NamedQuery(name = "InvoiceList.findAll", query = "SELECT i FROM InvoiceList i")})
public class InvoiceList implements Serializable {
public static enum COLUMNS {UPDATEDATETIME, UPDATEUSER, APPROVEDATETIME, APPROVEUSER, BLOCKNO, CREATEDATETIME, CREATEUSER, CURRINVOICE, DESTROYDATE, DESTROYREASONID, DESTROYUSER, FROMINVOICE, INVOICELISTID, INVOICESERIALID, SERIALNO, SHOPID, STAFFID, STATUS, TOINVOICE, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INVOICE_LIST_ID")
    @SequenceGenerator(name = "INVOICE_LIST_ID_GENERATOR", sequenceName = "INVOICE_LIST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INVOICE_LIST_ID_GENERATOR")
    private Long invoiceListId;
    @Basic(optional = false)
    @Column(name = "INVOICE_SERIAL_ID")
    private Long invoiceSerialId;
    @Basic(optional = false)
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "BLOCK_NO")
    private String blockNo;
    @Basic(optional = false)
    @Column(name = "FROM_INVOICE")
    private Long fromInvoice;
    @Basic(optional = false)
    @Column(name = "TO_INVOICE")
    private Long toInvoice;
    @Basic(optional = false)
    @Column(name = "CURR_INVOICE")
    private Long currInvoice;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
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
    @Column(name = "APPROVE_USER")
    private String approveUser;
    @Column(name = "APPROVE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveDatetime;
    @Column(name = "DESTROY_USER")
    private String destroyUser;
    @Column(name = "DESTROY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date destroyDate;
    @Column(name = "DESTROY_REASON_ID")
    private String destroyReasonId;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;

    public InvoiceList() {
    }

    public InvoiceList(Long invoiceListId) {
        this.invoiceListId = invoiceListId;
    }

    public InvoiceList(Long invoiceListId, Long invoiceSerialId, String serialNo, String blockNo, Long fromInvoice, Long toInvoice, Long currInvoice, String status, String createUser, Date createDatetime) {
        this.invoiceListId = invoiceListId;
        this.invoiceSerialId = invoiceSerialId;
        this.serialNo = serialNo;
        this.blockNo = blockNo;
        this.fromInvoice = fromInvoice;
        this.toInvoice = toInvoice;
        this.currInvoice = currInvoice;
        this.status = status;
        this.createUser = createUser;
        this.createDatetime = createDatetime;
    }

    public Long getInvoiceListId() {
        return invoiceListId;
    }

    public void setInvoiceListId(Long invoiceListId) {
        this.invoiceListId = invoiceListId;
    }

    public Long getInvoiceSerialId() {
        return invoiceSerialId;
    }

    public void setInvoiceSerialId(Long invoiceSerialId) {
        this.invoiceSerialId = invoiceSerialId;
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

    public Long getCurrInvoice() {
        return currInvoice;
    }

    public void setCurrInvoice(Long currInvoice) {
        this.currInvoice = currInvoice;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public Date getApproveDatetime() {
        return approveDatetime;
    }

    public void setApproveDatetime(Date approveDatetime) {
        this.approveDatetime = approveDatetime;
    }

    public String getDestroyUser() {
        return destroyUser;
    }

    public void setDestroyUser(String destroyUser) {
        this.destroyUser = destroyUser;
    }

    public Date getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(Date destroyDate) {
        this.destroyDate = destroyDate;
    }

    public String getDestroyReasonId() {
        return destroyReasonId;
    }

    public void setDestroyReasonId(String destroyReasonId) {
        this.destroyReasonId = destroyReasonId;
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
        hash += (invoiceListId != null ? invoiceListId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceList)) {
            return false;
        }
        InvoiceList other = (InvoiceList) object;
        if ((this.invoiceListId == null && other.invoiceListId != null) || (this.invoiceListId != null && !this.invoiceListId.equals(other.invoiceListId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.InvoiceList[ invoiceListId=" + invoiceListId + " ]";
    }
    
}
