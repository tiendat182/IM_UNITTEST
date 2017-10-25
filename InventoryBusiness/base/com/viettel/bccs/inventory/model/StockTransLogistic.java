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
 * @author thetm1
 */
@Entity
@Table(name = "STOCK_TRANS_LOGISTIC")
@NamedQueries({
    @NamedQuery(name = "StockTransLogistic.findAll", query = "SELECT s FROM StockTransLogistic s")})
public class StockTransLogistic implements Serializable {
public static enum COLUMNS {CREATEDATE, LOGISTICDESCRIPTION, LOGISTICRESPONSECODE, LOGISTICRETRY, PRINTUSERLIST, REJECTNOTEDESCRIPTION, REJECTNOTERESPONSE, REQUESTTYPELOGISTIC, STATUS, STOCKTRANSID, STOCKTRANSLOGISTICID, STOCKTRANSTYPE, UPDATELOGISTIC, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_LOGISTIC_ID")
    @SequenceGenerator(name = "STOCK_TRANS_LOGISTIC_ID_GENERATOR", sequenceName = "STOCK_TRANS_LOGISTIC_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_LOGISTIC_ID_GENERATOR")
    private Long stockTransLogisticId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "REQUEST_TYPE_LOGISTIC")
    private Long requestTypeLogistic;
    @Column(name = "STOCK_TRANS_TYPE")
    private Long stockTransType;
    @Column(name = "LOGISTIC_RETRY")
    private Long logisticRetry;
    @Column(name = "LOGISTIC_RESPONSE_CODE")
    private String logisticResponseCode;
    @Column(name = "LOGISTIC_DESCRIPTION")
    private String logisticDescription;
    @Column(name = "REJECT_NOTE_RESPONSE")
    private String rejectNoteResponse;
    @Column(name = "REJECT_NOTE_DESCRIPTION")
    private String rejectNoteDescription;
    @Column(name = "PRINT_USER_LIST")
    private String printUserList;
    @Column(name = "UPDATE_LOGISTIC")
    private Long updateLogistic;
    @Basic(optional = false)
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "STATUS")
    private Long status;

    public StockTransLogistic() {
    }

    public StockTransLogistic(Long stockTransLogisticId) {
        this.stockTransLogisticId = stockTransLogisticId;
    }

    public StockTransLogistic(Long stockTransLogisticId, Date createDate) {
        this.stockTransLogisticId = stockTransLogisticId;
        this.createDate = createDate;
    }

    public Long getStockTransLogisticId() {
        return stockTransLogisticId;
    }

    public void setStockTransLogisticId(Long stockTransLogisticId) {
        this.stockTransLogisticId = stockTransLogisticId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getRequestTypeLogistic() {
        return requestTypeLogistic;
    }

    public void setRequestTypeLogistic(Long requestTypeLogistic) {
        this.requestTypeLogistic = requestTypeLogistic;
    }

    public Long getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(Long stockTransType) {
        this.stockTransType = stockTransType;
    }

    public Long getLogisticRetry() {
        return logisticRetry;
    }

    public void setLogisticRetry(Long logisticRetry) {
        this.logisticRetry = logisticRetry;
    }

    public String getLogisticResponseCode() {
        return logisticResponseCode;
    }

    public void setLogisticResponseCode(String logisticResponseCode) {
        this.logisticResponseCode = logisticResponseCode;
    }

    public String getLogisticDescription() {
        return logisticDescription;
    }

    public void setLogisticDescription(String logisticDescription) {
        this.logisticDescription = logisticDescription;
    }

    public String getRejectNoteResponse() {
        return rejectNoteResponse;
    }

    public void setRejectNoteResponse(String rejectNoteResponse) {
        this.rejectNoteResponse = rejectNoteResponse;
    }

    public String getRejectNoteDescription() {
        return rejectNoteDescription;
    }

    public void setRejectNoteDescription(String rejectNoteDescription) {
        this.rejectNoteDescription = rejectNoteDescription;
    }

    public String getPrintUserList() {
        return printUserList;
    }

    public void setPrintUserList(String printUserList) {
        this.printUserList = printUserList;
    }

    public Long getUpdateLogistic() {
        return updateLogistic;
    }

    public void setUpdateLogistic(Long updateLogistic) {
        this.updateLogistic = updateLogistic;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockTransLogisticId != null ? stockTransLogisticId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransLogistic)) {
            return false;
        }
        StockTransLogistic other = (StockTransLogistic) object;
        if ((this.stockTransLogisticId == null && other.stockTransLogisticId != null) || (this.stockTransLogisticId != null && !this.stockTransLogisticId.equals(other.stockTransLogisticId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockTransLogistic[ stockTransLogisticId=" + stockTransLogisticId + " ]";
    }
    
}
