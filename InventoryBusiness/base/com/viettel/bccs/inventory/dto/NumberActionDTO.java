package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class NumberActionDTO extends BaseDTO implements Serializable, Comparable<NumberActionDTO> {

    private String actionType;
    private Date createDate;
    private String fromIsdn;
    private String note;
    private Long numberActionId;
    private Long telecomServiceId;
    private Long reasonId;
    private String toIsdn;
    private String userCreate;
    private String userIpAddress;
    private String serviceType;
    private String status;

    public String getKeySet() {
        return keySet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActionType() {
        return this.actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFromIsdn() {
        return this.fromIsdn;
    }

    public void setFromIsdn(String fromIsdn) {
        this.fromIsdn = fromIsdn;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getNumberActionId() {
        return this.numberActionId;
    }

    public void setNumberActionId(Long numberActionId) {
        this.numberActionId = numberActionId;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public Long getReasonId() {
        return this.reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getToIsdn() {
        return this.toIsdn;
    }

    public void setToIsdn(String toIsdn) {
        this.toIsdn = toIsdn;
    }

    public String getUserCreate() {
        return this.userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserIpAddress() {
        return this.userIpAddress;
    }

    public void setUserIpAddress(String userIpAddress) {
        this.userIpAddress = userIpAddress;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public int compareTo(NumberActionDTO o) {
        return o.getCreateDate().compareTo(this.getCreateDate());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
