package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.RequiredRoleMap;

import java.util.Date;
import java.util.List;

/**
 * Created by sinhhv on 2/4/2016.
 */
public class InfoSearchIsdnDTO {
    private Long telecomServiceId;
    private boolean inputFile = false;
    private String attachFileName;
    private String ownerType;
    private String ownerId;
    private String ownerCode;
    private String ownerName;
    private String ownerIdLock;
    private String ownerCodeLock;
    private String ownerNameLock;
    private String startRange;
    private String endRange;
    private String status;
    private Long filterrRuleId;
    private Long groupFilterRuleId;
    private Long ruleNiceIsdnId;
    private String fromPrice;
    private String toPrice;
    private Long proOfferId;
    private String isdnType;
    private String isdnLike;
    private boolean checkIsdnLike = false;
    private List<Long> listIsdnReadFromFile;
    private Date fromDate;
    private Date toDate;
    private RequiredRoleMap requiredRoleMap;// quyen tra cuu kho dac biet
    private RequiredRoleMap roleTypeKho;// quyen tra cuu kho mac dinh
    private boolean viewSpecialIsdn=false;

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public boolean getInputFile() {
        return inputFile;
    }

    public void setInputFile(boolean inputFile) {
        this.inputFile = inputFile;
    }

    public List<Long> getListIsdnReadFromFile() {
        return listIsdnReadFromFile;
    }

    public void setListIsdnReadFromFile(List<Long> listIsdnReadFromFile) {
        this.listIsdnReadFromFile = listIsdnReadFromFile;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFilterrRuleId() {
        return filterrRuleId;
    }

    public void setFilterrRuleId(Long filterrRuleId) {
        this.filterrRuleId = filterrRuleId;
    }

    public Long getGroupFilterRuleId() {
        return groupFilterRuleId;
    }

    public void setGroupFilterRuleId(Long groupFilterRuleId) {
        this.groupFilterRuleId = groupFilterRuleId;
    }

    public Long getRuleNiceIsdnId() {
        return ruleNiceIsdnId;
    }

    public void setRuleNiceIsdnId(Long ruleNiceIsdnId) {
        this.ruleNiceIsdnId = ruleNiceIsdnId;
    }

    public String getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(String fromPrice) {
        this.fromPrice = fromPrice;
    }

    public String getToPrice() {
        return toPrice;
    }

    public void setToPrice(String toPrice) {
        this.toPrice = toPrice;
    }

    public Long getProOfferId() {
        return proOfferId;
    }

    public void setProOfferId(Long proOfferId) {
        this.proOfferId = proOfferId;
    }

    public String getIsdnType() {
        return isdnType;
    }

    public String getIsdnLike() {
        return isdnLike;
    }

    public void setIsdnLike(String isdnLike) {
        this.isdnLike = isdnLike;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public void setIsdnType(String isdnType) {
        this.isdnType = isdnType;
    }

    public boolean getCheckIsdnLike() {
        return checkIsdnLike;
    }

    public void setCheckIsdnLike(boolean checkIsdnLike) {
        this.checkIsdnLike = checkIsdnLike;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public RequiredRoleMap getRequiredRoleMap() {
        return requiredRoleMap;
    }

    public void setRequiredRoleMap(RequiredRoleMap requiredRoleMap) {
        this.requiredRoleMap = requiredRoleMap;
    }

    public RequiredRoleMap getRoleTypeKho() {
        return roleTypeKho;
    }

    public void setRoleTypeKho(RequiredRoleMap roleTypeKho) {
        this.roleTypeKho = roleTypeKho;
    }

    public String getFullNameOwner() {
        if (!DataUtil.isNullObject(this.ownerCode) && !DataUtil.isNullObject(this.ownerName)) {
            return this.ownerCode + " - " + this.ownerName;
        }
        return "";
    }

    public boolean getViewSpecialIsdn() {
        return viewSpecialIsdn;
    }

    public void setViewSpecialIsdn(boolean viewSpecialIsdn) {
        this.viewSpecialIsdn = viewSpecialIsdn;
    }

    public String getOwnerIdLock() {
        return ownerIdLock;
    }

    public void setOwnerIdLock(String ownerIdLock) {
        this.ownerIdLock = ownerIdLock;
    }

    public String getOwnerCodeLock() {
        return ownerCodeLock;
    }

    public void setOwnerCodeLock(String ownerCodeLock) {
        this.ownerCodeLock = ownerCodeLock;
    }

    public String getOwnerNameLock() {
        return ownerNameLock;
    }

    public void setOwnerNameLock(String ownerNameLock) {
        this.ownerNameLock = ownerNameLock;
    }
}
