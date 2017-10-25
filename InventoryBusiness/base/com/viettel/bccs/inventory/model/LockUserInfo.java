package com.viettel.bccs.inventory.model;

/**
 * Created by hoangnt14 on 8/3/2016.
 */

import com.viettel.bccs.inventory.dto.LockUserInfoDTO;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author hoangnt14
 */
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "LOCK_USER_INFO")
public class LockUserInfo implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "TRANS_ID")
    private Long transId;
    @Column(name = "TRANS_CODE")
    private String transCode;
    @Column(name = "TRANS_TYPE")
    private Long transType;
    @Column(name = "TRANS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transDate;
    @Column(name = "TRANS_STATUS")
    private Long transStatus;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LOCK_TYPE_ID")
    private Long lockTypeId;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "LOCK_USER_INFO_ID_GENERATOR", sequenceName = "LOCK_USER_INFO_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCK_USER_INFO_ID_GENERATOR")
    private Long id;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "CHECK_ERP")
    private Long checkErp;

    public LockUserInfo() {
    }

    public LockUserInfo(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public Long getTransType() {
        return transType;
    }

    public void setTransType(Long transType) {
        this.transType = transType;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Long getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(Long transStatus) {
        this.transStatus = transStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getLockTypeId() {
        return lockTypeId;
    }

    public void setLockTypeId(Long lockTypeId) {
        this.lockTypeId = lockTypeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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

    public Long getCheckErp() {
        return checkErp;
    }

    public void setCheckErp(Long checkErp) {
        this.checkErp = checkErp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LockUserInfo)) {
            return false;
        }
        LockUserInfo other = (LockUserInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai1.LockUserInfo[ id=" + id + " ]";
    }

}
