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
 * @author sinhhv
 */
@Entity
@Table(name = "STOCK_ISDN_TRANS_DETAIL")
public class StockIsdnTransDetail implements Serializable {
public static enum COLUMNS {CREATEDTIME, FROMISDN, LENGTHISDN, QUANTITY, STOCKISDNTRANSDETAILID, STOCKISDNTRANSID, TOISDN, TYPEISDN, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_ISDN_TRANS_DETAIL_ID")
    @SequenceGenerator(name = "STOCK_ISDN_TRANS_DETAIL_ID_GENERATOR", sequenceName = "STOCK_ISDN_TRANS_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_ISDN_TRANS_DETAIL_ID_GENERATOR")
    private Long stockIsdnTransDetailId;
    @Column(name = "STOCK_ISDN_TRANS_ID")
    private Long stockIsdnTransId;
    @Column(name = "FROM_ISDN")
    private String fromIsdn;
    @Column(name = "TO_ISDN")
    private String toIsdn;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Column(name = "LENGTH_ISDN")
    private Long lengthIsdn;
    @Column(name = "TYPE_ISDN")
    private Long typeIsdn;

    public StockIsdnTransDetail() {
    }

    public StockIsdnTransDetail(Long stockIsdnTransDetailId) {
        this.stockIsdnTransDetailId = stockIsdnTransDetailId;
    }

    public Long getStockIsdnTransDetailId() {
        return stockIsdnTransDetailId;
    }

    public void setStockIsdnTransDetailId(Long stockIsdnTransDetailId) {
        this.stockIsdnTransDetailId = stockIsdnTransDetailId;
    }

    public String getFromIsdn() {
        return fromIsdn;
    }

    public void setFromIsdn(String fromIsdn) {
        this.fromIsdn = fromIsdn;
    }

    public String getToIsdn() {
        return toIsdn;
    }

    public void setToIsdn(String toIsdn) {
        this.toIsdn = toIsdn;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getLengthIsdn() {
        return lengthIsdn;
    }

    public void setLengthIsdn(Long lengthIsdn) {
        this.lengthIsdn = lengthIsdn;
    }

    public Long getTypeIsdn() {
        return typeIsdn;
    }

    public void setTypeIsdn(Long typeIsdn) {
        this.typeIsdn = typeIsdn;
    }

    public Long getStockIsdnTransId() {
        return stockIsdnTransId;
    }

    public void setStockIsdnTransId(Long stockIsdnTransId) {
        this.stockIsdnTransId = stockIsdnTransId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockIsdnTransDetailId != null ? stockIsdnTransDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockIsdnTransDetail)) {
            return false;
        }
        StockIsdnTransDetail other = (StockIsdnTransDetail) object;
        if ((this.stockIsdnTransDetailId == null && other.stockIsdnTransDetailId != null) || (this.stockIsdnTransDetailId != null && !this.stockIsdnTransDetailId.equals(other.stockIsdnTransDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.StockIsdnTransDetail[ stockIsdnTransDetailId=" + stockIsdnTransDetailId + " ]";
    }

}
