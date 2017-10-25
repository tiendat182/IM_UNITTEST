/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.sale.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author nhannt34
 */
@Entity
@Table(name = "SALE_TRANS_SERIAL", catalog = "", schema = "BCCS_SALE")
public class SaleTransSerial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALE_TRANS_SERIAL_ID")
    private Long saleTransSerialId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALE_TRANS_DETAIL_ID")
    private Long saleTransDetailId;
    @Column(name = "STOCK_MODEL_ID")
    private Long stockModelId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALE_TRANS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleTransDate;
    @Column(name = "USER_DELIVER")
    private Long userDeliver;
    @Column(name = "DATE_DELIVER")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDeliver;
    @Column(name = "USER_UPDATE")
    private Long userUpdate;
    @Size(max = 20)
    @Column(name = "FROM_SERIAL")
    private String fromSerial;
    @Size(max = 20)
    @Column(name = "TO_SERIAL")
    private String toSerial;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "SALE_TRANS_ID")
    private Long saleTransId;

    public SaleTransSerial() {
    }

    public SaleTransSerial(Long saleTransSerialId) {
        this.saleTransSerialId = saleTransSerialId;
    }

    public SaleTransSerial(Long saleTransSerialId, long saleTransDetailId, Date saleTransDate) {
        this.saleTransSerialId = saleTransSerialId;
        this.saleTransDetailId = saleTransDetailId;
        this.saleTransDate = saleTransDate;
    }

    public Long getSaleTransSerialId() {
        return saleTransSerialId;
    }

    public void setSaleTransSerialId(Long saleTransSerialId) {
        this.saleTransSerialId = saleTransSerialId;
    }

    public long getSaleTransDetailId() {
        return saleTransDetailId;
    }

    public void setSaleTransDetailId(long saleTransDetailId) {
        this.saleTransDetailId = saleTransDetailId;
    }

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public Date getSaleTransDate() {
        return saleTransDate;
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }

    public Long getUserDeliver() {
        return userDeliver;
    }

    public void setUserDeliver(Long userDeliver) {
        this.userDeliver = userDeliver;
    }

    public Date getDateDeliver() {
        return dateDeliver;
    }

    public void setDateDeliver(Date dateDeliver) {
        this.dateDeliver = dateDeliver;
    }

    public Long getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(Long userUpdate) {
        this.userUpdate = userUpdate;
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

    public Long getSaleTransId() {
        return saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saleTransSerialId != null ? saleTransSerialId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaleTransSerial)) {
            return false;
        }
        SaleTransSerial other = (SaleTransSerial) object;
        if ((this.saleTransSerialId == null && other.saleTransSerialId != null) || (this.saleTransSerialId != null && !this.saleTransSerialId.equals(other.saleTransSerialId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.sale.model.SaleTransSerial[ saleTransSerialId=" + saleTransSerialId + " ]";
    }
    
}
