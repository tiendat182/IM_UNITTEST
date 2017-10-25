package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockBalanceRequestDTO extends BaseDTO implements Serializable {

    private Date createDatetime;
    private String createUser;
    private String listEmailSign;
    private Long ownerId;
    private Long ownerType;
    private Long status;
    private Long stockRequestId;
    private Date updateDatetime;
    private String updateUser;
    private Long type;
    private Long stockTransActionId;
    private Date fromDate;
    private Date toDate;
    private Long shopId;
    private Long staffId;
    private Long signFlowId;
    private String accountName;
    private String accountPass;
    private String requestCode;
    private Long ownerIdLogin;
    private Long createStaffId;
    private List<StockBalanceDetailDTO> lstStockBalanceDetailDTO;

    public List<StockBalanceDetailDTO> getLstStockBalanceDetailDTO() {
        return lstStockBalanceDetailDTO;
    }

    public void setLstStockBalanceDetailDTO(List<StockBalanceDetailDTO> lstStockBalanceDetailDTO) {
        this.lstStockBalanceDetailDTO = lstStockBalanceDetailDTO;
    }

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public StockBalanceRequestDTO() {
    }

    public StockBalanceRequestDTO(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public String getKeySet() {
        return keySet;
    }

    public Long getOwnerIdLogin() {
        return ownerIdLogin;
    }

    public void setOwnerIdLogin(Long ownerIdLogin) {
        this.ownerIdLogin = ownerIdLogin;
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

    public String getListEmailSign() {
        return this.listEmailSign;
    }

    public void setListEmailSign(String listEmailSign) {
        this.listEmailSign = listEmailSign;
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

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStockRequestId() {
        return this.stockRequestId;
    }

    public void setStockRequestId(Long stockRequestId) {
        this.stockRequestId = stockRequestId;
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

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
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

    public Long getSignFlowId() {
        return signFlowId;
    }

    public void setSignFlowId(Long signFlowId) {
        this.signFlowId = signFlowId;
    }

    public Long getShopId() {
        return shopId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }
}
