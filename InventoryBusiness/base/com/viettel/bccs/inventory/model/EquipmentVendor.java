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
@Table(name = "EQUIPMENT_VENDOR")
@NamedQueries({
    @NamedQuery(name = "EquipmentVendor.findAll", query = "SELECT e FROM EquipmentVendor e")})
public class EquipmentVendor implements Serializable {
public static enum COLUMNS {DESCRIPTION, EQUIPMENTVENDORID, STATUS, VENDORCODE, VENDORNAME, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "EQUIPMENT_VENDOR_ID")
    private Long equipmentVendorId;
    @Column(name = "VENDOR_CODE")
    private String vendorCode;
    @Column(name = "VENDOR_NAME")
    private String vendorName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;

    public EquipmentVendor() {
    }

    public EquipmentVendor(Long equipmentVendorId) {
        this.equipmentVendorId = equipmentVendorId;
    }

    public Long getEquipmentVendorId() {
        return equipmentVendorId;
    }

    public void setEquipmentVendorId(Long equipmentVendorId) {
        this.equipmentVendorId = equipmentVendorId;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
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
        hash += (equipmentVendorId != null ? equipmentVendorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipmentVendor)) {
            return false;
        }
        EquipmentVendor other = (EquipmentVendor) object;
        if ((this.equipmentVendorId == null && other.equipmentVendorId != null) || (this.equipmentVendorId != null && !this.equipmentVendorId.equals(other.equipmentVendorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.EquipmentVendor[ equipmentVendorId=" + equipmentVendorId + " ]";
    }
    
}
