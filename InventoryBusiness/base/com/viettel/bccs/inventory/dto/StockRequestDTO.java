package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockRequestDTO extends BaseDTO implements Serializable {
    private Long stockRequestId;
    private Date createDatetime;
    private String createUser;
    private String note;
    private Long ownerId;
    private String ownerCode;
    private String ownerName;
    private Long ownerType;
    private String requestCode;
    private Long requestType;
    private String status;
    private String statusName;
    private Long stockTransId;
    private Date updateDatetime;
    private String updateUser;
    private Date fromDate;
    private Date toDate;
    private Long actionStaffId;
    private String accountName;
    private String accountPass;
    private String signUserList;
    private SignOfficeDTO signOfficeDTO;
    private byte[] fileContent;
    private List<StockTransDetailDTO> stockTransDetailDTOs;
    private String signVoffice;

    public StockRequestDTO() {
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPass() {
        return accountPass;
    }

    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }

    public String getSignUserList() {
        return signUserList;
    }

    public void setSignUserList(String signUserList) {
        this.signUserList = signUserList;
    }

    public String getKeySet() {
        return this.keySet;
    }

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

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getRequestCode() {
        return this.requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Long getRequestType() {
        return this.requestType;
    }

    public void setRequestType(Long requestType) {
        this.requestType = requestType;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStockRequestId() {
        return this.stockRequestId;
    }

    public void setStockRequestId(Long stockRequestId) {
        this.stockRequestId = stockRequestId;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
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

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public SignOfficeDTO getSignOfficeDTO() {
        return signOfficeDTO;
    }

    public void setSignOfficeDTO(SignOfficeDTO signOfficeDTO) {
        this.signOfficeDTO = signOfficeDTO;
    }

    public List<StockTransDetailDTO> getStockTransDetailDTOs() {
        return stockTransDetailDTOs;
    }

    public void setStockTransDetailDTOs(List<StockTransDetailDTO> stockTransDetailDTOs) {
        this.stockTransDetailDTOs = stockTransDetailDTOs;
    }

    public Long getActionStaffId() {
        return actionStaffId;
    }

    public void setActionStaffId(Long actionStaffId) {
        this.actionStaffId = actionStaffId;
    }

    public String getSignVoffice() {
        return signVoffice;
    }

    public void setSignVoffice(String signVoffice) {
        this.signVoffice = signVoffice;
    }
}
