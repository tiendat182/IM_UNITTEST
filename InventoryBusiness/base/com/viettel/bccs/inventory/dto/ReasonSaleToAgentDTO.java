package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class ReasonSaleToAgentDTO extends BaseDTO implements Serializable {

    private String reasonCode;
    private String reasonName;
    private Long reasonId;
    private String reasonType;
    private String reasonStatus;

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public String getReasonStatus() {
        return reasonStatus;
    }

    public void setReasonStatus(String reasonStatus) {
        this.reasonStatus = reasonStatus;
    }
}
