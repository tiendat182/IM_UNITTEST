package com.viettel.bccs.inventory.dto;
import java.lang.Long;
import java.util.Date;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
public class StockTransDetailKcsDTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private Date createDatetime;
    private Long quantity;
    private Long stockTransDetailKcsId;
    private Long stockTransId;
    private Long prodOfferId;
    private Long stockTransDetailId;
    private Long stateId;
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    public Long getQuantity() {
        return this.quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public Long getStockTransDetailKcsId() {
        return this.stockTransDetailKcsId;
    }
    public void setStockTransDetailKcsId(Long stockTransDetailKcsId) {
        this.stockTransDetailKcsId = stockTransDetailKcsId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getStockTransDetailId() {
        return stockTransDetailId;
    }

    public void setStockTransDetailId(Long stockTransDetailId) {
        this.stockTransDetailId = stockTransDetailId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
}
