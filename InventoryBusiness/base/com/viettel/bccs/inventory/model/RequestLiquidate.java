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
 * @author tuannm33
 */
@Entity
@Table(name = "REQUEST_LIQUIDATE")
public class RequestLiquidate implements Serializable {
    public static enum COLUMNS {CREATEDATETIME, CREATESHOPID, CREATESTAFFID, REQUESTCODE, REQUESTLIQUIDATEID, STATUS, UPDATEDATETIME, DOCUMENTNAME, DOCUMENTCONTENT, REJECTNOTE,APPROVESTAFFID,CREATEUSER,STOCKTRANSID,EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "REQUEST_LIQUIDATE_ID")
    @SequenceGenerator(name = "REQUEST_LIQUIDATE_ID_GENERATOR", sequenceName = "REQUEST_LIQUIDATE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUEST_LIQUIDATE_ID_GENERATOR")
    private Long requestLiquidateId;
    @Column(name = "CREATE_SHOP_ID")
    private Long createShopId;
    @Column(name = "CREATE_STAFF_ID")
    private Long createStaffId;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "REQUEST_CODE")
    private String requestCode;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;
    @Column(name = "DOCUMENT_NAME")
    private String documentName;
    @Column(name = "DOCUMENT_CONTENT")
    private byte[] documentContent;
    @Column(name = "REJECT_NOTE")
    private String rejectNote;
    @Column(name = "APPROVE_STAFF_ID")
    private Long approveStaffId;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;

    public RequestLiquidate() {
    }

    public RequestLiquidate(Long requestLiquidateId) {
        this.requestLiquidateId = requestLiquidateId;
    }

    public Long getRequestLiquidateId() {
        return requestLiquidateId;
    }

    public void setRequestLiquidateId(Long requestLiquidateId) {
        this.requestLiquidateId = requestLiquidateId;
    }

    public Long getCreateShopId() {
        return createShopId;
    }

    public void setCreateShopId(Long createShopId) {
        this.createShopId = createShopId;
    }

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public byte[] getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(byte[] documentContent) {
        this.documentContent = documentContent;
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public Long getApproveStaffId() {
        return approveStaffId;
    }

    public void setApproveStaffId(Long approveStaffId) {
        this.approveStaffId = approveStaffId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requestLiquidateId != null ? requestLiquidateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestLiquidate)) {
            return false;
        }
        RequestLiquidate other = (RequestLiquidate) object;
        if ((this.requestLiquidateId == null && other.requestLiquidateId != null) || (this.requestLiquidateId != null && !this.requestLiquidateId.equals(other.requestLiquidateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.RequestLiquidate[ requestLiquidateId=" + requestLiquidateId + " ]";
    }

}
