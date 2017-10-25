/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * @author ThanhNT77
 */
@Entity
@Table(name = "STOCK_ISDN_VT_SHOP_LOCK")
public class StockIsdnVtShopLock implements Serializable {
    public static enum COLUMNS {ISDN, OWNERID, OWNERTYPE, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private StockIsdnVtShopLockId stockIsdnVtShopLockId;


    public StockIsdnVtShopLock() {
    }

    public StockIsdnVtShopLockId getStockIsdnVtShopLockId() {
        return stockIsdnVtShopLockId;
    }

    public void setStockIsdnVtShopLockId(StockIsdnVtShopLockId stockIsdnVtShopLockId) {
        this.stockIsdnVtShopLockId = stockIsdnVtShopLockId;
    }
}
