package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.io.Serializable;
import java.util.Date;

public class LockUserInfoDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private String sqlCheckTrans;

    private String shopCode;
    private String shopName;
    private String staffCode;
    private String staffName;
    private String fromOwner;
    private String toOwner;
    private String lockTypeName;
    private String reasonName;
    private Long stockTransType;
    private Long checkErp;
    private Date createDate;
    private Long id;
    private Long lockTypeId;
    private Long prodOfferId;
    private Long quantity;
    private String serial;
    private Long shopId;
    private Long staffId;
    private Long stockTransId;
    private String transCode;
    private Date transDate;
    private Long transId;
    private Long transStatus;
    private Long transType;

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

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getFromOwner() {
        return fromOwner;
    }

    public void setFromOwner(String fromOwner) {
        this.fromOwner = fromOwner;
    }

    public String getToOwner() {
        return toOwner;
    }

    public void setToOwner(String toOwner) {
        this.toOwner = toOwner;
    }

    public String getLockTypeName() {
        return lockTypeName;
    }

    public void setLockTypeName(String lockTypeName) {
        this.lockTypeName = lockTypeName;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public Long getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(Long stockTransType) {
        this.stockTransType = stockTransType;
    }

    public String getSqlCheckTrans() {
        return sqlCheckTrans;
    }

    public void setSqlCheckTrans(String sqlCheckTrans) {
        this.sqlCheckTrans = sqlCheckTrans;
    }

    public Long getCheckErp() {
        return this.checkErp;
    }

    public void setCheckErp(Long checkErp) {
        this.checkErp = checkErp;
    }

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

    public Long getLockTypeId() {
        return this.lockTypeId;
    }

    public void setLockTypeId(Long lockTypeId) {
        this.lockTypeId = lockTypeId;
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

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return this.staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getTransCode() {
        return this.transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public Date getTransDate() {
        return this.transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Long getTransId() {
        return this.transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public Long getTransStatus() {
        return this.transStatus;
    }

    public void setTransStatus(Long transStatus) {
        this.transStatus = transStatus;
    }

    public Long getTransType() {
        return this.transType;
    }

    public void setTransType(Long transType) {
        this.transType = transType;
    }
}
