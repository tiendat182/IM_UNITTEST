package com.viettel.bccs.inventory.dto;

/**
 * Created by hoangnt14 on 7/14/2016.
 */
public class Serial {
    private String fromSerial;
    private String toSerial;
    private String quantity;
    private String a3A8;
    private String goodKind;
    private String simCode;

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

    public String getA3A8() {
        return a3A8;
    }

    public void setA3A8(String a3A8) {
        this.a3A8 = a3A8;
    }

    public String getGoodKind() {
        return goodKind;
    }

    public void setGoodKind(String goodKind) {
        this.goodKind = goodKind;
    }

    public String getSimCode() {
        return simCode;
    }

    public void setSimCode(String simCode) {
        this.simCode = simCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
