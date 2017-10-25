package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class ShopMapDTO extends BaseDTO implements Serializable {


    public static enum COLUMNS {MAPCODE, SHOPCODE, SHOPID, STATUS, EXCLUSE_ID_LIST}
    private String mapCode; // VTNET
    private String shopCode; // VTT
    private Long shopId;
    private Short status;

    public String getKeySet() {
        return keySet;
    }

    public String getMapCode() {
        return this.mapCode;
    }

    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    public String getShopCode() {
        return this.shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Short getStatus() {
        return this.status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }
}
