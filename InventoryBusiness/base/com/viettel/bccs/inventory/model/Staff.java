/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ThanhNT77
 */
@Entity
@Table(name = "STAFF")
@XmlRootElement
public class Staff implements Serializable {
public static enum COLUMNS {ADDRESS, AREACODE, BANKPLUSMOBILE, BIRTHDAY, BUSINESSLICENCE, BUSINESSMETHOD, CHANNELTYPEID, CONTRACTFROMDATE, CONTRACTMETHOD, CONTRACTNO, CONTRACTTODATE, DEPOSITVALUE, DISCOUNTPOLICY, DISTRICT, EMAIL, FILEATTACH, FILENAME, HASEQUIPMENT, HASTIN, HOME, IDISSUEDATE, IDISSUEPLACE, IDNO, ISDN, LASTLOCKTIME, LASTMODIFIED, LOCKSTATUS, NAME, NOTE, PIN, POINTOFSALE, POINTOFSALETYPE, PRECINCT, PRICEPOLICY, PROVINCE, SERIAL, SHOPID, SHOPOWNERID, STAFFCODE, STAFFID, STAFFOWNTYPE, STAFFOWNERID, STATUS, STOCKNUM, STOCKNUMIMP, STREET, STREETBLOCK, SUBOWNERID, SUBOWNERTYPE, TEL, TIN, TTNSCODE, TYPE, USERID, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Size(max = 300)
    @Column(name = "NAME")
    private String name;
    @Size(max = 1)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "BIRTHDAY")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Size(max = 20)
    @Column(name = "ID_NO")
    private String idNo;
    @Size(max = 100)
    @Column(name = "ID_ISSUE_PLACE")
    private String idIssuePlace;
    @Column(name = "ID_ISSUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date idIssueDate;
    @Size(max = 15)
    @Column(name = "TEL")
    private String tel;
    @Column(name = "TYPE")
    private Long type;
    @Size(max = 20)
    @Column(name = "SERIAL")
    private String serial;
    @Size(max = 15)
    @Column(name = "ISDN")
    private String isdn;
    @Size(max = 10)
    @Column(name = "PIN")
    private String pin;
    @Size(max = 500)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 5)
    @Column(name = "PROVINCE")
    private String province;
    @Size(max = 1)
    @Column(name = "STAFF_OWN_TYPE")
    private String staffOwnType;
    @Column(name = "STAFF_OWNER_ID")
    private Long staffOwnerId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;
    @Size(max = 20)
    @Column(name = "PRICE_POLICY")
    private String pricePolicy;
    @Size(max = 20)
    @Column(name = "DISCOUNT_POLICY")
    private String discountPolicy;
    @Size(max = 15)
    @Column(name = "POINT_OF_SALE")
    private String pointOfSale;
    @Column(name = "LOCK_STATUS")
    private Long lockStatus;
    @Column(name = "LAST_LOCK_TIME")
    @Temporal(TemporalType.DATE)
    private Date lastLockTime;
    @Column(name = "CONTRACT_METHOD")
    private Long contractMethod;
    @Column(name = "HAS_EQUIPMENT")
    private Long hasEquipment;
    @Size(max = 500)
    @Column(name = "NOTE")
    private String note;
    @Column(name = "BUSINESS_METHOD")
    private Long businessMethod;
    @Column(name = "HAS_TIN")
    private Long hasTin;
    @Size(max = 50)
    @Column(name = "TIN")
    private String tin;
    @Size(max = 5)
    @Column(name = "DISTRICT")
    private String district;
    @Size(max = 5)
    @Column(name = "PRECINCT")
    private String precinct;
    @Size(max = 20)
    @Column(name = "AREA_CODE")
    private String areaCode;
    @Size(max = 100)
    @Column(name = "STREET_BLOCK")
    private String streetBlock;
    @Size(max = 100)
    @Column(name = "STREET")
    private String street;
    @Size(max = 100)
    @Column(name = "HOME")
    private String home;
    @Column(name = "POINT_OF_SALE_TYPE")
    private Long pointOfSaleType;
    @Column(name = "STOCK_NUM_IMP")
    private Long stockNumImp;
    @Column(name = "STOCK_NUM")
    private Long stockNum;
    @Size(max = 200)
    @Column(name = "TTNS_CODE")
    private String ttnsCode;
    @Size(max = 100)
    @Column(name = "CONTRACT_NO")
    private String contractNo;
    @Size(max = 100)
    @Column(name = "FILE_NAME")
    private String fileName;
    /*
    @Lob
    @Column(name = "FILE_ATTACH")
    private Serializable fileAttach;*/
    @Column(name = "CONTRACT_FROM_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractFromDate;
    @Column(name = "CONTRACT_TO_DATE")
    @Temporal(TemporalType.DATE)
    private Date contractToDate;
    @Column(name = "DEPOSIT_VALUE")
    private Long depositValue;
    @Size(max = 20)
    @Column(name = "BANKPLUS_MOBILE")
    private String bankplusMobile;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 200)
    @Column(name = "BUSINESS_LICENCE")
    private String businessLicence;
    @Column(name = "SUB_OWNER_TYPE")
    private Long subOwnerType;
    @Column(name = "SUB_OWNER_ID")
    private Long subOwnerId;
    @Column(name = "LAST_MODIFIED")
    @Temporal(TemporalType.DATE)
    private Date lastModified;
    @Column(name = "SHOP_OWNER_ID")
    private Long shopOwnerId;
    @Column(name = "USER_ID")
    private Long userId;

    public Staff() {
    }

    public Staff(Long staffId) {
        this.staffId = staffId;
    }

    public Staff(Long staffId, Long shopId, String staffCode, Long channelTypeId) {
        this.staffId = staffId;
        this.shopId = shopId;
        this.staffCode = staffCode;
        this.channelTypeId = channelTypeId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStaffOwnType() {
        return staffOwnType;
    }

    public void setStaffOwnType(String staffOwnType) {
        this.staffOwnType = staffOwnType;
    }

    public Long getStaffOwnerId() {
        return staffOwnerId;
    }

    public void setStaffOwnerId(Long staffOwnerId) {
        this.staffOwnerId = staffOwnerId;
    }

    public Long getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public String getPricePolicy() {
        return pricePolicy;
    }

    public void setPricePolicy(String pricePolicy) {
        this.pricePolicy = pricePolicy;
    }

    public String getDiscountPolicy() {
        return discountPolicy;
    }

    public void setDiscountPolicy(String discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public String getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public Long getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Long lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Date getLastLockTime() {
        return lastLockTime;
    }

    public void setLastLockTime(Date lastLockTime) {
        this.lastLockTime = lastLockTime;
    }

    public Long getContractMethod() {
        return contractMethod;
    }

    public void setContractMethod(Long contractMethod) {
        this.contractMethod = contractMethod;
    }

    public Long getHasEquipment() {
        return hasEquipment;
    }

    public void setHasEquipment(Long hasEquipment) {
        this.hasEquipment = hasEquipment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getBusinessMethod() {
        return businessMethod;
    }

    public void setBusinessMethod(Long businessMethod) {
        this.businessMethod = businessMethod;
    }

    public Long getHasTin() {
        return hasTin;
    }

    public void setHasTin(Long hasTin) {
        this.hasTin = hasTin;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
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

    public Long getPointOfSaleType() {
        return pointOfSaleType;
    }

    public void setPointOfSaleType(Long pointOfSaleType) {
        this.pointOfSaleType = pointOfSaleType;
    }

    public Long getStockNumImp() {
        return stockNumImp;
    }

    public void setStockNumImp(Long stockNumImp) {
        this.stockNumImp = stockNumImp;
    }

    public Long getStockNum() {
        return stockNum;
    }

    public void setStockNum(Long stockNum) {
        this.stockNum = stockNum;
    }

    public String getTtnsCode() {
        return ttnsCode;
    }

    public void setTtnsCode(String ttnsCode) {
        this.ttnsCode = ttnsCode;
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

//    public Serializable getFileAttach() {
//        return fileAttach;
//    }
//
//    public void setFileAttach(Serializable fileAttach) {
//        this.fileAttach = fileAttach;
//    }

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

    public String getBankplusMobile() {
        return bankplusMobile;
    }

    public void setBankplusMobile(String bankplusMobile) {
        this.bankplusMobile = bankplusMobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public Long getSubOwnerType() {
        return subOwnerType;
    }

    public void setSubOwnerType(Long subOwnerType) {
        this.subOwnerType = subOwnerType;
    }

    public Long getSubOwnerId() {
        return subOwnerId;
    }

    public void setSubOwnerId(Long subOwnerId) {
        this.subOwnerId = subOwnerId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Long getShopOwnerId() {
        return shopOwnerId;
    }

    public void setShopOwnerId(Long shopOwnerId) {
        this.shopOwnerId = shopOwnerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffId != null ? staffId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Staff)) {
            return false;
        }
        Staff other = (Staff) object;
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.sale.model.Staff[ staffId=" + staffId + " ]";
    }
    
}
