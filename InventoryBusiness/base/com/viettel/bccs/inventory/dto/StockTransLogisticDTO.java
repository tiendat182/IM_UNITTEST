package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockTransLogisticDTO extends BaseDTO implements Serializable {

    private Date createDate;
    private String logisticDescription;
    private String logisticResponseCode;
    private Long logisticRetry;
    private String printUserList;
    private String rejectNoteDescription;
    private String rejectNoteResponse;
    private Long requestTypeLogistic;
    private Long status;
    private Long stockTransId;
    private Long stockTransLogisticId;
    private Long stockTransType;
    private Long updateLogistic;

    public String getKeySet() {
        return keySet; }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getLogisticDescription() {
        return this.logisticDescription;
    }
    public void setLogisticDescription(String logisticDescription) {
        this.logisticDescription = logisticDescription;
    }
    public String getLogisticResponseCode() {
        return this.logisticResponseCode;
    }
    public void setLogisticResponseCode(String logisticResponseCode) {
        this.logisticResponseCode = logisticResponseCode;
    }
    public Long getLogisticRetry() {
        return this.logisticRetry;
    }
    public void setLogisticRetry(Long logisticRetry) {
        this.logisticRetry = logisticRetry;
    }
    public String getPrintUserList() {
        return this.printUserList;
    }
    public void setPrintUserList(String printUserList) {
        this.printUserList = printUserList;
    }
    public String getRejectNoteDescription() {
        return this.rejectNoteDescription;
    }
    public void setRejectNoteDescription(String rejectNoteDescription) {
        this.rejectNoteDescription = rejectNoteDescription;
    }
    public String getRejectNoteResponse() {
        return this.rejectNoteResponse;
    }
    public void setRejectNoteResponse(String rejectNoteResponse) {
        this.rejectNoteResponse = rejectNoteResponse;
    }
    public Long getRequestTypeLogistic() {
        return this.requestTypeLogistic;
    }
    public void setRequestTypeLogistic(Long requestTypeLogistic) {
        this.requestTypeLogistic = requestTypeLogistic;
    }
    public Long getStatus() {
        return this.status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public Long getStockTransId() {
        return this.stockTransId;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
    public Long getStockTransLogisticId() {
        return this.stockTransLogisticId;
    }
    public void setStockTransLogisticId(Long stockTransLogisticId) {
        this.stockTransLogisticId = stockTransLogisticId;
    }
    public Long getStockTransType() {
        return this.stockTransType;
    }
    public void setStockTransType(Long stockTransType) {
        this.stockTransType = stockTransType;
    }
    public Long getUpdateLogistic() {
        return this.updateLogistic;
    }
    public void setUpdateLogistic(Long updateLogistic) {
        this.updateLogistic = updateLogistic;
    }
}
