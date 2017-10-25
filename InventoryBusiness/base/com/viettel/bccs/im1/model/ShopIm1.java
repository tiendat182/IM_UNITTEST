/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.im1.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ThanhNT77
 */
@Entity
@Table(name = "SHOP", schema = "BCCS_IM")

public class ShopIm1 implements Serializable {
public static enum COLUMNS {ACCOUNT, ADDRESS, AREACODE, BANKNAME, BANKPLUSMOBILE, BUSINESSLICENCE, CENTERCODE, CHANNELTYPEID, COMPANY, CONTACTNAME, CONTACTTITLE, CONTRACTFROMDATE, CONTRACTNO, CONTRACTTODATE, CREATEDATE, DEPOSITVALUE, DESCRIPTION, DISCOUNTPOLICY, DISTRICT, EMAIL, FAX, FILEATTACH, FILENAME, HOME, IDISSUEDATE, IDISSUEPLACE, IDNO, NAME, OLDSHOPCODE, PARSHOPCODE, PARENTSHOPID, PAYCOMM, PRECINCT, PRICEPOLICY, PROVINCE, PROVINCECODE, SHOP, SHOPCODE, SHOPID, SHOPPATH, SHOPTYPE, STATUS, STOCKNUM, STOCKNUMIMP, STREET, STREETBLOCK, TEL, TELNUMBER, TIN, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "NAME")
    private String name;
    @Column(name = "PARENT_SHOP_ID")
    private Long parentShopId;
    @Size(max = 40)
    @Column(name = "ACCOUNT")
    private String account;
    @Size(max = 150)
    @Column(name = "BANK_NAME")
    private String bankName;
    @Size(max = 500)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 100)
    @Column(name = "TEL")
    private String tel;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "FAX")
    private String fax;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Size(max = 1)
    @Column(name = "SHOP_TYPE")
    private String shopType;
    @Size(max = 100)
    @Column(name = "CONTACT_NAME")
    private String contactName;
    @Size(max = 30)
    @Column(name = "CONTACT_TITLE")
    private String contactTitle;
    @Size(max = 30)
    @Column(name = "TEL_NUMBER")
    private String telNumber;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 30)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 100)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 4)
    @Column(name = "PROVINCE")
    private String province;
    @Size(max = 20)
    @Column(name = "PAR_SHOP_CODE")
    private String parShopCode;
    @Size(max = 1)
    @Column(name = "CENTER_CODE")
    private String centerCode;
    @Size(max = 40)
    @Column(name = "OLD_SHOP_CODE")
    private String oldShopCode;
    @Size(max = 4000)
    @Column(name = "COMPANY")
    private String company;
    @Size(max = 50)
    @Column(name = "TIN")
    private String tin;
    @Size(max = 20)
    @Column(name = "SHOP")
    private String shop;
    @Size(max = 20)
    @Column(name = "PROVINCE_CODE")
    private String provinceCode;
    @Size(max = 1)
    @Column(name = "PAY_COMM")
    private String payComm;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;
    @Size(max = 20)
    @Column(name = "DISCOUNT_POLICY")
    private String discountPolicy;
    @Size(max = 20)
    @Column(name = "PRICE_POLICY")
    private String pricePolicy;
    @Size(max = 1)
    @Column(name = "STATUS")
    private String status;
    @Size(max = 500)
    @Column(name = "SHOP_PATH")
    private String shopPath;
    @Size(max = 3)
    @Column(name = "DISTRICT")
    private String district;
    @Size(max = 4)
    @Column(name = "PRECINCT")
    private String precinct;
    @Size(max = 15)
    @Column(name = "AREA_CODE")
    private String areaCode;
    @Size(max = 50)
    @Column(name = "ID_NO")
    private String idNo;
    @Size(max = 100)
    @Column(name = "ID_ISSUE_PLACE")
    private String idIssuePlace;
    @Column(name = "ID_ISSUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date idIssueDate;
    @Size(max = 100)
    @Column(name = "STREET_BLOCK")
    private String streetBlock;
    @Size(max = 100)
    @Column(name = "STREET")
    private String street;
    @Size(max = 100)
    @Column(name = "HOME")
    private String home;
    @Size(max = 100)
    @Column(name = "CONTRACT_NO")
    private String contractNo;
    @Size(max = 100)
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "CONTRACT_FROM_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractFromDate;
    @Column(name = "CONTRACT_TO_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractToDate;
    @Column(name = "DEPOSIT_VALUE")
    private Long depositValue;
    @Size(max = 200)
    @Column(name = "BUSINESS_LICENCE")
    private String businessLicence;
    @Size(max = 20)
    @Column(name = "BANKPLUS_MOBILE")
    private String bankplusMobile;
    @Column(name = "STOCK_NUM")
    private Long stockNum;
    @Column(name = "STOCK_NUM_IMP")
    private Long stockNumImp;

    public ShopIm1() {
    }

    public ShopIm1(Long shopId) {
        this.shopId = shopId;
    }

    public ShopIm1(Long shopId, String name, String shopCode, Long channelTypeId) {
        this.shopId = shopId;
        this.name = name;
        this.shopCode = shopCode;
        this.channelTypeId = channelTypeId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentShopId() {
        return parentShopId;
    }

    public void setParentShopId(Long parentShopId) {
        this.parentShopId = parentShopId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getParShopCode() {
        return parShopCode;
    }

    public void setParShopCode(String parShopCode) {
        this.parShopCode = parShopCode;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getOldShopCode() {
        return oldShopCode;
    }

    public void setOldShopCode(String oldShopCode) {
        this.oldShopCode = oldShopCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getPayComm() {
        return payComm;
    }

    public void setPayComm(String payComm) {
        this.payComm = payComm;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public String getDiscountPolicy() {
        return discountPolicy;
    }

    public void setDiscountPolicy(String discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public String getPricePolicy() {
        return pricePolicy;
    }

    public void setPricePolicy(String pricePolicy) {
        this.pricePolicy = pricePolicy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopPath() {
        return shopPath;
    }

    public void setShopPath(String shopPath) {
        this.shopPath = shopPath;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPrecinct() {
        return precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdIssuePlace() {
        return idIssuePlace;
    }

    public void setIdIssuePlace(String idIssuePlace) {
        this.idIssuePlace = idIssuePlace;
    }

    public Date getIdIssueDate() {
        return idIssueDate;
    }

    public void setIdIssueDate(Date idIssueDate) {
        this.idIssueDate = idIssueDate;
    }

    public String getStreetBlock() {
        return streetBlock;
    }

    public void setStreetBlock(String streetBlock) {
        this.streetBlock = streetBlock;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getContractFromDate() {
        return contractFromDate;
    }

    public void setContractFromDate(Date contractFromDate) {
        this.contractFromDate = contractFromDate;
    }

    public Date getContractToDate() {
        return contractToDate;
    }

    public void setContractToDate(Date contractToDate) {
        this.contractToDate = contractToDate;
    }

    public Long getDepositValue() {
        return depositValue;
    }

    public void setDepositValue(Long depositValue) {
        this.depositValue = depositValue;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public String getBankplusMobile() {
        return bankplusMobile;
    }

    public void setBankplusMobile(String bankplusMobile) {
        this.bankplusMobile = bankplusMobile;
    }

    public Long getStockNum() {
        return stockNum;
    }

    public void setStockNum(Long stockNum) {
        this.stockNum = stockNum;
    }

    public Long getStockNumImp() {
        return stockNumImp;
    }

    public void setStockNumImp(Long stockNumImp) {
        this.stockNumImp = stockNumImp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (shopId != null ? shopId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ShopIm1)) {
            return false;
        }
        ShopIm1 other = (ShopIm1) object;
        if ((this.shopId == null && other.shopId != null) || (this.shopId != null && !this.shopId.equals(other.shopId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.im1.model.ShopIm1[ shopId=" + shopId + " ]";
    }
    
}
