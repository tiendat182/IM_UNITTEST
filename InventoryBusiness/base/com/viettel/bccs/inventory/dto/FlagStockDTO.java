package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class FlagStockDTO extends BaseDTO implements Serializable {

    //Stock_total for export stock
    private boolean exportStock;
    private Long expAvailableQuantity;
    private Long expCurrentQuantity;
    private Long expHangQuantity;

    //Stock_total for import stock
    private boolean importStock;
    private Long impAvailableQuantity;
    private Long impCurrentQuantity;
    private Long impHangQuantity;

    //status for region export stock
    private String regionExportStatus;
    private String prefixExportActionCode;

    //status for region import stock
    private String regionImportStatus;
    private String prefixImportActionCode;

    private String prefixActionCode;

    //status for update stock_x
    private Long oldStatus;
    private Long newStatus;
    private String note;
    private Long stateId;
    private boolean updateOwnerId;
    private boolean insertReceiptExpense;
    // luu thong tin dat coc
    private String receiptExpenseType;
    private Long receiptExpenseTypeId;
    private String receiptExpensePayMethod;
    private String receiptExpenseStatus;
    private String depositType;
    private String depositStatus;
    private String depositTypeId;
    private boolean insertStockTotalAudit;
    private boolean serialListFromTransDetail;
    private Long requestType;
    private boolean updateAccountBalance;
    private Long stateIdForReasonId;
    private boolean updateRescue;
    private String statusForAgent;
    private Long findSerial;
    private String objectType;
    private boolean updateSaleDate = false;
    private boolean updateDamageProduct = false;
    private boolean isGpon = false;
    private boolean updateProdOfferId = false;
    private boolean updateStateDemo = false;
    private boolean updateDepositPrice;

    public boolean isUpdateDepositPrice() {
        return updateDepositPrice;
    }

    public void setUpdateDepositPrice(boolean updateDepositPrice) {
        this.updateDepositPrice = updateDepositPrice;
    }

    public boolean isUpdateStateDemo() {
        return updateStateDemo;
    }

    public void setUpdateStateDemo(boolean updateStateDemo) {
        this.updateStateDemo = updateStateDemo;
    }

    private Long shopId;

    public FlagStockDTO() {
        expAvailableQuantity = 0L;
        expCurrentQuantity = 0L;
        expHangQuantity = 0L;
        impAvailableQuantity = 0L;
        impCurrentQuantity = 0L;
        impHangQuantity = 0L;
        insertReceiptExpense = false;
        serialListFromTransDetail = false;
        insertStockTotalAudit = true;
        updateAccountBalance = false;
        updateSaleDate = false;
        updateDamageProduct = false;
        updateProdOfferId = false;
    }

    public boolean isUpdateProdOfferId() {
        return updateProdOfferId;
    }

    public void setUpdateProdOfferId(boolean updateProdOfferId) {
        this.updateProdOfferId = updateProdOfferId;
    }

    public boolean isExportStock() {
        return exportStock;
    }

    public void setExportStock(boolean exportStock) {
        this.exportStock = exportStock;
    }

    public Long getExpAvailableQuantity() {
        return expAvailableQuantity;
    }

    public void setExpAvailableQuantity(Long expAvailableQuantity) {
        this.expAvailableQuantity = expAvailableQuantity;
    }

    public Long getExpCurrentQuantity() {
        return expCurrentQuantity;
    }

    public void setExpCurrentQuantity(Long expCurrentQuantity) {
        this.expCurrentQuantity = expCurrentQuantity;
    }

    public Long getExpHangQuantity() {
        return expHangQuantity;
    }

    public void setExpHangQuantity(Long expHangQuantity) {
        this.expHangQuantity = expHangQuantity;
    }

    public boolean isImportStock() {
        return importStock;
    }

    public void setImportStock(boolean importStock) {
        this.importStock = importStock;
    }

    public Long getImpAvailableQuantity() {
        return impAvailableQuantity;
    }

    public void setImpAvailableQuantity(Long impAvailableQuantity) {
        this.impAvailableQuantity = impAvailableQuantity;
    }

    public Long getImpCurrentQuantity() {
        return impCurrentQuantity;
    }

    public void setImpCurrentQuantity(Long impCurrentQuantity) {
        this.impCurrentQuantity = impCurrentQuantity;
    }

    public Long getImpHangQuantity() {
        return impHangQuantity;
    }

    public void setImpHangQuantity(Long impHangQuantity) {
        this.impHangQuantity = impHangQuantity;
    }

    public String getRegionExportStatus() {
        return regionExportStatus;
    }

    public void setRegionExportStatus(String regionExportStatus) {
        this.regionExportStatus = regionExportStatus;
    }

    public String getRegionImportStatus() {
        return regionImportStatus;
    }

    public void setRegionImportStatus(String regionImportStatus) {
        this.regionImportStatus = regionImportStatus;
    }

    public String getPrefixExportActionCode() {
        return prefixExportActionCode;
    }

    public void setPrefixExportActionCode(String prefixExportActionCode) {
        this.prefixExportActionCode = prefixExportActionCode;
    }

    public String getPrefixImportActionCode() {
        return prefixImportActionCode;
    }

    public void setPrefixImportActionCode(String prefixImportActionCode) {
        this.prefixImportActionCode = prefixImportActionCode;
    }

    public String getPrefixActionCode() {
        return prefixActionCode;
    }

    public void setPrefixActionCode(String prefixActionCode) {
        this.prefixActionCode = prefixActionCode;
    }

    public Long getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Long oldStatus) {
        this.oldStatus = oldStatus;
    }

    public Long getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Long newStatus) {
        this.newStatus = newStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public boolean isUpdateOwnerId() {
        return updateOwnerId;
    }

    public void setUpdateOwnerId(boolean updateOwnerId) {
        this.updateOwnerId = updateOwnerId;
    }

    public boolean isInsertReceiptExpense() {
        return insertReceiptExpense;
    }

    public void setInsertReceiptExpense(boolean insertReceiptExpense) {
        this.insertReceiptExpense = insertReceiptExpense;
    }

    public String getReceiptExpenseType() {
        return receiptExpenseType;
    }

    public void setReceiptExpenseType(String receiptExpenseType) {
        this.receiptExpenseType = receiptExpenseType;
    }

    public Long getReceiptExpenseTypeId() {
        return receiptExpenseTypeId;
    }

    public void setReceiptExpenseTypeId(Long receiptExpenseTypeId) {
        this.receiptExpenseTypeId = receiptExpenseTypeId;
    }

    public String getReceiptExpensePayMethod() {
        return receiptExpensePayMethod;
    }

    public void setReceiptExpensePayMethod(String receiptExpensePayMethod) {
        this.receiptExpensePayMethod = receiptExpensePayMethod;
    }

    public String getReceiptExpenseStatus() {
        return receiptExpenseStatus;
    }

    public void setReceiptExpenseStatus(String receiptExpenseStatus) {
        this.receiptExpenseStatus = receiptExpenseStatus;
    }

    public String getDepositType() {
        return depositType;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

    public String getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }

    public boolean isInsertStockTotalAudit() {
        return insertStockTotalAudit;
    }

    public void setInsertStockTotalAudit(boolean insertStockTotalAudit) {
        this.insertStockTotalAudit = insertStockTotalAudit;
    }

    public boolean isSerialListFromTransDetail() {
        return serialListFromTransDetail;
    }

    public void setSerialListFromTransDetail(boolean serialListFromTransDetail) {
        this.serialListFromTransDetail = serialListFromTransDetail;
    }

    public Long getRequestType() {
        return requestType;
    }

    public void setRequestType(Long requestType) {
        this.requestType = requestType;
    }

    public boolean isUpdateAccountBalance() {
        return updateAccountBalance;
    }

    public void setUpdateAccountBalance(boolean updateAccountBalance) {
        this.updateAccountBalance = updateAccountBalance;
    }

    public String getDepositTypeId() {
        return depositTypeId;
    }

    public void setDepositTypeId(String depositTypeId) {
        this.depositTypeId = depositTypeId;
    }

    public Long getStateIdForReasonId() {
        return stateIdForReasonId;
    }

    public void setStateIdForReasonId(Long stateIdForReasonId) {
        this.stateIdForReasonId = stateIdForReasonId;
    }

    public boolean isUpdateRescue() {
        return updateRescue;
    }

    public void setUpdateRescue(boolean updateRescue) {
        this.updateRescue = updateRescue;
    }

    public String getStatusForAgent() {
        return statusForAgent;
    }

    public Long getFindSerial() {
        return findSerial;
    }

    public void setFindSerial(Long findSerial) {
        this.findSerial = findSerial;
    }

    public void setStatusForAgent(String statusForAgent) {
        this.statusForAgent = statusForAgent;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public boolean isUpdateSaleDate() {
        return updateSaleDate;
    }

    public void setUpdateSaleDate(boolean updateSaleDate) {
        this.updateSaleDate = updateSaleDate;
    }

    public boolean isUpdateDamageProduct() {
        return updateDamageProduct;
    }

    public void setUpdateDamageProduct(boolean updateDamageProduct) {
        this.updateDamageProduct = updateDamageProduct;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public boolean isGpon() {
        return isGpon;
    }

    public void setGpon(boolean isGpon) {
        this.isGpon = isGpon;
    }
}
