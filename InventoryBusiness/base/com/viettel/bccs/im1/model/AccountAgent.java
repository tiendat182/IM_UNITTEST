/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.im1.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Vietvt6
 */
@Entity
@Table(name = "ACCOUNT_AGENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountAgent.findAll", query = "SELECT a FROM AccountAgent a")})
public class AccountAgent implements Serializable {
public static enum COLUMNS {ACCOUNTID, ACCOUNTTYPE, AGENTID, BIRTHDATE, CENTREID, CHECKISDN, CHECKVAT, COMMBALANCE, COMMBALANCEDATE, CONTACTNO, CREATEDATE, CREDITEXPIRETIME, CREDITTIMELIMIT, CURRENTDEBTPAYMENT, DATECREATED, DISTRICT, EMAIL, FAX, ICCID, IDNO, ISDN, ISSUEDATE, ISSUEPLACE, LASTMODIFIED, LASTTIMERECOVER, LASTUPDATEIPADDRESS, LASTUPDATEKEY, LASTUPDATETIME, LASTUPDATEUSER, LIMITDEBTPAYMENT, LOGINFAILURECOUNT, MAXCREDITNUMBER, MAXCREDITVALUE, MINPAYPERMONTH, MPIN, MPINEXPIREDATE, MPINSTATUS, MSISDN, NUMADDMONEY, NUMPOSHPN, NUMPOSMOB, NUMPREHPN, NUMPREMOB, OBJECTDESTROY, OUTLETADDRESS, OWNERCODE, OWNERID, OWNERNAME, OWNERTYPE, PARENTID, PASSWORD, PRECINCT, PROVINCE, SAPCODE, SECUREQUESTION, SERIAL, SEX, STAFFCODE, STATUS, STATUSANYPAY, STATUSANYPAY2, TIN, TRADENAME, TRANSTYPE, USESALT, USERCREATED, USERRECOVER, WORKINGTIME, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ACCOUNT_ID")
    private Long accountId;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_CODE")
    private String ownerCode;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "USER_CREATED")
    private String userCreated;
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
    @Column(name = "MAX_CREDIT_NUMBER")
    private Long maxCreditNumber;
    @Column(name = "MAX_CREDIT_VALUE")
    private Long maxCreditValue;
    @Column(name = "CREDIT_TIME_LIMIT")
    private Long creditTimeLimit;
    @Column(name = "MIN_PAY_PER_MONTH")
    private Long minPayPerMonth;
    @Column(name = "CREDIT_EXPIRE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creditExpireTime;
    @Column(name = "STATUS_ANYPAY")
    private Long statusAnypay;
    @Column(name = "CHECK_VAT")
    private Long checkVat;
    @Column(name = "COMM_BALANCE")
    private Long commBalance;
    @Column(name = "COMM_BALANCE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commBalanceDate;
    @Column(name = "NUM_PRE_MOB")
    private Long numPreMob;
    @Column(name = "NUM_POS_MOB")
    private Long numPosMob;
    @Column(name = "NUM_PRE_HPN")
    private Long numPreHpn;
    @Column(name = "NUM_POS_HPN")
    private Long numPosHpn;
    @Column(name = "WORKING_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date workingTime;
    @Column(name = "TRANS_TYPE")
    private String transType;
    @Column(name = "OBJECT_DESTROY")
    private Long objectDestroy;
    @Column(name = "STATUS_ANYPAY2")
    private Long statusAnypay2;
    @Column(name = "CHECK_ISDN")
    private Long checkIsdn;
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;
    @Column(name = "LAST_UPDATE_IP_ADDRESS")
    private String lastUpdateIpAddress;
    @Column(name = "LAST_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    @Column(name = "LAST_UPDATE_KEY")
    private String lastUpdateKey;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CURRENT_DEBT_PAYMENT")
    private Long currentDebtPayment;
    @Column(name = "LIMIT_DEBT_PAYMENT")
    private Long limitDebtPayment;
    @Column(name = "USE_SALT")
    private String useSalt;
    @Column(name = "LAST_TIME_RECOVER")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTimeRecover;
    @Column(name = "USER_RECOVER")
    private Long userRecover;

    public AccountAgent() {
    }

    public AccountAgent(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
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

    public Long getMaxCreditNumber() {
        return maxCreditNumber;
    }

    public void setMaxCreditNumber(Long maxCreditNumber) {
        this.maxCreditNumber = maxCreditNumber;
    }

    public Long getMaxCreditValue() {
        return maxCreditValue;
    }

    public void setMaxCreditValue(Long maxCreditValue) {
        this.maxCreditValue = maxCreditValue;
    }

    public Long getCreditTimeLimit() {
        return creditTimeLimit;
    }

    public void setCreditTimeLimit(Long creditTimeLimit) {
        this.creditTimeLimit = creditTimeLimit;
    }

    public Long getMinPayPerMonth() {
        return minPayPerMonth;
    }

    public void setMinPayPerMonth(Long minPayPerMonth) {
        this.minPayPerMonth = minPayPerMonth;
    }

    public Date getCreditExpireTime() {
        return creditExpireTime;
    }

    public void setCreditExpireTime(Date creditExpireTime) {
        this.creditExpireTime = creditExpireTime;
    }

    public Long getStatusAnypay() {
        return statusAnypay;
    }

    public void setStatusAnypay(Long statusAnypay) {
        this.statusAnypay = statusAnypay;
    }

    public Long getCheckVat() {
        return checkVat;
    }

    public void setCheckVat(Long checkVat) {
        this.checkVat = checkVat;
    }

    public Long getCommBalance() {
        return commBalance;
    }

    public void setCommBalance(Long commBalance) {
        this.commBalance = commBalance;
    }

    public Date getCommBalanceDate() {
        return commBalanceDate;
    }

    public void setCommBalanceDate(Date commBalanceDate) {
        this.commBalanceDate = commBalanceDate;
    }

    public Long getNumPreMob() {
        return numPreMob;
    }

    public void setNumPreMob(Long numPreMob) {
        this.numPreMob = numPreMob;
    }

    public Long getNumPosMob() {
        return numPosMob;
    }

    public void setNumPosMob(Long numPosMob) {
        this.numPosMob = numPosMob;
    }

    public Long getNumPreHpn() {
        return numPreHpn;
    }

    public void setNumPreHpn(Long numPreHpn) {
        this.numPreHpn = numPreHpn;
    }

    public Long getNumPosHpn() {
        return numPosHpn;
    }

    public void setNumPosHpn(Long numPosHpn) {
        this.numPosHpn = numPosHpn;
    }

    public Date getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(Date workingTime) {
        this.workingTime = workingTime;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Long getObjectDestroy() {
        return objectDestroy;
    }

    public void setObjectDestroy(Long objectDestroy) {
        this.objectDestroy = objectDestroy;
    }

    public Long getStatusAnypay2() {
        return statusAnypay2;
    }

    public void setStatusAnypay2(Long statusAnypay2) {
        this.statusAnypay2 = statusAnypay2;
    }

    public Long getCheckIsdn() {
        return checkIsdn;
    }

    public void setCheckIsdn(Long checkIsdn) {
        this.checkIsdn = checkIsdn;
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

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateKey() {
        return lastUpdateKey;
    }

    public void setLastUpdateKey(String lastUpdateKey) {
        this.lastUpdateKey = lastUpdateKey;
    }

    public Long getCurrentDebtPayment() {
        return currentDebtPayment;
    }

    public void setCurrentDebtPayment(Long currentDebtPayment) {
        this.currentDebtPayment = currentDebtPayment;
    }

    public Long getLimitDebtPayment() {
        return limitDebtPayment;
    }

    public void setLimitDebtPayment(Long limitDebtPayment) {
        this.limitDebtPayment = limitDebtPayment;
    }

    public String getUseSalt() {
        return useSalt;
    }

    public void setUseSalt(String useSalt) {
        this.useSalt = useSalt;
    }

    public Date getLastTimeRecover() {
        return lastTimeRecover;
    }

    public void setLastTimeRecover(Date lastTimeRecover) {
        this.lastTimeRecover = lastTimeRecover;
    }

    public Long getUserRecover() {
        return userRecover;
    }

    public void setUserRecover(Long userRecover) {
        this.userRecover = userRecover;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountId != null ? accountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountAgent)) {
            return false;
        }
        AccountAgent other = (AccountAgent) object;
        if ((this.accountId == null && other.accountId != null) || (this.accountId != null && !this.accountId.equals(other.accountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.im1.model.AccountAgent[ accountId=" + accountId + " ]";
    }
    
}
