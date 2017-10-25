/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author DungPT16
 */
@Entity
@Table(name = "STOCK_TRANS_RESCUE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StockTransRescue.findAll", query = "SELECT s FROM StockTransRescue s")})
public class StockTransRescue implements Serializable {
    public static enum COLUMNS {APPROVESTAFFID, CREATEDATE, FROMOWNERID, FROMOWNERTYPE, REASONID, REQUESTCODE, STATUS, STOCKTRANSID, TOOWNERID, TOOWNERTYPE, EXCLUSE_ID_LIST}

    ;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_ID")
    @SequenceGenerator(name = "STOCK_TRANS_ID_RESCUE_GENERATOR", sequenceName = "STOCK_TRANS_RESCUE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_ID_RESCUE_GENERATOR")
    private Long stockTransId;
    @Column(name = "REQUEST_CODE")
    private String requestCode;
    @Column(name = "FROM_OWNER_ID")
    private Long fromOwnerId;
    @Column(name = "FROM_OWNER_TYPE")
    private Long fromOwnerType;
    @Column(name = "TO_OWNER_ID")
    private Long toOwnerId;
    @Column(name = "TO_OWNER_TYPE")
    private Long toOwnerType;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "APPROVE_STAFF_ID")
    private Long approveStaffId;

    public StockTransRescue() {
    }

    public StockTransRescue(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getApproveStaffId() {
        return approveStaffId;
    }

    public void setApproveStaffId(Long approveStaffId) {
        this.approveStaffId = approveStaffId;
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
        if (!(object instanceof StockTransRescue)) {
            return false;
        }
        StockTransRescue other = (StockTransRescue) object;
        if ((this.stockTransId == null && other.stockTransId != null) || (this.stockTransId != null && !this.stockTransId.equals(other.stockTransId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockTransRescue[ stockTransId=" + stockTransId + " ]";
    }

}
