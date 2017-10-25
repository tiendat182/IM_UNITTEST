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
@Table(name = "STOCK_BALANCE_SERIAL")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StockBalanceSerial.findAll", query = "SELECT s FROM StockBalanceSerial s")})
public class StockBalanceSerial implements Serializable {
    public static enum COLUMNS {CREATEDATETIME, FROMSERIAL, PRODOFFERID, STOCKBALANCEDETAIL, STOCKBALANCESERIALID, STOCKREQUESTID, TOSERIAL, EXCLUSE_ID_LIST}

    ;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_BALANCE_SERIAL_ID")
    @SequenceGenerator(name = "STOCK_BALANCE_SERIAL_ID_GENERATOR", sequenceName = "STOCK_BALANCE_SERIAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_BALANCE_SERIAL_ID_GENERATOR")
    private Long stockBalanceSerialId;
    @Basic(optional = false)
    @Column(name = "STOCK_REQUEST_ID")
    private Long stockRequestId;
    @Basic(optional = false)
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "FROM_SERIAL")
    private String fromSerial;
    @Column(name = "TO_SERIAL")
    private String toSerial;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Basic(optional = false)
    @Column(name = "STOCK_BALANCE_DETAIL")
    private Long stockBalanceDetail;

    public StockBalanceSerial() {
    }

    public StockBalanceSerial(Long stockBalanceSerialId) {
        this.stockBalanceSerialId = stockBalanceSerialId;
    }

    public StockBalanceSerial(Long stockBalanceSerialId, Long stockRequestId, Long prodOfferId, Long stockBalanceDetail) {
        this.stockBalanceSerialId = stockBalanceSerialId;
        this.stockRequestId = stockRequestId;
        this.prodOfferId = prodOfferId;
        this.stockBalanceDetail = stockBalanceDetail;
    }

    public Long getStockBalanceSerialId() {
        return stockBalanceSerialId;
    }

    public void setStockBalanceSerialId(Long stockBalanceSerialId) {
        this.stockBalanceSerialId = stockBalanceSerialId;
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

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getStockBalanceDetail() {
        return stockBalanceDetail;
    }

    public void setStockBalanceDetail(Long stockBalanceDetail) {
        this.stockBalanceDetail = stockBalanceDetail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockBalanceSerialId != null ? stockBalanceSerialId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockBalanceSerial)) {
            return false;
        }
        StockBalanceSerial other = (StockBalanceSerial) object;
        if ((this.stockBalanceSerialId == null && other.stockBalanceSerialId != null) || (this.stockBalanceSerialId != null && !this.stockBalanceSerialId.equals(other.stockBalanceSerialId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockBalanceSerial[ stockBalanceSerialId=" + stockBalanceSerialId + " ]";
    }

}
