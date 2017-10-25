package com.viettel.bccs.inventory.message;

import com.viettel.fw.dto.BaseMessage;


/**
 * @author ThanhNT77
 */
public class StockTransMessage extends BaseMessage {
    private String actionCode;

    public StockTransMessage() {
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
}
