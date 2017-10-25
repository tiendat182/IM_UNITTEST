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
 *
 * @author DungPT16
 */
@Entity
@Table(name = "STOCK_INSPECT_REAL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockInspectReal.findAll", query = "SELECT s FROM StockInspectReal s")})
public class StockInspectReal implements Serializable {
public static enum COLUMNS {CREATEDATE, FROMSERIAL, QUANTITY, STOCKINSPECTID, STOCKINSPECTREALID, TOSERIAL, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_INSPECT_REAL_ID")
    @SequenceGenerator(name = "STOCK_INSPECT_REAL_ID_GENERATOR", sequenceName = "STOCK_INSPECT_REAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_INSPECT_REAL_ID_GENERATOR")
    private Long stockInspectRealId;
    @Basic(optional = false)
    @Column(name = "STOCK_INSPECT_ID")
    private Long stockInspectId;
    @Column(name = "FROM_SERIAL")
    private String fromSerial;
    @Column(name = "TO_SERIAL")
    private String toSerial;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public StockInspectReal() {
    }

    public StockInspectReal(Long stockInspectRealId) {
        this.stockInspectRealId = stockInspectRealId;
    }

    public StockInspectReal(Long stockInspectRealId, Long stockInspectId) {
        this.stockInspectRealId = stockInspectRealId;
        this.stockInspectId = stockInspectId;
    }

    public Long getStockInspectRealId() {
        return stockInspectRealId;
    }

    public void setStockInspectRealId(Long stockInspectRealId) {
        this.stockInspectRealId = stockInspectRealId;
    }

    public Long getStockInspectId() {
        return stockInspectId;
    }

    public void setStockInspectId(Long stockInspectId) {
        this.stockInspectId = stockInspectId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockInspectRealId != null ? stockInspectRealId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockInspectReal)) {
            return false;
        }
        StockInspectReal other = (StockInspectReal) object;
        if ((this.stockInspectRealId == null && other.stockInspectRealId != null) || (this.stockInspectRealId != null && !this.stockInspectRealId.equals(other.stockInspectRealId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockInspectReal[ stockInspectRealId=" + stockInspectRealId + " ]";
    }
    
}
