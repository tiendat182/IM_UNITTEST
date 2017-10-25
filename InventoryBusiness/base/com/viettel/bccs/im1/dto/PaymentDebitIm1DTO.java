package com.viettel.bccs.im1.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class PaymentDebitIm1DTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private Date createDate;
    private String createUser;
    private Date lastUpdateDate;
    private String lastUpdateUser;
    private Long ownerId;
    private Long ownerType;
    private Long paymentDebitId;
    private Long paymentGroupId;
    private Long paymentGroupServiceId;
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateUser() {
        return this.createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
    public String getLastUpdateUser() {
        return this.lastUpdateUser;
    }
    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
    public Long getOwnerId() {
        return this.ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Long getOwnerType() {
        return this.ownerType;
    }
    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }
    public Long getPaymentDebitId() {
        return this.paymentDebitId;
    }
    public void setPaymentDebitId(Long paymentDebitId) {
        this.paymentDebitId = paymentDebitId;
    }
    public Long getPaymentGroupId() {
        return this.paymentGroupId;
    }
    public void setPaymentGroupId(Long paymentGroupId) {
        this.paymentGroupId = paymentGroupId;
    }
    public Long getPaymentGroupServiceId() {
        return this.paymentGroupServiceId;
    }
    public void setPaymentGroupServiceId(Long paymentGroupServiceId) {
        this.paymentGroupServiceId = paymentGroupServiceId;
    }
}
