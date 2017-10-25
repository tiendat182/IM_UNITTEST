package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ImportPartnerDetailDTO extends BaseDTO implements Serializable {

    private Date createDate;
    private Long importPartnerDetailId;
    private long importPartnerRequestId;
    private Long prodOfferId;
    private Long quantity;
    private Long stateId;
    private String prodOfferName;
    private Long prodOfferSimId;
    private String kind;
    private String a3a8;
    private List<StockTransSerialDTO> lstStockTransSerialDTO;
    private List<String> lstParam;
    private Long realQuantity;
    private String prodOfferCode;
    private String fromSerial;
    private String toSerial;
    private String strQuantity;
    private String strStateId;
    private String strUnitPrice;
    private Double unitPrice;
    private Double totalPrice;
    private String orderCode;
    private String strStartDateWarranty;
    private Date startDateWarranty;
    private String strTotalPrice;
    private String error;


    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStrTotalPrice() {
        return strTotalPrice;
    }

    public void setStrTotalPrice(String strTotalPrice) {
        this.strTotalPrice = strTotalPrice;
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

    public String getStrQuantity() {
        return strQuantity;
    }

    public void setStrQuantity(String strQuantity) {
        this.strQuantity = strQuantity;
    }

    public String getStrStateId() {
        return strStateId;
    }

    public void setStrStateId(String strStateId) {
        this.strStateId = strStateId;
    }

    public String getStrUnitPrice() {
        return strUnitPrice;
    }

    public void setStrUnitPrice(String strUnitPrice) {
        this.strUnitPrice = strUnitPrice;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getStrStartDateWarranty() {
        return strStartDateWarranty;
    }

    public void setStrStartDateWarranty(String strStartDateWarranty) {
        this.strStartDateWarranty = strStartDateWarranty;
    }

    public Date getStartDateWarranty() {
        return startDateWarranty;
    }

    public void setStartDateWarranty(Date startDateWarranty) {
        this.startDateWarranty = startDateWarranty;
    }

    public String getKeySet() {
        return keySet;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getImportPartnerDetailId() {
        return importPartnerDetailId;
    }

    public void setImportPartnerDetailId(Long importPartnerDetailId) {
        this.importPartnerDetailId = importPartnerDetailId;
    }

    public long getImportPartnerRequestId() {
        return importPartnerRequestId;
    }

    public void setImportPartnerRequestId(long importPartnerRequestId) {
        this.importPartnerRequestId = importPartnerRequestId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public Long getProdOfferSimId() {
        return prodOfferSimId;
    }

    public void setProdOfferSimId(Long prodOfferSimId) {
        this.prodOfferSimId = prodOfferSimId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getA3a8() {
        return a3a8;
    }

    public void setA3a8(String a3a8) {
        this.a3a8 = a3a8;
    }

    public List<StockTransSerialDTO> getLstStockTransSerialDTO() {
        return lstStockTransSerialDTO;
    }

    public void setLstStockTransSerialDTO(List<StockTransSerialDTO> lstStockTransSerialDTO) {
        this.lstStockTransSerialDTO = lstStockTransSerialDTO;
    }

    public List<String> getLstParam() {
        return lstParam;
    }

    public void setLstParam(List<String> lstParam) {
        this.lstParam = lstParam;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public Long getRealQuantity() {
        return realQuantity;
    }

    public void setRealQuantity(Long realQuantity) {
        this.realQuantity = realQuantity;
    }
}
