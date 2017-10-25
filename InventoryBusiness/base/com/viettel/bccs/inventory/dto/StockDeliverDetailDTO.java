package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockDeliverDetailDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Date createDate;
    private String note;
    private Long prodOfferId;
    private Long prodOfferTypeId;
    private Long quantityReal;
    private Long quantitySys;
    private Long stateId;
    private Long stockDeliverDetailId;
    private Long stockDeliverId;
    private String productName;
    private String stateName;
    private String productCode;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getProdOfferTypeId() {
        return this.prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public Long getQuantityReal() {
        return this.quantityReal;
    }

    public void setQuantityReal(Long quantityReal) {
        this.quantityReal = quantityReal;
    }

    public Long getQuantitySys() {
        return this.quantitySys;
    }

    public void setQuantitySys(Long quantitySys) {
        this.quantitySys = quantitySys;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getStockDeliverDetailId() {
        return this.stockDeliverDetailId;
    }

    public void setStockDeliverDetailId(Long stockDeliverDetailId) {
        this.stockDeliverDetailId = stockDeliverDetailId;
    }

    public Long getStockDeliverId() {
        return this.stockDeliverId;
    }

    public void setStockDeliverId(Long stockDeliverId) {
        this.stockDeliverId = stockDeliverId;
    }
}
