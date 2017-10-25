package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.List;

/**
 * Created by DungPT16 on 1/14/2016.
 */
public class DiscountDTO extends BaseDTO {
    private Long discountId;
    private Double discountRate; // Ty le chiet khau
    private Long discountAmount; // So tien chiet khau
    private String discountGroupName; // Ten chinh sach
    private List list;
    private Long stockModelId;
    private List listStockModels;
    private Long rate;
    private Long discountGroupId;
    private String type;
    private String discountRateString;

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountGroupName() {
        return discountGroupName;
    }

    public void setDiscountGroupName(String discountGroupName) {
        this.discountGroupName = discountGroupName;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public List getListStockModels() {
        return listStockModels;
    }

    public void setListStockModels(List listStockModels) {
        this.listStockModels = listStockModels;
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public Long getDiscountGroupId() {
        return discountGroupId;
    }

    public void setDiscountGroupId(Long discountGroupId) {
        this.discountGroupId = discountGroupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiscountRateString() {
        return discountRateString;
    }

    public void setDiscountRateString(String discountRateString) {
        this.discountRateString = discountRateString;
    }
}
