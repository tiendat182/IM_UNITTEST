package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

/**
 * Created by hoangnt14 on 8/5/2016.
 */
public class LockupTransactionCardDTO extends BaseDTO {
    private long stt;
    private Long transId;
    private String isdn;
    private String prodOfferCode;
    private String serial;
    private String pincode;
    private String transType;
    private String transCode;
    private String createDate;
    private String timeSent;
    private String valueCard;
    private String serialFailed;
    private String serialExchange;
    private String noSent;
    private String fromSerial;
    private String toSerial;

    public long getStt() {
        return stt;
    }

    public void setStt(long stt) {
        this.stt = stt;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public String getValueCard() {
        return valueCard;
    }

    public void setValueCard(String valueCard) {
        this.valueCard = valueCard;
    }

    public String getSerialFailed() {
        return serialFailed;
    }

    public void setSerialFailed(String serialFailed) {
        this.serialFailed = serialFailed;
    }

    public String getSerialExchange() {
        return serialExchange;
    }

    public void setSerialExchange(String serialExchange) {
        this.serialExchange = serialExchange;
    }

    public String getNoSent() {
        return noSent;
    }

    public void setNoSent(String noSent) {
        this.noSent = noSent;
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
}
