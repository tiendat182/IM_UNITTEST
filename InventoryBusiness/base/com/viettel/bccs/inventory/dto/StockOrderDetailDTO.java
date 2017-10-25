package com.viettel.bccs.inventory.dto;

import java.lang.Long;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockOrderDetailDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Long quantityOrder;
    private Long quantityReal;
    private Long prodOfferId;
    private Long stockOrderDetailId;
    private Long stockOrderId;

    public Long getQuantityOrder() {
        return this.quantityOrder;
    }

    public void setQuantityOrder(Long quantityOrder) {
        this.quantityOrder = quantityOrder;
    }

    public Long getQuantityReal() {
        return this.quantityReal;
    }

    public void setQuantityReal(Long quantityReal) {
        this.quantityReal = quantityReal;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getStockOrderDetailId() {
        return this.stockOrderDetailId;
    }

    public void setStockOrderDetailId(Long stockOrderDetailId) {
        this.stockOrderDetailId = stockOrderDetailId;
    }

    public Long getStockOrderId() {
        return this.stockOrderId;
    }

    public void setStockOrderId(Long stockOrderId) {
        this.stockOrderId = stockOrderId;
    }
}
