package com.viettel.bccs.inventory.dto;

import java.lang.Long;

import com.viettel.bccs.inventory.model.StockIsdnVtShopFeeId;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockIsdnVtShopFeeDTO extends BaseDTO implements Serializable {

    private StockIsdnVtShopFeeId stockIsdnVtShopFeeId;
    private Long amount;
    private String payStatus;

    public StockIsdnVtShopFeeDTO() {
        stockIsdnVtShopFeeId = new StockIsdnVtShopFeeId();
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public StockIsdnVtShopFeeId getStockIsdnVtShopFeeId() {
        return stockIsdnVtShopFeeId;
    }

    public void setStockIsdnVtShopFeeId(StockIsdnVtShopFeeId stockIsdnVtShopFeeId) {
        this.stockIsdnVtShopFeeId = stockIsdnVtShopFeeId;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
