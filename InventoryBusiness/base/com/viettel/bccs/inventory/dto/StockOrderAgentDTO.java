package com.viettel.bccs.inventory.dto;
import java.lang.Long;
import java.util.Date;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
public class StockOrderAgentDTO extends BaseDTO implements Serializable {
    private String bankCode;
    private Date createDate;
    private Long createStaffId;
    private Date lastModify;
    private String note;
    private Long orderType;
    private String requestCode;
    private Long shopId;
    private Long status;
    private Long stockOrderAgentId;
    private Long stockTransId;
    private Long updateStaffId;
    private Date fromDate;
    private Date toDate;

    public String getKeySet() {
        return keySet; }
    public String getBankCode() {
        return this.bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Long getCreateStaffId() {
        return this.createStaffId;
    }
    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }
    public Date getLastModify() {
        return this.lastModify;
    }
    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Long getOrderType() {
        return this.orderType;
    }
    public void setOrderType(Long orderType) {
        this.orderType = orderType;
    }
    public String getRequestCode() {
        return this.requestCode;
    }
    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }
    public Long getShopId() {
        return this.shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public Long getStatus() {
        return this.status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public Long getStockOrderAgentId() {
        return this.stockOrderAgentId;
    }
    public void setStockOrderAgentId(Long stockOrderAgentId) {
        this.stockOrderAgentId = stockOrderAgentId;
    }
    public Long getStockTransId() {
        return this.stockTransId;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }
    public Long getUpdateStaffId() {
        return this.updateStaffId;
    }
    public void setUpdateStaffId(Long updateStaffId) {
        this.updateStaffId = updateStaffId;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
}
