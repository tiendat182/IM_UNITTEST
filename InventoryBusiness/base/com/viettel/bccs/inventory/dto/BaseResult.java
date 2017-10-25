package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * Created by lamnv5 on 6/27/2016.
 */
public class BaseResult {
    String responseCode;
    String description;
    String ownerCode;
    String status;
    String amsSaleTransId;
    String transactionId;
    String transId;
    String stockModelCode;
    String serial;
    String licenseKey;

    public String getStockModelCode() {
        return stockModelCode;
    }
    public void setStockModelCode(String stockModelCode) {
        this.stockModelCode = stockModelCode;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmsSaleTransId() {
        return amsSaleTransId;
    }

    public void setAmsSaleTransId(String amsSaleTransId) {
        this.amsSaleTransId = amsSaleTransId;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    //longpn4 R12013
    public BaseResult(String responseCode, String description) {
        this.responseCode = responseCode;
        this.description = description;
    }

    public BaseResult( BaseMessage baseMessage){
        this.responseCode = baseMessage.getErrorCode();
        this.description = baseMessage.getDescription();
    }

    public BaseResult() {
    }

}
