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
 *
 * @author sinhhv
 */
@Entity
@Table(name = "AREA")
public class Area implements Serializable {
public static enum COLUMNS {AREACODE, AREAGROUP, CENTER, CREATEDATETIME, CREATEUSER, DISTRICT, FULLNAME, NAME, PARENTCODE, PRECINCT, PROVINCE, PROVINCECODE, PSTNCODE, STATUS, STREETBLOCK, UPDATEDATETIME, UPDATEUSER, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "AREA_CODE")
    private String areaCode;
    @Column(name = "PARENT_CODE")
    private String parentCode;
    @Column(name = "AREA_GROUP")
    private String areaGroup;
    @Column(name = "PROVINCE")
    private String province;
    @Column(name = "DISTRICT")
    private String district;
    @Column(name = "PRECINCT")
    private String precinct;
    @Column(name = "STREET_BLOCK")
    private String streetBlock;
    @Column(name = "NAME")
    private String name;
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "CENTER")
    private String center;
    @Column(name = "PSTN_CODE")
    private String pstnCode;
    @Column(name = "PROVINCE_CODE")
    private String provinceCode;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;

    public Area() {
    }

    public Area(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getAreaGroup() {
        return areaGroup;
    }

    public void setAreaGroup(String areaGroup) {
        this.areaGroup = areaGroup;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPrecinct() {
        return precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }

    public String getStreetBlock() {
        return streetBlock;
    }

    public void setStreetBlock(String streetBlock) {
        this.streetBlock = streetBlock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getPstnCode() {
        return pstnCode;
    }

    public void setPstnCode(String pstnCode) {
        this.pstnCode = pstnCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (areaCode != null ? areaCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Area)) {
            return false;
        }
        Area other = (Area) object;
        if ((this.areaCode == null && other.areaCode != null) || (this.areaCode != null && !this.areaCode.equals(other.areaCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.Area[ areaCode=" + areaCode + " ]";
    }
    
}
