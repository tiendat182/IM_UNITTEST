package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockIsdnTransDetailDTO extends BaseDTO implements Serializable {

    private Date createdTime;
    private String fromIsdn;
    private Long lengthIsdn;
    private Long quantity;
    private Long stockIsdnTransDetailId;
    private Long stockIsdnTransId;
    private String toIsdn;
    private Long typeIsdn;

    public String getKeySet() {
        return keySet; }
    public Date getCreatedTime() {
        return this.createdTime;
    }
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    public String getFromIsdn() {
        return this.fromIsdn;
    }
    public void setFromIsdn(String fromIsdn) {
        this.fromIsdn = fromIsdn;
    }
    public Long getLengthIsdn() {
        return this.lengthIsdn;
    }
    public void setLengthIsdn(Long lengthIsdn) {
        this.lengthIsdn = lengthIsdn;
    }
    public Long getQuantity() {
        return this.quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public Long getStockIsdnTransDetailId() {
        return this.stockIsdnTransDetailId;
    }
    public void setStockIsdnTransDetailId(Long stockIsdnTransDetailId) {
        this.stockIsdnTransDetailId = stockIsdnTransDetailId;
    }
    public Long getStockIsdnTransId() {
        return this.stockIsdnTransId;
    }
    public void setStockIsdnTransId(Long stockIsdnTransId) {
        this.stockIsdnTransId = stockIsdnTransId;
    }
    public String getToIsdn() {
        return this.toIsdn;
    }
    public void setToIsdn(String toIsdn) {
        this.toIsdn = toIsdn;
    }
    public Long getTypeIsdn() {
        return this.typeIsdn;
    }
    public void setTypeIsdn(Long typeIsdn) {
        this.typeIsdn = typeIsdn;
    }
}
