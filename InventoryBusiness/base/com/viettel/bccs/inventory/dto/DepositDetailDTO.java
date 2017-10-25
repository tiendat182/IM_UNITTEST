package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class DepositDetailDTO extends BaseDTO implements Serializable {

    private Long amount;
    private Date createDate;
    private Long depositDetailId;
    private Long depositId;
    private String depositType;
    private Date expiredDate;
    private Long posId;
    private Long price;
    private Long priceId;
    private Long quantity;
    private String serial;
    private Long stockModelId;
    private Long supplyMonth;
    private String supplyProgram;

    public String getKeySet() {
        return keySet; }
    public Long getAmount() {
        return this.amount;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Long getDepositDetailId() {
        return this.depositDetailId;
    }
    public void setDepositDetailId(Long depositDetailId) {
        this.depositDetailId = depositDetailId;
    }
    public Long getDepositId() {
        return this.depositId;
    }
    public void setDepositId(Long depositId) {
        this.depositId = depositId;
    }
    public String getDepositType() {
        return this.depositType;
    }
    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }
    public Date getExpiredDate() {
        return this.expiredDate;
    }
    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }
    public Long getPosId() {
        return this.posId;
    }
    public void setPosId(Long posId) {
        this.posId = posId;
    }
    public Long getPrice() {
        return this.price;
    }
    public void setPrice(Long price) {
        this.price = price;
    }
    public Long getPriceId() {
        return this.priceId;
    }
    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }
    public Long getQuantity() {
        return this.quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public String getSerial() {
        return this.serial;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
    public Long getStockModelId() {
        return this.stockModelId;
    }
    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }
    public Long getSupplyMonth() {
        return this.supplyMonth;
    }
    public void setSupplyMonth(Long supplyMonth) {
        this.supplyMonth = supplyMonth;
    }
    public String getSupplyProgram() {
        return this.supplyProgram;
    }
    public void setSupplyProgram(String supplyProgram) {
        this.supplyProgram = supplyProgram;
    }
}
