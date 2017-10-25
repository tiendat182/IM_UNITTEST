package com.viettel.bccs.inventory.dto;

import java.lang.Long;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class ChangeDamagedProductDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Long productOfferTypeId;
    private Long prodOfferId;
    private Long reasonId;
    private String oldSerial;
    private String newSerial;
    private String typeImport;
    // Thong tin them
    private String productCode;
    private String productName;
    private String ipAddress;

    public Long getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getOldSerial() {
        return oldSerial;
    }

    public void setOldSerial(String oldSerial) {
        this.oldSerial = oldSerial;
    }

    public String getNewSerial() {
        return newSerial;
    }

    public void setNewSerial(String newSerial) {
        this.newSerial = newSerial;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTypeImport() {
        return typeImport;
    }

    public void setTypeImport(String typeImport) {
        this.typeImport = typeImport;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
