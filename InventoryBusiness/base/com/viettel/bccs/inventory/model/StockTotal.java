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
 * @author anhvv4
 */
@Entity
@Table(name = "STOCK_TOTAL")
@NamedQueries({
    @NamedQuery(name = "StockTotal.findAll", query = "SELECT s FROM StockTotal s")})
public class StockTotal implements Serializable {
public static enum COLUMNS {AVAILABLEQUANTITY, CURRENTQUANTITY, HANGQUANTITY, MODIFIEDDATE, OWNERID, OWNERTYPE, PRODOFFERID, STATEID, STATUS, STOCKTOTALID, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TOTAL_ID")
    @SequenceGenerator(name = "STOCK_TOTAL_ID_GENERATOR", sequenceName = "STOCK_TOTAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TOTAL_ID_GENERATOR")
    private Long stockTotalId;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Basic(optional = false)
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "CURRENT_QUANTITY")
    private Long currentQuantity;
    @Column(name = "AVAILABLE_QUANTITY")
    private Long availableQuantity;
    @Column(name = "HANG_QUANTITY")
    private Long hangQuantity;
    @Column(name = "MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public StockTotal() {
    }

    public StockTotal(Long stockTotalId) {
        this.stockTotalId = stockTotalId;
    }

    public StockTotal(Long stockTotalId, Long stateId, Long status) {
        this.stockTotalId = stockTotalId;
        this.stateId = stateId;
        this.status = status;
    }

    public Long getStockTotalId() {
        return stockTotalId;
    }

    public void setStockTotalId(Long stockTotalId) {
        this.stockTotalId = stockTotalId;
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

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Long currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Long getHangQuantity() {
        return hangQuantity;
    }

    public void setHangQuantity(Long hangQuantity) {
        this.hangQuantity = hangQuantity;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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
        if (!(object instanceof StockTotal)) {
            return false;
        }
        StockTotal other = (StockTotal) object;
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
