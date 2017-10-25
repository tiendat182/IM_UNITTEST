package com.viettel.bccs.im1.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class ReqActivateKitDTO extends BaseDTO implements Serializable {

    private Date createDate;
    private String errorDescription;
    private String fromSerial;
    private Long processCount;
    private Date processDate;
    private Long processStatus;
    private Long reqId;
    private Date saleTransDate;
    private Long saleTransId;
    private Long saleTransType;
    private Long saleType;
    private Long shopId;
    private Long staffId;
    private String toSerial;

    public String getKeySet() {
        return keySet; }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getErrorDescription() {
        return this.errorDescription;
    }
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
    public String getFromSerial() {
        return this.fromSerial;
    }
    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }
    public Long getProcessCount() {
        return this.processCount;
    }
    public void setProcessCount(Long processCount) {
        this.processCount = processCount;
    }
    public Date getProcessDate() {
        return this.processDate;
    }
    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }
    public Long getProcessStatus() {
        return this.processStatus;
    }
    public void setProcessStatus(Long processStatus) {
        this.processStatus = processStatus;
    }
    public Long getReqId() {
        return this.reqId;
    }
    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }
    public Date getSaleTransDate() {
        return this.saleTransDate;
    }
    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }
    public Long getSaleTransId() {
        return this.saleTransId;
    }
    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }
    public Long getSaleTransType() {
        return this.saleTransType;
    }
    public void setSaleTransType(Long saleTransType) {
        this.saleTransType = saleTransType;
    }
    public Long getSaleType() {
        return this.saleType;
    }
    public void setSaleType(Long saleType) {
        this.saleType = saleType;
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
    public String getToSerial() {
        return this.toSerial;
    }
    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }
}
