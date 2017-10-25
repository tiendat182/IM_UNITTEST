package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockTransSerialDTO extends BaseDTO implements Serializable {

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
    private String errorMessage;
    private Long stateId;
    private Long prodOfferId;
    private Long stockTransId;
    private Long ownerId;
    private Long ownerType;
    private Long status;
    private String serialGpon;
    private String tvmsCadId;
    private String tvmsMacId;
    private Long notRule;

    public StockTransSerialDTO() {
    }

    public StockTransSerialDTO(String fromSerial, String toSerial, Long quantity) {
        this.fromSerial = fromSerial;
        this.toSerial = toSerial;
        this.quantity = quantity;
    }

    public String getStrCreateDatetime() {
        return createDatetime == null ? "" : DateUtil.date2ddMMyyyyString(createDatetime);
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void putError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getSerialGpon() {
        return serialGpon;
    }

    public void setSerialGpon(String serialGpon) {
        this.serialGpon = serialGpon;
    }

    public String getTvmsCadId() {
        return tvmsCadId;
    }

    public void setTvmsCadId(String tvmsCadId) {
        this.tvmsCadId = tvmsCadId;
    }

    public String getTvmsMacId() {
        return tvmsMacId;
    }

    public void setTvmsMacId(String tvmsMacId) {
        this.tvmsMacId = tvmsMacId;
    }

    public Long getNotRule() {
        return notRule;
    }

    public void setNotRule(Long notRule) {
        this.notRule = notRule;
    }
}
