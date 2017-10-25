package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class ApnDTO extends BaseDTO implements Serializable {

    private String apnCode;
    private Long apnId;
    private String apnName;
    private String centerCode;
    private Date createTime;
    private String createUser;
    private String description;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private Long status;

    public String getApnCode() {
        return this.apnCode;
    }

    public void setApnCode(String apnCode) {
        this.apnCode = apnCode;
    }

    public Long getApnId() {
        return this.apnId;
    }

    public void setApnId(Long apnId) {
        this.apnId = apnId;
    }

    public String getApnName() {
        return this.apnName;
    }

    public void setApnName(String apnName) {
        this.apnName = apnName;
    }

    public String getCenterCode() {
        return this.centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getKeySet() {
        return keySet;
    }
}
