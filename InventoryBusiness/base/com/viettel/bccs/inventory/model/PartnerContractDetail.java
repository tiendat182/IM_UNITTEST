/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ThanhNT77
 */
@Entity
@Table(name = "PARTNER_CONTRACT_DETAIL")
@XmlRootElement
public class PartnerContractDetail implements Serializable {
public static enum COLUMNS {CREATEDATE, CURRENCYTYPE, ORDERCODE, PARTNERCONTRACTDETAILID, PARTNERCONTRACTID, PRODOFFERID, QUANTITY, STARTDATEWARRANTY, STATEID, UNITPRICE, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PARTNER_CONTRACT_DETAIL_ID")
    @SequenceGenerator(name = "PARTNER_CONTRACT_DETAIL_ID_GENERATOR", sequenceName = "PARTNER_CONTRACT_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTNER_CONTRACT_DETAIL_ID_GENERATOR")
    private Long partnerContractDetailId;
    @Column(name = "PARTNER_CONTRACT_ID")
    private Long partnerContractId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CURRENCY_TYPE")
    private String currencyType;
    @Column(name = "UNIT_PRICE")
    private Long unitPrice;
    @Column(name = "ORDER_CODE")
    private String orderCode;
    @Column(name = "START_DATE_WARRANTY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateWarranty;

    public PartnerContractDetail() {
    }

    public PartnerContractDetail(Long partnerContractDetailId) {
        this.partnerContractDetailId = partnerContractDetailId;
    }

    public Long getPartnerContractDetailId() {
        return partnerContractDetailId;
    }

    public void setPartnerContractDetailId(Long partnerContractDetailId) {
        this.partnerContractDetailId = partnerContractDetailId;
    }

    public Long getPartnerContractId() {
        return partnerContractId;
    }

    public void setPartnerContractId(Long partnerContractId) {
        this.partnerContractId = partnerContractId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getStartDateWarranty() {
        return startDateWarranty;
    }

    public void setStartDateWarranty(Date startDateWarranty) {
        this.startDateWarranty = startDateWarranty;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerContractDetailId != null ? partnerContractDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PartnerContractDetail)) {
            return false;
        }
        PartnerContractDetail other = (PartnerContractDetail) object;
        if ((this.partnerContractDetailId == null && other.partnerContractDetailId != null) || (this.partnerContractDetailId != null && !this.partnerContractDetailId.equals(other.partnerContractDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.PartnerContractDetail[ partnerContractDetailId=" + partnerContractDetailId + " ]";
    }
    
}
