package com.viettel.bccs.partner.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class AccountAgentDTO extends BaseDTO implements Serializable {

    private Long accountId;
    private String accountType;
    private Long agentId;
    private Date birthDate;
    private Long centreId;
    private Long checkIsdn;
    private Long checkVat;
    private Long commBalance;
    private Date commBalanceDate;
    private String contactNo;
    private Date createDate;
    private Date creditExpireTime;
    private Long creditTimeLimit;
    private BigDecimal currentDebtPayment;
    private Date dateCreated;
    private String district;
    private String email;
    private String fax;
    private String iccid;
    private String idNo;
    private String isdn;
    private Date issueDate;
    private String issuePlace;
    private Date lastModified;
    private Date lastTimeRecover;
    private String lastUpdateIpAddress;
    private String lastUpdateKey;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private BigDecimal limitDebtPayment;
    private Long loginFailureCount;
    private Long maxCreditNumber;
    private Long maxCreditValue;
    private Long minPayPerMonth;
    private String mpin;
    private Date mpinExpireDate;
    private String mpinStatus;
    private String msisdn;
    private Long numAddMoney;
    private Long numPosHpn;
    private Long numPosMob;
    private Long numPreHpn;
    private Long numPreMob;
    private Long objectDestroy;
    private String outletAddress;
    private String ownerCode;
    private Long ownerId;
    private String ownerName;
    private Long ownerType;
    private Long parentId;
    private String password;
    private String precinct;
    private String province;
    private String sapCode;
    private String secureQuestion;
    private String serial;
    private String sex;
    private String staffCode;
    private Long status;
    private Long statusAnypay;
    private Long statusAnypay2;
    private String tin;
    private String tradeName;
    private String transType;
    private String useSalt;
    private String userCreated;
    private Long userRecover;
    private Date workingTime;

    public String getKeySet() {
        return keySet; }
    public Long getAccountId() {
        return this.accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public String getAccountType() {
        return this.accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public Long getAgentId() {
        return this.agentId;
    }
    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
    public Date getBirthDate() {
        return this.birthDate;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    public Long getCentreId() {
        return this.centreId;
    }
    public void setCentreId(Long centreId) {
        this.centreId = centreId;
    }
    public Long getCheckIsdn() {
        return this.checkIsdn;
    }
    public void setCheckIsdn(Long checkIsdn) {
        this.checkIsdn = checkIsdn;
    }
    public Long getCheckVat() {
        return this.checkVat;
    }
    public void setCheckVat(Long checkVat) {
        this.checkVat = checkVat;
    }
    public Long getCommBalance() {
        return this.commBalance;
    }
    public void setCommBalance(Long commBalance) {
        this.commBalance = commBalance;
    }
    public Date getCommBalanceDate() {
        return this.commBalanceDate;
    }
    public void setCommBalanceDate(Date commBalanceDate) {
        this.commBalanceDate = commBalanceDate;
    }
    public String getContactNo() {
        return this.contactNo;
    }
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getCreditExpireTime() {
        return this.creditExpireTime;
    }
    public void setCreditExpireTime(Date creditExpireTime) {
        this.creditExpireTime = creditExpireTime;
    }
    public Long getCreditTimeLimit() {
        return this.creditTimeLimit;
    }
    public void setCreditTimeLimit(Long creditTimeLimit) {
        this.creditTimeLimit = creditTimeLimit;
    }
    public BigDecimal getCurrentDebtPayment() {
        return this.currentDebtPayment;
    }
    public void setCurrentDebtPayment(BigDecimal currentDebtPayment) {
        this.currentDebtPayment = currentDebtPayment;
    }
    public Date getDateCreated() {
        return this.dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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
    public String getIccid() {
        return this.iccid;
    }
    public void setIccid(String iccid) {
        this.iccid = iccid;
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
    public Date getIssueDate() {
        return this.issueDate;
    }
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
    public String getIssuePlace() {
        return this.issuePlace;
    }
    public void setIssuePlace(String issuePlace) {
        this.issuePlace = issuePlace;
    }
    public Date getLastModified() {
        return this.lastModified;
    }
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    public Date getLastTimeRecover() {
        return this.lastTimeRecover;
    }
    public void setLastTimeRecover(Date lastTimeRecover) {
        this.lastTimeRecover = lastTimeRecover;
    }
    public String getLastUpdateIpAddress() {
        return this.lastUpdateIpAddress;
    }
    public void setLastUpdateIpAddress(String lastUpdateIpAddress) {
        this.lastUpdateIpAddress = lastUpdateIpAddress;
    }
    public String getLastUpdateKey() {
        return this.lastUpdateKey;
    }
    public void setLastUpdateKey(String lastUpdateKey) {
        this.lastUpdateKey = lastUpdateKey;
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
    public BigDecimal getLimitDebtPayment() {
        return this.limitDebtPayment;
    }
    public void setLimitDebtPayment(BigDecimal limitDebtPayment) {
        this.limitDebtPayment = limitDebtPayment;
    }
    public Long getLoginFailureCount() {
        return this.loginFailureCount;
    }
    public void setLoginFailureCount(Long loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }
    public Long getMaxCreditNumber() {
        return this.maxCreditNumber;
    }
    public void setMaxCreditNumber(Long maxCreditNumber) {
        this.maxCreditNumber = maxCreditNumber;
    }
    public Long getMaxCreditValue() {
        return this.maxCreditValue;
    }
    public void setMaxCreditValue(Long maxCreditValue) {
        this.maxCreditValue = maxCreditValue;
    }
    public Long getMinPayPerMonth() {
        return this.minPayPerMonth;
    }
    public void setMinPayPerMonth(Long minPayPerMonth) {
        this.minPayPerMonth = minPayPerMonth;
    }
    public String getMpin() {
        return this.mpin;
    }
    public void setMpin(String mpin) {
        this.mpin = mpin;
    }
    public Date getMpinExpireDate() {
        return this.mpinExpireDate;
    }
    public void setMpinExpireDate(Date mpinExpireDate) {
        this.mpinExpireDate = mpinExpireDate;
    }
    public String getMpinStatus() {
        return this.mpinStatus;
    }
    public void setMpinStatus(String mpinStatus) {
        this.mpinStatus = mpinStatus;
    }
    public String getMsisdn() {
        return this.msisdn;
    }
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    public Long getNumAddMoney() {
        return this.numAddMoney;
    }
    public void setNumAddMoney(Long numAddMoney) {
        this.numAddMoney = numAddMoney;
    }
    public Long getNumPosHpn() {
        return this.numPosHpn;
    }
    public void setNumPosHpn(Long numPosHpn) {
        this.numPosHpn = numPosHpn;
    }
    public Long getNumPosMob() {
        return this.numPosMob;
    }
    public void setNumPosMob(Long numPosMob) {
        this.numPosMob = numPosMob;
    }
    public Long getNumPreHpn() {
        return this.numPreHpn;
    }
    public void setNumPreHpn(Long numPreHpn) {
        this.numPreHpn = numPreHpn;
    }
    public Long getNumPreMob() {
        return this.numPreMob;
    }
    public void setNumPreMob(Long numPreMob) {
        this.numPreMob = numPreMob;
    }
    public Long getObjectDestroy() {
        return this.objectDestroy;
    }
    public void setObjectDestroy(Long objectDestroy) {
        this.objectDestroy = objectDestroy;
    }
    public String getOutletAddress() {
        return this.outletAddress;
    }
    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }
    public String getOwnerCode() {
        return this.ownerCode;
    }
    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }
    public Long getOwnerId() {
        return this.ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public String getOwnerName() {
        return this.ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public Long getOwnerType() {
        return this.ownerType;
    }
    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }
    public Long getParentId() {
        return this.parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPrecinct() {
        return this.precinct;
    }
    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }
    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getSapCode() {
        return this.sapCode;
    }
    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }
    public String getSecureQuestion() {
        return this.secureQuestion;
    }
    public void setSecureQuestion(String secureQuestion) {
        this.secureQuestion = secureQuestion;
    }
    public String getSerial() {
        return this.serial;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getStaffCode() {
        return this.staffCode;
    }
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }
    public Long getStatus() {
        return this.status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public Long getStatusAnypay() {
        return this.statusAnypay;
    }
    public void setStatusAnypay(Long statusAnypay) {
        this.statusAnypay = statusAnypay;
    }
    public Long getStatusAnypay2() {
        return this.statusAnypay2;
    }
    public void setStatusAnypay2(Long statusAnypay2) {
        this.statusAnypay2 = statusAnypay2;
    }
    public String getTin() {
        return this.tin;
    }
    public void setTin(String tin) {
        this.tin = tin;
    }
    public String getTradeName() {
        return this.tradeName;
    }
    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }
    public String getTransType() {
        return this.transType;
    }
    public void setTransType(String transType) {
        this.transType = transType;
    }
    public String getUseSalt() {
        return this.useSalt;
    }
    public void setUseSalt(String useSalt) {
        this.useSalt = useSalt;
    }
    public String getUserCreated() {
        return this.userCreated;
    }
    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }
    public Long getUserRecover() {
        return this.userRecover;
    }
    public void setUserRecover(Long userRecover) {
        this.userRecover = userRecover;
    }
    public Date getWorkingTime() {
        return this.workingTime;
    }
    public void setWorkingTime(Date workingTime) {
        this.workingTime = workingTime;
    }
}
