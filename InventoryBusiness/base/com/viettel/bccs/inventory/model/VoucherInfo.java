/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author HoangAnh
 */
@Entity
@Table(name = "VOUCHER_INFO")
public class VoucherInfo implements Serializable {
    public static enum COLUMNS {VOUCHERINFOID, SERIAL, PASS, STATUS, FROMDATE, TODATE, OBJECT, AMOUNT, CREATEDATE, LASTUPDATE, STAFFCODE, STATUSCONNECT, BONUSSTATUS}

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "VOUCHER_INFO_ID")
    @SequenceGenerator(name = "VOUCHER_INFO_ID_GENERATOR", sequenceName = "VOUCHER_INFO_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOUCHER_INFO_ID_GENERATOR")
    private Long voucherInfoId;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "PASS")
    private String pass;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "FROM_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;
    @Column(name = "TO_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;
    @Column(name = "OBJECT")
    private String object;
    @Column(name = "AMOUNT")
    private Long amount;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Column(name = "STATUS_CONNECT")
    private Long statusConnect;
    @Column(name = "BONUS_STATUS")
    private Short bonusStatus;

    public VoucherInfo() {
    }

    public VoucherInfo(Long voucherInfoId) {
        this.voucherInfoId = voucherInfoId;
    }

    public Long getVoucherInfoId() {
        return voucherInfoId;
    }

    public void setVoucherInfoId(Long voucherInfoId) {
        this.voucherInfoId = voucherInfoId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public Long getStatusConnect() {
        return statusConnect;
    }

    public void setStatusConnect(Long statusConnect) {
        this.statusConnect = statusConnect;
    }

    public Short getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(Short bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (voucherInfoId != null ? voucherInfoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VoucherInfo)) {
            return false;
        }
        VoucherInfo other = (VoucherInfo) object;
        if ((this.voucherInfoId == null && other.voucherInfoId != null) || (this.voucherInfoId != null && !this.voucherInfoId.equals(other.voucherInfoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.VoucherInfo[ voucherInfoId=" + voucherInfoId + " ]";
    }

}
