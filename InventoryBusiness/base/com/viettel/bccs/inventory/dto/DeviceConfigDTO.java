package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.List;

public class DeviceConfigDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    public static enum COLUMNS {CREATEDATE, CREATEUSER, ID, NEWPRODOFFERID, PRODOFFERID, STATEID, STATUS, UPDATEDATE, UPDATEUSER, EXCLUSE_ID_LIST}

    ;
    private Date createDate;
    private String createUser;
    private Long id;
    private Long newProdOfferId;
    private String newProdOfferName;
    private String newProdOfferCode;
    private Long prodOfferId;
    private Long stateId;
    private String status;
    private Date updateDate;
    private String updateUser;
    private String offerTypeName;
    private String productOfferCode;
    private String productOfferName;
    private String stateName;
    List<DeviceConfigDetailDTO> listAddDeviceConfigDetail;

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

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getOfferTypeName() {
        return offerTypeName;
    }

    public void setOfferTypeName(String offerTypeName) {
        this.offerTypeName = offerTypeName;
    }

    public String getProductOfferCode() {
        return productOfferCode;
    }

    public void setProductOfferCode(String productOfferCode) {
        this.productOfferCode = productOfferCode;
    }

    public String getProductOfferName() {
        return productOfferName;
    }

    public void setProductOfferName(String productOfferName) {
        this.productOfferName = productOfferName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public List<DeviceConfigDetailDTO> getListAddDeviceConfigDetail() {
        return listAddDeviceConfigDetail;
    }

    public void setListAddDeviceConfigDetail(List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) {
        this.listAddDeviceConfigDetail = listAddDeviceConfigDetail;
    }

    public String getNewProdOfferName() {
        return newProdOfferName;
    }

    public void setNewProdOfferName(String newProdOfferName) {
        this.newProdOfferName = newProdOfferName;
    }

    public String getNewProdOfferCode() {
        return newProdOfferCode;
    }

    public void setNewProdOfferCode(String newProdOfferCode) {
        this.newProdOfferCode = newProdOfferCode;
    }
}
