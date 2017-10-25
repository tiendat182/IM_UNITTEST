package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockDeliverDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Date createDate;
    private String createUser;
    private Long createUserId;
    private String createUserName;
    private Long newStaffId;
    private Long newStaffOwnerId;
    private String note;
    private Long oldStaffId;
    private Long oldStaffOwnerId;
    private Long ownerId;
    private Long ownerType;
    private Long status;
    private Long stockDeliverId;
    private Date updateDate;
    private String userName;
    private String passWord;
    private Long signFlowId;
    private String createDateStr;
    private String code;
    private Date fromDate;
    private Date toDate;
    private String statusName;
    private String ownerFullName;

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public String getCreateDateStr() {
        return createDate != null ? DateUtil.date2ddMMyyyyString(createDate) : "";
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Long getSignFlowId() {
        return signFlowId;
    }

    public void setSignFlowId(Long signFlowId) {
        this.signFlowId = signFlowId;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return this.createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getNewStaffId() {
        return this.newStaffId;
    }

    public void setNewStaffId(Long newStaffId) {
        this.newStaffId = newStaffId;
    }

    public Long getNewStaffOwnerId() {
        return this.newStaffOwnerId;
    }

    public void setNewStaffOwnerId(Long newStaffOwnerId) {
        this.newStaffOwnerId = newStaffOwnerId;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getOldStaffId() {
        return this.oldStaffId;
    }

    public void setOldStaffId(Long oldStaffId) {
        this.oldStaffId = oldStaffId;
    }

    public Long getOldStaffOwnerId() {
        return this.oldStaffOwnerId;
    }

    public void setOldStaffOwnerId(Long oldStaffOwnerId) {
        this.oldStaffOwnerId = oldStaffOwnerId;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStockDeliverId() {
        return this.stockDeliverId;
    }

    public void setStockDeliverId(Long stockDeliverId) {
        this.stockDeliverId = stockDeliverId;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
