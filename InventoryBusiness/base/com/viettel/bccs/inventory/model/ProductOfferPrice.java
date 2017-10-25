/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author thetm1
 */
@Entity
@Table(name = "PRODUCT_OFFER_PRICE")
@NamedQueries({
    @NamedQuery(name = "ProductOfferPrice.findAll", query = "SELECT p FROM ProductOfferPrice p")})
public class ProductOfferPrice implements Serializable {
public static enum COLUMNS {CREATEDATETIME, CREATEUSER, CRONEXPRESSION, DESCRIPTION, EFFECTDATETIME, EFFECTTYPE, EXPIREDATETIME, NAME, PLEDGEAMOUNT, PLEDGETIME, PRICE, PRICEPOLICYID, PRICETYPEID, PRIORPAY, PRIORITY, PRODOFFERID, PRODUCTOFFERPRICEID, STATUS, UPDATEDATETIME, UPDATEUSER, VAT, EXCLUSE_ID_LIST};

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "PRODUCT_OFFER_PRICE_ID")
    @SequenceGenerator(name = "PRODUCT_OFFER_PRICE_ID_GENERATOR", sequenceName = "PRODUCT_OFFER_PRICE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_OFFER_PRICE_ID_GENERATOR")
    private Long productOfferPriceId;
    @Basic(optional = false)
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Basic(optional = false)
    @Column(name = "PRICE_POLICY_ID")
    private Long pricePolicyId;
    @Basic(optional = false)
    @Column(name = "PRICE_TYPE_ID")
    private Long priceTypeId;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PRICE")
    private Long price;
    @Column(name = "VAT")
    private Long vat;
    @Column(name = "PLEDGE_AMOUNT")
    private Long pledgeAmount;
    @Column(name = "PLEDGE_TIME")
    private Long pledgeTime;
    @Column(name = "PRIOR_PAY")
    private Long priorPay;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "EFFECT_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectDatetime;
    @Column(name = "EXPIRE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireDatetime;
    @Column(name = "PRIORITY")
    private Long priority;
    @Column(name = "EFFECT_TYPE")
    private String effectType;
    @Column(name = "CRON_EXPRESSION")
    private String cronExpression;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Basic(optional = false)
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Basic(optional = false)
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Basic(optional = false)
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;

    public ProductOfferPrice() {
    }

    public ProductOfferPrice(Long productOfferPriceId) {
        this.productOfferPriceId = productOfferPriceId;
    }

    public ProductOfferPrice(Long productOfferPriceId, Long prodOfferId, Long pricePolicyId, Long priceTypeId, String name, Date createDatetime, String updateUser, Date updateDatetime) {
        this.productOfferPriceId = productOfferPriceId;
        this.prodOfferId = prodOfferId;
        this.pricePolicyId = pricePolicyId;
        this.priceTypeId = priceTypeId;
        this.name = name;
        this.createDatetime = createDatetime;
        this.updateUser = updateUser;
        this.updateDatetime = updateDatetime;
    }

    public Long getProductOfferPriceId() {
        return productOfferPriceId;
    }

    public void setProductOfferPriceId(Long productOfferPriceId) {
        this.productOfferPriceId = productOfferPriceId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getPricePolicyId() {
        return pricePolicyId;
    }

    public void setPricePolicyId(Long pricePolicyId) {
        this.pricePolicyId = pricePolicyId;
    }

    public Long getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(Long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getVat() {
        return vat;
    }

    public void setVat(Long vat) {
        this.vat = vat;
    }

    public Long getPledgeAmount() {
        return pledgeAmount;
    }

    public void setPledgeAmount(Long pledgeAmount) {
        this.pledgeAmount = pledgeAmount;
    }

    public Long getPledgeTime() {
        return pledgeTime;
    }

    public void setPledgeTime(Long pledgeTime) {
        this.pledgeTime = pledgeTime;
    }

    public Long getPriorPay() {
        return priorPay;
    }

    public void setPriorPay(Long priorPay) {
        this.priorPay = priorPay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getEffectDatetime() {
        return effectDatetime;
    }

    public void setEffectDatetime(Date effectDatetime) {
        this.effectDatetime = effectDatetime;
    }

    public Date getExpireDatetime() {
        return expireDatetime;
    }

    public void setExpireDatetime(Date expireDatetime) {
        this.expireDatetime = expireDatetime;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productOfferPriceId != null ? productOfferPriceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductOfferPrice)) {
            return false;
        }
        ProductOfferPrice other = (ProductOfferPrice) object;
        if ((this.productOfferPriceId == null && other.productOfferPriceId != null) || (this.productOfferPriceId != null && !this.productOfferPriceId.equals(other.productOfferPriceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.ProductOfferPrice[ productOfferPriceId=" + productOfferPriceId + " ]";
    }
    
}
