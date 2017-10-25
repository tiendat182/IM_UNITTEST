package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockIsdnVtShopDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private String address;
    private String areaCode;
    private String contactIsdn;
    private Date createDate;
    private String customerName;
    private String idNo;
    private String isdn;
    private Date lastModify;
    private String otp;
    private Long staffUpdate;
    private String status;
    private String viettelshopId;
    private Date expiredDate;
    private String payStatus;
    private Long requestId;
    private String valid;
    private String updateUser;

    

    public StockIsdnVtShopDTO(String isdn) {
        this.isdn = isdn;
    }

    public StockIsdnVtShopDTO(String isdn, String updateUser) {
        this.isdn = isdn;
        this.updateUser = updateUser;
    }

    public StockIsdnVtShopDTO() {
    }


    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getContactIsdn() {
        return this.contactIsdn;
    }

    public void setContactIsdn(String contactIsdn) {
        this.contactIsdn = contactIsdn;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIdNo() {
        return this.idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIsdn() {
        return this.isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public Date getLastModify() {
        return this.lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public String getOtp() {
        return this.otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Long getStaffUpdate() {
        return this.staffUpdate;
    }

    public void setStaffUpdate(Long staffUpdate) {
        this.staffUpdate = staffUpdate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getViettelshopId() {
        return this.viettelshopId;
    }

    public void setViettelshopId(String viettelshopId) {
        this.viettelshopId = viettelshopId;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
