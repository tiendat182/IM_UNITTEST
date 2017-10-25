package com.viettel.bccs.inventory.message;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransFileDTO;
import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * @author thanhnt77 24/08/2016
 */
public class BasePartnerMessage extends BaseMessage {
    private List<String> lsSerialErorr = Lists.newArrayList();
    private Long stockTransActionId;


    public BasePartnerMessage() {
    }

    public List<String> getLsSerialErorr() {
        return lsSerialErorr;
    }

    public void setLsSerialErorr(List<String> lsSerialErorr) {
        this.lsSerialErorr = lsSerialErorr;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }
}
