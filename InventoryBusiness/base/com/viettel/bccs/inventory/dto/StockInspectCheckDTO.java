package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;

/**
 * Created by DungPT16 on 2/19/2016.
 */
public class StockInspectCheckDTO extends BaseDTO implements Serializable {

    private Long productOfferTypeId;
    private Long prodOfferId;
    private String productName;
    private String productCode;
    private Long quantity;
    private Long currentQuantity;
    private Long availableQuantity;
    private Long quantityStockCheck;
    private Long quantityPoor;
    private Long quanittyReal;
    private Long stateId;
    private String stateName;
    private String fromSerial;
    private String toSerial;
    private Long stockInspectRealId;
    private Long ownerType;
    private Long ownerId;
    private String approveNote;
    private String note;
    private boolean disableQuantityPoor;


    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Long currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Long getQuantityStockCheck() {
        return quantityStockCheck;
    }

    public void setQuantityStockCheck(Long quantityStockCheck) {
        this.quantityStockCheck = quantityStockCheck;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSerial() {
        return toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public Long getStockInspectRealId() {
        return stockInspectRealId;
    }

    public void setStockInspectRealId(Long stockInspectRealId) {
        this.stockInspectRealId = stockInspectRealId;
    }

    public Long getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getQuantityPoor() {
        return quantityPoor;
    }

    public void setQuantityPoor(Long quantityPoor) {
        this.quantityPoor = quantityPoor;
    }

    public String getApproveNote() {
        return approveNote;
    }

    public void setApproveNote(String approveNote) {
        this.approveNote = approveNote;
    }

    public Long getQuanittyReal() {
        return quanittyReal;
    }

    public void setQuanittyReal(Long quanittyReal) {
        this.quanittyReal = quanittyReal;
    }

    public boolean isDisableQuantityPoor() {
        return disableQuantityPoor;
    }

    public void setDisableQuantityPoor(boolean disableQuantityPoor) {
        this.disableQuantityPoor = disableQuantityPoor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
