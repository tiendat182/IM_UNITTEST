package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockCheckReportDTO extends BaseDTO implements Serializable {

    private Date checkDate;
    private Date createDate;
    private String createUser;
    private byte[] fileContent;
    private String fileName;
    private String ownerCode;
    private Long ownerId;
    private String ownerName;
    private Short ownerType;
    private Long stockCheckReportId;
    private String checkDateStr;

    public String getKeySet() {
        return keySet;
    }

    public String getCheckDateStr() {
        return checkDateStr;
    }

    public void setCheckDateStr(String checkDateStr) {
        this.checkDateStr = checkDateStr;
    }

    public Date getCheckDate() {
        return this.checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
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

    public byte[] getFileContent() {
        return this.fileContent;
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

    public String getOwnerCode() {
        return this.ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Short getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(Short ownerType) {
        this.ownerType = ownerType;
    }

    public Long getStockCheckReportId() {
        return this.stockCheckReportId;
    }

    public void setStockCheckReportId(Long stockCheckReportId) {
        this.stockCheckReportId = stockCheckReportId;
    }
}
