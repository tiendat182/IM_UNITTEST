package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;

public class EquipmentDTO extends BaseDTO implements Serializable {

    private String code;
    private String description;
    private Long equipmentId;
    private String equipmentType;
    private long equipmentVendorId;
    private String name;
    private String status;

    public String getKeySet() {
        return keySet; }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
         this.code = code;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getEquipmentId() {
        return this.equipmentId;
    }
    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }
    public String getEquipmentType() {
        return this.equipmentType;
    }
    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }
    public long getEquipmentVendorId() {
        return this.equipmentVendorId;
    }
    public void setEquipmentVendorId(long equipmentVendorId) {
        this.equipmentVendorId = equipmentVendorId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
