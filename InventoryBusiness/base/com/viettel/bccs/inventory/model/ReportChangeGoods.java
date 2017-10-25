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

/**
 *
 * @author thetm1
 */
@Entity
@Table(name = "REPORT_CHANGE_GOODS")
@NamedQueries({
    @NamedQuery(name = "ReportChangeGoods.findAll", query = "SELECT r FROM ReportChangeGoods r")})
public class ReportChangeGoods implements Serializable {
public static enum COLUMNS {CREATEDATE, DATESENDSMS, ID, ISDN, PRODOFFERCODE, PRODOFFERID, PRODOFFERNAME, REASONID, SERIALNEW, SERIALOLD, SHOPCODE, SHOPID, SHOPNAME, STAFFCODE, STAFFID, STAFFNAME, STOCKTRANSID, TELECOMSERVICEID, TOTALSENDSMS, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Column(name = "SHOP_NAME")
    private String shopName;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Column(name = "STAFF_NAME")
    private String staffName;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "PROD_OFFER_CODE")
    private String prodOfferCode;
    @Column(name = "PROD_OFFER_NAME")
    private String prodOfferName;
    @Column(name = "SERIAL_NEW")
    private String serialNew;
    @Column(name = "SERIAL_OLD")
    private String serialOld;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "DATE_SEND_SMS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSendSms;
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "TOTAL_SEND_SMS")
    private Long totalSendSms;
    @Column(name = "REASON_ID")
    private Long reasonId;

    public ReportChangeGoods() {
    }

    public ReportChangeGoods(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

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

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public String getSerialNew() {
        return serialNew;
    }

    public void setSerialNew(String serialNew) {
        this.serialNew = serialNew;
    }

    public String getSerialOld() {
        return serialOld;
    }

    public void setSerialOld(String serialOld) {
        this.serialOld = serialOld;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Date getDateSendSms() {
        return dateSendSms;
    }

    public void setDateSendSms(Date dateSendSms) {
        this.dateSendSms = dateSendSms;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public Long getTotalSendSms() {
        return totalSendSms;
    }

    public void setTotalSendSms(Long totalSendSms) {
        this.totalSendSms = totalSendSms;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
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
        if (!(object instanceof ReportChangeGoods)) {
            return false;
        }
        ReportChangeGoods other = (ReportChangeGoods) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.ReportChangeGoods[ id=" + id + " ]";
    }

}
