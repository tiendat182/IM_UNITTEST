package com.viettel.bccs.inventory.dto;
import java.lang.Long;
import java.util.Date;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
public class StockDepositDTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private Long chanelTypeId;
    private Date dateFrom;
    private Date dateTo;
    private Date lastModify;
    private Long maxStock;
    private Long prodOfferId;
    private Long status;
    private Long stockDepositId;
    private Long transType;
    private String userModify;
    public Long getChanelTypeId() {
        return this.chanelTypeId;
    }
    public void setChanelTypeId(Long chanelTypeId) {
        this.chanelTypeId = chanelTypeId;
    }
    public Date getDateFrom() {
        return this.dateFrom;
    }
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }
    public Date getDateTo() {
        return this.dateTo;
    }
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
    public Date getLastModify() {
        return this.lastModify;
    }
    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }
    public Long getMaxStock() {
        return this.maxStock;
    }
    public void setMaxStock(Long maxStock) {
        this.maxStock = maxStock;
    }
    public Long getProdOfferId() {
        return this.prodOfferId;
    }
    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }
    public Long getStatus() {
        return this.status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public Long getStockDepositId() {
        return this.stockDepositId;
    }
    public void setStockDepositId(Long stockDepositId) {
        this.stockDepositId = stockDepositId;
    }
    public Long getTransType() {
        return this.transType;
    }
    public void setTransType(Long transType) {
        this.transType = transType;
    }
    public String getUserModify() {
        return this.userModify;
    }
    public void setUserModify(String userModify) {
        this.userModify = userModify;
    }
}
