package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.util.HashMap;

/**
 * Created by sinhhv on 12/1/2015.
 */
public class SearchNumberRangeDTO extends BaseDTO {

    String serviceType;
    String serviceName;
    String areaCode;
    String areaName;
    String pstnCode;
    String startRange;
    String endRange;
    String planningType;
    String userCreate;
    String ipAddress;
    String status;
    Long oldStatus;
    Long ownerId;
    String ownerType;
    String shopCode;
    Long shopId;
    String filePath;
    Long productOldId;
    Long productNewId;
    HashMap<String, String> mapISDN;

    public SearchNumberRangeDTO() {

    }

    public Long getProductNewId() {
        return productNewId;
    }

    public void setProductNewId(Long productNewId) {
        this.productNewId = productNewId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPstnCode() {
        return pstnCode;
    }

    public void setPstnCode(String pstnCode) {
        this.pstnCode = pstnCode;
    }

    public String getStartRange() {
        return startRange;
    }

    public void setStartRange(String startRange) {
        this.startRange = startRange;
    }

    public String getEndRange() {
        return endRange;
    }

    public void setEndRange(String endRange) {
        this.endRange = endRange;
    }

    public String getPlanningType() {
        return planningType;
    }

    public void setPlanningType(String planningType) {
        this.planningType = planningType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public HashMap<String, String> getMapISDN() {
        return mapISDN;
    }

    public void setMapISDN(HashMap<String, String> mapISDN) {
        this.mapISDN = mapISDN;
    }

    public Long getProductOldId() {
        return productOldId;
    }

    public void setProductOldId(Long productOldId) {
        this.productOldId = productOldId;
    }

    public Long getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Long oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
