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
 * @author DungPT16
 */
@Entity
@Table(name = "STOCK_TOTAL_CYCLE")
@NamedQueries({
        @NamedQuery(name = "StockTotalCycle.findAll", query = "SELECT s FROM StockTotalCycle s")})
public class StockTotalCycle implements Serializable {
    public static enum COLUMNS {CREATEDATETIME, CREATEUSER, OWNERID, OWNERTYPE, PRICECOST, PRODOFFERID, PRODUCTOFFERTYPEID, QUANTITY, QUANTITYCYCLE1, QUANTITYCYCLE2, QUANTITYCYCLE3, QUANTITYCYCLE4, RETAILPRICE, STATEID, STOCKTOTALCYCLEID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TOTAL_CYCLE_ID")
    @SequenceGenerator(name = "STOCK_TOTAL_CYCLE_ID_GENERATOR", sequenceName = "STOCK_TOTAL_CYCLE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TOTAL_CYCLE_ID_GENERATOR")
    private Long stockTotalCycleId;
    @Basic(optional = false)
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Basic(optional = false)
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_OFFER_TYPE_ID")
    private Long productOfferTypeId;
    @Basic(optional = false)
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Basic(optional = false)
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "QUANTITY_CYCLE_1")
    private Long quantityCycle1;
    @Column(name = "QUANTITY_CYCLE_2")
    private Long quantityCycle2;
    @Column(name = "QUANTITY_CYCLE_3")
    private Long quantityCycle3;
    @Column(name = "QUANTITY_CYCLE_4")
    private Long quantityCycle4;
    @Column(name = "PRICE_COST")
    private Long priceCost;
    @Column(name = "RETAIL_PRICE")
    private Long retailPrice;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "CREATE_USER")
    private String createUser;

    public StockTotalCycle() {
    }

    public StockTotalCycle(Long stockTotalCycleId) {
        this.stockTotalCycleId = stockTotalCycleId;
    }

    public StockTotalCycle(Long stockTotalCycleId, Long ownerType, Long ownerId, Long prodOfferId, Long stateId) {
        this.stockTotalCycleId = stockTotalCycleId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.prodOfferId = prodOfferId;
        this.stateId = stateId;
    }

    public Long getStockTotalCycleId() {
        return stockTotalCycleId;
    }

    public void setStockTotalCycleId(Long stockTotalCycleId) {
        this.stockTotalCycleId = stockTotalCycleId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantityCycle1() {
        return quantityCycle1;
    }

    public void setQuantityCycle1(Long quantityCycle1) {
        this.quantityCycle1 = quantityCycle1;
    }

    public Long getQuantityCycle2() {
        return quantityCycle2;
    }

    public void setQuantityCycle2(Long quantityCycle2) {
        this.quantityCycle2 = quantityCycle2;
    }

    public Long getQuantityCycle3() {
        return quantityCycle3;
    }

    public void setQuantityCycle3(Long quantityCycle3) {
        this.quantityCycle3 = quantityCycle3;
    }

    public Long getQuantityCycle4() {
        return quantityCycle4;
    }

    public void setQuantityCycle4(Long quantityCycle4) {
        this.quantityCycle4 = quantityCycle4;
    }

    public Long getPriceCost() {
        return priceCost;
    }

    public void setPriceCost(Long priceCost) {
        this.priceCost = priceCost;
    }

    public Long getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Long retailPrice) {
        this.retailPrice = retailPrice;
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

    public Long getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockTotalCycleId != null ? stockTotalCycleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTotalCycle)) {
            return false;
        }
        StockTotalCycle other = (StockTotalCycle) object;
        if ((this.stockTotalCycleId == null && other.stockTotalCycleId != null) || (this.stockTotalCycleId != null && !this.stockTotalCycleId.equals(other.stockTotalCycleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockTotalCycle[ stockTotalCycleId=" + stockTotalCycleId + " ]";
    }

}
