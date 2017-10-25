package com.viettel.bccs.inventory.dto;

/**
 * Created by sinhhv on 1/11/2016.
 */
public class ManageStockTransViewDetailDto {
    String fromNumber;
    String toNumber;
    String quantity;

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
