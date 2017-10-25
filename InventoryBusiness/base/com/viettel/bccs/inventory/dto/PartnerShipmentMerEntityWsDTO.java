package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;

/**
 * dto hung data tu webservice cua KTTS
 * @author thanhnt77
 */
public class PartnerShipmentMerEntityWsDTO extends BaseDTO {

    private String assetPrice;
    private Long companyId;
    private String contractCode;
    private Long contractId;
    private String count;
    private String entityType;
    private Date expiredWarrantyDate;
    private Long groupId;
    private String isCheckedKcs;
    private String isLock;
    private String isMerchandise;
    private String isTemp;
    private String keySearch;
    private Long merEntityId;
    private Long merId;
    private Long nationalId;
    private String originalPrice;
    private String merCode;
    private String path;
    private String serialNumber;
    private String shipmentNo;
    private Long statusId;
    private String unitPrice;
    private Long upgradeParentId;
    private Long warrantPartnerId;

    public PartnerShipmentMerEntityWsDTO() {
    }

    public String getAssetPrice() {
        return assetPrice;
    }

    public void setAssetPrice(String assetPrice) {
        this.assetPrice = assetPrice;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Date getExpiredWarrantyDate() {
        return expiredWarrantyDate;
    }

    public void setExpiredWarrantyDate(Date expiredWarrantyDate) {
        this.expiredWarrantyDate = expiredWarrantyDate;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getIsCheckedKcs() {
        return isCheckedKcs;
    }

    public void setIsCheckedKcs(String isCheckedKcs) {
        this.isCheckedKcs = isCheckedKcs;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public String getIsMerchandise() {
        return isMerchandise;
    }

    public void setIsMerchandise(String isMerchandise) {
        this.isMerchandise = isMerchandise;
    }

    public String getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(String isTemp) {
        this.isTemp = isTemp;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

    public Long getMerEntityId() {
        return merEntityId;
    }

    public void setMerEntityId(Long merEntityId) {
        this.merEntityId = merEntityId;
    }

    public Long getMerId() {
        return merId;
    }

    public void setMerId(Long merId) {
        this.merId = merId;
    }

    public Long getNationalId() {
        return nationalId;
    }

    public void setNationalId(Long nationalId) {
        this.nationalId = nationalId;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getUpgradeParentId() {
        return upgradeParentId;
    }

    public void setUpgradeParentId(Long upgradeParentId) {
        this.upgradeParentId = upgradeParentId;
    }

    public Long getWarrantPartnerId() {
        return warrantPartnerId;
    }

    public void setWarrantPartnerId(Long warrantPartnerId) {
        this.warrantPartnerId = warrantPartnerId;
    }

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode;
    }
}
