package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockTransSerialOfflineDTO extends BaseDTO implements Serializable {

    private String fromOwnerName;
    private String toOwnerName;
    private String modelName;
    private String modelStateName;
    private String actionCode;
    private Date createDatetime;
    private String fromSerial;
    private Long quantity;
    private Long stockTransDetailId;
    private Long stockTransSerialId;
    private String toSerial;
    private Long index;
    private Long stateId;
    private Long prodOfferId;
    private Long stockTransId;

    public StockTransSerialOfflineDTO() {
    }

    public StockTransSerialOfflineDTO(String fromSerial, String toSerial, Long quantity) {
        this.fromSerial = fromSerial;
        this.toSerial = toSerial;
        this.quantity = quantity;
    }

    public StockTransSerialOfflineDTO(StockTransSerialDTO stockTransSerialDTO) {
        this.fromSerial = stockTransSerialDTO.getFromSerial();
        this.toSerial = stockTransSerialDTO.getToSerial();
        this.quantity = stockTransSerialDTO.getQuantity();
        this.createDatetime = stockTransSerialDTO.getCreateDatetime();
        this.stockTransDetailId = stockTransSerialDTO.getStockTransDetailId();
        this.stateId = stockTransSerialDTO.getStateId();
    }

    public String getFromOwnerName() {
        return fromOwnerName;
    }

    public void setFromOwnerName(String fromOwnerName) {
        this.fromOwnerName = fromOwnerName;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getActionCode() {
        return this.actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getModelStateName() {
        return modelStateName;
    }

    public void setModelStateName(String modelStateName) {
        this.modelStateName = modelStateName;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getFromSerial() {
        return this.fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStockTransDetailId() {
        return this.stockTransDetailId;
    }

    public void setStockTransDetailId(Long stockTransDetailId) {
        this.stockTransDetailId = stockTransDetailId;
    }

    public Long getStockTransSerialId() {
        return this.stockTransSerialId;
    }

    public void setStockTransSerialId(Long stockTransSerialId) {
        this.stockTransSerialId = stockTransSerialId;
    }

    public String getToSerial() {
        return this.toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
}
