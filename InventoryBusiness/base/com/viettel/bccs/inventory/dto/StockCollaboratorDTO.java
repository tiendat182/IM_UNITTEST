package com.viettel.bccs.inventory.dto;

import com.viettel.bccs.inventory.dto.ws.ProductOfferingSM;
import com.viettel.fw.dto.BaseMessage;

import java.io.Serializable;
import java.util.List;

public class StockCollaboratorDTO extends BaseMessage implements Serializable {

    private List<ProductOfferingSM> productOfferingDTOs;

    public StockCollaboratorDTO() {
    }

    public StockCollaboratorDTO(boolean success, String errorCode, String keyMsg, String... paramsMsg) {
        super(success, errorCode, keyMsg, paramsMsg);
    }

    public List<ProductOfferingSM> getProductOfferingDTOs() {
        return productOfferingDTOs;
    }

    public void setProductOfferingDTOs(List<ProductOfferingSM> productOfferingDTOs) {
        this.productOfferingDTOs = productOfferingDTOs;
    }
}
