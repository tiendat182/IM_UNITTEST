package com.viettel.fw.dto;

import java.io.Serializable;

/**
 * Created by LamNV5 on 5/27/2015.
 */
public class UserDTO implements Serializable{
    private String staffCode;
    private String shopCode;
    private Long staffId;
    private Long shopId;
    private Long userId;
    private String ipAddress;

    public UserDTO() {
    }

    public UserDTO(Long userId, Long staffId, String staffCode, Long shopId, String shopCode, String ipAddress) {
        this.userId = userId;
        this.staffCode = staffCode;
        this.shopCode = shopCode;
        this.staffId = staffId;
        this.shopId = shopId;
        this.ipAddress = ipAddress;
    }

    public UserDTO(Long staffId, String staffCode, Long shopId, String shopCode) {
        this.staffId = staffId;
        this.staffCode = staffCode;
        this.shopId = shopId;
        this.shopCode = shopCode;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


}
