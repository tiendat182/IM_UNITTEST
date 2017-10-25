/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * @author DungPT16
 */
@Entity
@Table(name = "STOCK_ORDER_DETAIL")
@NamedQueries({
        @NamedQuery(name = "StockOrderDetail.findAll", query = "SELECT s FROM StockOrderDetail s")})
public class StockOrderDetail implements Serializable {
    public static enum COLUMNS {QUANTITYORDER, QUANTITYREAL, PRODOFFERID, STOCKORDERDETAILID, STOCKORDERID, EXCLUSE_ID_LIST}

    ;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_ORDER_DETAIL_ID")
    @SequenceGenerator(name = "STOCK_ORDER_DETAIL_ID_GENERATOR", sequenceName = "STOCK_ORDER_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_ORDER_DETAIL_ID_GENERATOR")
    private Long stockOrderDetailId;
    @Column(name = "STOCK_ORDER_ID")
    private Long stockOrderId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "QUANTITY_ORDER")
    private Long quantityOrder;
    @Column(name = "QUANTITY_REAL")
    private Long quantityReal;

    public StockOrderDetail() {
    }

    public StockOrderDetail(Long stockOrderDetailId) {
        this.stockOrderDetailId = stockOrderDetailId;
    }

    public Long getStockOrderDetailId() {
        return stockOrderDetailId;
    }

    public void setStockOrderDetailId(Long stockOrderDetailId) {
        this.stockOrderDetailId = stockOrderDetailId;
    }

    public Long getStockOrderId() {
        return stockOrderId;
    }

    public void setStockOrderId(Long stockOrderId) {
        this.stockOrderId = stockOrderId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getQuantityOrder() {
        return quantityOrder;
    }

    public void setQuantityOrder(Long quantityOrder) {
        this.quantityOrder = quantityOrder;
    }

    public Long getQuantityReal() {
        return quantityReal;
    }

    public void setQuantityReal(Long quantityReal) {
        this.quantityReal = quantityReal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockOrderDetailId != null ? stockOrderDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockOrderDetail)) {
            return false;
        }
        StockOrderDetail other = (StockOrderDetail) object;
        if ((this.stockOrderDetailId == null && other.stockOrderDetailId != null) || (this.stockOrderDetailId != null && !this.stockOrderDetailId.equals(other.stockOrderDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockOrderDetail[ stockOrderDetailId=" + stockOrderDetailId + " ]";
    }

}
