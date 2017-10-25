package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class AreaDTO extends BaseDTO implements Serializable {

    private String areaCode;
    private String areaGroup;
    private String center;
    private Date createDatetime;
    private String createUser;
    private String district;
    private String fullName;
    private String name;
    private String parentCode;
    private String precinct;
    private String province;
    private String provinceCode;
    private String pstnCode;
    private Short status;
    private String streetBlock;
    private Date updateDatetime;
    private String updateUser;
    private String areaCodeUpper;
    private String fullNameUpper;

    public AreaDTO() {
    }

    public String getKeySet() {
        return keySet; }
    public String getAreaCode() {
        return this.areaCode;
    }
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    public String getAreaGroup() {
        return this.areaGroup;
    }
    public void setAreaGroup(String areaGroup) {
        this.areaGroup = areaGroup;
    }
    public String getCenter() {
        return this.center;
    }
    public void setCenter(String center) {
        this.center = center;
    }
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    public String getCreateUser() {
        return this.createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public String getDistrict() {
        return this.district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getFullName() {
        return this.fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getParentCode() {
        return this.parentCode;
    }
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
    public String getPrecinct() {
        return this.precinct;
    }
    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }
    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getProvinceCode() {
        return this.provinceCode;
    }
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
    public String getPstnCode() {
        return this.pstnCode;
    }
    public void setPstnCode(String pstnCode) {
        this.pstnCode = pstnCode;
    }
    public Short getStatus() {
        return this.status;
    }
    public void setStatus(Short status) {
        this.status = status;
    }
    public String getStreetBlock() {
        return this.streetBlock;
    }
    public void setStreetBlock(String streetBlock) {
        this.streetBlock = streetBlock;
    }
    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
    public String getUpdateUser() {
        return this.updateUser;
    }
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getAreaCodeUpper() {
        return areaCodeUpper;
    }

    public void setAreaCodeUpper(String areaCodeUpper) {
        this.areaCodeUpper = areaCodeUpper;
    }

    public String getFullNameUpper() {
        return fullNameUpper;
    }

    public void setFullNameUpper(String fullNameUpper) {
        this.fullNameUpper = fullNameUpper;
    }
}
