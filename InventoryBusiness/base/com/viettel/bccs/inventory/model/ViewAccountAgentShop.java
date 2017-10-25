package com.viettel.bccs.inventory.model;

/**
 * Created by hoangnt14 on 1/5/2017.
 */

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author hoangnt14
 */
@Entity
@Table(name = "VIEW_ACCOUNT_AGENT_SHOP")

public class ViewAccountAgentShop implements Serializable {
public static enum COLUMNS {ACCOUNTID, ACCOUNTTYPE, ADDRESS, AGENTID, BIRTHDATE, CENTREID, CHANNELTYPEID, CHECKISDN, CHECKVAT, CONTACTNO, CREATEDATE, DATECREATED, DISTRICT, EMAIL, FAX, ICCID, ID, IDNO, ISDN, ISSUEDATE, ISSUEPLACE, LASTMODIFIED, LOGINFAILURECOUNT, MPIN, MPINEXPIREDATE, MPINSTATUS, MSISDN, NAME, NUMADDMONEY, OUTLETADDRESS, OWNERNAME, PARENTID, PARENTSHOPID, PASSWORD, PRECINCT, PROVINCE, SAPCODE, SECUREQUESTION, SERIAL, SEX, SHOPCODE, SHOPID, STAFFCODE, STATUS, STATUSCHANNEL, TELNUMBER, TIN, TRADENAME, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "SHOP_ID")
    private long shopId;
    @Column(name = "PARENT_SHOP_ID")
    private Long parentShopId;
    @Basic(optional = false)
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "TEL_NUMBER")
    private String telNumber;
    @Column(name = "ACCOUNT_ID")
    private Long accountId;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "ADDRESS")
    private String address;
    @Basic(optional = false)
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Column(name = "AGENT_ID")
    private Long agentId;
    @Column(name = "MSISDN")
    private String msisdn;
    @Column(name = "ICCID")
    private String iccid;
    @Column(name = "TRADE_NAME")
    private String tradeName;
    @Column(name = "OWNER_NAME")
    private String ownerName;
    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;
    @Column(name = "CONTACT_NO")
    private String contactNo;
    @Column(name = "OUTLET_ADDRESS")
    private String outletAddress;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "SECURE_QUESTION")
    private String secureQuestion;
    @Column(name = "MPIN")
    private String mpin;
    @Column(name = "SAP_CODE")
    private String sapCode;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LAST_MODIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "LOGIN_FAILURE_COUNT")
    private Long loginFailureCount;
    @Column(name = "ACCOUNT_TYPE")
    private String accountType;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "TIN")
    private String tin;
    @Column(name = "MPIN_EXPIRE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mpinExpireDate;
    @Column(name = "CENTRE_ID")
    private Long centreId;
    @Column(name = "ID_NO")
    private String idNo;
    @Column(name = "MPIN_STATUS")
    private String mpinStatus;
    @Column(name = "SEX")
    private String sex;
    @Column(name = "ISSUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueDate;
    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Column(name = "PROVINCE")
    private String province;
    @Column(name = "ISSUE_PLACE")
    private String issuePlace;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "DISTRICT")
    private String district;
    @Column(name = "PRECINCT")
    private String precinct;
    @Column(name = "NUM_ADD_MONEY")
    private Long numAddMoney;
    @Column(name = "CHECK_VAT")
    private Long checkVat;
    @Column(name = "STATUS_CHANNEL")
    private Long statusChannel;
    @Column(name = "CHECK_ISDN")
    private Long checkIsdn;
    @Id
    private Long id;

    public ViewAccountAgentShop() {
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public Long getParentShopId() {
        return parentShopId;
    }

    public void setParentShopId(Long parentShopId) {
        this.parentShopId = parentShopId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecureQuestion() {
        return secureQuestion;
    }

    public void setSecureQuestion(String secureQuestion) {
        this.secureQuestion = secureQuestion;
    }

    public String getMpin() {
        return mpin;
    }

    public void setMpin(String mpin) {
        this.mpin = mpin;
    }

    public String getSapCode() {
        return sapCode;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Long getLoginFailureCount() {
        return loginFailureCount;
    }

    public void setLoginFailureCount(Long loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public Date getMpinExpireDate() {
        return mpinExpireDate;
    }

    public void setMpinExpireDate(Date mpinExpireDate) {
        this.mpinExpireDate = mpinExpireDate;
    }

    public Long getCentreId() {
        return centreId;
    }

    public void setCentreId(Long centreId) {
        this.centreId = centreId;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMpinStatus() {
        return mpinStatus;
    }

    public void setMpinStatus(String mpinStatus) {
        this.mpinStatus = mpinStatus;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getIssuePlace() {
        return issuePlace;
    }

    public void setIssuePlace(String issuePlace) {
        this.issuePlace = issuePlace;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
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

    public Long getNumAddMoney() {
        return numAddMoney;
    }

    public void setNumAddMoney(Long numAddMoney) {
        this.numAddMoney = numAddMoney;
    }

    public Long getCheckVat() {
        return checkVat;
    }

    public void setCheckVat(Long checkVat) {
        this.checkVat = checkVat;
    }

    public Long getStatusChannel() {
        return statusChannel;
    }

    public void setStatusChannel(Long statusChannel) {
        this.statusChannel = statusChannel;
    }

    public Long getCheckIsdn() {
        return checkIsdn;
    }

    public void setCheckIsdn(Long checkIsdn) {
        this.checkIsdn = checkIsdn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

