/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.bccs.inventory.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author hungdv24
 */
@Entity
@Table(name = "PRODUCT_OFFERING")
public class ProductOffering implements Serializable {
    public static enum COLUMNS {CODE, CREATEDATETIME, CREATEUSER, DESCRIPTION, EFFECTDATETIME, EXPIREDATETIME, NAME, PRODUCTOFFERTYPEID, PRODUCTOFFERINGID, PRODUCTSPECID, STATUS, SUBTYPE, TELECOMSERVICEID, UPDATEDATETIME, UPDATEUSER, CHECKSERIAL, ACCOUNTINGMODELCODE, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "PRODUCT_OFFERING_ID_GENERATOR", sequenceName = "PRODUCT_OFFERING_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_OFFERING_ID_GENERATOR")
    @Column(name = "PROD_OFFER_ID")
    private Long productOfferingId;
    @Column(name = "PRODUCT_SPEC_ID")
    private Long productSpecId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_OFFER_TYPE_ID")
    private Long productOfferTypeId;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "CODE")
    private String code;
    @Column(name = "SUB_TYPE")
    private String subType;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Basic(optional = false)
    @Column(name = "EFFECT_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectDatetime;
    @Basic(optional = false)
    @Column(name = "EXPIRE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireDatetime;
    @Basic(optional = false)
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
    @Column(name = "CHECK_SERIAL")
    private Long checkSerial;
    @Column(name = "ACCOUNTING_MODEL_CODE")
    private String accountingModelCode;
    @Column(name = "DAY_STOCK_REMAIN")
    private Long dayStockRemain;
    @Column(name = "PRICE_COST")
    private Long priceCost;

    public ProductOffering() {
    }

    public ProductOffering(Long productOfferingId) {
        this.productOfferingId = productOfferingId;
    }

    public ProductOffering(Long productOfferingId, long productOfferTypeId, String name, String code, String description, String status, Date effectDatetime, Date expireDatetime, String createUser, Date createDatetime, String updateUser, Date updateDatetime, Long checkSerial) {
        this.productOfferingId = productOfferingId;
        this.productOfferTypeId = productOfferTypeId;
        this.name = name;
        this.code = code;
        this.description = description;
        this.status = status;
        this.effectDatetime = effectDatetime;
        this.expireDatetime = expireDatetime;
        this.createUser = createUser;
        this.createDatetime = createDatetime;
        this.updateUser = updateUser;
        this.updateDatetime = updateDatetime;
        this.checkSerial = checkSerial;
    }

    public String getAccountingModelCode() {
        return accountingModelCode;
    }

    public void setAccountingModelCode(String accountingModelCode) {
        this.accountingModelCode = accountingModelCode;
    }

    public Long getProductOfferingId() {
        return productOfferingId;
    }

    public void setProductOfferingId(Long productOfferingId) {
        this.productOfferingId = productOfferingId;
    }

    public Long getProductSpecId() {
        return productSpecId;
    }

    public void setProductSpecId(Long productSpecId) {
        this.productSpecId = productSpecId;
    }

    public long getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(Long checkSerial) {
        this.checkSerial = checkSerial;
    }

    public Long getDayStockRemain() {
        return dayStockRemain;
    }

    public void setDayStockRemain(Long dayStockRemain) {
        this.dayStockRemain = dayStockRemain;
    }

    public Long getPriceCost() {
        return priceCost;
    }

    public void setPriceCost(Long priceCost) {
        this.priceCost = priceCost;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productOfferingId != null ? productOfferingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductOffering)) {
            return false;
        }
        ProductOffering other = (ProductOffering) object;
        if ((this.productOfferingId == null && other.productOfferingId != null) || (this.productOfferingId != null && !this.productOfferingId.equals(other.productOfferingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "abc.ProductOffering[ productOfferingId=" + productOfferingId + " ]";
    }

}