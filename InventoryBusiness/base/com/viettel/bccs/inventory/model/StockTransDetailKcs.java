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
 *
 * @author ThanhNT77
 */
@Entity
@Table(name = "STOCK_TRANS_DETAIL_KCS")
@XmlRootElement
public class StockTransDetailKcs implements Serializable {
public static enum COLUMNS {CREATEDATETIME, QUANTITY, STOCKTRANSDETAILKCSID, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_DETAIL_KCS_ID")
    @SequenceGenerator(name = "STOCK_TRANS_DETAIL_KCS_ID_GENERATOR", sequenceName = "STOCK_TRANS_DETAIL_KCS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_DETAIL_KCS_ID_GENERATOR")
    private Long stockTransDetailKcsId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "STOCK_TRANS_DETAIL_ID")
    private Long stockTransDetailId;
    @Column(name = "STATE_ID")
    private Long stateId;

    public StockTransDetailKcs() {
    }

    public StockTransDetailKcs(Long stockTransDetailKcsId) {
        this.stockTransDetailKcsId = stockTransDetailKcsId;
    }

    public Long getStockTransDetailKcsId() {
        return stockTransDetailKcsId;
    }

    public void setStockTransDetailKcsId(Long stockTransDetailKcsId) {
        this.stockTransDetailKcsId = stockTransDetailKcsId;
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

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockTransDetailKcsId != null ? stockTransDetailKcsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransDetailKcs)) {
            return false;
        }
        StockTransDetailKcs other = (StockTransDetailKcs) object;
        if ((this.stockTransDetailKcsId == null && other.stockTransDetailKcsId != null) || (this.stockTransDetailKcsId != null && !this.stockTransDetailKcsId.equals(other.stockTransDetailKcsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockTransDetailKcs[ stockTransDetailKcsId=" + stockTransDetailKcsId + " ]";
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getStockTransDetailId() {
        return stockTransDetailId;
    }

    public void setStockTransDetailId(Long stockTransDetailId) {
        this.stockTransDetailId = stockTransDetailId;
    }
}
