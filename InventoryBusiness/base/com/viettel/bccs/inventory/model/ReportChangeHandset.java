/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * @author DungPT16
 */
@Entity
@Table(name = "REPORT_CHANGE_HANDSET")
@NamedQueries({
        @NamedQuery(name = "ReportChangeHandset.findAll", query = "SELECT r FROM ReportChangeHandset r")})
public class ReportChangeHandset implements Serializable {
    public static enum COLUMNS {ADJUSTAMOUNT, CHANGETYPE, CREATEDATE, CUSTNAME, CUSTTEL, DAMAGEGOODSTATUS, DEVSTAFFID, PRODOFFERIDNEW, PRODOFFERIDOLD, REPORTCHANGEHANDSETID, SERIALNEW, SERIALOLD, SHOPID, STAFFID, STOCKTRANSID, EXCLUSE_ID_LIST}

    ;

    private static final long serialVersionUID = 1L;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "PROD_OFFER_ID_OLD")
    private Long prodOfferIdOld;
    @Column(name = "SERIAL_OLD")
    private String serialOld;
    @Column(name = "SERIAL_NEW")
    private String serialNew;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "PROD_OFFER_ID_NEW")
    private Long prodOfferIdNew;
    @Column(name = "ADJUST_AMOUNT")
    private Long adjustAmount;
    @Column(name = "DAMAGE_GOOD_STATUS")
    private String damageGoodStatus;
    @Column(name = "CHANGE_TYPE")
    private Long changeType;
    @Column(name = "CUST_NAME")
    private String custName;
    @Column(name = "CUST_TEL")
    private String custTel;
    @Column(name = "DEV_STAFF_ID")
    private Long devStaffId;
    @Id
    @Basic(optional = false)
    @Column(name = "REPORT_CHANGE_HANDSET_ID")
    @SequenceGenerator(name = "REPORT_CHANGE_HANDSET_ID_GENERATOR", sequenceName = "REPORT_CHANGE_HANDSET_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPORT_CHANGE_HANDSET_ID_GENERATOR")
    private Long reportChangeHandsetId;

    public ReportChangeHandset() {
    }

    public ReportChangeHandset(Long reportChangeHandsetId) {
        this.reportChangeHandsetId = reportChangeHandsetId;
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

    public Long getProdOfferIdOld() {
        return prodOfferIdOld;
    }

    public void setProdOfferIdOld(Long prodOfferIdOld) {
        this.prodOfferIdOld = prodOfferIdOld;
    }

    public String getSerialOld() {
        return serialOld;
    }

    public void setSerialOld(String serialOld) {
        this.serialOld = serialOld;
    }

    public String getSerialNew() {
        return serialNew;
    }

    public void setSerialNew(String serialNew) {
        this.serialNew = serialNew;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getProdOfferIdNew() {
        return prodOfferIdNew;
    }

    public void setProdOfferIdNew(Long prodOfferIdNew) {
        this.prodOfferIdNew = prodOfferIdNew;
    }

    public Long getAdjustAmount() {
        return adjustAmount;
    }

    public void setAdjustAmount(Long adjustAmount) {
        this.adjustAmount = adjustAmount;
    }

    public String getDamageGoodStatus() {
        return damageGoodStatus;
    }

    public void setDamageGoodStatus(String damageGoodStatus) {
        this.damageGoodStatus = damageGoodStatus;
    }

    public Long getChangeType() {
        return changeType;
    }

    public void setChangeType(Long changeType) {
        this.changeType = changeType;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustTel() {
        return custTel;
    }

    public void setCustTel(String custTel) {
        this.custTel = custTel;
    }

    public Long getDevStaffId() {
        return devStaffId;
    }

    public void setDevStaffId(Long devStaffId) {
        this.devStaffId = devStaffId;
    }

    public Long getReportChangeHandsetId() {
        return reportChangeHandsetId;
    }

    public void setReportChangeHandsetId(Long reportChangeHandsetId) {
        this.reportChangeHandsetId = reportChangeHandsetId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportChangeHandsetId != null ? reportChangeHandsetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReportChangeHandset)) {
            return false;
        }
        ReportChangeHandset other = (ReportChangeHandset) object;
        if ((this.reportChangeHandsetId == null && other.reportChangeHandsetId != null) || (this.reportChangeHandsetId != null && !this.reportChangeHandsetId.equals(other.reportChangeHandsetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.ReportChangeHandset[ reportChangeHandsetId=" + reportChangeHandsetId + " ]";
    }

}
