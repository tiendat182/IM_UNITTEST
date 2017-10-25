package com.viettel.bccs.inventory.dto;

import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.sale.model.SaleTrans;

import java.util.Date;

public class SendSmsDTO {

    private String actionCode;
    private String stockTransType;
    private Long realTransUserId;
    private Date startTime;
    private Date endTime;
    private String result;
    private Long actionId;
    private StockTrans stockTrans;
    private SaleTrans saleTrans;
    private String stockTransStatus;
    private Staff staffImpExp;
    private String sendMess;
    private Long saleTransId;

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(String stockTransType) {
        this.stockTransType = stockTransType;
    }

    public Long getRealTransUserId() {
        return realTransUserId;
    }

    public void setRealTransUserId(Long realTransUserId) {
        this.realTransUserId = realTransUserId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public StockTrans getStockTrans() {
        return stockTrans;
    }

    public void setStockTrans(StockTrans stockTrans) {
        this.stockTrans = stockTrans;
    }

    public SaleTrans getSaleTrans() {
        return saleTrans;
    }

    public void setSaleTrans(SaleTrans saleTrans) {
        this.saleTrans = saleTrans;
    }

    public String getStockTransStatus() {
        return stockTransStatus;
    }

    public void setStockTransStatus(String stockTransStatus) {
        this.stockTransStatus = stockTransStatus;
    }

    public Staff getStaffImpExp() {
        return staffImpExp;
    }

    public void setStaffImpExp(Staff staffImpExp) {
        this.staffImpExp = staffImpExp;
    }

    public String getSendMess() {
        return sendMess;
    }

    public void setSendMess(String sendMess) {
        this.sendMess = sendMess;
    }

    public Long getSaleTransId() {
        return saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }
}
