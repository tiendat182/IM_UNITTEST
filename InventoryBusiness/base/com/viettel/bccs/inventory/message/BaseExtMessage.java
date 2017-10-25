package com.viettel.bccs.inventory.message;

import com.viettel.bccs.inventory.dto.StockTransFileDTO;
import com.viettel.fw.dto.BaseMessage;

/**
 * Created by ThaoNT19 on 2/20/2016.
 */
public class BaseExtMessage extends BaseMessage {
    StockTransFileDTO stockTransFileDTO ;
    private String descriptionFile;

    public StockTransFileDTO getStockTransFileDTO() {
        return stockTransFileDTO;
    }

    public void setStockTransFileDTO(StockTransFileDTO stockTransFileDTO) {
        this.stockTransFileDTO = stockTransFileDTO;
    }

    public BaseExtMessage(BaseMessage msg) {
        super(msg);
    }

    public BaseExtMessage() {
    }

    public String getDescriptionFile() {
        return descriptionFile;
    }

    public void setDescriptionFile(String descriptionFile) {
        this.descriptionFile = descriptionFile;
    }
}
