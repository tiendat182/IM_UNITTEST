/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.im1.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author thetm1
 */
@Entity
@Table(name = "BCCS_IM.STOCK_DEBIT")
public class StockDebitIm1 implements Serializable {
public static enum COLUMNS {CREATEDATE, CREATEUSER, DEBITDAYTYPE, DEBITVALUE, FINANCETYPE, LASTUPDATETIME, LASTUPDATEUSER, OWNERID, OWNERTYPE, STOCKDEBITID, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_DEBIT_ID")
    @SequenceGenerator(name = "STOCK_DEBIT_IM1_ID_GENERATOR", sequenceName = "BCCS_IM.STOCK_DEBIT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_DEBIT_IM1_ID_GENERATOR")
    private Long stockDebitId;
    @Column(name = "DEBIT_DAY_TYPE")
    private String debitDayType;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private String ownerType;
    @Column(name = "DEBIT_TYPE")
    private Long debitValue;
    @Column(name = "FINANCE_TYPE")
    private String financeType;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "LAST_UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;

    public StockDebitIm1() {
    }

    public StockDebitIm1(Long stockDebitId) {
        this.stockDebitId = stockDebitId;
    }

    public Long getStockDebitId() {
        return stockDebitId;
    }

    public void setStockDebitId(Long stockDebitId) {
        this.stockDebitId = stockDebitId;
    }

    public String getDebitDayType() {
        return debitDayType;
    }

    public void setDebitDayType(String debitDayType) {
        this.debitDayType = debitDayType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getDebitValue() {
        return debitValue;
    }

    public void setDebitValue(Long debitValue) {
        this.debitValue = debitValue;
    }

    public String getFinanceType() {
        return financeType;
    }

    public void setFinanceType(String financeType) {
        this.financeType = financeType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockDebitId != null ? stockDebitId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockDebitIm1)) {
            return false;
        }
        StockDebitIm1 other = (StockDebitIm1) object;
        if ((this.stockDebitId == null && other.stockDebitId != null) || (this.stockDebitId != null && !this.stockDebitId.equals(other.stockDebitId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.StockDebit[ stockDebitId=" + stockDebitId + " ]";
    }
    
}
