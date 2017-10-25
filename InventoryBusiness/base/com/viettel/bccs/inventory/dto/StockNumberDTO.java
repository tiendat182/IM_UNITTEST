package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.util.Date;

public class StockNumberDTO extends BaseDTO {

    private String areaCode;
    private Long ownerId;
    private Long ownerType;
    private Date createDate;
    private Long filterRuleId;
    private String hlrStatus;
    private Long id;
    private String imsi;
    private String isdn;
    private String isdnType;
    private String lastUpdateIpAddress;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private Long prodOfferId;
    private String prodOfferName;
    private String serial;
    private String serviceType;
    private String stationCode;
    private Long stationId;
    private String status;
    private Long stockId;
    private Long telecomServiceId;
    private Date updateDatetime;
    private String userCreate;
    private Long fromISDN;
    private Long toISDN;
    private Long total;
    private Long ruleOrder;
    private Long lockByStaff;

    public String getKeySet() {
        return keySet; }
    public String getAreaCode() {
        return this.areaCode;
    }
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Long getFilterRuleId() {
        return this.filterRuleId;
    }
    public void setFilterRuleId(Long filterRuleId) {
        this.filterRuleId = filterRuleId;
    }
    public String getHlrStatus() {
        return this.hlrStatus;
    }
    public void setHlrStatus(String hlrStatus) {
        this.hlrStatus = hlrStatus;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getImsi() {
        return this.imsi;
    }
    public void setImsi(String imsi) {
        this.imsi = imsi;
    }
    public String getIsdn() {
        return this.isdn;
    }
    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }
    public String getIsdnType() {
        return this.isdnType;
    }
    public void setIsdnType(String isdnType) {
        this.isdnType = isdnType;
    }
    public String getLastUpdateIpAddress() {
        return this.lastUpdateIpAddress;
    }
    public void setLastUpdateIpAddress(String lastUpdateIpAddress) {
        this.lastUpdateIpAddress = lastUpdateIpAddress;
    }
    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    public String getLastUpdateUser() {
        return this.lastUpdateUser;
    }
    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
    public Long getProdOfferId() {
        return this.prodOfferId;
    }
    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }
    public String getSerial() {
        return this.serial;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
    public String getServiceType() {
        return this.serviceType;
    }
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    public String getStationCode() {
        return this.stationCode;
    }
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }
    public Long getStationId() {
        return this.stationId;
    }
    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Long getStockId() {
        return this.stockId;
    }
    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }
    public Long getTelecomServiceId() {
        return this.telecomServiceId;
    }
    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }
    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
    public String getUserCreate() {
        return this.userCreate;
    }
    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
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
    public Long getFromISDN() {
        return fromISDN;
    }
    public void setFromISDN(Long fromISDN) {
        this.fromISDN = fromISDN;
    }
    public Long getToISDN() {
        return toISDN;
    }
    public void setToISDN(Long toISDN) {
        this.toISDN = toISDN;
    }
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    public Long getRuleOrder() {
        return ruleOrder;
    }
    public void setRuleOrder(Long ruleOrder) {
        this.ruleOrder = ruleOrder;
    }
    public String getProdOfferName() {
        return prodOfferName;
    }
    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public Long getLockByStaff() {
        return lockByStaff;
    }

    public void setLockByStaff(Long lockByStaff) {
        this.lockByStaff = lockByStaff;
    }
}
