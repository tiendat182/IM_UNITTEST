package com.viettel.bccs.inventory.dto;

import java.util.List;

/**
 * Created by hoangnt14 on 7/14/2016.
 */
public class GoodsDTO {
    private String goodsCode;
    private String goodsName;
    private String amountOrder;
    private String amountReal;
    private String goodsUnitName;
    private String goodsState;
    private Long stockModelId;
    private Long checkSerial;
    private List<Serial> lstSerial;
    private Long stockTransDetailOfflineId;

    public Long getStockTransDetailOfflineId() {
        return stockTransDetailOfflineId;
    }

    public void setStockTransDetailOfflineId(Long stockTransDetailOfflineId) {
        this.stockTransDetailOfflineId = stockTransDetailOfflineId;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getAmountOrder() {
        return amountOrder;
    }

    public void setAmountOrder(String amountOrder) {
        this.amountOrder = amountOrder;
    }

    public String getAmountReal() {
        return amountReal;
    }

    public void setAmountReal(String amountReal) {
        this.amountReal = amountReal;
    }

    public String getGoodsUnitName() {
        return goodsUnitName;
    }

    public void setGoodsUnitName(String goodsUnitName) {
        this.goodsUnitName = goodsUnitName;
    }

    public String getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(String goodsState) {
        this.goodsState = goodsState;
    }

    public List<Serial> getLstSerial() {
        return lstSerial;
    }

    public void setLstSerial(List<Serial> lstSerial) {
        this.lstSerial = lstSerial;
    }

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public Long getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(Long checkSerial) {
        this.checkSerial = checkSerial;
    }
}
