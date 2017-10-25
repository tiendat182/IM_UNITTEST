package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class ApLockTransDetailDTO extends BaseDTO implements Serializable {

    private Date createDate;
    private Long id;
    private Short lockType;
    private Long posId;
    private Long prodOfferId;
    private Long quantity;
    private String requestId;
    private String serial;

    public String getKeySet() {
        return keySet; }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Short getLockType() {
        return this.lockType;
    }
    public void setLockType(Short lockType) {
        this.lockType = lockType;
    }
    public Long getPosId() {
        return this.posId;
    }
    public void setPosId(Long posId) {
        this.posId = posId;
    }
    public Long getProdOfferId() {
        return this.prodOfferId;
    }
    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }
    public Long getQuantity() {
        return this.quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public String getRequestId() {
        return this.requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public String getSerial() {
        return this.serial;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
}
