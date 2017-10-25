package com.viettel.bccs.im1.dto;

import com.google.common.base.Joiner;
import com.viettel.fw.dto.BaseDTO;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

public class StaffIm1DTO extends BaseDTO {
    @JsonProperty("address")
    private String address;
    private String areaCode;
    private String bankplusMobile;
    private Date birthday;
    private String businessLicence;
    private Long businessMethod;
    private Long channelTypeId;
    private Date contractFromDate;
    private Long contractMethod;
    private String contractNo;
    private Date contractToDate;
    private Long depositValue;
    private String discountPolicy;
    private String district;
    private String email;
    private String fileName;
    private Long hasEquipment;
    private Long hasTin;
    private String home;
    private Date idIssueDate;
    private String idIssuePlace;
    private String idNo;
    private String isdn;
    private Date lastLockTime;
    private Date lastModified;
    private Long lockStatus;
    @JsonProperty("name")
    private String name;
    private String note;
    private String pin;
    private String pointOfSale;
    private Long pointOfSaleType;
    private String precinct;
    private String pricePolicy;
    private String province;
    private String serial;
    @JsonProperty("shop_id")
    private Long shopId;
    private Long shopOwnerId;
    @JsonProperty("staff_code")
    private String staffCode;
    @JsonProperty("staff_id")
    private Long staffId;
    private String staffOwnType;
    private Long staffOwnerId;
    private String status;
    private Long stockNum;
    private Long stockNumImp;
    private String street;
    private String streetBlock;
    private Long subOwnerId;
    private Long subOwnerType;
    private String tel;
    private String tin;
    private String ttnsCode;
    private Long type;
    private Long userId;
    private String shopCode;
    private ShopIm1DTO shopDTO;
    private String shopName;
    private Long shopChanelTypeId;
    private String shopProvince;
    private String shopPath;
    private Long saleTransStaffId;
    private String ipAddress;
    @JsonProperty("table_pk")
    private String tablePk;

    public StaffIm1DTO() {
    }

    public StaffIm1DTO(String staffCode, Long staffId, String shopCode, Long shopId) {
        this.staffCode = staffCode;
        this.staffId = staffId;
        this.shopCode = shopCode;
        this.shopId = shopId;
    }

    public String getKeySet() {
        return keySet;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    //getter and setter

    public String getTablePk() {
        return staffCode;
    }

    public void setTablePk(String tablePk) {
        this.tablePk = tablePk;
    }

    public String getShopPath() {
        return shopPath;
    }

    public void setShopPath(String shopPath) {
        this.shopPath = shopPath;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getBankplusMobile() {
        return this.bankplusMobile;
    }

    public void setBankplusMobile(String bankplusMobile) {
        this.bankplusMobile = bankplusMobile;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBusinessLicence() {
        return this.businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public Long getBusinessMethod() {
        return this.businessMethod;
    }

    public void setBusinessMethod(Long businessMethod) {
        this.businessMethod = businessMethod;
    }

    public Long getChannelTypeId() {
        return this.channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public Date getContractFromDate() {
        return this.contractFromDate;
    }

    public void setContractFromDate(Date contractFromDate) {
        this.contractFromDate = contractFromDate;
    }

    public Long getContractMethod() {
        return this.contractMethod;
    }

    public void setContractMethod(Long contractMethod) {
        this.contractMethod = contractMethod;
    }

    public String getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Date getContractToDate() {
        return this.contractToDate;
    }

    public void setContractToDate(Date contractToDate) {
        this.contractToDate = contractToDate;
    }

    public Long getDepositValue() {
        return this.depositValue;
    }

    public void setDepositValue(Long depositValue) {
        this.depositValue = depositValue;
    }

    public String getDiscountPolicy() {
        return this.discountPolicy;
    }

    public void setDiscountPolicy(String discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getHasEquipment() {
        return this.hasEquipment;
    }

    public void setHasEquipment(Long hasEquipment) {
        this.hasEquipment = hasEquipment;
    }

    public Long getHasTin() {
        return this.hasTin;
    }

    public void setHasTin(Long hasTin) {
        this.hasTin = hasTin;
    }

    public String getHome() {
        return this.home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public Date getIdIssueDate() {
        return this.idIssueDate;
    }

    public void setIdIssueDate(Date idIssueDate) {
        this.idIssueDate = idIssueDate;
    }

    public String getIdIssuePlace() {
        return this.idIssuePlace;
    }

    public void setIdIssuePlace(String idIssuePlace) {
        this.idIssuePlace = idIssuePlace;
    }

    public String getIdNo() {
        return this.idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIsdn() {
        return this.isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public Date getLastLockTime() {
        return this.lastLockTime;
    }

    public void setLastLockTime(Date lastLockTime) {
        this.lastLockTime = lastLockTime;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Long getLockStatus() {
        return this.lockStatus;
    }

    public void setLockStatus(Long lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPointOfSale() {
        return this.pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public Long getPointOfSaleType() {
        return this.pointOfSaleType;
    }

    public void setPointOfSaleType(Long pointOfSaleType) {
        this.pointOfSaleType = pointOfSaleType;
    }

    public String getPrecinct() {
        return this.precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }

    public String getPricePolicy() {
        return this.pricePolicy;
    }

    public void setPricePolicy(String pricePolicy) {
        this.pricePolicy = pricePolicy;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getShopOwnerId() {
        return this.shopOwnerId;
    }

    public void setShopOwnerId(Long shopOwnerId) {
        this.shopOwnerId = shopOwnerId;
    }

    public String getStaffCode() {
        return this.staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffOwnType() {
        return this.staffOwnType;
    }

    public void setStaffOwnType(String staffOwnType) {
        this.staffOwnType = staffOwnType;
    }

    public Long getStaffOwnerId() {
        return this.staffOwnerId;
    }

    public void setStaffOwnerId(Long staffOwnerId) {
        this.staffOwnerId = staffOwnerId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStockNum() {
        return this.stockNum;
    }

    public void setStockNum(Long stockNum) {
        this.stockNum = stockNum;
    }

    public Long getStockNumImp() {
        return this.stockNumImp;
    }

    public void setStockNumImp(Long stockNumImp) {
        this.stockNumImp = stockNumImp;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetBlock() {
        return this.streetBlock;
    }

    public void setStreetBlock(String streetBlock) {
        this.streetBlock = streetBlock;
    }

    public Long getSubOwnerId() {
        return this.subOwnerId;
    }

    public void setSubOwnerId(Long subOwnerId) {
        this.subOwnerId = subOwnerId;
    }

    public Long getSubOwnerType() {
        return this.subOwnerType;
    }

    public void setSubOwnerType(Long subOwnerType) {
        this.subOwnerType = subOwnerType;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTin() {
        return this.tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getTtnsCode() {
        return this.ttnsCode;
    }

    public void setTtnsCode(String ttnsCode) {
        this.ttnsCode = ttnsCode;
    }

    public Long getType() {
        return this.type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ShopIm1DTO getShopDTO() {
        return shopDTO;
    }

    public void setShopDTO(ShopIm1DTO shopDTO) {
        this.shopDTO = shopDTO;
    }


    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getShopChanelTypeId() {
        return shopChanelTypeId;
    }

    public void setShopChanelTypeId(Long shopChanelTypeId) {
        this.shopChanelTypeId = shopChanelTypeId;
    }

    public String getShopProvince() {
        return shopProvince;
    }

    public void setShopProvince(String shopProvince) {
        this.shopProvince = shopProvince;
    }

    public Long getSaleTransStaffId() {
        return saleTransStaffId;
    }

    public void setSaleTransStaffId(Long saleTransStaffId) {
        this.saleTransStaffId = saleTransStaffId;
    }

    @Override
    public String toString() {
        String s = Joiner.on(" - ").skipNulls().join(staffCode, name);
        return s;
    }
}
