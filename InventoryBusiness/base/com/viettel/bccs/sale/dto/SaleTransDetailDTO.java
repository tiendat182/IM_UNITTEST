package com.viettel.bccs.sale.dto;

import com.google.common.base.Joiner;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SaleTransDetailDTO extends BaseDTO implements Serializable {

    private String account;
    private String accountingModelCode;
    private String accountingModelName;
    private Long amount; // tong tien da co thue
    private Date deliverDate;
    private String deliverStatus;
    private Long discountAmount;
    private Long discountId;
    private Date expiredDate;
    private String inTransId;
    private Long modelProgramId;
    private String modelProgramName;
    private String note;
    private Long posId;
    private Long price; // don gia
    private Long priceId;
    private Long priceVat;
    private Long promotionAmount;
    private Long promotionId;
    private Long quantity;
    private String revenueType;
    private String saleServicesCode;
    private Long saleServicesId;
    private String saleServicesName;
    private Long saleServicesPrice;
    private Long saleServicesPriceId;
    private Long saleServicesPriceVat;
    private Long saleServicesProgramId;
    private String saleServicesProgramName;
    private Date saleTransDate;
    private Long saleTransDetailId;
    private Long saleTransId;
    private Long stateId;
    private String stockModelCode;
    private Long stockModelId;
    private String stockModelName;
    private String stockTypeCode;
    private Long stockTypeId;
    private String stockTypeName;
    private String supplyMethod;
    private Long supplyMonth;
    private String supplyProgram;
    private String transferGood;
    private String updateStockType;
    private Long userDeliver;
    private Long userUpdate;
    private Long vat; // ts
    private Long vatAmount; // tien thue gtgt
    private String stringVat; // tien thue gtgt

    private Long totalPrice;
    private String invoiceService;
    private String reasonCode;

    private String checkSerial; //Giao dich chi tiet nay co serial hay khong 1- co, null - khong
    private String newStockModelName; //Luu gia tri moi muc dich thay doi thong tin ten mat hang (chi voi mat hang co sale_tran_type  =13)

    //add cho nhap tu file
    private Long amountNotTax;
    private Boolean isValid;
    private String ErrorNote;
    private Long prodPackId;
    private String unitName; // ten bo
    private Long depositPriceId;
    private Long depositPrice;
    private Long debitPriceId;
    private Long debitPrice;
    private String label;
    // thiendn1: phuc vu truong hop in theo bo
    List<SaleTransDetailDTO> saleTransDetailDTOList;

    public String getStockLabel() {
        return Joiner.on(" - ").skipNulls().join(stockModelCode, stockModelName);
    }

    public void generateLabel() {
        if (DataUtil.isNullOrZero(stockModelId)) {
            label = Joiner.on(" - ").skipNulls().join(saleServicesCode, saleServicesName);
        } else {
            label = Joiner.on(" - ").skipNulls().join(stockModelCode, stockModelName);
        }
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public String getErrorNote() {
        return ErrorNote;
    }

    public void setErrorNote(String errorNote) {
        ErrorNote = errorNote;
    }

    public Long getAmountNotTax() {
        return amountNotTax;
    }

    public void setAmountNotTax(Long amountNotTax) {
        this.amountNotTax = amountNotTax;
    }


    public static enum COLUMNS {ACCOUNT, ACCOUNTINGMODELCODE, ACCOUNTINGMODELNAME, AMOUNT, DELIVERDATE, DELIVERSTATUS, DISCOUNTAMOUNT, DISCOUNTID, EXPIREDDATE, INTRANSID, MODELPROGRAMID, MODELPROGRAMNAME, NOTE, POSID, PRICE, PRICEID, PRICEVAT, PROMOTIONAMOUNT, PROMOTIONID, QUANTITY, REVENUETYPE, SALESERVICESCODE, SALESERVICESID, SALESERVICESNAME, SALESERVICESPRICE, SALESERVICESPRICEID, SALESERVICESPRICEVAT, SALESERVICESPROGRAMID, SALESERVICESPROGRAMNAME, SALETRANSDATE, SALETRANSDETAILID, SALETRANSID, STATEID, STOCKMODELCODE, STOCKMODELID, STOCKMODELNAME, STOCKTYPECODE, STOCKTYPEID, STOCKTYPENAME, SUPPLYMETHOD, SUPPLYMONTH, SUPPLYPROGRAM, TRANSFERGOOD, UPDATESTOCKTYPE, USERDELIVER, USERUPDATE, VAT, VATAMOUNT, EXCLUSE_ID_LIST}

    ;


    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountingModelCode() {
        return this.accountingModelCode;
    }

    public void setAccountingModelCode(String accountingModelCode) {
        this.accountingModelCode = accountingModelCode;
    }

    public String getAccountingModelName() {
        return this.accountingModelName;
    }

    public void setAccountingModelName(String accountingModelName) {
        this.accountingModelName = accountingModelName;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getDeliverDate() {
        return this.deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getDeliverStatus() {
        return this.deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public Long getDiscountAmount() {
        return this.discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Long getDiscountId() {
        return this.discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Date getExpiredDate() {
        return this.expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getInTransId() {
        return this.inTransId;
    }

    public void setInTransId(String inTransId) {
        this.inTransId = inTransId;
    }

    public Long getModelProgramId() {
        return this.modelProgramId;
    }

    public void setModelProgramId(Long modelProgramId) {
        this.modelProgramId = modelProgramId;
    }

    public String getModelProgramName() {
        return this.modelProgramName;
    }

    public void setModelProgramName(String modelProgramName) {
        this.modelProgramName = modelProgramName;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Long getPriceVat() {
        return this.priceVat;
    }

    public void setPriceVat(Long priceVat) {
        this.priceVat = priceVat;
    }

    public Long getPromotionAmount() {
        return this.promotionAmount;
    }

    public void setPromotionAmount(Long promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public Long getPromotionId() {
        return this.promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getRevenueType() {
        return this.revenueType;
    }

    public void setRevenueType(String revenueType) {
        this.revenueType = revenueType;
    }

    public String getSaleServicesCode() {
        return this.saleServicesCode;
    }

    public void setSaleServicesCode(String saleServicesCode) {
        this.saleServicesCode = saleServicesCode;
    }

    public Long getSaleServicesId() {
        return this.saleServicesId;
    }

    public void setSaleServicesId(Long saleServicesId) {
        this.saleServicesId = saleServicesId;
    }

    public String getSaleServicesName() {
        return this.saleServicesName;
    }

    public void setSaleServicesName(String saleServicesName) {
        this.saleServicesName = saleServicesName;
    }

    public Long getSaleServicesPrice() {
        return this.saleServicesPrice;
    }

    public void setSaleServicesPrice(Long saleServicesPrice) {
        this.saleServicesPrice = saleServicesPrice;
    }

    public Long getSaleServicesPriceId() {
        return this.saleServicesPriceId;
    }

    public void setSaleServicesPriceId(Long saleServicesPriceId) {
        this.saleServicesPriceId = saleServicesPriceId;
    }

    public Long getSaleServicesPriceVat() {
        return this.saleServicesPriceVat;
    }

    public void setSaleServicesPriceVat(Long saleServicesPriceVat) {
        this.saleServicesPriceVat = saleServicesPriceVat;
    }

    public Long getSaleServicesProgramId() {
        return this.saleServicesProgramId;
    }

    public void setSaleServicesProgramId(Long saleServicesProgramId) {
        this.saleServicesProgramId = saleServicesProgramId;
    }

    public String getSaleServicesProgramName() {
        return this.saleServicesProgramName;
    }

    public void setSaleServicesProgramName(String saleServicesProgramName) {
        this.saleServicesProgramName = saleServicesProgramName;
    }

    public Date getSaleTransDate() {
        return this.saleTransDate;
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }

    public Long getSaleTransDetailId() {
        return this.saleTransDetailId;
    }

    public void setSaleTransDetailId(Long saleTransDetailId) {
        this.saleTransDetailId = saleTransDetailId;
    }

    public Long getSaleTransId() {
        return this.saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStockModelCode() {
        return this.stockModelCode;
    }

    public void setStockModelCode(String stockModelCode) {
        this.stockModelCode = stockModelCode;
    }

    public Long getStockModelId() {
        return this.stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public String getStockModelName() {
        return this.stockModelName;
    }

    public void setStockModelName(String stockModelName) {
        this.stockModelName = stockModelName;
    }

    public String getStockTypeCode() {
        return this.stockTypeCode;
    }

    public void setStockTypeCode(String stockTypeCode) {
        this.stockTypeCode = stockTypeCode;
    }

    public Long getStockTypeId() {
        return this.stockTypeId;
    }

    public void setStockTypeId(Long stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public String getStockTypeName() {
        return this.stockTypeName;
    }

    public void setStockTypeName(String stockTypeName) {
        this.stockTypeName = stockTypeName;
    }

    public String getSupplyMethod() {
        return this.supplyMethod;
    }

    public void setSupplyMethod(String supplyMethod) {
        this.supplyMethod = supplyMethod;
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

    public String getTransferGood() {
        return this.transferGood;
    }

    public void setTransferGood(String transferGood) {
        this.transferGood = transferGood;
    }

    public String getUpdateStockType() {
        return this.updateStockType;
    }

    public void setUpdateStockType(String updateStockType) {
        this.updateStockType = updateStockType;
    }

    public Long getUserDeliver() {
        return this.userDeliver;
    }

    public void setUserDeliver(Long userDeliver) {
        this.userDeliver = userDeliver;
    }

    public Long getUserUpdate() {
        return this.userUpdate;
    }

    public void setUserUpdate(Long userUpdate) {
        this.userUpdate = userUpdate;
    }

    public Long getVat() {
        return this.vat;
    }

    public void setVat(Long vat) {
        this.vat = vat;
    }

    public Long getVatAmount() {
        return this.vatAmount;
    }

    public void setVatAmount(Long vatAmount) {
        this.vatAmount = vatAmount;
    }


    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }


    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getInvoiceService() {
        return invoiceService;
    }

    public void setInvoiceService(String invoiceService) {
        this.invoiceService = invoiceService;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getStringVat() {
        return stringVat;
    }

    public void setStringVat(String stringVat) {
        this.stringVat = stringVat;
    }

    public Long getProdPackId() {
        return prodPackId;
    }

    public void setProdPackId(Long prodPackId) {
        this.prodPackId = prodPackId;
    }

    public List<SaleTransDetailDTO> getSaleTransDetailDTOList() {
        return saleTransDetailDTOList;
    }

    public void setSaleTransDetailDTOList(List<SaleTransDetailDTO> saleTransDetailDTOList) {
        this.saleTransDetailDTOList = saleTransDetailDTOList;
    }

    public String getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(String checkSerial) {
        this.checkSerial = checkSerial;
    }

    public Long getDebitPrice() {
        return debitPrice;
    }

    public void setDebitPrice(Long debitPrice) {
        this.debitPrice = debitPrice;
    }

    public Long getDebitPriceId() {
        return debitPriceId;
    }

    public void setDebitPriceId(Long debitPriceId) {
        this.debitPriceId = debitPriceId;
    }

    public Long getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(Long depositPrice) {
        this.depositPrice = depositPrice;
    }

    public Long getDepositPriceId() {
        return depositPriceId;
    }

    public void setDepositPriceId(Long depositPriceId) {
        this.depositPriceId = depositPriceId;
    }

    public String getNewStockModelName() {
        return newStockModelName;
    }

    public void setNewStockModelName(String newStockModelName) {
        this.newStockModelName = newStockModelName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}