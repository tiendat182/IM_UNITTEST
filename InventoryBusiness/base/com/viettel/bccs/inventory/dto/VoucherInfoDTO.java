/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;

/**
 *
 * @author HoangAnh
 */
public class VoucherInfoDTO extends BaseDTO {

    private Long voucherInfoId;
    private String serial;
    private String pass;
    private Short status;
    private Date fromDate;
    private Date toDate;
    private String object;
    private Long amount;
    private Date createDate;
    private Date lastUpdate;
    private String staffCode;
    private Long statusConnect;
    private Short bonusStatus;


    public Long getVoucherInfoId() {
        return voucherInfoId;
    }

    public void setVoucherInfoId(Long voucherInfoId) {
        this.voucherInfoId = voucherInfoId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
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

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public Long getStatusConnect() {
        return statusConnect;
    }

    public void setStatusConnect(Long statusConnect) {
        this.statusConnect = statusConnect;
    }

    public Short getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Short bonusStatus) {
        this.bonusStatus = bonusStatus;
    }
}
