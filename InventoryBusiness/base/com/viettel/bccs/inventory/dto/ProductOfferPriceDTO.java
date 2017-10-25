package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class ProductOfferPriceDTO extends BaseDTO implements Serializable {
    private Long productOfferPriceId;
    private Long prodOfferId;
    private Long pricePolicyId;
    private Long priceTypeId;
    private String name;
    private String description;
    private Long price;
    private Long vat;
    private Long pledgeAmount;
    private Long pledgeTime;
    private Long priorPay;
    private String status;
    private Date effectDatetime;
    private Date expireDatetime;
    private Long priority;
    private String effectType;
    private String cronExpression;
    private String createUser;
    private Date createDatetime;
    private String updateUser;
    private Date updateDatetime;


    public String getKeySet() {
        return keySet;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEffectDatetime() {
        return this.effectDatetime;
    }

    public void setEffectDatetime(Date effectDatetime) {
        this.effectDatetime = effectDatetime;
    }

    public String getEffectType() {
        return this.effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public Date getExpireDatetime() {
        return this.expireDatetime;
    }

    public void setExpireDatetime(Date expireDatetime) {
        this.expireDatetime = expireDatetime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPledgeAmount() {
        return this.pledgeAmount;
    }

    public void setPledgeAmount(Long pledgeAmount) {
        this.pledgeAmount = pledgeAmount;
    }

    public Long getPledgeTime() {
        return this.pledgeTime;
    }

    public void setPledgeTime(Long pledgeTime) {
        this.pledgeTime = pledgeTime;
    }

    public Long getPrice() {
        return this.price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getPricePolicyId() {
        return this.pricePolicyId;
    }

    public void setPricePolicyId(Long pricePolicyId) {
        this.pricePolicyId = pricePolicyId;
    }

    public Long getPriceTypeId() {
        return this.priceTypeId;
    }

    public void setPriceTypeId(Long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public Long getPriorPay() {
        return this.priorPay;
    }

    public void setPriorPay(Long priorPay) {
        this.priorPay = priorPay;
    }

    public Long getPriority() {
        return this.priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getProductOfferPriceId() {
        return this.productOfferPriceId;
    }

    public void setProductOfferPriceId(Long productOfferPriceId) {
        this.productOfferPriceId = productOfferPriceId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Long getVat() {
        return this.vat;
    }

    public void setVat(Long vat) {
        this.vat = vat;
    }
}
