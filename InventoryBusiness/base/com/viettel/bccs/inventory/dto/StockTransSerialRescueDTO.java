package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;
import org.springframework.security.access.annotation.Secured;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockTransSerialRescueDTO extends BaseDTO implements Serializable {

    private Date createDate;
    private String fromSerial;
    private Long prodOfferId;
    private Long quantity;
    private Long stateId;
    private Long stockTransId;
    private Long stockTransSerialId;
    private String toSerial;
    private String productCode;
    private String productName;
    private Long prodOfferIdReturn;
    private String serialReturn;
    private String prodCodeReturn;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;

    public List<ProductOfferingTotalDTO> getLstProductOfferingTotalDTO() {
        return lstProductOfferingTotalDTO;
    }

    public void setLstProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO) {
        this.lstProductOfferingTotalDTO = lstProductOfferingTotalDTO;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public String getProdCodeReturn() {
        return prodCodeReturn;
    }

    public void setProdCodeReturn(String prodCodeReturn) {
        this.prodCodeReturn = prodCodeReturn;
    }

    public Long getProdOfferIdReturn() {
        return prodOfferIdReturn;
    }

    public void setProdOfferIdReturn(Long prodOfferIdReturn) {
        this.prodOfferIdReturn = prodOfferIdReturn;
    }

    public String getSerialReturn() {
        return serialReturn;
    }

    public void setSerialReturn(String serialReturn) {
        this.serialReturn = serialReturn;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getKeySet() {
        return keySet;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFromSerial() {
        return this.fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getStockTransSerialId() {
        return this.stockTransSerialId;
    }

    public void setStockTransSerialId(Long stockTransSerialId) {
        this.stockTransSerialId = stockTransSerialId;
    }

    public String getToSerial() {
        return this.toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }
}
