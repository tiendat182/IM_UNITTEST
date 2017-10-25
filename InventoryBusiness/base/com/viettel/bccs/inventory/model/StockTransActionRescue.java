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
@Table(name = "STOCK_TRANS_ACTION_RESCUE")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StockTransActionRescue.findAll", query = "SELECT s FROM StockTransActionRescue s")})
public class StockTransActionRescue implements Serializable {
    public static enum COLUMNS {ACTIONID, ACTIONSTAFFID, ACTIONTYPE, CREATEDATE, DESCRIPTION, STOCKTRANSID, EXCLUSE_ID_LIST}

    ;

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ACTION_ID")
    @SequenceGenerator(name = "ACTION_ID_RESCUE_GENERATOR", sequenceName = "STOCK_TRANS_ACTION_RESCUE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTION_ID_RESCUE_GENERATOR")
    private Long actionId;
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "ACTION_TYPE")
    private Long actionType;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "ACTION_STAFF_ID")
    private Long actionStaffId;
    @Column(name = "DESCRIPTION")
    private String description;

    public StockTransActionRescue() {
    }

    public StockTransActionRescue(Long actionId) {
        this.actionId = actionId;
    }

    public StockTransActionRescue(Long actionId, Long stockTransId) {
        this.actionId = actionId;
        this.stockTransId = stockTransId;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getActionType() {
        return actionType;
    }

    public void setActionType(Long actionType) {
        this.actionType = actionType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getActionStaffId() {
        return actionStaffId;
    }

    public void setActionStaffId(Long actionStaffId) {
        this.actionStaffId = actionStaffId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actionId != null ? actionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransActionRescue)) {
            return false;
        }
        StockTransActionRescue other = (StockTransActionRescue) object;
        if ((this.actionId == null && other.actionId != null) || (this.actionId != null && !this.actionId.equals(other.actionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockTransActionRescue[ actionId=" + actionId + " ]";
    }

}
