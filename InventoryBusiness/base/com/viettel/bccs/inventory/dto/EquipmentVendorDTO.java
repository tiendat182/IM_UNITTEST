package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;

public class EquipmentVendorDTO extends BaseDTO implements Serializable {

    private String description;
    private Long equipmentVendorId;
    private String status;
    private String vendorCode;
    private String vendorName;

    public String getKeySet() {
        return keySet; }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getEquipmentVendorId() {
        return this.equipmentVendorId;
    }
    public void setEquipmentVendorId(Long equipmentVendorId) {
        this.equipmentVendorId = equipmentVendorId;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getVendorCode() {
        return this.vendorCode;
    }
    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }
    public String getVendorName() {
        return this.vendorName;
    }
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
