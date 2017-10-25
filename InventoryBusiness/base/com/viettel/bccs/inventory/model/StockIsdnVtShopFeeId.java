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
@Embeddable
public class StockIsdnVtShopFeeId implements Serializable {

    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "FEE_TYPE")
    private String feeType;

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
}
