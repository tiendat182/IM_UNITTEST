package com.viettel.bccs.im1.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockTransIm1DTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private Long accType;
    private String account;
    private Date approveDate;
    private Long approveReasonId;
    private Long approveUserId;
    private Long bankplusStatus;
    private Long checkErp;
    private Date createDatetime;
    private Long depositStatus;
    private Long drawStatus;
    private Long fromOwnerId;
    private Long fromOwnerType;
    private Long fromStockTransId;
    private Long goodsWeight;
    private Long isAutoGen;
    private String note;
    private String partnerContractNo;
    private Long payStatus;
    private Date processEnd;
    private Long processOffline;
    private String processResult;
    private Date processStart;
    private Date realTransDate;
    private Long realTransUserId;
    private Long reasonId;
    private Long regionStockTransId;
    private Date rejectDate;
    private String rejectNote;
    private Long rejectReasonId;
    private Long rejectUserId;
    private String requestId;
    private String stockBase;
    private Long stockTransId;
    private Long stockTransStatus;
    private Long stockTransType;
    private String synStatus;
    private Long toOwnerId;
    private Long toOwnerType;
    private Long totalDebit;
    private Long transport;
    private String troubleType;
    private Long valid;
    private Long warrantyStock;
    public Long getAccType() {
        return this.accType;
    }
    public void setAccType(Long accType) {
        this.accType = accType;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public Date getApproveDate() {
        return this.approveDate;
    }
    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }
    public Long getApproveReasonId() {
        return this.approveReasonId;
    }
    public void setApproveReasonId(Long approveReasonId) {
        this.approveReasonId = approveReasonId;
    }
    public Long getApproveUserId() {
        return this.approveUserId;
    }
    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }
    public Long getBankplusStatus() {
        return this.bankplusStatus;
    }
    public void setBankplusStatus(Long bankplusStatus) {
        this.bankplusStatus = bankplusStatus;
    }
    public Long getCheckErp() {
        return this.checkErp;
    }
    public void setCheckErp(Long checkErp) {
        this.checkErp = checkErp;
    }
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    public Long getDepositStatus() {
        return this.depositStatus;
    }
    public void setDepositStatus(Long depositStatus) {
        this.depositStatus = depositStatus;
    }
    public Long getDrawStatus() {
        return this.drawStatus;
    }
    public void setDrawStatus(Long drawStatus) {
        this.drawStatus = drawStatus;
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
    public Long getFromStockTransId() {
        return this.fromStockTransId;
    }
    public void setFromStockTransId(Long fromStockTransId) {
        this.fromStockTransId = fromStockTransId;
    }
    public Long getGoodsWeight() {
        return this.goodsWeight;
    }
    public void setGoodsWeight(Long goodsWeight) {
        this.goodsWeight = goodsWeight;
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
    public String getPartnerContractNo() {
        return this.partnerContractNo;
    }
    public void setPartnerContractNo(String partnerContractNo) {
        this.partnerContractNo = partnerContractNo;
    }
    public Long getPayStatus() {
        return this.payStatus;
    }
    public void setPayStatus(Long payStatus) {
        this.payStatus = payStatus;
    }
    public Date getProcessEnd() {
        return this.processEnd;
    }
    public void setProcessEnd(Date processEnd) {
        this.processEnd = processEnd;
    }
    public Long getProcessOffline() {
        return this.processOffline;
    }
    public void setProcessOffline(Long processOffline) {
        this.processOffline = processOffline;
    }
    public String getProcessResult() {
        return this.processResult;
    }
    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }
    public Date getProcessStart() {
        return this.processStart;
    }
    public void setProcessStart(Date processStart) {
        this.processStart = processStart;
    }
    public Date getRealTransDate() {
        return this.realTransDate;
    }
    public void setRealTransDate(Date realTransDate) {
        this.realTransDate = realTransDate;
    }
    public Long getRealTransUserId() {
        return this.realTransUserId;
    }
    public void setRealTransUserId(Long realTransUserId) {
        this.realTransUserId = realTransUserId;
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
    public Date getRejectDate() {
        return this.rejectDate;
    }
    public void setRejectDate(Date rejectDate) {
        this.rejectDate = rejectDate;
    }
    public String getRejectNote() {
        return this.rejectNote;
    }
    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }
    public Long getRejectReasonId() {
        return this.rejectReasonId;
    }
    public void setRejectReasonId(Long rejectReasonId) {
        this.rejectReasonId = rejectReasonId;
    }
    public Long getRejectUserId() {
        return this.rejectUserId;
    }
    public void setRejectUserId(Long rejectUserId) {
        this.rejectUserId = rejectUserId;
    }
    public String getRequestId() {
        return this.requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
    public Long getStockTransStatus() {
        return this.stockTransStatus;
    }
    public void setStockTransStatus(Long stockTransStatus) {
        this.stockTransStatus = stockTransStatus;
    }
    public Long getStockTransType() {
        return this.stockTransType;
    }
    public void setStockTransType(Long stockTransType) {
        this.stockTransType = stockTransType;
    }
    public String getSynStatus() {
        return this.synStatus;
    }
    public void setSynStatus(String synStatus) {
        this.synStatus = synStatus;
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
    public Long getTotalDebit() {
        return this.totalDebit;
    }
    public void setTotalDebit(Long totalDebit) {
        this.totalDebit = totalDebit;
    }
    public Long getTransport() {
        return this.transport;
    }
    public void setTransport(Long transport) {
        this.transport = transport;
    }
    public String getTroubleType() {
        return this.troubleType;
    }
    public void setTroubleType(String troubleType) {
        this.troubleType = troubleType;
    }
    public Long getValid() {
        return this.valid;
    }
    public void setValid(Long valid) {
        this.valid = valid;
    }
    public Long getWarrantyStock() {
        return this.warrantyStock;
    }
    public void setWarrantyStock(Long warrantyStock) {
        this.warrantyStock = warrantyStock;
    }
}
