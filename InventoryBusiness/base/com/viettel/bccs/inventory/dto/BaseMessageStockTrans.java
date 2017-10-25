package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

/**
 * Created by DungPT16 on 3/19/2016.
 */
public class BaseMessageStockTrans extends BaseMessage {
    private Long stockTransActionId;

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }
}
