package com.viettel.bccs.cc.dto;

import java.io.Serializable;

/**
 * Created by duongtv10 on 10/31/2015.
 */
public class CardInfoProvisioningDTO implements Serializable {
    private String cardExpired;
    //ngày hết hạn
    private String cardValue;
    //Mệnh giá
    private String cardSuspended;
    //trạng thái
    private String pinNumber;
    //Số pin
    private String isdn;
    //Số thuê bao
    private String dateUsed;
    //Ngày nạp
    private String responeCode;
    private String valueCard;
    private boolean result;
    private String errorDescription;

    public String getCardExpired() {
        return cardExpired;
    }

    public void setCardExpired(String cardExpired) {
        this.cardExpired = cardExpired;
    }

    public String getCardValue() {
        return cardValue;
    }

    public void setCardValue(String cardValue) {
        this.cardValue = cardValue;
    }

    public String getCardSuspended() {
        return cardSuspended;
    }

    public void setCardSuspended(String cardSuspended) {
        this.cardSuspended = cardSuspended;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getDateUsed() {
        return dateUsed;
    }

    public void setDateUsed(String dateUsed) {
        this.dateUsed = dateUsed;
    }

    public String getResponeCode() {
        return responeCode;
    }

    public void setResponeCode(String responeCode) {
        this.responeCode = responeCode;
    }

    public String getValueCard() {
        return valueCard;
    }

    public void setValueCard(String valueCard) {
        this.valueCard = valueCard;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

}
