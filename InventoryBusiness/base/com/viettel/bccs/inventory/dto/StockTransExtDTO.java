package com.viettel.bccs.inventory.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockTransExtDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private String extKey;
    private String extValue;
    private Long id;
    private Long stockTransId;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExtKey() {
        return this.extKey;
    }

    public void setExtKey(String extKey) {
        this.extKey = extKey;
    }

    public String getExtValue() {
        return this.extValue;
    }

    public void setExtValue(String extValue) {
        this.extValue = extValue;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
}
