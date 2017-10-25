package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
public class StockTransDocumentDTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private byte[] fileAttach;
    private Long stockTransDocumentId;
    private Long stockTransId;
    private Long transType;
    public byte[] getFileAttach() {
        return this.fileAttach;
    }
    public void setFileAttach(byte[] fileAttach) {
        this.fileAttach = fileAttach;
    }
    public Long getStockTransDocumentId() {
        return this.stockTransDocumentId;
    }
    public void setStockTransDocumentId(Long stockTransDocumentId) {
        this.stockTransDocumentId = stockTransDocumentId;
    }
    public Long getStockTransId() {
        return this.stockTransId;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
    public Long getTransType() {
        return this.transType;
    }
    public void setTransType(Long transType) {
        this.transType = transType;
    }
}
