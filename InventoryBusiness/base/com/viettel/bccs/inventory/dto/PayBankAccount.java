package com.viettel.bccs.inventory.dto;

import java.io.Serializable;

/**
 * Created by PM2-LAMNV5 on 1/9/2016.
 */
public class PayBankAccount implements Serializable {
    private String bankCode;
    private String isdn;

    public PayBankAccount() {
    }

    public PayBankAccount(String bankCode, String isdn) {
        this.bankCode = bankCode;
        this.isdn = isdn;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }
}
