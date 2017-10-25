package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class ApTransInfoDTO extends BaseDTO implements Serializable {

    private Long createShopId;
    private Long createStaffId;
    private String extendList;
    private String feeList;
    private Short goodLocked;
    private String ipList;
    private Date logDate;
    private String posId;
    private String pstnList;
    private Short qtyIssueDecrease;
    private Short qtySupplyIssueDecrease;
    private String requestId;
    private String response;
    private Long shopId;
    private Long staffId;
    private Short status;
    private String stockList;
    private String toString;
    private Short transType;
    private String version;

    public String getKeySet() {
        return keySet; }
    public Long getCreateShopId() {
        return this.createShopId;
    }
    public void setCreateShopId(Long createShopId) {
        this.createShopId = createShopId;
    }
    public Long getCreateStaffId() {
        return this.createStaffId;
    }
    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }
    public String getExtendList() {
        return this.extendList;
    }
    public void setExtendList(String extendList) {
        this.extendList = extendList;
    }
    public String getFeeList() {
        return this.feeList;
    }
    public void setFeeList(String feeList) {
        this.feeList = feeList;
    }
    public Short getGoodLocked() {
        return this.goodLocked;
    }
    public void setGoodLocked(Short goodLocked) {
        this.goodLocked = goodLocked;
    }
    public String getIpList() {
        return this.ipList;
    }
    public void setIpList(String ipList) {
        this.ipList = ipList;
    }
    public Date getLogDate() {
        return this.logDate;
    }
    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
    public String getPosId() {
        return this.posId;
    }
    public void setPosId(String posId) {
        this.posId = posId;
    }
    public String getPstnList() {
        return this.pstnList;
    }
    public void setPstnList(String pstnList) {
        this.pstnList = pstnList;
    }
    public Short getQtyIssueDecrease() {
        return this.qtyIssueDecrease;
    }
    public void setQtyIssueDecrease(Short qtyIssueDecrease) {
        this.qtyIssueDecrease = qtyIssueDecrease;
    }
    public Short getQtySupplyIssueDecrease() {
        return this.qtySupplyIssueDecrease;
    }
    public void setQtySupplyIssueDecrease(Short qtySupplyIssueDecrease) {
        this.qtySupplyIssueDecrease = qtySupplyIssueDecrease;
    }
    public String getRequestId() {
        return this.requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public String getResponse() {
        return this.response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    public Long getShopId() {
        return this.shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public Long getStaffId() {
        return this.staffId;
    }
    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
    public Short getStatus() {
        return this.status;
    }
    public void setStatus(Short status) {
        this.status = status;
    }
    public String getStockList() {
        return this.stockList;
    }
    public void setStockList(String stockList) {
        this.stockList = stockList;
    }
    public String getToString() {
        return this.toString;
    }
    public void setToString(String toString) {
        this.toString = toString;
    }
    public Short getTransType() {
        return this.transType;
    }
    public void setTransType(Short transType) {
        this.transType = transType;
    }
    public String getVersion() {
        return this.version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
}
