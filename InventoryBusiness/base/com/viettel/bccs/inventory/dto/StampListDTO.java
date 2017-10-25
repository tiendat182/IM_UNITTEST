package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

/**
 * Created by hoangnt14 on 12/27/2016.
 */
public class StampListDTO {
    private String stockModelCode;
    private String fromSerial;

    public String getStockModelCode() {
        return stockModelCode;
    }

    public void setStockModelCode(String stockModelCode) {
        this.stockModelCode = stockModelCode;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

}
