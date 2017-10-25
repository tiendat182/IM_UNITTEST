package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class ReportChangeGoodsDTO extends BaseDTO implements Serializable {

    private Date createDate;
    private Date dateSendSms;
    private Long id;
    private String isdn;
    private String prodOfferCode;
    private Long prodOfferId;
    private String prodOfferName;
    private Long reasonId;
    private String serialNew;
    private String serialOld;
    private String shopCode;
    private Long shopId;
    private String shopName;
    private String staffCode;
    private Long staffId;
    private String staffName;
    private Long stockTransId;
    private Long telecomServiceId;
    private Long totalSendSms;

    public String getKeySet() {
        return keySet; }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Date getDateSendSms() {
        return this.dateSendSms;
    }
    public void setDateSendSms(Date dateSendSms) {
        this.dateSendSms = dateSendSms;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getIsdn() {
        return this.isdn;
    }
    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }
    public String getProdOfferCode() {
        return this.prodOfferCode;
    }
    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }
    public Long getProdOfferId() {
        return this.prodOfferId;
    }
    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }
    public String getProdOfferName() {
        return this.prodOfferName;
    }
    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }
    public Long getReasonId() {
        return this.reasonId;
    }
    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }
    public String getSerialNew() {
        return this.serialNew;
    }
    public void setSerialNew(String serialNew) {
        this.serialNew = serialNew;
    }
    public String getSerialOld() {
        return this.serialOld;
    }
    public void setSerialOld(String serialOld) {
        this.serialOld = serialOld;
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
    public String getShopName() {
        return this.shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getStaffCode() {
        return this.staffCode;
    }
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }
    public Long getStaffId() {
        return this.staffId;
    }
    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
    public String getStaffName() {
        return this.staffName;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    public Long getStockTransId() {
        return this.stockTransId;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
    public Long getTelecomServiceId() {
        return this.telecomServiceId;
    }
    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }
    public Long getTotalSendSms() {
        return this.totalSendSms;
    }
    public void setTotalSendSms(Long totalSendSms) {
        this.totalSendSms = totalSendSms;
    }
}
