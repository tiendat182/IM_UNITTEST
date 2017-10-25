package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockTransInputDTO extends BaseDTO implements Serializable {

    private String actionCode;
    private String createUser;
    private Long actionStaffId;
    private Date createDatetime;
    private Long fromOwnerId;
    private Long fromOwnerType;
    private Long toOwnerId;
    private Long toOwnerType;
    private Long reasonId;
    private String reasonCode;
    private String reasonName;
    private String note;
    private String stockBase;
    private Boolean checkErp = false;
    private Long regionStockId;
    private String regionStockName;
    private Long stockTransActionId;
    private Long stockTransId;
    private String codeName;
    //thaont19
    private String createDatetimeStr;
    private Long exchangeStockId;
    private Long regionStockTransId;
    // Doi tuong kenh
    private Long channelTypeId;
    private Long payMethod;
    private Long programSaleId;
    private Long totalAmount;
    private String payStatus;
    private String depositStatus;
    //sinhhv them yeu cau dai ly
    private String agentRequestCode;
    private Long agentOrderType;
    private String agentOrderTypeName;
    private Long stockOrderAgentId;
    private Long isAutoGen;
    //outsource sonat
    private String transportSource;

    public String getKeySet() {
        return keySet;
    }

    public StockTransInputDTO() {
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Long getActionStaffId() {
        return actionStaffId;
    }

    public void setActionStaffId(Long actionStaffId) {
        this.actionStaffId = actionStaffId;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Long getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStockBase() {
        return stockBase;
    }

    public void setStockBase(String stockBase) {
        this.stockBase = stockBase;
    }

    public Boolean getCheckErp() {
        return checkErp;
    }

    public void setCheckErp(Boolean checkErp) {
        this.checkErp = checkErp;
    }

    public Long getRegionStockId() {
        return regionStockId;
    }

    public void setRegionStockId(Long regionStockId) {
        this.regionStockId = regionStockId;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getRegionStockName() {
        return regionStockName;
    }

    public void setRegionStockName(String regionStockName) {
        this.regionStockName = regionStockName;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getCreateDatetimeStr() {
        return createDatetimeStr;
    }

    public void setCreateDatetimeStr(String createDatetimeStr) {
        this.createDatetimeStr = createDatetimeStr;
    }

    public Long getExchangeStockId() {
        return exchangeStockId;
    }

    public void setExchangeStockId(Long exchangeStockId) {
        this.exchangeStockId = exchangeStockId;
    }

    public Long getRegionStockTransId() {
        return regionStockTransId;
    }

    public void setRegionStockTransId(Long regionStockTransId) {
        this.regionStockTransId = regionStockTransId;
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

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }

    public String getAgentOrderTypeName() {
        return agentOrderTypeName;
    }

    public void setAgentOrderTypeName(String agentOrderTypeName) {
        this.agentOrderTypeName = agentOrderTypeName;
    }

    public String getAgentRequestCode() {
        return agentRequestCode;
    }

    public void setAgentRequestCode(String agentRequestCode) {
        this.agentRequestCode = agentRequestCode;
    }

    public Long getAgentOrderType() {
        return agentOrderType;
    }

    public Long getStockOrderAgentId() {
        return stockOrderAgentId;
    }

    public void setStockOrderAgentId(Long stockOrderAgentId) {
        this.stockOrderAgentId = stockOrderAgentId;
    }

    public void setAgentOrderType(Long agentOrderType) {
        this.agentOrderType = agentOrderType;
        if(agentOrderType.equals(1L)){
            this.agentOrderTypeName= BundleUtil.getText("special.staff.export.deposit");
        }else{
            this.agentOrderTypeName=BundleUtil.getText("mn.stock.agency.export.finish");
        }
    }

    public Long getIsAutoGen() {
        return isAutoGen;
    }

    public void setIsAutoGen(Long isAutoGen) {
        this.isAutoGen = isAutoGen;
    }

    public String getTransportSource() {
        return transportSource;
    }

    public void setTransportSource(String transportSource) {
        this.transportSource = transportSource;
    }
}
