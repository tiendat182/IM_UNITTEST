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
@Table(name = "STOCK_TRANS_DETAIL_RESCUE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StockTransDetailRescue.findAll", query = "SELECT s FROM StockTransDetailRescue s")})
public class StockTransDetailRescue implements Serializable {
    public static enum COLUMNS {CREATEDATE, PRODOFFERID, QUANTITY, STATEID, STOCKTRANSDETAILID, STOCKTRANSID, EXCLUSE_ID_LIST}

    ;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_DETAIL_ID")
    @SequenceGenerator(name = "STOCK_TRANS_DETAIL_RESCUE_ID_GENERATOR", sequenceName = "STOCK_TRANS_DETAIL_RESCUE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_DETAIL_RESCUE_ID_GENERATOR")
    private Long stockTransDetailId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public StockTransDetailRescue() {
    }

    public StockTransDetailRescue(Long stockTransDetailId) {
        this.stockTransDetailId = stockTransDetailId;
    }

    public Long getStockTransDetailId() {
        return stockTransDetailId;
    }

    public void setStockTransDetailId(Long stockTransDetailId) {
        this.stockTransDetailId = stockTransDetailId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        if (!(object instanceof StockTransDetailRescue)) {
            return false;
        }
        StockTransDetailRescue other = (StockTransDetailRescue) object;
        if ((this.stockTransDetailId == null && other.stockTransDetailId != null) || (this.stockTransDetailId != null && !this.stockTransDetailId.equals(other.stockTransDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockTransDetailRescue[ stockTransDetailId=" + stockTransDetailId + " ]";
    }

}
