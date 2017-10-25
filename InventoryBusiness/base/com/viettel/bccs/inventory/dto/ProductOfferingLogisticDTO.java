package com.viettel.bccs.inventory.dto;

/**
 * Created by hoangnt14 on 7/28/2016.
 */
public class ProductOfferingLogisticDTO {
    private Long stockModelId;
    private String goodCode;
    private String goodName;
    private Long amount;
    private Long goodState;
    private String goodUnitName;
    private String transferGoodCode;

    public String getTransferGoodCode() {
        return transferGoodCode;
    }

    public void setTransferGoodCode(String transferGoodCode) {
        this.transferGoodCode = transferGoodCode;
    }

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public String getGoodCode() {
        return goodCode;
    }

    public void setGoodCode(String goodCode) {
        this.goodCode = goodCode;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getGoodState() {
        return goodState;
    }

    public void setGoodState(Long goodState) {
        this.goodState = goodState;
    }

    public String getGoodUnitName() {
        return goodUnitName;
    }

    public void setGoodUnitName(String goodUnitName) {
        this.goodUnitName = goodUnitName;
    }
}
