package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class ProductErpDTO extends BaseDTO implements Serializable {

    private Date createDate;
    private String createUser;
    private Long ownerId;
    private Long ownerType;
    private Long prodOfferId;
    private Long productErpId;
    private Long quantity;
    private Date updateDatetime;
    private String updateUser;

    public String getKeySet() {
        return keySet; }
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
    public Long getProductErpId() {
        return this.productErpId;
    }
    public void setProductErpId(Long productErpId) {
        this.productErpId = productErpId;
    }
    public Long getQuantity() {
        return this.quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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
}
