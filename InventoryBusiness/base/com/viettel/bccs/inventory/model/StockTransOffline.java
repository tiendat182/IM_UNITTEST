/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author anhvv4
 */
@Entity
@Table(name = "STOCK_TRANS_OFFLINE")
public class StockTransOffline implements Serializable {
    public static enum COLUMNS {CHECKERP, CREATEDATETIME, DEPOSITSTATUS, FROMOWNERID, FROMOWNERTYPE, ISAUTOGEN, NOTE, PAYSTATUS, REASONID, REGIONSTOCKTRANSID, REQUESTID, STATUS, STOCKBASE, STOCKTRANSID, TOOWNERID, TOOWNERTYPE, TOTALAMOUNT, EXCLUSE_ID_LIST, TRANSPORT, REALTRANSUSERID};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_ID")
    @SequenceGenerator(name = "STOCK_TRANS_OFFLINE_ID_GENERATOR", sequenceName = "STOCK_TRANS_OFFLINE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_OFFLINE_ID_GENERATOR")
    private Long stockTransId;
    @Column(name = "FROM_OWNER_ID")
    private Long fromOwnerId;
    @Column(name = "FROM_OWNER_TYPE")
    private Long fromOwnerType;
    @Column(name = "TO_OWNER_ID")
    private Long toOwnerId;
    @Column(name = "TO_OWNER_TYPE")
    private Long toOwnerType;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "TOTAL_AMOUNT")
    private Long totalAmount;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "STOCK_BASE")
    private String stockBase;
    @Column(name = "CHECK_ERP")
    private String checkErp;
    @Column(name = "PAY_STATUS")
    private String payStatus;
    @Column(name = "DEPOSIT_STATUS")
    private String depositStatus;
    @Column(name = "REQUEST_ID")
    private Long requestId;
    @Column(name = "REGION_STOCK_TRANS_ID")
    private Long regionStockTransId;
    @Column(name = "IS_AUTO_GEN")
    private Long isAutoGen;
    @Column(name = "transport")
    private String transport;
    @Column(name = "STOCK_TRANS_TYPE")
    private String stockTransType;
    @Column(name = "IMPORT_REASON_ID")
    private Long importReasonId;
    @Column(name = "IMPORT_NOTE")
    private String importNote;
    @Column(name = "REJECT_REASON_ID")
    private Long rejectReasonId;
    @Column(name = "REJECT_NOTE")
    private String rejectNote;
    @Column(name = "REAL_TRANS_USER_ID")
    private Long realTransUserId;

    public StockTransOffline() {
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Long getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStockBase() {
        return stockBase;
    }

    public void setStockBase(String stockBase) {
        this.stockBase = stockBase;
    }

    public String getCheckErp() {
        return checkErp;
    }

    public void setCheckErp(String checkErp) {
        this.checkErp = checkErp;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getRegionStockTransId() {
        return regionStockTransId;
    }

    public void setRegionStockTransId(Long regionStockTransId) {
        this.regionStockTransId = regionStockTransId;
    }

    public Long getIsAutoGen() {
        return isAutoGen;
    }

    public void setIsAutoGen(Long isAutoGen) {
        this.isAutoGen = isAutoGen;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public Long getImportReasonId() {
        return importReasonId;
    }

    public void setImportReasonId(Long importReasonId) {
        this.importReasonId = importReasonId;
    }

    public String getImportNote() {
        return importNote;
    }

    public void setImportNote(String importNote) {
        this.importNote = importNote;
    }

    public Long getRejectReasonId() {
        return rejectReasonId;
    }

    public void setRejectReasonId(Long rejectReasonId) {
        this.rejectReasonId = rejectReasonId;
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public Long getRealTransUserId() {
        return realTransUserId;
    }

    public void setRealTransUserId(Long realTransUserId) {
        this.realTransUserId = realTransUserId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockTransId != null ? stockTransId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransOffline)) {
            return false;
        }
        StockTransOffline other = (StockTransOffline) object;
        if ((this.stockTransId == null && other.stockTransId != null) || (this.stockTransId != null && !this.stockTransId.equals(other.stockTransId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.StockTrans[ stockTransId=" + stockTransId + " ]";
    }

    public String getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(String stockTransType) {
        this.stockTransType = stockTransType;
    }
}
