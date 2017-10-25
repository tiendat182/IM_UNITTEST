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
 * @author tuydv1
 */
@Entity
@Table(name = "STOCK_ORDER_AGENT")
@NamedQueries({
        @NamedQuery(name = "StockOrderAgent.findAll", query = "SELECT s FROM StockOrderAgent s")})
public class StockOrderAgent implements Serializable {
    public static enum COLUMNS {BANKCODE, CREATEDATE, CREATESTAFFID, LASTMODIFY, NOTE, ORDERTYPE, REQUESTCODE, SHOPID, STATUS, STOCKORDERAGENTID, STOCKTRANSID, UPDATESTAFFID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_ORDER_AGENT_ID")
    @SequenceGenerator(name = "STOCK_ORDER_AGENT_ID_GENERATOR", sequenceName = "STOCK_ORDER_AGENT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_ORDER_AGENT_ID_GENERATOR")
    private Long stockOrderAgentId;
    @Basic(optional = false)
    @Column(name = "REQUEST_CODE")
    private String requestCode;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "CREATE_STAFF_ID")
    private Long createStaffId;
    @Column(name = "ORDER_TYPE")
    private Long orderType;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LAST_MODIFY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModify;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "UPDATE_STAFF_ID")
    private Long updateStaffId;
    @Column(name = "BANK_CODE")
    private String bankCode;

    public StockOrderAgent() {
    }

    public StockOrderAgent(Long stockOrderAgentId) {
        this.stockOrderAgentId = stockOrderAgentId;
    }

    public StockOrderAgent(Long stockOrderAgentId, String requestCode) {
        this.stockOrderAgentId = stockOrderAgentId;
        this.requestCode = requestCode;
    }

    public Long getStockOrderAgentId() {
        return stockOrderAgentId;
    }

    public void setStockOrderAgentId(Long stockOrderAgentId) {
        this.stockOrderAgentId = stockOrderAgentId;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Long getOrderType() {
        return orderType;
    }

    public void setOrderType(Long orderType) {
        this.orderType = orderType;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getUpdateStaffId() {
        return updateStaffId;
    }

    public void setUpdateStaffId(Long updateStaffId) {
        this.updateStaffId = updateStaffId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockOrderAgentId != null ? stockOrderAgentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockOrderAgent)) {
            return false;
        }
        StockOrderAgent other = (StockOrderAgent) object;
        if ((this.stockOrderAgentId == null && other.stockOrderAgentId != null) || (this.stockOrderAgentId != null && !this.stockOrderAgentId.equals(other.stockOrderAgentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockOrderAgent[ stockOrderAgentId=" + stockOrderAgentId + " ]";
    }

}
