package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class ChangeModelTransDetailDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Long changeModelTransDetailId;
    private Long changeModelTransId;
    private Date createDate;
    private Long newProdOfferId;
    private String note;
    private Long oldProdOfferId;
    private Long quantity;
    private Long stateId;
    private String newProdOfferCode;
    private String newProdOfferName;
    private String oldProdOfferCode;
    private String oldProdOfferName;
    private String stateStr;

    public String getNewProdOfferName() {
        return newProdOfferName;
    }

    public void setNewProdOfferName(String newProdOfferName) {
        this.newProdOfferName = newProdOfferName;
    }

    public String getOldProdOfferName() {
        return oldProdOfferName;
    }

    public void setOldProdOfferName(String oldProdOfferName) {
        this.oldProdOfferName = oldProdOfferName;
    }

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public Long getChangeModelTransDetailId() {
        return this.changeModelTransDetailId;
    }

    public void setChangeModelTransDetailId(Long changeModelTransDetailId) {
        this.changeModelTransDetailId = changeModelTransDetailId;
    }

    public Long getChangeModelTransId() {
        return this.changeModelTransId;
    }

    public void setChangeModelTransId(Long changeModelTransId) {
        this.changeModelTransId = changeModelTransId;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getNewProdOfferId() {
        return this.newProdOfferId;
    }

    public void setNewProdOfferId(Long newProdOfferId) {
        this.newProdOfferId = newProdOfferId;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getOldProdOfferId() {
        return this.oldProdOfferId;
    }

    public void setOldProdOfferId(Long oldProdOfferId) {
        this.oldProdOfferId = oldProdOfferId;
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

    public String getNewProdOfferCode() {
        return newProdOfferCode;
    }

    public void setNewProdOfferCode(String newProdOfferCode) {
        this.newProdOfferCode = newProdOfferCode;
    }

    public String getOldProdOfferCode() {
        return oldProdOfferCode;
    }

    public void setOldProdOfferCode(String oldProdOfferCode) {
        this.oldProdOfferCode = oldProdOfferCode;
    }
}
