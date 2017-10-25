package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class RevokeKitTransDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Date addMoneyDate;
    private Long addMoneyStatus;
    private Date createDate;
    private Long createStaffId;
    private Long id;
    private Long createShopId;

    public Date getAddMoneyDate() {
        return this.addMoneyDate;
    }

    public void setAddMoneyDate(Date addMoneyDate) {
        this.addMoneyDate = addMoneyDate;
    }

    public Long getAddMoneyStatus() {
        return this.addMoneyStatus;
    }

    public void setAddMoneyStatus(Long addMoneyStatus) {
        this.addMoneyStatus = addMoneyStatus;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateStaffId() {
        return this.createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Long getCreateShopId() {
        return createShopId;
    }

    public void setCreateShopId(Long createShopId) {
        this.createShopId = createShopId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
