package com.viettel.bccs.inventory.dto;

/**
 * Created by hoangnt14 on 7/14/2016.
 */
public class OrderObjectDTO {
    private String orderCode;
    private String reason;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
