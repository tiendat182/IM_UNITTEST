package com.viettel.bccs.sale.dto;

import java.lang.Long;
import java.util.Date;
import java.lang.Integer;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class PaymentGroupDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Date createDate;
    private String createUser;
    private Long debitDayType;
    private Date lastUpdateDate;
    private String lastUpdateUser;
    private Long maxDayDebit;
    private Long maxMoneyDebit;
    private String name;
    private Long paymentGroupId;
    private Long status;
    private String type;

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

    public Long getDebitDayType() {
        return debitDayType;
    }

    public void setDebitDayType(Long debitDayType) {
        this.debitDayType = debitDayType;
    }

    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUser() {
        return this.lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Long getMaxDayDebit() {
        return this.maxDayDebit;
    }

    public void setMaxDayDebit(Long maxDayDebit) {
        this.maxDayDebit = maxDayDebit;
    }

    public Long getMaxMoneyDebit() {
        return this.maxMoneyDebit;
    }

    public void setMaxMoneyDebit(Long maxMoneyDebit) {
        this.maxMoneyDebit = maxMoneyDebit;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPaymentGroupId() {
        return this.paymentGroupId;
    }

    public void setPaymentGroupId(Long paymentGroupId) {
        this.paymentGroupId = paymentGroupId;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
