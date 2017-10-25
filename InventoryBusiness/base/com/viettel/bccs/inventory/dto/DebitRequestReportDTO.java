package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

/**
 * Created by hoangnt14 on 12/16/2016.
 */
public class DebitRequestReportDTO extends BaseDTO {
    private String staffBCCS;
    private String staffName;
    private String channelName;
    private Long revenueInMonth;
    private String distance;
    private String debitDayType;
    private Long debitValuePromotion;
    private Long debitValueUsual;
    private String paymentDayType;
    private Long paymentValuePromotion;
    private Long paymentValueUsual;
    private Long paymentValueHoliday;
    private String paymentGroupServiceName;
    private String note;
    private Long ownerId;
    private String ownerType;

    public String getStaffBCCS() {
        return staffBCCS;
    }

    public void setStaffBCCS(String staffBCCS) {
        this.staffBCCS = staffBCCS;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Long getRevenueInMonth() {
        return revenueInMonth;
    }

    public void setRevenueInMonth(Long revenueInMonth) {
        this.revenueInMonth = revenueInMonth;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDebitDayType() {
        return debitDayType;
    }

    public void setDebitDayType(String debitDayType) {
        this.debitDayType = debitDayType;
    }

    public Long getDebitValuePromotion() {
        return debitValuePromotion;
    }

    public void setDebitValuePromotion(Long debitValuePromotion) {
        this.debitValuePromotion = debitValuePromotion;
    }

    public Long getDebitValueUsual() {
        return debitValueUsual;
    }

    public void setDebitValueUsual(Long debitValueUsual) {
        this.debitValueUsual = debitValueUsual;
    }

    public String getPaymentDayType() {
        return paymentDayType;
    }

    public void setPaymentDayType(String paymentDayType) {
        this.paymentDayType = paymentDayType;
    }

    public Long getPaymentValuePromotion() {
        return paymentValuePromotion;
    }

    public void setPaymentValuePromotion(Long paymentValuePromotion) {
        this.paymentValuePromotion = paymentValuePromotion;
    }

    public Long getPaymentValueUsual() {
        return paymentValueUsual;
    }

    public void setPaymentValueUsual(Long paymentValueUsual) {
        this.paymentValueUsual = paymentValueUsual;
    }

    public String getPaymentGroupServiceName() {
        return paymentGroupServiceName;
    }

    public void setPaymentGroupServiceName(String paymentGroupServiceName) {
        this.paymentGroupServiceName = paymentGroupServiceName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getPaymentValueHoliday() {
        return paymentValueHoliday;
    }

    public void setPaymentValueHoliday(Long paymentValueHoliday) {
        this.paymentValueHoliday = paymentValueHoliday;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
}
