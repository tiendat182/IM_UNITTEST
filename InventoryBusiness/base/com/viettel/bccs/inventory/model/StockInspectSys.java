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
@Table(name = "STOCK_INSPECT_SYS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockInspectSys.findAll", query = "SELECT s FROM StockInspectSys s")})
public class StockInspectSys implements Serializable {
public static enum COLUMNS {CREATEDATE, FROMSERIAL, QUANTITY, STOCKINSPECTID, STOCKINSPECTSYSID, TOSERIAL, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_INSPECT_SYS_ID")
    @SequenceGenerator(name = "STOCK_INSPECT_SYS_ID_GENERATOR", sequenceName = "STOCK_INSPECT_SYS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_INSPECT_SYS_ID_GENERATOR")
    private Long stockInspectSysId;
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

    public StockInspectSys() {
    }

    public StockInspectSys(Long stockInspectSysId) {
        this.stockInspectSysId = stockInspectSysId;
    }

    public StockInspectSys(Long stockInspectSysId, Long stockInspectId) {
        this.stockInspectSysId = stockInspectSysId;
        this.stockInspectId = stockInspectId;
    }

    public Long getStockInspectSysId() {
        return stockInspectSysId;
    }

    public void setStockInspectSysId(Long stockInspectSysId) {
        this.stockInspectSysId = stockInspectSysId;
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
        hash += (stockInspectSysId != null ? stockInspectSysId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockInspectSys)) {
            return false;
        }
        StockInspectSys other = (StockInspectSys) object;
        if ((this.stockInspectSysId == null && other.stockInspectSysId != null) || (this.stockInspectSysId != null && !this.stockInspectSysId.equals(other.stockInspectSysId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockInspectSys[ stockInspectSysId=" + stockInspectSysId + " ]";
    }
    
}
