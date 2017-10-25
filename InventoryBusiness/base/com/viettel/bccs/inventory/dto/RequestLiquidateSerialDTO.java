package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class RequestLiquidateSerialDTO extends BaseDTO implements Serializable {

    private Date createDatetime;
    private String fromSerial;
    private Long quantity;
    private Long requestLiquidateId;
    private Long requestLiquidateSerialId;
    private Long requestLiquidateDetailId;
    private String toSerial;

    public String getKeySet() {
        return keySet;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getFromSerial() {
        return this.fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getRequestLiquidateId() {
        return this.requestLiquidateId;
    }

    public void setRequestLiquidateId(Long requestLiquidateId) {
        this.requestLiquidateId = requestLiquidateId;
    }

    public Long getRequestLiquidateSerialId() {
        return this.requestLiquidateSerialId;
    }

    public void setRequestLiquidateSerialId(Long requestLiquidateSerialId) {
        this.requestLiquidateSerialId = requestLiquidateSerialId;
    }

    public Long getRequestLiquidateDetailId() {
        return requestLiquidateDetailId;
    }

    public void setRequestLiquidateDetailId(Long requestLiquidateDetailId) {
        this.requestLiquidateDetailId = requestLiquidateDetailId;
    }

    public String getToSerial() {
        return this.toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }
}
