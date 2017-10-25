package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockTotalCycleReportDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private String createDatetime;

    private Long quantity;
    private Long quantityCycle;
    private Long quantityCycle1;
    private Long quantityCycle2;
    private Long quantityCycle3;
    private Long quantityCycle4;
    private Long quantityOver;

    private Long retailPriceQuantityCycle;
    private Long retailPriceQuantityCycle1;
    private Long retailPriceQuantityCycle2;
    private Long retailPriceQuantityCycle3;
    private Long retailPriceQuantityCycle4;

    private Long priceCostQuantityCycle;
    private Long priceCostQuantityCycle1;
    private Long priceCostQuantityCycle2;
    private Long priceCostQuantityCycle3;
    private Long priceCostQuantityCycle4;

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getQuantityCycle() {
        return quantityCycle;
    }

    public void setQuantityCycle(Long quantityCycle) {
        this.quantityCycle = quantityCycle;
    }

    public Long getRetailPriceQuantityCycle() {
        return retailPriceQuantityCycle;
    }

    public void setRetailPriceQuantityCycle(Long retailPriceQuantityCycle) {
        this.retailPriceQuantityCycle = retailPriceQuantityCycle;
    }

    public Long getPriceCostQuantityCycle() {
        return priceCostQuantityCycle;
    }

    public void setPriceCostQuantityCycle(Long priceCostQuantityCycle) {
        this.priceCostQuantityCycle = priceCostQuantityCycle;
    }

    public Long getQuantityCycle1() {
        return quantityCycle1;
    }

    public void setQuantityCycle1(Long quantityCycle1) {
        this.quantityCycle1 = quantityCycle1;
    }

    public Long getQuantityCycle2() {
        return quantityCycle2;
    }

    public void setQuantityCycle2(Long quantityCycle2) {
        this.quantityCycle2 = quantityCycle2;
    }

    public Long getQuantityCycle3() {
        return quantityCycle3;
    }

    public void setQuantityCycle3(Long quantityCycle3) {
        this.quantityCycle3 = quantityCycle3;
    }

    public Long getQuantityCycle4() {
        return quantityCycle4;
    }

    public void setQuantityCycle4(Long quantityCycle4) {
        this.quantityCycle4 = quantityCycle4;
    }

    public Long getRetailPriceQuantityCycle1() {
        return retailPriceQuantityCycle1;
    }

    public void setRetailPriceQuantityCycle1(Long retailPriceQuantityCycle1) {
        this.retailPriceQuantityCycle1 = retailPriceQuantityCycle1;
    }

    public Long getRetailPriceQuantityCycle2() {
        return retailPriceQuantityCycle2;
    }

    public void setRetailPriceQuantityCycle2(Long retailPriceQuantityCycle2) {
        this.retailPriceQuantityCycle2 = retailPriceQuantityCycle2;
    }

    public Long getRetailPriceQuantityCycle3() {
        return retailPriceQuantityCycle3;
    }

    public void setRetailPriceQuantityCycle3(Long retailPriceQuantityCycle3) {
        this.retailPriceQuantityCycle3 = retailPriceQuantityCycle3;
    }

    public Long getRetailPriceQuantityCycle4() {
        return retailPriceQuantityCycle4;
    }

    public void setRetailPriceQuantityCycle4(Long retailPriceQuantityCycle4) {
        this.retailPriceQuantityCycle4 = retailPriceQuantityCycle4;
    }

    public Long getPriceCostQuantityCycle1() {
        return priceCostQuantityCycle1;
    }

    public void setPriceCostQuantityCycle1(Long priceCostQuantityCycle1) {
        this.priceCostQuantityCycle1 = priceCostQuantityCycle1;
    }

    public Long getPriceCostQuantityCycle2() {
        return priceCostQuantityCycle2;
    }

    public void setPriceCostQuantityCycle2(Long priceCostQuantityCycle2) {
        this.priceCostQuantityCycle2 = priceCostQuantityCycle2;
    }

    public Long getPriceCostQuantityCycle3() {
        return priceCostQuantityCycle3;
    }

    public void setPriceCostQuantityCycle3(Long priceCostQuantityCycle3) {
        this.priceCostQuantityCycle3 = priceCostQuantityCycle3;
    }

    public Long getPriceCostQuantityCycle4() {
        return priceCostQuantityCycle4;
    }

    public void setPriceCostQuantityCycle4(Long priceCostQuantityCycle4) {
        this.priceCostQuantityCycle4 = priceCostQuantityCycle4;
    }

    public Long getQuantityOver() {
        return quantityOver;
    }

    public void setQuantityOver(Long quantityOver) {
        this.quantityOver = quantityOver;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
