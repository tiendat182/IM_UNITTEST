package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

import java.io.Serializable;

public class StockSimMessage extends BaseMessage implements Serializable {

    private StockSimDTO stockSimDTO;

    public StockSimDTO getStockSimDTO() {
        return stockSimDTO;
    }

    public void setStockSimDTO(StockSimDTO stockSimDTO) {
        this.stockSimDTO = stockSimDTO;
    }
}
