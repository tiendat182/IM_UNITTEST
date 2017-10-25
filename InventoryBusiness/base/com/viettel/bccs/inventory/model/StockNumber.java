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
 * @author anhvv4
 */
@Entity
@Table(name = "STOCK_NUMBER")
public class StockNumber implements Serializable {
    public static enum COLUMNS {FILTERSTATUSLIST,OWNERID,AREACODE, OWNERTYPE, CREATEDATE, HLRSTATUS, ID, IMSI, ISDN, ISDNTYPE, LASTUPDATEIPADDRESS, LASTUPDATETIME, LASTUPDATEUSER, SERIAL, SERVICETYPE, STATIONCODE, STATIONID, STATUS, STOCKID, TELECOMSERVICEID, UPDATEDATETIME, USERCREATE, EXCLUSE_ID_LIST, FILTERRULEID, FILTERRULEIDLIST, PRODOFFERID, LOCKBYSTAFF}

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "STOCK_NUMBER_ID_GENERATOR", sequenceName = "STOCK_NUMBER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_NUMBER_ID_GENERATOR")
    private Long id;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Column(name = "HLR_STATUS")
    private String hlrStatus;
    @Column(name = "ISDN_TYPE")
    private String isdnType;
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "IMSI")
    private String imsi;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "AREA_CODE")
    private String areaCode;
    @Column(name = "SERVICE_TYPE")
    private String serviceType;
    @Column(name = "USER_CREATE")
    private String userCreate;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LAST_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;
    @Column(name = "LAST_UPDATE_IP_ADDRESS")
    private String lastUpdateIpAddress;
    @Column(name = "STATION_ID")
    private Long stationId;
    @Column(name = "STATION_CODE")
    private String stationCode;
    @Column(name = "REMOVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date removeDate;
    @Column(name = "LOCK_BY_STAFF")
    private Long lockByStaff;
    @Column(name = "SALE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;
    @Column(name = "DSLAM_ID")
    private Long dslamId;
    @Column(name = "UPDATE_NUMBER")
    private Long updateNumber;
    @Column(name = "CONNECTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date connectionDate;
    @Column(name = "CONNECTION_TYPE")
    private String connectionType;
    @Column(name = "CONNECTION_STATUS")
    private String connectionStatus;

    public StockNumber() {
    }

    public StockNumber(Long id) {
        this.id = id;
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

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public String getHlrStatus() {
        return hlrStatus;
    }

    public void setHlrStatus(String hlrStatus) {
        this.hlrStatus = hlrStatus;
    }

    public String getIsdnType() {
        return isdnType;
    }

    public void setIsdnType(String isdnType) {
        this.isdnType = isdnType;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getLastUpdateIpAddress() {
        return lastUpdateIpAddress;
    }

    public void setLastUpdateIpAddress(String lastUpdateIpAddress) {
        this.lastUpdateIpAddress = lastUpdateIpAddress;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public Date getRemoveDate() {
        return removeDate;
    }

    public void setRemoveDate(Date removeDate) {
        this.removeDate = removeDate;
    }

    public Long getLockByStaff() {
        return lockByStaff;
    }

    public void setLockByStaff(Long lockByStaff) {
        this.lockByStaff = lockByStaff;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Long getDslamId() {
        return dslamId;
    }

    public void setDslamId(Long dslamId) {
        this.dslamId = dslamId;
    }

    public Long getUpdateNumber() {
        return updateNumber;
    }

    public void setUpdateNumber(Long updateNumber) {
        this.updateNumber = updateNumber;
    }

    public Date getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
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
        if (!(object instanceof StockNumber)) {
            return false;
        }
        StockNumber other = (StockNumber) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.StockNumber[ id=" + id + " ]";
    }

}
