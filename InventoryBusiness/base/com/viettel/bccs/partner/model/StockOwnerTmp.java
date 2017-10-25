/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.partner.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author DungPT16
 */
@Entity
@Table(name = "STOCK_OWNER_TMP", schema = "BCCS_IM")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StockOwnerTmp.findAll", query = "SELECT s FROM StockOwnerTmp s")})
public class StockOwnerTmp implements Serializable {
    public static enum COLUMNS {AGENTID, CHANNELTYPEID, CODE, CURRENTDEBIT, DATERESET, MAXDEBIT, NAME, OWNERID, OWNERTYPE, STOCKID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_ID")
    private Long stockId;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;
    @Column(name = "OWNER_TYPE")
    private String ownerType;
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;
    @Column(name = "MAX_DEBIT")
    private Long maxDebit;
    @Column(name = "CURRENT_DEBIT")
    private Long currentDebit;
    @Column(name = "DATE_RESET")
    private Long dateReset;
    //@Column(name = "AGENT_ID")
    @Transient
    private Long agentId;

    public StockOwnerTmp() {
    }

    public StockOwnerTmp(Long stockId) {
        this.stockId = stockId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public Long getMaxDebit() {
        return maxDebit;
    }

    public void setMaxDebit(Long maxDebit) {
        this.maxDebit = maxDebit;
    }

    public Long getCurrentDebit() {
        return currentDebit;
    }

    public void setCurrentDebit(Long currentDebit) {
        this.currentDebit = currentDebit;
    }

    public Long getDateReset() {
        return dateReset;
    }

    public void setDateReset(Long dateReset) {
        this.dateReset = dateReset;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockId != null ? stockId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockOwnerTmp)) {
            return false;
        }
        StockOwnerTmp other = (StockOwnerTmp) object;
        if ((this.stockId == null && other.stockId != null) || (this.stockId != null && !this.stockId.equals(other.stockId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.partner.model.StockOwnerTmp[ stockId=" + stockId + " ]";
    }

}
