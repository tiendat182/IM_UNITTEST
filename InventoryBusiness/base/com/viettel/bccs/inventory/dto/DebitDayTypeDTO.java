package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;

public class DebitDayTypeDTO extends BaseDTO {
    private Date createDate;
    private String createUser;
    private String debitDayType;
    private String debitDayCode;
    private String debitDayName;
    private Date endDate;
    private String fileName;
    private byte[] fileContent;
    private String detailFileUpload;
    private Long id;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private Date startDate;
    private String status;
    private String debitLevel;
    public boolean isDisableDowload() {
        return fileContent == null || fileContent.length == 0;
    }

    public String getDebitLevel() {
        return debitLevel;
    }

    public void setDebitLevel(String debitLevel) {
        this.debitLevel = debitLevel;
    }

    public String getKeySet() {
        return keySet;
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

    public String getDebitDayType() {
        return this.debitDayType;
    }

    public void setDebitDayType(String debitDayType) {
        this.debitDayType = debitDayType;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDebitDayCode() {
        return debitDayCode;
    }

    public void setDebitDayCode(String debitDayCode) {
        this.debitDayCode = debitDayCode;
    }

    public String getDebitDayName() {
        return debitDayName;
    }

    public void setDebitDayName(String debitDayName) {
        this.debitDayName = debitDayName;
    }

    public String getDetailFileUpload() {
        return detailFileUpload;
    }

    public void setDetailFileUpload(String detailFileUpload) {
        this.detailFileUpload = detailFileUpload;
    }
}
