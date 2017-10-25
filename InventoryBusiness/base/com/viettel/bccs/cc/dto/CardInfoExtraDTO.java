package com.viettel.bccs.cc.dto;

/**
 * Created by tunglt16 on 4/15/2016.
 */
public class CardInfoExtraDTO {
    private String serial;
    private String chargeAmount;
    private String chargeIsdn;
    private String chargeDateTime;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getChargeIsdn() {
        return chargeIsdn;
    }

    public void setChargeIsdn(String chargeIsdn) {
        this.chargeIsdn = chargeIsdn;
    }

    public String getChargeDateTime() {
        return chargeDateTime;
    }

    public void setChargeDateTime(String chargeDateTime) {
        this.chargeDateTime = chargeDateTime;
    }
}
