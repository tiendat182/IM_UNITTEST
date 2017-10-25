package com.viettel.bccs.im1.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class VcRequestDTO extends BaseDTO implements Serializable {

    private Date createTime;
    private String fromSerial;
    private Date lastProcessTime;
    private Long rangeId;
    private Long requestId;
    private Long requestType;
    private Long shopId;
    private Long staffId;
    private Date startProcessTime;
    private Long status;
    private Date sysCreateTime;
    private Date sysProcessTime;
    private String toSerial;
    private Long transId;
    private String userId;

    public String getKeySet() {
        return keySet; }
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getFromSerial() {
        return this.fromSerial;
    }
    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }
    public Date getLastProcessTime() {
        return this.lastProcessTime;
    }
    public void setLastProcessTime(Date lastProcessTime) {
        this.lastProcessTime = lastProcessTime;
    }
    public Long getRangeId() {
        return this.rangeId;
    }
    public void setRangeId(Long rangeId) {
        this.rangeId = rangeId;
    }
    public Long getRequestId() {
        return this.requestId;
    }
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    public Long getRequestType() {
        return this.requestType;
    }
    public void setRequestType(Long requestType) {
        this.requestType = requestType;
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
    public Date getStartProcessTime() {
        return this.startProcessTime;
    }
    public void setStartProcessTime(Date startProcessTime) {
        this.startProcessTime = startProcessTime;
    }
    public Long getStatus() {
        return this.status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public Date getSysCreateTime() {
        return this.sysCreateTime;
    }
    public void setSysCreateTime(Date sysCreateTime) {
        this.sysCreateTime = sysCreateTime;
    }
    public Date getSysProcessTime() {
        return this.sysProcessTime;
    }
    public void setSysProcessTime(Date sysProcessTime) {
        this.sysProcessTime = sysProcessTime;
    }
    public String getToSerial() {
        return this.toSerial;
    }
    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }
    public Long getTransId() {
        return this.transId;
    }
    public void setTransId(Long transId) {
        this.transId = transId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
