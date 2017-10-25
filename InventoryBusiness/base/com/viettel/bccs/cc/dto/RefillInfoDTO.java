package com.viettel.bccs.cc.dto;

import java.io.Serializable;

/**
 * Created by duongtv10 on 10/31/2015.
 */

public class RefillInfoDTO implements Serializable {
    private String refillDate;
    //Ngày nạp thẻ
    private String refillHour;
    //Giờ nạp thẻ
    private String refillAmount;
    //Mệnh giá
    private String seriNo;
    //Serial
    private String pinNo;
    //Pin
    private String promAmount;
    //Khuyến mại
    private String promDateTotal;
    //Ngày khuyến mại
    private String amountBefore;
    //Tài khoán trước
    private String amountAfter;
    //Tài khoản sau khi nạp
    private String actDateBefore;
    //Ngày sử dụng trước
    private String actDateAfter;
    //Ngày sử dụng sau
    private String isdn;

    //Số thuê bao
    public RefillInfoDTO() {
        //RefillInfoDTO
    }

    public RefillInfoDTO(String refillDate, String refillHour, String refillAmount, String seriNo, String pinNo, String promAmount, String promDateTotal, String amountBefore, String amountAfter, String actDateBefore, String actDateAfter, String isdn) {
        this.refillDate = refillDate;
        this.refillHour = refillHour;
        this.refillAmount = refillAmount;
        this.seriNo = seriNo;
        this.pinNo = pinNo;
        this.promAmount = promAmount;
        this.promDateTotal = promDateTotal;
        this.amountBefore = amountBefore;
        this.amountAfter = amountAfter;
        this.actDateBefore = actDateBefore;
        this.actDateAfter = actDateAfter;
        this.isdn = isdn;
    }

    public String getRefillDate() {
        return refillDate;
    }

    public void setRefillDate(String refillDate) {
        this.refillDate = refillDate;
    }

    public String getRefillHour() {
        return refillHour;
    }

    public void setRefillHour(String refillHour) {
        this.refillHour = refillHour;
    }

    public String getRefillAmount() {
        return refillAmount;
    }

    public void setRefillAmount(String refillAmount) {
        this.refillAmount = refillAmount;
    }

    public String getSeriNo() {
        return seriNo;
    }

    public void setSeriNo(String seriNo) {
        this.seriNo = seriNo;
    }

    public String getPinNo() {
        return pinNo;
    }

    public void setPinNo(String pinNo) {
        this.pinNo = pinNo;
    }

    public String getPromAmount() {
        return promAmount;
    }

    public void setPromAmount(String promAmount) {
        this.promAmount = promAmount;
    }

    public String getPromDateTotal() {
        return promDateTotal;
    }

    public void setPromDateTotal(String promDateTotal) {
        this.promDateTotal = promDateTotal;
    }

    public String getAmountBefore() {
        return amountBefore;
    }

    public void setAmountBefore(String amountBefore) {
        this.amountBefore = amountBefore;
    }

    public String getAmountAfter() {
        return amountAfter;
    }

    public void setAmountAfter(String amountAfter) {
        this.amountAfter = amountAfter;
    }

    public String getActDateBefore() {
        return actDateBefore;
    }

    public void setActDateBefore(String actDateBefore) {
        this.actDateBefore = actDateBefore;
    }

    public String getActDateAfter() {
        return actDateAfter;
    }

    public void setActDateAfter(String actDateAfter) {
        this.actDateAfter = actDateAfter;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }
}
