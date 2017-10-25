package com.viettel.bccs.inventory.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 6/30/2016.
 */
public class UpdatePincodeDTO {
    private Long shopPincodeId;
    private Date dateImport;
    private String updateType;
    private String updateStyle;
    private Long prodOfferingId;
    private String serial;
    private String pincode;
    private String status;
    private String desc;
    private String stt;

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

    public Long getShopPincodeId() {
        return shopPincodeId;
    }

    public void setShopPincodeId(Long shopPincodeId) {
        this.shopPincodeId = shopPincodeId;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
