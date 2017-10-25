package com.viettel.bccs.sale.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class SubscriberDTO extends BaseDTO implements Serializable {
    private Long subId;
    private String isdn;
    private String serial;
    private String productCode;
    private Long status;
    private String actStatus;
    private Date staDateTime;
    private String reasonCode;
    private String regTypeID;

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getSerial() {
        return serial;
    }

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String ReasonDTO() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getActStatus() {
        return actStatus;
    }

    public void setActStatus(String actStatus) {
        this.actStatus = actStatus;
    }

    public Date getStaDateTime() {
        return staDateTime;
    }

    public void setStaDateTime(Date staDateTime) {
        this.staDateTime = staDateTime;
    }

    public String getRegTypeID() {
        return regTypeID;
    }

    public void setRegTypeID(String regTypeID) {
        this.regTypeID = regTypeID;
    }
}

