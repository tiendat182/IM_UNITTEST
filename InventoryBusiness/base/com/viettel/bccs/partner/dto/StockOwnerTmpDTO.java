package com.viettel.bccs.partner.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;

public class StockOwnerTmpDTO extends BaseDTO implements Serializable {

    private Long agentId;
    private Long channelTypeId;
    private String code;
    private Long currentDebit;
    private Long dateReset;
    private Long maxDebit;
    private String name;
    private Long ownerId;
    private String ownerType;
    private Long stockId;

    public String getKeySet() {
        return keySet; }
    public Long getAgentId() {
        return this.agentId;
    }
    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
    public Long getChannelTypeId() {
        return this.channelTypeId;
    }
    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Long getCurrentDebit() {
        return this.currentDebit;
    }
    public void setCurrentDebit(Long currentDebit) {
        this.currentDebit = currentDebit;
    }
    public Long getDateReset() {
        return this.dateReset;
    }
    public void setDateReset(Long dateReset) {
        this.dateReset = dateReset;
    }
    public Long getMaxDebit() {
        return this.maxDebit;
    }
    public void setMaxDebit(Long maxDebit) {
        this.maxDebit = maxDebit;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getOwnerId() {
        return this.ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public String getOwnerType() {
        return this.ownerType;
    }
    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
    public Long getStockId() {
        return this.stockId;
    }
    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }
}
