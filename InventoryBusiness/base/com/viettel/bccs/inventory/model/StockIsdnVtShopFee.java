/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author ThanhNT77
 */
@Entity
@Table(name = "STOCK_ISDN_VT_SHOP_FEE")
public class StockIsdnVtShopFee implements Serializable {
    public static enum COLUMNS {AMOUNT, FEETYPE, ISDN, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private StockIsdnVtShopFeeId stockIsdnVtShopFeeId;
    @Column(name = "AMOUNT")
    private Long amount;


    public StockIsdnVtShopFee() {
    }

    public StockIsdnVtShopFeeId getStockIsdnVtShopFeeId() {
        return stockIsdnVtShopFeeId;
    }

    public void setStockIsdnVtShopFeeId(StockIsdnVtShopFeeId stockIsdnVtShopFeeId) {
        this.stockIsdnVtShopFeeId = stockIsdnVtShopFeeId;
    }


    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}
