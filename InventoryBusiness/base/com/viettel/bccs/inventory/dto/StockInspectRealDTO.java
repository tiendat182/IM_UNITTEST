package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockInspectRealDTO extends BaseDTO implements Serializable {

    private Date createDate;
    private String fromSerial;
    private Long quantity;
    private Long stockInspectId;
    private Long stockInspectRealId;
    private String toSerial;
    private String singleSerial;
    private String serial2D;
    private String productOfferName;

    public String getKeySet() {
        return keySet;
    }

    public String getProductOfferName() {
        return productOfferName;
    }

    public void setProductOfferName(String productOfferName) {
        this.productOfferName = productOfferName;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFromSerial() {
        return this.fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStockInspectId() {
        return this.stockInspectId;
    }

    public void setStockInspectId(Long stockInspectId) {
        this.stockInspectId = stockInspectId;
    }

    public Long getStockInspectRealId() {
        return this.stockInspectRealId;
    }

    public void setStockInspectRealId(Long stockInspectRealId) {
        this.stockInspectRealId = stockInspectRealId;
    }

    public String getToSerial() {
        return this.toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public String getSingleSerial() {
        return singleSerial;
    }

    public void setSingleSerial(String singleSerial) {
        this.singleSerial = singleSerial;
    }

    public String getSerial2D() {
        return serial2D;
    }

    public void setSerial2D(String serial2D) {
        this.serial2D = serial2D;
    }
}
