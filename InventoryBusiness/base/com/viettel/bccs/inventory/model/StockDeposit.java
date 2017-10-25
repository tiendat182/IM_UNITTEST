package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "STOCK_DEPOSIT")
public class StockDeposit implements Serializable {
public static enum COLUMNS {CHANELTYPEID, DATEFROM, DATETO, LASTMODIFY, MAXSTOCK, PRODOFFERID, STATUS, STOCKDEPOSITID, TRANSTYPE, USERMODIFY, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Basic(optional = false)
    @Column(name = "CHANEL_TYPE_ID")
    private Long chanelTypeId;
    @Column(name = "MAX_STOCK")
    private Long maxStock;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "DATE_FROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "DATE_TO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_DEPOSIT_ID")
    private Long stockDepositId;
    @Column(name = "TRANS_TYPE")
    private Long transType;
    @Column(name = "USER_MODIFY")
    private String userModify;
    @Column(name = "LAST_MODIFY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModify;

    public StockDeposit() {
    }

    public StockDeposit(Long stockDepositId) {
        this.stockDepositId = stockDepositId;
    }

    public StockDeposit(Long stockDepositId, Long prodOfferId, Long chanelTypeId) {
        this.stockDepositId = stockDepositId;
        this.prodOfferId = prodOfferId;
        this.chanelTypeId = chanelTypeId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getChanelTypeId() {
        return chanelTypeId;
    }

    public void setChanelTypeId(Long chanelTypeId) {
        this.chanelTypeId = chanelTypeId;
    }

    public Long getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(Long maxStock) {
        this.maxStock = maxStock;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Long getStockDepositId() {
        return stockDepositId;
    }

    public void setStockDepositId(Long stockDepositId) {
        this.stockDepositId = stockDepositId;
    }

    public Long getTransType() {
        return transType;
    }

    public void setTransType(Long transType) {
        this.transType = transType;
    }

    public String getUserModify() {
        return userModify;
    }

    public void setUserModify(String userModify) {
        this.userModify = userModify;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockDepositId != null ? stockDepositId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockDeposit)) {
            return false;
        }
        StockDeposit other = (StockDeposit) object;
        if ((this.stockDepositId == null && other.stockDepositId != null) || (this.stockDepositId != null && !this.stockDepositId.equals(other.stockDepositId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai1.StockDeposit[ stockDepositId=" + stockDepositId + " ]";
    }

}

