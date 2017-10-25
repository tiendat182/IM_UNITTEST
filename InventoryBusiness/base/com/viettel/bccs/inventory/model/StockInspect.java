/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author DungPT16
 */
@Entity
@Table(name = "STOCK_INSPECT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockInspect.findAll", query = "SELECT s FROM StockInspect s")})
public class StockInspect implements Serializable {
public static enum COLUMNS {APPROVEDATE, APPROVENOTE, APPROVESTATUS, APPROVEUSER, APPROVEUSERID, APPROVEUSERNAME, CREATEDATE, CREATEUSER, CREATEUSERID, CREATEUSERNAME, ISFINISHED, ISFINISHEDALL, ISSCAN, OWNERID, OWNERTYPE, PRODOFFERID, PRODOFFERTYPEID, QUANTITYREAL, QUANTITYSYS, STATEID, STOCKINSPECTID, UPDATEDATE, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_INSPECT_ID")
//    @SequenceGenerator(name = "STOCK_INSPECT_ID_GENERATOR", sequenceName = "STOCK_INSPECT_SEQ", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_INSPECT_ID_GENERATOR")
    private Long stockInspectId;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "PROD_OFFER_TYPE_ID")
    private Long prodOfferTypeId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "QUANTITY_SYS")
    private Long quantitySys;
    @Column(name = "QUANTITY_REAL")
    private Long quantityReal;
    @Column(name = "QUANTITY_POOR")
    private Long quantityPoor;
    @Column(name = "QUANTITY_FINANCE")
    private Long quantityFinance;
    @Column(name = "IS_FINISHED")
    private Long isFinished;
    @Column(name = "APPROVE_STATUS")
    private Long approveStatus;
    @Column(name = "CREATE_USER_ID")
    private Long createUserId;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_USER_NAME")
    private String createUserName;
    @Column(name = "APPROVE_USER_ID")
    private Long approveUserId;
    @Column(name = "APPROVE_USER")
    private String approveUser;
    @Column(name = "APPROVE_USER_NAME")
    private String approveUserName;
    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveDate;
    @Column(name = "APPROVE_NOTE")
    private String approveNote;
    @Column(name = "IS_FINISHED_ALL")
    private Long isFinishedAll;
    @Column(name = "IS_SCAN")
    private Long isScan;
    @Column(name = "NOTE")
    private String note;

    public StockInspect() {
    }

    public StockInspect(Long stockInspectId) {
        this.stockInspectId = stockInspectId;
    }

    public Long getStockInspectId() {
        return stockInspectId;
    }

    public void setStockInspectId(Long stockInspectId) {
        this.stockInspectId = stockInspectId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getProdOfferTypeId() {
        return prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getQuantitySys() {
        return quantitySys;
    }

    public void setQuantitySys(Long quantitySys) {
        this.quantitySys = quantitySys;
    }

    public Long getQuantityReal() {
        return quantityReal;
    }

    public void setQuantityReal(Long quantityReal) {
        this.quantityReal = quantityReal;
    }

    public Long getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Long isFinished) {
        this.isFinished = isFinished;
    }

    public Long getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Long approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public String getApproveUserName() {
        return approveUserName;
    }

    public void setApproveUserName(String approveUserName) {
        this.approveUserName = approveUserName;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getApproveNote() {
        return approveNote;
    }

    public void setApproveNote(String approveNote) {
        this.approveNote = approveNote;
    }

    public Long getIsFinishedAll() {
        return isFinishedAll;
    }

    public void setIsFinishedAll(Long isFinishedAll) {
        this.isFinishedAll = isFinishedAll;
    }

    public Long getIsScan() {
        return isScan;
    }

    public void setIsScan(Long isScan) {
        this.isScan = isScan;
    }

    public Long getQuantityPoor() {
        return quantityPoor;
    }

    public void setQuantityPoor(Long quantityPoor) {
        this.quantityPoor = quantityPoor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getQuantityFinance() {
        return quantityFinance;
    }

    public void setQuantityFinance(Long quantityFinance) {
        this.quantityFinance = quantityFinance;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockInspectId != null ? stockInspectId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockInspect)) {
            return false;
        }
        StockInspect other = (StockInspect) object;
        if ((this.stockInspectId == null && other.stockInspectId != null) || (this.stockInspectId != null && !this.stockInspectId.equals(other.stockInspectId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockInspect[ stockInspectId=" + stockInspectId + " ]";
    }
    
}
