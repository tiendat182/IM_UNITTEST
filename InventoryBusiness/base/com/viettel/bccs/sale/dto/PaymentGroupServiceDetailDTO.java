package com.viettel.bccs.sale.dto;
import java.lang.Long;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
public class PaymentGroupServiceDetailDTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private Long id;
    private Long maxDayDebit;
    private Long paymentGroupServiceId;
    private Long telecomServiceId;
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getMaxDayDebit() {
        return this.maxDayDebit;
    }
    public void setMaxDayDebit(Long maxDayDebit) {
        this.maxDayDebit = maxDayDebit;
    }
    public Long getPaymentGroupServiceId() {
        return this.paymentGroupServiceId;
    }
    public void setPaymentGroupServiceId(Long paymentGroupServiceId) {
        this.paymentGroupServiceId = paymentGroupServiceId;
    }
    public Long getTelecomServiceId() {
        return this.telecomServiceId;
    }
    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }
}
