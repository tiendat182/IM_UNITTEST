package com.viettel.bccs.inventory.dto;

import java.util.Date;

/**
 * Created by sinhhv on 1/11/2016.
 */
public class ManageTransStockDto {
    String transCode;
    Date fromDate;
    Date toDate;
    String typeReceiveOwner;
    String receiveOwnerStockId;
    String typeTakeOwner;
    String takeOwnerStockId;
    String ownerCreateId;
    String notes;
    String receiveShopId;
    String fromShopId;
    Long reasonId;
    String isdn;

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
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

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getTypeReceiveOwner() {
        return typeReceiveOwner;
    }

    public void setTypeReceiveOwner(String typeReceiveOwner) {
        this.typeReceiveOwner = typeReceiveOwner;
    }

    public String getReceiveOwnerStockId() {
        return receiveOwnerStockId;
    }

    public void setReceiveOwnerStockId(String receiveOwnerStockId) {
        this.receiveOwnerStockId = receiveOwnerStockId;
    }

    public String getTypeTakeOwner() {
        return typeTakeOwner;
    }

    public void setTypeTakeOwner(String typeTakeOwner) {
        this.typeTakeOwner = typeTakeOwner;
    }

    public String getTakeOwnerStockId() {
        return takeOwnerStockId;
    }

    public void setTakeOwnerStockId(String takeOwnerStockId) {
        this.takeOwnerStockId = takeOwnerStockId;
    }

    public String getOwnerCreateId() {
        return ownerCreateId;
    }

    public void setOwnerCreateId(String ownerCreateId) {
        this.ownerCreateId = ownerCreateId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReceiveShopId() {
        return receiveShopId;
    }

    public void setReceiveShopId(String receiveShopId) {
        this.receiveShopId = receiveShopId;
    }

    public String getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(String fromShopId) {
        this.fromShopId = fromShopId;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }
}
