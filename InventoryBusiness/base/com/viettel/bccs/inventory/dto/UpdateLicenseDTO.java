package com.viettel.bccs.inventory.dto;

import java.util.Date;

/**
 * Created by hoangnt14 on 7/4/2016.
 */
public class UpdateLicenseDTO {
    private Long shopAntiId;
    private Date dateImport;
    private String updateType;
    private String updateStyle;
    private Long prodOfferingId;
    private String serial;
    private String licenseKey;
    private String status;
    private String desc;
    private String stt;

    public Long getShopAntiId() {
        return shopAntiId;
    }

    public void setShopAntiId(Long shopAntiId) {
        this.shopAntiId = shopAntiId;
    }

    public Date getDateImport() {
        return dateImport;
    }

    public void setDateImport(Date dateImport) {
        this.dateImport = dateImport;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getUpdateStyle() {
        return updateStyle;
    }

    public void setUpdateStyle(String updateStyle) {
        this.updateStyle = updateStyle;
    }

    public Long getProdOfferingId() {
        return prodOfferingId;
    }

    public void setProdOfferingId(Long prodOfferingId) {
        this.prodOfferingId = prodOfferingId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }
}
