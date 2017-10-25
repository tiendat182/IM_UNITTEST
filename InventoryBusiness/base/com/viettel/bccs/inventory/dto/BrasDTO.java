package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class BrasDTO extends BaseDTO implements Serializable {

    private String aaaIp;
    private Long brasId;
    private String code;
    private String description;
    private Long equipmentId;
    private String ip;
    private String name;
    private String port;
    private String slot;
    private String status;
    private Date createDatetime;
    private String createUser;
    private Date updateDatetime;
    private String updateUser;
    private String ipUpper;

    public String getKeySet() {
        return keySet; }
    public String getAaaIp() {
        return this.aaaIp;
    }
    public void setAaaIp(String aaaIp) {
        this.aaaIp = aaaIp;
    }
    public Long getBrasId() {
        return this.brasId;
    }
    public void setBrasId(Long brasId) {
        this.brasId = brasId;
    }
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
    public String getIp() {
        return this.ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPort() {
        return this.port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public String getSlot() {
        return this.slot;
    }
    public void setSlot(String slot) {
        this.slot = slot;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getIpUpper() {
        return ipUpper;
    }
    public void setIpUpper(String ipUpper) {
        this.ipUpper = ipUpper;
    }
    public Date getCreateDatetime() {
        return createDatetime;
    }
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    public String getCreateUser() {
        return createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public Date getUpdateDatetime() {
        return updateDatetime;
    }
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
    public String getUpdateUser() {
        return updateUser;
    }
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
