package com.viettel.bccs.inventory.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by hoangnt14 on 12/26/2016.
 */
public class VStockNumberDTO implements Serializable {
    @JsonProperty("id")
    private String id;
    @JsonProperty("isdn")
    private String isdn;
    @JsonProperty("owner_code")
    private String ownerCode;
    @JsonProperty("owner_name")
    private String ownerName;
    @JsonProperty("stock_model_name")
    private String stockModelName;
    @JsonProperty("stock_model_id")
    private String stockModelId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("shop_path")
    private String shopPath;
    @JsonProperty("channel_type_id")
    private String channelTypeId;

    public VStockNumberDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStockModelName() {
        return stockModelName;
    }

    public void setStockModelName(String stockModelName) {
        this.stockModelName = stockModelName;
    }

    public String getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(String stockModelId) {
        this.stockModelId = stockModelId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopPath() {
        return shopPath;
    }

    public void setShopPath(String shopPath) {
        this.shopPath = shopPath;
    }

    public String getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(String channelTypeId) {
        this.channelTypeId = channelTypeId;
    }
}
