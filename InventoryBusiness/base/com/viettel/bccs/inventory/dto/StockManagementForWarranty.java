package com.viettel.bccs.inventory.dto;


import java.io.Serializable;

/**
 * BO xu ly cac ham ve quan ly kho de phuc vu cho he thong Bao Hanh
 *
 * @author thanhnt77
 */
public class StockManagementForWarranty implements Serializable {

    private String ownerCode;
    private String ownerName;
    private String stockModelCode;
    private String stockModelName;
    private String stateName;
    private Long stateId;
    private Long ownerId;
    private Long ownerType;
    private Long stockModelId;
    private Long quantity;

    public StockManagementForWarranty() {
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStockModelCode() {
        return stockModelCode;
    }

    public void setStockModelCode(String stockModelCode) {
        this.stockModelCode = stockModelCode;
    }

    public String getStockModelName() {
        return stockModelName;
    }

    public void setStockModelName(String stockModelName) {
        this.stockModelName = stockModelName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
