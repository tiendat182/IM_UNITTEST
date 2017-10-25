package com.viettel.bccs.inventory.dto;

/**
 * Created by vanho on 05/06/2017.
 */
public class UpdateImsiInfoDTO {

    private String fromSerial;
    private String toSerial;
    private Long stockModelId;

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

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    @Override
    public String toString(){
        return "FROM_SERIAL: " + fromSerial + ", TO_SERIAL: " + toSerial + ", STOCK_MODEL_ID: " + stockModelId;
    }
}
