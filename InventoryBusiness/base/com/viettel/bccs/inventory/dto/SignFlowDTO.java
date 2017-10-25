package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class SignFlowDTO extends BaseDTO implements Serializable {

    private Date createDate;
    private String createUser;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private String name;
    private String shopCode;
    private Long shopId;
    private Long signFlowId;
    private String status;
    private String email;
    private Long vofficeRoleId;
    private Long currentShopId;

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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getShopCode() {
        return this.shopCode;
    }
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
    public Long getShopId() {
        return this.shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public Long getSignFlowId() {
        return this.signFlowId;
    }
    public void setSignFlowId(Long signFlowId) {
        this.signFlowId = signFlowId;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getVofficeRoleId() {
        return vofficeRoleId;
    }

    public void setVofficeRoleId(Long vofficeRoleId) {
        this.vofficeRoleId = vofficeRoleId;
    }

    public Long getCurrentShopId() {
        return currentShopId;
    }

    public void setCurrentShopId(Long currentShopId) {
        this.currentShopId = currentShopId;
    }
}
