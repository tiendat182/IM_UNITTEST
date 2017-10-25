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
 *
 * @author Dungha7
 */
@Entity
@Table(name = "STOCK_REQUEST_ORDER")
public class StockRequestOrder implements Serializable {
public static enum COLUMNS {CREATEDATETIME, CREATEUSER, DESCRIPTION, ORDERCODE, ORDERTYPE, OWNERID, OWNERTYPE, STATUS, STOCKREQUESTORDERID, UPDATEDATETIME, UPDATEUSER, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_REQUEST_ORDER_ID")
    @SequenceGenerator(name = "STOCK_REQUEST_ORDER_ID_GENERATOR", sequenceName = "STOCK_REQUEST_ORDER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_REQUEST_ORDER_ID_GENERATOR")
    private Long stockRequestOrderId;
    @Column(name = "ORDER_CODE")
    private String orderCode;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "ORDER_TYPE")
    private Long orderType;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "RETRY")
    private Long retry;
    @Column(name = "ERROR_CODE")
    private String errorCode;
    @Column(name = "ERROR_CODE_DESCRIPTION")
    private String errorCodeDescription;

    public StockRequestOrder() {
    }

    public Long getRetry() {
        return retry;
    }

    public void setRetry(Long retry) {
        this.retry = retry;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCodeDescription() {
        return errorCodeDescription;
    }

    public void setErrorCodeDescription(String errorCodeDescription) {
        this.errorCodeDescription = errorCodeDescription;
    }

    public StockRequestOrder(Long stockRequestOrderId) {
        this.stockRequestOrderId = stockRequestOrderId;
    }

    public Long getStockRequestOrderId() {
        return stockRequestOrderId;
    }

    public void setStockRequestOrderId(Long stockRequestOrderId) {
        this.stockRequestOrderId = stockRequestOrderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Long getOrderType() {
        return orderType;
    }

    public void setOrderType(Long orderType) {
        this.orderType = orderType;
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
        hash += (stockRequestOrderId != null ? stockRequestOrderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockRequestOrder)) {
            return false;
        }
        StockRequestOrder other = (StockRequestOrder) object;
        if ((this.stockRequestOrderId == null && other.stockRequestOrderId != null) || (this.stockRequestOrderId != null && !this.stockRequestOrderId.equals(other.stockRequestOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.StockRequestOrder[ stockRequestOrderId=" + stockRequestOrderId + " ]";
    }
    
}
