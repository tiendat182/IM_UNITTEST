package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * Created by hoangnt14 on 1/24/2017.
 */
public class StockDeliverResultDTO extends BaseMessage {
    private List<StockDeliverInforDTO> lstStockDeliverInforDTO;

    public List<StockDeliverInforDTO> getLstStockDeliverInforDTO() {
        return lstStockDeliverInforDTO;
    }

    public void setLstStockDeliverInforDTO(List<StockDeliverInforDTO> lstStockDeliverInforDTO) {
        this.lstStockDeliverInforDTO = lstStockDeliverInforDTO;
    }

    public StockDeliverResultDTO() {

    }

    public StockDeliverResultDTO(List<StockDeliverInforDTO> lstStockDeliverInforDTO, boolean success, String errCode, String description, String... paramsMsg) {
        super(success, errCode, description, paramsMsg);
        this.lstStockDeliverInforDTO = lstStockDeliverInforDTO;
    }
}
