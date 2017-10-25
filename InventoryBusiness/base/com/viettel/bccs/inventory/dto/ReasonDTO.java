package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;

public class ReasonDTO extends BaseDTO implements Serializable {

    private Long haveMoney;
    private String reasonCode;
    private String reasonDescription;
    private Long reasonId;
    private String reasonName;
    private String reasonStatus;
    private String reasonType;
    private String service;

    public String getKeySet() {
        return keySet; }
    public Long getHaveMoney() {
        return this.haveMoney;
    }
    public void setHaveMoney(Long haveMoney) {
        this.haveMoney = haveMoney;
    }
    public String getReasonCode() {
        return this.reasonCode;
    }
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
    public String getReasonDescription() {
        return this.reasonDescription;
    }
    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }
    public Long getReasonId() {
        return this.reasonId;
    }
    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }
    public String getReasonName() {
        return this.reasonName;
    }
    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }
    public String getReasonStatus() {
        return this.reasonStatus;
    }
    public void setReasonStatus(String reasonStatus) {
        this.reasonStatus = reasonStatus;
    }
    public String getReasonType() {
        return this.reasonType;
    }
    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }
    public String getService() {
        return this.service;
    }
    public void setService(String service) {
        this.service = service;
    }
}
