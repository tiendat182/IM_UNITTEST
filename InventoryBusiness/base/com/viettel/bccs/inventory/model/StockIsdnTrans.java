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
 * @author sinhhv
 */
@Entity
@Table(name = "STOCK_ISDN_TRANS")
public class StockIsdnTrans implements Serializable {
public static enum COLUMNS {CREATEDTIME, CREATEDUSERCODE, CREATEDUSERID, CREATEDUSERIP, CREATEDUSERNAME, FROMOWNERCODE, FROMOWNERID, FROMOWNERNAME, FROMOWNERTYPE, LASTUPDATEDTIME, LASTUPDATEDUSERCODE, LASTUPDATEDUSERID, LASTUPDATEDUSERIP, LASTUPDATEDUSERNAME, NOTE, QUANTITY, REASONCODE, REASONID, REASONNAME, STATUS, STOCKISDNTRANSCODE, STOCKISDNTRANSID, STOCKTYPEID, STOCKTYPENAME, TOOWNERCODE, TOOWNERID, TOOWNERNAME, TOOWNERTYPE, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_ISDN_TRANS_ID")
    @SequenceGenerator(name = "STOCK_ISDN_TRANS_ID_GENERATOR", sequenceName = "STOCK_ISDN_TRANS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_ISDN_TRANS_ID_GENERATOR")
    private Long stockIsdnTransId;
    @Column(name = "STOCK_ISDN_TRANS_CODE")
    private String stockIsdnTransCode;
    @Column(name = "FROM_OWNER_TYPE")
    private Long fromOwnerType;
    @Column(name = "FROM_OWNER_ID")
    private Long fromOwnerId;
    @Column(name = "FROM_OWNER_CODE")
    private String fromOwnerCode;
    @Column(name = "FROM_OWNER_NAME")
    private String fromOwnerName;
    @Column(name = "TO_OWNER_TYPE")
    private Long toOwnerType;
    @Column(name = "TO_OWNER_ID")
    private Long toOwnerId;
    @Column(name = "TO_OWNER_CODE")
    private String toOwnerCode;
    @Column(name = "TO_OWNER_NAME")
    private String toOwnerName;
    @Column(name = "STOCK_TYPE_ID")
    private Long stockTypeId;
    @Column(name = "STOCK_TYPE_NAME")
    private String stockTypeName;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Column(name = "REASON_CODE")
    private String reasonCode;
    @Column(name = "REASON_NAME")
    private String reasonName;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "CREATED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Column(name = "CREATED_USER_ID")
    private Long createdUserId;
    @Column(name = "CREATED_USER_CODE")
    private String createdUserCode;
    @Column(name = "CREATED_USER_NAME")
    private String createdUserName;
    @Column(name = "CREATED_USER_IP")
    private String createdUserIp;
    @Column(name = "LAST_UPDATED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;
    @Column(name = "LAST_UPDATED_USER_ID")
    private Long lastUpdatedUserId;
    @Column(name = "LAST_UPDATED_USER_CODE")
    private String lastUpdatedUserCode;
    @Column(name = "LAST_UPDATED_USER_NAME")
    private String lastUpdatedUserName;
    @Column(name = "LAST_UPDATED_USER_IP")
    private String lastUpdatedUserIp;

    public StockIsdnTrans() {
    }

    public StockIsdnTrans(Long stockIsdnTransId) {
        this.stockIsdnTransId = stockIsdnTransId;
    }

    public Long getStockIsdnTransId() {
        return stockIsdnTransId;
    }

    public void setStockIsdnTransId(Long stockIsdnTransId) {
        this.stockIsdnTransId = stockIsdnTransId;
    }

    public String getStockIsdnTransCode() {
        return stockIsdnTransCode;
    }

    public void setStockIsdnTransCode(String stockIsdnTransCode) {
        this.stockIsdnTransCode = stockIsdnTransCode;
    }

    public Long getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public String getFromOwnerCode() {
        return fromOwnerCode;
    }

    public void setFromOwnerCode(String fromOwnerCode) {
        this.fromOwnerCode = fromOwnerCode;
    }

    public String getFromOwnerName() {
        return fromOwnerName;
    }

    public void setFromOwnerName(String fromOwnerName) {
        this.fromOwnerName = fromOwnerName;
    }

    public Long getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public String getToOwnerCode() {
        return toOwnerCode;
    }

    public void setToOwnerCode(String toOwnerCode) {
        this.toOwnerCode = toOwnerCode;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public Long getStockTypeId() {
        return stockTypeId;
    }

    public void setStockTypeId(Long stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public String getStockTypeName() {
        return stockTypeName;
    }

    public void setStockTypeName(String stockTypeName) {
        this.stockTypeName = stockTypeName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserCode() {
        return createdUserCode;
    }

    public void setCreatedUserCode(String createdUserCode) {
        this.createdUserCode = createdUserCode;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public String getCreatedUserIp() {
        return createdUserIp;
    }

    public void setCreatedUserIp(String createdUserIp) {
        this.createdUserIp = createdUserIp;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Long getLastUpdatedUserId() {
        return lastUpdatedUserId;
    }

    public void setLastUpdatedUserId(Long lastUpdatedUserId) {
        this.lastUpdatedUserId = lastUpdatedUserId;
    }

    public String getLastUpdatedUserCode() {
        return lastUpdatedUserCode;
    }

    public void setLastUpdatedUserCode(String lastUpdatedUserCode) {
        this.lastUpdatedUserCode = lastUpdatedUserCode;
    }

    public String getLastUpdatedUserName() {
        return lastUpdatedUserName;
    }

    public void setLastUpdatedUserName(String lastUpdatedUserName) {
        this.lastUpdatedUserName = lastUpdatedUserName;
    }

    public String getLastUpdatedUserIp() {
        return lastUpdatedUserIp;
    }

    public void setLastUpdatedUserIp(String lastUpdatedUserIp) {
        this.lastUpdatedUserIp = lastUpdatedUserIp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockIsdnTransId != null ? stockIsdnTransId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockIsdnTrans)) {
            return false;
        }
        StockIsdnTrans other = (StockIsdnTrans) object;
        if ((this.stockIsdnTransId == null && other.stockIsdnTransId != null) || (this.stockIsdnTransId != null && !this.stockIsdnTransId.equals(other.stockIsdnTransId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.StockIsdnTrans[ stockIsdnTransId=" + stockIsdnTransId + " ]";
    }
    
}
