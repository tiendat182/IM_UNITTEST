package com.viettel.bccs.inventory.dto;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.dto.BaseDTO;

import java.util.List;

public class ExportStockInputDTO extends BaseDTO {
    private Long actionType;
    private String actionCode;
    private Long actionId;
    private Long transStatus;
    private Long transType;
    private Long fromOwnerType;
    private String fromOwnerCode;
    private Long fromOwnerId;
    private String fromOwnerName;
    private Long toOwnerType;
    private Long toOwnerId;
    private String toOwnerCode;
    private String toOwnerName;
    private String reasonName;
    //Loai kho xuat
    private String receiver;
    private String approver;
    private String dateExported;
    private String note;
    private Long status;
    private Long payMethodeid;
    private List<StockTransFullDTO> lstGoods;
    private String pricePolicy;
    private Long userId;
    private Long newStateId;
    private Long senderId;
    private String sender;
    private Long reasonIdImport;
    private Long reasonIdExport;
    private Long staffId;
    private String staffCode;
    private String stockTransStatus;
    private String stockTransType;
    private String stockTransActionStatus1;
    private String stockTransActionStatus2;
    private String stockTransActionStatus3;
    private String typeAction;
    private String prefixActionCode = "";
    private boolean checkIm1;
    boolean isValidLimitImport = true; // check validate han muc kho nhap, cai nay chi dung cho ham xuat/nhap

    /**
     * ham xu ly set trang thai action theo loai ham
     * @author thanhnt77
     */
    public void createStatusByTypeAction(String typeAction) {
        this.typeAction = typeAction;
        if (Const.WARRANTY_TYPE_ACTION.EXPORT.equals(typeAction)) {
            this.stockTransType = Const.STOCK_TRANS_TYPE.EXPORT;
            this.stockTransStatus = Const.STOCK_TRANS_STATUS.IMPORTED;
            this.stockTransActionStatus1 = Const.STOCK_TRANS_STATUS.EXPORT_NOTE;
            this.stockTransActionStatus2 = Const.STOCK_TRANS_STATUS.EXPORTED;
            this.stockTransActionStatus3 = Const.STOCK_TRANS_STATUS.IMPORTED;
            this.staffId = fromOwnerId;
            this.staffCode = fromOwnerCode;
            this.prefixActionCode = "PXBH00";
        } else if (Const.WARRANTY_TYPE_ACTION.IMPORT.equals(typeAction)) {
            this.stockTransType = Const.STOCK_TRANS_TYPE.IMPORT;
            this.stockTransStatus = Const.STOCK_TRANS_STATUS.IMPORTED;
            this.stockTransActionStatus1 = Const.STOCK_TRANS_STATUS.IMPORT_NOTE;
            this.stockTransActionStatus2 = Const.STOCK_TRANS_STATUS.IMPORTED;
            this.staffId = toOwnerId;
            this.staffCode = toOwnerCode;
            this.prefixActionCode = "PNBH00";
        } else if (Const.WARRANTY_TYPE_ACTION.EXP_IMP_FOR_STAFF.equals(typeAction)) {
            this.stockTransType = Const.STOCK_TRANS_TYPE.EXPORT;
            this.stockTransStatus = Const.STOCK_TRANS_STATUS.IMPORTED;
            this.stockTransActionStatus1 = Const.STOCK_TRANS_STATUS.EXPORT_NOTE;
            this.stockTransActionStatus2 = Const.STOCK_TRANS_STATUS.EXPORTED;
            this.stockTransActionStatus3 = Const.STOCK_TRANS_STATUS.IMPORTED;
            this.staffId = fromOwnerId;
            this.staffCode = fromOwnerCode;
            this.prefixActionCode = "PXBH00";
        } else if (Const.WARRANTY_TYPE_ACTION.EXPORT_CHANGE_STATEID.equals(typeAction)) {
            this.stockTransType = Const.STOCK_TRANS_TYPE.EXPORT;
            this.stockTransStatus = Const.STOCK_TRANS_STATUS.IMPORTED;
            this.stockTransActionStatus1 = Const.STOCK_TRANS_STATUS.EXPORT_NOTE;
            this.stockTransActionStatus2 = Const.STOCK_TRANS_STATUS.EXPORTED;
            this.stockTransActionStatus3 = Const.STOCK_TRANS_STATUS.IMPORTED;
            this.staffId = fromOwnerId;
            this.staffCode = fromOwnerCode;
            this.prefixActionCode = "PXBH00";
        } else if (Const.WARRANTY_TYPE_ACTION.IMPORT_GPON.equals(typeAction)) {
            this.stockTransType = Const.STOCK_TRANS_TYPE.IMPORT;
            this.stockTransStatus = Const.STOCK_TRANS_STATUS.IMPORTED;
            this.stockTransActionStatus1 = Const.STOCK_TRANS_STATUS.IMPORT_NOTE;
            this.stockTransActionStatus2 = Const.STOCK_TRANS_STATUS.IMPORTED;
            this.staffId = toOwnerId;
            this.staffCode = toOwnerCode;
            this.prefixActionCode = "PNBH00";
        }
    }

    public ExportStockInputDTO() {
    }

    public String getPrefixActionCode() {
        return prefixActionCode;
    }

    public Long getActionType() {
        return actionType;
    }

    public void setActionType(Long actionType) {
        this.actionType = actionType;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public Long getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(Long transStatus) {
        this.transStatus = transStatus;
    }

    public Long getTransType() {
        return transType;
    }

    public void setTransType(Long transType) {
        this.transType = transType;
    }

    public Long getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public String getFromOwnerCode() {
        return fromOwnerCode;
    }

    public void setFromOwnerCode(String fromOwnerCode) {
        this.fromOwnerCode = fromOwnerCode;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public String getFromOwnerName() {
        return fromOwnerName;
    }

    public void setFromOwnerName(String fromOwnerName) {
        this.fromOwnerName = fromOwnerName;
    }

    public Long getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public String getToOwnerCode() {
        return toOwnerCode;
    }

    public void setToOwnerCode(String toOwnerCode) {
        this.toOwnerCode = toOwnerCode;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getDateExported() {
        return dateExported;
    }

    public void setDateExported(String dateExported) {
        this.dateExported = dateExported;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getPayMethodeid() {
        return payMethodeid;
    }

    public void setPayMethodeid(Long payMethodeid) {
        this.payMethodeid = payMethodeid;
    }

    public List<StockTransFullDTO> getLstGoods() {
        return lstGoods;
    }

    public void setLstGoods(List<StockTransFullDTO> lstGoods) {
        this.lstGoods = lstGoods;
    }

    public String getPricePolicy() {
        return pricePolicy;
    }

    public void setPricePolicy(String pricePolicy) {
        this.pricePolicy = pricePolicy;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNewStateId() {
        return newStateId;
    }

    public void setNewStateId(Long newStateId) {
        this.newStateId = newStateId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getReasonIdImport() {
        return reasonIdImport;
    }

    public void setReasonIdImport(Long reasonIdImport) {
        this.reasonIdImport = reasonIdImport;
    }

    public Long getReasonIdExport() {
        return reasonIdExport;
    }

    public void setReasonIdExport(Long reasonIdExport) {
        this.reasonIdExport = reasonIdExport;
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

    public String getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(String stockTransType) {
        this.stockTransType = stockTransType;
    }

    public String getStockTransActionStatus1() {
        return stockTransActionStatus1;
    }

    public void setStockTransActionStatus1(String stockTransActionStatus1) {
        this.stockTransActionStatus1 = stockTransActionStatus1;
    }

    public String getStockTransActionStatus2() {
        return stockTransActionStatus2;
    }

    public void setStockTransActionStatus2(String stockTransActionStatus2) {
        this.stockTransActionStatus2 = stockTransActionStatus2;
    }

    public String getStockTransActionStatus3() {
        return stockTransActionStatus3;
    }

    public void setStockTransActionStatus3(String stockTransActionStatus3) {
        this.stockTransActionStatus3 = stockTransActionStatus3;
    }

    public void setPrefixActionCode(String prefixActionCode) {
        this.prefixActionCode = prefixActionCode;
    }

    public String getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(String typeAction) {
        this.typeAction = typeAction;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public boolean isCheckIm1() {
        return checkIm1;
    }

    public void setCheckIm1(boolean checkIm1) {
        this.checkIm1 = checkIm1;
    }

    public boolean isValidLimitImport() {
        return isValidLimitImport;
    }

    public void setValidLimitImport(boolean isValidLimitImport) {
        this.isValidLimitImport = isValidLimitImport;
    }
}
