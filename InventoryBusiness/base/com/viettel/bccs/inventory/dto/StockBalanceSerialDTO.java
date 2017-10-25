package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockBalanceSerialDTO extends BaseDTO implements Serializable {

    private Date createDatetime;
    private String fromSerial;
    private Long prodOfferId;
    private Long stockBalanceDetail;
    private Long stockBalanceSerialId;
    private Long stockRequestId;
    private String toSerial;
    //data for print
    private String ownerCode;
    private String ownerName;
    private String prodOfferCode;
    private String prodOfferName;
    private String quantity;

    public String getKeySet() {
        return keySet;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getFromSerial() {
        return this.fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getStockBalanceDetail() {
        return this.stockBalanceDetail;
    }

    public void setStockBalanceDetail(Long stockBalanceDetail) {
        this.stockBalanceDetail = stockBalanceDetail;
    }

    public Long getStockBalanceSerialId() {
        return this.stockBalanceSerialId;
    }

    public void setStockBalanceSerialId(Long stockBalanceSerialId) {
        this.stockBalanceSerialId = stockBalanceSerialId;
    }

    public Long getStockRequestId() {
        return this.stockRequestId;
    }

    public void setStockRequestId(Long stockRequestId) {
        this.stockRequestId = stockRequestId;
    }

    public String getToSerial() {
        return this.toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
