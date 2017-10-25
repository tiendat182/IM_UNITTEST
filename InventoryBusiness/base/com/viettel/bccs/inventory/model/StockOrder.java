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
 * @author DungPT16
 */
@Entity
@Table(name = "STOCK_ORDER")
@NamedQueries({
        @NamedQuery(name = "StockOrder.findAll", query = "SELECT s FROM StockOrder s")})
public class StockOrder implements Serializable {
    public static enum COLUMNS {APPROVEDATE, APPROVESTAFFID, CANCELDATE, CREATEDATE, REFUSEDATE, REFUSESTAFFID, SHOPID, STAFFID, STATUS, STOCKORDERCODE, STOCKORDERID, EXCLUSE_ID_LIST}

    ;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_ORDER_ID")
    private Long stockOrderId;
    @Column(name = "STOCK_ORDER_CODE")
    private String stockOrderCode;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "CANCEL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancelDate;
    @Column(name = "REFUSE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date refuseDate;
    @Column(name = "REFUSE_STAFF_ID")
    private Long refuseStaffId;
    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveDate;
    @Column(name = "APPROVE_STAFF_ID")
    private Long approveStaffId;

    public StockOrder() {
    }

    public StockOrder(Long stockOrderId) {
        this.stockOrderId = stockOrderId;
    }

    public Long getStockOrderId() {
        return stockOrderId;
    }

    public void setStockOrderId(Long stockOrderId) {
        this.stockOrderId = stockOrderId;
    }

    public String getStockOrderCode() {
        return stockOrderCode;
    }

    public void setStockOrderCode(String stockOrderCode) {
        this.stockOrderCode = stockOrderCode;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Date getRefuseDate() {
        return refuseDate;
    }

    public void setRefuseDate(Date refuseDate) {
        this.refuseDate = refuseDate;
    }

    public Long getRefuseStaffId() {
        return refuseStaffId;
    }

    public void setRefuseStaffId(Long refuseStaffId) {
        this.refuseStaffId = refuseStaffId;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Long getApproveStaffId() {
        return approveStaffId;
    }

    public void setApproveStaffId(Long approveStaffId) {
        this.approveStaffId = approveStaffId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockOrderId != null ? stockOrderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockOrder)) {
            return false;
        }
        StockOrder other = (StockOrder) object;
        if ((this.stockOrderId == null && other.stockOrderId != null) || (this.stockOrderId != null && !this.stockOrderId.equals(other.stockOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockOrder[ stockOrderId=" + stockOrderId + " ]";
    }

}
