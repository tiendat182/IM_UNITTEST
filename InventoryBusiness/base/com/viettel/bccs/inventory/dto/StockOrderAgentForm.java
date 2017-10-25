/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.dto;

import java.util.Date;

/**
 *
 * @author truongnq5
 */
public class StockOrderAgentForm {

    private Date fromDate;
    private Date toDate;
    private Date dateRequest;
    private String branchCode;
    private String branchName;
    private String shopCode;
    private String shopName;
    private String staffCode;
    private String staffName;
    private String strFromDate;
    private String strToDate;
    private String strStatus;
    private String requestCode;
    private Long status;
    private String stockModelCode;
    private String stockModelName;
    private Long shopId;
    private Long staffId;
    private Long stockTransId;
    private String strCreateDate;
    private String strLastModify;
    private String reasonName;
    private Long quantity;
    private Long stockModelId;
    private Long orderType;
    private String note;
    private String unit;
    private Long stockTypeId;
    private Long stateId;
    private String stockTypeName;
    private String noteGoods;
    private Date createDate;
    private Date lastModify;
    private String strOrderType;
    private Long stockOrderAgentId;
    private String stateName;
    private Long channelTypeId;
    private String bankCode;
    private String bankName;
    private Long maxMoneyDebit;
    private Long currentDebit;
    private String approveStaff;
    private Date approveDate;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public String getNoteGoods() {
        return noteGoods;
    }

    public void setNoteGoods(String noteGoods) {
        this.noteGoods = noteGoods;
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

    public Date getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(Date dateRequest) {
        this.dateRequest = dateRequest;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStrFromDate() {
        return strFromDate;
    }

    public void setStrFromDate(String strFromDate) {
        this.strFromDate = strFromDate;
    }

    public String getStrToDate() {
        return strToDate;
    }

    public void setStrToDate(String strToDate) {
        this.strToDate = strToDate;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getStockModelCode() {
        return stockModelCode;
    }

    public void setStockModelCode(String stockModelCode) {
        this.stockModelCode = stockModelCode;
    }

    public String getStockModelName() {
        return stockModelName;
    }

    public void setStockModelName(String stockModelName) {
        this.stockModelName = stockModelName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getStrCreateDate() {
        return strCreateDate;
    }

    public void setStrCreateDate(String strCreateDate) {
        this.strCreateDate = strCreateDate;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public Long getOrderType() {
        return orderType;
    }

    public void setOrderType(Long orderType) {
        this.orderType = orderType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getStockTypeId() {
        return stockTypeId;
    }

    public void setStockTypeId(Long stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStockTypeName() {
        return stockTypeName;
    }

    public void setStockTypeName(String stockTypeName) {
        this.stockTypeName = stockTypeName;
    }

    public String getStrOrderType() {
        return strOrderType;
    }

    public void setStrOrderType(String strOrderType) {
        this.strOrderType = strOrderType;
    }

    public Long getStockOrderAgentId() {
        return stockOrderAgentId;
    }

    public void setStockOrderAgentId(Long stockOrderAgentId) {
        this.stockOrderAgentId = stockOrderAgentId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStrLastModify() {
        return strLastModify;
    }

    public void setStrLastModify(String strLastModify) {
        this.strLastModify = strLastModify;
    }

    public Long getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    private Long saleTransId;
    private String saleTransCode;
    private Date saleTransDate;
    private String staffCreate;
    private String shopExp;
    private String shopImp;
    private String approveStatus;
    private Long totalAmount;
    private Long totalDiscount;

    public Long getSaleTransId() {
        return saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public String getSaleTransCode() {
        return saleTransCode;
    }

    public void setSaleTransCode(String saleTransCode) {
        this.saleTransCode = saleTransCode;
    }

    public Date getSaleTransDate() {
        return saleTransDate;
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }

    public String getStaffCreate() {
        return staffCreate;
    }

    public void setStaffCreate(String staffCreate) {
        this.staffCreate = staffCreate;
    }

    public String getShopExp() {
        return shopExp;
    }

    public void setShopExp(String shopExp) {
        this.shopExp = shopExp;
    }

    public String getShopImp() {
        return shopImp;
    }

    public void setShopImp(String shopImp) {
        this.shopImp = shopImp;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Long totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Long getMaxMoneyDebit() {
        return maxMoneyDebit;
    }

    public void setMaxMoneyDebit(Long maxMoneyDebit) {
        this.maxMoneyDebit = maxMoneyDebit;
    }

    public String getApproveStaff() {
        return approveStaff;
    }

    public void setApproveStaff(String approveStaff) {
        this.approveStaff = approveStaff;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Long getCurrentDebit() {
        return currentDebit;
    }

    public void setCurrentDebit(Long currentDebit) {
        this.currentDebit = currentDebit;
    }

}
