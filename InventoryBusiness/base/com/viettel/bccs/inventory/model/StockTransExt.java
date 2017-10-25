/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;

/**
 * @author ThanhNT77
 */
@Entity
@Table(name = "STOCK_TRANS_EXT", catalog = "", schema = "BCCS_IM")
@NamedQueries({
        @NamedQuery(name = "StockTransExt.findAll", query = "SELECT s FROM StockTransExt s"),
        @NamedQuery(name = "StockTransExt.findById", query = "SELECT s FROM StockTransExt s WHERE s.id = :id"),
        @NamedQuery(name = "StockTransExt.findByStockTransId", query = "SELECT s FROM StockTransExt s WHERE s.stockTransId = :stockTransId"),
        @NamedQuery(name = "StockTransExt.findByExtKey", query = "SELECT s FROM StockTransExt s WHERE s.extKey = :extKey"),
        @NamedQuery(name = "StockTransExt.findByExtValue", query = "SELECT s FROM StockTransExt s WHERE s.extValue = :extValue")})
public class StockTransExt implements Serializable {
    public static enum COLUMNS {EXTKEY, EXTVALUE, ID, STOCKTRANSID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "STOCK_TRANS_EXT_ID_GENERATOR", sequenceName = "STOCK_TRANS_EXT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_EXT_ID_GENERATOR")
    private Long id;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "EXT_KEY")
    private String extKey;
    @Column(name = "EXT_VALUE")
    private String extValue;
    @Column(name = "STATUS")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StockTransExt() {
    }

    public StockTransExt(Long id) {
        this.id = id;
    }

    public StockTransExt(Long id, Long stockTransId, String extKey, String extValue) {
        this.id = id;
        this.stockTransId = stockTransId;
        this.extKey = extKey;
        this.extValue = extValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getExtKey() {
        return extKey;
    }

    public void setExtKey(String extKey) {
        this.extKey = extKey;
    }

    public String getExtValue() {
        return extValue;
    }

    public void setExtValue(String extValue) {
        this.extValue = extValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransExt)) {
            return false;
        }
        StockTransExt other = (StockTransExt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockTransExt[ id=" + id + " ]";
    }

}
