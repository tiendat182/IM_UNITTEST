/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.sale.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author nhannt34
 */
@Entity
@Table(name = "SALE_TRANS_DETAIL", catalog = "", schema = "BCCS_SALE")
public class SaleTransDetail implements Serializable {
    public static enum COLUMNS {SALETRANSDETAILID, SALETRANSID}

    ;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALE_TRANS_DETAIL_ID")
    private Long saleTransDetailId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALE_TRANS_ID")
    private Long saleTransId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALE_TRANS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleTransDate;
    @Column(name = "STOCK_MODEL_ID")
    private Long stockModelId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "PRICE_ID")
    private Long priceId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "DISCOUNT_ID")
    private Long discountId;
    @Column(name = "AMOUNT")
    private Long amount;
    @Column(name = "AMOUNT_NOT_TAX")
    private Long amountNotTax;
    @Size(max = 1)
    @Column(name = "TRANSFER_GOOD")
    private String transferGood;
    @Column(name = "PROMOTION_ID")
    private Long promotionId;
    @Column(name = "PROMOTION_AMOUNT")
    private Long promotionAmount;
    @Size(max = 1000)
    @Column(name = "NOTE")
    private String note;
    @Column(name = "UPDATE_STOCK_TYPE")
    private String updateStockType;
    @Column(name = "USER_DELIVER")
    private Long userDeliver;
    @Column(name = "DELIVER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliverDate;
    @Column(name = "USER_UPDATE")
    private Long userUpdate;
    @Column(name = "DELIVER_STATUS")
    private String deliverStatus;
    @Column(name = "DISCOUNT_AMOUNT")
    private Long discountAmount;
    @Column(name = "VAT_AMOUNT")
    private Long vatAmount;
    @Column(name = "SALE_SERVICES_ID")
    private Long saleServicesId;
    @Column(name = "SALE_SERVICES_PRICE_ID")
    private Long saleServicesPriceId;
    @Column(name = "STOCK_TYPE_ID")
    private Long stockTypeId;
    @Size(max = 50)
    @Column(name = "STOCK_TYPE_CODE")
    private String stockTypeCode;
    @Size(max = 100)
    @Column(name = "STOCK_TYPE_NAME")
    private String stockTypeName;
    @Size(max = 50)
    @Column(name = "STOCK_MODEL_CODE")
    private String stockModelCode;
    @Size(max = 200)
    @Column(name = "STOCK_MODEL_NAME")
    private String stockModelName;
    @Column(name = "PRICE")
    private Long price;
    @Size(max = 50)
    @Column(name = "SALE_SERVICES_CODE")
    private String saleServicesCode;
    @Size(max = 200)
    @Column(name = "SALE_SERVICES_NAME")
    private String saleServicesName;
    @Column(name = "SALE_SERVICES_PRICE")
    private Long saleServicesPrice;
    @Column(name = "SALE_SERVICES_PRICE_VAT")
    private Long saleServicesPriceVat;
    @Column(name = "PRICE_VAT")
    private Long priceVat;
    @Size(max = 50)
    @Column(name = "ACCOUNTING_MODEL_CODE")
    private String accountingModelCode;
    @Size(max = 100)
    @Column(name = "ACCOUNTING_MODEL_NAME")
    private String accountingModelName;
    @Size(max = 2)
    @Column(name = "REVENUE_TYPE")
    private String revenueType;
    @Size(max = 10)
    @Column(name = "SUPPLY_METHOD")
    private String supplyMethod;
    @Size(max = 100)
    @Column(name = "ACCOUNT")
    private String account;
    @Column(name = "EXPIRED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredDate;
    @Column(name = "SUPPLY_MONTH")
    private Long supplyMonth;
    @Size(max = 50)
    @Column(name = "SUPPLY_PROGRAM")
    private String supplyProgram;
    @Column(name = "POS_ID")
    private Long posId;
    @Column(name = "MODEL_PROGRAM_ID")
    private Long modelProgramId;
    @Column(name = "SALE_SERVICES_PROGRAM_ID")
    private Long saleServicesProgramId;
    @Size(max = 100)
    @Column(name = "MODEL_PROGRAM_NAME")
    private String modelProgramName;
    @Size(max = 100)
    @Column(name = "SALE_SERVICES_PROGRAM_NAME")
    private String saleServicesProgramName;
    @Size(max = 20)
    @Column(name = "IN_TRANS_ID")
    private String inTransId;
    @Column(name = "PROD_PACK_ID")
    private Long prodPackId;
    @Column(name = "deposit_price_id")
    private Long depositPriceId;
    @Column(name = "deposit_price")
    private Long depositPrice;
    @Column(name = "debit_price_id")
    private Long debitPriceId;
    @Column(name = "debit_price")
    private Long debitPrice;

    public SaleTransDetail() {
    }

    public SaleTransDetail(Long saleTransDetailId) {
        this.saleTransDetailId = saleTransDetailId;
    }

    public SaleTransDetail(Long saleTransDetailId, Long saleTransId, Date saleTransDate, Long quantity) {
        this.saleTransDetailId = saleTransDetailId;
        this.saleTransId = saleTransId;
        this.saleTransDate = saleTransDate;
        this.quantity = quantity;
    }

    public Long getSaleTransDetailId() {
        return saleTransDetailId;
    }

    public void setSaleTransDetailId(Long saleTransDetailId) {
        this.saleTransDetailId = saleTransDetailId;
    }

    public Long getSaleTransId() {
        return saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public Date getSaleTransDate() {
        return saleTransDate;
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getTransferGood() {
        return transferGood;
    }

    public void setTransferGood(String transferGood) {
        this.transferGood = transferGood;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Long getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(Long promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUpdateStockType() {
        return updateStockType;
    }

    public void setUpdateStockType(String updateStockType) {
        this.updateStockType = updateStockType;
    }

    public Long getUserDeliver() {
        return userDeliver;
    }

    public void setUserDeliver(Long userDeliver) {
        this.userDeliver = userDeliver;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Long getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(Long userUpdate) {
        this.userUpdate = userUpdate;
    }

    public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Long getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Long vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Long getSaleServicesId() {
        return saleServicesId;
    }

    public void setSaleServicesId(Long saleServicesId) {
        this.saleServicesId = saleServicesId;
    }

    public Long getSaleServicesPriceId() {
        return saleServicesPriceId;
    }

    public void setSaleServicesPriceId(Long saleServicesPriceId) {
        this.saleServicesPriceId = saleServicesPriceId;
    }

    public Long getStockTypeId() {
        return stockTypeId;
    }

    public void setStockTypeId(Long stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public String getStockTypeCode() {
        return stockTypeCode;
    }

    public void setStockTypeCode(String stockTypeCode) {
        this.stockTypeCode = stockTypeCode;
    }

    public String getStockTypeName() {
        return stockTypeName;
    }

    public void setStockTypeName(String stockTypeName) {
        this.stockTypeName = stockTypeName;
    }

    public String getStockModelCode() {
        return stockModelCode;
    }

    public void setStockModelCode(String stockModelCode) {
        this.stockModelCode = stockModelCode;
    }

    public String getStockModelName() {
        return stockModelName;
    }

    public void setStockModelName(String stockModelName) {
        this.stockModelName = stockModelName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getSaleServicesCode() {
        return saleServicesCode;
    }

    public void setSaleServicesCode(String saleServicesCode) {
        this.saleServicesCode = saleServicesCode;
    }

    public String getSaleServicesName() {
        return saleServicesName;
    }

    public void setSaleServicesName(String saleServicesName) {
        this.saleServicesName = saleServicesName;
    }

    public Long getSaleServicesPrice() {
        return saleServicesPrice;
    }

    public void setSaleServicesPrice(Long saleServicesPrice) {
        this.saleServicesPrice = saleServicesPrice;
    }

    public Long getSaleServicesPriceVat() {
        return saleServicesPriceVat;
    }

    public void setSaleServicesPriceVat(Long saleServicesPriceVat) {
        this.saleServicesPriceVat = saleServicesPriceVat;
    }

    public Long getPriceVat() {
        return priceVat;
    }

    public void setPriceVat(Long priceVat) {
        this.priceVat = priceVat;
    }

    public String getAccountingModelCode() {
        return accountingModelCode;
    }

    public void setAccountingModelCode(String accountingModelCode) {
        this.accountingModelCode = accountingModelCode;
    }

    public String getAccountingModelName() {
        return accountingModelName;
    }

    public void setAccountingModelName(String accountingModelName) {
        this.accountingModelName = accountingModelName;
    }

    public String getRevenueType() {
        return revenueType;
    }

    public void setRevenueType(String revenueType) {
        this.revenueType = revenueType;
    }

    public String getSupplyMethod() {
        return supplyMethod;
    }

    public void setSupplyMethod(String supplyMethod) {
        this.supplyMethod = supplyMethod;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Long getSupplyMonth() {
        return supplyMonth;
    }

    public void setSupplyMonth(Long supplyMonth) {
        this.supplyMonth = supplyMonth;
    }

    public String getSupplyProgram() {
        return supplyProgram;
    }

    public void setSupplyProgram(String supplyProgram) {
        this.supplyProgram = supplyProgram;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public Long getModelProgramId() {
        return modelProgramId;
    }

    public void setModelProgramId(Long modelProgramId) {
        this.modelProgramId = modelProgramId;
    }

    public Long getSaleServicesProgramId() {
        return saleServicesProgramId;
    }

    public void setSaleServicesProgramId(Long saleServicesProgramId) {
        this.saleServicesProgramId = saleServicesProgramId;
    }

    public String getModelProgramName() {
        return modelProgramName;
    }

    public void setModelProgramName(String modelProgramName) {
        this.modelProgramName = modelProgramName;
    }

    public String getSaleServicesProgramName() {
        return saleServicesProgramName;
    }

    public void setSaleServicesProgramName(String saleServicesProgramName) {
        this.saleServicesProgramName = saleServicesProgramName;
    }

    public String getInTransId() {
        return inTransId;
    }

    public void setInTransId(String inTransId) {
        this.inTransId = inTransId;
    }

    public Long getAmountNotTax() {
        return amountNotTax;
    }

    public void setAmountNotTax(Long amountNotTax) {
        this.amountNotTax = amountNotTax;
    }

    public Long getProdPackId() {
        return prodPackId;
    }

    public void setProdPackId(Long prodPackId) {
        this.prodPackId = prodPackId;
    }

    public Long getDepositPriceId() {
        return depositPriceId;
    }

    public void setDepositPriceId(Long depositPriceId) {
        this.depositPriceId = depositPriceId;
    }

    public Long getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(Long depositPrice) {
        this.depositPrice = depositPrice;
    }

    public Long getDebitPriceId() {
        return debitPriceId;
    }

    public void setDebitPriceId(Long debitPriceId) {
        this.debitPriceId = debitPriceId;
    }

    public Long getDebitPrice() {
        return debitPrice;
    }

    public void setDebitPrice(Long debitPrice) {
        this.debitPrice = debitPrice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saleTransDetailId != null ? saleTransDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaleTransDetail)) {
            return false;
        }
        SaleTransDetail other = (SaleTransDetail) object;
        if ((this.saleTransDetailId == null && other.saleTransDetailId != null) || (this.saleTransDetailId != null && !this.saleTransDetailId.equals(other.saleTransDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.sale.model.SaleTransDetail[ saleTransDetailId=" + saleTransDetailId + " ]";
    }


}
