package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockDeviceTransferDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    public static enum COLUMNS {CREATEDATE, CREATEUSER, ID, NEWPRODOFFERID, NEWSTATEID, PRODOFFERID, STATEID, STOCKREQUESTID, EXCLUSE_ID_LIST}

    ;
    private Date createDate;
    private String createUser;
    private Long id;
    private Long newProdOfferId;
    private String newProbOfferName;
    private String newProbOfferCode;
    private Long newStateId;
    private Long prodOfferId;
    private Long stateId;
    private Long stockRequestId;

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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNewProdOfferId() {
        return this.newProdOfferId;
    }

    public void setNewProdOfferId(Long newProdOfferId) {
        this.newProdOfferId = newProdOfferId;
    }

    public Long getNewStateId() {
        return newStateId;
    }

    public void setNewStateId(Long newStateId) {
        this.newStateId = newStateId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }


    public Long getStockRequestId() {
        return this.stockRequestId;
    }

    public void setStockRequestId(Long stockRequestId) {
        this.stockRequestId = stockRequestId;
    }

    public String getNewProbOfferName() {
        return newProbOfferName;
    }

    public void setNewProbOfferName(String newProbOfferName) {
        this.newProbOfferName = newProbOfferName;
    }

    public String getNewProbOfferCode() {
        return newProbOfferCode;
    }

    public void setNewProbOfferCode(String newProbOfferCode) {
        this.newProbOfferCode = newProbOfferCode;
    }
}
