/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.im1.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author anhvv4
 */
@Entity
@Table(name = "STOCK_TOTAL",schema = "BCCS_IM")
@NamedQueries({
    @NamedQuery(name = "StockTotalIm1.findAll", query = "SELECT s FROM StockTotal s")})
public class StockTotalIm1 implements Serializable {
public static enum COLUMNS {OWNERID, OWNERTYPE, STOCKMODELID, STATEID, QUANTITY, QUANTITYISSUE, MODIFIEDDATE, STATUS,
    QUANTITYDIAL, MAXDEBIT, CURRENTDEBIT, DATERESET, LIMITQUANTITY, QUANTITYHANG, EXCLUSEIDLIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TOTAL_ID")
    @SequenceGenerator(name = "STOCK_TOTAL_ID_GENERATOR", sequenceName = "STOCK_TOTAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TOTAL_ID_GENERATOR")
    private Long stockTotalId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "QUANTITY_ISSUE")
    private Long quantityIssue;
    @Column(name = "MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @Column(name = "STATE_ID")
    private Short stateId;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "QUANTITY_DIAL")
    private Long quantityDial;
    @Column(name = "MAX_DEBIT")
    private Long maxDebit;
    @Column(name = "CURRENT_DEBIT")
    private Long currentDebit;
    @Column(name = "DATE_RESET")
    private Long dateReset;
    @Column(name = "LIMIT_QUANTITY")
    private Integer limitQuantity;
    @Column(name = "QUANTITY_HANG")
    private Long quantityHang;
    @JoinColumn(name = "STOCK_MODEL_ID")
    private Long stockModelId;

    public StockTotalIm1() {
    }

    public StockTotalIm1(Long stockTotalId) {
        this.stockTotalId = stockTotalId;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Short getStateId() {
        return stateId;
    }

    public void setStateId(Short stateId) {
        this.stateId = stateId;
    }

    public Long getStockTotalId() {
        return stockTotalId;
    }

    public void setStockTotalId(Long stockTotalId) {
        this.stockTotalId = stockTotalId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantityIssue() {
        return quantityIssue;
    }

    public void setQuantityIssue(Long quantityIssue) {
        this.quantityIssue = quantityIssue;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Long getQuantityDial() {
        return quantityDial;
    }

    public void setQuantityDial(Long quantityDial) {
        this.quantityDial = quantityDial;
    }

    public Long getMaxDebit() {
        return maxDebit;
    }

    public void setMaxDebit(Long maxDebit) {
        this.maxDebit = maxDebit;
    }

    public Long getCurrentDebit() {
        return currentDebit;
    }

    public void setCurrentDebit(Long currentDebit) {
        this.currentDebit = currentDebit;
    }

    public Long getDateReset() {
        return dateReset;
    }

    public void setDateReset(Long dateReset) {
        this.dateReset = dateReset;
    }

    public Integer getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(Integer limitQuantity) {
        this.limitQuantity = limitQuantity;
    }

    public Long getQuantityHang() {
        return quantityHang;
    }

    public void setQuantityHang(Long quantityHang) {
        this.quantityHang = quantityHang;
    }

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockTotalId != null ? stockTotalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTotalIm1)) {
            return false;
        }
        StockTotalIm1 other = (StockTotalIm1) object;
        if ((this.stockTotalId == null && other.stockTotalId != null) || (this.stockTotalId != null && !this.stockTotalId.equals(other.stockTotalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.StockTotal[ stockTotalId=" + stockTotalId + " ]";
    }
    
}
