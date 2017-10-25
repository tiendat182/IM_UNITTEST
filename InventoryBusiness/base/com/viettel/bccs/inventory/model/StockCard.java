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
 *
 * @author anhvv4
 */
@Entity
@Table(name = "STOCK_CARD")
@NamedQueries({
    @NamedQuery(name = "StockCard.findAll", query = "SELECT s FROM StockCard s")})
public class StockCard implements Serializable {
public static enum COLUMNS {CREATEDATE, CREATEUSER, FROMSERIAL, ID, OWNERID, OWNERTYPE, POCODE, PRODOFFERID, SALEDATE, STATEID, STATUS, TELECOMSERVICEID, TOSERIAL, VCSTATUS, EXCLUSE_ID_LIST, BANKPLUSSTATUS};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "STOCK_CARD_ID_GENERATOR", sequenceName = "STOCK_CARD_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_CARD_ID_GENERATOR")
    private Long id;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Basic(optional = false)
    @Column(name = "SERIAL")
    private String serial;
    @Basic(optional = false)
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "SALE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;
    @Column(name = "PO_CODE")
    private String poCode;
    @Column(name = "VC_STATUS")
    private Long vcStatus;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "BANKPLUS_STATUS")
    private Long bankplusStatus;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;


    private String fromSerial;
    private String toSerial;

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public StockCard() {
    }

    public StockCard(Long id) {
        this.id = id;
    }

    public StockCard(Long id, String fromSerial, String toSerial) {
        this.id = id;
        this.fromSerial = fromSerial;
        this.toSerial = toSerial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSerial() {
        return toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public String getPoCode() {
        return poCode;
    }

    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }

    public Long getVcStatus() {
        return vcStatus;
    }

    public void setVcStatus(Long vcStatus) {
        this.vcStatus = vcStatus;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockCard)) {
            return false;
        }
        StockCard other = (StockCard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.StockCard[ id=" + id + " ]";
    }

    public Long getBankplusStatus() {
        return bankplusStatus;
    }

    public void setBankplusStatus(Long bankplusStatus) {
        this.bankplusStatus = bankplusStatus;
    }
}
