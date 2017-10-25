package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.List;

public class DivideSerialActionDTO extends BaseDTO implements Serializable {
    private Date createDatetime;
    private String createUser;
    private Long id;
    private String serialContent;
    private Long stockTransActionId;
    private String fromSerial;
    private String toSerial;
    private List<Long> lstStockTransActionId;
    private List<StockTransSerialDTO> lstStockTransSerial;
    private List<String> lstStockImpInFile;
    private String actionCode;
    private String stockTransStatus;
    private String serialStatus;
    private Date fromDate;
    private Date toDate;
    private String ownerCode;

    public String getKeySet() {
        return keySet;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialContent() {
        return this.serialContent;
    }

    public void setSerialContent(String serialContent) {
        this.serialContent = serialContent;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSerial() {
        return toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public List<Long> getLstStockTransActionId() {
        return lstStockTransActionId;
    }

    public void setLstStockTransActionId(List<Long> lstStockTransActionId) {
        this.lstStockTransActionId = lstStockTransActionId;
    }

    public List<StockTransSerialDTO> getLstStockTransSerial() {
        return lstStockTransSerial;
    }

    public void setLstStockTransSerial(List<StockTransSerialDTO> lstStockTransSerial) {
        this.lstStockTransSerial = lstStockTransSerial;
    }

    public List<String> getLstStockImpInFile() {
        return lstStockImpInFile;
    }

    public void setLstStockImpInFile(List<String> lstStockImpInFile) {
        this.lstStockImpInFile = lstStockImpInFile;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getStockTransStatus() {
        return stockTransStatus;
    }

    public void setStockTransStatus(String stockTransStatus) {
        this.stockTransStatus = stockTransStatus;
    }

    public String getSerialStatus() {
        return serialStatus;
    }

    public void setSerialStatus(String serialStatus) {
        this.serialStatus = serialStatus;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }
}
