package com.viettel.bccs.inventory.dto;

import java.util.Date;

/**
 * Created by DungPT16 on 1/15/2016.
 */
public class SaleTransInfoMin {
    private Date saleDate;
    private Long stockTransId;
    private String address; //Dia chi
    private boolean printInvoice = true;
    private PayBankAccount payBankAccount = new PayBankAccount(); //Tai khoan ngan hang
    private boolean stockOrderAgent;
    private String custName;
    private String telNumber;
    private String email;

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPrintInvoice() {
        return printInvoice;
    }

    public void setPrintInvoice(boolean printInvoice) {
        this.printInvoice = printInvoice;
    }

    public boolean isStockOrderAgent() {
        return stockOrderAgent;
    }

    public void setStockOrderAgent(boolean stockOrderAgent) {
        this.stockOrderAgent = stockOrderAgent;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PayBankAccount getPayBankAccount() {
        return payBankAccount;
    }

    public void setPayBankAccount(PayBankAccount payBankAccount) {
        this.payBankAccount = payBankAccount;
    }
}
