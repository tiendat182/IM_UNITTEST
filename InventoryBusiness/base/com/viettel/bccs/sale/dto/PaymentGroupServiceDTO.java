package com.viettel.bccs.sale.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class PaymentGroupServiceDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private String code;
    private Date createDate;
    private String createUser;
    private Long id;
    private Date lastUpdateDate;
    private String lastUpdateUser;
    private String name;
    private Long status;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
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

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
