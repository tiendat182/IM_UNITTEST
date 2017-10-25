/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Dungha7
 */
@Entity
@Table(name = "STOCK_REQUEST_ORDER_DETAIL")
public class StockRequestOrderDetail implements Serializable {
    public static enum COLUMNS {APPROVEDQUANTITY, FROMOWNERID, FROMOWNERTYPE, FROMSTOCKTRANSID, NOTE, PRODOFFERID, REQUESTQUANTITY, STATEID, STATUS, STOCKREQUESTORDERDETAILID, STOCKREQUESTORDERID, STOCKTRANSID, TOOWNERID, TOOWNERTYPE,EXPORTTRANSID, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_REQUEST_ORDER_DETAIL_ID")
    @SequenceGenerator(name = "STOCK_REQUEST_ORDER_DETAIL_ID_GENERATOR", sequenceName = "STOCK_REQUEST_ORDER_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_REQUEST_ORDER_DETAIL_ID_GENERATOR")
    private Long stockRequestOrderDetailId;
    @Column(name = "STOCK_REQUEST_ORDER_ID")
    private Long stockRequestOrderId;
    @Column(name = "FROM_OWNER_ID")
    private Long fromOwnerId;
    @Column(name = "FROM_OWNER_TYPE")
    private Long fromOwnerType;
    @Column(name = "TO_OWNER_ID")
    private Long toOwnerId;
    @Column(name = "TO_OWNER_TYPE")
    private Long toOwnerType;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "LAST_UPDATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatetime;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "REQUEST_QUANTITY")
    private Long requestQuantity;
    @Column(name = "APPROVED_QUANTITY")
    private Long approvedQuantity;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "EXPORT_TRANS_ID")
    private Long exportTransId;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "IMPORT_TRANS_ID")
    private Long importTransId;

    public StockRequestOrderDetail() {
    }

    public StockRequestOrderDetail(Long stockRequestOrderDetailId) {
        this.stockRequestOrderDetailId = stockRequestOrderDetailId;
    }

    public StockRequestOrderDetail(Long stockRequestOrderDetailId, Long stockRequestOrderId) {
        this.stockRequestOrderDetailId = stockRequestOrderDetailId;
        this.stockRequestOrderId = stockRequestOrderId;
    }

    public Long getStockRequestOrderDetailId() {
        return stockRequestOrderDetailId;
    }

    public void setStockRequestOrderDetailId(Long stockRequestOrderDetailId) {
        this.stockRequestOrderDetailId = stockRequestOrderDetailId;
    }

    public Long getStockRequestOrderId() {
        return stockRequestOrderId;
    }

    public void setStockRequestOrderId(Long stockRequestOrderId) {
        this.stockRequestOrderId = stockRequestOrderId;
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

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getLastUpdatetime() {
        return lastUpdatetime;
    }

    public void setLastUpdatetime(Date lastUpdatetime) {
        this.lastUpdatetime = lastUpdatetime;
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

    public Long getRequestQuantity() {
        return requestQuantity;
    }

    public void setRequestQuantity(Long requestQuantity) {
        this.requestQuantity = requestQuantity;
    }

    public Long getApprovedQuantity() {
        return approvedQuantity;
    }

    public void setApprovedQuantity(Long approvedQuantity) {
        this.approvedQuantity = approvedQuantity;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getExportTransId() {
        return exportTransId;
    }

    public void setExportTransId(Long exportTransId) {
        this.exportTransId = exportTransId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getImportTransId() {
        return importTransId;
    }

    public void setImportTransId(Long importTransId) {
        this.importTransId = importTransId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockRequestOrderDetailId != null ? stockRequestOrderDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockRequestOrderDetail)) {
            return false;
        }
        StockRequestOrderDetail other = (StockRequestOrderDetail) object;
        if ((this.stockRequestOrderDetailId == null && other.stockRequestOrderDetailId != null) || (this.stockRequestOrderDetailId != null && !this.stockRequestOrderDetailId.equals(other.stockRequestOrderDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.StockRequestOrderDetail[ stockRequestOrderDetailId=" + stockRequestOrderDetailId + " ]";
    }
    
}
