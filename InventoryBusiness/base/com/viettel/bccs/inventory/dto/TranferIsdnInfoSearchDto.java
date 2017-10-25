package com.viettel.bccs.inventory.dto;

import java.util.Date;

/**
 * Created by sinhhv on 1/12/2016.
 */
public class TranferIsdnInfoSearchDto {
    String transCode;
    String status;
    VShopStaffDTO ownerDeliverStock;
    VShopStaffDTO ownerReceiveStock;
    Date fromDate;
    Date toDate;

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public VShopStaffDTO getOwnerDeliverStock() {
        return ownerDeliverStock;
    }

    public void setOwnerDeliverStock(VShopStaffDTO ownerDeliverStock) {
        this.ownerDeliverStock = ownerDeliverStock;
    }

    public VShopStaffDTO getOwnerReceiveStock() {
        return ownerReceiveStock;
    }

    public void setOwnerReceiveStock(VShopStaffDTO ownerReceiveStock) {
        this.ownerReceiveStock = ownerReceiveStock;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
