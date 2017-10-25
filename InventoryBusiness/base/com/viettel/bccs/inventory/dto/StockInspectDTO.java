package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockInspectDTO extends BaseDTO implements Serializable {

    private Date approveDate;
    private String approveNote;
    private Long approveStatus;
    private String approveUser;
    private Long approveUserId;
    private String approveUserName;
    private Date createDate;
    private String createUser;
    private Long createUserId;
    private String createUserName;
    private Long isFinished;
    private Long isFinishedAll;
    private Long isScan;
    private Long ownerId;
    private Long ownerType;
    private Long prodOfferId;
    private Long prodOfferTypeId;
    private Long quantityReal;
    private Long quantitySys;
    private Long quantityPoor;
    private Long quantityFinance;
    private Long stateId;
    private Long stockInspectId;
    private Date updateDate;
    private Long shopId;
    private String shopName;
    private String shopCode;
    private String note;
    private List<StockInspectRealDTO> stockInspectRealDTOs = Lists.newArrayList();

    public String getKeySet() {
        return keySet;
    }

    public Date getApproveDate() {
        return this.approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getApproveNote() {
        return this.approveNote;
    }

    public void setApproveNote(String approveNote) {
        this.approveNote = approveNote;
    }

    public Long getApproveStatus() {
        return this.approveStatus;
    }

    public void setApproveStatus(Long approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getApproveUser() {
        return this.approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public Long getApproveUserId() {
        return this.approveUserId;
    }

    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }

    public String getApproveUserName() {
        return this.approveUserName;
    }

    public void setApproveUserName(String approveUserName) {
        this.approveUserName = approveUserName;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return this.createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getIsFinished() {
        return this.isFinished;
    }

    public void setIsFinished(Long isFinished) {
        this.isFinished = isFinished;
    }

    public Long getIsFinishedAll() {
        return this.isFinishedAll;
    }

    public void setIsFinishedAll(Long isFinishedAll) {
        this.isFinishedAll = isFinishedAll;
    }

    public Long getIsScan() {
        return this.isScan;
    }

    public void setIsScan(Long isScan) {
        this.isScan = isScan;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getProdOfferTypeId() {
        return this.prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public Long getQuantityReal() {
        return this.quantityReal;
    }

    public void setQuantityReal(Long quantityReal) {
        this.quantityReal = quantityReal;
    }

    public Long getQuantitySys() {
        return this.quantitySys;
    }

    public void setQuantitySys(Long quantitySys) {
        this.quantitySys = quantitySys;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getStockInspectId() {
        return this.stockInspectId;
    }

    public void setStockInspectId(Long stockInspectId) {
        this.stockInspectId = stockInspectId;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<StockInspectRealDTO> getStockInspectRealDTOs() {
        return stockInspectRealDTOs;
    }

    public void setStockInspectRealDTOs(List<StockInspectRealDTO> stockInspectRealDTOs) {
        this.stockInspectRealDTOs = stockInspectRealDTOs;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Long getQuantityPoor() {
        return quantityPoor;
    }

    public void setQuantityPoor(Long quantityPoor) {
        this.quantityPoor = quantityPoor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getQuantityFinance() {
        return quantityFinance;
    }

    public void setQuantityFinance(Long quantityFinance) {
        this.quantityFinance = quantityFinance;
    }
}
