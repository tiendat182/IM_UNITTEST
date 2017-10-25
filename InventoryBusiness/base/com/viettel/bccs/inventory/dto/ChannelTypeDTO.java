package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class ChannelTypeDTO extends BaseDTO implements Serializable {
    private Long channelTypeId;
    private String name;
    private String objectType;
    private String isVtUnit;

    public String getKeySet() {
        return keySet;
    }

    public Long getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getIsVtUnit() {
        return isVtUnit;
    }

    public void setIsVtUnit(String isVtUnit) {
        this.isVtUnit = isVtUnit;
    }
}
