package com.viettel.bccs.inventory.dto;

import java.lang.Long;

import com.viettel.bccs.inventory.model.StockIsdnVtShopLockId;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockIsdnVtShopLockDTO extends BaseDTO implements Serializable {

    private StockIsdnVtShopLockId stockIsdnVtShopLockId;

    public StockIsdnVtShopLockDTO() {
        stockIsdnVtShopLockId = new StockIsdnVtShopLockId();
    }

    public StockIsdnVtShopLockId getStockIsdnVtShopLockId() {
        return stockIsdnVtShopLockId;
    }

    public void setStockIsdnVtShopLockId(StockIsdnVtShopLockId stockIsdnVtShopLockId) {
        this.stockIsdnVtShopLockId = stockIsdnVtShopLockId;
    }
}
