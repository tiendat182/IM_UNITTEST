package com.viettel.bccs.im1.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockTransSerialIm1DTO extends BaseDTO implements Serializable {
 private Long stockTransSerialId;
    private Long stateId;
    private Long stockTransId;
    private Long stockModelId;
    private String fromSerial;
    private String toSerial;
    private Long quantity;
    private Date createDatetime;
    private Date demoDate;
    private Long lengthIsdn;
    private String typeIsdn;
    private Long valid;
    private String serialGpon;
    private String tvmsCadId;
    private String tvmsMacId;
    public String getKeySet() {
        return keySet;
    }
    public Long getStockTransSerialId() {
        return this.stockTransSerialId;
    }
    public void setStockTransSerialId(Long stockTransSerialId) {
        this.stockTransSerialId = stockTransSerialId;
    }
    public Long getStateId() {
        return this.stateId;
    }
    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
    public Long getStockTransId() {
        return this.stockTransId;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
    public Long getStockModelId() {
        return this.stockModelId;
    }
    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }
    public String getFromSerial() {
        return this.fromSerial;
    }
    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }
    public String getToSerial() {
        return this.toSerial;
    }
    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }
    public Long getQuantity() {
        return this.quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    public Date getDemoDate() {
        return this.demoDate;
    }
    public void setDemoDate(Date demoDate) {
        this.demoDate = demoDate;
    }
    public Long getLengthIsdn() {
        return this.lengthIsdn;
    }
    public void setLengthIsdn(Long lengthIsdn) {
        this.lengthIsdn = lengthIsdn;
    }
    public String getTypeIsdn() {
        return this.typeIsdn;
    }
    public void setTypeIsdn(String typeIsdn) {
        this.typeIsdn = typeIsdn;
    }
    public Long getValid() {
        return this.valid;
    }
    public void setValid(Long valid) {
        this.valid = valid;
    }

    public String getSerialGpon() {
        return serialGpon;
    }

    public void setSerialGpon(String serialGpon) {
        this.serialGpon = serialGpon;
    }

    public String getTvmsCadId() {
        return tvmsCadId;
    }

    public void setTvmsCadId(String tvmsCadId) {
        this.tvmsCadId = tvmsCadId;
    }

    public String getTvmsMacId() {
        return tvmsMacId;
    }

    public void setTvmsMacId(String tvmsMacId) {
        this.tvmsMacId = tvmsMacId;
    }
}
