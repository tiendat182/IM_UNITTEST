package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockTransDetailOfflineDTO extends BaseDTO implements Serializable {

    private Long amount;
    private Date createDatetime;
    private Long price;
    private Long prodOfferId;
    private Long quantity;
    private Long stateId;
    private Long stockTransDetailId;
    private Long stockTransId;
    private Long prodOfferTypeId;
    private String prodOfferTypeName;
    private String tableName;
    private String prodOfferName;
    private String unit;
    private String stateName;
    private Long total;
    private Long checkSerial;
    private List<StockTransSerialDTO> lstStockTransSerial;

    public String getKeySet() {
        return keySet;
    }

    public StockTransDetailOfflineDTO() {
    }

    public StockTransDetailOfflineDTO(StockTransDetailDTO otherStockTransDetail) {
        this.stockTransDetailId = otherStockTransDetail.getStockTransDetailId();
        this.stateId = otherStockTransDetail.getStateId();
        this.quantity = otherStockTransDetail.getQuantity();
        this.price = otherStockTransDetail.getPrice();
        this.amount = otherStockTransDetail.getAmount();
        this.createDatetime = otherStockTransDetail.getCreateDatetime();
        this.stockTransId = otherStockTransDetail.getStockTransId();
        this.prodOfferId = otherStockTransDetail.getProdOfferId();
    }

    public StockTransDetailOfflineDTO(Long prodOfferId, Long stateId) {
        this.prodOfferId = prodOfferId;
        this.stateId = stateId;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getPrice() {
        return this.price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getStockTransDetailId() {
        return this.stockTransDetailId;
    }

    public void setStockTransDetailId(Long stockTransDetailId) {
        this.stockTransDetailId = stockTransDetailId;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getProdOfferTypeId() {
        return prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public String getProdOfferTypeName() {
        return prodOfferTypeName;
    }

    public void setProdOfferTypeName(String prodOfferTypeName) {
        this.prodOfferTypeName = prodOfferTypeName;
    }

    public List<StockTransSerialDTO> getLstStockTransSerial() {
        return lstStockTransSerial;
    }

    public void setLstStockTransSerial(List<StockTransSerialDTO> lstStockTransSerial) {
        this.lstStockTransSerial = lstStockTransSerial;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(Long checkSerial) {
        this.checkSerial = checkSerial;
    }
}
