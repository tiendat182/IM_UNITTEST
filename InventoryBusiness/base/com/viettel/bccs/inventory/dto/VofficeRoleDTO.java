package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class VofficeRoleDTO extends BaseDTO implements Serializable {

    private Date createDate;
    private String createUser;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private String roleCode;
    private String roleName;
    private String status;
    private Long vofficeRoleId;

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
    public String getRoleCode() {
        return this.roleCode;
    }
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    public String getRoleName() {
        return this.roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Long getVofficeRoleId() {
        return this.vofficeRoleId;
    }
    public void setVofficeRoleId(Long vofficeRoleId) {
        this.vofficeRoleId = vofficeRoleId;
    }
}
