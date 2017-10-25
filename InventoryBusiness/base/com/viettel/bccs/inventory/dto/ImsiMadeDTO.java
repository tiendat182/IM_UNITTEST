package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;
import java.lang.Integer;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class ImsiMadeDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    public static enum COLUMNS {CONTENT, CREATEDATE, FROMIMSI, ID, OPKEY, PO, PRODOFFERID, QUANTITY, SERIALNO, SIMTYPE, TOIMSI, USERCREATE, EXCLUSE_ID_LIST}

    ;
    private byte[] content;
    private Date createDate;
    private String fromImsi;
    private Long id;
    private String opKey;
    private String po;
    private Long prodOfferId;
    private String prodOfferName;
    private Long quantity;
    private String serialNo;
    private String simType;
    private String toImsi;
    private String userCreate;

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFromImsi() {
        return this.fromImsi;
    }

    public void setFromImsi(String fromImsi) {
        this.fromImsi = fromImsi;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpKey() {
        return this.opKey;
    }

    public void setOpKey(String opKey) {
        this.opKey = opKey;
    }

    public String getPo() {
        return this.po;
    }

    public void setPo(String po) {
        this.po = po;
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

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSimType() {
        return this.simType;
    }

    public void setSimType(String simType) {
        this.simType = simType;
    }

    public String getToImsi() {
        return this.toImsi;
    }

    public void setToImsi(String toImsi) {
        this.toImsi = toImsi;
    }

    public String getUserCreate() {
        return this.userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }
}
