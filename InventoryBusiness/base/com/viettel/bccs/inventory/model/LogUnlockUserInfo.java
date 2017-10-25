/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ThanhNT77
 */
@Entity
@Table(name = "LOG_UNLOCK_USER_INFO")
public class LogUnlockUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "UNLOCK_USER_INFO_ID")
    @SequenceGenerator(name = "LOG_UNLOCK_USER_INFO_GENERATOR", sequenceName = "LOG_UNLOCK_USER_INFO_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_UNLOCK_USER_INFO_GENERATOR")

    private Long unlockUserInfoId;
    @Column(name = "UNLOCK_SHOP_ID")
    private Long unlockShopId;
    @Column(name = "UNLOCK_STAFF_ID")
    private Long unlockStaffId;
    @Column(name = "UNLOCK_SHOP_CODE")
    private String unlockShopCode;
    @Column(name = "UNLOCK_SHOP_NAME")
    private String unlockShopName;
    @Column(name = "UNLOCK_STAFF_CODE")
    private String unlockStaffCode;
    @Column(name = "UNLOCK_STAFF_NAME")
    private String unlockStaffName;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Column(name = "SHOP_NAME")
    private String shopName;
    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Column(name = "STAFF_NAME")
    private String staffName;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "UNLOCK_SHOP_PATH")
    private String unlockShopPath;
    @Column(name = "SHOP_PATH")
    private String shopPath;


    public LogUnlockUserInfo() {
    }

    public Long getUnlockUserInfoId() {
        return unlockUserInfoId;
    }

    public Long getUnlockShopId() {
        return unlockShopId;
    }

    public void setUnlockShopId(Long unlockShopId) {
        this.unlockShopId = unlockShopId;
    }

    public Long getUnlockStaffId() {
        return unlockStaffId;
    }

    public void setUnlockStaffId(Long unlockStaffId) {
        this.unlockStaffId = unlockStaffId;
    }

    public String getUnlockShopCode() {
        return unlockShopCode;
    }

    public void setUnlockShopCode(String unlockShopCode) {
        this.unlockShopCode = unlockShopCode;
    }

    public String getUnlockShopName() {
        return unlockShopName;
    }

    public void setUnlockShopName(String unlockShopName) {
        this.unlockShopName = unlockShopName;
    }

    public String getUnlockStaffCode() {
        return unlockStaffCode;
    }

    public void setUnlockStaffCode(String unlockStaffCode) {
        this.unlockStaffCode = unlockStaffCode;
    }

    public String getUnlockStaffName() {
        return unlockStaffName;
    }

    public void setUnlockStaffName(String unlockStaffName) {
        this.unlockStaffName = unlockStaffName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getUnlockShopPath() {
        return unlockShopPath;
    }

    public void setUnlockShopPath(String unlockShopPath) {
        this.unlockShopPath = unlockShopPath;
    }

    public String getShopPath() {
        return shopPath;
    }

    public void setShopPath(String shopPath) {
        this.shopPath = shopPath;
    }

    public void setUnlockUserInfoId(Long unlockUserInfoId) {
        this.unlockUserInfoId = unlockUserInfoId;
    }


}
