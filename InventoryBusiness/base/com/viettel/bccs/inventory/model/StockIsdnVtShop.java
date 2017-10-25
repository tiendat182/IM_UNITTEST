/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ThanhNT77
 */
@Entity
@Table(name = "STOCK_ISDN_VT_SHOP")
public class StockIsdnVtShop implements Serializable {
    public static enum COLUMNS {ADDRESS, AREACODE, CONTACTISDN, CREATEDATE, CUSTOMERNAME, IDNO, ISDN, LASTMODIFY, OTP, STAFFUPDATE, STATUS, VIETTELSHOPID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "OTP")
    private String otp;
    @Column(name = "ID_NO")
    private String idNo;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LAST_MODIFY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModify;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "STAFF_UPDATE")
    private Long staffUpdate;
    @Column(name = "CONTACT_ISDN")
    private String contactIsdn;
    @Column(name = "VIETTELSHOP_ID")
    private String viettelshopId;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "AREA_CODE")
    private String areaCode;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "EXPIRED_DATE")
    private Date expiredDate;
    @Column(name = "PAY_STATUS")
    private String payStatus;
    @Column(name = "REQUEST_ID")
    private Long requestId;
    @Column(name = "VALID")
    private String valid;


    public StockIsdnVtShop() {
    }

    public StockIsdnVtShop(String isdn) {
        this.isdn = isdn;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStaffUpdate() {
        return staffUpdate;
    }

    public void setStaffUpdate(Long staffUpdate) {
        this.staffUpdate = staffUpdate;
    }

    public String getContactIsdn() {
        return contactIsdn;
    }

    public void setContactIsdn(String contactIsdn) {
        this.contactIsdn = contactIsdn;
    }

    public String getViettelshopId() {
        return viettelshopId;
    }

    public void setViettelshopId(String viettelshopId) {
        this.viettelshopId = viettelshopId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }


    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (isdn != null ? isdn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockIsdnVtShop)) {
            return false;
        }
        StockIsdnVtShop other = (StockIsdnVtShop) object;
        if ((this.isdn == null && other.isdn != null) || (this.isdn != null && !this.isdn.equals(other.isdn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockIsdnVtShop[ isdn=" + isdn + " ]";
    }

}
