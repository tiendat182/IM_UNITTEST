package com.viettel.bccs.inventory.dto.ws;

import com.viettel.fw.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductOfferingSM extends BaseDTO {

    private String code;
    private Long productOfferingId;
    private Long quantity;
    private Long stateId;
    private List<StockTransSerialSM> listStockTransSerialDTOs = new ArrayList<>();

    public Long getProductOfferingId() {
        return productOfferingId;
    }

    public void setProductOfferingId(Long productOfferingId) {
        this.productOfferingId = productOfferingId;
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

    public List<StockTransSerialSM> getListStockTransSerialDTOs() {
        return listStockTransSerialDTOs;
    }

    public void setListStockTransSerialDTOs(List<StockTransSerialSM> listStockTransSerialDTOs) {
        this.listStockTransSerialDTOs = listStockTransSerialDTOs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
