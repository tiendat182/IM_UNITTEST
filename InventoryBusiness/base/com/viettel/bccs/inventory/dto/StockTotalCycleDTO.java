package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockTotalCycleDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Date createDatetime;
    private String createUser;
    private Long ownerId;
    private Long ownerType;
    private Long priceCost;
    private Long prodOfferId;
    private Long productOfferTypeId;
    private Long quantity;
    private Long quantityCycle;
    private Long quantityCycle1;
    private Long quantityCycle2;
    private Long quantityCycle3;
    private Long quantityCycle4;
    private Long quantityOver;
    private Long retailPrice;
    private Long stateId;
    private Long stockTotalCycleId;
    private String shopCode;
    private String shopName;
    private String prodOfferCode;
    private String prodOfferName;
    private Long dayStockRemain;

    public StockTotalCycleDTO() {
        this.retailPrice = 0L;
        this.quantityOver = 0L;
        this.quantityCycle4 = 0L;
        this.quantityCycle3 = 0L;
        this.quantityCycle2 = 0L;
        this.quantityCycle1 = 0L;
        this.quantityCycle = 0L;
        this.quantity = 0L;
        this.priceCost = 0L;
    }

    public Long getQuantityCycle() {
        return quantityCycle;
    }

    public void setQuantityCycle(Long quantityCycle) {
        this.quantityCycle = quantityCycle;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getPriceCost() {
        return this.priceCost;
    }

    public void setPriceCost(Long priceCost) {
        this.priceCost = priceCost;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getProductOfferTypeId() {
        return this.productOfferTypeId;
    }

    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantityCycle1() {
        return this.quantityCycle1;
    }

    public void setQuantityCycle1(Long quantityCycle1) {
        this.quantityCycle1 = quantityCycle1;
    }

    public Long getQuantityCycle2() {
        return this.quantityCycle2;
    }

    public void setQuantityCycle2(Long quantityCycle2) {
        this.quantityCycle2 = quantityCycle2;
    }

    public Long getQuantityCycle3() {
        return this.quantityCycle3;
    }

    public void setQuantityCycle3(Long quantityCycle3) {
        this.quantityCycle3 = quantityCycle3;
    }

    public Long getQuantityCycle4() {
        return this.quantityCycle4;
    }

    public void setQuantityCycle4(Long quantityCycle4) {
        this.quantityCycle4 = quantityCycle4;
    }

    public Long getRetailPrice() {
        return this.retailPrice;
    }

    public void setRetailPrice(Long retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getStockTotalCycleId() {
        return this.stockTotalCycleId;
    }

    public void setStockTotalCycleId(Long stockTotalCycleId) {
        this.stockTotalCycleId = stockTotalCycleId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public Long getQuantityOver() {
        return quantityOver;
    }

    public void setQuantityOver(Long quantityOver) {
        this.quantityOver = quantityOver;
    }

    public Long getDayStockRemain() {
        return dayStockRemain;
    }

    public void setDayStockRemain(Long dayStockRemain) {
        this.dayStockRemain = dayStockRemain;
    }
}
