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
 * @author tuydv1
 */
@Entity
@Table(name = "STOCK_ORDER_AGENT_DETAIL")
@NamedQueries({
    @NamedQuery(name = "StockOrderAgentDetail.findAll", query = "SELECT s FROM StockOrderAgentDetail s")})
public class StockOrderAgentDetail implements Serializable {
public static enum COLUMNS {CREATEDATE, NOTE, PRODOFFERID, QUANTITY, STATEID, STOCKORDERAGENTDETAILID, STOCKORDERAGENTID, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_ORDER_AGENT_DETAIL_ID")
    @SequenceGenerator(name = "STOCK_ORDER_AGENT_DETAIL_ID_GENERATOR", sequenceName = "STOCK_ORDER_AGENT_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_ORDER_AGENT_DETAIL_ID_GENERATOR")
    private Long stockOrderAgentDetailId;
    @Basic(optional = false)
    @Column(name = "STOCK_ORDER_AGENT_ID")
    private long stockOrderAgentId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "NOTE")
    private String note;

    public StockOrderAgentDetail() {
    }

    public StockOrderAgentDetail(Long stockOrderAgentDetailId) {
        this.stockOrderAgentDetailId = stockOrderAgentDetailId;
    }

    public StockOrderAgentDetail(Long stockOrderAgentDetailId, long stockOrderAgentId) {
        this.stockOrderAgentDetailId = stockOrderAgentDetailId;
        this.stockOrderAgentId = stockOrderAgentId;
    }

    public Long getStockOrderAgentDetailId() {
        return stockOrderAgentDetailId;
    }

    public void setStockOrderAgentDetailId(Long stockOrderAgentDetailId) {
        this.stockOrderAgentDetailId = stockOrderAgentDetailId;
    }

    public long getStockOrderAgentId() {
        return stockOrderAgentId;
    }

    public void setStockOrderAgentId(long stockOrderAgentId) {
        this.stockOrderAgentId = stockOrderAgentId;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockOrderAgentDetailId != null ? stockOrderAgentDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockOrderAgentDetail)) {
            return false;
        }
        StockOrderAgentDetail other = (StockOrderAgentDetail) object;
        if ((this.stockOrderAgentDetailId == null && other.stockOrderAgentDetailId != null) || (this.stockOrderAgentDetailId != null && !this.stockOrderAgentDetailId.equals(other.stockOrderAgentDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockOrderAgentDetail[ stockOrderAgentDetailId=" + stockOrderAgentDetailId + " ]";
    }
    
}
