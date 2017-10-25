package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.fw.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnhNV33 on 01/21/2016.
 */
public class FieldExportFileDTO extends BaseDTO {

    private String actionCode;//ma lenh xuat
    private String fromOwnerCode;//ma kho xuat
    private String toOwnerCode;//ma kho nhan
    private String productOfferCode;//ma mat hang
    private Long quantity;//so luong
    private String strQuantity;//so luong
    private String note;//ghi chu
    private String strState;//trang thai hang NEW (moi), DEMAGE (hong)
    private Long stateId;//id trang thai hang
    private Long fromOwnerId;//id kho xuat
    private Long toOwnerId;//id kho nhan
    private Long productOfferId;//id mat hang
    private ShopDTO exportShop;//doi tuong kho xuat
    private ShopDTO receiveShop;//doi tuong kho nhan
    private ProductOfferingDTO productOfferingDTO;//doi tuong mat hang
    private String stockBase;//can cu xuat kho
    private String errorMsg;//noi dung loi
    private String strCreateDate;
    private String actionCodeOrder;
    private String fromOwnerName;//ma kho xuat
    private String toOwnerName;//ma kho nhan
    private String prodOfferCode;
    private String prodOfferName;
    private String fromSerial;
    private String toSerial;
    private String stateName;
    private Long quanlity;
    private String key;
    private ProductOfferingDTO productOfferingDTONew;//doi tuong mat hang
    private String prodOfferCodeNew;//ma mat hang
    private String prodOfferNameNew;
    private Long productOfferIdNew;//id mat hang



    private List<StockTransDetailDTO> lsStockTransDetail = Lists.newArrayList();


    public FieldExportFileDTO() {

    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getFromOwnerCode() {
        return fromOwnerCode;
    }

    public void setFromOwnerCode(String fromOwnerCode) {
        this.fromOwnerCode = fromOwnerCode;
    }

    public String getToOwnerCode() {
        return toOwnerCode;
    }

    public void setToOwnerCode(String toOwnerCode) {
        this.toOwnerCode = toOwnerCode;
    }

    public String getProductOfferCode() {
        return productOfferCode;
    }

    public void setProductOfferCode(String productOfferCode) {
        this.productOfferCode = productOfferCode;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStrState() {
        return strState;
    }

    public void setStrState(String strState) {
        this.strState = strState;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Long getProductOfferId() {
        return productOfferId;
    }

    public void setProductOfferId(Long productOfferId) {
        this.productOfferId = productOfferId;
    }

    public ShopDTO getExportShop() {
        return exportShop;
    }

    public void setExportShop(ShopDTO exportShop) {
        this.exportShop = exportShop;
    }

    public ShopDTO getReceiveShop() {
        return receiveShop;
    }

    public void setReceiveShop(ShopDTO receiveShop) {
        this.receiveShop = receiveShop;
    }

    public ProductOfferingDTO getProductOfferingDTO() {
        return productOfferingDTO;
    }

    public void setProductOfferingDTO(ProductOfferingDTO productOfferingDTO) {
        this.productOfferingDTO = productOfferingDTO;
    }

    public String getStockBase() {
        return stockBase;
    }

    public void setStockBase(String stockBase) {
        this.stockBase = stockBase;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getStrQuantity() {
        return strQuantity;
    }

    public void setStrQuantity(String strQuantity) {
        this.strQuantity = strQuantity;
    }

    public String getStrCreateDate() {
        return strCreateDate;
    }

    public void setStrCreateDate(String strCreateDate) {
        this.strCreateDate = strCreateDate;
    }

    public String getActionCodeOrder() {
        return actionCodeOrder;
    }

    public void setActionCodeOrder(String actionCodeOrder) {
        this.actionCodeOrder = actionCodeOrder;
    }

    public String getFromOwnerName() {
        return fromOwnerName;
    }

    public void setFromOwnerName(String fromOwnerName) {
        this.fromOwnerName = fromOwnerName;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public List<StockTransDetailDTO> getLsStockTransDetail() {
        return lsStockTransDetail;
    }

    public void setLsStockTransDetail(List<StockTransDetailDTO> lsStockTransDetail) {
        this.lsStockTransDetail = lsStockTransDetail;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Long getQuanlity() {
        return quanlity;
    }

    public void setQuanlity(Long quanlity) {
        this.quanlity = quanlity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSerial() {
        return toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public String getProdOfferCodeNew() {
        return prodOfferCodeNew;
    }

    public void setProdOfferCodeNew(String prodOfferCodeNew) {
        this.prodOfferCodeNew = prodOfferCodeNew;
    }

    public String getProdOfferNameNew() {
        return prodOfferNameNew;
    }

    public void setProdOfferNameNew(String prodOfferNameNew) {
        this.prodOfferNameNew = prodOfferNameNew;
    }

    public Long getProductOfferIdNew() {
        return productOfferIdNew;
    }

    public void setProductOfferIdNew(Long productOfferIdNew) {
        this.productOfferIdNew = productOfferIdNew;
    }

    public ProductOfferingDTO getProductOfferingDTONew() {
        return productOfferingDTONew;
    }

    public void setProductOfferingDTONew(ProductOfferingDTO productOfferingDTONew) {
        this.productOfferingDTONew = productOfferingDTONew;
    }
}

