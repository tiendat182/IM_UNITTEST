package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class MtDTO extends BaseDTO implements Serializable {

    private String appId;
    private String appName;
    private String channel;
    private String message;
    private Long moHisId;
    private String msisdn;
    private Long mtId;
    private Date receiveTime;
    private Long retryNum;

    public String getKeySet() {
        return keySet; }
    public String getAppId() {
        return this.appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getAppName() {
        return this.appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getChannel() {
        return this.channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Long getMoHisId() {
        return this.moHisId;
    }
    public void setMoHisId(Long moHisId) {
        this.moHisId = moHisId;
    }
    public String getMsisdn() {
        return this.msisdn;
    }
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    public Long getMtId() {
        return this.mtId;
    }
    public void setMtId(Long mtId) {
        this.mtId = mtId;
    }
    public Date getReceiveTime() {
        return this.receiveTime;
    }
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }
    public Long getRetryNum() {
        return this.retryNum;
    }
    public void setRetryNum(Long retryNum) {
        this.retryNum = retryNum;
    }
}
