package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

/**
 * Created by ChungNV7 on 12/21/2015.
 */
public class TableNumberRangeDTO extends BaseDTO {

    private String shopCode;
    private String shopName;
    private String telecomService;
    private String fromISDN;
    private String toISDN;
    private Long total;
    private String status;
    private String statusName;
    private Long shopId;
    private String rowKey;
    private String telecomServiceName;
    private String oldStatus;
    private String oldStatusName;
    private String productCodeOld;
    private String productCodeNew;
    private Long productOfferId;
    private String productNameOld;
    private String productNameNew;
    private Long productOfferNewId;

    public Long getProductOfferNewId() {
        return productOfferNewId;
    }

    public void setProductOfferNewId(Long productOfferNewId) {
        this.productOfferNewId = productOfferNewId;
    }

    public String getProductNameOld() {
        return productNameOld;
    }

    public void setProductNameOld(String productNameOld) {
        this.productNameOld = productNameOld;
    }

    public String getProductNameNew() {
        return productNameNew;
    }

    public void setProductNameNew(String productNameNew) {
        this.productNameNew = productNameNew;
    }

    public Long getProductOfferId() {
        return productOfferId;
    }

    public void setProductOfferId(Long productOfferId) {
        this.productOfferId = productOfferId;
    }

    public String getProductCodeOld() {
        return productCodeOld;
    }

    public void setProductCodeOld(String productCodeOld) {
        this.productCodeOld = productCodeOld;
    }

    public String getProductCodeNew() {
        return productCodeNew;
    }

    public void setProductCodeNew(String productCodeNew) {
        this.productCodeNew = productCodeNew;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTelecomService() {
        return telecomService;
    }

    public void setTelecomService(String telecomService) {
        this.telecomService = telecomService;
    }

    public String getFromISDN() {
        return fromISDN;
    }

    public void setFromISDN(String fromISDN) {
        this.fromISDN = fromISDN;
    }

    public String getToISDN() {
        return toISDN;
    }

    public void setToISDN(String toISDN) {
        this.toISDN = toISDN;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getTelecomServiceName() {
        return telecomServiceName;
    }

    public void setTelecomServiceName(String telecomServiceName) {
        this.telecomServiceName = telecomServiceName;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getOldStatusName() {
        return oldStatusName;
    }

    public void setOldStatusName(String oldStatusName) {
        this.oldStatusName = oldStatusName;
    }

    @Override
    public String toString() {
        return rowKey + "";
    }
}
