package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class ProductExchangeDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Date createDatetime;
    private String createUser;
    private String description;
    private Date endDatetime;
    private Long newProdOfferId;
    private Long prodOfferId;
    private Long prodOfferTypeId;
    private Long productExchangeId;
    private String prodExchangeName;
    private String prodExchangeNameNew;
    private String prodExchangeCode;
    private String prodExchangeCodeNew;
    private Date startDatetime;
    private String status;
    private Date updateDatetime;
    private String updateUser;

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDatetime() {
        return this.endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Long getNewProdOfferId() {
        return this.newProdOfferId;
    }

    public void setNewProdOfferId(Long newProdOfferId) {
        this.newProdOfferId = newProdOfferId;
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

    public Long getProductExchangeId() {
        return this.productExchangeId;
    }

    public void setProductExchangeId(Long productExchangeId) {
        this.productExchangeId = productExchangeId;
    }

    public Date getStartDatetime() {
        return this.startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getProdExchangeName() {
        return prodExchangeName;
    }

    public void setProdExchangeName(String prodExchangeName) {
        this.prodExchangeName = prodExchangeName;
    }

    public String getProdExchangeNameNew() {
        return prodExchangeNameNew;
    }

    public void setProdExchangeNameNew(String prodExchangeNameNew) {
        this.prodExchangeNameNew = prodExchangeNameNew;
    }

    public String getProdExchangeCode() {
        return prodExchangeCode;
    }

    public void setProdExchangeCode(String prodExchangeCode) {
        this.prodExchangeCode = prodExchangeCode;
    }

    public String getProdExchangeCodeNew() {
        return prodExchangeCodeNew;
    }

    public void setProdExchangeCodeNew(String prodExchangeCodeNew) {
        this.prodExchangeCodeNew = prodExchangeCodeNew;
    }
}
