package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockTransDTO extends BaseDTO implements Serializable {
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
    private String fromOwnerCode;
    private String fromOwnerName;
    private String toOwnerCode;
    private String toOwnerName;
    private String actionCode;
    private String actionCodeNote;
    private Long stockTransActionId;
    private String stockTransStatus;
    private String statusName;
    private String userCreate;
    private Long shopId;
    private Long staffId;
    private String actionType;
    private String reasonName;
    private String reasonCode;
    private String logicstic;
    private String userName;
    private String passWord;
    private Long signFlowId;
    private String transport;
    private boolean signVoffice;
    private String processOffline;
    private Date processStartDate;
    private Date processEndDate;
    private Long vofficeStatus;
    private Long regionStockId;
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
    private String createUserIpAdress;
    private Long realTransUserId;
    // Doi tuong kenh
    private Long channelTypeId;
    private Long payMethod;
    private Long programSaleId;
    private Long bankplusStatus;
    private Long accountBalanceId;
    private Long depositPrice;
    private Long depositTotal;
    private Long stockOrderAgentId;
    private Date stockTransDate;
    private Long fromStockTransId;
    private String account;
    private String accType;
    private String troubleType;
    private Long sourceType;
    private String custCode;
    private String stockCode;
    private String orderType;
    private Long inputType;
    private Long outputType;
    private String location;
    private String areaCode;
    private String typeReceive;
    private Date ieExpectDate;
    private String staffCode;
    private String staffName;
    private String shopCode;
    private String shopName;
    private Date fromDate;
    private Date toDate;
    private String request;
    private Long exchangeStockTransActionId;

    //outsource sonat
    private String transportSource;

    public Long getExchangeStockTransActionId() {
        return exchangeStockTransActionId;
    }

    public void setExchangeStockTransActionId(Long exchangeStockTransActionId) {
        this.exchangeStockTransActionId = exchangeStockTransActionId;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Long getInputType() {
        return inputType;
    }

    public void setInputType(Long inputType) {
        this.inputType = inputType;
    }

    public Long getOutputType() {
        return outputType;
    }

    public void setOutputType(Long outputType) {
        this.outputType = outputType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getTypeReceive() {
        return typeReceive;
    }

    public void setTypeReceive(String typeReceive) {
        this.typeReceive = typeReceive;
    }

    public Date getIeExpectDate() {
        return ieExpectDate;
    }

    public void setIeExpectDate(Date ieExpectDate) {
        this.ieExpectDate = ieExpectDate;
    }

    public Long getDepositTotal() {
        return depositTotal;
    }

    public void setDepositTotal(Long depositTotal) {
        this.depositTotal = depositTotal;
    }

    private List<StockTransDetailDTO> lsStockTransDetailDTOList;

    private String devliver;

    public String getDevliver() {
        return devliver;
    }

    public void setDevliver(String devliver) {
        this.devliver = devliver;
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

    public String getCreateUserIpAdress() {
        return createUserIpAdress;
    }

    public void setCreateUserIpAdress(String createUserIpAdress) {
        this.createUserIpAdress = createUserIpAdress;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public Long getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public Long getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Long payMethod) {
        this.payMethod = payMethod;
    }

    public Long getProgramSaleId() {
        return programSaleId;
    }

    public void setProgramSaleId(Long programSaleId) {
        this.programSaleId = programSaleId;
    }

    public String getToOwnerCode() {
        return toOwnerCode;
    }

    public void setToOwnerCode(String toOwnerCode) {
        this.toOwnerCode = toOwnerCode;
    }

    public String getFromOwnerCode() {
        return fromOwnerCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public void setFromOwnerCode(String fromOwnerCode) {
        this.fromOwnerCode = fromOwnerCode;
    }

    public Long getBankplusStatus() {
        return bankplusStatus;
    }

    public void setBankplusStatus(Long bankplusStatus) {
        this.bankplusStatus = bankplusStatus;
    }

    public Long getAccountBalanceId() {
        return accountBalanceId;
    }

    public void setAccountBalanceId(Long accountBalanceId) {
        this.accountBalanceId = accountBalanceId;
    }

    public Long getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(Long depositPrice) {
        this.depositPrice = depositPrice;
    }

    public List<StockTransDetailDTO> getLsStockTransDetailDTOList() {
        return lsStockTransDetailDTOList;
    }

    public void setLsStockTransDetailDTOList(List<StockTransDetailDTO> lsStockTransDetailDTOList) {
        this.lsStockTransDetailDTOList = lsStockTransDetailDTOList;
    }

    public Long getRealTransUserId() {
        return realTransUserId;
    }

    public void setRealTransUserId(Long realTransUserId) {
        this.realTransUserId = realTransUserId;
    }

    public Long getStockOrderAgentId() {
        return stockOrderAgentId;
    }

    public void setStockOrderAgentId(Long stockOrderAgentId) {
        this.stockOrderAgentId = stockOrderAgentId;
    }

    public Date getStockTransDate() {
        return stockTransDate;
    }

    public void setStockTransDate(Date stockTransDate) {
        this.stockTransDate = stockTransDate;
    }

    public Long getFromStockTransId() {
        return fromStockTransId;
    }

    public void setFromStockTransId(Long fromStockTransId) {
        this.fromStockTransId = fromStockTransId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getTroubleType() {
        return troubleType;
    }

    public void setTroubleType(String troubleType) {
        this.troubleType = troubleType;
    }

    public String getActionCodeNote() {
        return actionCodeNote;
    }

    public void setActionCodeNote(String actionCodeNote) {
        this.actionCodeNote = actionCodeNote;
    }

    public Long getSourceType() {
        return sourceType;
    }

    public void setSourceType(Long sourceType) {
        this.sourceType = sourceType;
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

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getTransportSource() {
        return transportSource;
    }

    public void setTransportSource(String transportSource) {
        this.transportSource = transportSource;
    }
}
