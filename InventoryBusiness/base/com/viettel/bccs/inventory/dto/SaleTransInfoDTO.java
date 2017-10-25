package com.viettel.bccs.inventory.dto;

import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.fw.dto.BaseDTO;

import java.util.Date;

/**
 * Created by DungPT16 on 1/14/2016.
 */
public class SaleTransInfoDTO extends BaseDTO {
    private boolean other;
    private Date saleDate;
    private Long salerId;
    private String custName;
    private String company;
    private String tin; //Ma so thue
    private Long telecomServiceId;
    private String telNumber;
    private Long saleTransId;
    private String referenceNo;
    private Long recordWorkId; //Chuong trinh ban hang luu dong
    private String note;
    private String address; //Dia chi
    private Long stockTransId;
    private Long totalDiscount;
    private Long totalDiscountAfterTax;

    private Long paidAmount;
    private Long remainAmount;
    private Long reasonId;
    private Long payMethodId;
    private Long amountNotTax;
    private Long amountTax;
    private Long tax;
    private SaleTransDTO saleTrans;
    private String bankCode;
    private boolean stockOrderAgent;
    private String email;

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Long getSalerId() {
        return salerId;
    }

    public void setSalerId(Long salerId) {
        this.salerId = salerId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public Long getSaleTransId() {
        return saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Long getRecordWorkId() {
        return recordWorkId;
    }

    public void setRecordWorkId(Long recordWorkId) {
        this.recordWorkId = recordWorkId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Long totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Long getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Long paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Long getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(Long remainAmount) {
        this.remainAmount = remainAmount;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Long getPayMethodId() {
        return payMethodId;
    }

    public void setPayMethodId(Long payMethodId) {
        this.payMethodId = payMethodId;
    }

    public Long getTotalDiscountAfterTax() {
        return totalDiscountAfterTax;
    }

    public void setTotalDiscountAfterTax(Long totalDiscountAfterTax) {
        this.totalDiscountAfterTax = totalDiscountAfterTax;
    }

    public Long getAmountNotTax() {
        return amountNotTax;
    }

    public void setAmountNotTax(Long amountNotTax) {
        this.amountNotTax = amountNotTax;
    }

    public Long getAmountTax() {
        return amountTax;
    }

    public void setAmountTax(Long amountTax) {
        this.amountTax = amountTax;
    }

    public Long getTax() {
        return tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    public SaleTransDTO getSaleTrans() {
        return saleTrans;
    }

    public void setSaleTrans(SaleTransDTO saleTrans) {
        this.saleTrans = saleTrans;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public boolean isStockOrderAgent() {
        return stockOrderAgent;
    }

    public void setStockOrderAgent(boolean stockOrderAgent) {
        this.stockOrderAgent = stockOrderAgent;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
