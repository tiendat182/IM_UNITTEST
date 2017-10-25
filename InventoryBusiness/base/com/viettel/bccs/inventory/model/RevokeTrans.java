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
@Table(name = "REVOKE_TRANS")
@NamedQueries({
    @NamedQuery(name = "RevokeTrans.findAll", query = "SELECT r FROM RevokeTrans r")})
public class RevokeTrans implements Serializable {
public static enum COLUMNS {ACCOUNTID, AMOUNTNEEDREVOKE, AMOUNTREVOKED, CMREQUEST, CONTRACTNO, CREATEDATE, POSID, REVOKETRANSID, SALETRANSDATE, SALETRANSID, SHOPID, STAFFID, STATUS, STOCKTRANSID, TELECOMSERVICEID, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "REVOKE_TRANS_ID")
    private Long revokeTransId;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "POS_ID")
    private Long posId;
    @Column(name = "CONTRACT_NO")
    private String contractNo;
    @Column(name = "ACCOUNT_ID")
    private String accountId;
    @Column(name = "AMOUNT_REVOKED")
    private Long amountRevoked;
    @Column(name = "AMOUNT_NEED_REVOKE")
    private Long amountNeedRevoke;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "SALE_TRANS_ID")
    private Long saleTransId;
    @Column(name = "SALE_TRANS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleTransDate;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "CM_REQUEST")
    private String cmRequest;

    public RevokeTrans() {
    }

    public RevokeTrans(Long revokeTransId) {
        this.revokeTransId = revokeTransId;
    }

    public Long getRevokeTransId() {
        return revokeTransId;
    }

    public void setRevokeTransId(Long revokeTransId) {
        this.revokeTransId = revokeTransId;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Long getAmountRevoked() {
        return amountRevoked;
    }

    public void setAmountRevoked(Long amountRevoked) {
        this.amountRevoked = amountRevoked;
    }

    public Long getAmountNeedRevoke() {
        return amountNeedRevoke;
    }

    public void setAmountNeedRevoke(Long amountNeedRevoke) {
        this.amountNeedRevoke = amountNeedRevoke;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getSaleTransId() {
        return saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public Date getSaleTransDate() {
        return saleTransDate;
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
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

    public String getCmRequest() {
        return cmRequest;
    }

    public void setCmRequest(String cmRequest) {
        this.cmRequest = cmRequest;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (revokeTransId != null ? revokeTransId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RevokeTrans)) {
            return false;
        }
        RevokeTrans other = (RevokeTrans) object;
        if ((this.revokeTransId == null && other.revokeTransId != null) || (this.revokeTransId != null && !this.revokeTransId.equals(other.revokeTransId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.RevokeTrans[ revokeTransId=" + revokeTransId + " ]";
    }
    
}
