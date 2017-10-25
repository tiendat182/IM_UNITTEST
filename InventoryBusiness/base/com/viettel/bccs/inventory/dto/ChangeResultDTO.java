package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

/**
 * Created by hoangnt14 on 8/9/2016.
 */
public class ChangeResultDTO extends BaseMessage {

    private Long stockTransId2G;
    private Long stockTransId3G;

    public ChangeResultDTO() {
    }

    public ChangeResultDTO(Long stockTransId2G, Long stockTransId3G, boolean success, String errorCode, String keyMsg, String... paramsMsg) {
        super(success, errorCode, keyMsg, paramsMsg);
        this.stockTransId2G = stockTransId2G;
        this.stockTransId3G = stockTransId3G;
    }

    public ChangeResultDTO(boolean success, String errorCode, String keyMsg, String... paramsMsg) {
        super(success, errorCode, keyMsg, paramsMsg);
    }

    public Long getStockTransId2G() {
        return stockTransId2G;
    }

    public void setStockTransId2G(Long stockTransId2G) {
        this.stockTransId2G = stockTransId2G;
    }

    public Long getStockTransId3G() {
        return stockTransId3G;
    }

    public void setStockTransId3G(Long stockTransId3G) {
        this.stockTransId3G = stockTransId3G;
    }
}
