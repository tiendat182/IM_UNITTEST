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
@Table(name = "STOCK_TRANS_SERIAL")
@NamedQueries({
    @NamedQuery(name = "StockTransSerial.findAll", query = "SELECT s FROM StockTransSerial s")})
public class StockTransSerial implements Serializable {
public static enum COLUMNS {CREATEDATETIME, FROMSERIAL, QUANTITY, STOCKTRANSDETAILID, STOCKTRANSSERIALID, TOSERIAL, STATEID, PRODOFFERID, STOCKTRANSID, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_SERIAL_ID")
    @SequenceGenerator(name = "STOCK_TRANS_SERIAL_ID_GENERATOR", sequenceName = "STOCK_TRANS_SERIAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_SERIAL_ID_GENERATOR")
    private Long stockTransSerialId;
    @Column(name = "FROM_SERIAL")
    private String fromSerial;
    @Column(name = "TO_SERIAL")
    private String toSerial;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "STOCK_TRANS_DETAIL_ID")
    private Long stockTransDetailId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "NOT_RULE")
    private Long notRule;

    public StockTransSerial() {
    }

    public StockTransSerial(Long stockTransSerialId) {
        this.stockTransSerialId = stockTransSerialId;
    }

    public Long getStockTransSerialId() {
        return stockTransSerialId;
    }

    public void setStockTransSerialId(Long stockTransSerialId) {
        this.stockTransSerialId = stockTransSerialId;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSerial() {
        return toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
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

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getNotRule() {
        return notRule;
    }

    public void setNotRule(Long notRule) {
        this.notRule = notRule;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockTransSerialId != null ? stockTransSerialId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransSerial)) {
            return false;
        }
        StockTransSerial other = (StockTransSerial) object;
        if ((this.stockTransSerialId == null && other.stockTransSerialId != null) || (this.stockTransSerialId != null && !this.stockTransSerialId.equals(other.stockTransSerialId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.StockTransSerial[ stockTransSerialId=" + stockTransSerialId + " ]";
    }
    
}
