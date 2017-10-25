package com.viettel.bccs.inventory.dto;

import java.io.Serializable;

public class ImpExpStockDTO implements Serializable {
    private Long prodOfferId;
    private Long stateId;
    private String fromSerial;
    private String toSerial;
    private Long quantity;
    private String serialGpon;
    private String tvmsCadId;
    private String tvmsMacId;

    public ImpExpStockDTO() {
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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
