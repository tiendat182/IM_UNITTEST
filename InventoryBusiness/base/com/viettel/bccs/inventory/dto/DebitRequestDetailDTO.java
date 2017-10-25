package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.util.Date;

public class DebitRequestDetailDTO extends BaseDTO {


    private String debitDayType;
    private Long debitValue;
    private String financeType;
    private String note;
    private Long ownerId;
    private String ownerType;
    private Date requestDate;
    private Long requestDetailId;
    private Long requestId;
    private String requestType;
    private String status;
    private String oldFinanceType;
    private String oldDebitDayType;
    private String oldDebitValue;
    private String shopId;
    private String shopCode;
    private String ownerCode;
    private Long longOldDebitValue;
    private String result;
    private String stt;
    private String financeName;
    private String debitLevelName;
    private String oldFinanceName;
    private String oldDebitLevelName;
    private Long staffId;
    private String typeDebit;
    private Long paymentGroupId;
    private Long paymentGroupServiceId;
    private String paymentGroupCode;
    private String paymentGroupServiceCode;
    private String paymentGroupName;
    private String paymentGroupServiceName;
    private Long paymentDebitValue;
    private Double distance;
    private String distanceStr;
    private String paymentType;

    public String getDistanceStr() {
        return distanceStr;
    }

    public void setDistanceStr(String distanceStr) {
        this.distanceStr = distanceStr;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getPaymentGroupName() {
        return paymentGroupName;
    }

    public void setPaymentGroupName(String paymentGroupName) {
        this.paymentGroupName = paymentGroupName;
    }

    public String getPaymentGroupServiceName() {
        return paymentGroupServiceName;
    }

    public void setPaymentGroupServiceName(String paymentGroupServiceName) {
        this.paymentGroupServiceName = paymentGroupServiceName;
    }

    public Long getPaymentDebitValue() {
        return paymentDebitValue;
    }

    public void setPaymentDebitValue(Long paymentDebitValue) {
        this.paymentDebitValue = paymentDebitValue;
    }

    public String getPaymentGroupCode() {
        return paymentGroupCode;
    }

    public void setPaymentGroupCode(String paymentGroupCode) {
        this.paymentGroupCode = paymentGroupCode;
    }

    public String getPaymentGroupServiceCode() {
        return paymentGroupServiceCode;
    }

    public void setPaymentGroupServiceCode(String paymentGroupServiceCode) {
        this.paymentGroupServiceCode = paymentGroupServiceCode;
    }

    public Long getPaymentGroupId() {
        return paymentGroupId;
    }

    public void setPaymentGroupId(Long paymentGroupId) {
        this.paymentGroupId = paymentGroupId;
    }

    public Long getPaymentGroupServiceId() {
        return paymentGroupServiceId;
    }

    public void setPaymentGroupServiceId(Long paymentGroupServiceId) {
        this.paymentGroupServiceId = paymentGroupServiceId;
    }

    public String getTypeDebit() {
        return typeDebit;
    }

    public void setTypeDebit(String typeDebit) {
        this.typeDebit = typeDebit;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getOldDebitLevelName() {
        return oldDebitLevelName;
    }

    public void setOldDebitLevelName(String oldDebitLevelName) {
        this.oldDebitLevelName = oldDebitLevelName;
    }

    public String getOldFinanceName() {
        return oldFinanceName;
    }

    public void setOldFinanceName(String oldFinanceName) {
        this.oldFinanceName = oldFinanceName;
    }

    public String getDebitLevelName() {
        return debitLevelName;
    }

    public void setDebitLevelName(String debitLevelName) {
        this.debitLevelName = debitLevelName;
    }

    public String getFinanceName() {
        return financeName;
    }

    public void setFinanceName(String financeName) {
        this.financeName = financeName;
    }

    public String getKeySet() {
        return keySet;
    }


    public Long getLongOldDebitValue() {
        if (!DataUtil.isNullOrEmpty(this.oldDebitValue)) {
            return DataUtil.safeToLong(oldDebitValue);
        }
        return longOldDebitValue;
    }

    public void setLongOldDebitValue(Long longOldDebitValue) {
        this.longOldDebitValue = longOldDebitValue;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getDebitDayType() {
        return this.debitDayType;
    }

    public void setDebitDayType(String debitDayType) {
        this.debitDayType = debitDayType;
    }

    public Long getDebitValue() {
        return debitValue;
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

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Date getRequestDate() {
        return this.requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Long getRequestDetailId() {
        return this.requestDetailId;
    }

    public void setRequestDetailId(Long requestDetailId) {
        this.requestDetailId = requestDetailId;
    }

    public Long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOldFinanceType() {
        return oldFinanceType;
    }

    public void setOldFinanceType(String oldFinanceType) {
        this.oldFinanceType = oldFinanceType;
    }

    public String getOldDebitDayType() {
        return oldDebitDayType;
    }

    public void setOldDebitDayType(String oldDebitDayType) {
        this.oldDebitDayType = oldDebitDayType;
    }

    public String getOldDebitValue() {
        return oldDebitValue;
    }

    public void setOldDebitValue(String oldDebitValue) {
        this.oldDebitValue = oldDebitValue;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }
}
