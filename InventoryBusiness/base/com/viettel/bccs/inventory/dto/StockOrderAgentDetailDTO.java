package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockOrderAgentDetailDTO extends BaseDTO implements Serializable {
    private Date createDate;
    private String note;
    private Long prodOfferId;
    private Long quantity;
    private Long stateId;
    private Long stockOrderAgentDetailId;
    private long stockOrderAgentId;
    private String requestCode;
    private String prodOfferName;

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public String getKeySet() {
        return keySet;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getStockOrderAgentDetailId() {
        return this.stockOrderAgentDetailId;
    }

    public void setStockOrderAgentDetailId(Long stockOrderAgentDetailId) {
        this.stockOrderAgentDetailId = stockOrderAgentDetailId;
    }

    public long getStockOrderAgentId() {
        return this.stockOrderAgentId;
    }

    public void setStockOrderAgentId(long stockOrderAgentId) {
        this.stockOrderAgentId = stockOrderAgentId;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }
}
