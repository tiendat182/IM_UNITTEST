package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.util.Date;

public class StockDebitDTO extends BaseDTO {

    private Date createDate;
    private String createUser;
    private String debitDayType;
    private Long debitValue;
    private String financeType;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private Long ownerId;
    private String ownerCode;
    private String ownerType;
    private Long stockDebitId;
    private String debitType;
    private String debitDayTypeName;
    private String financeTypeName;
    private Long totalPrice;
    private Long totalValueReceiveStock;
    private Long totalValueTransSuspendReceiveStock;
    private Long displayDebitValue;
    private Long displayFinance;

    public Long getDisplayFinance() {
        return displayFinance;
    }

    public void setDisplayFinance(Long displayFinance) {
        this.displayFinance = displayFinance;
    }

    public Long getDisplayDebitValue() {
        return displayDebitValue;
    }

    public void setDisplayDebitValue(Long displayDebitValue) {
        this.displayDebitValue = displayDebitValue;
    }

    public Long getTotalCanReceive() {
        Long value = (debitValue == null ? 0L : debitValue) - (totalValueReceiveStock == null ? 0L : totalValueReceiveStock) - (totalValueTransSuspendReceiveStock == null ? 0L : totalValueTransSuspendReceiveStock);
        return value < 0L ? 0L : value;
    }


    public String getKeySet() {
        return keySet;
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

    public String getDebitDayType() {
        return this.debitDayType;
    }

    public void setDebitDayType(String debitDayType) {
        this.debitDayType = debitDayType;
    }

    public Long getDebitValue() {
        return this.debitValue;
    }

    public void setDebitValue(Long debitValue) {
        this.debitValue = debitValue;
    }

    public String getFinanceType() {
        return this.financeType;
    }

    public void setFinanceType(String financeType) {
        this.financeType = financeType;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateUser() {
        return this.lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getStockDebitId() {
        return this.stockDebitId;
    }

    public void setStockDebitId(Long stockDebitId) {
        this.stockDebitId = stockDebitId;
    }

    public String getDebitType() {
        return debitType;
    }

    public void setDebitType(String debitType) {
        this.debitType = debitType;
    }

    public String getDebitDayTypeName() {
        return debitDayTypeName;
    }

    public void setDebitDayTypeName(String debitDayTypeName) {
        this.debitDayTypeName = debitDayTypeName;
    }

    public String getFinanceTypeName() {
        return financeTypeName;
    }

    public void setFinanceTypeName(String financeTypeName) {
        this.financeTypeName = financeTypeName;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getTotalValueReceiveStock() {
        return totalValueReceiveStock;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public void setTotalValueReceiveStock(Long totalValueReceiveStock) {
        this.totalValueReceiveStock = totalValueReceiveStock;
    }

    public Long getTotalValueTransSuspendReceiveStock() {
        return totalValueTransSuspendReceiveStock;
    }

    public void setTotalValueTransSuspendReceiveStock(Long totalValueTransSuspendReceiveStock) {
        this.totalValueTransSuspendReceiveStock = totalValueTransSuspendReceiveStock;
    }
}
