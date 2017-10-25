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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thetm1
 */
@Entity
@Table(name = "AP_TRANS_INFO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ApTransInfo.findAll", query = "SELECT a FROM ApTransInfo a")})
public class ApTransInfo implements Serializable {
public static enum COLUMNS {CREATESHOPID, CREATESTAFFID, EXTENDLIST, FEELIST, GOODLOCKED, IPLIST, LOGDATE, POSID, PSTNLIST, QTYISSUEDECREASE, QTYSUPPLYISSUEDECREASE, REQUESTID, RESPONSE, SHOPID, STAFFID, STATUS, STOCKLIST, TOSTRING, TRANSTYPE, VERSION, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "REQUEST_ID")
    private String requestId;
    @Column(name = "GOOD_LOCKED")
    private Short goodLocked;
    @Column(name = "QTY_ISSUE_DECREASE")
    private Short qtyIssueDecrease;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "CREATE_STAFF_ID")
    private Long createStaffId;
    @Column(name = "CREATE_SHOP_ID")
    private Long createShopId;
    @Column(name = "POS_ID")
    private String posId;
    @Lob
    @Column(name = "TO_STRING")
    private String toString;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "RESPONSE")
    private String response;
    @Column(name = "LOG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logDate;
    @Column(name = "QTY_SUPPLY_ISSUE_DECREASE")
    private Short qtySupplyIssueDecrease;
    @Column(name = "FEE_LIST")
    private String feeList;
    @Column(name = "STOCK_LIST")
    private String stockList;
    @Column(name = "VERSION")
    private String version;
    @Column(name = "TRANS_TYPE")
    private Short transType;
    @Column(name = "EXTEND_LIST")
    private String extendList;
    @Column(name = "PSTN_LIST")
    private String pstnList;
    @Column(name = "IP_LIST")
    private String ipList;

    public ApTransInfo() {
    }

    public ApTransInfo(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Short getGoodLocked() {
        return goodLocked;
    }

    public void setGoodLocked(Short goodLocked) {
        this.goodLocked = goodLocked;
    }

    public Short getQtyIssueDecrease() {
        return qtyIssueDecrease;
    }

    public void setQtyIssueDecrease(Short qtyIssueDecrease) {
        this.qtyIssueDecrease = qtyIssueDecrease;
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

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Long getCreateShopId() {
        return createShopId;
    }

    public void setCreateShopId(Long createShopId) {
        this.createShopId = createShopId;
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getToString() {
        return toString;
    }

    public void setToString(String toString) {
        this.toString = toString;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public Short getQtySupplyIssueDecrease() {
        return qtySupplyIssueDecrease;
    }

    public void setQtySupplyIssueDecrease(Short qtySupplyIssueDecrease) {
        this.qtySupplyIssueDecrease = qtySupplyIssueDecrease;
    }

    public String getFeeList() {
        return feeList;
    }

    public void setFeeList(String feeList) {
        this.feeList = feeList;
    }

    public String getStockList() {
        return stockList;
    }

    public void setStockList(String stockList) {
        this.stockList = stockList;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Short getTransType() {
        return transType;
    }

    public void setTransType(Short transType) {
        this.transType = transType;
    }

    public String getExtendList() {
        return extendList;
    }

    public void setExtendList(String extendList) {
        this.extendList = extendList;
    }

    public String getPstnList() {
        return pstnList;
    }

    public void setPstnList(String pstnList) {
        this.pstnList = pstnList;
    }

    public String getIpList() {
        return ipList;
    }

    public void setIpList(String ipList) {
        this.ipList = ipList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requestId != null ? requestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApTransInfo)) {
            return false;
        }
        ApTransInfo other = (ApTransInfo) object;
        if ((this.requestId == null && other.requestId != null) || (this.requestId != null && !this.requestId.equals(other.requestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "algorithm.ApTransInfo[ requestId=" + requestId + " ]";
    }
    
}
