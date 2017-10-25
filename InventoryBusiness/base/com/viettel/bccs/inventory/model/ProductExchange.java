package com.viettel.bccs.inventory.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * @author Dungha7
 */
@Entity
@Table(name = "PRODUCT_EXCHANGE")
public class ProductExchange implements Serializable {
    public static enum COLUMNS {DESCRIPTION, NEWPRODOFFERID, PRODOFFERID, PRODOFFERTYPEID, PRODUCTEXCHANGEID, STATUS, CREATEDATETIME, CREATEUSER, ENDDATETIME, STARTDATETIME, UPDATEDATETIME, UPDATEUSER, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "PRODUCT_EXCHANGE_ID")
    @SequenceGenerator(name = "PRODUCT_EXCHANGE_ID_GENERATOR", sequenceName = "PRODUCT_EXCHANGE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_EXCHANGE_ID_GENERATOR")
    private Long productExchangeId;
    @Column(name = "PROD_OFFER_TYPE_ID")
    private Long prodOfferTypeId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "NEW_PROD_OFFER_ID")
    private Long newProdOfferId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "START_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDatetime;
    @Column(name = "END_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDatetime;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "DESCRIPTION")
    private String description;

    public ProductExchange() {
    }

    public ProductExchange(Long productExchangeId) {
        this.productExchangeId = productExchangeId;
    }

    public Long getProductExchangeId() {
        return productExchangeId;
    }

    public void setProductExchangeId(Long productExchangeId) {
        this.productExchangeId = productExchangeId;
    }

    public Long getProdOfferTypeId() {
        return prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getNewProdOfferId() {
        return newProdOfferId;
    }

    public void setNewProdOfferId(Long newProdOfferId) {
        this.newProdOfferId = newProdOfferId;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productExchangeId != null ? productExchangeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductExchange)) {
            return false;
        }
        ProductExchange other = (ProductExchange) object;
        if ((this.productExchangeId == null && other.productExchangeId != null) || (this.productExchangeId != null && !this.productExchangeId.equals(other.productExchangeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.ProductExchange[ productExchangeId=" + productExchangeId + " ]";
    }

}
