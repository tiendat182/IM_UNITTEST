package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockTransFullOfflineDTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private String actionCode;
    private Long actionStaffId;
    private String actionStatus;
    private Long basisPrice;
    private Long checkDeposit;
    private Long checkSerial;
    private Date createDatetime;
    private String createUser;
    private String depositStatus;
    private Long fromOwnerId;
    private Long fromOwnerType;
    private String note;
    private String payStatus;
    private String prodOfferCode;
    private Long prodOfferId;
    private String prodOfferName;
    private Long productOfferTypeId;
    private String productOfferTypeName;
    private Long quantity;
    private Long reasonId;
    private Long stateId;
    private String stateName;
    private String status;
    private Long stockTransActionId;
    private Long stockTransDetailId;
    private Long stockTransId;
    private String stockTransType;
    private Long toOwnerId;
    private Long toOwnerType;
    private String unit;
    public String getActionCode() {
        return this.actionCode;
    }
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    public Long getActionStaffId() {
        return this.actionStaffId;
    }
    public void setActionStaffId(Long actionStaffId) {
        this.actionStaffId = actionStaffId;
    }
    public String getActionStatus() {
        return this.actionStatus;
    }
    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }
    public Long getBasisPrice() {
        return this.basisPrice;
    }
    public void setBasisPrice(Long basisPrice) {
        this.basisPrice = basisPrice;
    }
    public Long getCheckDeposit() {
        return this.checkDeposit;
    }
    public void setCheckDeposit(Long checkDeposit) {
        this.checkDeposit = checkDeposit;
    }
    public Long getCheckSerial() {
        return this.checkSerial;
    }
    public void setCheckSerial(Long checkSerial) {
        this.checkSerial = checkSerial;
    }
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    public String getCreateUser() {
        return this.createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public String getDepositStatus() {
        return this.depositStatus;
    }
    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }
    public Long getFromOwnerId() {
        return this.fromOwnerId;
    }
    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }
    public Long getFromOwnerType() {
        return this.fromOwnerType;
    }
    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getPayStatus() {
        return this.payStatus;
    }
    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
    public String getProdOfferCode() {
        return this.prodOfferCode;
    }
    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }
    public Long getProdOfferId() {
        return this.prodOfferId;
    }
    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }
    public String getProdOfferName() {
        return this.prodOfferName;
    }
    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }
    public Long getProductOfferTypeId() {
        return this.productOfferTypeId;
    }
    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }
    public String getProductOfferTypeName() {
        return this.productOfferTypeName;
    }
    public void setProductOfferTypeName(String productOfferTypeName) {
        this.productOfferTypeName = productOfferTypeName;
    }
    public Long getQuantity() {
        return this.quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public Long getReasonId() {
        return this.reasonId;
    }
    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }
    public Long getStateId() {
        return this.stateId;
    }
    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
    public String getStateName() {
        return this.stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Long getStockTransActionId() {
        return this.stockTransActionId;
    }
    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
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
    public String getStockTransType() {
        return this.stockTransType;
    }
    public void setStockTransType(String stockTransType) {
        this.stockTransType = stockTransType;
    }
    public Long getToOwnerId() {
        return this.toOwnerId;
    }
    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }
    public Long getToOwnerType() {
        return this.toOwnerType;
    }
    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
