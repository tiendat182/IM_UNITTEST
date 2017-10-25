package com.viettel.bccs.inventory.dto;

import com.google.common.base.Joiner;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class ShopDTO extends BaseDTO implements Serializable {

    private String account;
    private String address;
    private String areaCode;
    private String bankName;
    private String bankplusMobile;
    private String businessLicence;
    private String centerCode;
    private Long channelTypeId;
    private String company;
    private String contactName;
    private String contactTitle;
    private Date contractFromDate;
    private String contractNo;
    private Date contractToDate;
    private Date createDate;
    private Long depositValue;
    private String description;
    private String discountPolicy;
    private String district;
    private String email;
    private String fax;
    private String fileName;
    private String home;
    private Date idIssueDate;
    private String idIssuePlace;
    private String idNo;
    private String name;
    private String oldShopCode;
    private String parShopCode;
    private Long parentShopId;
    private String payComm;
    private String precinct;
    private String pricePolicy;
    private String province;
    private String provinceCode;
    private String shop;
    private String shopCode;
    private Long shopId;
    private String shopPath;
    private String shopType;
    private String status;
    private Long stockNum;
    private Long stockNumImp;
    private String street;
    private String streetBlock;
    private String tel;
    private String telNumber;
    private String tin;
    private Long shopKeeperId;
    private Long shopDirectorId;

    public Long getShopKeeperId() {
        return shopKeeperId;
    }

    public void setShopKeeperId(Long shopKeeperId) {
        this.shopKeeperId = shopKeeperId;
    }

    public Long getShopDirectorId() {
        return shopDirectorId;
    }

    public void setShopDirectorId(Long shopDirectorId) {
        this.shopDirectorId = shopDirectorId;
    }

    public String getShopFullName() {
        return Joiner.on("_").skipNulls().join(shopCode, name);
    }

    public String getKeySet() {
        return keySet; }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
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
    public String getBankName() {
        return this.bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getBankplusMobile() {
        return this.bankplusMobile;
    }
    public void setBankplusMobile(String bankplusMobile) {
        this.bankplusMobile = bankplusMobile;
    }
    public String getBusinessLicence() {
        return this.businessLicence;
    }
    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }
    public String getCenterCode() {
        return this.centerCode;
    }
    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }
    public Long getChannelTypeId() {
        return this.channelTypeId;
    }
    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }
    public String getCompany() {
        return this.company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getContactName() {
        return this.contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getContactTitle() {
        return this.contactTitle;
    }
    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }
    public Date getContractFromDate() {
        return this.contractFromDate;
    }
    public void setContractFromDate(Date contractFromDate) {
        this.contractFromDate = contractFromDate;
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
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Long getDepositValue() {
        return this.depositValue;
    }
    public void setDepositValue(Long depositValue) {
        this.depositValue = depositValue;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public String getFax() {
        return this.fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOldShopCode() {
        return this.oldShopCode;
    }
    public void setOldShopCode(String oldShopCode) {
        this.oldShopCode = oldShopCode;
    }
    public String getParShopCode() {
        return this.parShopCode;
    }
    public void setParShopCode(String parShopCode) {
        this.parShopCode = parShopCode;
    }
    public Long getParentShopId() {
        return this.parentShopId;
    }
    public void setParentShopId(Long parentShopId) {
        this.parentShopId = parentShopId;
    }
    public String getPayComm() {
        return this.payComm;
    }
    public void setPayComm(String payComm) {
        this.payComm = payComm;
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
    public String getProvinceCode() {
        return this.provinceCode;
    }
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
    public String getShop() {
        return this.shop;
    }
    public void setShop(String shop) {
        this.shop = shop;
    }
    public String getShopCode() {
        return this.shopCode;
    }
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
    public Long getShopId() {
        return this.shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public String getShopPath() {
        return this.shopPath;
    }
    public void setShopPath(String shopPath) {
        this.shopPath = shopPath;
    }
    public String getShopType() {
        return this.shopType;
    }
    public void setShopType(String shopType) {
        this.shopType = shopType;
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
    public String getTel() {
        return this.tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getTelNumber() {
        return this.telNumber;
    }
    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }
    public String getTin() {
        return this.tin;
    }
    public void setTin(String tin) {
        this.tin = tin;
    }
}
