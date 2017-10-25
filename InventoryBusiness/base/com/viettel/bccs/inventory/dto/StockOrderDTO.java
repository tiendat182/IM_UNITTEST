package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockOrderDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Date approveDate;
    private Long approveStaffId;
    private Date cancelDate;
    private Date createDate;
    private Date refuseDate;
    private Long refuseStaffId;
    private Long shopId;
    private Long staffId;
    private Long status;
    private String stockOrderCode;
    private Long stockOrderId;
    private String staffCode;
    private String staffName;

    public Date getApproveDate() {
        return this.approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Long getApproveStaffId() {
        return this.approveStaffId;
    }

    public void setApproveStaffId(Long approveStaffId) {
        this.approveStaffId = approveStaffId;
    }

    public Date getCancelDate() {
        return this.cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getRefuseDate() {
        return this.refuseDate;
    }

    public void setRefuseDate(Date refuseDate) {
        this.refuseDate = refuseDate;
    }

    public Long getRefuseStaffId() {
        return this.refuseStaffId;
    }

    public void setRefuseStaffId(Long refuseStaffId) {
        this.refuseStaffId = refuseStaffId;
    }

    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getStockOrderCode() {
        return this.stockOrderCode;
    }

    public void setStockOrderCode(String stockOrderCode) {
        this.stockOrderCode = stockOrderCode;
    }

    public Long getStockOrderId() {
        return this.stockOrderId;
    }

    public void setStockOrderId(Long stockOrderId) {
        this.stockOrderId = stockOrderId;
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
}
