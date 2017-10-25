package com.viettel.bccs.inventory.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 7/14/2016.
 */
public class BillStockDTO {
    private String orderCode;
    private String impactType;
    private String reason;
    private String status;
    private String errorMessage;
    private String orderType;
    private Date ieExpectDate;
    private String inputType;
    private String outputType;
    private String transCode;
    private String stockCode;
    private String actionCode;
    //Ma lenh ben Logistic
    private String orderAction;
    private String orderActionOld;
    //Ma doi tac cho Logistic
    private String custCode;
    private String staffAction;

    private String stockerCode;
    private List<GoodsDTO> lstGoods;

    public BillStockDTO() {
    }

    public BillStockDTO(String errorCode, String errorMessage) {
        this.status = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getOrderActionOld() {
        return orderActionOld;
    }

    public void setOrderActionOld(String orderActionOld) {
        this.orderActionOld = orderActionOld;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getImpactType() {
        return impactType;
    }

    public void setImpactType(String impactType) {
        this.impactType = impactType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getErrorCode() {
        return status;
    }

    public void setErrorCode(String errorCode) {
        this.status = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getIeExpectDate() {
        return ieExpectDate;
    }

    public void setIeExpectDate(Date ieExpectDate) {
        this.ieExpectDate = ieExpectDate;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public List<GoodsDTO> getLstGoods() {
        return lstGoods;
    }

    public void setLstGoods(List<GoodsDTO> lstGoods) {
        this.lstGoods = lstGoods;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getOrderAction() {
        return orderAction;
    }

    public void setOrderAction(String orderAction) {
        this.orderAction = orderAction;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getStaffAction() {
        return staffAction;
    }

    public void setStaffAction(String staffAction) {
        this.staffAction = staffAction;
    }

    public String getStockerCode() {
        return stockerCode;
    }

    public void setStockerCode(String stockerCode) {
        this.stockerCode = stockerCode;
    }
}
