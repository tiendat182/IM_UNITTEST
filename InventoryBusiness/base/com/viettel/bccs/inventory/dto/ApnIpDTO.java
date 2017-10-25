package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class ApnIpDTO extends BaseDTO implements Serializable {

    private Long apnId;
    private Long apnIpId;
    private String centerCode;
    private Date createTime;
    private String createUser;
    private String ip;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private Long status;
    private String subNet;
    private String apnStr;

    public Long getApnId() {
        return this.apnId;
    }

    public void setApnId(Long apnId) {
        this.apnId = apnId;
    }

    public Long getApnIpId() {
        return this.apnIpId;
    }

    public void setApnIpId(Long apnIpId) {
        this.apnIpId = apnIpId;
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

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getSubNet() {
        return this.subNet;
    }

    public void setSubNet(String subNet) {
        this.subNet = subNet;
    }

    public String getKeySet() {
        return keySet;
    }

    public String getApnStr() {
        return apnStr;
    }

    public void setApnStr(String apnStr) {
        this.apnStr = apnStr;
    }
}
