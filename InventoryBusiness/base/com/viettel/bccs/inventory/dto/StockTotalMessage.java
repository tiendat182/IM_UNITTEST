package com.viettel.bccs.inventory.dto;

import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.fw.dto.BaseMessage;

import java.io.Serializable;

public class StockTotalMessage extends BaseMessage implements Serializable {

    private StockTotalDTO stockTotalDTO;
    private StockTotalIm1DTO stockTotalIm1DTO;

    public StockTotalDTO getStockTotalDTO() {
        return stockTotalDTO;
    }

    public void setStockTotalDTO(StockTotalDTO stockTotalDTO) {
        this.stockTotalDTO = stockTotalDTO;
    }

    public StockTotalIm1DTO getStockTotalIm1DTO() {
        return stockTotalIm1DTO;
    }

    public void setStockTotalIm1DTO(StockTotalIm1DTO stockTotalIm1DTO) {
        this.stockTotalIm1DTO = stockTotalIm1DTO;
    }
}
