package com.viettel.bccs.inventory.dto;

import java.util.Date;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class ImsiInfoDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    public static enum COLUMNS {CREATEDATE, DOCNO, ENDDATE, ID, IMSI, LASTUPDATETIME, LASTUPDATEUSER, STARTDATE, STATUS, USERCREATE, EXCLUSE_ID_LIST}

    ;
    private Date createDate;
    private String docNo;
    private Date endDate;
    private long id;
    private String imsi;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private Date startDate;
    private String status;
    private String userCreate;

    private String fromImsi;
    private String toImsi;
    private Long quantity;


    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDocNo() {
        return this.docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImsi() {
        return this.imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateUser() {
        return this.lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserCreate() {
        return this.userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getFromImsi() {
        return fromImsi;
    }

    public void setFromImsi(String fromImsi) {
        this.fromImsi = fromImsi;
    }

    public String getToImsi() {
        return toImsi;
    }

    public void setToImsi(String toImsi) {
        this.toImsi = toImsi;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
