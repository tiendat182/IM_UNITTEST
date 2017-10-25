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
 * @author anhvv4
 */
@Entity
@Table(name = "STOCK_HANDSET")
@NamedQueries({
        @NamedQuery(name = "StockHandset.findAll", query = "SELECT s FROM StockHandset s")})
public class StockHandset implements Serializable {
    public static enum COLUMNS {CONNECTIONDATE, CONNECTIONSTATUS, CONNECTIONTYPE, CONTRACTCODE, CREATEDATE, CREATEUSER, DEPOSITPRICE, EXPORTTOCOLLDATE, ID, OWNERID, OWNERTYPE, PARTNERID, POCODE, PRODOFFERID, SALEDATE, SERIAL, STATEID, STATUS, TELECOMSERVICEID, TVMSCADID, TVMSMACID, UPDATEDATETIME, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "STOCK_HANDSET_ID_GENERATOR", sequenceName = "STOCK_HANDSET_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_HANDSET_ID_GENERATOR")
    private Long id;
    @Basic(optional = false)
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
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
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;
    @Column(name = "DEPOSIT_PRICE")
    private String depositPrice;
    @Column(name = "PO_CODE")
    private String poCode;
    @Column(name = "CONTRACT_CODE")
    private String contractCode;
    @Column(name = "CONNECTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date connectionDate;
    @Column(name = "CONNECTION_STATUS")
    private Long connectionStatus;
    @Column(name = "CONNECTION_TYPE")
    private String connectionType;
    @Column(name = "PARTNER_ID")
    private Long partnerId;
    @Column(name = "TVMS_CAD_ID")
    private String tvmsCadId;
    @Column(name = "TVMS_MAC_ID")
    private String tvmsMacId;
    @Column(name = "EXPORT_TO_COLL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exportToCollDate;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "LICENSE_KEY")
    private String licenseKey;
    @Column(name = "SO_PIN")
    private String soPin;
    @Column(name = "SERIAL_GPON")
    private String serialGpon;

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public StockHandset() {
    }

    public StockHandset(Long id) {
        this.id = id;
    }

    public StockHandset(Long id, String serial) {
        this.id = id;
        this.serial = serial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(String depositPrice) {
        this.depositPrice = depositPrice;
    }

    public String getPoCode() {
        return poCode;
    }

    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }

    public Long getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(Long connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getTvmsCadId() {
        return tvmsCadId;
    }

    public void setTvmsCadId(String tvmsCadId) {
        this.tvmsCadId = tvmsCadId;
    }

    public String getTvmsMacId() {
        return tvmsMacId;
    }

    public void setTvmsMacId(String tvmsMacId) {
        this.tvmsMacId = tvmsMacId;
    }

    public Date getExportToCollDate() {
        return exportToCollDate;
    }

    public void setExportToCollDate(Date exportToCollDate) {
        this.exportToCollDate = exportToCollDate;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getSoPin() {
        return soPin;
    }

    public void setSoPin(String soPin) {
        this.soPin = soPin;
    }

    public String getSerialGpon() {
        return serialGpon;
    }

    public void setSerialGpon(String serialGpon) {
        this.serialGpon = serialGpon;
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
        if (!(object instanceof StockHandset)) {
            return false;
        }
        StockHandset other = (StockHandset) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.StockHandset[ id=" + id + " ]";
    }

}
