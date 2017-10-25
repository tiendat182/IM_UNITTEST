/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author tuydv1
 */
@Entity
@Table(name = "PARTNER_CONTRACT")
@XmlRootElement
public class PartnerContract implements Serializable {
public static enum COLUMNS {ACTIONCODE, CONTRACTCODE, CONTRACTDATE, CONTRACTSTATUS, CREATEDATE, CURRENCYRATE, CURRENCYTYPE, DELIVERYLOCATION, IMPORTDATE, KCSDATE, KCSNO, LASTMODIFIED, ORDERCODE, PARTNERCONTRACTID, PARTNERID, POCODE, PODATE, PRODOFFERID, REQUESTDATE, STARTDATEWARRANTY, STOCKTRANSID, UNITPRICE, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PARTNER_CONTRACT_ID")
    @SequenceGenerator(name = "PARTNER_CONTRACT_ID_GENERATOR", sequenceName = "PARTNER_CONTRACT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTNER_CONTRACT_ID_GENERATOR")
    private Long partnerContractId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "ACTION_CODE")
    private String actionCode;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LAST_MODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "CONTRACT_CODE")
    private String contractCode;
    @Column(name = "PO_CODE")
    private String poCode;
    @Column(name = "CONTRACT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contractDate;
    @Column(name = "PO_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date poDate;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "DELIVERY_LOCATION")
    private String deliveryLocation;
    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;
    @Column(name = "PARTNER_ID")
    private Long partnerId;
    @Column(name = "CURRENCY_TYPE")
    private String currencyType;
    @Column(name = "UNIT_PRICE")
    private Long unitPrice;
    @Column(name = "CONTRACT_STATUS")
    private Short contractStatus;
    @Column(name = "IMPORT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date importDate;
    @Column(name = "ORDER_CODE")
    private String orderCode;
    @Column(name = "START_DATE_WARRANTY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateWarranty;
    @Column(name = "KCS_NO")
    private String kcsNo;
    @Column(name = "KCS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date kcsDate;
    @Column(name = "CURRENCY_RATE")
    private Long currencyRate;

    public PartnerContract() {
    }

    public PartnerContract(Long partnerContractId) {
        this.partnerContractId = partnerContractId;
    }

    public Long getPartnerContractId() {
        return partnerContractId;
    }

    public void setPartnerContractId(Long partnerContractId) {
        this.partnerContractId = partnerContractId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getPoCode() {
        return poCode;
    }

    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getPoDate() {
        return poDate;
    }

    public void setPoDate(Date poDate) {
        this.poDate = poDate;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
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

    public Short getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Short contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
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

    public String getKcsNo() {
        return kcsNo;
    }

    public void setKcsNo(String kcsNo) {
        this.kcsNo = kcsNo;
    }

    public Date getKcsDate() {
        return kcsDate;
    }

    public void setKcsDate(Date kcsDate) {
        this.kcsDate = kcsDate;
    }

    public Long getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(Long currencyRate) {
        this.currencyRate = currencyRate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerContractId != null ? partnerContractId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PartnerContract)) {
            return false;
        }
        PartnerContract other = (PartnerContract) object;
        if ((this.partnerContractId == null && other.partnerContractId != null) || (this.partnerContractId != null && !this.partnerContractId.equals(other.partnerContractId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.PartnerContract[ partnerContractId=" + partnerContractId + " ]";
    }
    
}
