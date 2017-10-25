package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "STOCK_REQUEST_ORDER_TRANS")
public class StockRequestOrderTrans implements Serializable {
    public static enum COLUMNS {CREATEDATETIME, ERRORCODE, ERRORCODEDESCRIPTION, FROMOWNERID, FROMOWNERTYPE, RETRY, STATUS, STOCKREQUESTORDERID, STOCKREQUESTORDERTRANSID, STOCKTRANSID, TOOWNERID, TOOWNERTYPE, STOCKTRANSTYPE, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_REQUEST_ORDER_TRANS_ID")
    @SequenceGenerator(name = "STOCK_REQUEST_ORDER_TRANS_ID_GENERATOR", sequenceName = "STOCK_REQUEST_ORDER_TRANS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_REQUEST_ORDER_TRANS_ID_GENERATOR")
    private Long stockRequestOrderTransId;
    @Column(name = "STOCK_REQUEST_ORDER_ID")
    private Long stockRequestOrderId;
    @Column(name = "FROM_OWNER_TYPE")
    private Long fromOwnerType;
    @Column(name = "FROM_OWNER_ID")
    private Long fromOwnerId;
    @Column(name = "TO_OWNER_TYPE")
    private Long toOwnerType;
    @Column(name = "TO_OWNER_ID")
    private Long toOwnerId;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "RETRY")
    private Long retry;
    @Column(name = "ERROR_CODE")
    private String errorCode;
    @Column(name = "ERROR_CODE_DESCRIPTION")
    private String errorCodeDescription;
    @Column(name = "STOCK_TRANS_TYPE")
    private Long stockTransType;

    public Long getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(Long stockTransType) {
        this.stockTransType = stockTransType;
    }

    public StockRequestOrderTrans() {
    }

    public StockRequestOrderTrans(Long stockRequestOrderTransId) {
        this.stockRequestOrderTransId = stockRequestOrderTransId;
    }

    public Long getStockRequestOrderTransId() {
        return stockRequestOrderTransId;
    }

    public void setStockRequestOrderTransId(Long stockRequestOrderTransId) {
        this.stockRequestOrderTransId = stockRequestOrderTransId;
    }

    public Long getStockRequestOrderId() {
        return stockRequestOrderId;
    }

    public void setStockRequestOrderId(Long stockRequestOrderId) {
        this.stockRequestOrderId = stockRequestOrderId;
    }

    public Long getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockRequestOrderTransId != null ? stockRequestOrderTransId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockRequestOrderTrans)) {
            return false;
        }
        StockRequestOrderTrans other = (StockRequestOrderTrans) object;
        if ((this.stockRequestOrderTransId == null && other.stockRequestOrderTransId != null) || (this.stockRequestOrderTransId != null && !this.stockRequestOrderTransId.equals(other.stockRequestOrderTransId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai1.StockRequestOrderTrans[ stockRequestOrderTransId=" + stockRequestOrderTransId + " ]";
    }

}
