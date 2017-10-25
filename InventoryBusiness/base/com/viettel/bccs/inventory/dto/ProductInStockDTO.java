package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

/**
 * Created by Hellpoethero on 07/09/2016.
 */
public class ProductInStockDTO extends BaseDTO {
    private Long ownerId;
    private Long ownerType;
    private Long prodOfferId;
    private Long stateAfterId; //status
    private Long stateId;

    public Long getOwnerId() {return ownerId;}

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {return ownerType;}

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getStateAfterId() { return stateAfterId; }

    public void setStateAfterId(Long stateAfterId) { this.stateAfterId = stateAfterId; }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
}
