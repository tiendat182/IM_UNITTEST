package com.viettel.bccs.inventory.dto;

/**
 * Created by hoangnt14 on 4/5/2017.
 */
public class StockDeviceDTO {
    private String actionCode;
    private String serial;
    private String productOfferCode;
    private String newProductOfferCode;
    private String newProductOfferName;
    private Long quantity;
    private String statusName;
    private Long price;
    private String requestShopName;
    private Long requestShopId;

    public String getRequestShopName() {
        return requestShopName;
    }

    public void setRequestShopName(String requestShopName) {
        this.requestShopName = requestShopName;
    }

    public Long getRequestShopId() {
        return requestShopId;
    }

    public void setRequestShopId(Long requestShopId) {
        this.requestShopId = requestShopId;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getProductOfferCode() {
        return productOfferCode;
    }

    public void setProductOfferCode(String productOfferCode) {
        this.productOfferCode = productOfferCode;
    }

    public String getNewProductOfferCode() {
        return newProductOfferCode;
    }

    public void setNewProductOfferCode(String newProductOfferCode) {
        this.newProductOfferCode = newProductOfferCode;
    }

    public String getNewProductOfferName() {
        return newProductOfferName;
    }

    public void setNewProductOfferName(String newProductOfferName) {
        this.newProductOfferName = newProductOfferName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
