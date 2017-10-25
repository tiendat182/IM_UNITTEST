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
@Table(name = "STOCK_TRANS_SERIAL_RESCUE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StockTransSerialRescue.findAll", query = "SELECT s FROM StockTransSerialRescue s")})
public class StockTransSerialRescue implements Serializable {
    public static enum COLUMNS {CREATEDATE, FROMSERIAL, PRODOFFERID, QUANTITY, STATEID, STOCKTRANSID, STOCKTRANSSERIALID, TOSERIAL, EXCLUSE_ID_LIST, PRODOFFERIDRETURN, SERIALRETURN}

    ;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_SERIAL_ID")
    @SequenceGenerator(name = "STOCK_TRANS_SERIAL_RESCUE_ID_GENERATOR", sequenceName = "STOCK_TRANS_SERIAL_RESCUE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_SERIAL_RESCUE_ID_GENERATOR")
    private Long stockTransSerialId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "PROD_OFFER_ID_RETURN")
    private Long prodOfferIdReturn;
    @Column(name = "FROM_SERIAL")
    private String fromSerial;
    @Column(name = "SERIAL_RETURN")
    private String serialReturn;
    @Column(name = "TO_SERIAL")
    private String toSerial;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public StockTransSerialRescue() {
    }

    public StockTransSerialRescue(Long stockTransSerialId) {
        this.stockTransSerialId = stockTransSerialId;
    }

    public Long getStockTransSerialId() {
        return stockTransSerialId;
    }

    public void setStockTransSerialId(Long stockTransSerialId) {
        this.stockTransSerialId = stockTransSerialId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getProdOfferIdReturn() {
        return prodOfferIdReturn;
    }

    public void setProdOfferIdReturn(Long prodOfferIdReturn) {
        this.prodOfferIdReturn = prodOfferIdReturn;
    }

    public String getSerialReturn() {
        return serialReturn;
    }

    public void setSerialReturn(String serialReturn) {
        this.serialReturn = serialReturn;
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
        if (!(object instanceof StockTransSerialRescue)) {
            return false;
        }
        StockTransSerialRescue other = (StockTransSerialRescue) object;
        if ((this.stockTransSerialId == null && other.stockTransSerialId != null) || (this.stockTransSerialId != null && !this.stockTransSerialId.equals(other.stockTransSerialId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockTransSerialRescue[ stockTransSerialId=" + stockTransSerialId + " ]";
    }

}
