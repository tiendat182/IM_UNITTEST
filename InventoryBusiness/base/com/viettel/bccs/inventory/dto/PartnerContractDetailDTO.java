package com.viettel.bccs.inventory.dto;
import java.lang.Long;
import java.util.Date;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
public class PartnerContractDetailDTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private Date createDate;
    private String currencyType;
    private String orderCode;
    private Long partnerContractDetailId;
    private Long partnerContractId;
    private Long prodOfferId;
    private Long quantity;
    private Date startDateWarranty;
    private Long stateId;
    private Long unitPrice;
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCurrencyType() {
        return this.currencyType;
    }
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
    public String getOrderCode() {
        return this.orderCode;
    }
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    public Long getPartnerContractDetailId() {
        return this.partnerContractDetailId;
    }
    public void setPartnerContractDetailId(Long partnerContractDetailId) {
        this.partnerContractDetailId = partnerContractDetailId;
    }
    public Long getPartnerContractId() {
        return this.partnerContractId;
    }
    public void setPartnerContractId(Long partnerContractId) {
        this.partnerContractId = partnerContractId;
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
    public Date getStartDateWarranty() {
        return this.startDateWarranty;
    }
    public void setStartDateWarranty(Date startDateWarranty) {
        this.startDateWarranty = startDateWarranty;
    }
    public Long getStateId() {
        return this.stateId;
    }
    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
    public Long getUnitPrice() {
        return this.unitPrice;
    }
    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }
}
