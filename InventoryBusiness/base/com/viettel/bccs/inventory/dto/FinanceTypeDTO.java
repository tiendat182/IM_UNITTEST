package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;
public class FinanceTypeDTO extends BaseDTO {

    private Date createDate;
    private String createUser;
    private Long dayNum;
    private String financeType;
    private Long id;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private String status;
    private String strDayNum;
    private String statusName;

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
    public Long getDayNum() {
        return this.dayNum;
    }
    public void setDayNum(Long dayNum) {
        this.dayNum = dayNum;
    }
    public String getFinanceType() {
        return this.financeType;
    }
    public void setFinanceType(String financeType) {
        this.financeType = financeType;
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

    public String getStrDayNum() {
        return strDayNum;
    }

    public void setStrDayNum(String strDayNum) {
        this.strDayNum = strDayNum;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
