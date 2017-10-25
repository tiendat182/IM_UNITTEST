package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;
public class DebitLevelDTO extends BaseDTO {

    private Date createDate;
    private String createUser;
    private Long debitAmount;
    private String debitDayType;
    private String debitLevel;
    private Long id;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private String status;
    public String getKeySet() {
        return keySet; }
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
    public Long getDebitAmount() {
        return this.debitAmount;
    }
    public void setDebitAmount(Long debitAmount) {
        this.debitAmount = debitAmount;
    }
    public String getDebitDayType() {
        return this.debitDayType;
    }
    public void setDebitDayType(String debitDayType) {
        this.debitDayType = debitDayType;
    }
    public String getDebitLevel() {
        return this.debitLevel;
    }
    public void setDebitLevel(String debitLevel) {
        this.debitLevel = debitLevel;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
