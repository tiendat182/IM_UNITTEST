package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import org.codehaus.jackson.annotate.JsonProperty;

public class StockCycleDTO extends BaseDTO {

    @JsonProperty("table_pk")
    private String tablePk;
    @JsonProperty("prod_offer_id")
    private String prodOfferId;
    @JsonProperty("owner_id")
    private String ownerId;
    @JsonProperty("owner_type")
    private String ownerType;
    @JsonProperty("state_id")
    private String stateId;
    @JsonProperty("serial")
    private String serial;
    @JsonProperty("create_date")
    private String createDate;
    @JsonProperty("stock_trans_id")
    private String stockTransId;

    public String getTablePk() {
        return tablePk;
    }

    public void setTablePk(String tablePk) {
        this.tablePk = tablePk;
    }

    public String getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(String prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(String stockTransId) {
        this.stockTransId = stockTransId;
    }

}
