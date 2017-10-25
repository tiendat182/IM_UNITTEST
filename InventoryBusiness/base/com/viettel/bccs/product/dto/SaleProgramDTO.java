package com.viettel.bccs.product.dto;

import com.google.common.base.Joiner;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class SaleProgramDTO extends BaseDTO implements Serializable {

    public static enum COLUMNS {CODE, CREATEDATETIME, CREATEUSER, NAME, SALEPROGRAMGROUPID, SALEPROGRAMID, STATUS, TYPE, UPDATEDATETIME, UPDATEUSER, EXCLUSE_ID_LIST}

    ;
    private String code;
    private Date createDatetime;
    private String createUser;
    private String name;
    private Long saleProgramGroupId;
    private Long saleProgramId;
    private String status;
    private String type;
    private Date updateDatetime;
    private String updateUser;

    public String getLabel() {
        return Joiner.on(" - ").skipNulls().join(code, name);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSaleProgramGroupId() {
        return this.saleProgramGroupId;
    }

    public void setSaleProgramGroupId(Long saleProgramGroupId) {
        this.saleProgramGroupId = saleProgramGroupId;
    }

    public Long getSaleProgramId() {
        return this.saleProgramId;
    }

    public void setSaleProgramId(Long saleProgramId) {
        this.saleProgramId = saleProgramId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
