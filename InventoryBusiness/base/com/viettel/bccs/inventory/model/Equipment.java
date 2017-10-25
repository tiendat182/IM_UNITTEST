/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author hoangnt14
 */
@Entity
@Table(name = "EQUIPMENT")
@NamedQueries({
    @NamedQuery(name = "Equipment.findAll", query = "SELECT e FROM Equipment e")})
public class Equipment implements Serializable {
public static enum COLUMNS {CODE, DESCRIPTION, EQUIPMENTID, EQUIPMENTTYPE, EQUIPMENTVENDORID, NAME, STATUS, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "EQUIPMENT_ID")
    private Long equipmentId;
    @Basic(optional = false)
    @Column(name = "EQUIPMENT_VENDOR_ID")
    private long equipmentVendorId;
    @Basic(optional = false)
    @Column(name = "EQUIPMENT_TYPE")
    private String equipmentType;
    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;

    public Equipment() {
    }

    public Equipment(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Equipment(Long equipmentId, long equipmentVendorId, String equipmentType) {
        this.equipmentId = equipmentId;
        this.equipmentVendorId = equipmentVendorId;
        this.equipmentType = equipmentType;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public long getEquipmentVendorId() {
        return equipmentVendorId;
    }

    public void setEquipmentVendorId(Long equipmentVendorId) {
        this.equipmentVendorId = equipmentVendorId;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (equipmentId != null ? equipmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equipment)) {
            return false;
        }
        Equipment other = (Equipment) object;
        if ((this.equipmentId == null && other.equipmentId != null) || (this.equipmentId != null && !this.equipmentId.equals(other.equipmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.Equipment[ equipmentId=" + equipmentId + " ]";
    }
    
}
