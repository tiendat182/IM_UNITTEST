package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;

/**
 * Created by hoangnt14 on 8/5/2016.
 */
public class ExchangeCardBankplusDTO extends BaseDTO {

    private Date saleDate;
    private String serialError;
    private String serialChange;
    private Long prodOfferId;
    private String isdn;

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public String getSerialError() {
        return serialError;
    }

    public void setSerialError(String serialError) {
        this.serialError = serialError;
    }

    public String getSerialChange() {
        return serialChange;
    }

    public void setSerialChange(String serialChange) {
        this.serialChange = serialChange;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }
}
