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
 * @author DungPT16
 */
@Entity
@Table(name = "STOCK_BALANCE_DETAIL")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StockBalanceDetail.findAll", query = "SELECT s FROM StockBalanceDetail s")})
public class StockBalanceDetail implements Serializable {
    public static enum COLUMNS {CREATEDATETIME, PRODOFFERID, QUANTITY, QUANTITYBCCS, QUANTITYERP, QUANTITYREAL, STOCKBALANCEDETAILID, STOCKREQUESTID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_BALANCE_DETAIL_ID")
    @SequenceGenerator(name = "STOCK_BALANCE_DETAIL_ID_GENERATOR", sequenceName = "STOCK_BALANCE_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_BALANCE_DETAIL_ID_GENERATOR")
    private Long stockBalanceDetailId;
    @Basic(optional = false)
    @Column(name = "STOCK_REQUEST_ID")
    private Long stockRequestId;
    @Basic(optional = false)
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "QUANTITY_BCCS")
    private Long quantityBccs;
    @Column(name = "QUANTITY_REAL")
    private Long quantityReal;
    @Column(name = "QUANTITY_ERP")
    private Long quantityErp;
    @Basic(optional = false)
    @Column(name = "QUANTITY")
    private Long quantity;
    @Basic(optional = false)
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;

    public StockBalanceDetail() {
    }

    public StockBalanceDetail(Long stockBalanceDetailId) {
        this.stockBalanceDetailId = stockBalanceDetailId;
    }

    public StockBalanceDetail(Long stockBalanceDetailId, Long stockRequestId, Long prodOfferId, Long quantity, Date createDatetime) {
        this.stockBalanceDetailId = stockBalanceDetailId;
        this.stockRequestId = stockRequestId;
        this.prodOfferId = prodOfferId;
        this.quantity = quantity;
        this.createDatetime = createDatetime;
    }

    public Long getStockBalanceDetailId() {
        return stockBalanceDetailId;
    }

    public void setStockBalanceDetailId(Long stockBalanceDetailId) {
        this.stockBalanceDetailId = stockBalanceDetailId;
    }

    public Long getStockRequestId() {
        return stockRequestId;
    }

    public void setStockRequestId(Long stockRequestId) {
        this.stockRequestId = stockRequestId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getQuantityBccs() {
        return quantityBccs;
    }

    public void setQuantityBccs(Long quantityBccs) {
        this.quantityBccs = quantityBccs;
    }

    public Long getQuantityReal() {
        return quantityReal;
    }

    public void setQuantityReal(Long quantityReal) {
        this.quantityReal = quantityReal;
    }

    public Long getQuantityErp() {
        return quantityErp;
    }

    public void setQuantityErp(Long quantityErp) {
        this.quantityErp = quantityErp;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockBalanceDetailId != null ? stockBalanceDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockBalanceDetail)) {
            return false;
        }
        StockBalanceDetail other = (StockBalanceDetail) object;
        if ((this.stockBalanceDetailId == null && other.stockBalanceDetailId != null) || (this.stockBalanceDetailId != null && !this.stockBalanceDetailId.equals(other.stockBalanceDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockBalanceDetail[ stockBalanceDetailId=" + stockBalanceDetailId + " ]";
    }

}
