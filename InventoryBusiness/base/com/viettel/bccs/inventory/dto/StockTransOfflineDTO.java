package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockTransOfflineDTO extends BaseDTO implements Serializable {
    private String checkErp;
    private Date createDatetime;
    private String depositStatus;
    private Long fromOwnerId;
    private Long fromOwnerType;
    private Long isAutoGen;
    private String note;
    private String payStatus;
    private Long reasonId;
    private Long regionStockTransId;
    private Long requestId;
    private String status;
    private String stockBase;
    private Long stockTransId;
    private Long toOwnerId;
    private Long toOwnerType;
    private Long totalAmount;
    private Date fromCreateDate;
    private Date toCreateDate;
    private String fromOwnerName;
    private String toOwnerName;
    private String actionCode;
    private Long stockTransActionId;
    private String stockTransStatus;
    private String statusName;
    private String userCreate;
    private Long shopId;
    private Long staffId;
    private String actionType;
    private String reasonName;
    private String logicstic;
    private String userName;
    private String passWord;
    private Long signFlowId;
    private String transport;
    private String logistic;
    private boolean signVoffice;
    private String processOffline;
    private Date processStartDate;
    private Date processEndDate;
    private Long vofficeStatus;
    private Long regionStockId;
    private String voffficeStatusName;
    private String fromOwnerAddress;
    private String toOwnerAddress;
    private String stockTransType;
    private Long importReasonId;
    private String importNote;
    private String stockAgent;
    private String fromStock;
    private Long exchangeStockId;
    private Long rejectReasonId;
    private String rejectNote;
    private Long realTransUserId;


    public StockTransOfflineDTO() {
    }

    public StockTransOfflineDTO(StockTransDTO otherStockTrans) {
        this.status = otherStockTrans.getStatus();
        this.totalAmount = otherStockTrans.getTotalAmount();
        this.checkErp = otherStockTrans.getCheckErp();
        this.isAutoGen = otherStockTrans.getIsAutoGen();
        this.transport = otherStockTrans.getTransport();
        this.importReasonId = otherStockTrans.getImportReasonId();
        this.importNote = otherStockTrans.getImportNote();
        this.stockTransId = otherStockTrans.getStockTransId();
        this.fromOwnerId = otherStockTrans.getFromOwnerId();
        this.fromOwnerType = otherStockTrans.getFromOwnerType();
        this.toOwnerId = otherStockTrans.getToOwnerId();
        this.toOwnerType = otherStockTrans.getToOwnerType();
        this.createDatetime = otherStockTrans.getCreateDatetime();
        this.stockTransType = otherStockTrans.getStockTransType();
        this.reasonId = otherStockTrans.getReasonId();
        this.stockTransStatus = otherStockTrans.getStockTransStatus();
        this.payStatus = otherStockTrans.getPayStatus();
        this.depositStatus = otherStockTrans.getDepositStatus();
        this.note = otherStockTrans.getNote();
        this.realTransUserId = otherStockTrans.getRealTransUserId();
        this.stockBase = otherStockTrans.getStockBase();
        this.rejectReasonId = otherStockTrans.getRejectReasonId();
        this.rejectNote = otherStockTrans.getRejectNote();
    }


    public String getToOwnerAddress() {
        return toOwnerAddress;
    }

    public void setToOwnerAddress(String toOwnerAddress) {
        this.toOwnerAddress = toOwnerAddress;
    }

    public String getFromOwnerAddress() {
        return fromOwnerAddress;
    }

    public void setFromOwnerAddress(String fromOwnerAddress) {
        this.fromOwnerAddress = fromOwnerAddress;
    }

    public Long getVofficeStatus() {
        return vofficeStatus;
    }

    public void setVofficeStatus(Long vofficeStatus) {
        this.vofficeStatus = vofficeStatus;
    }

    public String getProcessOffline() {
        return processOffline;
    }

    public void setProcessOffline(String processOffline) {
        this.processOffline = processOffline;
    }

    public Date getProcessStartDate() {
        return processStartDate;
    }

    public void setProcessStartDate(Date processStartDate) {
        this.processStartDate = processStartDate;
    }

    public Date getProcessEndDate() {
        return processEndDate;
    }

    public void setProcessEndDate(Date processEndDate) {
        this.processEndDate = processEndDate;
    }

    public String getKeySet() {
        return keySet;
    }

    public String getLogicstic() {
        return logicstic;
    }

    public void setLogicstic(String logicstic) {
        this.logicstic = logicstic;
    }

    public String getActionType() {
        return actionType;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStockTransStatus() {
        return stockTransStatus;
    }

    public void setStockTransStatus(String stockTransStatus) {
        this.stockTransStatus = stockTransStatus;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Date getFromCreateDate() {
        return fromCreateDate;
    }

    public void setFromCreateDate(Date fromCreateDate) {
        this.fromCreateDate = fromCreateDate;
    }

    public Date getToCreateDate() {
        return toCreateDate;
    }

    public void setToCreateDate(Date toCreateDate) {
        this.toCreateDate = toCreateDate;
    }

    public String getFromOwnerName() {
        return fromOwnerName;
    }

    public void setFromOwnerName(String fromOwnerName) {
        this.fromOwnerName = fromOwnerName;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getCheckErp() {
        return this.checkErp;
    }

    public void setCheckErp(String checkErp) {
        this.checkErp = checkErp;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getDepositStatus() {
        return this.depositStatus;
    }

    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }

    public Long getFromOwnerId() {
        return this.fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getFromOwnerType() {
        return this.fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getIsAutoGen() {
        return this.isAutoGen;
    }

    public void setIsAutoGen(Long isAutoGen) {
        this.isAutoGen = isAutoGen;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPayStatus() {
        return this.payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Long getReasonId() {
        return this.reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Long getRegionStockTransId() {
        return this.regionStockTransId;
    }

    public void setRegionStockTransId(Long regionStockTransId) {
        this.regionStockTransId = regionStockTransId;
    }

    public Long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStockBase() {
        return this.stockBase;
    }

    public void setStockBase(String stockBase) {
        this.stockBase = stockBase;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getToOwnerId() {
        return this.toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Long getToOwnerType() {
        return this.toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Long getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Long getSignFlowId() {
        return signFlowId;
    }

    public void setSignFlowId(Long signFlowId) {
        this.signFlowId = signFlowId;
    }

    public String getLogistic() {
        return logistic;
    }

    public void setLogistic(String logistic) {
        this.logistic = logistic;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public boolean isSignVoffice() {
        return signVoffice;
    }

    public void setSignVoffice(boolean signVoffice) {
        this.signVoffice = signVoffice;
    }

    public Long getRegionStockId() {
        return regionStockId;
    }

    public void setRegionStockId(Long regionStockId) {
        this.regionStockId = regionStockId;
    }

    public String getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(String stockTransType) {
        this.stockTransType = stockTransType;
    }

    public Long getImportReasonId() {
        return importReasonId;
    }

    public void setImportReasonId(Long importReasonId) {
        this.importReasonId = importReasonId;
    }

    public String getImportNote() {
        return importNote;
    }

    public void setImportNote(String importNote) {
        this.importNote = importNote;
    }

    public String getStockAgent() {
        return stockAgent;
    }

    public void setStockAgent(String stockAgent) {
        this.stockAgent = stockAgent;
    }

    public String getFromStock() {
        return fromStock;
    }

    public void setFromStock(String fromStock) {
        this.fromStock = fromStock;
    }

    public Long getExchangeStockId() {
        return exchangeStockId;
    }

    public void setExchangeStockId(Long exchangeStockId) {
        this.exchangeStockId = exchangeStockId;
    }

    public Long getRejectReasonId() {
        return rejectReasonId;
    }

    public void setRejectReasonId(Long rejectReasonId) {
        this.rejectReasonId = rejectReasonId;
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public Long getRealTransUserId() {
        return realTransUserId;
    }

    public void setRealTransUserId(Long realTransUserId) {
        this.realTransUserId = realTransUserId;
    }
}
