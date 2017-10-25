package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.util.List;

public class StockTransFileDTO extends BaseDTO {

    private StockTransDTO stockTransDTO;
    private StockTransActionDTO stockTransActionDTO;
    private List<StockTransDetailDTO> lstStockTransDetail;
    private String msgError;
    private String key;

    public StockTransDTO getStockTransDTO() {
        return stockTransDTO;
    }

    public void setStockTransDTO(StockTransDTO stockTransDTO) {
        this.stockTransDTO = stockTransDTO;
    }

    public StockTransActionDTO getStockTransActionDTO() {
        return stockTransActionDTO;
    }

    public void setStockTransActionDTO(StockTransActionDTO stockTransActionDTO) {
        this.stockTransActionDTO = stockTransActionDTO;
    }

    public List<StockTransDetailDTO> getLstStockTransDetail() {
        return lstStockTransDetail;
    }

    public void setLstStockTransDetail(List<StockTransDetailDTO> lstStockTransDetail) {
        this.lstStockTransDetail = lstStockTransDetail;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
