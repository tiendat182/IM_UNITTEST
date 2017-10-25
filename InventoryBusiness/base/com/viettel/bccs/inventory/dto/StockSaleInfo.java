package com.viettel.bccs.inventory.dto;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DungPT16 on 1/15/2016.
 */
public class StockSaleInfo extends StockSaleInfoMin {
    private ProductOfferingDTO stockModel = new ProductOfferingDTO();
    private ProductOfferTypeDTO stockType = new ProductOfferTypeDTO();
    private ProductOfferPriceDTO price = new ProductOfferPriceDTO(); //Gia duy nhat sau khi chon tren giao dien
    @XStreamImplicit(itemFieldName = "priceLst")
    private List<ProductOfferPriceDTO> priceLst = new ArrayList<>();//Danh sach gia ma giao dich vien co the chon
    @XStreamImplicit(itemFieldName = "candidateStockLst")
    private List<ProductOfferingDTO> candidateStockLst = new ArrayList<>(); //Danh sach hang hoa ung voi serial ma GDV co the chon
    private Long localAddDate; //thoi diem add vao list
    private boolean inPackage; //ban theo bo
    private boolean service;
    private boolean bySerial;
    private String errorMsg; //loi gan voi mat hang
    private Long amount;
    private Long vatAmount;
    private Long amountNotTax;
    private String note;
    private Long saleTransDetailId;
    private Long discountId;

    // Dang fake du lieu o day
    private String unitName;
    private Long totalSerial;

    public StockSaleInfo() {
        // DO NOTHING HERE
    }

    public Long getAmountNotTax() {
        return amountNotTax;
    }

    public void setAmountNotTax(Long amountNotTax) {
        this.amountNotTax = amountNotTax;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Long vatAmount) {
        this.vatAmount = vatAmount;
    }

    public boolean isService() {
        return service;
    }

    public void setService(boolean service) {
        this.service = service;
    }


    public boolean isInPackage() {
        return inPackage;
    }

    public void setInPackage(boolean inPackage) {
        this.inPackage = inPackage;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ProductOfferTypeDTO getStockType() {
        return stockType;
    }

    public void setStockType(ProductOfferTypeDTO stockType) {
        this.stockType = stockType;
    }

    public Long getSaleTransDetailId() {
        return saleTransDetailId;
    }

    public void setSaleTransDetailId(Long saleTransDetailId) {
        this.saleTransDetailId = saleTransDetailId;
    }

    public ProductOfferingDTO getStockModel() {
        return stockModel;
    }

    public void setStockModel(ProductOfferingDTO stockModel) {
        this.stockModel = stockModel;
    }

    public ProductOfferPriceDTO getPrice() {
        return price;
    }

    public void setPrice(ProductOfferPriceDTO price) {
        this.price = price;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public List<ProductOfferPriceDTO> getPriceLst() {
        return priceLst;
    }

    public void setPriceLst(List<ProductOfferPriceDTO> priceLst) {
        this.priceLst = priceLst;
    }

    public boolean isBySerial() {
        return bySerial;
    }

    public void setBySerial(boolean bySerial) {
        this.bySerial = bySerial;
    }

    public List<ProductOfferingDTO> getCandidateStockLst() {
        return candidateStockLst;
    }

    public void setCandidateStockLst(List<ProductOfferingDTO> candidateStockLst) {
        this.candidateStockLst = candidateStockLst;
    }

    public Long getLocalAddDate() {
        return localAddDate;
    }

    public void setLocalAddDate(Long localAddDate) {
        this.localAddDate = localAddDate;
    }

    public Long getTotalSerial() {
        return totalSerial;
    }

    public void setTotalSerial(Long totalSerial) {
        this.totalSerial = totalSerial;
    }
}
