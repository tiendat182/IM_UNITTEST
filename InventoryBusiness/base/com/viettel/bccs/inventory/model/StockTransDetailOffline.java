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
 * @author anhvv4
 */
@Entity
@Table(name = "STOCK_TRANS_DETAIL_OFFLINE")
public class StockTransDetailOffline implements Serializable {
    public static enum COLUMNS {AMOUNT, CREATEDATETIME, PRICE, PRODOFFERID, QUANTITY, STATEID, STOCKTRANSDETAILID, STOCKTRANSID, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_DETAIL_ID")
    @SequenceGenerator(name = "STOCK_TRANS_DETAIL_OFFLINE_ID_GENERATOR", sequenceName = "STOCK_TRANS_SERIAL_OFFLINE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_DETAIL_OFFLINE_ID_GENERATOR")
    private Long stockTransDetailId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "PRICE")
    private Long price;
    @Column(name = "AMOUNT")
    private Long amount;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public StockTransDetailOffline() {
    }

    public StockTransDetailOffline(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getStockTransDetailId() {
        return stockTransDetailId;
    }

    public void setStockTransDetailId(Long stockTransDetailId) {
        this.stockTransDetailId = stockTransDetailId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
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
        hash += (stockTransDetailId != null ? stockTransDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransDetailOffline)) {
            return false;
        }
        StockTransDetailOffline other = (StockTransDetailOffline) object;
        if ((this.stockTransDetailId == null && other.stockTransDetailId != null) || (this.stockTransDetailId != null && !this.stockTransDetailId.equals(other.stockTransDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.StockTransDetail[ stockTransDetailId=" + stockTransDetailId + " ]";
    }

}
