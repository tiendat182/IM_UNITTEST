package com.viettel.bccs.sale.dto;

import com.viettel.fw.dto.BaseDTO;

/**
 * Created by hoangnt14 on 12/19/2016.
 */
public class PaymentGroupDayTypeDTO extends BaseDTO {

    private Long id;
    private Long paymentGroupId;
    private Long debitDayType;
    private Long status;
    private Long maxDebitMoney;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaymentGroupId() {
        return paymentGroupId;
    }

    public void setPaymentGroupId(Long paymentGroupId) {
        this.paymentGroupId = paymentGroupId;
    }

    public Long getDebitDayType() {
        return debitDayType;
    }

    public void setDebitDayType(Long debitDayType) {
        this.debitDayType = debitDayType;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getMaxDebitMoney() {
        return maxDebitMoney;
    }

    public void setMaxDebitMoney(Long maxDebitMoney) {
        this.maxDebitMoney = maxDebitMoney;
    }
}
