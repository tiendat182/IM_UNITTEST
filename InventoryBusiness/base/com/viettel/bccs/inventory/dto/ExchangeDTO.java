package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;

public class ExchangeDTO extends BaseDTO implements Serializable {

    private String description;
    private Long exchangeId;
    private Long maxGroup;
    private String name;
    private Long serverId;
    private String status;
    private Long typeId;

    public String getKeySet() {
        return keySet; }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getExchangeId() {
        return this.exchangeId;
    }
    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }
    public Long getMaxGroup() {
        return this.maxGroup;
    }
    public void setMaxGroup(Long maxGroup) {
        this.maxGroup = maxGroup;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getServerId() {
        return this.serverId;
    }
    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Long getTypeId() {
        return this.typeId;
    }
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
